package secretive.team.mutation;

import org.springframework.stereotype.Service;
import secretive.concept.EntityReferenceFactory;
import secretive.exception.ErrorMessage;
import secretive.exception.throwable.ApiException;
import secretive.team.TeamDto;
import secretive.team.presentation.TeamCreationRequest;

import java.util.List;
import java.util.UUID;

@Service
public class TeamService {
    private final TeamRepo teamRepo;
    private final EntityReferenceFactory referenceFactory;

    public TeamService(TeamRepo teamRepo, EntityReferenceFactory referenceFactory) {
        this.teamRepo = teamRepo;
        this.referenceFactory = referenceFactory;
    }

    public TeamDto create(TeamCreationRequest creationRequest) {
        var parentProjectReference = referenceFactory.getProjectReference(creationRequest.projectId());
        var team = new Team(creationRequest, parentProjectReference);

        return teamRepo.save(team).toDto();
    }

    public TeamDto retrieve(UUID id) {
        var team = teamRepo.findById(id);

        return team.map(Team::toDto)
                .orElseThrow(() -> new ApiException(ErrorMessage.NOT_FOUND_BY_ID, 404, "team", id));
    }

    public TeamDto retrieveByName(String name) {
        var team = teamRepo.findByName(name);

        return team.map(Team::toDto)
                .orElseThrow(() -> new ApiException(ErrorMessage.NOT_FOUND_BY_NAME, 404, "team", name));
    }

    public List<TeamDto> retrieveAllByProject(UUID projectId) {
        var team = teamRepo.findAllByProjectId(projectId);

        return team.stream().map(Team::toDto).toList();
    }

}
