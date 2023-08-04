package cz.inqool.thesaurus.system.description;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CustomDescriptionRepository {
    Map<String, List<Description>> findByCategoriesMap(Set<String> ids);
}
