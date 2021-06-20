package ru.finex.gs.model.entity;

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
public class ParameterEntity implements Entity {
	private int persistenceId;

	private int STR;
	private int DEX;
	private int CON;
	private int INT;
	private int WIT;
	private int MEN;
	@Deprecated
	private int LUC;
	@Deprecated
	private int CHA;

	@Override
	public ParameterEntity clone() {
		try {
			return (ParameterEntity) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}
}
