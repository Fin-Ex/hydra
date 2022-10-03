# --- !Ups

create table if not exists game_objects(
    id serial primary key,
    create_date timestamp default transaction_timestamp(),
    delete_date timestamp
);

create sequence if not exists crests_id_seq;

create table if not exists alliance_crests(
    id int primary key default nextval('crests_id_seq'),
    crest bytea check(octet_length(crest) = 256)
);

create table if not exists alliances(
    id serial primary key,
    "name" varchar(24) unique not null,
    crest_id int references alliance_crests(id) on delete cascade on update cascade
);
create unique index if not exists alliances_name_idx on alliances("name");

create table if not exists clan_crests(
    id int primary key default nextval('crests_id_seq'),
    crest bytea check(octet_length(crest) = 256)
);

create table if not exists clan_large_crests(
    id int primary key default nextval('crests_id_seq'),
    crest bytea check(octet_length(crest) = 2176)
);

create table if not exists clans(
    id serial primary key,
    "name" varchar(24) unique not null,
    alliance_id int references alliances(id) on delete cascade on update cascade,
    crest_id int references clan_crests(id) on delete cascade on update cascade,
    large_crest_id int references clan_large_crests(id) on delete cascade on update cascade
);
create unique index if not exists clans_name_idx on clans("name");
create index if not exists clans_alliance_id_idx on clans(alliance_id);

create table if not exists game_object_clan_components(
    id serial primary key,
    game_object_id int unique not null references game_objects(id) on delete cascade on update cascade,
    clan_id int references clans(id) on delete cascade on update cascade
);
create unique index if not exists game_object_clan_components_game_object_id_idx on game_object_clan_components(game_object_id);
create index if not exists game_object_clan_components_clan_id_idx on game_object_clan_components(clan_id);

create table if not exists game_object_position_components(
    id serial primary key,
    game_object_id int unique not null references game_objects(id) on delete cascade on update cascade,
    x float8 not null,
    y float8 not null,
    z float8 not null
);
create unique index if not exists game_object_position_components_game_object_id_idx on game_object_position_components(game_object_id);

create table if not exists game_object_player_components(
    id serial primary key,
    game_object_id int unique not null references game_objects(id) on delete cascade on update cascade,
    login varchar not null,
# -- human, elf, dElf, orc, dwarf, kamael, etheria
    race int not null check(race >= 0 and race < 7),
# -- male, female, etc
    gender int not null check(gender >= 0 and gender < 3),
# -- fighter/wizard
    appearance_class int not null check(appearance_class >= 0 and appearance_class <= 1),
# -- FIXME m0nster.mind: correct max value
    hair_type int not null check(hair_type >= 0 and hair_type < 10),
# -- FIXME m0nster.mind: correct max value
    hair_color int not null check(hair_color >= 0 and hair_type < 10),
# -- FIXME m0nster.mind: correct max value
    face_type int not null check(face_type >= 0 and face_type < 10),
# -- none, half, flag
    pvp_mode int not null check(pvp_mode >= 0 and pvp_mode < 3) default 0,
    "name" varchar(16) unique not null,
    name_color int not null default -1,
    title varchar(16) not null,
    title_color int not null default -1
);
create unique index if not exists game_object_player_components_game_object_id_idx on game_object_player_components(game_object_id);
create index if not exists game_object_player_components_login_idx on game_object_player_components(login);
create unique index if not exists game_object_player_components_name_idx on game_object_player_components(id);

create table if not exists game_object_parameter_components(
    id serial primary key,
    game_object_id int unique not null references game_objects(id) on delete cascade on update cascade,
    str int not null check(str >= 0 and str <= 100),
    dex int not null check(dex >= 0 and dex <= 100),
    con int not null check(con >= 0 and con <= 100),
    "int" int not null check("int" >= 0 and "int" <= 100),
    wit int not null check(wit >= 0 and wit <= 100),
    men int not null check(men >= 0 and men <= 100),
    luc int not null check(luc >= 0 and luc <= 100),
    cha int not null check(cha >= 0 and cha <= 100)
);
create unique index if not exists game_object_parameter_components_game_object_id_idx on game_object_parameter_components(game_object_id);

create table if not exists game_object_stat_components(
    id serial primary key,
    game_object_id int unique not null references game_objects(id) on delete cascade on update cascade,
    p_atk int not null check(p_atk > 0),
    p_def int not null,
    accuracy int not null,
    evasion int not null,
    attack_speed int not null check(attack_speed > 0),
    critical_rate int not null,
    m_atk int not null check(m_atk > 0),
    m_def int not null,
    cast_speed int not null check(cast_speed > 0),
    magic_critical_rate int not null,
    magic_evasion int not null,
    magic_accuracy int not null
);
create unique index if not exists game_object_stat_components_game_object_id_idx on game_object_stat_components(game_object_id);

create table if not exists game_object_status_components(
    id serial primary key,
    game_object_id int unique not null references game_objects(id) on delete cascade on update cascade,
    hp float8 not null check(hp >= 0),
    max_hp float8 not null check(max_hp > 0),
    mp float8 not null check(mp >= 0),
    max_mp float8 not null check(mp >= 0),
    cp float8 not null check(cp >= 0),
    max_cp float8 not null check(cp >= 0)
);
create unique index if not exists game_object_status_components_game_object_id_idx on game_object_status_components(game_object_id);

insert into game_object_prototypes(id, "name") values (1, 'test_player') on conflict(id) do nothing;
insert into game_object_component_prototypes(id, component, prototype_id, data) values
    (1, 'ru.finex.ws.l2.component.prototype.CoordinatePrototype', 1, '{ "x": 82698, "y": 148638, "z": -3473 }'),
    (2, 'ru.finex.ws.l2.component.prototype.StatusPrototype', 1, '{ "hp": 100, "maxHp": 100, "mp": 100, "maxMp": 100, "cp": 100, "maxCp": 100 }'),
    (3, 'ru.finex.ws.l2.component.prototype.AbnormalPrototype', 1, '{}'),
    (4, 'ru.finex.ws.l2.component.prototype.ClanPrototype', 1, '{}'),
    (5, 'ru.finex.ws.l2.component.prototype.ClassPrototype', 1, '{}'),
    (6, 'ru.finex.ws.l2.component.prototype.ClientPrototype', 1, '{}'),
    (7, 'ru.finex.ws.l2.component.prototype.CollisionPrototype', 1, '{}'),
    (8, 'ru.finex.ws.l2.component.prototype.CubicPrototype', 1, '{}'),
    (9, 'ru.finex.ws.l2.component.prototype.MountPrototype', 1, '{}'),
    (11, 'ru.finex.ws.l2.component.prototype.PlayerPrototype', 1, '{ "race": "HUMAN", "gender": "MALE", "appearanceClass": "FIGHTER", "hairType": 0, "hairColor": 0, "faceType": 0, "nameColor": -1, "titleColor": -1 }'),
    (12, 'ru.finex.ws.l2.component.prototype.RecommendationPrototype', 1, '{}'),
    (13, 'ru.finex.ws.l2.component.prototype.SpeedPrototype', 1, '{}'),
    (14, 'ru.finex.ws.l2.component.prototype.StatePrototype', 1, '{}'),
    (15, 'ru.finex.ws.l2.component.prototype.StorePrototype', 1, '{}'),
    (16, 'ru.finex.ws.l2.component.prototype.VisualEquipPrototype', 1, '{}'),
    (17, 'ru.finex.ws.l2.component.prototype.ParameterPrototype', 1, '{}'),
    (18, 'ru.finex.ws.l2.component.prototype.StatPrototype', 1, '{}')
    on conflict(id) do update set
        component = EXCLUDED.component,
        prototype_id = EXCLUDED.prototype_id,
        data = EXCLUDED.data;

create or replace view game_object_avatars as
    select
        game_object.id,
        player."name",
        player."login",
        coalesce(clan.clan_id, 0) clan_id,
        player.gender,
        player.race,
        player.appearance_class,
        pos.x,
        pos.y,
        pos.z,
        status.hp,
        status.mp,
        player.hair_type,
        player.hair_color,
        player.face_type,
        status.max_hp,
        status.max_mp,
        game_object.delete_date
    from game_objects game_object
    join game_object_player_components player on game_object.id = player.game_object_id
    join game_object_clan_components clan on game_object.id = clan.game_object_id
    join game_object_position_components pos on game_object.id = pos.game_object_id
    join game_object_status_components status on game_object.id = status.game_object_id;

# --- !Downs

drop view if exists game_object_avatars cascade;
delete from game_object_prototypes where id = 1;
drop table if exists game_object_status_components cascade;
drop table if exists game_object_stat_components  cascade;
drop table if exists game_object_parameter_components cascade;
drop table if exists game_object_player_components cascade;
drop table if exists position_components cascade;
drop table if exists game_object_clan_components cascade;
drop table if exists clans cascade;
drop table if exists clan_large_crests cascade;
drop table if exists clan_crests cascade;
drop table if exists alliances cascade;
drop table if exists alliance_crests cascade;
drop sequence if exists crests_id_seq;
drop table if exists game_objects cascade;