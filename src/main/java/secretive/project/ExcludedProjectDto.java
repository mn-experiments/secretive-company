package secretive.project;

import secretive.department.DepartmentDto;

import java.util.UUID;

public record ExcludedProjectDto(
        UUID id,
        String name,
        DepartmentDto department
) {
}
