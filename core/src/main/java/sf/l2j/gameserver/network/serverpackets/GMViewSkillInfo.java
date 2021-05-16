package sf.l2j.gameserver.network.serverpackets;

import java.util.Collection;

import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.skills.L2Skill;

public class GMViewSkillInfo extends L2GameServerPacket {

	private final Player _activeChar;
	private Collection<L2Skill> _skills;

	public GMViewSkillInfo(Player cha) {
		_activeChar = cha;
		_skills = _activeChar.getSkills().values();
	}

	@Override
	protected final void writeImpl() {
		writeC(0x91);
		writeS(_activeChar.getName());
		writeD(_skills.size());

		boolean isDisabled = false;
		if (_activeChar.getClan() != null) {
			isDisabled = _activeChar.getClan().getReputationScore() < 0;
		}

		for (L2Skill skill : _skills) {
			writeD(skill.isPassive() ? 1 : 0);
			writeD(skill.getLevel());
			writeD(skill.getId());
			writeC(isDisabled && skill.isClanSkill() ? 1 : 0);
		}
	}
}
