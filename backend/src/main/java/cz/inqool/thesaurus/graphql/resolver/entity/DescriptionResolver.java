package cz.inqool.thesaurus.graphql.resolver.entity;

import cz.inqool.thesaurus.graphql.dto.ContentTO;
import cz.inqool.thesaurus.graphql.dto.DescriptionTO;
import cz.inqool.thesaurus.system.content.ContentDataLoaderCreator;
import cz.inqool.thesaurus.system.content.ContentRepository;
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
public class DescriptionResolver implements GraphQLResolver<DescriptionTO> {

    private final ContentRepository contentRepository;

    @Transactional
    public CompletableFuture<List<ContentTO>> contents(DescriptionTO description, DataFetchingEnvironment env) {
        DataLoader<String, List<ContentTO>> dataLoader = env.getDataLoader(ContentDataLoaderCreator.DESCRIPTION);
        return dataLoader.load(description.getId(), env.getLocalContext());
    }
}
