package cz.inqool.thesaurus.system.history;

import cz.inqool.thesaurus.dao.Store;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryEventRepository extends Store<HistoryEvent, QHistoryEvent> {
    HistoryEvent findByLabel(String label);
}
