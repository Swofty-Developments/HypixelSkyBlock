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

    public static RavengardRoomCatalog load(Path path) {
        try {
            return new Gson().fromJson(Files.readString(path), RavengardRoomCatalog.class);
        } catch (Exception exception) {
            throw new IllegalStateException("Could not load room catalog " + path, exception);
        }
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
        @SerializedName("h") private int depth;
        private String color;
        private boolean letterTile;
        private List<DoorSocket> sockets;
        private List<RoomObject> objects;

        public List<DoorSocket> getSockets() {
            return sockets == null ? List.of() : sockets;
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
