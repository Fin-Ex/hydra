package net.sf.l2j.gameserver.network.serverpackets;

import org.slf4j.LoggerFactory;

import net.sf.l2j.gameserver.model.pledge.ClanMember;

/**
 * Format : (ch) dSd
 * @author -Wooden-
 */
public class PledgeReceivePowerInfo extends L2GameServerPacket
{
	private final ClanMember _member;
	
	public PledgeReceivePowerInfo(ClanMember member)
	{
		_member = member;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xfe);
		writeH(0x3c);
		
		writeD(_member.getPowerGrade()); // power grade
		writeS(_member.getName());
		writeD(_member.getClan().getRankPrivs(_member.getPowerGrade())); // privileges
	}
}