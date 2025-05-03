package secretive.team.presentation;

import java.util.UUID;

public record TeamCreationRequest(String name, UUID projectId) {
}
