package ru.finex.ws.l2.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.finex.core.model.entity.EntityObject;

/**
 * @author finfan
 */
@Data
@Entity
@Table(name = "game_object_stat_components")
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "game_object_stat_components_id_seq",
	sequenceName = "game_object_stat_components_id_seq", allocationSize = 1)
public class StatComponentEntity implements EntityObject<Integer> {

	@Id
	@Column(name = "id")
	@GeneratedValue(generator = "game_object_stat_components_id_seq", strategy = GenerationType.SEQUENCE)
	private Integer persistenceId;

	@Column(name = "game_object_id", unique = true, nullable = false)
	private Integer gameObjectPersistenceId;

	@NotNull
	@Min(1)
	private Integer pAtk;
	@NotNull
	private Integer pDef;
	@NotNull
	private Integer accuracy;
	@NotNull
	private Integer evasion;
	@NotNull
	@Min(1)
	private Integer attackSpeed;
	@NotNull
	private Integer criticalRate;

	@NotNull
	@Min(1)
	private Integer mAtk;
	@NotNull
	private Integer mDef;
	@NotNull
	@Min(1)
	private Integer castSpeed;
	@Min(1)
	private Integer magicCriticalRate;
	@Min(1)
	private Integer magicEvasion;
	@Min(1)
	private Integer magicAccuracy;

	@Override
	public StatComponentEntity clone() {
		try {
			return (StatComponentEntity) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}
}
