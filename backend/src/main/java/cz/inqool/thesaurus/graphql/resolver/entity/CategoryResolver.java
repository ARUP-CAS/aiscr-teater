package cz.inqool.thesaurus.graphql.resolver.entity;

import cz.inqool.thesaurus.graphql.dto.CategoryTO;
import cz.inqool.thesaurus.graphql.dto.DescriptionTO;
import cz.inqool.thesaurus.system.category.CategoryMapper;
import cz.inqool.thesaurus.system.category.CategoryRepository;
import cz.inqool.thesaurus.system.description.DescriptionDataLoaderCreator;
import cz.inqool.thesaurus.system.description.DescriptionRepository;
import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class CategoryResolver implements GraphQLResolver<CategoryTO> {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final DescriptionRepository descriptionRepository;
    private final MessageSource messageSource;

    @Transactional
    public CompletableFuture<List<DescriptionTO>> descriptions(CategoryTO category, DataFetchingEnvironment env) {
        DataLoader<String, List<DescriptionTO>> dataLoader = env.getDataLoader(DescriptionDataLoaderCreator.CATEGORY);
        return dataLoader.load(category.getId(), env.getLocalContext());
    }

    @Transactional
    public List<CategoryTO> children(CategoryTO category, DataFetchingEnvironment env) {
        return categoryMapper.toDto(categoryRepository.findChildren(category.getId(), env.getLocalContext()), env.getLocalContext());
//        DataLoader<String, List<CategoryTO>> dataLoader = env.getDataLoader(CategoryDataLoaderCreator._CHILDREN);
//        return dataLoader.load(category.getId(), env.getLocalContext());
    }

    @Transactional
    public List<CategoryTO> neighbors(CategoryTO category, DataFetchingEnvironment env) {
        return categoryMapper.toDto(categoryRepository.findNeighbors(category.getId(), env.getLocalContext()), env.getLocalContext());
//        DataLoader<String, List<CategoryTO>> dataLoader = env.getDataLoader(CategoryDataLoaderCreator._NEIGHBORS);
//        return dataLoader.load(category.getId(), env.getLocalContext());
    }

    @Transactional
    public List<CategoryTO> parentsNeighbors(CategoryTO category, DataFetchingEnvironment env) {
        return categoryMapper.toDto(categoryRepository.findParentNeighbors(category.getId(), env.getLocalContext()), env.getLocalContext());
//        DataLoader<String, List<CategoryTO>> dataLoader = env.getDataLoader(CategoryDataLoaderCreator._PARENT_NEIGHBORS);
//        return dataLoader.load(category.getId(), env.getLocalContext());
    }
}
