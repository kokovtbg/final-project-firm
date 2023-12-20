package bg.sirma.project.controller;

import bg.sirma.project.exception.CsvException;
import bg.sirma.project.service.CsvService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/csv")
public class CsvController {
    private final CsvService csvService;

    @Autowired
    public CsvController(CsvService csvService) {
        this.csvService = csvService;
    }

    @PostMapping
    public ResponseEntity<List<String>> postCsvFile(@RequestParam(value = "date-pattern") String datePattern,
                                      @NotNull @RequestParam(value = "file") MultipartFile file) throws CsvException {
        csvService.save(file);
        List<String> exceptions = csvService.read(datePattern);

        return ResponseEntity.ok(exceptions);
    }
}
