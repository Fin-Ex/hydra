package ru.finex.gs.model.component;

import lombok.Getter;
import lombok.Setter;
import ru.finex.gs.model.GameObject;

/**
 * @author m0nster.mind
 */
public abstract class AbstractComponent implements Component {

    @Getter @Setter
    private GameObject gameObject;

    @Override
    public void onAttached() {

    }

    @Override
    public void onRestored() {

    }

    @Override
    public void onDeattached() {

    }

    @Override
    public void onPreUpdate() {

    }

    @Override
    public void onUpdate() {

    }

    @Override
    public void onPostUpdate() {

    }
}
