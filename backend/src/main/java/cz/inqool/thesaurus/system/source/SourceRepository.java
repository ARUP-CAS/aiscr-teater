package cz.inqool.thesaurus.system.source;

import cz.inqool.thesaurus.dao.Store;
import org.springframework.stereotype.Repository;

@Repository
public interface SourceRepository extends Store<Source, QSource> {
}
