package secretive.team.mutation;

import org.springframework.stereotype.Service;
import secretive.exception.ErrorMessage;
import secretive.exception.throwable.ApiException;
import secretive.project.mutation.ProjectService;
import secretive.team.TeamDto;
import secretive.team.presentation.TeamCreationRequest;

import java.util.List;
import java.util.UUID;

@Service
public class TeamService {
    private final TeamRepo teamRepo;
    private final ProjectService projectService;

    public TeamService(TeamRepo teamRepo, ProjectService projectService) {
        this.teamRepo = teamRepo;
        this.projectService = projectService;
    }

    public TeamDto create(TeamCreationRequest creationRequest) {
        var parentProject = projectService.getReference(creationRequest.projectId());
        var team = new Team(creationRequest, parentProject);

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
