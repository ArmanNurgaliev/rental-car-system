create table users (
    id                      bigserial primary key,
    email                   varchar(255),
    password                varchar(255),
    name                    varchar(255),
    phone                   varchar(255),
    status                  varchar(255),
    location_id             bigint,
    street_address          varchar(255),
    city                    varchar(255),
    country                 varchar(255),
    state                   varchar(255),
    zip_code                varchar(255),
        constraint users_status_check
            check ((status)::text = ANY
        ((ARRAY ['ACTIVE'::character varying, 'CLOSED'::character varying, 'CANCELED'::character varying, 'BLACKLISTED'::character varying, 'BLOCKED'::character varying])::text[]))
);

create table roles (
    role_id serial primary key,
    name    varchar(255)
);

create table users_roles (
    role_id integer not null
        constraint roles_users
            references roles,
    user_id bigint  not null
        constraint user_roles
            references users,
    primary key (role_id, user_id)
);