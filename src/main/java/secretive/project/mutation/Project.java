package secretive.project.mutation;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import secretive.concept.ApiEntity;
import secretive.concept.EntityReference;
import secretive.department.mutation.Department;
import secretive.project.ExcludedProjectDto;
import secretive.project.ProjectDto;
import secretive.project.presentation.ProjectCreationRequest;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Entity
public class Project implements ApiEntity {
    @Id
    @NotNull
    UUID id;

    @NotNull
    String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    Department department;

    @NotNull
    OffsetDateTime createdAt;

    @NotNull
    OffsetDateTime updatedAt;

    Project() {
    }

    Project(ProjectCreationRequest projectCreationRequest, EntityReference<Department> parentDepartment) {
        name = projectCreationRequest.name();
        id = UUID.randomUUID();
        department = parentDepartment.value();

        var now = OffsetDateTime.now().withOffsetSameInstant(ZoneOffset.UTC);

        createdAt = now;
        updatedAt = now;
    }

    public ProjectDto toDto() {
        return new ProjectDto(id, name, department.toDto());
    }

    public ExcludedProjectDto excludedProjectDto() {
        return new ExcludedProjectDto(id, name, department.toDto());
    }
}
