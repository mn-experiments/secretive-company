package secretive.team;

import secretive.project.ProjectDto;

import java.util.UUID;

public record TeamDto(UUID id, String name, ProjectDto project) {
}
