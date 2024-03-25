package fr.ub.sitis.fts.service;

import org.apache.lucene.document.Document;

import java.util.List;

public interface DocumentService {
    List<Document> queryDocPatient();

    List<Document> queryDocPatientWithTerms();
}
