package secretive.concept;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import secretive.department.mutation.Department;
import secretive.exception.ErrorMessage;
import secretive.exception.throwable.ApiException;
import secretive.project.mutation.Project;
import secretive.team.mutation.Team;

import java.util.UUID;

@Service
public class EntityReferenceFactory {
    private final JpaRepository<Department, UUID> departmentRepo;
    private final JpaRepository<Project, UUID> projectRepo;
    private final JpaRepository<Team, UUID> teamRepo;

    public EntityReferenceFactory(JpaRepository<Department, UUID> departmentRepo,
                                  JpaRepository<Project, UUID> projectRepo,
                                  JpaRepository<Team, UUID> teamRepo) {
        this.departmentRepo = departmentRepo;
        this.projectRepo = projectRepo;
        this.teamRepo = teamRepo;
    }

    public EntityReference<Department> getDepartmentReference(UUID id) {
        return getReference(id, "department", departmentRepo);
    }

    public EntityReference<Project> getProjectReference(UUID id) {
        return getReference(id, "project", projectRepo);
    }

    public EntityReference<Team> getTeamReference(UUID id) {
        return getReference(id, "team", teamRepo);
    }

    private <T extends ApiEntity, ID> EntityReference<T> getReference(ID id, String type, JpaRepository<T, ID> repo) {
        if (repo.existsById(id)) {
            return new EntityReference<>(repo.getReferenceById(id));
        }

        throw new ApiException(ErrorMessage.RELATION_NOT_FOUND_BY_ID, 400, type, id);
    }

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
     */
    public static class EntityReference<ENTITY extends ApiEntity> {
        private final ENTITY entity;

        private EntityReference(ENTITY entity) {
            this.entity = entity;
        }

        public ENTITY value() {
            return entity;
        }
    }

}
