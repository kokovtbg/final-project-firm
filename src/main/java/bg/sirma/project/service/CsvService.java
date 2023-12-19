package bg.sirma.project.service;

import bg.sirma.project.exception.CsvException;
import bg.sirma.project.utils.MyDateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Objects;

@Service
public class CsvService {
    private final MyDateValidator dateValidator;
    private static final String PATH = "src/main/java/bg/sirma/project/temp/";
    private static String fileName = "";

    @Autowired
    public CsvService(MyDateValidator dateValidator) {
        this.dateValidator = dateValidator;
    }

    public void read(String pattern) throws CsvException {
        try (BufferedReader reader = Files.newBufferedReader(Path.of(PATH + fileName))) {
            String line = reader.readLine();
            while (line != null) {
                String[] data = line.split("\\s*,\\s*");
                int employeeId = Integer.parseInt(data[0]);
                int projectId = Integer.parseInt(data[1]);
                String startDateString = data[2];
                LocalDate startDate;
                if (dateValidator.isValid(startDateString, pattern)) {
                    startDate = LocalDate.parse(startDateString);
                } else {
                    throw new CsvException(String.format("Must enter valid startDate with pattern - %s", pattern));
                }

                String endDateString = data[3];
                LocalDate endDate;
                if (!endDateString.equals("NULL") && dateValidator.isValid(endDateString, pattern)) {
                    endDate = LocalDate.parse(data[3]);
                } else if (endDateString.equals("NULL")) {
                    endDate = LocalDate.now();
                } else {
                    throw new CsvException(String.format("Must enter valid endDate with pattern - %s", pattern));
                }

                System.out.printf("%d-%d-%s-%s%n", employeeId, projectId, startDate, endDate);

                line = reader.readLine();
            }
        } catch (IOException e) {
            throw new CsvException("Could not parse data. Valid is `employeeId,projectId,startDate,endDate(NULL)`");
        } catch (NumberFormatException e) {
            throw new CsvException("Must contain valid ID for employee and project");
        }
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

        fileName = file.getOriginalFilename();
    }
}
