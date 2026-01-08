package net.swofty.type.skywarsconfigurator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import net.minestom.server.coordinate.Pos;
import net.swofty.commons.skywars.SkywarsGameType;
import net.swofty.commons.skywars.map.SkywarsMapsConfig;
import org.tinylog.Logger;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Tracks the current map configuration session.
 */
@Getter
public class MapConfigurationSession {
    private final String mapId;
    private final String mapName;
    private final List<IslandConfig> islands = new ArrayList<>();
    private final List<SkywarsGameType> types = new ArrayList<>();
    private double centerX, centerY, centerZ;
    private int voidY = 0;
    private int minX, minZ, maxX, maxZ;

    public MapConfigurationSession(String mapId, String mapName) {
        this.mapId = mapId;
        this.mapName = mapName;
    }

    public void setCenter(double x, double y, double z) {
        this.centerX = x;
        this.centerY = y;
        this.centerZ = z;
    }

    public void setVoidY(int y) {
        this.voidY = y;
    }

    public void setBounds(int minX, int minZ, int maxX, int maxZ) {
        this.minX = minX;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxZ = maxZ;
    }

    public void addIsland(Pos spawnPos) {
        IslandConfig island = new IslandConfig(islands.size(), spawnPos);
        islands.add(island);
    }

    public void addType(SkywarsGameType type) {
        if (!types.contains(type)) {
            types.add(type);
        }
    }

    public void removeType(SkywarsGameType type) {
        types.remove(type);
    }

    public SkywarsMapsConfig.MapEntry toMapEntry() {
        SkywarsMapsConfig.MapEntry entry = new SkywarsMapsConfig.MapEntry();
        entry.setId(mapId);
        entry.setName(mapName);

        SkywarsMapsConfig.MapEntry.MapConfiguration config = new SkywarsMapsConfig.MapEntry.MapConfiguration();
        config.setTypes(types.isEmpty() ? List.of(SkywarsGameType.SOLO_NORMAL) : new ArrayList<>(types));
        config.setVoidY(voidY);

        // Set center position
        config.setCenter(new SkywarsMapsConfig.PitchYawPosition(centerX, centerY, centerZ, 0, 0));

        // Set bounds
        SkywarsMapsConfig.Position minPos = new SkywarsMapsConfig.Position(minX, 0, minZ);
        SkywarsMapsConfig.Position maxPos = new SkywarsMapsConfig.Position(maxX, 256, maxZ);
        config.setBounds(new SkywarsMapsConfig.MapBounds(minPos, maxPos));

        // Set islands (chests are auto-detected at runtime by ChestScanner)
        List<SkywarsMapsConfig.IslandSpawn> islandList = new ArrayList<>();
        for (IslandConfig island : islands) {
            SkywarsMapsConfig.IslandSpawn spawn = new SkywarsMapsConfig.IslandSpawn();
            spawn.setTeamId(island.getIslandId());
            spawn.setCageCenter(new SkywarsMapsConfig.PitchYawPosition(
                    island.getSpawnX(), island.getSpawnY(), island.getSpawnZ(), 0, 0));
            islandList.add(spawn);
        }
        config.setIslands(islandList);

        entry.setConfiguration(config);
        return entry;
    }

    public void saveToFile() {
        SkywarsMapsConfig.MapEntry entry = toMapEntry();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(entry);

        File outputDir = new File("./configuration/skywars/");
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        File outputFile = new File(outputDir, mapId + "_config.json");
        try (FileWriter writer = new FileWriter(outputFile)) {
            writer.write(json);
            Logger.info("Saved map configuration to " + outputFile.getAbsolutePath());
        } catch (Exception e) {
            Logger.error("Failed to save map configuration", e);
        }
    }

    @Getter
    public static class IslandConfig {
        private final int islandId;
        private final double spawnX, spawnY, spawnZ;

        public IslandConfig(int islandId, Pos spawnPos) {
            this.islandId = islandId;
            this.spawnX = spawnPos.x();
            this.spawnY = spawnPos.y();
            this.spawnZ = spawnPos.z();
        }
    }
}
