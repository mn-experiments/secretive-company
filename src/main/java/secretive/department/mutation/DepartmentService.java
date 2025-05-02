package secretive.department.mutation;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import secretive.concept.ApiService;
import secretive.department.DepartmentDto;
import secretive.department.presentation.DepartmentCreationRequest;
import secretive.exception.throwable.ApiException;
import secretive.exception.ErrorMessage;

import java.util.UUID;

@Service
public class DepartmentService implements ApiService<Department> {
    private final DepartmentRepo departmentRepo;

    DepartmentService(DepartmentRepo departmentRepo) {
        this.departmentRepo = departmentRepo;
    }

    @Transactional
    public DepartmentDto create(DepartmentCreationRequest creationRequest) {
        Department save = departmentRepo.save(new Department(creationRequest));
        return save.toDto();
    }

    public DepartmentDto retrieve(UUID id) {
        return departmentRepo.findById(id)
                .orElseThrow(() -> new ApiException(ErrorMessage.NOT_FOUND_BY_ID, 404, "department", id))
                .toDto();
    }

    public DepartmentDto retrieveByName(String name) {
        return departmentRepo.findByName(name)
                .orElseThrow(() -> new ApiException(ErrorMessage.NOT_FOUND_BY_NAME, 404, "department", name))
                .toDto();
    }

    @Override
    public Department getReference(UUID id) {
        if (id == null) {
            throw new ApiException(ErrorMessage.OBJECT_FIELD_SHOULD_NOT_BE_NULL, 400, "department", "id");
        }
        return departmentRepo.getReferenceById(id);
    }
}
