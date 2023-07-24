package ru.finex.auth.hydra.model.entity;

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
import org.hibernate.validator.constraints.Range;
import ru.finex.core.model.entity.EntityObject;

/**
 * @author m0nster.mind
 */
@Data
@Entity
@Table(name = "server_list")
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "server_list_id_seq", sequenceName = "server_list_id_seq", allocationSize = 1)
public class ServerDataEntity implements EntityObject<Integer> {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "server_list_id_seq", strategy = GenerationType.SEQUENCE)
    private Integer persistenceId;

    @NotNull
    private String host;
    @NotNull
    @Range(max = 65535)
    private Integer port;
    @NotNull
    private Integer ageLimit;
    @NotNull
    private Boolean isPvp;
    @NotNull
    @Range(max = 65535)
    private Integer maxClients;

    @NotNull
    private Boolean isNormal;
    @NotNull
    private Boolean isRelax;
    @NotNull
    private Boolean isPublicTest;
    @NotNull
    private Boolean isNoLabel;
    @NotNull
    private Boolean isDeniedAvatarCreation;
    @NotNull
    private Boolean isEvent;
    @NotNull
    private Boolean isFree;

    @NotNull
    private Boolean isBrackets;

}
