package secretive.department.presentation;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public record DepartmentCreationRequest(String name, Set<UUID> excludedDepartments) {
    public Set<UUID> excludedDepartments() {
        return Objects.requireNonNullElse(excludedDepartments, Set.of());
    }

    public DepartmentCreationRequest(String name) {
        this(name, Set.of());
    }
}
