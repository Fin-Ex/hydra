package sf.l2j.gameserver.model.actor.instance;

import sf.l2j.gameserver.model.actor.template.NpcTemplate;
import sf.l2j.gameserver.model.base.ClassId;
import sf.l2j.gameserver.model.base.ClassRace;
import sf.l2j.gameserver.model.base.ClassType;

public final class VillageMasterFighter extends VillageMaster {

	public VillageMasterFighter(int objectId, NpcTemplate template) {
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

		return pclass.getType() == ClassType.FIGHTER;
	}
}
