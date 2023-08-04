package cz.inqool.thesaurus.system.search;

import cz.inqool.thesaurus.graphql.dto.SearchItemTO;
import cz.inqool.thesaurus.graphql.dto.SearchTypeTO;
import cz.inqool.thesaurus.system.Language;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.SuggestionBuilder;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class ElasticsearchService {

    private static final String INDEX_NAME = "category";
    private static final String METADATA_FIELD_NAME = "metadata";
    private final RestHighLevelClient client;
    private final SearchItemMapper searchItemMapper;


    public List<SearchItemTO> search(String query, int limit, Language language) {
        if (limit <= 0) {
            limit = 10;
        }
        final String localizedNameField = getLocalizedNameField(language);
        return executeSearchRequest(buildSearchRequest(query), localizedNameField)
                .stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

    private SearchRequest buildSearchRequest(String expression) {
        SearchRequest searchRequest = new SearchRequest(INDEX_NAME);
        HighlightBuilder highlightBuilder = new HighlightBuilder()
                .field(new HighlightBuilder.Field("name_cs"))
                .field(new HighlightBuilder.Field("name_en"))
                .field(new HighlightBuilder.Field("name_de"))
                .field(new HighlightBuilder.Field(METADATA_FIELD_NAME));

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
//  Possible workaround to achieve fuzzy querying without explicit "~" in the query (not tested for side effects):
//  QueryBuilders.queryStringQuery(expression + "~")
                .query(QueryBuilders.queryStringQuery(expression)
                        .field("name_cs", 1f)
                        .field("name_en", 1f)
                        .field("name_de", 1f)
                        .field(METADATA_FIELD_NAME, 0.5f)
                        .analyzer("my_analyzer")
                        .fuzziness(Fuzziness.AUTO))
                .highlighter(highlightBuilder);
        SuggestionBuilder<CompletionSuggestionBuilder> suggestionBuilderCs =
                SuggestBuilders.completionSuggestion("name_cs_suggest")
                        .analyzer("my_analyzer")
                        .prefix(expression)
                        .size(50);
        SuggestionBuilder<CompletionSuggestionBuilder> suggestionBuilderEn =
                SuggestBuilders.completionSuggestion("name_en_suggest")
                        .analyzer("my_analyzer")
                        .prefix(expression)
                        .size(50);
        SuggestionBuilder<CompletionSuggestionBuilder> suggestionBuilderDe =
                SuggestBuilders.completionSuggestion("name_de_suggest")
                        .analyzer("my_analyzer")
                        .prefix(expression)
                        .size(50);

        SuggestBuilder suggestBuilder = new SuggestBuilder()
                .addSuggestion("name_cs_suggest", suggestionBuilderCs)
                .addSuggestion("name_en_suggest", suggestionBuilderEn)
                .addSuggestion("name_de_suggest", suggestionBuilderDe);
        searchSourceBuilder.suggest(suggestBuilder);
        searchRequest.source(searchSourceBuilder);
        return searchRequest;
    }

    private Set<SearchItemTO> executeSearchRequest(SearchRequest searchRequest, String localizedNameField) {
        SearchResponse response = null;
        try {
            response = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("Exception while performing search", e);
        }
        Set<SearchItemTO> results = new LinkedHashSet<>();
        if (response != null) {
            results.addAll(getQueryStringQueryNameResults(response, localizedNameField));
            results.addAll(getSuggestedNameResults(response, localizedNameField));
            results.addAll(getQueryStringQueryMetadataResults(response, localizedNameField));
        }
        return results;
    }

    private List<SearchItemTO> getQueryStringQueryNameResults(SearchResponse response, String localizedNameField) {
        List<SearchItemTO> results = new ArrayList<>();
        response.getHits()
                .forEach(hit -> {
                    if (hit.getHighlightFields().containsKey("name_cs") ||
                            hit.getHighlightFields().containsKey("name_en") ||
                            hit.getHighlightFields().containsKey("name_de")) {
                        SearchItemTO resultItem = searchItemMapper.toDto(hit.getSourceAsMap(), localizedNameField);
                        resultItem.setSearchType(SearchTypeTO.NAME);
                        results.add(resultItem);
                    }
                });
        return results;
    }

    private List<SearchItemTO> getSuggestedNameResults(SearchResponse response, String localizedNameField) {
        List<SearchItemTO> results = new ArrayList<>();
        Suggest suggest = response.getSuggest();
        if (suggest != null) {
            List<CompletionSuggestion.Entry.Option> options = Stream.concat(
                            Stream.concat(((CompletionSuggestion) suggest.getSuggestion("name_cs_suggest")).getEntries().stream()
                                            .flatMap(entry -> entry.getOptions().stream()),
                                    ((CompletionSuggestion) suggest.getSuggestion("name_en_suggest")).getEntries().stream()
                                            .flatMap(entry -> entry.getOptions().stream())),
                            ((CompletionSuggestion) suggest.getSuggestion("name_de_suggest")).getEntries().stream()
                                    .flatMap(entry -> entry.getOptions().stream()))
                    .collect(Collectors.toList());
            options.forEach(option -> {
                SearchItemTO resultItem = searchItemMapper.toDto(option.getHit().getSourceAsMap(), localizedNameField);
                resultItem.setSearchType(SearchTypeTO.NAME);
                results.add(resultItem);
            });
        }
        return results;
    }

    private List<SearchItemTO> getQueryStringQueryMetadataResults(SearchResponse response, String localizedNameField) {
        List<SearchItemTO> results = new ArrayList<>();
        response.getHits()
                .forEach(hit -> {
                    if (hit.getHighlightFields().containsKey("metadata")) {
                        SearchItemTO resultItem = searchItemMapper.toDto(hit.getSourceAsMap(), localizedNameField);
                        resultItem.setSearchType(SearchTypeTO.METADATA);
                        results.add(resultItem);
                    }
                });
        return results;
    }

    private String getLocalizedNameField(Language language) {
        if (language == Language.EN) return "name_en";
        if (language == Language.DE) return "name_de";
        return "name_cs";
    }
}
