/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sf.finex.data;

import java.util.List;
import lombok.Data;
import sf.finex.enums.EGradeType;
import sf.finex.enums.ERandomQuestType;
import sf.finex.enums.ETownType;
import sf.l2j.gameserver.model.holder.IntIntHolder;

/**
 *
 * @author FinFan
 */
@Data
public class RandomQuestData {

	// Builder data values
	private int id;
	private ERandomQuestType type;
	private ETownType town;
	private EGradeType grade;
	private RandomQuestConditionData condition;
	private List<QuestRewardData> rewards;
	private String description;
	private String name;
	private long exp;
	private int sp;
	private List<IntIntHolder> questItems;

	// Active data values
	private int ownerId;
	private boolean done;
	private int counter;
	private int boardId;
}
