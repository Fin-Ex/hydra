package sf.l2j.gameserver.network.serverpackets;

import java.util.ArrayList;

public class ExEnchantSkillList extends L2GameServerPacket {

	private final ArrayList<Skill> _skills;

	class Skill {

		public int id;
		public int nextLevel;
		public int sp;
		public int exp;

		Skill(int pId, int pNextLevel, int pSp, int pExp) {
			id = pId;
			nextLevel = pNextLevel;
			sp = pSp;
			exp = pExp;
		}
	}

	public void addSkill(int id, int level, int sp, int exp) {
		_skills.add(new Skill(id, level, sp, exp));
	}

	public ExEnchantSkillList() {
		_skills = new ArrayList<>();
	}

	@Override
	protected void writeImpl() {
		writeC(0xfe);
		writeH(0x17);

		writeD(_skills.size());
		for (Skill sk : _skills) {
			writeD(sk.id);
			writeD(sk.nextLevel);
			writeD(sk.sp);
			writeQ(sk.exp);
		}
	}
}
