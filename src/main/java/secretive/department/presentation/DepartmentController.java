package secretive.department.presentation;

import org.springframework.web.bind.annotation.*;
import secretive.department.DepartmentDto;
import secretive.department.mutation.DepartmentService;

import java.util.UUID;

@RestController
@RequestMapping("department")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping
    public DepartmentDto create(@RequestBody DepartmentCreationRequest creationRequest) {
        DepartmentDto departmentDto = departmentService.create(creationRequest);
        return departmentDto;
    }

    @GetMapping("{id}")
    public DepartmentDto retrieve(@PathVariable UUID id) {
        return departmentService.retrieve(id);
    }

    @GetMapping("/name/{name}")
    public DepartmentDto retrieveByName(@PathVariable String name) {
        return departmentService.retrieveByName(name);
    }
}
