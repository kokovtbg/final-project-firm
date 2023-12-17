package bg.sirma.project.service;

import bg.sirma.project.exception.CsvException;
import bg.sirma.project.utils.MyDateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
public class CsvService {
    private final MyDateValidator dateValidator;
    private static final String PATH = "src/main/java/bg/sirma/project/temp/";

    @Autowired
    public CsvService(MyDateValidator dateValidator) {
        this.dateValidator = dateValidator;
    }

    public boolean read() {
        try (Reader reader = Files.newBufferedReader(Path.of(PATH))) {

        } catch (IOException e) {
            return false;
        }

        return true;
    }

    public void save(MultipartFile file) throws CsvException {
        if (file.isEmpty()) {
            throw new CsvException("File can not be empty!");
        }

        Path destinationFile = Path.of(PATH)
                .resolve(Paths.get(Objects.requireNonNull(file.getOriginalFilename())))
                .normalize()
                .toAbsolutePath();

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new CsvException("Failed to store the file.");
        }

    }
}
