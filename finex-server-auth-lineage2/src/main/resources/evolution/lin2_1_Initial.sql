# --- !Ups

create table if not exists server_list(
    id serial primary key,
    host varchar not null,
    port int not null check(port > 0 and port < 65536),
    age_limit int not null default 0,
    is_pvp bool not null default false,
    max_clients int not null check(max_clients > 0 and max_clients < 65536) default 2000,
    is_normal bool not null default true,
    is_relax bool not null default false,
    is_public_test bool not null default false,
    is_no_label bool not null default false,
    is_denied_avatar_creation bool not null default false,
    is_event bool not null default false,
    is_free bool not null default false,
    is_brackets bool not null default false
);

create table if not exists user_information(
    id bigserial primary key,
    user_id bigint unique not null references users(id) on delete cascade on update cascade,
    last_server int references server_list(id) on delete restrict on update cascade
);

# --- !Downs

drop table if exists user_information;
drop table if exists server_list;