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
    hair_type int not null check(hair_type >= 0 and hair_type < 7),
    hair_color int not null check(hair_color >= 0 and hair_color < 4),
    face_type int not null check(face_type >= 0 and face_type < 3),
# -- none, half, flag
    pvp_mode int not null check(pvp_mode >= 0 and pvp_mode < 3) default 0,
    "name" varchar(16) unique not null,
    name_color int not null default -1,
    title varchar(16) not null default '',
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
    critical_rate int not null,
    m_atk int not null check(m_atk > 0),
    m_def int not null,
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

create table if not exists game_object_class_components(
    id serial primary key,
    game_object_id int not null references game_objects(id) on delete cascade on update cascade,
    class_id int not null check(class_id >= 0),
    "level" int not null check("level" > 0) default 1,
    exp bigint not null check(exp >= 0) default 0,
    is_active int not null check(is_active = 0 or is_active = 1) default 1
);
create index if not exists game_object_class_components_game_object_id_idx on game_object_class_components(game_object_id);

do $$
declare
    abstract_id int;;
    abstract_race_id int;;
    abstract_class_id int;;
    proto_id int;;
begin
    insert into game_object_prototypes("name") values ('player_abstract') returning id into abstract_id;;
    insert into game_object_component_prototypes(component, prototype_id, data) values
        ('ru.finex.ws.l2.component.prototype.PlayerPrototype', abstract_id, '{ "hairType": 0, "hairColor": 0, "faceType": 0, "nameColor": -1, "titleColor": -1 }'),
        ('ru.finex.ws.l2.component.prototype.StatPrototype', abstract_id, '{ "accuracy": 0, "evasion": 0, "magicEvasion": 0, "magicAccuracy": 0 }'),
        ('ru.finex.ws.l2.component.prototype.ClientPrototype', abstract_id, '{}'),
        ('ru.finex.ws.l2.component.prototype.AbnormalPrototype', abstract_id, '{}'),
        ('ru.finex.ws.l2.component.prototype.ClanPrototype', abstract_id, '{}'),
        ('ru.finex.ws.l2.component.prototype.StatePrototype', abstract_id, '{}'),
        ('ru.finex.ws.l2.component.prototype.StorePrototype', abstract_id, '{}'),
        ('ru.finex.ws.l2.component.prototype.CubicPrototype', abstract_id, '{}'),
        ('ru.finex.ws.l2.component.prototype.MountPrototype', abstract_id, '{}'),
        ('ru.finex.ws.l2.component.prototype.RecommendationPrototype', abstract_id, '{}'),
        ('ru.finex.ws.l2.component.prototype.VisualEquipPrototype', abstract_id, '{}');;

    insert into game_object_prototypes("name", parent_id) values ('player_abstract_human', abstract_id) returning id into abstract_race_id;;
    insert into game_object_component_prototypes(component, prototype_id, data) values
        ('ru.finex.ws.l2.component.prototype.CoordinatePrototype', abstract_race_id, '{ "x": -114539, "y": 260101, "z": -1192 }'),
        ('ru.finex.ws.l2.component.prototype.PlayerPrototype', abstract_race_id, '{ "race": "HUMAN" }'),
        ('ru.finex.ws.l2.component.prototype.SpeedPrototype', abstract_race_id, '{ "attackSpeed": 300, "castSpeed": 333 }'),
        ('ru.finex.ws.l2.component.prototype.StatPrototype', abstract_race_id, '{ "criticalRate": 4, "magicCriticalRate": 5 }');;

    insert into game_object_prototypes("name", parent_id) values ('player_human_fighter', abstract_race_id) returning id into abstract_class_id;;
    insert into game_object_component_prototypes(component, prototype_id, data) values
        ('ru.finex.ws.l2.component.prototype.StatusPrototype', abstract_class_id, '{ "hp": 80, "maxHp": 80, "mp": 30, "maxMp": 30, "cp": 32, "maxCp": 32 }'),
        ('ru.finex.ws.l2.component.prototype.ClassPrototype', abstract_class_id, '{ "classId": 0 }'),
        ('ru.finex.ws.l2.component.prototype.PlayerPrototype', abstract_class_id, '{ "appearanceClass": "FIGHTER" }'),
        ('ru.finex.ws.l2.component.prototype.SpeedPrototype', abstract_class_id, '{ "walkSpeed": 80, "runSpeed": 132, "swimSpeed": 50, "attackSpeed": 300, "castSpeed": 333 }'),
        ('ru.finex.ws.l2.component.prototype.ParameterPrototype', abstract_class_id, '{ "str": 88, "dex": 55, "con": 82, "int": 39, "wit": 39, "men": 38, "luc": 34, "cha": 41 }'),
        ('ru.finex.ws.l2.component.prototype.StatPrototype', abstract_class_id, '{ "pAtk": 4, "mAtk": 6, "pDef": 80, "mDef": 41 }');;

    insert into game_object_prototypes("name", parent_id) values ('player_human_fighter_male', abstract_class_id) returning id into proto_id;;
    insert into game_object_component_prototypes(component, prototype_id, data) values
        ('ru.finex.ws.l2.component.prototype.ColliderPrototype', proto_id, '{ "width": 9, "height": 23 }'),
        ('ru.finex.ws.l2.component.prototype.PlayerPrototype', proto_id, '{ "gender": "MALE" }');;

    insert into game_object_prototypes("name", parent_id) values ('player_human_fighter_female', abstract_class_id) returning id into proto_id;;
    insert into game_object_component_prototypes(component, prototype_id, data) values
        ('ru.finex.ws.l2.component.prototype.ColliderPrototype', proto_id, '{ "width": 8, "height": 23.5 }'),
        ('ru.finex.ws.l2.component.prototype.PlayerPrototype', proto_id, '{ "gender": "FEMALE" }');;

    insert into game_object_prototypes("name", parent_id) values ('player_human_mystic', abstract_race_id) returning id into abstract_class_id;;
    insert into game_object_component_prototypes(component, prototype_id, data) values
        ('ru.finex.ws.l2.component.prototype.StatusPrototype', abstract_class_id, '{ "hp": 101, "maxHp": 101, "mp": 40, "maxMp": 40, "cp": 50.5, "maxCp": 50.5 }'),
        ('ru.finex.ws.l2.component.prototype.ClassPrototype', abstract_class_id, '{ "classId": 10 }'),
        ('ru.finex.ws.l2.component.prototype.PlayerPrototype', abstract_class_id, '{ "appearanceClass": "WIZARD" }'),
        ('ru.finex.ws.l2.component.prototype.SpeedPrototype', abstract_class_id, '{ "walkSpeed": 78, "runSpeed": 124, "swimSpeed": 50 }'),
        ('ru.finex.ws.l2.component.prototype.ParameterPrototype', abstract_class_id, '{ "str": 38, "dex": 27, "con": 41, "int": 79, "wit": 78, "men": 78, "luc": 34, "cha": 41 }'),
        ('ru.finex.ws.l2.component.prototype.StatPrototype', abstract_class_id, '{ "pAtk": 3, "mAtk": 6, "pDef": 54, "mDef": 41 }');;

    insert into game_object_prototypes("name", parent_id) values ('player_human_mystic_male', abstract_class_id) returning id into proto_id;;
    insert into game_object_component_prototypes(component, prototype_id, data) values
        ('ru.finex.ws.l2.component.prototype.ColliderPrototype', proto_id, '{ "width": 7.5, "height": 22.8 }'),
        ('ru.finex.ws.l2.component.prototype.PlayerPrototype', proto_id, '{ "gender": "MALE" }');;

    insert into game_object_prototypes("name", parent_id) values ('player_human_mystic_female', abstract_class_id) returning id into proto_id;;
    insert into game_object_component_prototypes(component, prototype_id, data) values
        ('ru.finex.ws.l2.component.prototype.ColliderPrototype', proto_id, '{ "width": 6.5, "height": 22.5 }'),
        ('ru.finex.ws.l2.component.prototype.PlayerPrototype', proto_id, '{ "gender": "FEMALE" }');;
end $$;

create or replace view game_object_avatars as
    select
        game_object.id,
        player."name",
        player."login",
        coalesce(clan.clan_id, 0) clan_id,
        player.gender,
        player.race,
        player.appearance_class,
        class.class_id,
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
    join game_object_status_components status on game_object.id = status.game_object_id
    join game_object_class_components class on game_object.id = class.game_object_id and class.is_active = 1;

# --- !Downs

drop view if exists game_object_avatars cascade;
drop table if exists game_object_class_components cascade;
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