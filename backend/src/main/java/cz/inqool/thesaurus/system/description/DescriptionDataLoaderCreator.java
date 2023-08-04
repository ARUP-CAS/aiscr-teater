package cz.inqool.thesaurus.system.description;

import cz.inqool.thesaurus.graphql.dataloader.DataLoaderCreator;
import cz.inqool.thesaurus.graphql.dataloader.DataLoaderRegistration;
import cz.inqool.thesaurus.graphql.dto.DescriptionTO;
import cz.inqool.thesaurus.system.Language;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class DescriptionDataLoaderCreator implements DataLoaderCreator {

    public static final String CATEGORY = "CATEGORY_DESCRIPTIONS_DATA_LOADER";
    private final DescriptionRepository descriptionRepository;
    private final DescriptionMapper descriptionMapper;

    public DataLoaderRegistration<DescriptionTO> categoryRegistration() {
        return DataLoaderRegistration.<DescriptionTO>builder()
                .dataLoader(DataLoader.newMappedDataLoader((ids, context) ->
                        CompletableFuture.supplyAsync(() -> descriptionMapper.toDto(
                                descriptionRepository.findByCategoriesMap(ids), (Language) context.getKeyContextsList().get(0))
                        )))
                .name(CATEGORY)
                .build();
    }

    @Override
    public Set<DataLoaderRegistration> getDataLoaderRegistrations() {
        return Set.of(
                categoryRegistration()
        );
    }
}
