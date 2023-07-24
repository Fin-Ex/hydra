# Hydra World Server
## Create avatar (obsolete)
```sql
do $$ 
declare
	object_id int;
begin 
	insert into game_objects(delete_date) values (null) returning id into object_id;
	insert into game_object_clan_components(game_object_id) values(object_id);
	insert into game_object_parameter_components(game_object_id, str, dex, con, "int", wit, men, luc, cha) values 
		(object_id, 50, 50, 50, 50, 50, 50, 50, 50);
	insert into game_object_player_components(game_object_id, login, race, gender, appearance_class, hair_type, hair_color, face_type, "name", title) values 
		(object_id, 'test', 0, 0, 0, 0, 0, 0, 'test', '');
	insert into game_object_position_components(game_object_id, x, y, z) values 
		(object_id, 82698, 148638, -3473);
	insert into game_object_stat_components(game_object_id, p_atk, p_def, accuracy, evasion, attack_speed, critical_rate, m_atk, m_def, cast_speed, magic_critical_rate, magic_evasion, magic_accuracy) values 
		(object_id, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200);
	insert into game_object_status_components(game_object_id, hp, max_hp, mp, max_mp, cp, max_cp) values 
		(object_id, 100, 100, 100, 100, 100, 100);
end$$;
```