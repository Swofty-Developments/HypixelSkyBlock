package net.swofty.commons.replay;

import lombok.Getter;

@Getter
public enum ReplayError {
    UNSUPPORTED_CODEC(3, "UNSUPPORTED CODEC {}"),
    REPLAY_NOT_FOUND(404, "REPLAY NOT FOUND")
    ;

    private final int code;
    private final String message;

    ReplayError(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public String format() {
        return "ERROR-" + this.code + " - " + this.message;
    }
}
