package fr.ub.sitis.fts.service;

import fr.ub.sitis.fts.entity.Patient;
import fr.ub.sitis.fts.repository.PatientRepository;
import lombok.RequiredArgsConstructor;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.Field;

import org.apache.lucene.index.IndexOptions;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final PatientRepository patientRepository;

    @Override
    public List<Document> queryDocPatient() {
        return patientRepository.findAll().stream().map(this::createDocument).collect(Collectors.toList());
    }

    @Override
    public List<Document> queryDocPatientWithTerms() {
        return patientRepository.findAll().stream().map(this::createDocumentWithTerms).collect(Collectors.toList());
    }

    private Document createDocumentWithTerms(Patient patient) {
        FieldType fieldType = new FieldType(TextField.TYPE_NOT_STORED);
        fieldType.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
        fieldType.setStoreTermVectors(true);
        fieldType.setStoreTermVectorOffsets(true);
        fieldType.setStoreTermVectorPayloads(true);
        fieldType.setStoreTermVectorPositions(true);
        fieldType.setTokenized(true);

        Document document = new Document();
        document.add(new StringField("pat_id", patient.getId(), Field.Store.YES));
        document.add(new Field("pat_doc", patient.getDocument(), fieldType));
        return document;
    }

    private Document createDocument(Patient patient) {
        Document document = new Document();
        document.add(new StringField("pat_id", patient.getId(), Field.Store.YES));
        document.add(new TextField("pat_doc", patient.getDocument(), Field.Store.YES));
        return document;
    }
}