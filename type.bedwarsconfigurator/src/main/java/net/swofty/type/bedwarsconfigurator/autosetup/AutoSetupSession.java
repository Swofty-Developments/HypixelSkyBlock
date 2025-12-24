package net.swofty.type.bedwarsconfigurator.autosetup;

import lombok.Getter;
import lombok.Setter;
import net.minestom.server.instance.Instance;
import net.swofty.commons.bedwars.BedwarsGameType;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig.*;

import java.util.*;

@Getter
@Setter
public class AutoSetupSession {
    private static final Map<UUID, AutoSetupSession> sessions = new HashMap<>();

    private final UUID playerUuid;
    private Instance instance;

    private String mapId;
    private String mapName;
    private final List<BedwarsGameType> gameTypes = new ArrayList<>();
    private Double minX, maxX, minY, maxY, minZ, maxZ;
    private final Map<TeamKey, TeamConfig> teams = new EnumMap<>(TeamKey.class);
    private final List<Position> diamondGenerators = new ArrayList<>();
    private final List<Position> emeraldGenerators = new ArrayList<>();
    private GeneratorSpeed generatorSpeed = GeneratorSpeed.SLOW;
    private int diamondAmount = 1;
    private int diamondMax = 4;
    private int emeraldAmount = 1;
    private int emeraldMax = 2;

    private PitchYawPosition waitingLocation;
    private PitchYawPosition spectatorLocation;

    public AutoSetupSession(UUID playerUuid, Instance instance) {
        this.playerUuid = playerUuid;
        this.instance = instance;
    }

    public static AutoSetupSession getOrCreate(UUID playerUuid, Instance instance) {
        AutoSetupSession existing = sessions.get(playerUuid);
        if (existing != null) {
            // Update instance if player switched maps
            existing.setInstance(instance);
            return existing;
        }
        AutoSetupSession session = new AutoSetupSession(playerUuid, instance);
        sessions.put(playerUuid, session);
        return session;
    }

    public static AutoSetupSession get(UUID playerUuid) {
        return sessions.get(playerUuid);
    }

    public static void remove(UUID playerUuid) {
        sessions.remove(playerUuid);
    }

    public static Collection<AutoSetupSession> getAllSessions() {
        return sessions.values();
    }

    public boolean hasBounds() {
        return minX != null && maxX != null && minY != null && maxY != null && minZ != null && maxZ != null;
    }

    public void setBoundsMin(double x, double y, double z) {
        this.minX = x;
        this.minY = y;
        this.minZ = z;
    }

    public void setBoundsMax(double x, double y, double z) {
        this.maxX = x;
        this.maxY = y;
        this.maxZ = z;
    }

    public TeamConfig getOrCreateTeam(TeamKey key) {
        return teams.computeIfAbsent(key, k -> new TeamConfig());
    }

    public void clear() {
        mapId = null;
        mapName = null;
        gameTypes.clear();
        minX = maxX = minY = maxY = minZ = maxZ = null;
        teams.clear();
        diamondGenerators.clear();
        emeraldGenerators.clear();
        generatorSpeed = GeneratorSpeed.SLOW;
        diamondAmount = 1;
        diamondMax = 4;
        emeraldAmount = 1;
        emeraldMax = 2;
        waitingLocation = null;
        spectatorLocation = null;
    }

    public void loadFromMapEntry(BedWarsMapsConfig.MapEntry entry) {
        clear();

        this.mapId = entry.getId();
        this.mapName = entry.getName();

        MapEntry.MapConfiguration config = entry.getConfiguration();
        if (config == null) return;

        // Load game types
        if (config.getTypes() != null) {
            gameTypes.addAll(config.getTypes());
        }

        // Load bounds
        if (config.getBounds() != null) {
            MapEntry.MapConfiguration.MapBounds bounds = config.getBounds();
            if (bounds.getX() != null) {
                minX = bounds.getX().min();
                maxX = bounds.getX().max();
            }
            if (bounds.getY() != null) {
                minY = bounds.getY().min();
                maxY = bounds.getY().max();
            }
            if (bounds.getZ() != null) {
                minZ = bounds.getZ().min();
                maxZ = bounds.getZ().max();
            }
        }

        // Load generator settings
        if (config.getGeneratorSpeed() != null) {
            generatorSpeed = config.getGeneratorSpeed();
        }

        // Load teams
        if (config.getTeams() != null) {
            for (Map.Entry<TeamKey, MapTeam> teamEntry : config.getTeams().entrySet()) {
                TeamKey teamKey = teamEntry.getKey();
                MapTeam mapTeam = teamEntry.getValue();
                TeamConfig teamConfig = getOrCreateTeam(teamKey);

                if (mapTeam.getSpawn() != null) {
                    teamConfig.setSpawn(mapTeam.getSpawn());
                }
                if (mapTeam.getBed() != null) {
                    teamConfig.setBedFeet(mapTeam.getBed().feet());
                    teamConfig.setBedHead(mapTeam.getBed().head());
                }
                if (mapTeam.getGenerator() != null) {
                    teamConfig.setGenerator(mapTeam.getGenerator());
                }
                if (mapTeam.getShop() != null) {
                    teamConfig.setItemShop(mapTeam.getShop().item());
                    teamConfig.setTeamShop(mapTeam.getShop().team());
                }
            }
        }

        // Load locations
        if (config.getLocations() != null) {
            var locations = config.getLocations();
            if (locations.getWaiting() != null) {
                Position w = locations.getWaiting();
                waitingLocation = new PitchYawPosition(w.x(), w.y(), w.z(), 0, 0);
            }
            if (locations.getSpectator() != null) {
                Position s = locations.getSpectator();
                spectatorLocation = new PitchYawPosition(s.x(), s.y(), s.z(), 0, 0);
            }
        }

        // Load global generators
        if (config.getGlobal_generator() != null) {
            var diamondGen = config.getGlobal_generator().get("diamond");
            if (diamondGen != null) {
                diamondAmount = diamondGen.getAmount();
                diamondMax = diamondGen.getMax();
                if (diamondGen.getLocations() != null) {
                    diamondGenerators.addAll(diamondGen.getLocations());
                }
            }
            var emeraldGen = config.getGlobal_generator().get("emerald");
            if (emeraldGen != null) {
                emeraldAmount = emeraldGen.getAmount();
                emeraldMax = emeraldGen.getMax();
                if (emeraldGen.getLocations() != null) {
                    emeraldGenerators.addAll(emeraldGen.getLocations());
                }
            }
        }
    }

    public MapEntry toMapEntry() {
        MapEntry entry = new MapEntry();
        entry.setId(mapId);
        entry.setName(mapName);

        MapEntry.MapConfiguration config = new MapEntry.MapConfiguration();
        config.setTypes(new ArrayList<>(gameTypes));

        // Generator config
        config.setGeneratorSpeed(generatorSpeed);

        // Bounds
        if (hasBounds()) {
            MapEntry.MapConfiguration.MapBounds bounds = new MapEntry.MapConfiguration.MapBounds();
            bounds.setX(new MinMax(minX, maxX));
            bounds.setY(new MinMax(minY, maxY));
            bounds.setZ(new MinMax(minZ, maxZ));
            config.setBounds(bounds);
        }

        // Teams
        Map<TeamKey, MapTeam> teamsMap = new EnumMap<>(TeamKey.class);
        for (Map.Entry<TeamKey, TeamConfig> teamEntry : teams.entrySet()) {
            TeamConfig tc = teamEntry.getValue();
            MapTeam mapTeam = new MapTeam();

            if (tc.getSpawn() != null) {
                mapTeam.setSpawn(tc.getSpawn());
            }
            if (tc.getBedFeet() != null && tc.getBedHead() != null) {
                mapTeam.setBed(new TwoBlockPosition(tc.getBedFeet(), tc.getBedHead()));
            }
            if (tc.getGenerator() != null) {
                mapTeam.setGenerator(tc.getGenerator());
            }
            if (tc.getItemShop() != null || tc.getTeamShop() != null) {
                mapTeam.setShop(new MapTeam.Shops(tc.getItemShop(), tc.getTeamShop()));
            }

            teamsMap.put(teamEntry.getKey(), mapTeam);
        }
        config.setTeams(teamsMap);

        // Locations
        MapEntry.MapConfiguration.MapLocations locations = new MapEntry.MapConfiguration.MapLocations();
        if (waitingLocation != null) {
            locations.setWaiting(new Position(waitingLocation.x(), waitingLocation.y(), waitingLocation.z()));
        }
        if (spectatorLocation != null) {
            locations.setSpectator(new Position(spectatorLocation.x(), spectatorLocation.y(), spectatorLocation.z()));
        }
        config.setLocations(locations);

        // Global generators
        Map<String, MapEntry.MapConfiguration.GlobalGenerator> globalGenerators = new HashMap<>();

        if (!diamondGenerators.isEmpty()) {
            MapEntry.MapConfiguration.GlobalGenerator diamondGen = new MapEntry.MapConfiguration.GlobalGenerator();
            diamondGen.setAmount(diamondAmount);
            diamondGen.setMax(diamondMax);
            diamondGen.setLocations(new ArrayList<>(diamondGenerators));
            globalGenerators.put("diamond", diamondGen);
        }

        if (!emeraldGenerators.isEmpty()) {
            MapEntry.MapConfiguration.GlobalGenerator emeraldGen = new MapEntry.MapConfiguration.GlobalGenerator();
            emeraldGen.setAmount(emeraldAmount);
            emeraldGen.setMax(emeraldMax);
            emeraldGen.setLocations(new ArrayList<>(emeraldGenerators));
            globalGenerators.put("emerald", emeraldGen);
        }

        config.setGlobal_generator(globalGenerators);
        entry.setConfiguration(config);

        return entry;
    }

    @Getter
    @Setter
    public static class TeamConfig {
        private PitchYawPosition spawn;
        private Position bedFeet;
        private Position bedHead;
        private Position generator;
        private PitchYawPosition itemShop;
        private PitchYawPosition teamShop;
    }
}


