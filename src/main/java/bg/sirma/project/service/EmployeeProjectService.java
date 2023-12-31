package bg.sirma.project.service;

import bg.sirma.project.exception.EmployeeProjectException;
import bg.sirma.project.model.EmployeeProject;
import bg.sirma.project.model.EmployeeProjectProjection;
import bg.sirma.project.repository.EmployeeProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class EmployeeProjectService {
    private final EmployeeProjectRepository employeeProjectRepository;

    @Autowired
    public EmployeeProjectService(EmployeeProjectRepository employeeProjectRepository) {
        this.employeeProjectRepository = employeeProjectRepository;
    }

    public void create(int employeeId, int projectId, LocalDate startDate, LocalDate endDate) throws EmployeeProjectException {
        EmployeeProject employeeProject = new EmployeeProject(employeeId, projectId, startDate, endDate);
        validateRecord(employeeProject);
        employeeProjectRepository.save(employeeProject);
    }

    public EmployeeProjectProjection getMaxDuration() {
        return employeeProjectRepository.maxDuration();
    }

    private void validateRecord(EmployeeProject employeeProject) throws EmployeeProjectException {
        EmployeeProject employeeProjectFromDb = employeeProjectRepository
                .findByEmployeeIdAndProjectId(employeeProject.getEmployeeId(),
                        employeeProject.getProjectId());
        if (employeeProjectFromDb == null) {
            return;
        }

        LocalDate startDateFromDB = employeeProjectFromDb.getStartDate();
        LocalDate endDateFromDB = employeeProjectFromDb.getEndDate();
        if ((employeeProject.getEndDate() != null && employeeProject.getEndDate().isBefore(employeeProject.getStartDate())) ||
                (employeeProject.getEndDate() != null && employeeProject.getEndDate().isEqual(employeeProject.getStartDate()))) {
            throw new EmployeeProjectException("End date must be after start date");
        }

        if (employeeProject.getStartDate().isAfter(LocalDate.now()) ||
                (employeeProject.getEndDate() != null && employeeProject.getEndDate().isAfter(LocalDate.now()))) {
            throw new EmployeeProjectException("Dates must be before today");
        }

        if (startDateFromDB.isEqual(employeeProject.getStartDate()) ||
                endDateFromDB.isEqual(employeeProject.getEndDate())) {
            throw new EmployeeProjectException(String
                    .format("Start date %s and end date %s for employee with ID %d and project with ID %d exist",
                            employeeProject.getStartDate(),
                            employeeProject.getEndDate(),
                            employeeProject.getEmployeeId(),
                            employeeProject.getProjectId()));
        }

        if (startDateFromDB.isBefore(employeeProject.getStartDate()) &&
                endDateFromDB.isAfter(employeeProject.getStartDate())) {
            throw new EmployeeProjectException(String
                    .format("Start date %s for employee with ID %d and project with ID %d is not valid",
                            employeeProject.getStartDate(),
                            employeeProject.getEmployeeId(),
                            employeeProject.getProjectId()));
        }

        if (startDateFromDB.isBefore(employeeProject.getEndDate()) &&
                endDateFromDB.isAfter(employeeProject.getEndDate())) {
            throw new EmployeeProjectException(String
                    .format("End date %s for employee with ID %d and project with ID %d is not valid",
                            employeeProject.getEndDate(),
                            employeeProject.getEmployeeId(),
                            employeeProject.getProjectId()));
        }
    }
}
