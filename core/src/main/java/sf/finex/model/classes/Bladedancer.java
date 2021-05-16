/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sf.finex.model.classes;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import sf.l2j.Config;
import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.model.actor.events.OnAttack;
import sf.l2j.gameserver.model.actor.events.OnAttackStance;
import sf.l2j.gameserver.model.actor.events.OnCast;
import sf.l2j.gameserver.network.SystemMessageId;
import sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 *
 * @author FinFan
 */
@Slf4j
public final class Bladedancer extends AbstractClassComponent {

	@Getter @Setter private float rhythmPoints;

	public Bladedancer(Player player) {
		super(player);
		getGameObject().getEventBus().subscribe().cast(OnAttack.class).forEach(this::onAttack);
		getGameObject().getEventBus().subscribe().cast(OnCast.class).forEach(this::onCast);
		getGameObject().getEventBus().subscribe().cast(OnAttackStance.class).forEach(this::onRemoveAttackStance);
	}

	private void onRemoveAttackStance(OnAttackStance e) {
		//TODO: not works
		if (e.isEnded()) {
			rhythmPoints = 0;
			getGameObject().sendMessage("Attack stance was removed and rhythm points setted to 0.");
		}
	}

	private void onAttack(OnAttack e) {
		rhythmPoints += Math.min(e.getInfo().isCrit ? 1.2f : 0.4f, 100);
		if (!Config.OFF_RHYTHM_FEEL_MESSAGE) {
			getGameObject().sendPacket(SystemMessage.getSystemMessage(SystemMessageId.RHYTHM_FEEL_IS_S1).addNumber((int) rhythmPoints));
		}
	}

	private void onCast(OnCast event) {
		switch (event.getSkill().getId()) {
			case 223: //sting
				rhythmPoints += 1;
				getGameObject().sendPacket(SystemMessage.getSystemMessage(SystemMessageId.RHYTHM_FEEL_IS_S1).addNumber((int) rhythmPoints));
				break;

			case 84: // poison blade dance
			case 408: // demonic blade dance
				rhythmPoints += 3;
				getGameObject().sendPacket(SystemMessage.getSystemMessage(SystemMessageId.RHYTHM_FEEL_IS_S1).addNumber((int) rhythmPoints));
				break;

			default:
				if (event.getSkill().isDance()) {
					getGameObject().sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_WAS_REINFORCEMENT_BY_S2)
							.addSkillName(event.getSkill())
							.addNumber((int) ((calcRhythm() - 1) * 100)));
					// remove points after dance
					if (!Config.OFF_RHYTHM_FEEL_MESSAGE && rhythmPoints > 0) {
						getGameObject().sendPacket(SystemMessage.getSystemMessage(SystemMessageId.RHYTHM_FEEL_IS_S1).addNumber(0));
					}
					rhythmPoints = 0;
				}
				break;
		}
	}

	public double calcRhythm() {
		return rhythmPoints / 50. + 1;
	}

	@Override
	public Player getGameObject() {
		return super.getGameObject().getPlayer();
	}
}
