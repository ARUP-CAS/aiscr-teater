package cz.inqool.thesaurus;

import com.google.gson.Gson;
import graphql.schema.DataFetcher;
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
import org.elasticsearch.search.suggest.completion.CompletionSuggestion.Entry;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion.Entry.Option;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class GraphQLDataFetchers {

    private final CategoryRepository categoryRepository;
    private final EntityManager entityManager;
    private final RestHighLevelClient client;
    private final Gson gson = new Gson();
    private String placeholderType;

    @Inject
    public GraphQLDataFetchers(CategoryRepository categoryRepository, EntityManager entityManager, RestHighLevelClient client) {
        this.categoryRepository = categoryRepository;
        this.entityManager = entityManager;
        this.client = client;
    }


    //Methods for single category queries


    public DataFetcher getSingleCategoryWithIdDataFetcher() {
        return dataFetchingEnvironment -> categoryRepository.findById(dataFetchingEnvironment.getArgument("id"))
                .orElse(null);
    }

    public DataFetcher getSingleCategoryWithUrlDataFetcher() {
        return dataFetchingEnvironment -> categoryRepository.findByUrl(dataFetchingEnvironment.getArgument("url"))
                .orElse(null);
    }

    public DataFetcher getChildren() {
        return dataFetchingEnvironment -> {
            Category category = dataFetchingEnvironment.getSource();
            return getChildCategoriesWithId(category.getId());
        };
    }

    public DataFetcher getNeighbors() {
        return dataFetchingEnvironment -> {
            Category category = dataFetchingEnvironment.getSource();
            if (category.getParent() != null) {
                return getChildCategoriesWithId(category.getParent().getId());
            }
            return getRootCategories();
        };
    }

    public DataFetcher getParentsNeighbors() {
        return dataFetchingEnvironment -> {
            Category category = dataFetchingEnvironment.getSource();
            if (category.getParent() != null) {
                if (category.getParent().getParent() != null) {
                    return getChildCategoriesWithId(category.getParent().getParent().getId());
                }
                return getRootCategories();
            }
            return Collections.emptyList();
        };
    }

    private List<CategoryBasic> getChildCategoriesWithId(String id) {
        Query query = entityManager.createNativeQuery(
                "WITH RECURSIVE parents AS (" +
                        "SELECT" +
                        "   id," +
                        "   category_type," +
                        "   name," +
                        "   url," +
                        "   parent_id," +
                        "   is_leaf, " +
                        "   date_accurate," +
                        "   date_from," +
                        "   date_to " +
                        "FROM" +
                        "   category " +
                        "WHERE" +
                        "   id = :selected_category " +
                        "UNION " +
                        "SELECT" +
                        "   ch.id," +
                        "   ch.category_type," +
                        "   ch.name," +
                        "   ch.url," +
                        "   ch.parent_id," +
                        "   ch.is_leaf, " +
                        "   ch.date_accurate," +
                        "   ch.date_from," +
                        "   ch.date_to " +
                        "FROM" +
                        "   category ch " +
                        "INNER JOIN parents p ON p.id = ch.parent_id AND " +
                        "(p.category_type = :placeholder_type or p.id = :selected_category)" +
                        ") " +
                        "SELECT * " +
                        "FROM parents " +
                        "WHERE id != :selected_category " +
                        "ORDER BY date_accurate ASC, date_from ASC, date_to ASC, name ASC;")
                .setParameter("placeholder_type", placeholderType)
                .setParameter("selected_category", id);
        List<Object[]> result = query.getResultList();
        return mapCategoriesBasic(result);
    }


    //Methods for breadcrumb navigation queries


    public DataFetcher getCategoryBreadcrumbsWithIdDataFetcher() {
        return dataFetchingEnvironment -> {
            Query query = entityManager.createNativeQuery(
                    "WITH RECURSIVE parents AS (" +
                            "SELECT" +
                            "   id," +
                            "   category_type," +
                            "   name," +
                            "   url," +
                            "   parent_id, " +
                            "   is_leaf " +
                            "FROM" +
                            "   category " +
                            "WHERE" +
                            "   id = :selected_category " +
                            "UNION " +
                            "SELECT" +
                            "   ch.id," +
                            "   ch.category_type," +
                            "   ch.name," +
                            "   ch.url," +
                            "   ch.parent_id, " +
                            "   ch.is_leaf " +
                            "FROM" +
                            "   category ch " +
                            "INNER JOIN parents p ON p.parent_id = ch.id" +
                            ") " +
                            "SELECT * " +
                            "FROM parents;")
                    .setParameter("selected_category", dataFetchingEnvironment.getArgument("id"));
            List<Object[]> result = query.getResultList();
            return mapCategoriesBasic(result);
        };
    }

    public DataFetcher getCategoryBreadcrumbsWithUrlDataFetcher() {
        return dataFetchingEnvironment -> {
            Query query = entityManager.createNativeQuery(
                    "WITH RECURSIVE parents AS (" +
                            "SELECT" +
                            "   id," +
                            "   category_type," +
                            "   name," +
                            "   url," +
                            "   parent_id, " +
                            "   is_leaf " +
                            "FROM" +
                            "   category " +
                            "WHERE" +
                            "   url = :selected_category " +
                            "UNION " +
                            "SELECT" +
                            "   ch.id," +
                            "   ch.category_type," +
                            "   ch.name," +
                            "   ch.url," +
                            "   ch.parent_id, " +
                            "   ch.is_leaf " +
                            "FROM" +
                            "   category ch " +
                            "INNER JOIN parents p ON p.parent_id = ch.id" +
                            ") " +
                            "SELECT * " +
                            "FROM parents;")
                    .setParameter("selected_category", dataFetchingEnvironment.getArgument("url"));
            List<Object[]> result = query.getResultList();
            return mapCategoriesBasic(result);
        };
    }


    //Method for root category query.


    public DataFetcher getListRootCategoriesDataFetcher() {
        return dataFetchingEnvironment -> getRootCategories();
    }

    private List<CategoryBasic> getRootCategories() {
        Query query = entityManager.createNativeQuery(
                "WITH RECURSIVE parents AS (" +
                        "SELECT" +
                        "   id," +
                        "   category_type," +
                        "   name," +
                        "   url," +
                        "   parent_id, " +
                        "   is_leaf " +
                        "FROM" +
                        "   category " +
                        "WHERE" +
                        "   parent_id IS NULL " +
                        "UNION " +
                        "SELECT" +
                        "   ch.id," +
                        "   ch.category_type," +
                        "   ch.name," +
                        "   ch.url," +
                        "   ch.parent_id, " +
                        "   ch.is_leaf " +
                        "FROM" +
                        "   category ch " +
                        "INNER JOIN parents p ON p.id = ch.parent_id " +
                        "AND (p.category_type = :placeholder_type)" +
                        ") " +
                        "SELECT * " +
                        "FROM parents " +
                        "ORDER BY CAST(SPLIT_PART(url, '-', 1) AS INTEGER);")
                .setParameter("placeholder_type", placeholderType);
        List<Object[]> result = query.getResultList();
        return mapCategoriesBasic(result);
    }

    private static List<CategoryBasic> mapCategoriesBasic(List<Object[]> list) {
        return list.stream()
                .map(c -> new CategoryBasic()
                        .setId((String) c[0])
                        .setCategoryType(CategoryType.valueOf((String) c[1]))
                        .setName((String) c[2])
                        .setUrl((String) c[3])
                        .setLeaf((boolean) c[5]))
                .collect(Collectors.toList());
    }


    //Methods for search query.


    public DataFetcher getSearchDataFetcher() {
        return dataFetchingEnvironment -> {
            String expression = dataFetchingEnvironment.getArgument("value");
            int limit = dataFetchingEnvironment.containsArgument("limit") ?
                    dataFetchingEnvironment.getArgument("limit") : 1000;
            return executeSearchRequest(buildSearchRequest(expression))
                    .stream()
                    .limit(limit)
                    .collect(Collectors.toList());
        };
    }

    private SearchRequest buildSearchRequest(String expression) {
        SearchRequest searchRequest = new SearchRequest("category");
        HighlightBuilder highlightBuilder = new HighlightBuilder()
                .field(new HighlightBuilder.Field("name"))
                .field(new HighlightBuilder.Field("description"));

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
//  Possible workaround to achieve fuzzy querying without explicit "~" in the query (not tested for side effects):
//  QueryBuilders.queryStringQuery(expression + "~")
                .query(QueryBuilders.queryStringQuery(expression)
                        .field("name", 1f)
                        .field("description", 0.5f)
                        .analyzer("my_analyzer")
                        .fuzziness(Fuzziness.AUTO))
                .highlighter(highlightBuilder);
        SuggestionBuilder suggestionBuilder =
                SuggestBuilders.completionSuggestion("name_suggest")
                        .analyzer("my_analyzer")
                        .prefix(expression)
                        .size(50);

        SuggestBuilder suggestBuilder = new SuggestBuilder().addSuggestion("suggest_name", suggestionBuilder);
        searchSourceBuilder.suggest(suggestBuilder);
        searchRequest.source(searchSourceBuilder);
        return searchRequest;
    }

    private Set<SearchResultItem> executeSearchRequest(SearchRequest searchRequest) {
        SearchResponse response = null;
        try {
            response = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("Exception while performing search", e);
        }
        Set<SearchResultItem> results = new LinkedHashSet<>();
        if (response != null) {
            results.addAll(getQueryStringQueryNameResults(response));
            results.addAll(getSuggestedNameResults(response));
            results.addAll(getQueryStringQueryMetadataResults(response));
        }
        return results;
    }

    private List<SearchResultItem> getSuggestedNameResults(SearchResponse response) {
        List<SearchResultItem> results = new ArrayList<>();
        Suggest suggest = response.getSuggest();
        CompletionSuggestion completionSuggestion = suggest != null ? suggest.getSuggestion("suggest_name") : null;
        if (completionSuggestion != null) {
            for (Entry entry : completionSuggestion.getEntries()) {
                for (Option option : entry) {
                    SearchResultItem resultItem = (gson.fromJson(option.getHit().getSourceAsString(),
                            SearchResultItem.class).setSearchType(SearchType.NAME));
                    addIfNotBlank(results, resultItem);
                }
            }
        }
        return results;
    }

    private List<SearchResultItem> getQueryStringQueryMetadataResults(SearchResponse response) {
        List<SearchResultItem> results = new ArrayList<>();
        response.getHits()
                .forEach(hit -> {
                    SearchResultItem resultItem = gson.fromJson(hit.getSourceAsString(), SearchResultItem.class);
                    if (hit.getHighlightFields().containsKey("description")) {
                        resultItem.setSearchType(SearchType.METADATA);
                        addIfNotBlank(results, resultItem);
                    }
                });
        return results;
    }

    private List<SearchResultItem> getQueryStringQueryNameResults(SearchResponse response) {
        List<SearchResultItem> results = new ArrayList<>();
        response.getHits()
                .forEach(hit -> {
                    SearchResultItem resultItem = gson.fromJson(hit.getSourceAsString(), SearchResultItem.class);
                    if (hit.getHighlightFields().containsKey("name")) {
                        resultItem.setSearchType(SearchType.NAME);
                        addIfNotBlank(results, resultItem);
                    }
                });
        return results;
    }

    private static List<SearchResultItem> addIfNotBlank(List<SearchResultItem> list, SearchResultItem item) {
        if (item.getCategoryType() != CategoryType.BLANK) {
            list.add(item);
        }
        return list;
    }

    @Inject
    public void setPlaceholderType(@Value("${placeholder_type}") String placeholderType) {
        this.placeholderType = placeholderType;
    }
}