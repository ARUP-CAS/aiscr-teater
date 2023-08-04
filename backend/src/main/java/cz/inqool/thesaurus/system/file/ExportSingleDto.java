package cz.inqool.thesaurus.system.file;

import cz.inqool.thesaurus.system.category.CategoryExportDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class ExportSingleDto {
    private CategoryExportDto category;
    private LocalDate lastImport;
}
