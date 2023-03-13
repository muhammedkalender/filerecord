create extension if not exists  "uuid-ossp";

create table if not exists  public.t_file_records
(
    id         uuid         not null
        primary key,
    created_at timestamp    not null,
    deleted_at timestamp,
    extension  varchar(255),
    name       varchar(255) not null,
    path       varchar(255) not null,
    size_inkb  bigint       not null,
    updated_at timestamp    not null
);

alter table public.t_file_records
    owner to postgres;

create table if not exists  public.t_member
(
    id        uuid         not null
        primary key,
    full_name varchar(32)  not null,
    password  varchar(255) not null,
    username  varchar(32)  not null
        constraint uk_member_username
            unique
);

alter table public.t_member
    owner to postgres;

create table if not exists public.t_refresh_token
(
    id            uuid         not null
        primary key,
    exp_date      timestamp    not null,
    refresh_token varchar(255) not null
        constraint uk_refresh_token
            unique,
    member_id     uuid         not null
        constraint fk_member_refresh_token
            references public.t_member
);

alter table public.t_refresh_token
    owner to postgres;

INSERT  INTO public.t_member (id, full_name, password, username)
VALUES ('1741ffed-54d1-420c-a283-c2324bac5c19', 'Muhammed Kalender',
        '$2b$12$WUCwZE7uX0rqKkjrZlO0X.hOmZLbVsVoMLMIBiC0.IpDP6LDfDG5C', 'admin')
ON CONFLICT 
    DO NOTHING;
