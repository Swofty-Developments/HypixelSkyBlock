package net.swofty.type.ravengardgeneric.texturepack.widgets;

import net.minestom.server.network.packet.server.play.MapDataPacket;
import net.swofty.type.ravengardgeneric.texturepack.TexturePackRenderer;

public class TexturePackWorldHudWidget implements HudMapWidget {
    private final HudWidgetType type;

    public TexturePackWorldHudWidget(HudWidgetType type) {
        this.type = type;
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
        renderer.renderWorld(colors, context.instance(), context.position());
        renderer.applyStandardOverlay(colors);
        renderer.drawFacingArrowOnto(colors, context.position().yaw());
        return renderer.toPacket(mapId(), colors, anchor());
    }
}
