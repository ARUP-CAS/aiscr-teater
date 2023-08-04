package cz.inqool.thesaurus.system.content;

import cz.inqool.thesaurus.graphql.dto.ContentTO;
import cz.inqool.thesaurus.system.Language;
import cz.inqool.thesaurus.system.category.CategoryExportDto;
import cz.inqool.thesaurus.system.description.DescriptionMapper;
import cz.inqool.thesaurus.system.quote.QuoteMapper;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ContentMapper {

    private DescriptionMapper descriptionMapper;
    private QuoteMapper quoteMapper;

    public ContentTO toDto(Content content, Language language) {
        return content == null ? null :
                ContentTO.builder()
                        .setId(content.getId())
                        .setText(content.getText())
                        .setDescription(descriptionMapper.toDto(content.getDescription(), language))
                        .build();
    }

    public Map<String, List<ContentTO>> toDto(Map<String, List<Content>> contents, Language language) {
        return contents.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .map(content -> toDto(content, language))
                                .collect(Collectors.toList())
                ));
    }

    public CategoryExportDto.DescriptionExportDto.ContentExportDto toExportDto(Content content) {
        return CategoryExportDto.DescriptionExportDto.ContentExportDto.builder()
                .text(content.getText())
                .language(content.getLanguage())
                .quotes(content.getQuotes().stream().map(quoteMapper::toExportDto).collect(Collectors.toList()))
                .build();
    }

    @Inject
    public void setDescriptionMapper(DescriptionMapper descriptionMapper) {
        this.descriptionMapper = descriptionMapper;
    }

    @Inject
    public void setQuoteMapper(QuoteMapper quoteMapper) {
        this.quoteMapper = quoteMapper;
    }
}
