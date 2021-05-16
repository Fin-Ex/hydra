/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sf.finex.data;

import java.util.List;
import lombok.Data;

/**
 *
 * @author FinFan
 */
@Data
public class QuestData {

	private final int id;
	private final long exp;
	private final int sp;
	private final List<QuestRewardData> rewards;
}
