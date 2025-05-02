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

    private String projectPath;
    private String projectPathWithId;
    private String projectPathWithName;
    private String projectPathWithDepartment;
    private String departmentPath;

    @BeforeEach
    void pathSetup() {
        projectPath = "http://localhost:%d/project".formatted(port);
        projectPathWithId = projectPath + "/%s";
        projectPathWithName = projectPath + "/name/%s";
        projectPathWithDepartment = projectPath + "?departmentId=%s";
        departmentPath = "http://localhost:%d/department".formatted(port);
    }

    @Test
    void canCreateProject() {
        var departmentCreationRequest = new DepartmentCreationRequest("d1");

        var department = template.postForEntity(departmentPath, departmentCreationRequest, DepartmentDto.class)
                .getBody();

        var projectCreationRequest = new ProjectCreationRequest("p1", department.id());

        var response = template.postForEntity(projectPath, projectCreationRequest, ProjectDto.class);

        assertThat(response.getStatusCode().value()).isEqualTo(200);

        var createdProject = response.getBody();
        assertThat(createdProject.id()).isNotNull();
        assertThat(createdProject.department()).isEqualTo(department);
        assertThat(createdProject.name()).isEqualTo(projectCreationRequest.name());
    }

    @Test
    void canRetrieveProject() {
        var departmentCreationRequest = new DepartmentCreationRequest("d1");

        var department = template.postForEntity(departmentPath, departmentCreationRequest, DepartmentDto.class)
                .getBody();

        var projectCreationRequest1 = new ProjectCreationRequest("p1", department.id());
        var projectCreationRequest2 = new ProjectCreationRequest("p2", department.id());

        var createdProject1 = template.postForEntity(projectPath, projectCreationRequest1, ProjectDto.class).getBody();
        var createdProject2 = template.postForEntity(projectPath, projectCreationRequest2, ProjectDto.class).getBody();

        var retrievedProject1 = template.getForEntity(
                projectPathWithId.formatted(createdProject1.id()),
                ProjectDto.class
        ).getBody();

        var retrievedProject2 = template.getForEntity(
                projectPathWithName.formatted(createdProject2.name()),
                ProjectDto.class
        ).getBody();

        var departmentProjects = (template.getForEntity(
                projectPathWithDepartment.formatted(department.id()),
                ProjectDto[].class
        ).getBody());

        assertThat(createdProject1).isEqualTo(retrievedProject1);
        assertThat(createdProject2).isEqualTo(retrievedProject2);
        assertThat(departmentProjects).containsExactlyInAnyOrder(createdProject1, createdProject2);
    }

    @Test
    void failCreationIfMissingDepartment() {

        var department = new DepartmentDto(UUID.randomUUID(), "random-dep");

        var projectCreationRequest = new ProjectCreationRequest("p1", department.id());

        var createdProject = template.postForEntity(projectPath, projectCreationRequest, ErrorResponse.class);

        assertThat(createdProject.getStatusCode().value()).isEqualTo(400);
        assertThat(createdProject.getBody().errors())
                .contains("failed to create because the linked [department] does not exist");
    }
}
