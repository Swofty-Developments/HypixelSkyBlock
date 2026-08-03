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

    public static final class DungeonInstance {
        private final UUID gameId = UUID.randomUUID();
        private final String mode;
        private final long seed;
        private final InstanceContainer instance;
        private final CompletableFuture<Void> ready;
        private final RavengardDungeonGenerator.GeneratedDungeon generated;
        private final Set<UUID> players = ConcurrentHashMap.newKeySet();

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

    public static void removeIfEmpty(UUID gameId) {
        DungeonInstance dungeonInstance = INSTANCES.get(gameId);
        if (dungeonInstance != null && dungeonInstance.getPlayers().isEmpty()) {
            INSTANCES.remove(gameId);
            MinecraftServer.getInstanceManager().unregisterInstance(dungeonInstance.getInstance());
        }
    }
}
