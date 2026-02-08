package net.swofty.type.ravengardgeneric.texturepack.widgets;

import net.swofty.type.ravengardgeneric.texturepack.TexturePackRenderer;

public enum HudWidgetType {
    WORLD_HUD("world_hud", 9999, TexturePackRenderer.HudAnchor.TOP_RIGHT),
    SERVER_INFO("server_info", 10000, TexturePackRenderer.HudAnchor.BOTTOM_RIGHT),
    TOP_BANNER("top_banner", 10001, TexturePackRenderer.HudAnchor.TOP),
    STATS_HUD("stats_hud", 10002, TexturePackRenderer.HudAnchor.TOP_RIGHT_BELOW);

    private final String id;
    private final int mapId;
    private final TexturePackRenderer.HudAnchor anchor;

    HudWidgetType(String id, int mapId, TexturePackRenderer.HudAnchor anchor) {
        this.id = id;
        this.mapId = mapId;
        this.anchor = anchor;
    }

    public String id() {
        return id;
    }

    public int mapId() {
        return mapId;
    }

    public TexturePackRenderer.HudAnchor anchor() {
        return anchor;
    }
}
