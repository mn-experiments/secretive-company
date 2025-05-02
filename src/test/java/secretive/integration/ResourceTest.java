package secretive.integration;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public abstract class ResourceTest {

    private static PostgreSQLContainer<?> db = new PostgreSQLContainer<>("postgres:17");

    @LocalServerPort
    protected Integer port;

    @Autowired
    protected TestRestTemplate template;

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

}
