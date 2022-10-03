package ru.finex.auth.l2.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.finex.core.model.entity.EntityObject;

/**
 * @author m0nster.mind
 */
@Data
@Entity
@Table(name = "user_information")
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "user_information_id_seq", sequenceName = "user_information_id_seq", allocationSize = 1)
public class UserInformationEntity implements EntityObject<Long> {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "user_information_id_seq", strategy = GenerationType.SEQUENCE)
    private Long persistenceId;

    @NotNull
    private Long userId;
    private Integer lastServer;

}
