package secretive.project.presentation;

import org.springframework.web.bind.annotation.*;
import secretive.project.ProjectDto;
import secretive.project.mutation.ProjectService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("project")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    ProjectDto create(@RequestBody ProjectCreationRequest creationRequest) {
        return projectService.create(creationRequest);
    }

    @GetMapping("{id}")
    ProjectDto retrieveById(@PathVariable UUID id) {
        return projectService.retrieve(id);
    }

    @GetMapping("name/{name}")
    ProjectDto retrieveById(@PathVariable String name) {
        return projectService.retrieveByName(name);
    }

    @GetMapping
    List<ProjectDto> retrieveByDepartmentId(@RequestParam UUID departmentId) {
        return projectService.retrieveAllByDepartment(departmentId);
    }
}
