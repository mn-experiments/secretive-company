package secretive.team.mutation;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import secretive.project.mutation.Project;
import secretive.team.TeamDto;
import secretive.team.presentation.TeamCreationRequest;

import java.util.UUID;

@Entity
class Team {
    @Id
    UUID id = UUID.randomUUID();

    String name;

    @ManyToOne(fetch = FetchType.LAZY)
    Project project;

    Team() {
    }

    Team(TeamCreationRequest creationRequest, Project parentProject) {
        name = creationRequest.name();
        project = parentProject;
    }

    public TeamDto toDto() {
        return new TeamDto(id, name, project.toDto());
    }
}
