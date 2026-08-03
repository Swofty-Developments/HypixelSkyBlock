package net.swofty.type.ravengarddungeon.game;

import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.LightingChunk;
import net.swofty.type.ravengarddungeon.generator.RavengardDungeonGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public final class DungeonInstanceRegistry {
    public static final int MAX_INSTANCES = 4;
    private static final int STANDARD_ROOM_COUNT = 24;
    private static final int STANDARD_MAX_PLAYERS = 5;

    private static final Map<UUID, DungeonInstance> INSTANCES = new ConcurrentHashMap<>();

    private DungeonInstanceRegistry() {
    }

    public static final long EMPTY_LIFETIME_MILLIS = 30_000;

    public static final class DungeonInstance {
        private final UUID gameId = UUID.randomUUID();
        private final String mode;
        private final long seed;
        private final InstanceContainer instance;
        private final CompletableFuture<Void> ready;
        private final RavengardDungeonGenerator.GeneratedDungeon generated;
        private final Set<UUID> players = ConcurrentHashMap.newKeySet();
        private volatile long emptySince = System.currentTimeMillis();

        public void markPlayerJoined(UUID player) {
            players.add(player);
            emptySince = 0;
        }

        public void markPlayerLeft(UUID player) {
            if (players.remove(player) && players.isEmpty()) {
                emptySince = System.currentTimeMillis();
            }
        }

        public long getRemainingLifeSeconds() {
            if (!players.isEmpty() || emptySince == 0) return -1;
            return Math.max(0, (EMPTY_LIFETIME_MILLIS - (System.currentTimeMillis() - emptySince)) / 1000);
        }

        public boolean isExpired() {
            return players.isEmpty() && emptySince > 0
                    && System.currentTimeMillis() - emptySince > EMPTY_LIFETIME_MILLIS;
        }

        private DungeonInstance(String mode, long seed, InstanceContainer instance,
                                RavengardDungeonGenerator.GeneratedDungeon generated,
                                CompletableFuture<Void> ready) {
            this.mode = mode;
            this.seed = seed;
            this.instance = instance;
            this.generated = generated;
            this.ready = ready;
        }

        public UUID getGameId() {
            return gameId;
        }

        public String getMode() {
            return mode;
        }

        public long getSeed() {
            return seed;
        }

        public InstanceContainer getInstance() {
            return instance;
        }

        public CompletableFuture<Void> whenReady() {
            return ready;
        }

        public RavengardDungeonGenerator.GeneratedDungeon getGenerated() {
            return generated;
        }

        public Set<UUID> getPlayers() {
            return players;
        }

        public boolean isAcceptingJoins() {
            return mode.equals("STANDARD") && players.size() < STANDARD_MAX_PLAYERS;
        }
    }

    public static DungeonInstance create(String mode) {
        long seed;
        int roomCount = STANDARD_ROOM_COUNT;
        if (mode.startsWith("ADMIN")) {
            String[] parts = mode.split(":");
            seed = parts.length > 1 ? Long.parseLong(parts[1]) : ThreadLocalRandom.current().nextLong();
            if (parts.length > 2) roomCount = Integer.parseInt(parts[2]);
        } else {
            seed = ThreadLocalRandom.current().nextLong();
        }

        RavengardDungeonGenerator.GeneratedDungeon generated =
                RavengardDungeonGenerator.generate(seed, roomCount);
        InstanceContainer instance = MinecraftServer.getInstanceManager().createInstanceContainer();
        instance.setChunkSupplier(LightingChunk::new);
        CompletableFuture<Void> ready = RavengardDungeonGenerator.stamp(generated, instance);

        DungeonInstance dungeonInstance = new DungeonInstance(
                mode.startsWith("ADMIN") ? "ADMIN" : "STANDARD", seed, instance, generated, ready);
        INSTANCES.put(dungeonInstance.getGameId(), dungeonInstance);
        return dungeonInstance;
    }

    public static DungeonInstance get(UUID gameId) {
        return INSTANCES.get(gameId);
    }

    public static List<DungeonInstance> all() {
        return new ArrayList<>(INSTANCES.values());
    }

    public static int remainingSlots() {
        return Math.max(0, MAX_INSTANCES - INSTANCES.size());
    }

    public static DungeonInstance findByIdPrefix(String prefix) {
        for (DungeonInstance instance : INSTANCES.values()) {
            if (instance.getGameId().toString().equals(prefix)
                    || instance.getGameId().toString().startsWith(prefix)) {
                return instance;
            }
        }
        return null;
    }

    public static void sendPlayerIn(net.minestom.server.entity.Player player, DungeonInstance instance) {
        instance.markPlayerJoined(player.getUuid());
        player.sendMessage("§7Preparing your dungeon (seed §f" + instance.getSeed() + "§7)...");
        boolean aerial = instance.getMode().equals("ADMIN");
        instance.whenReady().thenRun(() -> player.scheduler().scheduleNextTick(() -> {
            net.minestom.server.coordinate.Pos spawn;
            if (aerial) {
                double[] center = boundsCenter(instance);
                spawn = new net.minestom.server.coordinate.Pos(center[0], 140, center[1], 0, 90);
                player.setGameMode(net.minestom.server.entity.GameMode.CREATIVE);
            } else {
                spawn = instance.getGenerated().spawn().withY(67);
            }
            player.setInstance(instance.getInstance(), spawn);
            player.sendMessage("§aEntered dungeon §f" + instance.getGameId().toString().substring(0, 8)
                    + "§a (" + instance.getGenerated().dungeon().getRoomCount() + " rooms, mode "
                    + instance.getMode() + ").");
        }));
    }

    public static double[] boundsCenter(DungeonInstance instance) {
        int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE;
        int minZ = Integer.MAX_VALUE, maxZ = Integer.MIN_VALUE;
        for (var placement : instance.getGenerated().dungeon().getPlacements()) {
            minX = Math.min(minX, placement.originX());
            maxX = Math.max(maxX, placement.originX() + placement.getFootprintWidth());
            minZ = Math.min(minZ, placement.originZ());
            maxZ = Math.max(maxZ, placement.originZ() + placement.getFootprintDepth());
        }
        return new double[]{(minX + maxX) / 2.0, (minZ + maxZ) / 2.0};
    }

    public static void startExpiryTask() {
        MinecraftServer.getSchedulerManager().buildTask(() -> {
            for (DungeonInstance instance : INSTANCES.values()) {
                if (instance.isExpired()) {
                    INSTANCES.remove(instance.getGameId());
                    MinecraftServer.getInstanceManager().unregisterInstance(instance.getInstance());
                }
            }
        }).repeat(net.minestom.server.timer.TaskSchedule.seconds(1)).schedule();
    }
}
