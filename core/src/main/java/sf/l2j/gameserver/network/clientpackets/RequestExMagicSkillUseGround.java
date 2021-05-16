package sf.l2j.gameserver.network.clientpackets;

import sf.l2j.commons.math.MathUtil;

import sf.l2j.gameserver.data.SkillTable;
import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.model.location.Location;
import sf.l2j.gameserver.network.serverpackets.ActionFailed;
import sf.l2j.gameserver.network.serverpackets.ValidateLocation;
import sf.l2j.gameserver.skills.L2Skill;

/**
 * Fromat:(ch) dddddc
 *
 * @author -Wooden-
 */
public final class RequestExMagicSkillUseGround extends L2GameClientPacket {

	private int _x, _y, _z;
	private int _skillId;
	private boolean _ctrlPressed, _shiftPressed;

	@Override
	protected void readImpl() {
		_x = readD();
		_y = readD();
		_z = readD();
		_skillId = readD();
		_ctrlPressed = readD() != 0;
		_shiftPressed = readC() != 0;
	}

	@Override
	protected void runImpl() {
		// Get the current player
		final Player activeChar = getClient().getActiveChar();
		if (activeChar == null) {
			return;
		}

		// Get the level of the used skill
		final int level = activeChar.getSkillLevel(_skillId);
		if (level <= 0) {
			activeChar.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}

		// Get the L2Skill template corresponding to the skillID received from the client
		final L2Skill skill = SkillTable.getInstance().getInfo(_skillId, level);
		if (skill != null) {
			activeChar.setCurrentSkillWorldPosition(new Location(_x, _y, _z));

			// normally magicskilluse packet turns char client side but for these skills, it doesn't (even with correct target)
			activeChar.setHeading(MathUtil.calculateHeadingFrom(activeChar.getX(), activeChar.getY(), _x, _y));
			activeChar.broadcastPacket(new ValidateLocation(activeChar));

			activeChar.useMagic(skill, _ctrlPressed, _shiftPressed);
		} else {
			activeChar.sendPacket(ActionFailed.STATIC_PACKET);
			_log.warn("No skill found with id: " + _skillId + " and level: " + level);
		}
	}
}
