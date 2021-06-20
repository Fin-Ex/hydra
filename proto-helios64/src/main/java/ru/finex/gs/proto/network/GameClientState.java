package ru.finex.gs.proto.network;

/**
 * @author m0nster.mind
 */
public enum GameClientState {
    CONNECTED, // client has just connected
    AUTHED, // client has authed but doesnt has character attached to it yet
    IN_GAME // client has selected a char and is in game
}
