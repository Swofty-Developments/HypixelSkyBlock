package net.swofty.type.skyblockgeneric.entity.mob.mobs.seacreature;

import java.util.LinkedHashMap;
import java.util.Map;
import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

public final class SeaCreatureSpawner {

    private static final Map<String, SeaCreatureProfile> PROFILES = new LinkedHashMap<>();

    static {
        SeaCreatureProfiles.CANONICAL.forEach(SeaCreatureSpawner::register);
    }

    private SeaCreatureSpawner() {
    }

    public static void register(SeaCreatureProfile profile) {
        PROFILES.put(profile.id(), profile);
    }

    public static boolean canSpawn(String seaCreatureId) {
        return PROFILES.containsKey(seaCreatureId);
    }

    public static @Nullable SeaCreatureProfile getProfile(String seaCreatureId) {
        return PROFILES.get(seaCreatureId);
    }

    public static @Nullable SeaCreatureMob spawn(SkyBlockPlayer angler, String seaCreatureId, Pos position) {
        SeaCreatureProfile profile = PROFILES.get(seaCreatureId);
        if (profile == null) {
            Logger.warn("No sea creature profile registered for id={}, no spawn will occur", seaCreatureId);
            return null;
        }

        Instance instance = angler.getInstance();
        if (instance == null) {
            return null;
        }

        SeaCreatureMob mob = SeaCreatureMob.create(profile);
        mob.setInstance(instance, position);

        broadcastCatch(angler, profile, instance, position);
        return mob;
    }

    private static void broadcastCatch(SkyBlockPlayer angler, SeaCreatureProfile profile, Instance instance, Pos position) {
        Component selfMessage = Component.text("§3§lSEA CREATURE! §bA " + profile.displayName().toUpperCase() + " has spawned!");
        angler.sendMessage(selfMessage);

        Component nearbyMessage = Component.text("§3§lSEA CREATURE! §b" + angler.getUsername()
                + " caught §a" + profile.displayName() + "§b!");
        instance.getPlayers().stream()
                .filter(player -> player.getPosition().distance(position) <= 32)
                .filter(player -> !player.getUuid().equals(angler.getUuid()))
                .forEach(player -> player.sendMessage(nearbyMessage));
    }
}
