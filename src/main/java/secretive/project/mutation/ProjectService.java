package secretive.project.mutation;

import org.springframework.stereotype.Service;
import secretive.department.mutation.DepartmentService;
import secretive.exception.ErrorMessage;
import secretive.exception.throwable.ApiException;
import secretive.project.ProjectDto;
import secretive.project.presentation.ProjectCreationRequest;

import java.util.List;
import java.util.UUID;

@Service
public class ProjectService {
    private final DepartmentService departmentService;
    private final ProjectRepo projectRepo;

    ProjectService(DepartmentService departmentService, ProjectRepo projectRepo) {
        this.departmentService = departmentService;
        this.projectRepo = projectRepo;
    }

    public ProjectDto create(ProjectCreationRequest creationRequest) {
        var project = new Project(creationRequest, departmentService.getReference(creationRequest.departmentId()));

        return projectRepo.save(project).toDto();
    }

    public ProjectDto retrieve(UUID id) {
        return projectRepo.findById(id)
                .orElseThrow(() -> new ApiException(ErrorMessage.NOT_FOUND_BY_ID, 404, "project", id))
                .toDto();
    }

    public ProjectDto retrieveByName(String name) {
        return projectRepo.findByName(name)
                .orElseThrow(() -> new ApiException(ErrorMessage.NOT_FOUND_BY_NAME, 404, "project", name))
                .toDto();
    }

    public List<ProjectDto> retrieveAllByDepartment(UUID departmentId) {
        return projectRepo.findAllByDepartmentId(departmentId)
                .stream().map(Project::toDto).toList();
    }

}
