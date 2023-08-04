package cz.inqool.thesaurus.system.category;

import cz.inqool.thesaurus.graphql.dataloader.DataLoaderCreator;
import cz.inqool.thesaurus.graphql.dataloader.DataLoaderRegistration;
import cz.inqool.thesaurus.graphql.dto.CategoryTO;
import cz.inqool.thesaurus.system.Language;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class CategoryDataLoaderCreator implements DataLoaderCreator {

    public static final String _CHILDREN = "CATEGORY_CHILDREN_DATA_LOADER";
    public static final String _NEIGHBORS = "CATEGORY_NEIGHBORS_DATA_LOADER";
    public static final String _PARENT_NEIGHBORS = "CATEGORY_PARENT_NEIGHBORS_DATA_LOADER";
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public DataLoaderRegistration<CategoryTO> childrenRegistration() {
        return DataLoaderRegistration.<CategoryTO>builder()
                .dataLoader(DataLoader.newMappedDataLoader((ids, context) ->
                        CompletableFuture.supplyAsync(() -> {
                            Language language = (Language) context.getKeyContextsList().get(0);
                                    return categoryMapper.toDto(
                                            categoryRepository.findChildrenMap(ids, language), language);
                                }
                        )))
                .name(_CHILDREN)
                .build();
    }

    public DataLoaderRegistration<CategoryTO> neighborsRegistration() {
        return DataLoaderRegistration.<CategoryTO>builder()
                .dataLoader(DataLoader.newMappedDataLoader((ids, context) ->
                        CompletableFuture.supplyAsync(() -> {
                            Language language = (Language) context.getKeyContextsList().get(0);
                                    return categoryMapper.toDto(
                                            categoryRepository.findNeighborsMap(ids, language), language);
                                }
                        )))
                .name(_NEIGHBORS)
                .build();
    }

    public DataLoaderRegistration<CategoryTO> parentsNeighborsRegistration() {
        return DataLoaderRegistration.<CategoryTO>builder()
                .dataLoader(DataLoader.newMappedDataLoader((ids, context) ->
                        CompletableFuture.supplyAsync(() -> {
                            Language language = (Language) context.getKeyContextsList().get(0);
                                    return categoryMapper.toDto(
                                            categoryRepository.findParentNeighborsMap(ids, language), language);
                                }
                        )))
                .name(_PARENT_NEIGHBORS)
                .build();
    }

    @Override
    public Set<DataLoaderRegistration> getDataLoaderRegistrations() {
        return Set.of(
                childrenRegistration(),
                neighborsRegistration(),
                parentsNeighborsRegistration()
        );
    }
}

