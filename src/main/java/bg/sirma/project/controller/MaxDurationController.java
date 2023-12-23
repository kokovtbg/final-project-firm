package bg.sirma.project.controller;

import bg.sirma.project.model.EmployeeProjectProjection;
import bg.sirma.project.service.EmployeeProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/max-duration")
public class MaxDurationController {
    private final EmployeeProjectService employeeProjectService;

    @Autowired
    public MaxDurationController(EmployeeProjectService employeeProjectService) {
        this.employeeProjectService = employeeProjectService;
    }

    @GetMapping
    public EmployeeProjectProjection getMaxDuration() {
        return employeeProjectService.getMaxDuration();
    }
}
