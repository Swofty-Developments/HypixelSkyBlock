package net.swofty.type.ravengardgeneric.texturepack;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.packet.server.play.MapDataPacket;
import net.swofty.type.ravengardgeneric.texturepack.widgets.HudWidgetType;

import java.util.List;

public class TexturePackRenderer {
    private final TexturePackWorldRasterRenderer worldRenderer = new TexturePackWorldRasterRenderer();
    private final TexturePackHudTextRenderer textRenderer = new TexturePackHudTextRenderer();
    private final TexturePackMarkerEncoder markerEncoder = new TexturePackMarkerEncoder();

    public MapDataPacket render(Instance instance, Pos center, String shortServerName, String date) {
        return render(
                instance,
                center,
                shortServerName,
                date,
                HudWidgetType.WORLD_HUD.anchor(),
                HudWidgetType.WORLD_HUD.mapId()
        );
    }

    public MapDataPacket render(Instance instance, Pos center, String shortServerName, String date, HudAnchor anchor) {
        return render(instance, center, shortServerName, date, anchor, HudWidgetType.WORLD_HUD.mapId());
    }

    public MapDataPacket render(
            Instance instance,
            Pos center,
            String shortServerName,
            String date,
            HudAnchor anchor,
            int mapId
    ) {
        byte[] colors = createEmptyBuffer();
        renderWorld(colors, instance, center);
        applyStandardOverlay(colors);
        drawFacingArrowOnto(colors, center.yaw());
        drawServerHudText(colors, shortServerName, date, "mc.hypixel.net");
        return toPacket(mapId, colors, anchor);
    }

    public byte[] createEmptyBuffer() {
        int mapSize = TexturePackRenderConstants.MAP_SIZE;
        return new byte[mapSize * mapSize];
    }

    public void renderWorld(byte[] colors, Instance instance, Pos center) {
        worldRenderer.renderWorld(colors, instance, center);
    }

    public void applyStandardOverlay(byte[] colors) {
        worldRenderer.applyStandardOverlay(colors);
    }

    public void drawFacingArrowOnto(byte[] colors, float yaw) {
        worldRenderer.drawFacingArrow(colors, yaw);
    }

    public void drawServerHudText(byte[] colors, String shortServerName, String date, String domainText) {
        textRenderer.drawServerHudText(colors, shortServerName, date, domainText);
    }

    public void drawTopBannerText(byte[] colors, String title) {
        textRenderer.drawTopBannerText(colors, title);
    }

    public void drawStatsHud(byte[] colors, String timer, int swords, int players, int squads) {
        textRenderer.drawStatsHud(colors, timer, swords, players, squads);
    }

    public MapDataPacket toPacket(int mapId, byte[] colors, HudAnchor anchor) {
        markerEncoder.writeMarkerSignature(colors, anchor);
        int mapSize = TexturePackRenderConstants.MAP_SIZE;
        return new MapDataPacket(
                mapId,
                (byte) 0,
                false,
                false,
                List.of(),
                new MapDataPacket.ColorContent(
                        (byte) mapSize,
                        (byte) mapSize,
                        (byte) 0,
                        (byte) 0,
                        colors
                )
        );
    }

    public enum HudAnchor {
        TOP_RIGHT(0),
        RIGHT(1),
        BOTTOM_RIGHT(2),
        LEFT(3),
        TOP(4),
        BOTTOM(5),
        TOP_RIGHT_BELOW(6);

        private final int code;

        HudAnchor(int code) {
            this.code = code;
        }

        public int code() {
            return code;
        }
    }
}
