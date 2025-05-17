package secretive.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import secretive.department.DepartmentDto;
import secretive.department.presentation.DepartmentCreationRequest;
import secretive.exception.ErrorResponse;
import secretive.project.ProjectDto;
import secretive.project.presentation.ProjectCreationRequest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class ProjectResourceTest extends ResourceTest {

    @Test
    void canCreateProject() {
        var departmentCreationRequest = new DepartmentCreationRequest("d1");

        var clazz = DepartmentDto.class;
        var p = departmentPath;
        var department = doPost(p, departmentCreationRequest, clazz).getBody();

        var projectCreationRequest = new ProjectCreationRequest("p1", department.id());

        var response = doPost(projectPath, projectCreationRequest, ProjectDto.class);

        assertThat(response.getStatusCode().value()).isEqualTo(200);

        var createdProject = response.getBody();
        assertThat(createdProject.id()).isNotNull();
        assertThat(createdProject.department()).isEqualTo(department);
        assertThat(createdProject.name()).isEqualTo(projectCreationRequest.name());
    }

    @Test
    void canRetrieveProject() {
        var departmentCreationRequest = new DepartmentCreationRequest("d1");

        var departmentDtoResponseEntity = doPost(departmentPath, departmentCreationRequest, DepartmentDto.class);
        var department = departmentDtoResponseEntity.getBody();

        var projectCreationRequest1 = new ProjectCreationRequest("p1", department.id());
        var projectCreationRequest2 = new ProjectCreationRequest("p2", department.id());

        var createdProject1 = doPost(projectPath, projectCreationRequest1, ProjectDto.class).getBody();
        var createdProject2 = doPost(projectPath, projectCreationRequest2, ProjectDto.class).getBody();

        var retrievedProject1 = doGet(projectPathWithId.formatted(createdProject1.id()), ProjectDto.class).getBody();

        var retrievedProject2 =
                doGet(projectPathWithName.formatted(createdProject2.name()), ProjectDto.class).getBody();

        var departmentProjects =
                doGet(projectPathWithDepartment.formatted(department.id()), ProjectDto[].class).getBody();

        assertThat(createdProject1).isEqualTo(retrievedProject1);
        assertThat(createdProject2).isEqualTo(retrievedProject2);
        assertThat(departmentProjects).containsExactlyInAnyOrder(createdProject1, createdProject2);
    }

    @Test
    void failCreationIfMissingDepartment() {

        var department = new DepartmentDto(UUID.randomUUID(), "random-dep");

        var projectCreationRequest = new ProjectCreationRequest("p1", department.id());

        var createdProject = doPost(projectPath, projectCreationRequest, ErrorResponse.class);

        assertThat(createdProject.getStatusCode().value()).isEqualTo(400);
        assertThat(createdProject.getBody().errors())
                .contains(
                        "a link to object [department] with id [%s] is not possible because it does not exist".formatted(
                                department.id()));
    }
}
