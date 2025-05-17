package secretive.project.mutation;

import org.springframework.stereotype.Service;
import secretive.concept.EntityReference;
import secretive.concept.EntityReferenceFactory;
import secretive.department.mutation.Department;
import secretive.exception.ErrorMessage;
import secretive.exception.throwable.ApiException;
import secretive.project.ProjectDto;
import secretive.project.presentation.ProjectCreationRequest;

import java.util.List;
import java.util.UUID;

@Service
public class ProjectService {
    private final EntityReferenceFactory referenceFactory;
    private final ProjectRepo projectRepo;

    ProjectService(EntityReferenceFactory referenceFactory, ProjectRepo projectRepo) {
        this.referenceFactory = referenceFactory;
        this.projectRepo = projectRepo;
    }

    public ProjectDto create(ProjectCreationRequest creationRequest) {
        var parentDepartmentReference = referenceFactory.getDepartmentReference(creationRequest.departmentId());
        var project = new Project(creationRequest, parentDepartmentReference);

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
