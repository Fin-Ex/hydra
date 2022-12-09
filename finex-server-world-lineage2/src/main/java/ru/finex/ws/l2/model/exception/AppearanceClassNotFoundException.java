package ru.finex.ws.l2.model.exception;

import ru.finex.ws.l2.model.Gender;
import ru.finex.ws.l2.model.enums.Race;

/**
 * @author m0nster.mind
 */
public class AppearanceClassNotFoundException extends RuntimeException {

    public AppearanceClassNotFoundException() {
    }

    public AppearanceClassNotFoundException(String message) {
        super(message);
    }

    public AppearanceClassNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public AppearanceClassNotFoundException(Throwable cause) {
        super(cause);
    }

    public AppearanceClassNotFoundException(Race race, Gender gender) {
        super("Unknown gender " + gender + " for race: " + race);
    }

    public AppearanceClassNotFoundException(Race race) {
        super("Unknown race: " + race);
    }

}
