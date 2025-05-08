package secretive.department;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public record DepartmentDto(UUID id,
                            String name,
                            Set<ExcludedDepartmentDto> excludedDepartments) {

    public Set<ExcludedDepartmentDto> excludedDepartments() {
        return Objects.requireNonNullElse(excludedDepartments, Set.of());
    }

    public DepartmentDto(UUID id, String name) {
        this(id, name, Set.of());
    }
}
