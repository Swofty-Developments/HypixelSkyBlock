package net.swofty.type.generic.utility;

import lombok.Getter;
import net.minestom.server.component.DataComponents;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.metadata.other.ItemFrameMeta;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.network.packet.server.play.MapDataPacket;
import net.minestom.server.utils.Direction;
import net.swofty.type.generic.user.HypixelPlayer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.GZIPInputStream;

public abstract class AbstractMapSystem {
    private static final ConcurrentHashMap<Integer, byte[]> MAP_COLOR_CACHE = new ConcurrentHashMap<>(64, 0.75f, 1);

    private MapDataPacket[] mapPacketCache;
    private volatile boolean initialized = false;

    protected abstract MapConfiguration getConfiguration();
    protected abstract String[] getCompressedMapData();

    private void initializeMapPackets() {
        if (initialized) return;

        synchronized (this) {
            if (initialized) return;

            MapConfiguration config = getConfiguration();
            String[] compressedData = getCompressedMapData();
            int totalMaps = config.getColumns() * config.getRows();

            if (compressedData.length != totalMaps) {
                throw new IllegalStateException(
                        "Expected " + totalMaps + " map data strings, got " + compressedData.length
                );
            }

            mapPacketCache = new MapDataPacket[totalMaps];

            for (int i = 0; i < totalMaps; i++) {
                mapPacketCache[i] = new MapDataPacket(
                        1 + i,
                        (byte) 0,
                        false,
                        false,
                        List.of(),
                        new MapDataPacket.ColorContent(
                                (byte) 128,
                                (byte) 128,
                                (byte) 0,
                                (byte) 0,
                                decodeMapColors(compressedData[i])
                        )
                );
            }

            initialized = true;
        }
    }

    public void placeItemFrames(Instance instance) {
        MapConfiguration c = getConfiguration();
        int yStart = c.topLeft.blockY();

        for (int row = 0; row < c.rows; row++) {
            int y = yStart - row;

            for (int col = 0; col < c.columns; col++) {
                Pos pos = c.facing.resolvePosition(c.topLeft, col, y);

                Entity frame = new Entity(EntityType.ITEM_FRAME);
                int index = row * c.columns + col;
                int id = 1 + index;

                frame.editEntityMeta(ItemFrameMeta.class, meta -> {
                    meta.setItem(
                            ItemStack.builder(Material.FILLED_MAP)
                                    .set(DataComponents.MAP_ID, id)
                                    .build()
                    );
                    meta.setDirection(c.facing.getAttachmentFace());
                });

                frame.setInstance(instance, pos);
            }
        }
    }

    public void sendMapData(HypixelPlayer player) {
        if (!initialized) {
            initializeMapPackets();
        }
        player.sendPackets(mapPacketCache);
    }

    protected byte[] decodeMapColors(String compressedData) {
        int cacheKey = compressedData.hashCode();
        byte[] cached = MAP_COLOR_CACHE.get(cacheKey);
        if (cached != null) {
            return cached;
        }

        try {
            byte[] decompressed = Base64.getDecoder().decode(compressedData);
            try (ByteArrayInputStream bais = new ByteArrayInputStream(decompressed);
                 GZIPInputStream gis = new GZIPInputStream(bais)) {
                byte[] result = gis.readAllBytes();
                MAP_COLOR_CACHE.put(cacheKey, result);
                return result;
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to decode map colors for " + getClass().getSimpleName(), e);
        }
    }

    public static class MapConfiguration {
        @Getter private final Pos topLeft;
        @Getter private final Pos bottomRight;
        @Getter private final MapFacing facing;

        @Getter private final int columns;
        @Getter private final int rows;

        public MapConfiguration(Pos topLeft, Pos bottomRight, MapFacing facing) {
            this.topLeft = topLeft;
            this.bottomRight = bottomRight;
            this.facing = facing;

            this.rows = topLeft.blockY() - bottomRight.blockY() + 1;

            if (facing == MapFacing.WEST || facing == MapFacing.EAST) {
                this.columns = Math.abs(topLeft.blockZ() - bottomRight.blockZ()) + 1;
            } else {
                this.columns = Math.abs(topLeft.blockX() - bottomRight.blockX()) + 1;
            }
        }
    }

    public enum MapFacing {
        WEST {
            public Pos resolvePosition(Pos tl, int col, int y) {
                return new Pos(tl.blockX(), y, tl.blockZ() - col + 0.5);
            }
            public Direction getAttachmentFace() { return Direction.EAST; }
        },
        EAST {
            public Pos resolvePosition(Pos tl, int col, int y) {
                return new Pos(tl.blockX(), y, tl.blockZ() + col + 0.5);
            }
            public Direction getAttachmentFace() { return Direction.WEST; }
        },
        NORTH {
            public Pos resolvePosition(Pos tl, int col, int y) {
                return new Pos(tl.blockX() + col + 0.5, y, tl.blockZ());
            }
            public Direction getAttachmentFace() { return Direction.SOUTH; }
        },
        SOUTH {
            public Pos resolvePosition(Pos tl, int col, int y) {
                return new Pos(tl.blockX() - col + 0.5, y, tl.blockZ());
            }
            public Direction getAttachmentFace() { return Direction.NORTH; }
        };

        public abstract Pos resolvePosition(Pos topLeft, int col, int y);
        public abstract Direction getAttachmentFace();
    }

}