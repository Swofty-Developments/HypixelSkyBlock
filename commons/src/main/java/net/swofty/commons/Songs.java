package net.swofty.commons;

import lombok.Getter;

import java.nio.file.Path;

@Getter
public enum Songs {
    WILDERNESS(Path.of("./configuration/skyblock/songs/wilderness.nbs")),
    A_SILENT_MEMOIR(Path.of("./configuration/skyblock/songs/a_silent_memoir.nbs"))
    ;

    private final Path path;

    Songs(Path path) {
        this.path = path;
    }
}
