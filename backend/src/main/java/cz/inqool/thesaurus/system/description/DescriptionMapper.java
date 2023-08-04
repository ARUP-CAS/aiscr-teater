package cz.inqool.thesaurus.system.description;

import cz.inqool.thesaurus.graphql.dto.DescriptionTO;
import cz.inqool.thesaurus.system.Language;
import cz.inqool.thesaurus.system.category.CategoryExportDto;
import cz.inqool.thesaurus.system.category.CategoryMapper;
import cz.inqool.thesaurus.system.content.ContentMapper;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DescriptionMapper {

    private CategoryMapper categoryMapper;
    private ContentMapper contentMapper;
    private MessageSource messageSource;

    public DescriptionTO toDto(Description description, Language language) {
        return description == null ? null :
                DescriptionTO.builder()
                        .setId(description.getId())
                        .setCategory(categoryMapper.toDto(description.getCategory(), language))
                        .setTitle(messageSource.getMessage(description.getTitlePropertyKey(), null, language.getLocale()))
                        .build();
    }

    public Map<String, List<DescriptionTO>> toDto(Map<String, List<Description>> descriptions, Language language) {
        return descriptions.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .map(description -> toDto(description, language))
                                .collect(Collectors.toList())
                ));
    }

    public CategoryExportDto.DescriptionExportDto toExportDto(Description description) {
        return CategoryExportDto.DescriptionExportDto.builder()
                .title(CategoryExportDto.DescriptionExportDto.DescriptionTitle.builder()
                        .cz(messageSource.getMessage(description.getTitlePropertyKey(), null, Language.CS.getLocale()))
                        .en(messageSource.getMessage(description.getTitlePropertyKey(), null, Language.EN.getLocale()))
                        .de(messageSource.getMessage(description.getTitlePropertyKey(), null, Language.DE.getLocale()))
                        .build())
                .contents(description.getContents().stream().map(contentMapper::toExportDto).collect(Collectors.toList()))
                .build();
    }

    @Inject
    public void setCategoryMapper(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    @Inject
    public void setContentMapper(ContentMapper contentMapper) {
        this.contentMapper = contentMapper;
    }

    @Inject
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
}
