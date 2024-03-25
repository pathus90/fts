package fr.ub.sitis.fts.service;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@Component
public class LuceneIndexer {
    private final IndexWriter writer;

    @Value("${index.path:C:\\Users\\VO2GROUP\\Documents\\indexSitis}")
    private String indexDir;

    public LuceneIndexer() throws IOException {
        FSDirectory dir = FSDirectory.open(Paths.get("C:\\Users\\VO2GROUP\\Documents\\indexSitis"));

        StandardAnalyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        writer = new IndexWriter(dir, config);
    }

    /**
     * Indexes the documents retrieved from the database.
     *
     * @param documents The list of documents to be indexed.
     * @throws IOException If an I/O error occurs.
     */
    public void indexDocumentsFromDatabase(List<Document> documents) throws IOException {
        for (Document document : documents) {
            writer.addDocument(document);
        }
    }

    public void closeIndex() throws IOException {
        writer.close();
    }
}
