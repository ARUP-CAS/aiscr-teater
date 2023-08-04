package cz.inqool.thesaurus.graphql.dataloader;

import lombok.Builder;
import lombok.Getter;
import org.dataloader.DataLoader;

import java.util.List;


@Getter
@Builder
public class DataLoaderRegistration<T> {
    private final String name;
    private final DataLoader<String, List<T>> dataLoader;
}
