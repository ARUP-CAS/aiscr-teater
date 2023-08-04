package cz.inqool.thesaurus.graphql.dataloader;

import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoaderRegistry;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoaderRegistryFactory {

    private final List<DataLoaderCreator> dataLoaderCreators;

    public DataLoaderRegistry create() {
        var registry = new DataLoaderRegistry();

        dataLoaderCreators.forEach(dataLoader ->
                dataLoader.getDataLoaderRegistrations().forEach(registration ->
                        registry.register(registration.getName(), registration.getDataLoader())));

        return registry;
    }

}
