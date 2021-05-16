package sf.finex.data;

import lombok.Data;
import sf.l2j.gameserver.model.actor.Player;

/**
 * A datatype used to retain Henna infos. Hennas are called "dye" ingame, and
 * enhance {@link Player} stats for a fee.<br>
 * You can draw up to 3 hennas (depending about your current class rank), but
 * accumulated boni for a stat can't be higher than +5. There is no limit in
 * reduction.
 */
@Data
public final class DyeData {

	private final int symbolId;
	private final int dyeId;
	private final int price;
	private final int STR;
	private final int DEX;
	private final int CON;
	private final int INT;
	private final int WIT;
	private final int MEN;

	public static final int getRequiredDyeAmount() {
		return 10;
	}
}
