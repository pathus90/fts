package fr.ub.sitis.fts.service;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class SearchLSImpl implements SearchLS {
    private static final Logger logger = Logger.getLogger(SearchLS.class.getName());
    private static final StandardAnalyzer analyzer = new StandardAnalyzer();
    private static final String indexLocation = "C:\\Users\\VO2GROUP\\Documents\\indexSitis";

    public List<String> listTermsByPatient(int patientID) throws IOException {
        try (IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexLocation)))) {
            ArrayList<String> terms = new ArrayList<>();
            Terms termsDoc = reader.getTermVector(patientID, "pat_doc");
            TermsEnum termsEnum = termsDoc.iterator();
            logger.info("Terms for doc: " + patientID);
            while (termsEnum.next() != null) {
                String term = termsEnum.term().utf8ToString();
                logger.log(Level.INFO, term);
                terms.add(term);
            }
            return terms;
        }
    }

    public void listDocsFromBooleanQuery(List<String> keywords) {
        try (IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(indexLocation).toPath()))) {
            IndexSearcher searcher = new IndexSearcher(reader);
            BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
            for (String term : keywords) {
                queryBuilder.add(new TermQuery(new Term("pat_doc", term)), BooleanClause.Occur.SHOULD);
            }
            Query query = queryBuilder.build();
            logger.info("Query: " + query.toString());
            TopDocs topDocs = searcher.search(query, 5);
            logger.info("Number of hits: " + topDocs.totalHits);
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                Document document = searcher.doc(scoreDoc.doc);
                logger.info("Document ID: " + document.get("pat_id") + " Score: " + scoreDoc.score);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error while searching documents: " + e.getMessage(), e);
        }
    }

    public List<String> search() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            try (IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(indexLocation).toPath()))) {
                IndexSearcher searcher = new IndexSearcher(reader);
                String input = "";
                while (!input.equalsIgnoreCase("q")) {
                    System.out.println("Enter your query (q to quit):");
                    input = br.readLine();
                    if (input.equalsIgnoreCase("q")) {
                        break;
                    }
                    Query query = new QueryParser("pat_doc", analyzer).parse(input);
                    TopDocs topDocs = searcher.search(query, 10);
                    logger.info("Number of patients found: " + topDocs.totalHits);
                    for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                        Document document = searcher.doc(scoreDoc.doc);
                        logger.info("Patient ID: " + document.get("pat_id") + " Score: " + scoreDoc.score);
                        return  listTermsByPatient(scoreDoc.doc);
                    }
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "IO Exception occurred: " + e.getMessage(), e);
        } catch (org.apache.lucene.queryparser.classic.ParseException e) {
            logger.log(Level.SEVERE, "Parse Exception occurred: " + e.getMessage(), e);
        }
        return null;
    }
}
