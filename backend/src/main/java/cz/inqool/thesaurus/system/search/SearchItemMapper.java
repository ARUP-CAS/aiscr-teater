package cz.inqool.thesaurus.system.search;

import cz.inqool.thesaurus.graphql.dto.SearchItemTO;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SearchItemMapper {

    public SearchItemTO toDto(Map<String, Object> elasticResult, String localizedNameField) {
        return SearchItemTO.builder()
                .setId((String) elasticResult.get("id"))
                .setName((String) elasticResult.get(localizedNameField))
                .setUrl((String) elasticResult.get("url"))
                .build();
    }
}
