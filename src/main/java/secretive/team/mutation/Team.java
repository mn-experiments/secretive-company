package secretive.team.mutation;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import secretive.project.mutation.Project;
import secretive.team.TeamDto;
import secretive.team.presentation.TeamCreationRequest;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Entity
class Team {
    @Id
    @NotNull
    UUID id = UUID.randomUUID();

    @NotNull
    String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    Project project;

    @NotNull
    OffsetDateTime createdAt;

    @NotNull
    OffsetDateTime updatedAt;

    Team() {
    }

    Team(TeamCreationRequest creationRequest, Project parentProject) {
        name = creationRequest.name();
        project = parentProject;

        var now = OffsetDateTime.now().withOffsetSameInstant(ZoneOffset.UTC);
        createdAt = now;
        updatedAt = now;
    }

    public TeamDto toDto() {
        return new TeamDto(id, name, project.toDto());
    }
}
