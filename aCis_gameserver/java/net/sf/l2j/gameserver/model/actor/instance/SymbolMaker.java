package net.sf.l2j.gameserver.model.actor.instance;

import org.slf4j.LoggerFactory;

import net.sf.finex.model.dye.DyeComponent;
import net.sf.finex.data.tables.DyeTable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.HennaEquipList;
import net.sf.l2j.gameserver.network.serverpackets.HennaRemoveList;

public class SymbolMaker extends Folk {

	public SymbolMaker(int objectID, NpcTemplate template) {
		super(objectID, template);
	}

	@Override
	public void onBypassFeedback(Player player, String command) {
		switch (command) {
			case "Draw":
				player.sendPacket(new HennaEquipList(player, DyeTable.getInstance().holder()));
				break;

			case "RemoveList":
				boolean hasHennas = false;
				final DyeComponent dye = player.getComponent(DyeComponent.class);
				for (int i = 0; i < 3; i++) {
					if (dye.hasDye(i)) {
						hasHennas = true;
					}
				}

				if (hasHennas) {
					player.sendPacket(new HennaRemoveList(player));
				} else {
					player.sendPacket(SystemMessageId.SYMBOL_NOT_FOUND);
				}
				break;

			default:
				super.onBypassFeedback(player, command);
				break;
		}
	}

	@Override
	public String getHtmlPath(int npcId, int val) {
		return "data/html/symbolmaker/SymbolMaker.htm";
	}
}
