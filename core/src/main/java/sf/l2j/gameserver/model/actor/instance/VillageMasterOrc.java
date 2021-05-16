package sf.l2j.gameserver.model.actor.instance;

import sf.l2j.gameserver.model.actor.template.NpcTemplate;
import sf.l2j.gameserver.model.base.ClassId;
import sf.l2j.gameserver.model.base.ClassRace;

public final class VillageMasterOrc extends VillageMaster {

	public VillageMasterOrc(int objectId, NpcTemplate template) {
		super(objectId, template);
	}

	@Override
	protected final boolean checkVillageMasterRace(ClassId pclass) {
		if (pclass == null) {
			return false;
		}

		return pclass.getRace() == ClassRace.ORC;
	}
}
