package secretive.team.mutation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TeamRepo extends JpaRepository<Team, UUID> {
    Optional<Team> findByName(String name);
    List<Team> findAllByProjectId(UUID projectId);
}
