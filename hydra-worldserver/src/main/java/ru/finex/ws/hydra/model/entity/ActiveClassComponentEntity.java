package ru.finex.ws.hydra.model.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * @author m0nster.mind
 */
@Entity
@DiscriminatorValue("1")
public class ActiveClassComponentEntity extends ClassComponentEntity {
}
