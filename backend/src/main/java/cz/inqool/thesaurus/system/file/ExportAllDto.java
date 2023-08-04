package cz.inqool.thesaurus.system.file;

import cz.inqool.thesaurus.system.category.CategoryExportDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
public class ExportAllDto {
    private List<CategoryExportDto> categories;
    private LocalDate lastImport;
}
