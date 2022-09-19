package ru.finex.ws.l2.network.session;

/**
 * @author m0nster.mind
 */
public enum GameClientState {
    NOT_CONNECTED,
    CONNECTED, // client has just connected
    AUTHED, // client has authed but doesnt has character attached to it yet
    IN_GAME // client has selected a char and is in game
}
