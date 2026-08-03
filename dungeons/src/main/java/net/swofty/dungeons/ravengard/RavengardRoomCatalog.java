package net.swofty.dungeons.ravengard;

import com.google.gson.Gson;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class RavengardRoomCatalog {
    private String source;
    private int floorY;
    private int roofY;
    private List<Room> rooms;

    public static RavengardRoomCatalog load(Path path) {
        try {
            return new Gson().fromJson(Files.readString(path), RavengardRoomCatalog.class);
        } catch (Exception exception) {
            throw new IllegalStateException("Could not load room catalog " + path, exception);
        }
    }

    public String source() {
        return source;
    }

    public int floorY() {
        return floorY;
    }

    public int roofY() {
        return roofY;
    }

    public List<Room> rooms() {
        return rooms == null ? List.of() : rooms;
    }

    public static final class Room {
        private String id;
        private int x0, z0, x1, z1;
        private int w, h;
        private String color;
        private boolean letterTile;
        private List<Socket> sockets;
        private List<ObjectSpawn> objects;

        public String id() {
            return id;
        }

        public int x0() {
            return x0;
        }

        public int z0() {
            return z0;
        }

        public int x1() {
            return x1;
        }

        public int z1() {
            return z1;
        }

        public int width() {
            return w;
        }

        public int depth() {
            return h;
        }

        public String color() {
            return color;
        }

        public boolean letterTile() {
            return letterTile;
        }

        public List<Socket> sockets() {
            return sockets == null ? List.of() : sockets;
        }

        public List<ObjectSpawn> objects() {
            return objects == null ? List.of() : objects;
        }
    }

    public static final class Socket {
        private String side;
        private double x, y, z;
        private double width;

        public Socket() {
        }

        public Socket(String side, double x, double y, double z, double width) {
            this.side = side;
            this.x = x;
            this.y = y;
            this.z = z;
            this.width = width;
        }

        public String side() {
            return side;
        }

        public double x() {
            return x;
        }

        public double y() {
            return y;
        }

        public double z() {
            return z;
        }

        public double width() {
            return width;
        }

        public static String oppositeOf(String side) {
            return switch (side) {
                case "north" -> "south";
                case "south" -> "north";
                case "east" -> "west";
                default -> "east";
            };
        }
    }

    public static final class ObjectSpawn {
        private String category;
        private String type;
        private double x, y, z, yaw;

        public String category() {
            return category;
        }

        public String type() {
            return type;
        }

        public double x() {
            return x;
        }

        public double y() {
            return y;
        }

        public double z() {
            return z;
        }

        public double yaw() {
            return yaw;
        }
    }
}
