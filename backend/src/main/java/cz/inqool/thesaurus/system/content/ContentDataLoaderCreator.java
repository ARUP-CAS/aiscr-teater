package cz.inqool.thesaurus.system.content;

import cz.inqool.thesaurus.graphql.dataloader.DataLoaderCreator;
import cz.inqool.thesaurus.graphql.dataloader.DataLoaderRegistration;
import cz.inqool.thesaurus.graphql.dto.ContentTO;
import cz.inqool.thesaurus.system.Language;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class ContentDataLoaderCreator implements DataLoaderCreator {

    public static final String DESCRIPTION = "DESCRIPTION_CONTENTS_DATA_LOADER";
    private final ContentRepository contentRepository;
    private final ContentMapper contentMapper;

    public DataLoaderRegistration<ContentTO> descriptionRegistration() {
        return DataLoaderRegistration.<ContentTO>builder()
                .dataLoader(DataLoader.newMappedDataLoader((ids, context) ->
                        CompletableFuture.supplyAsync(() -> {
                            Language language = (Language) context.getKeyContextsList().get(0);
                                    return contentMapper.toDto(
                                            contentRepository.findByDescriptionAndLanguageMap(ids, language), language);
                                }
                        )))
                .name(DESCRIPTION)
                .build();
    }

    @Override
    public Set<DataLoaderRegistration> getDataLoaderRegistrations() {
        return Set.of(
                descriptionRegistration()
        );
    }
}
