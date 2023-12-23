package bg.sirma.project.repository;

import bg.sirma.project.model.EmployeeProject;
import bg.sirma.project.model.EmployeeProjectProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EmployeeProjectRepository extends JpaRepository<EmployeeProject, Integer> {
    EmployeeProject findByEmployeeIdAndProjectId(int employeeId, int projectId);

    @Query(value = "SELECT\n" +
            "sum(\n" +
            "CASE\n" +
            "WHEN\n" +
            "(empl1.start_date <= empl2.start_date \n" +
            "AND empl2.start_date <= ifnull(empl1.end_date, curdate()) \n" +
            "AND ifnull(empl1.end_date, curdate()) <= ifnull(empl2.end_date, curdate()))\n" +
            "THEN \n" +
            "datediff(ifnull(empl1.end_date, curdate()), empl2.start_date)\n" +
            "\n" +
            "WHEN\n" +
            "(empl2.start_date <= empl1.start_date \n" +
            "AND empl1.start_date <= ifnull(empl2.end_date, curdate()) \n" +
            "AND ifnull(empl2.end_date, curdate()) <= ifnull(empl1.end_date, curdate()))\n" +
            "THEN \n" +
            "datediff(ifnull(empl2.end_date, curdate()), empl1.start_date)\n" +
            "\n" +
            "WHEN\n" +
            "(empl1.start_date <= empl2.start_date \n" +
            "AND empl2.start_date <= ifnull(empl2.end_date, curdate()) \n" +
            "AND ifnull(empl2.end_date, curdate()) <= ifnull(empl1.end_date, curdate()))\n" +
            "THEN\n" +
            "datediff(ifnull(empl2.end_date, curdate()), empl2.start_date)\n" +
            "\n" +
            "WHEN\n" +
            "(empl2.start_date <= empl1.start_date \n" +
            "AND empl1.start_date <= ifnull(empl1.end_date, curdate()) \n" +
            "AND ifnull(empl1.end_date, curdate()) <= ifnull(empl2.end_date, curdate()))\n" +
            "THEN \n" +
            "datediff(ifnull(empl1.end_date, curdate()), empl1.start_date)\n" +
            "END\n" +
            ") AS diff,\n" +
            "\n" +
            "empl1.employee_id AS firstEmployee, \n" +
            "empl2.employee_id AS secondEmployee,\n" +
            "group_concat(DISTINCT empl1.project_id) AS commonProjects \n" +
            "\n" +
            "FROM s_employees.employee_projects AS empl1\n" +
            "JOIN s_employees.employee_projects AS empl2\n" +
            "ON empl1.project_id = empl2.project_id\n" +
            "AND empl1.employee_id < empl2.employee_id\n" +
            "GROUP BY empl1.employee_id, empl2.employee_id\n" +
            "ORDER BY `diff` DESC\n" +
            "LIMIT 1", nativeQuery = true)
    EmployeeProjectProjection maxDuration();
}
