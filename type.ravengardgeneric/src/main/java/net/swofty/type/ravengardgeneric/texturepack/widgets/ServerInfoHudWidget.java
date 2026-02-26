package net.swofty.type.ravengardgeneric.texturepack.widgets;

import net.minestom.server.network.packet.server.play.MapDataPacket;
import net.swofty.type.ravengardgeneric.texturepack.TexturePackRenderer;

public class ServerInfoHudWidget implements HudMapWidget {
    public static final String DEFAULT_DOMAIN = "mc.hypixel.net";

    private final HudWidgetType type;
    private final String domainText;

    public ServerInfoHudWidget(HudWidgetType type, String domainText) {
        this.type = type;
        this.domainText = domainText;
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
        renderer.drawServerHudText(colors, context.shortServerName(), context.date(), domainText);
        return renderer.toPacket(mapId(), colors, anchor());
    }
}
