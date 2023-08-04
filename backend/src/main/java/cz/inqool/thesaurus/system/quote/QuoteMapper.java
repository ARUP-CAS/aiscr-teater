package cz.inqool.thesaurus.system.quote;

import cz.inqool.thesaurus.graphql.dto.QuoteTO;
import cz.inqool.thesaurus.system.Language;
import cz.inqool.thesaurus.system.category.CategoryExportDto;
import cz.inqool.thesaurus.system.content.ContentMapper;
import cz.inqool.thesaurus.system.source.SourceMapper;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class QuoteMapper {

    private ContentMapper contentMapper;
    private SourceMapper sourceMapper;

    public QuoteTO toDto(Quote quote, Language language) {
        return quote == null ? null :
                QuoteTO.builder()
                        .setId(quote.getId())
                        .setTitle(quote.getTitle())
                        .setDate(quote.getDate())
                        .setLocationPage(quote.getLocationPage())
                        .setLocationUrl(quote.getLocationUrl())
                        .setContent(contentMapper.toDto(quote.getContent(), language))
                        .setSource(sourceMapper.toDto(quote.getSource()))
                        .build();
    }

    public Map<String, List<QuoteTO>> toDto(Map<String, List<Quote>> quotes, Language language) {
        return quotes.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .map(quote -> toDto(quote, language))
                                .collect(Collectors.toList())
                ));
    }

    public CategoryExportDto.DescriptionExportDto.ContentExportDto.QuoteExportDto toExportDto(Quote quote) {
        return CategoryExportDto.DescriptionExportDto.ContentExportDto.QuoteExportDto.builder()
                .title(quote.getTitle())
                .date(quote.getDate())
                .locationPage(quote.getLocationPage())
                .locationUrl(quote.getLocationUrl())
                .source(sourceMapper.toExportDto(quote.getSource()))
                .build();
    }

    @Inject
    public void setContentMapper(ContentMapper contentMapper) {
        this.contentMapper = contentMapper;
    }

    @Inject
    public void setSourceMapper(SourceMapper sourceMapper) {
        this.sourceMapper = sourceMapper;
    }
}
