package fr.ub.sitis.fts.controller;

import fr.ub.sitis.fts.service.SearchLS;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class SearchController {

    private final SearchLS searchLS;

    @PostMapping("/search")
    public List<String> search(@RequestBody List<String> words) {
        searchLS.listDocsFromBooleanQuery(words);
        searchLS.search();

        return new ArrayList<>();
    }
}