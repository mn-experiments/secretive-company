package secretive.project;

import secretive.department.DepartmentDto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ProjectDto(
        UUID id,
        String name,
        DepartmentDto department
) {
}
