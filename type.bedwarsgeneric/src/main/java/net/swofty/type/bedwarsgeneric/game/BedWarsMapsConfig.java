package net.swofty.type.bedwarsgeneric.game;

import lombok.Getter;
import net.swofty.commons.BedwarsGameType;

import java.util.List;
import java.util.Map;

@Getter
@SuppressWarnings("unused")
public class BedWarsMapsConfig {
    private List<MapEntry> maps;

    @Getter
    public static class MapEntry {
        private String id;
        private String name;
        private MapConfiguration configuration;

        @Getter
        public static class MapConfiguration {
            private List<BedwarsGameType> types;
            private Map<String, TeamGeneratorConfig> generator;
            private MapBounds bounds;
            private Map<TeamKey, MapTeam> teams;
            private MapLocations locations;
            private Map<String, GlobalGenerator> global_generator;

            @Getter
            public static class MapLocations {
                private Position waiting;
                private Position spectator;
            }

            @Getter
            public static class MapBounds {
                private MinMax x;
                private MinMax y;
                private MinMax z;
            }

            @Getter
            public static class TeamGeneratorConfig {
                private int delay;
                private int amount;
            }

            @Getter
            public static class GlobalGenerator {
                private int amount;
                private int max;
                private List<Position> locations;
            }

        }
    }

    public record Position(double x, double y, double z) {
    }

    public record PitchYawPosition(double x, double y, double z, float pitch, float yaw) {
    }

    public record TwoBlockPosition(Position feet, Position head) {
    }

    public record MinMax(double min, double max) {
    }

    public enum TeamKey {
        RED("Red", "§c", 0xFF0000),
        BLUE("Blue", "§9", 0x5555FF),
        GREEN("Green", "§a", 0x55FF55),
        YELLOW("Yellow", "§e", 0xFFFF55),
        AQUA("Aqua", "§b", 0x00AAAA),
        WHITE("White", "§f", 0xFFFFFF),
        PINK("Pink", "§d", 0xFF55FF),
        GRAY("Gray", "§7", 0xAAAAAA);

        @Getter
        private final String name;
        private final String chatColor;
        private final int armorColor;

        TeamKey(String name, String chatColor, int rbg) {
            this.name = name;
            this.chatColor = chatColor;
            this.armorColor = rbg;
        }

        public int rgb() {
            return armorColor;
        }

        public String chatColor() {
            return chatColor;
        }
    }

    @Getter
    public static class MapTeam {
        private Shops shop;
        private PitchYawPosition spawn;
        private TwoBlockPosition bed;
        private Position generator;

        public record Shops(PitchYawPosition item, PitchYawPosition team) {
        }
    }

}
