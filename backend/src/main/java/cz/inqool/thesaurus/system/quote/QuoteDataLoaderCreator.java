package cz.inqool.thesaurus.system.quote;

import cz.inqool.thesaurus.graphql.dataloader.DataLoaderCreator;
import cz.inqool.thesaurus.graphql.dataloader.DataLoaderRegistration;
import cz.inqool.thesaurus.graphql.dto.QuoteTO;
import cz.inqool.thesaurus.system.Language;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class QuoteDataLoaderCreator implements DataLoaderCreator {

    public static final String CONTENT = "CONTENT_QUOTES_DATA_LOADER";
    private final QuoteRepository quoteRepository;
    private final QuoteMapper quoteMapper;

    public DataLoaderRegistration<QuoteTO> contentRegistration() {
        return DataLoaderRegistration.<QuoteTO>builder()
                .dataLoader(DataLoader.newMappedDataLoader((ids, context) ->
                        CompletableFuture.supplyAsync(() -> quoteMapper.toDto(
                                quoteRepository.findByContentsMap(ids), (Language) context.getKeyContextsList().get(0))
                        )))
                .name(CONTENT)
                .build();
    }

    @Override
    public Set<DataLoaderRegistration> getDataLoaderRegistrations() {
        return Set.of(
                contentRegistration()
        );
    }
}
