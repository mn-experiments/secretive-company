package secretive.concept;

import org.springframework.stereotype.Service;
import secretive.department.mutation.Department;
import secretive.department.mutation.DepartmentReferenceFactory;
import secretive.project.mutation.Project;
import secretive.project.mutation.ProjectReferenceFactory;
import secretive.team.mutation.Team;
import secretive.team.mutation.TeamReferenceFactory;

import java.util.UUID;

@Service
public class EntityReferenceFactory {
    private final DepartmentReferenceFactory departmentReferenceFactory;
    private final ProjectReferenceFactory projectReferenceFactory;
    private final TeamReferenceFactory teamReferenceFactory;

    public EntityReferenceFactory(DepartmentReferenceFactory departmentReferenceFactory,
                                  ProjectReferenceFactory projectReferenceFactory,
                                  TeamReferenceFactory teamReferenceFactory) {
        this.departmentReferenceFactory = departmentReferenceFactory;
        this.projectReferenceFactory = projectReferenceFactory;
        this.teamReferenceFactory = teamReferenceFactory;
    }

    public EntityReference<Department> getDepartmentReference(UUID id) {
        return departmentReferenceFactory.getReference(id);
    }

    public EntityReference<Project> getProjectReference(UUID id) {
        return projectReferenceFactory.getReference(id);
    }

    public EntityReference<Team> getTeamReference(UUID id) {
        return teamReferenceFactory.getReference(id);
    }
}
