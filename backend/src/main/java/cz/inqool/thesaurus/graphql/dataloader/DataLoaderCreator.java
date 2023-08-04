package cz.inqool.thesaurus.graphql.dataloader;

import java.util.Set;

public interface DataLoaderCreator {
    Set<DataLoaderRegistration> getDataLoaderRegistrations();
}
