package secretive.integration;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class ResourceTest {

    private static PostgreSQLContainer<?> db = new PostgreSQLContainer<>("postgres:17");

    @LocalServerPort
    protected Integer port;

    @Autowired
    private TestRestTemplate template;

    protected String teamPath;
    protected String teamPathWithId;
    protected String teamPathWithName;
    protected String teamPathWithProject;
    protected String projectPath;
    protected String projectPathWithId;
    protected String projectPathWithName;
    protected String projectPathWithDepartment;

    protected String departmentPath;

    private String[] tables = {"department", "project", "team"};

    @Autowired
    private JdbcClient jdbcTemplate;

    @BeforeAll
    static void setup() {
        db.start();
    }

    /**
     * <p>
     * This is needed because the specific {@code webEnvironment} of this{@link SpringBootTest}
     * uses a random port, in such a case a real http server is created and run on a different thread,
     * not on the thread where the DB transactions happen.
     * </p>
     *
     * @see <a href=https://docs.spring.io/spring-boot/reference/testing/spring-boot-applications.html>
     * spring docs
     * </a>
     */
    @BeforeEach
    void dbCleanup() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, tables);

        teamPath = "http://localhost:%d/team".formatted(port);
        teamPathWithId = teamPath + "/%s";
        teamPathWithName = teamPath + "/name/%s";
        teamPathWithProject = teamPath + "?projectId=%s";

        projectPath = "http://localhost:%d/project".formatted(port);
        projectPathWithId = projectPath + "/%s";
        projectPathWithName = projectPath + "/name/%s";
        projectPathWithDepartment = projectPath + "?departmentId=%s";

        departmentPath = "http://localhost:%d/department".formatted(port);

    }

    @DynamicPropertySource
    static void propertyConfig(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", db::getJdbcUrl);
        registry.add("spring.datasource.password", db::getPassword);
        registry.add("spring.datasource.username", db::getUsername);
    }

    protected <T> ResponseEntity<T> doPost(String p, Object body, Class<T> clazz) {
        return template.postForEntity(p, body, clazz);
    }

    protected <T> ResponseEntity<T> doGet(String path, Class<T> clazz) {
        return template.getForEntity(path, clazz);
    }
}
