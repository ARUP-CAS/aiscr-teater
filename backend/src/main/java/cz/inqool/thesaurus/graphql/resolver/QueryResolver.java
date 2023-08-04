package cz.inqool.thesaurus.graphql.resolver;

import cz.inqool.thesaurus.graphql.dto.CategoryTO;
import cz.inqool.thesaurus.graphql.dto.SearchItemTO;
import cz.inqool.thesaurus.graphql.dto.SourceTO;
import cz.inqool.thesaurus.system.Language;
import cz.inqool.thesaurus.system.category.CategoryMapper;
import cz.inqool.thesaurus.system.category.CategoryRepository;
import cz.inqool.thesaurus.system.category.QCategory;
import cz.inqool.thesaurus.system.category.QCategoryCompleteView;
import cz.inqool.thesaurus.system.history.HistoryEventRepository;
import cz.inqool.thesaurus.system.search.ElasticsearchService;
import cz.inqool.thesaurus.system.source.SourceMapper;
import cz.inqool.thesaurus.system.source.SourceRepository;
import graphql.execution.DataFetcherResult;
import graphql.kickstart.tools.GraphQLQueryResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.time.ZonedDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class QueryResolver implements GraphQLQueryResolver {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final SourceRepository sourceRepository;
    private final SourceMapper sourceMapper;
    private final HistoryEventRepository historyEventRepository;
    private final ElasticsearchService elasticsearchService;

    public DataFetcherResult<CategoryTO> singleCategoryWithId(String id, Language language) {
        return DataFetcherResult.<CategoryTO>newResult()
                .data(categoryMapper.toDto(categoryRepository.findById(id).orElseThrow(), language))
                .localContext(language)
                .build();
    }

    public DataFetcherResult<CategoryTO> singleCategoryWithUrl(String url, Language language) {
        return DataFetcherResult.<CategoryTO>newResult()
                .data(categoryMapper.toDto(categoryRepository.findByUrl(url).orElseThrow(), language))
                .localContext(language)
                .build();
    }

    public DataFetcherResult<List<CategoryTO>> categoryBreadcrumbsWithId(String id, Language language) {
        return DataFetcherResult.<List<CategoryTO>>newResult()
                .data(categoryMapper.toDto(categoryRepository.findBreadcrumbsById(id), language))
                .localContext(language)
                .build();
    }

    public DataFetcherResult<List<CategoryTO>> categoryBreadcrumbsWithUrl(String url, Language language) {
        return DataFetcherResult.<List<CategoryTO>>newResult()
                .data(categoryMapper.toDto(categoryRepository.findBreadcrumbsByUrl(url), language))
                .localContext(language)
                .build();
    }

    public DataFetcherResult<List<CategoryTO>> listRootCategories(Language language) {
        return DataFetcherResult.<List<CategoryTO>>newResult()
                .data(categoryMapper.toDto(categoryRepository.findRootCategories(), language))
                .localContext(language)
                .build();
    }

    public DataFetcherResult<List<SourceTO>> listSources() {
        return DataFetcherResult.<List<SourceTO>>newResult()
                .data(sourceMapper.toDto(sourceRepository.findAll()))
                .build();
    }

    public DataFetcherResult<List<SearchItemTO>> search(String query, int limit, Language language) {
        return DataFetcherResult.<List<SearchItemTO>>newResult()
                .data(elasticsearchService.search(query,limit, language))
                .build();
    }

    public ZonedDateTime lastImport() {
        return historyEventRepository.findByLabel("last_import_date").getTimestamp();
    }

    public String exportAll() {
        return "http://localhost:8080/api/export";
    }
}
