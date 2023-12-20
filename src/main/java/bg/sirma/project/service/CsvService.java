package bg.sirma.project.service;

import bg.sirma.project.exception.CsvException;
import bg.sirma.project.exception.EmployeeProjectException;
import bg.sirma.project.model.EmployeeProject;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CsvService {
    private final MyDateValidator dateValidator;
    private final EmployeeProjectService employeeProjectService;
    private static final String PATH = "src/main/java/bg/sirma/project/temp/";
    private static String fileName = "";

    @Autowired
    public CsvService(MyDateValidator dateValidator, EmployeeProjectService employeeProjectService) {
        this.dateValidator = dateValidator;
        this.employeeProjectService = employeeProjectService;
    }

    public List<String> read(String pattern) throws CsvException {
        List<String> exceptions = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(Path.of(PATH + fileName))) {
            String line = reader.readLine();
            int row = 1;
            while (line != null) {
                String[] data = line.split("\\s*,\\s*");
                try {
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
                        endDate = null;
                    } else {
                        throw new CsvException(String.format("Must enter valid endDate with pattern - %s", pattern));
                    }

                    employeeProjectService.create(employeeId, projectId, startDate, endDate);
                } catch (NumberFormatException e) {
                    exceptions.add(String.format("Must contain valid ID for employee and project at row %d", row));
                } catch (ArrayIndexOutOfBoundsException e) {
                    exceptions.add(String.format("Could not parse data at row %d. Valid is `employeeId,projectId,startDate,endDate(NULL)`", row));
                } catch (CsvException | EmployeeProjectException e) {
                    exceptions.add(e.getMessage());
                }

                row++;
                line = reader.readLine();
            }
        } catch (IOException e) {
            throw new CsvException("Could not parse data. Valid is `employeeId,projectId,startDate,endDate(NULL)`");
        }

        return exceptions;
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
