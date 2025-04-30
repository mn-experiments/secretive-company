create table department(
    id uuid default gen_random_uuid() primary key,
    name varchar(100),
    created_at timestamptz,
    updated_at timestamptz
)