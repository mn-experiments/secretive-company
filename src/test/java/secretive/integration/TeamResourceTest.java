package secretive.integration;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import secretive.department.DepartmentDto;
import secretive.department.presentation.DepartmentCreationRequest;
import secretive.exception.ErrorResponse;
import secretive.project.ProjectDto;
import secretive.project.presentation.ProjectCreationRequest;
import secretive.team.TeamDto;
import secretive.team.presentation.TeamCreationRequest;

import static org.assertj.core.api.Assertions.assertThat;

public class TeamResourceTest extends ResourceTest {

    @Test
    void canCreateTeam() {

        var team = createTeam("d1", "p1", "t1");

        assertThat(team.getStatusCode().value()).isEqualTo(200);

        var body = team.getBody();
        assertThat(body.id()).isNotNull();
        assertThat(body.name()).isEqualTo("t1");
        assertThat(body.project().name()).isEqualTo("p1");
    }

    @Test
    void canRetrieveTeam() {
        var createdTeam = createTeam("d1", "p1", "t1").getBody();

        var idRetrievedTeam =
                doGet(teamPathWithId.formatted(createdTeam.id()), TeamDto.class).getBody();

        var nameRetrievedTeam = doGet(teamPathWithName.formatted(createdTeam.name()), TeamDto.class).getBody();

        assertThat(createdTeam).isEqualTo(idRetrievedTeam);
        assertThat(idRetrievedTeam).isEqualTo(nameRetrievedTeam);

    }

    @Test
    void canListTeamsByProject() {
        var team1 = createTeam("d1", "p1", "t1").getBody();
        var project = team1.project();
        var team2 = doPost(teamPath, new TeamCreationRequest("t2", team1.project().id()), TeamDto.class).getBody();

        var teams = doGet(teamPathWithProject.formatted(project.id()), TeamDto[].class).getBody();

        assertThat(teams).containsExactlyInAnyOrder(team1, team2);
    }

    private ResponseEntity<TeamDto> createTeam(String departmentName, String projectName, String teamName) {
        var d = new DepartmentCreationRequest(departmentName);
        var department = doPost(departmentPath, d, DepartmentDto.class).getBody();

        var p = new ProjectCreationRequest(projectName, department.id());
        var project = doPost(projectPath, p, ProjectDto.class).getBody();

        var t = new TeamCreationRequest(teamName, project.id());

        return doPost(teamPath, t, TeamDto.class);

    }
}
