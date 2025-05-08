create table department_department_exclusion(
    department_id uuid not null,
    excluded_department_id uuid not null,
    primary key(department_id, excluded_department_id),
    constraint fk_department
        foreign key(department_id)
        references department(id)
        on delete cascade,
    constraint fk_excluded_department
        foreign key(excluded_department_id)
        references department(id)
);

create table department_project_exclusion(
    department_id uuid not null,
    excluded_project_id uuid not null,
    primary key(department_id, excluded_project_id),
        constraint fk_department
            foreign key(department_id)
            references department(id)
            on delete cascade,
        constraint fk_excluded_project
            foreign key(excluded_project_id)
            references project(id)
);

create table department_team_exclusion(
    department_id uuid not null,
    excluded_team_id uuid not null,
    primary key(department_id, excluded_team_id),
        constraint fk_department
            foreign key(department_id)
            references department(id)
            on delete cascade,
        constraint fk_excluded_team
            foreign key(excluded_team_id)
            references team(id)
);
