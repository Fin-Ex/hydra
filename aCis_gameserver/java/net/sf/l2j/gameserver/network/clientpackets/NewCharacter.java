package net.sf.l2j.gameserver.network.clientpackets;

import org.slf4j.LoggerFactory;

import net.sf.l2j.gameserver.data.CharTemplateTable;
import net.sf.l2j.gameserver.model.base.ClassId;
import net.sf.l2j.gameserver.network.serverpackets.CharTemplates;

public final class NewCharacter extends L2GameClientPacket
{
	@Override
	protected void readImpl()
	{
	}
	
	@Override
	protected void runImpl()
	{
		CharTemplates ct = new CharTemplates();
		
		ct.addChar(CharTemplateTable.getInstance().getTemplate(0));
		ct.addChar(CharTemplateTable.getInstance().getTemplate(ClassId.HumanFighter));
		ct.addChar(CharTemplateTable.getInstance().getTemplate(ClassId.HymanMystic));
		ct.addChar(CharTemplateTable.getInstance().getTemplate(ClassId.ElvenFighter));
		ct.addChar(CharTemplateTable.getInstance().getTemplate(ClassId.ElvenMystic));
		ct.addChar(CharTemplateTable.getInstance().getTemplate(ClassId.DarkFighter));
		ct.addChar(CharTemplateTable.getInstance().getTemplate(ClassId.DarkMystic));
		ct.addChar(CharTemplateTable.getInstance().getTemplate(ClassId.OrcFighter));
		ct.addChar(CharTemplateTable.getInstance().getTemplate(ClassId.OrcMystic));
		ct.addChar(CharTemplateTable.getInstance().getTemplate(ClassId.DwarvenFighter));
		
		sendPacket(ct);
	}
}