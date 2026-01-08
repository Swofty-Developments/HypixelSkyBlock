package net.swofty.type.skywarsconfigurator.commands;

import net.hollowcube.polar.AnvilPolar;
import net.hollowcube.polar.PolarLoader;
import net.hollowcube.polar.PolarWorld;
import net.hollowcube.polar.PolarWriter;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.InstanceContainer;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.user.categories.Rank;
import net.swofty.type.skywarsconfigurator.MapConfigurationSession;
import net.swofty.type.skywarsconfigurator.TypeSkywarsConfiguratorLoader;
import org.tinylog.Logger;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@CommandParameters(aliases = "choose choosemap selectmap select",
        description = "Choose a SkyWars map to configure",
        usage = "/choosemap <map>",
        permission = Rank.STAFF,
        allowsConsole = false)
public class ChooseMapCommand extends HypixelCommand {

    private static final Path CONFIG_PATH = Path.of("./configuration/skywars/");

    @Override
    public void registerUsage(MinestomCommand command) {
        var mapArg = ArgumentType.String("map");
        mapArg.setSuggestionCallback((sender, context, suggestion) -> {
            Set<String> addedIds = new HashSet<>();

            // Add polar files
            File configDir = CONFIG_PATH.toFile();
            if (configDir.exists() && configDir.isDirectory()) {
                File[] polarFiles = configDir.listFiles((dir, name) -> name.endsWith(".polar"));
                if (polarFiles != null) {
                    for (File polarFile : polarFiles) {
                        String mapId = polarFile.getName().replace(".polar", "");
                        if (!addedIds.contains(mapId.toLowerCase())) {
                            suggestion.addEntry(new SuggestionEntry(mapId, Component.text(mapId + " §a(polar)")));
                            addedIds.add(mapId.toLowerCase());
                        }
                    }
                }

                // Add Anvil world folders (need conversion)
                File[] anvilFolders = configDir.listFiles(file -> {
                    if (!file.isDirectory()) return false;
                    // Check if it's an Anvil world (has region folder)
                    File regionDir = new File(file, "region");
                    return regionDir.exists() && regionDir.isDirectory();
                });
                if (anvilFolders != null) {
                    for (File anvilFolder : anvilFolders) {
                        String mapId = anvilFolder.getName();
                        if (!addedIds.contains(mapId.toLowerCase())) {
                            suggestion.addEntry(new SuggestionEntry(mapId, Component.text(mapId + " §c(anvil - needs conversion)")));
                        }
                    }
                }
            }
        });

        command.addSyntax((sender, context) -> {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(Component.text("§cThis command can only be executed by a player."));
                return;
            }
            String mapId = context.get("map");

            File polarFile = CONFIG_PATH.resolve(mapId + ".polar").toFile();
            File anvilFolder = CONFIG_PATH.resolve(mapId).toFile();
            File regionDir = new File(anvilFolder, "region");

            // Check if we need to convert from Anvil
            if (!polarFile.exists() && anvilFolder.exists() && regionDir.exists()) {
                player.sendMessage(Component.text("§eFound Anvil world folder for: " + mapId));
                player.sendMessage(Component.text("§eConverting to Polar format... This may take a moment."));

                try {
                    convertAnvilToPolar(player, mapId, anvilFolder, polarFile);
                } catch (Exception e) {
                    player.sendMessage(Component.text("§cFailed to convert world: " + e.getMessage()));
                    Logger.error("Failed to convert Anvil to Polar", e);
                    return;
                }
            }

            // Now check if polar file exists
            if (!polarFile.exists()) {
                sender.sendMessage(Component.text("§cNo polar file or Anvil world found for map: " + mapId));
                sender.sendMessage(Component.text("§7Place a .polar file or Anvil world folder in: " + CONFIG_PATH.toAbsolutePath()));
                return;
            }

            // Load the polar world
            InstanceContainer mapInstance = MinecraftServer.getInstanceManager().createInstanceContainer();
            try {
                mapInstance.setChunkLoader(new PolarLoader(polarFile.toPath()));
            } catch (IOException e) {
                sender.sendMessage(Component.text("§cFailed to load map: " + mapId));
                Logger.error("Failed to load polar file", e);
                return;
            }

            // Create a new configuration session
            MapConfigurationSession session = new MapConfigurationSession(mapId, mapId);
            TypeSkywarsConfiguratorLoader.setCurrentSession(session);

            player.sendMessage(Component.text("§aLoaded map: §f" + mapId));
            player.sendMessage(Component.text("§7Use §b/swconfig §7to configure the map."));

            player.setInstance(mapInstance);
        }, mapArg);
    }

    private void convertAnvilToPolar(Player player, String mapId, File anvilFolder, File polarFile) throws Exception {
        // Find all populated chunks from region files
        File regionDir = new File(anvilFolder, "region");
        File[] regionFiles = regionDir.listFiles((dir, name) -> name.endsWith(".mca"));

        if (regionFiles == null || regionFiles.length == 0) {
            throw new Exception("No region files found in " + regionDir.getAbsolutePath());
        }

        player.sendMessage(Component.text("§7Found " + regionFiles.length + " region file(s)..."));

        // Collect all populated chunk coordinates
        Set<Long> populatedChunks = new HashSet<>();

        for (File regionFile : regionFiles) {
            // Parse region coordinates from filename: r.X.Z.mca
            String name = regionFile.getName();
            String[] parts = name.replace("r.", "").replace(".mca", "").split("\\.");
            if (parts.length != 2) continue;

            int regionX = Integer.parseInt(parts[0]);
            int regionZ = Integer.parseInt(parts[1]);

            // Scan the region file to find populated chunks
            List<int[]> chunks = getPopulatedChunksFromRegion(regionFile, regionX, regionZ);
            for (int[] chunk : chunks) {
                populatedChunks.add(((long) chunk[0] << 32) | (chunk[1] & 0xFFFFFFFFL));
            }
        }

        player.sendMessage(Component.text("§7Converting " + populatedChunks.size() + " chunks to Polar format..."));

        // Convert Anvil to Polar using only populated chunks
        PolarWorld polarWorld = AnvilPolar.anvilToPolar(
                anvilFolder.toPath(),
                (x, z) -> populatedChunks.contains(((long) x << 32) | (z & 0xFFFFFFFFL))
        );

        // Write to file
        byte[] polarBytes = PolarWriter.write(polarWorld);
        Files.write(polarFile.toPath(), polarBytes);

        player.sendMessage(Component.text("§aPolar file created: " + polarFile.getName()));

        // Delete the original Anvil folder
        player.sendMessage(Component.text("§7Deleting original Anvil folder..."));
        deleteDirectory(anvilFolder.toPath());
        player.sendMessage(Component.text("§aOriginal Anvil folder deleted."));
    }

    /**
     * Scans a region file to find which chunks actually contain data
     */
    private List<int[]> getPopulatedChunksFromRegion(File regionFile, int regionX, int regionZ) {
        List<int[]> chunks = new ArrayList<>();

        try (RandomAccessFile raf = new RandomAccessFile(regionFile, "r")) {
            // The first 4096 bytes contain the chunk offset table
            // Each chunk has 4 bytes: 3 bytes offset + 1 byte sector count
            for (int localZ = 0; localZ < 32; localZ++) {
                for (int localX = 0; localX < 32; localX++) {
                    int index = (localX + localZ * 32) * 4;
                    raf.seek(index);

                    int offset = raf.readInt();
                    // If offset is 0, chunk doesn't exist
                    if (offset != 0) {
                        int chunkX = regionX * 32 + localX;
                        int chunkZ = regionZ * 32 + localZ;
                        chunks.add(new int[]{chunkX, chunkZ});
                    }
                }
            }
        } catch (IOException e) {
            Logger.warn("Failed to read region file: " + regionFile.getName(), e);
            // Fallback: load all 32x32 chunks in region
            for (int localZ = 0; localZ < 32; localZ++) {
                for (int localX = 0; localX < 32; localX++) {
                    int chunkX = regionX * 32 + localX;
                    int chunkZ = regionZ * 32 + localZ;
                    chunks.add(new int[]{chunkX, chunkZ});
                }
            }
        }

        return chunks;
    }

    private void deleteDirectory(Path path) throws IOException {
        if (Files.isDirectory(path)) {
            try (var entries = Files.list(path)) {
                for (Path entry : entries.toList()) {
                    deleteDirectory(entry);
                }
            }
        }
        Files.deleteIfExists(path);
    }
}
