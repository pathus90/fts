package fr.ub.sitis.fts.service;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public interface SearchLS {
    List<String> listTermsByPatient(int patientID) throws IOException;

    void listDocsFromBooleanQuery(List<String> keywords);

    List<String> search();
}
