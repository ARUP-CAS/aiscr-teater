package cz.inqool.thesaurus.system.description;

import cz.inqool.thesaurus.dao.StoreImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

@Transactional(readOnly = true)
public class DescriptionRepositoryImpl extends StoreImpl<Description, QDescription> implements CustomDescriptionRepository {

    public DescriptionRepositoryImpl() {
        super(Description.class, QDescription.class);
    }

    @Override
    public Map<String, List<Description>> findByCategoriesMap(Set<String> ids) {
        Map<String, List<Description>> fetch = query()
                .where(qObject.category.id.in(ids))
                .transform(groupBy(qObject.category.id).as(list(qObject)));
        detachAll();
        return fetch;
    }
}
