package ru.finex.core.model.entity;

/**
 * @author m0nster.mind
 */
public interface Entity extends Cloneable {

    int getPersistenceId();

    Entity clone();

}
