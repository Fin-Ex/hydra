/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sf.finex.model.classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import sf.finex.data.RecipeData;
import sf.finex.data.SpecializeData;
import sf.finex.enums.ECraftSpec;
import sf.finex.enums.EUIEventType;
import sf.finex.model.craft.Craft;
import sf.l2j.L2DatabaseFactory;
import sf.l2j.gameserver.data.ItemTable;
import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.model.holder.IntIntHolder;
import sf.l2j.gameserver.network.serverpackets.ActionFailed;
import sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

/**
 *
 * @author FinFan
 */
@Slf4j
public final class Warsmith extends AbstractClassComponent {

	@Getter
	@Setter
	private List<SpecializeData> specializes = new ArrayList<>();

	public Warsmith(Player player) {
		super(player);
		for (ECraftSpec craftSpec : ECraftSpec.values()) {
			specializes.add(new SpecializeData(craftSpec));
		}
	}

	/* CRAFT METHODS ******************************************/
	public void addExp(RecipeData recipe) {
		// find required data
		final SpecializeData data = getSpecDataForItem(recipe);
		if (data == null) {
			return;
		}

		if (data.getLvl() == Craft.MAX_LVL) {
			getGameObject().sendMessage("You already max level.");
			return;
		}

		// calculate base exp
		int earnEXP = calcExpForItem(recipe);

		// calculate penalty from recipe level and current level
		double lvldiff = recipe.getLevel() / (data.getLvl() / 2.);
		earnEXP *= lvldiff;

		// calculate bonus exp for success rate if there chance is lower than 100
		int successRate = recipe.getSuccessRate();
		if (successRate != 100) {
			earnEXP /= successRate / 100.;
		}

		// add experience
		int calcled = data.getExp() + earnEXP;
		int neededExp = Craft.getExpForLevel(data.getLvl() + 1);
		if (calcled >= neededExp) {
			int expDiff = data.getExp() + earnEXP - neededExp;
			// increase craft level;
			data.setLvl(data.getLvl() + 1);
			data.setExp(expDiff);
			getGameObject().sendMessage("You increase your level!");
		} else {
			data.setExp(calcled);
			getGameObject().sendMessage("You get " + earnEXP + " craft " + data.getSpecialize().name().toLowerCase() + " EXP.");
		}

		if (recipe.getSuccessRate() <= 70) {
			data.setSuccesess(data.getLvl() + 1);
		}
	}

	@Deprecated
	public void removeExp(RecipeData recipe) {
		final SpecializeData data = getSpecDataForItem(recipe);
		if (data == null) {
			return;
		}

		// calculate base exp
		int lostEXP = calcExpForItem(recipe) / 2;
		int calced = data.getExp() - lostEXP;
		if (calced < 0) {
			// reduce level
			data.setExp(Craft.getExpForLevel(data.getLvl() - 1));
			if (data.getExp() == 0) {
				data.setLvl(0);
			} else {
				data.setLvl(data.getLvl() - 1);
				data.setExp(data.getExp() - lostEXP);
				getGameObject().sendMessage("Your craft level of " + data.getSpecialize().name() + " was decreased!");
			}
		}

		if (recipe.getSuccessRate() <= 70) {
			data.setFails(data.getFails() + 1);
		}
		getGameObject().sendMessage("You lost " + lostEXP + " for not success craft.");
	}

	private int calcExpForItem(RecipeData recipe) {
		int exp = 0;
		for (IntIntHolder resource : recipe.getIngredients()) {
			exp += ItemTable.getInstance().getTemplate(resource.getId()).getReferencePrice() * resource.getValue();
		}
		exp /= recipe.getIngredients().size();
		exp = (int) Math.sqrt(exp);
		return exp;
	}

	private SpecializeData getSpecDataForItem(RecipeData recipe) {
		for (SpecializeData data : specializes) {
			if (data.getSpecialize() == recipe.getSpec()) {
				return data;
			}
		}

		return null;
	}

	/* SPECIALIZATION METHODS *******************************************/
	public SpecializeData getSpecialize(ECraftSpec spec) {
		for (SpecializeData data : specializes) {
			if (data.getSpecialize() == spec) {
				return data;
			}
		}
		return null;
	}

	public int getRating(ECraftSpec spec) {
		final SpecializeData data = getSpecialize(spec);
		double koeff = (data.getSuccesess() + 1) / (data.getFails() + 1);
		int rating = (int) Math.sqrt(data.getExp());
		rating *= koeff;
		return rating;
	}

	public float getPercent(ECraftSpec spec) {
		final SpecializeData data = getSpecialize(spec);
		if (data.getSuccesess() == 0 && data.getFails() == 0) {
			return 0;
		}

		if (data.getFails() == 0) {
			return 100;
		}

		return data.getSuccesess() / data.getFails() * 100;
	}

	@Override
	public void onAdd() {
	}

	@Override
	public void onRemove() {
		delete();
	}

	@Override
	public Player getGameObject() {
		return super.getGameObject().getPlayer();
	}

	/* PERSISTANCE *******************************************************/
	public static void insert(Player player) {
		final List<SpecializeData> specializes = new ArrayList<>();
		for (ECraftSpec craftSpec : ECraftSpec.values()) {
			specializes.add(new SpecializeData(craftSpec));
		}

		try (Connection con = L2DatabaseFactory.getInstance().getConnection(); PreparedStatement st = con.prepareStatement("REPLACE INTO character_craft (objectId,lvl,exp,fails,succesess,specialize) VALUES (?,?,?,?,?,?)")) {
			for (SpecializeData data : specializes) {
				st.setInt(1, player.getObjectId());
				st.setInt(2, data.getLvl());
				st.setInt(3, data.getExp());
				st.setInt(4, data.getFails());
				st.setInt(5, data.getSuccesess());
				st.setInt(6, data.getSpecialize().ordinal());
				st.executeUpdate();
			}
		} catch (SQLException ex) {
			log.error("", ex);
		}
	}

	@Override
	public void store() {
		try (Connection con = L2DatabaseFactory.getInstance().getConnection(); PreparedStatement st = con.prepareStatement("UPDATE character_craft SET lvl=?, exp=?, fails=?, succesess=? WHERE objectId=? AND specialize=?")) {
			for (SpecializeData data : specializes) {
				st.setInt(1, data.getLvl());
				st.setInt(2, data.getExp());
				st.setInt(3, data.getFails());
				st.setInt(4, data.getSuccesess());
				st.setInt(5, getGameObject().getObjectId());
				st.setInt(6, data.getSpecialize().ordinal());
				st.executeUpdate();
			}
		} catch (SQLException ex) {
			log.error("", ex);
		}
	}

	@Override
	public void restore() {
		try (Connection con = L2DatabaseFactory.getInstance().getConnection(); PreparedStatement st = con.prepareStatement("SELECT * FROM character_craft WHERE objectId=? AND specialize=?")) {
			for (SpecializeData data : specializes) {
				st.setInt(1, getGameObject().getObjectId());
				st.setInt(2, data.getSpecialize().ordinal());
				try (ResultSet rset = st.executeQuery()) {
					while (rset.next()) {
						data.setLvl(rset.getInt("lvl"));
						data.setExp(rset.getInt("exp"));
						data.setFails(rset.getInt("fails"));
						data.setSuccesess(rset.getInt("succesess"));
					}
				}
			}
		} catch (SQLException ex) {
			log.error("", ex);
		}
	}

	@Override
	public void delete() {
		try (Connection con = L2DatabaseFactory.getInstance().getConnection(); PreparedStatement st = con.prepareStatement("DELETE FROM character_craft WHERE objectId=?")) {
			st.setInt(1, getGameObject().getObjectId());
			st.executeUpdate();
		} catch (SQLException ex) {
			log.error("", ex);
		}
	}

	@Override
	public void remove(Object... args) {
		throw new UnsupportedOperationException("Operation not handle.");
	}

	/* UI FUNC *************************************************************/
	@Override
	public void showHtml(EUIEventType event) {
		final NpcHtmlMessage html = new NpcHtmlMessage(0);
		switch (event) {
			case INFO: {
				html.setFile("data/html/personal/warsmith.htm");
				for (SpecializeData spec : specializes) {
					final int specId = spec.getSpecialize().ordinal();
					html.replace("%spec" + specId + "%", spec.getSpecialize().getSimpleName());
					html.replace("%specLvl" + specId + "%", spec.getLvl());
					html.replace("%specExp" + specId + "%", spec.getExp() + "/" + Craft.getExpForLevel(spec.getLvl()));
					html.replace("%specFails" + specId + "%", spec.getFails());
					html.replace("%specSuccesses" + specId + "%", spec.getSuccesess());
				}
				break;
			}

			default:
				return;
		}
		html.replace("%name%", getGameObject().getName());
		getGameObject().sendPacket(html);
		getGameObject().sendPacket(ActionFailed.STATIC_PACKET);
	}
}
