create table department(
    id uuid not null,
    name varchar(100) unique not null,
    created_at timestamptz not null,
    updated_at timestamptz not null,
    primary key(id)
);

create table project(
    id uuid,
    department_id uuid not null,
    name varchar(100) unique not null,
    created_at timestamptz not null,
    updated_at timestamptz not null,
    primary key(id),
    constraint fk_department
        foreign key(department_id)
        references department(id)
        on delete cascade
        on update no action
);

create table team(
    id uuid not null,
    project_id uuid not null,
    name varchar(100) unique not null,
    created_at timestamptz not null,
    updated_at timestamptz not null,
    primary key(id),
    constraint fk_project
        foreign key(project_id)
        references project(id)
        on delete cascade
        on update no action
);