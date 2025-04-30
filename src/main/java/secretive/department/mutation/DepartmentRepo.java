package secretive.department.mutation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
interface DepartmentRepo extends JpaRepository<Department, UUID> {
    Optional<Department> findByName(String name);
}
