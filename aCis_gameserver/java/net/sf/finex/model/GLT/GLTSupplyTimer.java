/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.GLT;

import it.sauronsoftware.cron4j.Scheduler;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import net.sf.l2j.commons.random.Rnd;
import net.sf.l2j.gameserver.data.NpcTable;
import net.sf.l2j.gameserver.model.L2Spawn;
import net.sf.l2j.gameserver.model.actor.ai.CtrlIntention;
import net.sf.l2j.gameserver.model.actor.instance.GLTChest;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.model.location.Location;
import net.sf.l2j.gameserver.network.SystemMessageId;

/**
 *
 * @author finfan
 */
@Slf4j
public class GLTSupplyTimer extends Scheduler implements Runnable {

	private final List<GLTChest> values;
	private final int minute;

	public GLTSupplyTimer(int minute) {
		this.values = new ArrayList<>();
		this.minute = minute;
	}

	public void invoke() {
		schedule("*/" + minute + " * * * *", this);
		start();
	}

	@Override
	public void run() {
		try {
			for(int i = 0; i < Rnd.get(10, 25); i++) {
				final NpcTemplate template = NpcTable.getInstance().getTemplate(50006);
				final L2Spawn spawn = new L2Spawn(template);

				final Location loc = GLTController.getInstance().getZone().getChaoticSpawnLoc();
				spawn.setLoc(loc.getX(), loc.getY(), loc.getZ() + 300, Rnd.get(Short.MAX_VALUE));
				spawn.setRespawnState(false);
				final GLTChest chest = (GLTChest) spawn.doSpawn(false, false);
				chest.setFullHpMpCp();
				chest.getAI().setIntention(CtrlIntention.MOVE_TO, new Location(spawn.getLoc().add(0, 0, -300)));
				values.add(chest);
				log.info("Dropped suuply box: {} on {}", chest, loc);
			}
			
			GLTController.getInstance().broadcast(SystemMessageId.GLT_SUPPLY_BOX_DROP);
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void stop() throws IllegalStateException {
		values.forEach(chest -> chest.deleteMe());
		values.clear();
		super.stop();
	}

}
