package fr.ub.sitis.fts;

import fr.ub.sitis.fts.service.DocumentService;
import fr.ub.sitis.fts.service.DocumentServiceImpl;
import fr.ub.sitis.fts.service.LuceneIndexer;
import fr.ub.sitis.fts.service.SearchLS;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

@SpringBootApplication
public class FtsApplication {

	public static void main(String[] args) {
		SpringApplication.run(FtsApplication.class, args);
	}

	@Bean
	public CommandLineRunner initializeData(LuceneIndexer ind, DocumentService documentService) {
		return args -> {
			// Retrieve data at application startup and perform any necessary initialization

			ind.indexDocumentsFromDatabase(documentService.queryDocPatientWithTerms());


			List<String> mots = new ArrayList<>();
			mots.add("diabete");
			mots.add("principal");

			ind.closeIndex();
		};
	}

}
