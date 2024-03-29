package ru.finex.ws.hydra.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author m0nster.mind
 */
@Data
//@Entity
//@Table(name = "game_object_player_settings")
@NoArgsConstructor
@AllArgsConstructor
//@SequenceGenerator(name = "game_object_player_settings_id_seq",
//    sequenceName = "game_object_player_settings_id_seq", allocationSize = 1)
public class PlayerSettings /*implements EntityObject<Integer>*/ {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "game_object_player_settings_id_seq", strategy = GenerationType.SEQUENCE)
    private Integer persistenceId;

    // TODO m0nster.mind: развернуть в нормальную структуру
    private String ini;

}
