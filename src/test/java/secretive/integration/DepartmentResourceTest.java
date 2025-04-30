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

}
