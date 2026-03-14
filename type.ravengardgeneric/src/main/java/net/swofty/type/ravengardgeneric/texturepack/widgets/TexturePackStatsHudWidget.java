package net.swofty.type.ravengardgeneric.texturepack.widgets;

import net.minestom.server.network.packet.server.play.MapDataPacket;
import net.swofty.type.ravengardgeneric.texturepack.TexturePackRenderer;

import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class TexturePackStatsHudWidget implements HudMapWidget {
    private final HudWidgetType type;
    private final Supplier<String> timerSupplier;
    private final IntSupplier swordCountSupplier;
    private final IntSupplier playerCountSupplier;
    private final IntSupplier squadCountSupplier;

    public TexturePackStatsHudWidget(
            HudWidgetType type,
            Supplier<String> timerSupplier,
            IntSupplier swordCountSupplier,
            IntSupplier playerCountSupplier,
            IntSupplier squadCountSupplier
    ) {
        this.type = type;
        this.timerSupplier = timerSupplier;
        this.swordCountSupplier = swordCountSupplier;
        this.playerCountSupplier = playerCountSupplier;
        this.squadCountSupplier = squadCountSupplier;
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
        renderer.drawStatsHud(
                colors,
                timerSupplier.get(),
                swordCountSupplier.getAsInt(),
                playerCountSupplier.getAsInt(),
                squadCountSupplier.getAsInt()
        );
        return renderer.toPacket(mapId(), colors, anchor());
    }
}
