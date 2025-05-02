package secretive.project.presentation;

import java.util.UUID;

public record ProjectCreationRequest(String name, UUID departmentId) {
}
