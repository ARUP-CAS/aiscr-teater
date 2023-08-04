package cz.inqool.thesaurus.system.category;

import cz.inqool.thesaurus.system.Language;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CustomCategoryRepository {
    Map<String, List<Category>> findChildrenMap(Set<String> ids, Language language);
    Map<String, List<Category>> findNeighborsMap(Set<String> ids, Language language);
    Map<String, List<Category>> findParentNeighborsMap(Set<String> ids, Language language);

    List<Category> findChildren(String id, Language language);
    List<Category> findNeighbors(String id, Language language);
    List<Category> findParentNeighbors(String id, Language language);
    List<Category> findBreadcrumbsById(String id);
    List<Category> findBreadcrumbsByUrl(String url);
    List<Category> findRootCategories();
    List<CategoryCompleteView> findCategoriesForExport();
    CategoryExportMainView findCategoryForExport(String id);
}
