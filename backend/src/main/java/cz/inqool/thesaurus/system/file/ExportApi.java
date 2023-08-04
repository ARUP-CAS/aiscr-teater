package cz.inqool.thesaurus.system.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api/export")
@RequiredArgsConstructor
public class ExportApi {


    private final ObjectMapper objectMapper;
    private final ExportService exportService;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<ByteArrayResource> exportSingle(@PathVariable("id") String id) throws IOException {

        ExportSingleDto dto = exportService.exportSingle(id);
        byte[] bytes = objectMapper.writeValueAsBytes(dto);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + dto.getCategory().getId() + ".json\"")
                .header("Content-Length", String.valueOf(bytes.length))
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ByteArrayResource(bytes));
    }

    @GetMapping(produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<InputStreamResource> exportAll() throws IOException {

        File file = new File("export.json");

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"")
                .header("Content-Length", String.valueOf(file.length()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(new InputStreamResource(file.toURI().toURL().openStream()));
    }

    @GetMapping(value = "/original/{fileName}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<InputStreamResource> exportOriginal(@PathVariable("fileName") String fileName) throws IOException {

        File file = new File("json/" + fileName);
        if (!file.getParent().equals("json")) {
            throw new IllegalArgumentException("Path not in the json subdirectory");
        }

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"")
                .header("Content-Length", String.valueOf(file.length()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(new InputStreamResource(file.toURI().toURL().openStream()));
    }
}
