package secretive.department.mutation;

import org.springframework.stereotype.Component;
import secretive.concept.EntityReference;
import secretive.exception.ErrorMessage;
import secretive.exception.throwable.ApiException;

import java.util.UUID;

@Component
public class DepartmentReferenceFactory {
    private final DepartmentRepo departmentRepo;

    public DepartmentReferenceFactory(DepartmentRepo departmentRepo) {
        this.departmentRepo = departmentRepo;
    }

    public EntityReference<Department> getReference(UUID id) {
        if (departmentRepo.existsById(id)) {
            return new EntityReference(departmentRepo.getReferenceById(id));
        }

        throw new ApiException(ErrorMessage.RELATION_NOT_FOUND_BY_ID, 400, "department", id);
    }
}
