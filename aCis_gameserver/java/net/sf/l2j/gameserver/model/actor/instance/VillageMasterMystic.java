package net.sf.l2j.gameserver.model.actor.instance;

import org.slf4j.LoggerFactory;

import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.model.base.ClassId;
import net.sf.l2j.gameserver.model.base.ClassRace;
import net.sf.l2j.gameserver.model.base.ClassType;

public final class VillageMasterMystic extends VillageMaster {

	public VillageMasterMystic(int objectId, NpcTemplate template) {
		super(objectId, template);
	}

	@Override
	protected final boolean checkVillageMasterRace(ClassId pclass) {
		if (pclass == null) {
			return false;
		}

		return pclass.getRace() == ClassRace.HUMAN || pclass.getRace() == ClassRace.ELF;
	}

	@Override
	protected final boolean checkVillageMasterTeachType(ClassId pclass) {
		if (pclass == null) {
			return false;
		}

		return pclass.getType() == ClassType.MYSTIC;
	}
}
