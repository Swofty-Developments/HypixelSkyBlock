package net.swofty.user.fairysouls;

import lombok.Getter;
import lombok.Setter;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.swofty.data.DataHandler;
import net.swofty.data.datapoints.DatapointIntegerList;
import net.swofty.data.mongodb.FairySoulDatabase;
import net.swofty.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class FairySoul {
    private static final Map<Integer, FairySoul> SOULS_CACHE = new HashMap<>();

    @Getter
    private int id;

    @Setter
    private Pos location;
    @Setter
    private FairySoulZone zone;

    public FairySoul(int id, Pos location, FairySoulZone zone) {
        this.id = id;
        this.location = location;
        this.zone = zone;
    }

    public void delete() {
        SOULS_CACHE.remove(id);
    }

    public void spawnEntity(Instance instance) {
        new EntityFairySoul(this).spawn(instance);
    }

    public void collect(SkyBlockPlayer player) {
        if (player.getDataHandler().get(DataHandler.Data.FAIRY_SOULS, DatapointIntegerList.class).hasOrAdd(id)) {
            player.sendMessage("§d§lSOUL! §fYou found a §dFairy Soul§f!");
        } else {
            player.sendMessage("§dYou have already found that Fairy Soul!");
        }
    }

    public static List<FairySoul> getFairySouls() {
        return new ArrayList<>(SOULS_CACHE.values());
    }

    public static FairySoul getFairySoul(int id) {
        return SOULS_CACHE.get(id);
    }

    public static void cacheFairySouls() {
        for (FairySoul soul : FairySoulDatabase.getAllSouls()) {
            if (soul.getZone() == null) {
                soul.delete();
            } else {
                SOULS_CACHE.put(soul.getId(), soul);
            }
        }
    }

    public static void spawnEntities(Instance instance) {
        getFairySouls().forEach(soul -> soul.spawnEntity(instance));
    }

    public static void spawnEntities(Instance instance, FairySoulZone zone) {
        getFairySouls().forEach(soul -> {
            if (soul.zone == zone)
                soul.spawnEntity(instance);
        });
    }

}
