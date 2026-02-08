package net.swofty.type.ravengardgeneric.texturepack.widgets;

import net.minestom.server.network.packet.server.play.MapDataPacket;
import net.swofty.type.ravengardgeneric.texturepack.TexturePackRenderer;

public class TopBannerHudWidget implements HudMapWidget {
    public static final String DEFAULT_TITLE = "RAVENGARD";

    private final HudWidgetType type;
    private final String title;

    public TopBannerHudWidget(HudWidgetType type, String title) {
        this.type = type;
        this.title = title;
    }

    @Override
    public String id() {
        return type.id();
    }

    @Override
    public int mapId() {
        return type.mapId();
    }

    @Override
    public TexturePackRenderer.HudAnchor anchor() {
        return type.anchor();
    }

    @Override
    public MapDataPacket render(HudMapWidgetContext context) {
        TexturePackRenderer renderer = context.renderer();
        byte[] colors = renderer.createEmptyBuffer();
        renderer.drawTopBannerText(colors, title);
        return renderer.toPacket(mapId(), colors, anchor());
    }
}
