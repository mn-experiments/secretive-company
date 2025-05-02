package secretive.concept;

import java.util.UUID;

public interface ApiService<T extends ApiEntity> {
    T getReference(UUID id);
}
