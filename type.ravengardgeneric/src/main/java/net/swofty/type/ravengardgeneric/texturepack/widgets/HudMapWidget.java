package net.swofty.type.ravengardgeneric.texturepack.widgets;

import net.minestom.server.network.packet.server.play.MapDataPacket;
import net.swofty.type.ravengardgeneric.texturepack.TexturePackRenderer;

public interface HudMapWidget {
    String id();

    int mapId();

    TexturePackRenderer.HudAnchor anchor();

    default boolean enabled(HudMapWidgetContext context) {
        return true;
    }

    MapDataPacket render(HudMapWidgetContext context);
}
