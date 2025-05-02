package secretive.project.mutation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjectRepo extends JpaRepository<Project, UUID> {
    Optional<Project> findByName(String name);

    List<Project> findAllByDepartmentId(UUID departmentId);
}
