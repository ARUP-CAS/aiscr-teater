package cz.inqool.thesaurus.system.source;

import cz.inqool.thesaurus.graphql.dto.SourceTO;
import cz.inqool.thesaurus.system.category.CategoryExportDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SourceMapper {

    public SourceTO toDto(Source source) {
        return source == null ? null :
                SourceTO.builder()
                        .setId(source.getId())
                        .setLabel(source.getLabel())
                        .setUrl(source.getUrl())
                        .build();
    }

    public List<SourceTO> toDto(List<Source> sources) {
        return sources.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public CategoryExportDto.DescriptionExportDto.ContentExportDto.QuoteExportDto.SourceExportDto toExportDto(Source source) {
        return source == null ? null : CategoryExportDto.DescriptionExportDto.ContentExportDto.QuoteExportDto.SourceExportDto.builder()
                .label(source.getLabel())
                .url(source.getUrl())
                .build();
    }
}
