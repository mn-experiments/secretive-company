package secretive.department.mutation;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import secretive.department.DepartmentDto;
import secretive.department.presentation.DepartmentCreationRequest;

import java.util.UUID;

@Service
public class DepartmentService {
    private final DepartmentRepo departmentRepo;

    public DepartmentService(DepartmentRepo departmentRepo) {
        this.departmentRepo = departmentRepo;
    }

    @Transactional
    public DepartmentDto create(DepartmentCreationRequest creationRequest) {
        Department save = departmentRepo.save(new Department(creationRequest));
        return save.toDto();
    }

    public DepartmentDto retrieve(UUID id) {
        return departmentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("not found"))
                .toDto();
    }

    public DepartmentDto retrieveByName(String name) {
        return departmentRepo.findByName(name)
                .orElseThrow(() -> new RuntimeException("not found"))
                .toDto();
    }
}
