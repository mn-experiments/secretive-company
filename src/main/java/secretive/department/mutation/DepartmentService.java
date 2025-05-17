package secretive.department.mutation;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import secretive.concept.EntityReferenceFactory;
import secretive.department.DepartmentDto;
import secretive.department.presentation.DepartmentCreationRequest;
import secretive.exception.ErrorMessage;
import secretive.exception.throwable.ApiException;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DepartmentService {
    private final EntityReferenceFactory referenceFactory;
    private final DepartmentRepo departmentRepo;

    DepartmentService(EntityReferenceFactory referenceFactory, DepartmentRepo departmentRepo) {
        this.referenceFactory = referenceFactory;
        this.departmentRepo = departmentRepo;
    }

    @Transactional
    public DepartmentDto create(DepartmentCreationRequest creationRequest) {
        var excludedDepartmentReferences = creationRequest.excludedDepartments().stream()
                .map(referenceFactory::getDepartmentReference)
                .collect(Collectors.toSet());

        var excludedProjectsReferences = creationRequest.excludedProjects().stream()
                .map(referenceFactory::getProjectReference)
                .collect(Collectors.toSet());

        var excludedTeamsReferences = creationRequest.excludedTeams().stream()
                .map(referenceFactory::getTeamReference)
                .collect(Collectors.toSet());

        Department save = departmentRepo.save(new Department(creationRequest,
                excludedDepartmentReferences,
                excludedProjectsReferences,
                excludedTeamsReferences));
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

}
