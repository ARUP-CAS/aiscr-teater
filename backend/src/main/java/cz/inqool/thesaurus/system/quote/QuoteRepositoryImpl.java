package cz.inqool.thesaurus.system.quote;

import cz.inqool.thesaurus.dao.StoreImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

@Transactional(readOnly = true)
public class QuoteRepositoryImpl extends StoreImpl<Quote, QQuote> implements CustomQuoteRepository {

    public QuoteRepositoryImpl() {
        super(Quote.class, QQuote.class);
    }

    @Override
    public Map<String, List<Quote>> findByContentsMap(Set<String> ids) {
        Map<String, List<Quote>> fetch = query()
                .where(qObject.content.id.in(ids))
                .transform(groupBy(qObject.content.id).as(list(qObject)));
        detachAll();
        return fetch;
    }
}
