package ru.finex.ws.l2.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import ru.finex.core.model.entity.EntityObject;

/**
 * @author finfan
 */
@Data
@Entity
@Table(name = "game_object_parameter_components")
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "game_object_parameter_components_id_seq",
	sequenceName = "game_object_parameter_components_id_seq", allocationSize = 1)
public class ParameterComponentEntity implements EntityObject<Integer>, GameObjectRelation {

	@Id
	@Column(name = "id")
	@GeneratedValue(generator = "game_object_parameter_components_id_seq", strategy = GenerationType.SEQUENCE)
	private Integer persistenceId;

	@Column(name = "game_object_id", unique = true, nullable = false)
	private Integer gameObjectPersistenceId;

	@Range(min = 0, max = 100)
	@Column(name = "str", nullable = false)
	private int STR;
	@Range(min = 0, max = 100)
	@Column(name = "dex", nullable = false)
	private int DEX;
	@Range(min = 0, max = 100)
	@Column(name = "con", nullable = false)
	private int CON;
	@Range(min = 0, max = 100)
	@Column(name = "int", nullable = false)
	private int INT;
	@Range(min = 0, max = 100)
	@Column(name = "wit", nullable = false)
	private int WIT;
	@Range(min = 0, max = 100)
	@Column(name = "men", nullable = false)
	private int MEN;
	@Range(min = 0, max = 100)
	@Column(name = "luc", nullable = false)
	private int LUC;
	@Range(min = 0, max = 100)
	@Column(name = "cha", nullable = false)
	private int CHA;

	@Override
	public ParameterComponentEntity clone() {
		try {
			return (ParameterComponentEntity) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}
}
