package net.swofty.commons;

import lombok.Getter;

import java.nio.file.Path;

@Getter
public enum Songs {
    WILDERNESS(Path.of("./configuration/songs/wilderness.nbs")),
    ;

    private final Path path;

    Songs(Path path) {
        this.path = path;
    }
}
