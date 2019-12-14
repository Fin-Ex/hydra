package net.sf.l2j.gameserver.model.entity;

import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.List;

import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.entity.Siege.SiegeSide;
import net.sf.l2j.gameserver.model.pledge.Clan;

public interface Siegable {

	public void startSiege();

	public void endSiege();

	public List<Clan> getAttackerClans();

	public List<Clan> getDefenderClans();

	public boolean checkSide(Clan clan, SiegeSide type);

	public boolean checkSides(Clan clan, SiegeSide... types);

	public boolean checkSides(Clan clan);

	public Npc getFlag(Clan clan);

	public Calendar getSiegeDate();
}
