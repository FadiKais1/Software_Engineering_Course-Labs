package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

public enum MessageType implements Serializable {
    CONNECT,
    WAITING_FOR_PLAYER,
    GAME_START,
    MAKE_MOVE,
    BOARD_UPDATE,
    INVALID_MOVE,
    GAME_OVER,
    PLAYER_DISCONNECTED,
    ERROR
}