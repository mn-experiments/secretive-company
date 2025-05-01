package secretive.integration;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import secretive.department.DepartmentDto;
import secretive.department.presentation.DepartmentCreationRequest;
import secretive.exception.ErrorResponse;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class DepartmentResourceTest {

    static PostgreSQLContainer<?> db = new PostgreSQLContainer<>("postgres:17");

    @LocalServerPort
    protected Integer port;

    @Autowired
    protected TestRestTemplate template;

    private String path;

    @BeforeEach
    void pathSetup() {
        path = "http://localhost:%d/department".formatted(port);
    }

    @BeforeAll
    static void setup() {
        db.start();
    }

    @DynamicPropertySource
    static void propertyConfig(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", db::getJdbcUrl);
        registry.add("spring.datasource.password", db::getPassword);
        registry.add("spring.datasource.username", db::getUsername);
    }

    @Test
    void canCreateDepartment() {
        var creationRequest = new DepartmentCreationRequest("dep1");

        var response = template.postForEntity(path, creationRequest, DepartmentDto.class);
        var createdDto = response.getBody();
        var retrievedDto = template
                .getForEntity(path + "/name/" + creationRequest.name(), DepartmentDto.class)
                .getBody();

        assertThat(createdDto).isEqualTo(retrievedDto);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }

    @Test
    void return404WhenRetrievingMissingDepartmentByName() {
        var response = template.getForEntity(path + "/name/missing-dept", ErrorResponse.class);

        assertThat(response.getStatusCode().value()).isEqualTo(404);
        assertThat(response.getBody().errors())
                .anyMatch(it -> it.contains("[department] with name [missing-dept] does not exist"));
    }

    @Test
    void return404WhenRetrievingMissingDepartmentById() {
        UUID randomId = UUID.randomUUID();
        var response = template.getForEntity(path + "/" + randomId, ErrorResponse.class);

        assertThat(response.getStatusCode().value()).isEqualTo(404);
        assertThat(response.getBody().errors())
                .anyMatch(it -> it.contains("[department] with id [%s] does not exist".formatted(randomId)));
    }

    @Test
    void invalidCreationRequestIsRejected() {
        var creationRequest = new DepartmentCreationRequest(null);

        var response = template.postForEntity(path, creationRequest, ErrorResponse.class);

        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(response.getBody().errors())
                .anyMatch(it -> it.contains("[name]: must not be null"));
    }

}
