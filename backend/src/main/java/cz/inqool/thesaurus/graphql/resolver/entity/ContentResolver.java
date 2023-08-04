package cz.inqool.thesaurus.graphql.resolver.entity;

import cz.inqool.thesaurus.graphql.dto.ContentTO;
import cz.inqool.thesaurus.graphql.dto.QuoteTO;
import cz.inqool.thesaurus.system.quote.QuoteDataLoaderCreator;
import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class ContentResolver implements GraphQLResolver<ContentTO> {

    @Transactional
    public CompletableFuture<List<QuoteTO>> quotes(ContentTO content, DataFetchingEnvironment env) {
        DataLoader<String, List<QuoteTO>> dataLoader = env.getDataLoader(QuoteDataLoaderCreator.CONTENT);
        return dataLoader.load(content.getId(), env.getLocalContext());
    }
}
