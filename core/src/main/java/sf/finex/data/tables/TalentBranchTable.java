/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sf.finex.data.tables;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import sf.finex.StorageTable;
import sf.finex.data.TalentBranchData;
import sf.finex.data.TalentData;
import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.model.base.ClassId;

/**
 *
 * @author FinFan
 */
@Slf4j
public final class TalentBranchTable extends StorageTable {

	@Getter
	private static final TalentBranchTable instance = new TalentBranchTable();

	private final List<TalentBranchData> holder = new ArrayList<>();
	private final Map<ClassId, StringBuilder> htmlBuilder = new HashMap<>();

	private TalentBranchTable() {
		load();
	}

	@Override
	public void reload() {
		holder.clear();
		htmlBuilder.clear();
		load();
		log.info("Talents Branches Reloaded!");
	}

	@Override
	protected void load() {
		final Gson gson = new Gson();
		final File file = new File("data/json/talents/branches.json");
		try (Reader reader = new InputStreamReader(new FileInputStream(file))) {
			holder.addAll(gson.fromJson(reader, new TypeToken<List<TalentBranchData>>() {
			}.getType()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		log.info("Loaded {} talent branches for classes.", holder.size());
		buildHtmlTree();
	}

	@Override
	public TalentBranchData get(int identifier) {
		for (int i = 0; i < holder.size(); i++) {
			final TalentBranchData data = holder.get(i);
			if (data.getId() == identifier) {
				return data;
			}
		}

		return null;
	}

	@Override
	public List<TalentBranchData> holder() {
		return holder;
	}

	public final StringBuilder getTalentsHtml(ClassId classId) {
		final ClassId oriClass = classId.level() == 3 ? classId.getParent() : classId;
		if (!htmlBuilder.containsKey(oriClass)) {
			throw new UnsupportedOperationException("Talents not exist in talents.gson for class " + oriClass);
		}

		return htmlBuilder.get(oriClass);
	}

	private void buildHtmlTree() {
		// build html for talents
		for (TalentBranchData branchData : holder) {
			final ClassId classId = branchData.getClassId();

			// get talent list for class
			final List<TalentData> talentList = new ArrayList<>();
			for (int i = 0; i < branchData.getTalents().length; i++) {
				final TalentData data = TalentTable.getInstance().get(branchData.getTalents()[i]);
				if (data == null) {
					log.warn("Talent data is null for ");
					continue;
				}

				talentList.add(data);
			}

			// build html view of talents window
			final StringBuilder sb = new StringBuilder();
			sb.append("<html imgsrc=v1c01.talent_bg><title>%points%</title><body><a action=\"bypass -h talentReset\"><font color=FAEBD7>Reset</font></a>");
			sb.append("<center><table width=283>");
			int i = 1;
			for (TalentData data : talentList) {
				if (i == 1) {
					sb.append("<tr>");
				}

				sb.append("<td align=center width=70 height=40><button value=\"").append("\" action=\"bypass -h talentInfo ").append(data.getId())
						.append("\" width=32 height=32 ")
						.append("back=\"").append(data.getIcon()).append("\" ")
						.append("fore=\"").append(data.getIcon()).append("\"").append("</td>");

				i++;
				if (i > 3) {
					sb.append("</tr>");
					i = 1;
				}
			}
			sb.append("</table></center></body></html>");
			htmlBuilder.put(classId, sb);

		}
	}

	public TalentBranchData getBranch(ClassId classId) {
		for (TalentBranchData data : holder) {
			if (classId.equalsOrChildOf(data.getClassId())) {
				return data;
			}
		}

		return null;
	}

	/**
	 * Player classId has a branch
	 *
	 * @param player
	 * @return
	 */
	public boolean checkBranch(Player player) {
		return getBranch(player.getClassId()) != null;
	}
}
