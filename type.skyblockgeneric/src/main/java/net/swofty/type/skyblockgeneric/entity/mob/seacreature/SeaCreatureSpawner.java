package net.swofty.type.skyblockgeneric.entity.mob.seacreature;

import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class SeaCreatureSpawner {

    private static final Map<String, Supplier<SeaCreatureMob>> FACTORIES = new LinkedHashMap<>();

    static {
        register(MobSquid::new);
        register(MobSeaWalker::new);
        register(MobNightSquid::new);
        register(MobSeaGuardian::new);
        register(MobLavaBlaze::new);
        register(MobMagmaSlug::new);
    }

    private SeaCreatureSpawner() {
    }

    public static void register(Supplier<SeaCreatureMob> factory) {
        FACTORIES.put(factory.get().getSeaCreatureId(), factory);
    }

    public static boolean canSpawn(String seaCreatureId) {
        return FACTORIES.containsKey(seaCreatureId);
    }

    public static @Nullable SeaCreatureMob spawn(SkyBlockPlayer angler, String seaCreatureId, Pos position) {
        Supplier<SeaCreatureMob> factory = FACTORIES.get(seaCreatureId);
        if (factory == null) {
            Logger.warn("No sea creature factory registered for id={}, falling back to nothing", seaCreatureId);
            return null;
        }

        Instance instance = angler.getInstance();
        if (instance == null) {
            return null;
        }

        SeaCreatureMob mob = factory.get();
        mob.setInstance(instance, position);

        Component selfMessage = Component.text("§3§lSEA CREATURE! §bA " + mob.getDisplayName().toUpperCase() + " has spawned!");
        angler.sendMessage(selfMessage);

        Component nearbyMessage = Component.text("§3§lSEA CREATURE! §b" + angler.getUsername()
                + " caught §a" + mob.getDisplayName() + "§b!");
        instance.getPlayers().stream()
                .filter(player -> player.getPosition().distance(position) <= 32)
                .filter(player -> !player.getUuid().equals(angler.getUuid()))
                .forEach(player -> player.sendMessage(nearbyMessage));

        return mob;
    }
}
