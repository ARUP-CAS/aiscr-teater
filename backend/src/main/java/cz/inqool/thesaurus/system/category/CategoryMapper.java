package cz.inqool.thesaurus.system.category;

import cz.inqool.thesaurus.graphql.dto.CategoryTO;
import cz.inqool.thesaurus.system.Language;
import cz.inqool.thesaurus.system.description.DescriptionMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {

    private DescriptionMapper descriptionMapper;
    private String urlBase;

    public CategoryTO toDto(Category category, Language language) {
        return category == null ? null :
                CategoryTO.builder()
                        .setDateAccurate(category.getDateAccurate())
                        .setId(category.getId())
                        .setUrl(category.getUrl())
                        .setName(category.getName().getLocalizedName(language))
                        .setDateTo(category.getDateTo())
                        .setDateFrom(category.getDateFrom())
                        .setLeaf(category.isLeaf())
                        .build();
    }

    public Map<String, List<CategoryTO>> toDto(Map<String, List<Category>> categories, Language language) {
        return categories.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .map(category -> toDto(category, language))
                                .collect(Collectors.toList())
                ));
    }

    public List<CategoryTO> toDto(List<Category> categories, Language language) {
        return categories.stream()
                .map(category -> toDto(category, language))
                .collect(Collectors.toList());
    }

    public CategoryExportDto toSingleExportDto(Category category) {
        return category == null ? null :
                CategoryExportDto.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .dateFrom(category.getDateFrom())
                        .dateTo(category.getDateTo())
                        .dateAccurate(category.getDateAccurate())
                        .url(urlBase + "id/" + category.getId())
                        .parent(toSimpleExportDto(category.getParent()))
                        .children(category.getChildren().stream().map(this::toSimpleExportDto).collect(Collectors.toList()))
                        .descriptions(category.getDescriptions().stream().map(descriptionMapper::toExportDto).collect(Collectors.toList()))
                        .build();
    }

    private CategoryExportDto toSimpleExportDto(Category category) {
        return category == null ? null :
                CategoryExportDto.builder()
                        .url(urlBase + "id/" + category.getId())
                        .build();
    }

    public CategoryExportDto toCompleteExportDto(Category category) {
        return category == null ? null :
                CategoryExportDto.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .dateFrom(category.getDateFrom())
                        .dateTo(category.getDateTo())
                        .dateAccurate(category.getDateAccurate())
                        .url(urlBase + "id/" + category.getId())
                        .parent(toSimpleExportDto(category.getParent()))
                        .children(category.getChildren().stream().map(this::toCompleteExportDto).collect(Collectors.toList()))
                        .descriptions(category.getDescriptions().stream().map(descriptionMapper::toExportDto).collect(Collectors.toList()))
                        .build();
    }

    @Inject
    public void setDescriptionMapper(DescriptionMapper descriptionMapper) {
        this.descriptionMapper = descriptionMapper;
    }

    @Inject
    public void setUrlBase(@Value("${url-base}") String urlBase) {
        this.urlBase = urlBase;
    }
}
