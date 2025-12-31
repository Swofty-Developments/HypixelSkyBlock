package net.swofty.type.murdermysteryconfigurator.autosetup;

import lombok.Getter;
import lombok.Setter;
import net.minestom.server.instance.Instance;
import net.swofty.commons.murdermystery.MurderMysteryGameType;
import net.swofty.commons.murdermystery.map.MurderMysteryMapsConfig;
import net.swofty.commons.murdermystery.map.MurderMysteryMapsConfig.*;

import java.util.*;

@Getter
@Setter
public class MurderMysterySetupSession {
    private static final Map<UUID, MurderMysterySetupSession> sessions = new HashMap<>();

    private final UUID playerUuid;
    private Instance instance;

    private String mapId;
    private String mapName;
    private final List<MurderMysteryGameType> gameTypes = new ArrayList<>();

    private PitchYawPosition waitingLocation;
    private final List<Position> goldSpawns = new ArrayList<>();
    private final List<Position> playerSpawns = new ArrayList<>();
    private final Map<String, EditableKillRegion> killRegions = new LinkedHashMap<>();

    @Getter
    @Setter
    public static class EditableKillRegion {
        private String name;
        private Position minPos;
        private Position maxPos;

        public EditableKillRegion(String name) {
            this.name = name;
        }

        public boolean isComplete() {
            return minPos != null && maxPos != null;
        }

        public KillRegion toKillRegion() {
            return new KillRegion(name, minPos, maxPos);
        }

        public static EditableKillRegion fromKillRegion(KillRegion kr) {
            EditableKillRegion e = new EditableKillRegion(kr.name());
            e.setMinPos(kr.min());
            e.setMaxPos(kr.max());
            return e;
        }
    }

    public MurderMysterySetupSession(UUID playerUuid, Instance instance) {
        this.playerUuid = playerUuid;
        this.instance = instance;
    }

    public static MurderMysterySetupSession getOrCreate(UUID playerUuid, Instance instance) {
        MurderMysterySetupSession existing = sessions.get(playerUuid);
        if (existing != null) {
            existing.setInstance(instance);
            return existing;
        }
        MurderMysterySetupSession session = new MurderMysterySetupSession(playerUuid, instance);
        sessions.put(playerUuid, session);
        return session;
    }

    public static MurderMysterySetupSession get(UUID playerUuid) {
        return sessions.get(playerUuid);
    }

    public static void remove(UUID playerUuid) {
        sessions.remove(playerUuid);
    }

    public static Collection<MurderMysterySetupSession> getAllSessions() {
        return sessions.values();
    }

    public void clear() {
        mapId = null;
        mapName = null;
        gameTypes.clear();
        waitingLocation = null;
        goldSpawns.clear();
        playerSpawns.clear();
        killRegions.clear();
    }

    public void loadFromMapEntry(MurderMysteryMapsConfig.MapEntry entry) {
        clear();

        this.mapId = entry.getId();
        this.mapName = entry.getName();

        MapEntry.MapConfiguration config = entry.getConfiguration();
        if (config == null) return;

        // Load game types
        if (config.getTypes() != null) {
            gameTypes.addAll(config.getTypes());
        }

        // Load locations
        if (config.getLocations() != null) {
            var locations = config.getLocations();
            waitingLocation = locations.getWaiting();
        }

        // Load gold spawns
        if (config.getGoldSpawns() != null) {
            goldSpawns.addAll(config.getGoldSpawns());
        }

        // Load player spawns
        if (config.getPlayerSpawns() != null) {
            playerSpawns.addAll(config.getPlayerSpawns());
        }

        // Load kill regions
        if (config.getKillRegions() != null) {
            for (KillRegion kr : config.getKillRegions()) {
                killRegions.put(kr.name(), EditableKillRegion.fromKillRegion(kr));
            }
        }
    }

    public MapEntry toMapEntry() {
        MapEntry entry = new MapEntry();
        entry.setId(mapId);
        entry.setName(mapName);

        MapEntry.MapConfiguration config = new MapEntry.MapConfiguration();
        config.setTypes(new ArrayList<>(gameTypes));

        // Locations
        MapEntry.MapConfiguration.MapLocations locations = new MapEntry.MapConfiguration.MapLocations();
        locations.setWaiting(waitingLocation);
        config.setLocations(locations);

        // Gold spawns
        config.setGoldSpawns(new ArrayList<>(goldSpawns));

        // Player spawns
        config.setPlayerSpawns(new ArrayList<>(playerSpawns));

        // Kill regions (only save complete ones)
        List<KillRegion> completeKillRegions = killRegions.values().stream()
                .filter(EditableKillRegion::isComplete)
                .map(EditableKillRegion::toKillRegion)
                .toList();
        config.setKillRegions(new ArrayList<>(completeKillRegions));

        entry.setConfiguration(config);
        return entry;
    }
}
