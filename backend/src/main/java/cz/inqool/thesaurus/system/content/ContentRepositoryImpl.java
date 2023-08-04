package cz.inqool.thesaurus.system.content;

import cz.inqool.thesaurus.dao.StoreImpl;
import cz.inqool.thesaurus.system.Language;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

@Transactional(readOnly = true)
public class ContentRepositoryImpl extends StoreImpl<Content, QContent> implements CustomContentRepository {

    public ContentRepositoryImpl() {
        super(Content.class, QContent.class);
    }

    @Override
    public Map<String, List<Content>> findByDescriptionAndLanguageMap(Set<String> ids, Language language) {
        Map<String, List<Content>> fetch = query()
                .where(qObject.description.id.in(ids))
                .where(qObject.language.eq(language).or(qObject.language.isNull()))
                .transform(groupBy(qObject.description.id).as(list(qObject)));
        detachAll();
        return fetch;
    }
}
