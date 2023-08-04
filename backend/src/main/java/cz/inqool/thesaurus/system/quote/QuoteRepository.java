package cz.inqool.thesaurus.system.quote;

import cz.inqool.thesaurus.dao.Store;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuoteRepository extends Store<Quote, QQuote>, CustomQuoteRepository {
    List<Quote> findAllByContentId(String contentId);
}
