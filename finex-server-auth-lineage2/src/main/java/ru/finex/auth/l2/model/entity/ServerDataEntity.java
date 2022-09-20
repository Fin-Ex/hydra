package ru.finex.auth.l2.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.finex.core.model.entity.EntityObject;

/**
 * @author m0nster.mind
 */
@Data
@Entity
@Table(name = "server_list")
public class ServerDataEntity implements EntityObject<Integer> {

    @Column(name = "id")
    private Integer persistenceId;

    @NotNull
    private String host;
    @NotNull
    private Integer port;
    @NotNull
    private Integer ageLimit;
    @NotNull
    private Boolean isPvp;
    @NotNull
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
