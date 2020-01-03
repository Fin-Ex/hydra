/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.l2j.gameserver.handler;

/**
 *
 * @author finfan
 */
public enum EAdminCommandType {
	admin_admin,
	admin_admin1,
	admin_admin2,
	admin_admin3,
	admin_admin4,
	admin_gmlist,
	admin_kill,
	admin_silence,
	admin_tradeoff,
	admin_reload,
	admin_announce,
	admin_ann,
	admin_say,
	admin_ban, // returns ban commands
	admin_ban_acc,
	admin_ban_char,
	admin_ban_chat,
	admin_unban, // returns unban commands
	admin_unban_acc,
	admin_unban_char,
	admin_unban_chat,
	admin_jail,
	admin_unjail,
	admin_bkpage,
	admin_bk,
	admin_delbk,
	admin_getbuffs,
	admin_stopbuff,
	admin_stopallbuffs,
	admin_areacancel,
	admin_removereuse,
	admin_reload_cache_path,
	admin_reload_cache_file,
	admin_camera,
	admin_cameramode,
	admin_itemcreate,
	admin_create_item,
	admin_create_set,
	admin_create_coin,
	admin_reward_all,
	admin_cw_info,
	admin_cw_remove,
	admin_cw_goto,
	admin_cw_add,
	admin_cw_info_menu,
	admin_delete,
	admin_open,
	admin_close,
	admin_openall,
	admin_closeall,
	admin_changelvl, // edit player access level
	admin_edit_character,
	admin_current_player,
	admin_setkarma, // sets karma of target char to any amount. //setkarma <karma>
	admin_character_info, // given a player name, displays an information window
	admin_show_characters, // list of characters
	admin_find_character, // find a player by his name or a part of it (case-insensitive)
	admin_find_ip, // find all the player connections from a given IPv4 number
	admin_find_account, // list all the characters from an account (useful for GMs w/o DB access)
	admin_find_dualbox, // list all IPs with more than 1 char logged in (dualbox)
	admin_rec, // gives recommendation points
	admin_settitle, // changes char's title
	admin_setname, // changes char's name
	admin_setsex, // changes char's sex
	admin_setcolor, // change char name's color
	admin_settcolor, // change char title's color
	admin_setclass, // changes char's classId
	admin_summon_info, // displays an information window about target summon
	admin_unsummon, // unsummon target's pet/summon
	admin_summon_setlvl, // set the pet's level
	admin_show_pet_inv, // show pet's inventory
	admin_fullfood, // fulfills a pet's food bar
	admin_party_info, // find party infos of targeted character, if any
	admin_clan_info, // find clan infos of the character, if any
	admin_remove_clan_penalty, // removes clan penalties
	admin_show_droplist,
	admin_show_scripts,
	admin_show_shop,
	admin_show_shoplist,
	admin_show_skilllist,
	admin_hide,
	admin_earthquake,
	admin_earthquake_menu,
	admin_gmspeed,
	admin_gmspeed_menu,
	admin_unpara_all,
	admin_para_all,
	admin_unpara,
	admin_para,
	admin_unpara_all_menu,
	admin_para_all_menu,
	admin_unpara_menu,
	admin_para_menu,
	admin_social,
	admin_social_menu,
	admin_effect,
	admin_effect_menu,
	admin_abnormal,
	admin_abnormal_menu,
	admin_jukebox,
	admin_play_sound,
	admin_atmosphere,
	admin_atmosphere_menu,
	admin_seteh, // 6
	admin_setec, // 10
	admin_seteg, // 9
	admin_setel, // 11
	admin_seteb, // 12
	admin_setew, // 7
	admin_setes, // 8
	admin_setle, // 1
	admin_setre, // 2
	admin_setlf, // 4
	admin_setrf, // 5
	admin_seten, // 3
	admin_setun, // 0
	admin_setba, // 13
	admin_enchant,
	admin_add_exp_sp_to_character,
	admin_add_exp_sp,
	admin_remove_exp_sp,
	admin_geo_bug,
	admin_geo_pos,
	admin_geo_see,
	admin_geo_move,
	admin_path_find,
	admin_path_info,
	admin_gm,
	admin_gmchat,
	admin_gmchat_menu,
	admin_heal,
	admin_help,
	admin_invul,
	admin_setinvul,
	admin_character_disconnect,
	admin_kick,
	admin_kick_non_gm,
	admin_knownlist,
	admin_knownlist_page,
	admin_addlevel,
	admin_setlevel,
	admin_server,
	admin_server_shutdown,
	admin_server_restart,
	admin_server_abort,
	admin_server_gm_only,
	admin_server_all,
	admin_server_max_player,
	admin_mammon_find,
	admin_mammon_respawn,
	admin_manor,
	admin_char_manage,
	admin_teleport_character_to_menu,
	admin_addseq,
	admin_playseqq,
	admin_delsequence,
	admin_editsequence,
	admin_addsequence,
	admin_playsequence,
	admin_movie,
	admin_updatesequence,
	admin_broadcast,
	admin_playmovie,
	admin_broadmovie,
	admin_endoly,
	admin_sethero,
	admin_setnoble,
	admin_forge,
	admin_forge2,
	admin_forge3,
	admin_msg,
	admin_view_petitions,
	admin_view_petition,
	admin_accept_petition,
	admin_reject_petition,
	admin_reset_petitions,
	admin_pledge,
	admin_polymorph,
	admin_unpolymorph,
	admin_polymorph_menu,
	admin_unpolymorph_menu,
	admin_res,
	admin_res_monster,
	admin_ride,
	admin_unride,
	admin_buy,
	admin_gmshop,
	admin_siege,
	admin_add_attacker,
	admin_add_defender,
	admin_list_siege_clans,
	admin_clear_siege_list,
	admin_move_defenders,
	admin_spawn_doors,
	admin_endsiege,
	admin_startsiege,
	admin_setcastle,
	admin_removecastle,
	admin_clanhall,
	admin_clanhallset,
	admin_clanhalldel,
	admin_clanhallopendoors,
	admin_clanhallclosedoors,
	admin_clanhallteleportself,
	admin_reset_certificates,
	admin_show_skills,
	admin_remove_skills,
	admin_skill_list,
	admin_skill_index,
	admin_add_skill,
	admin_remove_skill,
	admin_get_skills,
	admin_reset_skills,
	admin_give_all_skills,
	admin_remove_all_skills,
	admin_add_clan_skill,
	admin_st,
	admin_list_spawns,
	admin_show_spawns,
	admin_spawn,
	admin_spawn_index,
	admin_unspawnall,
	admin_respawnall,
	admin_spawn_reload,
	admin_npc_index,
	admin_spawn_once,
	admin_show_npcs,
	admin_spawnnight,
	admin_spawnday,
	admin_spawnfence,
	admin_deletefence,
	admin_listfence,
	admin_target,
	admin_runmod,
	admin_instant_move,
	admin_tele,
	admin_tele_areas,
	admin_goto,
	admin_teleportto, // deprecated
	admin_recall,
	admin_recall_party,
	admin_recall_clan,
	admin_move_to,
	admin_sendhome,
	admin_zone_check,
	admin_zone_visual;
	
	public String getCommand() {
		return name().substring(6);
	}
}
