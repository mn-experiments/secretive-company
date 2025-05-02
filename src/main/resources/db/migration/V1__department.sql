create table department(
    id uuid default gen_random_uuid(),
    name varchar(100) unique not null,
    created_at timestamptz,
    updated_at timestamptz,
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