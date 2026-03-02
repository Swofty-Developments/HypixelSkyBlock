package net.swofty.type.generic;

import net.swofty.commons.Songs;

public interface SkyBlockTypeLoader extends HypixelTypeLoader {
    default Songs getIslandSong() {
        return null;
    }
}
