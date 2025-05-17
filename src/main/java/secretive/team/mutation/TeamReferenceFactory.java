package secretive.team.mutation;

import org.springframework.stereotype.Component;
import secretive.concept.EntityReference;
import secretive.exception.ErrorMessage;
import secretive.exception.throwable.ApiException;
import secretive.project.mutation.Project;
import secretive.project.mutation.ProjectRepo;

import java.util.UUID;

@Component
public class TeamReferenceFactory {
    private final TeamRepo teamRepo;

    public TeamReferenceFactory(TeamRepo teamRepo) {
        this.teamRepo = teamRepo;
    }

    public EntityReference<Team> getReference(UUID id) {
        if (teamRepo.existsById(id)) {
            return new EntityReference(teamRepo.getReferenceById(id));
        }

        throw new ApiException(ErrorMessage.RELATION_NOT_FOUND_BY_ID, 400, "team", id);
    }
}
