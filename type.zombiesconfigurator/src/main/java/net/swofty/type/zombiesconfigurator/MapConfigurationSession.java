package net.swofty.type.zombiesconfigurator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.Setter;
import net.minestom.server.coordinate.Pos;
import net.swofty.commons.zombies.ZombiesMap;
import net.swofty.commons.zombies.map.ZombiesMapsConfig;
import org.tinylog.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@Getter
@Setter
public class MapConfigurationSession {
    private String mapId;
    private String mapName;
    private ZombiesMap mapType = ZombiesMap.DEAD_END;

    // Locations
    private Pos spawnPosition;
    private Pos spectatorPosition;
    private Pos practiceZombiePosition;

    // Bounds
    private Double boundsMinX, boundsMaxX;
    private Double boundsMinY, boundsMaxY;
    private Double boundsMinZ, boundsMaxZ;

    // Windows
    private Map<String, WindowData> windows = new HashMap<>();

    // Doors
    private Map<String, DoorData> doors = new HashMap<>();

    // Spawners
    private Map<String, SpawnerData> spawners = new HashMap<>();

    // Boss Spawns
    private Map<String, BossSpawnData> bossSpawns = new HashMap<>();

    // Shops
    private Map<String, ShopData> shops = new HashMap<>();

    // Machines
    private Map<String, MachineData> machines = new HashMap<>();

    // Weapon Chests
    private List<Pos> weaponChests = new ArrayList<>();

    // Team Machines
    private List<Pos> teamMachines = new ArrayList<>();

    // Stats Hologram
    private Pos statsHologram;

    public MapConfigurationSession(String mapId, String mapName) {
        this.mapId = mapId;
        this.mapName = mapName;
    }

    @Getter
    @Setter
    public static class WindowData {
        private String name;
        private List<Pos> blockPositions = new ArrayList<>();
        private Pos zombieAreaPos1, zombieAreaPos2;
        private Pos playerAreaPos1, playerAreaPos2;
        private List<String> connectedSpawners = new ArrayList<>();
    }

    @Getter
    @Setter
    public static class DoorData {
        private String name;
        private int goldCost;
        private Pos areaPos1, areaPos2;
        private List<String> connectedDoors = new ArrayList<>();
        private List<String> connectedSpawners = new ArrayList<>();
        private Integer openOnRound;
        private boolean isPowerSwitchDoor;
        private String fakeDoorName;
    }

    @Getter
    @Setter
    public static class SpawnerData {
        private String name;
        private Pos location;
        private boolean isDefault;
        private String requiredDoor;
        private List<String> connectedWindows = new ArrayList<>();
    }

    @Getter
    @Setter
    public static class BossSpawnData {
        private String name;
        private Pos location;
        private String zombieType;
        private boolean isDefault;
    }

    @Getter
    @Setter
    public static class ShopData {
        private ZombiesMapsConfig.ShopType type;
        private Pos location;
        private int goldCost;
        private ArmorShopData armorConfig;
        private WeaponShopData weaponConfig;
        private PerkShopData perkConfig;
    }

    @Getter
    @Setter
    public static class ArmorShopData {
        private String armorType;
        private String armorPart;
    }

    @Getter
    @Setter
    public static class WeaponShopData {
        private String weaponName;
        private int weaponGold;
        private int ammoGold;
    }

    @Getter
    @Setter
    public static class PerkShopData {
        private String perkName;
    }

    @Getter
    @Setter
    public static class MachineData {
        private String type;
        private Pos location;
        private int goldCost;
    }

    public void saveToFile() {
        ZombiesMapsConfig.MapEntry mapEntry = new ZombiesMapsConfig.MapEntry();
        mapEntry.setId(mapId);
        mapEntry.setName(mapName);
        mapEntry.setMapType(mapType);

        ZombiesMapsConfig.MapEntry.MapConfiguration config = new ZombiesMapsConfig.MapEntry.MapConfiguration();

        // Set locations
        if (spawnPosition != null || spectatorPosition != null || practiceZombiePosition != null) {
            ZombiesMapsConfig.MapEntry.MapConfiguration.MapLocations locations = 
                new ZombiesMapsConfig.MapEntry.MapConfiguration.MapLocations();
            
            if (spawnPosition != null) {
                locations.setSpawn(new ZombiesMapsConfig.PitchYawPosition(
                    spawnPosition.x(), spawnPosition.y(), spawnPosition.z(),
                    spawnPosition.pitch(), spawnPosition.yaw()
                ));
            }
            if (spectatorPosition != null) {
                locations.setSpectator(new ZombiesMapsConfig.PitchYawPosition(
                    spectatorPosition.x(), spectatorPosition.y(), spectatorPosition.z(),
                    spectatorPosition.pitch(), spectatorPosition.yaw()
                ));
            }
            if (practiceZombiePosition != null) {
                locations.setPracticeZombie(new ZombiesMapsConfig.Position(
                    practiceZombiePosition.x(), practiceZombiePosition.y(), practiceZombiePosition.z()
                ));
            }
            config.setLocations(locations);
        }

        // Set bounds
        if (boundsMinX != null && boundsMaxX != null && boundsMinY != null && 
            boundsMaxY != null && boundsMinZ != null && boundsMaxZ != null) {
            ZombiesMapsConfig.MapEntry.MapConfiguration.MapBounds bounds = 
                new ZombiesMapsConfig.MapEntry.MapConfiguration.MapBounds();
            bounds.setX(new ZombiesMapsConfig.MinMax(boundsMinX, boundsMaxX));
            bounds.setY(new ZombiesMapsConfig.MinMax(boundsMinY, boundsMaxY));
            bounds.setZ(new ZombiesMapsConfig.MinMax(boundsMinZ, boundsMaxZ));
            config.setBounds(bounds);
        }

        // Set windows
        if (!windows.isEmpty()) {
            Map<String, ZombiesMapsConfig.MapEntry.MapConfiguration.Window> windowMap = new HashMap<>();
            for (Map.Entry<String, WindowData> entry : windows.entrySet()) {
                WindowData wd = entry.getValue();
                ZombiesMapsConfig.MapEntry.MapConfiguration.Window window = 
                    new ZombiesMapsConfig.MapEntry.MapConfiguration.Window();
                window.setName(wd.getName());
                
                List<ZombiesMapsConfig.Position> blockPos = new ArrayList<>();
                for (Pos p : wd.getBlockPositions()) {
                    blockPos.add(new ZombiesMapsConfig.Position(p.x(), p.y(), p.z()));
                }
                window.setBlockPositions(blockPos);
                
                if (wd.getZombieAreaPos1() != null && wd.getZombieAreaPos2() != null) {
                    ZombiesMapsConfig.MapEntry.MapConfiguration.Window.Area zombieArea = 
                        new ZombiesMapsConfig.MapEntry.MapConfiguration.Window.Area();
                    zombieArea.setPos1(new ZombiesMapsConfig.Position(
                        wd.getZombieAreaPos1().x(), wd.getZombieAreaPos1().y(), wd.getZombieAreaPos1().z()
                    ));
                    zombieArea.setPos2(new ZombiesMapsConfig.Position(
                        wd.getZombieAreaPos2().x(), wd.getZombieAreaPos2().y(), wd.getZombieAreaPos2().z()
                    ));
                    window.setZombieArea(zombieArea);
                }
                
                if (wd.getPlayerAreaPos1() != null && wd.getPlayerAreaPos2() != null) {
                    ZombiesMapsConfig.MapEntry.MapConfiguration.Window.Area playerArea = 
                        new ZombiesMapsConfig.MapEntry.MapConfiguration.Window.Area();
                    playerArea.setPos1(new ZombiesMapsConfig.Position(
                        wd.getPlayerAreaPos1().x(), wd.getPlayerAreaPos1().y(), wd.getPlayerAreaPos1().z()
                    ));
                    playerArea.setPos2(new ZombiesMapsConfig.Position(
                        wd.getPlayerAreaPos2().x(), wd.getPlayerAreaPos2().y(), wd.getPlayerAreaPos2().z()
                    ));
                    window.setPlayerArea(playerArea);
                }
                
                window.setConnectedSpawners(wd.getConnectedSpawners());
                windowMap.put(entry.getKey(), window);
            }
            config.setWindows(windowMap);
        }

        // Set doors
        if (!doors.isEmpty()) {
            Map<String, ZombiesMapsConfig.MapEntry.MapConfiguration.Door> doorMap = new HashMap<>();
            for (Map.Entry<String, DoorData> entry : doors.entrySet()) {
                DoorData dd = entry.getValue();
                ZombiesMapsConfig.MapEntry.MapConfiguration.Door door = 
                    new ZombiesMapsConfig.MapEntry.MapConfiguration.Door();
                door.setName(dd.getName());
                door.setGoldCost(dd.getGoldCost());
                
                if (dd.getAreaPos1() != null && dd.getAreaPos2() != null) {
                    ZombiesMapsConfig.MapEntry.MapConfiguration.Door.Area area = 
                        new ZombiesMapsConfig.MapEntry.MapConfiguration.Door.Area();
                    area.setPos1(new ZombiesMapsConfig.Position(
                        dd.getAreaPos1().x(), dd.getAreaPos1().y(), dd.getAreaPos1().z()
                    ));
                    area.setPos2(new ZombiesMapsConfig.Position(
                        dd.getAreaPos2().x(), dd.getAreaPos2().y(), dd.getAreaPos2().z()
                    ));
                    door.setArea(area);
                }
                
                door.setConnectedDoors(dd.getConnectedDoors());
                door.setConnectedSpawners(dd.getConnectedSpawners());
                door.setOpenOnRound(dd.getOpenOnRound());
                door.setPowerSwitchDoor(dd.isPowerSwitchDoor());
                door.setFakeDoorName(dd.getFakeDoorName());
                doorMap.put(entry.getKey(), door);
            }
            config.setDoors(doorMap);
        }

        // Set spawners
        if (!spawners.isEmpty()) {
            Map<String, ZombiesMapsConfig.MapEntry.MapConfiguration.Spawner> spawnerMap = new HashMap<>();
            for (Map.Entry<String, SpawnerData> entry : spawners.entrySet()) {
                SpawnerData sd = entry.getValue();
                ZombiesMapsConfig.MapEntry.MapConfiguration.Spawner spawner = 
                    new ZombiesMapsConfig.MapEntry.MapConfiguration.Spawner();
                spawner.setName(sd.getName());
                if (sd.getLocation() != null) {
                    spawner.setLocation(new ZombiesMapsConfig.Position(
                        sd.getLocation().x(), sd.getLocation().y(), sd.getLocation().z()
                    ));
                }
                spawner.setDefault(sd.isDefault());
                spawner.setRequiredDoor(sd.getRequiredDoor());
                spawner.setConnectedWindows(sd.getConnectedWindows());
                spawnerMap.put(entry.getKey(), spawner);
            }
            config.setSpawners(spawnerMap);
        }

        // Set boss spawns
        if (!bossSpawns.isEmpty()) {
            Map<String, ZombiesMapsConfig.MapEntry.MapConfiguration.BossSpawn> bossSpawnMap = new HashMap<>();
            for (Map.Entry<String, BossSpawnData> entry : bossSpawns.entrySet()) {
                BossSpawnData bsd = entry.getValue();
                ZombiesMapsConfig.MapEntry.MapConfiguration.BossSpawn bossSpawn = 
                    new ZombiesMapsConfig.MapEntry.MapConfiguration.BossSpawn();
                bossSpawn.setName(bsd.getName());
                if (bsd.getLocation() != null) {
                    bossSpawn.setLocation(new ZombiesMapsConfig.Position(
                        bsd.getLocation().x(), bsd.getLocation().y(), bsd.getLocation().z()
                    ));
                }
                bossSpawn.setZombieType(bsd.getZombieType());
                bossSpawn.setDefault(bsd.isDefault());
                bossSpawnMap.put(entry.getKey(), bossSpawn);
            }
            config.setBossSpawns(bossSpawnMap);
        }

        // Set shops
        if (!shops.isEmpty()) {
            Map<String, ZombiesMapsConfig.MapEntry.MapConfiguration.Shop> shopMap = new HashMap<>();
            for (Map.Entry<String, ShopData> entry : shops.entrySet()) {
                ShopData sd = entry.getValue();
                ZombiesMapsConfig.MapEntry.MapConfiguration.Shop shop = 
                    new ZombiesMapsConfig.MapEntry.MapConfiguration.Shop();
                shop.setType(sd.getType());
                if (sd.getLocation() != null) {
                    shop.setLocation(new ZombiesMapsConfig.Position(
                        sd.getLocation().x(), sd.getLocation().y(), sd.getLocation().z()
                    ));
                }
                shop.setGoldCost(sd.getGoldCost());
                
                if (sd.getArmorConfig() != null) {
                    ZombiesMapsConfig.MapEntry.MapConfiguration.Shop.ArmorShopConfig armorConfig = 
                        new ZombiesMapsConfig.MapEntry.MapConfiguration.Shop.ArmorShopConfig();
                    armorConfig.setArmorType(ZombiesMapsConfig.MapEntry.MapConfiguration.Shop.ArmorShopConfig.ArmorType
                        .valueOf(sd.getArmorConfig().getArmorType()));
                    armorConfig.setArmorPart(ZombiesMapsConfig.MapEntry.MapConfiguration.Shop.ArmorShopConfig.ArmorPart
                        .valueOf(sd.getArmorConfig().getArmorPart()));
                    shop.setArmorConfig(armorConfig);
                }
                
                if (sd.getWeaponConfig() != null) {
                    ZombiesMapsConfig.MapEntry.MapConfiguration.Shop.WeaponShopConfig weaponConfig = 
                        new ZombiesMapsConfig.MapEntry.MapConfiguration.Shop.WeaponShopConfig();
                    weaponConfig.setWeaponName(sd.getWeaponConfig().getWeaponName());
                    weaponConfig.setWeaponGold(sd.getWeaponConfig().getWeaponGold());
                    weaponConfig.setAmmoGold(sd.getWeaponConfig().getAmmoGold());
                    shop.setWeaponConfig(weaponConfig);
                }
                
                if (sd.getPerkConfig() != null) {
                    ZombiesMapsConfig.MapEntry.MapConfiguration.Shop.PerkShopConfig perkConfig = 
                        new ZombiesMapsConfig.MapEntry.MapConfiguration.Shop.PerkShopConfig();
                    perkConfig.setPerkName(sd.getPerkConfig().getPerkName());
                    shop.setPerkConfig(perkConfig);
                }
                
                shopMap.put(entry.getKey(), shop);
            }
            config.setShops(shopMap);
        }

        // Set machines
        if (!machines.isEmpty()) {
            Map<String, ZombiesMapsConfig.MapEntry.MapConfiguration.Machine> machineMap = new HashMap<>();
            for (Map.Entry<String, MachineData> entry : machines.entrySet()) {
                MachineData md = entry.getValue();
                ZombiesMapsConfig.MapEntry.MapConfiguration.Machine machine = 
                    new ZombiesMapsConfig.MapEntry.MapConfiguration.Machine();
                machine.setType(ZombiesMapsConfig.MapEntry.MapConfiguration.Machine.MachineType
                    .valueOf(md.getType()));
                if (md.getLocation() != null) {
                    machine.setLocation(new ZombiesMapsConfig.Position(
                        md.getLocation().x(), md.getLocation().y(), md.getLocation().z()
                    ));
                }
                machine.setGoldCost(md.getGoldCost());
                machineMap.put(entry.getKey(), machine);
            }
            config.setMachines(machineMap);
        }

        // Set weapon chests
        if (!weaponChests.isEmpty()) {
            List<ZombiesMapsConfig.Position> chestList = new ArrayList<>();
            for (Pos p : weaponChests) {
                chestList.add(new ZombiesMapsConfig.Position(p.x(), p.y(), p.z()));
            }
            config.setWeaponChests(chestList);
        }

        // Set team machines
        if (!teamMachines.isEmpty()) {
            List<ZombiesMapsConfig.Position> teamMachineList = new ArrayList<>();
            for (Pos p : teamMachines) {
                teamMachineList.add(new ZombiesMapsConfig.Position(p.x(), p.y(), p.z()));
            }
            config.setTeamMachines(teamMachineList);
        }

        // Set stats hologram
        if (statsHologram != null) {
            config.setStatsHologram(new ZombiesMapsConfig.Position(
                statsHologram.x(), statsHologram.y(), statsHologram.z()
            ));
        }

        mapEntry.setConfiguration(config);

        // Save to file
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File outputFile = new File("configuration/zombies/" + mapId + ".json");
        outputFile.getParentFile().mkdirs();

        try (FileWriter writer = new FileWriter(outputFile)) {
            gson.toJson(mapEntry, writer);
            Logger.info("Saved Zombies map configuration to " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            Logger.error(e, "Failed to save Zombies map configuration");
        }
    }
}
