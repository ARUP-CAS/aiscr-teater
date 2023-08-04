package cz.inqool.thesaurus.system.category;

import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.StringPath;
import cz.inqool.thesaurus.dao.StoreImpl;
import cz.inqool.thesaurus.system.Language;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

@Transactional(readOnly = true)
public class CategoryRepositoryImpl extends StoreImpl<Category, QCategory> implements CustomCategoryRepository {

    public CategoryRepositoryImpl() {
        super(Category.class, QCategory.class);
    }

    @Override
    public Map<String, List<Category>> findChildrenMap(Set<String> ids, Language language) {
        Map<String, List<Category>> fetch = query()
                .where(qObject.parent.id.in(ids))
                .orderBy(qObject.dateAccurate.castToNum(Long.class).asc(), qObject.dateFrom.castToNum(Long.class).asc(), qObject.dateTo.castToNum(Long.class).asc(), getStringPathFromLanguage(language).lower().asc())
                .transform(groupBy(qObject.parent.id).as(list(qObject)));
        detachAll();
        return fetch;
    }

    @Override
    public Map<String, List<Category>> findNeighborsMap(Set<String> ids, Language language) {
        Map<String, List<Category>> fetch = query()
                .where(qObject.parent.id.in(
                        query()
                                .select(qObject.parent.id)
                                .where(qObject.id.in(ids))
                                .fetch()))
                .orderBy(qObject.dateAccurate.castToNum(Long.class).asc(), qObject.dateFrom.castToNum(Long.class).asc(), qObject.dateTo.castToNum(Long.class).asc(), getStringPathFromLanguage(language).lower().asc())
                .transform(groupBy(qObject.parent.id).as(list(qObject)));
        detachAll();
        return fetch;
    }

    @Override
    public Map<String, List<Category>> findParentNeighborsMap(Set<String> ids, Language language) {
        Map<String, List<Category>> fetch = query()
                .where(qObject.parent.id.in(
                        query()
                                .select(qObject.parent.parent.id)
                                .where(qObject.id.in(ids))
                                .fetch()))
                .orderBy(qObject.dateAccurate.castToNum(Long.class).asc(), qObject.dateFrom.castToNum(Long.class).asc(), qObject.dateTo.castToNum(Long.class).asc(), getStringPathFromLanguage(language).lower().asc())
                .transform(groupBy(qObject.parent.id).as(list(qObject)));
        detachAll();
        return fetch;
    }

    @Override
    public List<Category> findChildren(String id, Language language) {
        List<Category> fetch = query()
                .select(qObject)
                .where(qObject.parent.id.eq(id))
                .orderBy(qObject.dateAccurate.castToNum(Long.class).asc(), qObject.dateFrom.castToNum(Long.class).asc(), qObject.dateTo.castToNum(Long.class).asc(), getStringPathFromLanguage(language).lower().asc())
                .fetch();
        detachAll();
        return fetch;
    }

    @Override
    public List<Category> findNeighbors(String id, Language language) {
        Category category = find(id);
        if (category.getParent() == null) {
            return findRootCategories();
        }
        List<Category> fetch = query()
                .select(qObject)
                .where(qObject.parent.eq(category.getParent()))
                .orderBy(qObject.dateAccurate.castToNum(Long.class).asc(), qObject.dateFrom.castToNum(Long.class).asc(), qObject.dateTo.castToNum(Long.class).asc(), getStringPathFromLanguage(language).lower().asc())
                .fetch();
        detachAll();
        return fetch;
    }

    @Override
    public List<Category> findParentNeighbors(String id, Language language) {
        Category category = find(id);
        if (category.getParent() != null
                && category.getParent().getParent() == null) {
            return findRootCategories();
        }
        List<Category> fetch = query()
                .select(qObject)
                .where(qObject.parent.eq(query()
                        .select(qObject.parent.parent)
                        .where(qObject.id.eq(id))))
                .orderBy(qObject.dateAccurate.castToNum(Long.class).asc(), qObject.dateFrom.castToNum(Long.class).asc(), qObject.dateTo.castToNum(Long.class).asc(), getStringPathFromLanguage(language).lower().asc())
                .fetch();
        detachAll();
        return fetch;
    }

    @Override
    public List<Category> findBreadcrumbsById(String id) {
        Category fetch = query()
                .select(qObject)
                .where(qObject.id.eq(id))
                .fetchOne();
        detachAll();
        return extractBreadcrumbs(fetch);
    }

    @Override
    public List<Category> findBreadcrumbsByUrl(String url) {
        Category fetch = query()
                .select(qObject)
                .where(qObject.url.eq(url))
                .fetchOne();
        detachAll();
        return extractBreadcrumbs(fetch);
    }

    @NotNull
    private List<Category> extractBreadcrumbs(@NonNull Category fetch) {
        List<Category> breadcrumbs = new ArrayList<>();
        breadcrumbs.add(fetch);
        while (fetch.getParent() != null) {
            fetch = fetch.getParent();
            breadcrumbs.add(fetch);
        }
        return breadcrumbs;
    }

    @Override
    public List<Category> findRootCategories() {
        List<Category> fetch = query()
                .select(qObject)
                .where(qObject.parent.isNull())
                .fetch();
        detachAll();
        return fetch;
    }

    @Override
    public List<CategoryCompleteView> findCategoriesForExport() {
        QCategoryCompleteView completeView = QCategoryCompleteView.categoryCompleteView;
        List<CategoryCompleteView> fetch = queryFactory.from(completeView)
                .select(completeView)
                .where(completeView.parent.isNull())
                .fetch();
        detachAll();
        return fetch;
    }

    @Override
    public CategoryExportMainView findCategoryForExport(String id) {
        QCategoryExportMainView exportMainView = QCategoryExportMainView.categoryExportMainView;
        CategoryExportMainView fetch = queryFactory.from(exportMainView)
                .select(exportMainView)
                .where(exportMainView.id.eq(id))
                .fetchOne();
        detachAll();
        return fetch;
    }

    private StringPath getStringPathFromLanguage(Language language) {
        return new PathBuilder<>(qObject.getType(), qObject.getMetadata().getName()).getString("name." + language.getLocale().getLanguage());
    }
}
