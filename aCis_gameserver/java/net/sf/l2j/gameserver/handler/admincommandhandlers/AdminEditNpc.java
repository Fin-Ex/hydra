package net.sf.l2j.gameserver.handler.admincommandhandlers;


import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import net.sf.l2j.commons.lang.StringUtil;

import net.sf.l2j.gameserver.data.ItemTable;
import net.sf.l2j.gameserver.data.NpcTable;
import net.sf.l2j.gameserver.data.manager.BuyListManager;
import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.instance.Merchant;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate.SkillType;
import net.sf.l2j.gameserver.model.buylist.NpcBuyList;
import net.sf.l2j.gameserver.model.buylist.Product;
import net.sf.l2j.gameserver.model.item.DropCategory;
import net.sf.l2j.gameserver.model.item.DropData;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.scripting.EventType;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.templates.skills.ESkillType;

public class AdminEditNpc implements IAdminCommandHandler {

	private static final int PAGE_LIMIT = 20;

	private static final String[] ADMIN_COMMANDS = {
		"admin_show_droplist",
		"admin_show_scripts",
		"admin_show_shop",
		"admin_show_shoplist",
		"admin_show_skilllist"
	};

	@Override
	public boolean useAdminCommand(String command, Player activeChar) {
		final StringTokenizer st = new StringTokenizer(command, " ");
		st.nextToken();

		if (command.startsWith("admin_show_shoplist")) {
			try {
				showShopList(activeChar, Integer.parseInt(st.nextToken()));
			} catch (Exception e) {
				activeChar.sendMessage("Usage: //show_shoplist <list_id>");
			}
		} else if (command.startsWith("admin_show_shop")) {
			try {
				showShop(activeChar, Integer.parseInt(st.nextToken()));
			} catch (Exception e) {
				activeChar.sendMessage("Usage: //show_shop <npc_id>");
			}
		} else if (command.startsWith("admin_show_droplist")) {
			try {
				int npcId = Integer.parseInt(st.nextToken());
				int page = (st.hasMoreTokens()) ? Integer.parseInt(st.nextToken()) : 1;

				showNpcDropList(activeChar, npcId, page);
			} catch (Exception e) {
				activeChar.sendMessage("Usage: //show_droplist <npc_id> [<page>]");
			}
		} else if (command.startsWith("admin_show_skilllist")) {
			try {
				showNpcSkillList(activeChar, Integer.parseInt(st.nextToken()));
			} catch (Exception e) {
				activeChar.sendMessage("Usage: //show_skilllist <npc_id>");
			}
		} else if (command.startsWith("admin_show_scripts")) {
			try {
				showScriptsList(activeChar, Integer.parseInt(st.nextToken()));
			} catch (Exception e) {
				activeChar.sendMessage("Usage: //show_scripts <npc_id>");
			}
		}

		return true;
	}

	private static void showShopList(Player activeChar, int listId) {
		final NpcBuyList buyList = BuyListManager.getInstance().getBuyList(listId);
		if (buyList == null) {
			activeChar.sendMessage("BuyList template is unknown for id: " + listId + ".");
			return;
		}

		final StringBuilder sb = new StringBuilder(500);
		StringUtil.append(sb, "<html><body><center><font color=\"LEVEL\">", NpcTable.getInstance().getTemplate(buyList.getNpcId()).getName(), " (", buyList.getNpcId(), ") buylist id: ", buyList.getListId(), "</font></center><br><table width=\"100%\"><tr><td width=200>Item</td><td width=80>Price</td></tr>");

		for (Product product : buyList.getProducts()) {
			StringUtil.append(sb, "<tr><td>", product.getItem().getName(), "</td><td>", product.getPrice(), "</td></tr>");
		}

		sb.append("</table></body></html>");

		final NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setHtml(sb.toString());
		activeChar.sendPacket(html);
	}

	private static void showShop(Player activeChar, int npcId) {
		final List<NpcBuyList> buyLists = BuyListManager.getInstance().getBuyListsByNpcId(npcId);
		if (buyLists.isEmpty()) {
			activeChar.sendMessage("No buyLists found for id: " + npcId + ".");
			return;
		}

		final StringBuilder sb = new StringBuilder(500);
		StringUtil.append(sb, "<html><title>Merchant Shop Lists</title><body>");

		if (activeChar.getTarget() instanceof Merchant) {
			Npc merchant = (Npc) activeChar.getTarget();
			int taxRate = merchant.getCastle().getTaxPercent();

			StringUtil.append(sb, "<center><font color=\"LEVEL\">", merchant.getName(), " (", npcId, ")</font></center><br>Tax rate: ", taxRate, "%");
		}

		StringUtil.append(sb, "<table width=\"100%\">");

		for (NpcBuyList buyList : buyLists) {
			StringUtil.append(sb, "<tr><td><a action=\"bypass -h admin_show_shoplist ", buyList.getListId(), " 1\">Buylist id: ", buyList.getListId(), "</a></td></tr>");
		}

		StringUtil.append(sb, "</table></body></html>");

		final NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setHtml(sb.toString());
		activeChar.sendPacket(html);
	}

	private static void showNpcDropList(Player activeChar, int npcId, int page) {
		final NpcTemplate npcData = NpcTable.getInstance().getTemplate(npcId);
		if (npcData == null) {
			activeChar.sendMessage("Npc template is unknown for id: " + npcId + ".");
			return;
		}

		final StringBuilder sb = new StringBuilder(2000);
		StringUtil.append(sb, "<html><title>Show droplist page ", page, "</title><body><center><font color=\"LEVEL\">", npcData.getName(), " (", npcId, ")</font></center><br>");

		if (!npcData.getDropData().isEmpty()) {
			sb.append("Drop type legend: <font color=\"3BB9FF\">Drop</font> | <font color=\"00ff00\">Sweep</font><br><table><tr><td width=25>cat.</td><td width=255>item</td></tr>");

			int myPage = 1;
			int i = 0;
			int shown = 0;
			boolean hasMore = false;

			for (DropCategory cat : npcData.getDropData()) {
				if (shown == PAGE_LIMIT) {
					hasMore = true;
					break;
				}

				for (DropData drop : cat.getAllDrops()) {
					if (myPage != page) {
						i++;
						if (i == PAGE_LIMIT) {
							myPage++;
							i = 0;
						}
						continue;
					}

					if (shown == PAGE_LIMIT) {
						hasMore = true;
						break;
					}

					StringUtil.append(sb, "<tr><td><font color=\"", ((cat.isSweep()) ? "00FF00" : "3BB9FF"), "\">", cat.getCategoryType(), "</td><td>", ItemTable.getInstance().getTemplate(drop.getItemId()).getName(), " (", drop.getItemId(), ")</td></tr>");
					shown++;
				}
			}

			sb.append("</table><table width=\"100%\" bgcolor=666666><tr>");

			if (page > 1) {
				StringUtil.append(sb, "<td width=120><a action=\"bypass -h admin_show_droplist ", npcId, " ", page - 1, "\">Prev Page</a></td>");
				if (!hasMore) {
					StringUtil.append(sb, "<td width=100>Page ", page, "</td><td width=70></td></tr>");
				}
			}

			if (hasMore) {
				if (page <= 1) {
					sb.append("<td width=120></td>");
				}

				StringUtil.append(sb, "<td width=100>Page ", page, "</td><td width=70><a action=\"bypass -h admin_show_droplist ", npcId, " ", page + 1, "\">Next Page</a></td></tr>");
			}
			sb.append("</table>");
		} else {
			sb.append("This NPC has no drops.");
		}

		sb.append("</body></html>");

		final NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setHtml(sb.toString());
		activeChar.sendPacket(html);
	}

	private static void showNpcSkillList(Player activeChar, int npcId) {
		final NpcTemplate npcData = NpcTable.getInstance().getTemplate(npcId);
		if (npcData == null) {
			activeChar.sendMessage("Npc template is unknown for id: " + npcId + ".");
			return;
		}

		final StringBuilder sb = new StringBuilder(500);
		StringUtil.append(sb, "<html><body><center><font color=\"LEVEL\">", npcData.getName(), " (", npcId, ") skills</font></center><br>");

		if (!npcData.getSkills().isEmpty()) {
			SkillType type = null; // Used to see if we moved of type.

			// For any type of SkillType
			for (Map.Entry<SkillType, List<L2Skill>> entry : npcData.getSkills().entrySet()) {
				if (type != entry.getKey()) {
					type = entry.getKey();
					StringUtil.append(sb, "<br><font color=\"LEVEL\">", type.name(), "</font><br1>");
				}

				for (L2Skill skill : entry.getValue()) {
					StringUtil.append(sb, ((skill.getSkillType() == ESkillType.NOTDONE) ? ("<font color=\"777777\">" + skill.getName() + "</font>") : skill.getName()), " [", skill.getId(), "-", skill.getLevel(), "]<br1>");
				}
			}
		} else {
			sb.append("This NPC doesn't hold any skill.");
		}

		sb.append("</body></html>");

		final NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setHtml(sb.toString());
		activeChar.sendPacket(html);
	}

	private static void showScriptsList(Player activeChar, int npcId) {
		final NpcTemplate npcData = NpcTable.getInstance().getTemplate(npcId);
		if (npcData == null) {
			activeChar.sendMessage("Npc template is unknown for id: " + npcId + ".");
			return;
		}

		final StringBuilder sb = new StringBuilder(500);
		StringUtil.append(sb, "<html><body><center><font color=\"LEVEL\">", npcData.getName(), " (", npcId, ")</font></center><br>");

		if (!npcData.getEventQuests().isEmpty()) {
			EventType type = null; // Used to see if we moved of type.

			// For any type of EventType
			for (Map.Entry<EventType, List<Quest>> entry : npcData.getEventQuests().entrySet()) {
				if (type != entry.getKey()) {
					type = entry.getKey();
					StringUtil.append(sb, "<br><font color=\"LEVEL\">", type.name(), "</font><br1>");
				}

				for (Quest quest : entry.getValue()) {
					StringUtil.append(sb, quest.getName(), "<br1>");
				}
			}
		} else {
			sb.append("This NPC isn't affected by scripts.");
		}

		sb.append("</body></html>");

		final NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setHtml(sb.toString());
		activeChar.sendPacket(html);
	}

	@Override
	public String[] getAdminCommandList() {
		return ADMIN_COMMANDS;
	}
}
