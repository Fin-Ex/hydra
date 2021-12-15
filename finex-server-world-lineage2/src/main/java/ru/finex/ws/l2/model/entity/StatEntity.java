package ru.finex.ws.l2.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.finex.core.model.entity.Entity;

/**
 * @author finfan
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatEntity implements Entity<Integer> {

	private Integer persistenceId;

	private int pAtk;
	private int pDef;
	private int accuracy;
	private int evasion;
	private int attackSpeed;
	private int criticalRate;

	private int mAtk;
	private int mDef;
	private int castSpeed;
	private int magicCriticalRate;
	private int magicEvasion;
	private int magicAccuracy;

	@Override
	public StatEntity clone() {
		try {
			return (StatEntity) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}
}
