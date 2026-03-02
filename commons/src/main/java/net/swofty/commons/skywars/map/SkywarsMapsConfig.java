package net.swofty.commons.skywars.map;

import lombok.Getter;
import lombok.Setter;
import net.swofty.commons.mc.HypixelPosition;
import net.swofty.commons.skywars.SkywarsGameType;

import java.util.List;

@Getter
@Setter
@SuppressWarnings("unused")
public class SkywarsMapsConfig {
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
            private List<SkywarsGameType> types;
            private HypixelPosition center;
            private List<IslandSpawn> islands;
            private int voidY;
            private MapBounds bounds;
        }
    }

    @Getter
    @Setter
    public static class IslandSpawn {
        private int teamId;
        private HypixelPosition cageCenter;
    }

    public record MapBounds(HypixelPosition min, HypixelPosition max) {
        public boolean isWithinBounds(double x, double y, double z) {
            if (min == null || max == null) return true;
            double minX = Math.min(min.x(), max.x());
            double maxX = Math.max(min.x(), max.x());
            double minZ = Math.min(min.z(), max.z());
            double maxZ = Math.max(min.z(), max.z());

            return x >= minX && x <= maxX && z >= minZ && z <= maxZ;
        }
    }
}
