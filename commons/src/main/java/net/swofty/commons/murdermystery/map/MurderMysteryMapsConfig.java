package net.swofty.commons.murdermystery.map;

import lombok.Getter;
import lombok.Setter;
import net.swofty.commons.murdermystery.MurderMysteryGameType;

import java.util.List;

@Getter
@Setter
@SuppressWarnings("unused")
public class MurderMysteryMapsConfig {
    private List<MapEntry> maps;

    @Getter
    @Setter
    public static class MapEntry {
        private String id;
        private String name;
        private MapConfiguration configuration;

        @Getter
        @Setter
        public static class MapConfiguration {
            private List<MurderMysteryGameType> types;
            private MapLocations locations;
            private List<Position> goldSpawns;
            private List<Position> playerSpawns;
            private List<KillRegion> killRegions;

            @Getter
            @Setter
            public static class MapLocations {
                private PitchYawPosition waiting;
            }
        }
    }

    public record Position(double x, double y, double z) {
    }

    public record PitchYawPosition(double x, double y, double z, float pitch, float yaw) {
    }

    public record KillRegion(String name, Position min, Position max) {
        public boolean isComplete() {
            return min != null && max != null;
        }

        public boolean contains(double x, double y, double z) {
            if (!isComplete()) return false;
            double minX = Math.min(min.x(), max.x());
            double maxX = Math.max(min.x(), max.x());
            double minY = Math.min(min.y(), max.y());
            double maxY = Math.max(min.y(), max.y());
            double minZ = Math.min(min.z(), max.z());
            double maxZ = Math.max(min.z(), max.z());

            // Check X and Z bounds
            if (x < minX || x > maxX || z < minZ || z > maxZ) {
                return false;
            }

            // If min.y == max.y (flat plane), treat as "below Y" death zone
            // This is common for water/void death zones
            if (Math.abs(minY - maxY) < 0.01) {
                return y <= minY;
            }

            // Normal box check
            return y >= minY && y <= maxY;
        }
    }
}
