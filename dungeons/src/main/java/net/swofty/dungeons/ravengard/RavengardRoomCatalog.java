package net.swofty.dungeons.ravengard;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Getter
public final class RavengardRoomCatalog {
    private String source;
    private int floorY;
    private int roofY;
    private List<DungeonRoom> rooms;
    private DungeonRoom citadel;

    public static RavengardRoomCatalog load(Path path) {
        try {
            return new Gson().fromJson(Files.readString(path), RavengardRoomCatalog.class);
        } catch (Exception exception) {
            throw new IllegalStateException("Could not load room catalog " + path, exception);
        }
    }

    public DungeonRoom getCitadel() {
        return citadel;
    }

    public List<DungeonRoom> getRooms() {
        return rooms == null ? List.of() : rooms;
    }

    @Getter
    public static final class DungeonRoom {
        private String id;
        @SerializedName("x0") private int minX;
        @SerializedName("z0") private int minZ;
        @SerializedName("x1") private int maxX;
        @SerializedName("z1") private int maxZ;
        @SerializedName("w") private int width;
        private Integer roofY;
        @SerializedName("h") private int depth;
        private String color;
        private boolean letterTile;
        private List<int[]> mask;
        private List<DoorSocket> sockets;
        private List<RoomObject> objects;

        public List<DoorSocket> getSockets() {
            return sockets == null ? List.of() : sockets;
        }

        /** Footprint rows as [rowZ, startX, endX] runs; falls back to the full box. */
        public List<int[]> getMask() {
            if (mask != null && !mask.isEmpty()) {
                return mask;
            }
            List<int[]> full = new java.util.ArrayList<>();
            for (int rowZ = 0; rowZ < depth; rowZ++) {
                full.add(new int[]{rowZ, 0, width - 1});
            }
            return full;
        }

        public List<RoomObject> getObjects() {
            return objects == null ? List.of() : objects;
        }
    }

    @Getter
    public static final class DoorSocket {
        private Direction side;
        private double x;
        private double y;
        private double z;
        private double width;
    }

    @Getter
    public static final class RoomObject {
        private String category;
        private String type;
        private double x;
        private double y;
        private double z;
        private double yaw;
    }
}
