package net.swofty.type.ravengardgeneric.hud;

import java.util.function.Supplier;

public enum RavengardMinimapArea {
    HUB(RavengardHudGlyphs::hubTiles),
    CRYPT(RavengardHudGlyphs::cryptTiles);

    private final Supplier<String> tiles;

    RavengardMinimapArea(Supplier<String> tiles) {
        this.tiles = tiles;
    }

    public String tiles() {
        return tiles.get();
    }
}
