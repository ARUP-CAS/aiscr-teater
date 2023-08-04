package cz.inqool.thesaurus.system.quote;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CustomQuoteRepository {
    Map<String, List<Quote>> findByContentsMap(Set<String> ids);
}
