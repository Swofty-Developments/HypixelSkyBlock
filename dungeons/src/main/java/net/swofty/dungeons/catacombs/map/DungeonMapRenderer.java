package net.swofty.dungeons.catacombs.map;

import net.swofty.dungeons.SkyBlockDungeon;

import javax.imageio.ImageIO;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class DungeonMapRenderer {
    private static final int TILE = 48;
    private static final int GAP = 14;
    private static final int PADDING = 24;

    public DungeonMapRenderResult renderPng(SkyBlockDungeon dungeon, Path outputPath) throws IOException {
        int maxX = dungeon.getRooms().keySet().stream().mapToInt(Map.Entry::getKey).max().orElse(0);
        int maxY = dungeon.getRooms().keySet().stream().mapToInt(Map.Entry::getValue).max().orElse(0);
        int width = PADDING * 2 + (maxX + 1) * TILE + maxX * GAP;
        int height = PADDING * 2 + (maxY + 1) * TILE + maxY * GAP;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setColor(new Color(0x151515));
        graphics.fillRect(0, 0, width, height);
        graphics.setStroke(new BasicStroke(6, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        graphics.setColor(new Color(0xD9C7A7));
        for (SkyBlockDungeon.DungeonDoor door : dungeon.getDoorConnections()) {
            int x1 = center(door.x1());
            int y1 = center(door.y1());
            int x2 = center(door.x2());
            int y2 = center(door.y2());
            graphics.drawLine(x1, y1, x2, y2);
        }
        dungeon.getRooms().forEach((point, room) -> {
            int x = origin(point.getKey());
            int y = origin(point.getValue());
            graphics.setColor(DungeonMapPalette.forRoom(room.getRoomType()));
            graphics.fillRoundRect(x, y, TILE, TILE, 8, 8);
            graphics.setColor(room.isCritical() ? Color.WHITE : new Color(0x2A2A2A));
            graphics.setStroke(new BasicStroke(room.isCritical() ? 3 : 1));
            graphics.drawRoundRect(x, y, TILE, TILE, 8, 8);
        });
        graphics.dispose();
        Files.createDirectories(outputPath.getParent());
        ImageIO.write(image, "png", outputPath.toFile());
        return new DungeonMapRenderResult(outputPath, width, height);
    }

    private int origin(int coordinate) {
        return PADDING + coordinate * (TILE + GAP);
    }

    private int center(int coordinate) {
        return origin(coordinate) + TILE / 2;
    }
}
