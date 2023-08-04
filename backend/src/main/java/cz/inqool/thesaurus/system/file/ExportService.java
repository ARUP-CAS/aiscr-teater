package cz.inqool.thesaurus.system.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.inqool.thesaurus.system.category.*;
import cz.inqool.thesaurus.system.history.HistoryEventRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExportService {

    private final CategoryRepository categoryRepository;
    private final HistoryEventRepository historyEventRepository;
    private final CategoryMapper categoryMapper;
    private final ObjectMapper objectMapper;

    public ExportSingleDto exportSingle(@NonNull String id) {
        CategoryExportMainView category = categoryRepository.findCategoryForExport(id);
        return ExportSingleDto.builder()
                .category(categoryMapper.toSingleExportDto(category.toEntity()))
                .lastImport(historyEventRepository.findByLabel("last_import_date").getTimestamp().toLocalDate())
                .build();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void exportAll() throws IOException {
        log.info("Creating export file.");
        File file = new File("export.json");
        List<CategoryExportDto> categories = categoryRepository.findCategoriesForExport().stream()
                .map(CategoryCompleteView::toEntity)
                .map(categoryMapper::toCompleteExportDto)
                .collect(Collectors.toList());
        objectMapper.writeValue(file, ExportAllDto.builder()
                .categories(categories)
                .lastImport(historyEventRepository.findByLabel("last_import_date").getTimestamp().toLocalDate())
                .build());
        log.info("Export file created.");
    }
}
