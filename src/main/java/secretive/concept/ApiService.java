package secretive.concept;

import org.springframework.stereotype.Service;

@Service
public class ApiService<T extends ApiEntity> {
    protected final EntityReferenceFactory entityReferenceFactory;

    protected ApiService(EntityReferenceFactory entityReferenceFactory) {
        this.entityReferenceFactory = entityReferenceFactory;
    }
}
