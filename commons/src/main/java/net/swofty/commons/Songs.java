package net.swofty.commons;

import lombok.Getter;

import java.nio.file.Path;

@Getter
public enum Songs {
    ABSTRACT_RINGING(Path.of("./configuration/skyblock/songs/abstract_ringing.nbs")),
    A_SILENT_MEMOIR(Path.of("./configuration/skyblock/songs/a_silent_memoir.nbs")),
    AMBIENT_CAVES(Path.of("./configuration/skyblock/songs/ambient_caves.nbs")),
    ;

    private final Path path;

    Songs(Path path) {
        this.path = path;
    }
}
