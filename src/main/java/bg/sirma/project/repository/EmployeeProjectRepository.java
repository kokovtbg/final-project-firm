package bg.sirma.project.repository;

import bg.sirma.project.model.EmployeeProject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeProjectRepository extends JpaRepository<EmployeeProject, Integer> {
    EmployeeProject findByEmployeeIdAndProjectId(int employeeId, int projectId);
}
