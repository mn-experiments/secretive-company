package secretive.concept;

/**
 * <p>
 * This is a wrapper for JPA entities that have been instantiated with the
 * {@link org.springframework.data.jpa.repository.JpaRepository#getReferenceById} method
 * or any other JPA repository method that builds a reference instead of fetching the actual entity
 * data from the DB.
 * </p>
 * <p>
 * The intention is to make it explicit that the object that is produced is a reference.
 * </p>
 *
 * @param <ENTITY>
 */
public class EntityReference<ENTITY extends ApiEntity> {
    private final ENTITY entity;

    public EntityReference(ENTITY entity) {
        this.entity = entity;
    }

    public ENTITY value() {
        return entity;
    }
}
