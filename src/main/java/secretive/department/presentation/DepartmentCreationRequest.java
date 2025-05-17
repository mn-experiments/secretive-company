package secretive.department.presentation;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public record DepartmentCreationRequest(String name,
                                        Set<UUID> excludedDepartments,
                                        Set<UUID> excludedProjects,
                                        Set<UUID> excludedTeams
                                        ) {
    public Set<UUID> excludedDepartments() {
        return Objects.requireNonNullElse(excludedDepartments, Set.of());
    }

    public DepartmentCreationRequest(String name) {
        this(name, Set.of(), Set.of(), Set.of());
    }

    public DepartmentCreationRequest(String name, Set<UUID> excludedDepartments) {
        this(name, excludedDepartments, Set.of(), Set.of());
    }
}
