package secretive.project.mutation;

import org.springframework.stereotype.Component;
import secretive.concept.EntityReference;
import secretive.exception.ErrorMessage;
import secretive.exception.throwable.ApiException;

import java.util.UUID;

@Component
public class ProjectReferenceFactory {
    private final ProjectRepo projectRepo;

    public ProjectReferenceFactory(ProjectRepo projectRepo) {
        this.projectRepo = projectRepo;
    }

    public EntityReference<Project> getReference(UUID id) {
        if (projectRepo.existsById(id)) {
            return new EntityReference(projectRepo.getReferenceById(id));
        }

        throw new ApiException(ErrorMessage.RELATION_NOT_FOUND_BY_ID, 400, "project", id);
    }
}
