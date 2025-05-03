package secretive.team.presentation;

import org.springframework.web.bind.annotation.*;
import secretive.team.TeamDto;
import secretive.team.mutation.TeamService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("team")
public class TeamController {
    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping
    TeamDto create(@RequestBody TeamCreationRequest creationRequest) {
        return teamService.create(creationRequest);
    }

    @GetMapping("{id}")
    TeamDto retrieve(@PathVariable UUID id) {
        return teamService.retrieve(id);
    }

    @GetMapping("name/{name}")
    TeamDto retrieveByName(@PathVariable String name) {
        return teamService.retrieveByName(name);
    }

    @GetMapping
    List<TeamDto> retrieveAllByProject(@RequestParam UUID projectId) {
        return teamService.retrieveAllByProject(projectId);
    }
}
