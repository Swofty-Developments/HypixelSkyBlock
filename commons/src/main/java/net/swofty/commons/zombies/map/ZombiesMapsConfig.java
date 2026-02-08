package net.swofty.commons.zombies.map;

import lombok.Getter;
import lombok.Setter;
import net.swofty.commons.zombies.ZombiesMap;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@SuppressWarnings("unused")
public class ZombiesMapsConfig {
    private List<MapEntry> maps;

    @Getter
    @Setter
    public static class MapEntry {
        private String id;
        private String name;
        private ZombiesMap mapType;
        private MapConfiguration configuration;

        @Getter
        @Setter
        public static class MapConfiguration {
            private MapBounds bounds;
            private MapLocations locations;
            private Map<String, Window> windows;
            private Map<String, Door> doors;
            private Map<String, Spawner> spawners;
            private Map<String, BossSpawn> bossSpawns;
            private Map<String, Shop> shops;
            private Map<String, Machine> machines;
            private List<Position> weaponChests;
            private List<Position> teamMachines;
            private Position statsHologram;

            @Getter
            @Setter
            public static class MapLocations {
                private PitchYawPosition spawn;
                private PitchYawPosition spectator;
                private Position practiceZombie;
            }

            @Getter
            @Setter
            public static class MapBounds {
                private MinMax x;
                private MinMax y;
                private MinMax z;
            }

            @Getter
            @Setter
            public static class Window {
                private String name;
                private List<Position> blockPositions;
                private Area zombieArea;
                private Area playerArea;
                private List<String> connectedSpawners;

                @Getter
                @Setter
                public static class Area {
                    private Position pos1;
                    private Position pos2;
                }
            }

            @Getter
            @Setter
            public static class Door {
                private String name;
                private int goldCost;
                private Area area;
                private List<String> connectedDoors;
                private List<String> connectedSpawners;
                private Integer openOnRound;
                private boolean isPowerSwitchDoor;
                private String fakeDoorName;

                @Getter
                @Setter
                public static class Area {
                    private Position pos1;
                    private Position pos2;
                }
            }

            @Getter
            @Setter
            public static class Spawner {
                private String name;
                private Position location;
                private boolean isDefault;
                private String requiredDoor;
                private List<String> connectedWindows;
            }

            @Getter
            @Setter
            public static class BossSpawn {
                private String name;
                private Position location;
                private String zombieType;
                private boolean isDefault;
            }

            @Getter
            @Setter
            public static class Shop {
                private ShopType type;
                private Position location;
                private int goldCost;
                private ArmorShopConfig armorConfig;
                private WeaponShopConfig weaponConfig;
                private PerkShopConfig perkConfig;

                @Getter
                @Setter
                public static class ArmorShopConfig {
                    private ArmorType armorType;
                    private ArmorPart armorPart;

                    public enum ArmorType {
                        LEATHER, GOLD, IRON, DIAMOND
                    }

                    public enum ArmorPart {
                        UP, DOWN
                    }
                }

                @Getter
                @Setter
                public static class WeaponShopConfig {
                    private String weaponName;
                    private int weaponGold;
                    private int ammoGold;
                }

                @Getter
                @Setter
                public static class PerkShopConfig {
                    private String perkName;
                }
            }

            @Getter
            @Setter
            public static class Machine {
                private MachineType type;
                private Position location;
                private int goldCost;

                public enum MachineType {
                    ULTIMATE_MACHINE,
                    POWER_SWITCH,
                    TEAM_MACHINE
                }
            }
        }
    }

    public record Position(double x, double y, double z) {
    }

    public record PitchYawPosition(double x, double y, double z, float pitch, float yaw) {
    }

    public record MinMax(double min, double max) {
    }

    public enum ShopType {
        ARMOR,
        WEAPON,
        PERK
    }
}
