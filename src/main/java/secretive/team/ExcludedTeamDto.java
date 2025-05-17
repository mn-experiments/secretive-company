package secretive.team;

import secretive.project.ProjectDto;

import java.util.UUID;

public record ExcludedTeamDto(UUID id, String name, ProjectDto project) {
}
