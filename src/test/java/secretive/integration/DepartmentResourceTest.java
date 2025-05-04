package secretive.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import secretive.department.DepartmentDto;
import secretive.department.presentation.DepartmentCreationRequest;
import secretive.exception.ErrorResponse;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class DepartmentResourceTest extends ResourceTest {

    private String path;

    @BeforeEach
    void pathSetup() {
        path = "http://localhost:%d/department".formatted(port);
    }

    @Test
    void canCreateDepartment() {
        var creationRequest = new DepartmentCreationRequest("dep1");

        var response = doPost(path, creationRequest, DepartmentDto.class);
        var createdDto = response.getBody();
        var retrievedDto = doGet(path + "/name/" + creationRequest.name(), DepartmentDto.class)
                .getBody();

        assertThat(createdDto).isEqualTo(retrievedDto);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }

    @Test
    void return404WhenRetrievingMissingDepartmentByName() {
        var response = doGet(path + "/name/missing-dept", ErrorResponse.class);

        assertThat(response.getStatusCode().value()).isEqualTo(404);
        assertThat(response.getBody().errors())
                .anyMatch(it -> it.contains("[department] with name [missing-dept] does not exist"));
    }

    @Test
    void return404WhenRetrievingMissingDepartmentById() {
        UUID randomId = UUID.randomUUID();
        var response = doGet(path + "/" + randomId, ErrorResponse.class);

        assertThat(response.getStatusCode().value()).isEqualTo(404);
        assertThat(response.getBody().errors())
                .anyMatch(it -> it.contains("[department] with id [%s] does not exist".formatted(randomId)));
    }

    @Test
    void invalidCreationRequestIsRejected() {
        var creationRequest = new DepartmentCreationRequest(null);

        var response = doPost(path, creationRequest, ErrorResponse.class);

        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(response.getBody().errors())
                .anyMatch(it -> it.contains("[name]: must not be null"));
    }

}
