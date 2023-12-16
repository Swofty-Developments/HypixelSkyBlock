package net.swofty.entity.villager;

import lombok.Getter;
import net.minestom.server.coordinate.Pos;
import net.swofty.SkyBlock;
import net.swofty.entity.hologram.ServerHolograms;
import net.swofty.user.SkyBlockPlayer;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class SkyBlockVillagerNPC {
    private static Map<SkyBlockVillagerNPC, VillagerEntityImpl> villagers = new HashMap();

    private final NPCVillagerParameters parameters;

    protected SkyBlockVillagerNPC(NPCVillagerParameters parameters) {
        this.parameters = parameters;
    }

    public abstract void onClick(PlayerClickVillagerNPCEvent e);

    public void register() {
        String[] holograms = parameters.holograms();

        ServerHolograms.addExternalHologram(ServerHolograms.ExternalHologram.builder()
                .pos(new Pos(parameters.position().x(), parameters.position().y() + 0.9, parameters.position().z()))
                .text(holograms)
                .instance(SkyBlock.getInstanceContainer())
                .build());

        VillagerEntityImpl entity = new VillagerEntityImpl(getParameters().profession());
        entity.setInstance(SkyBlock.getInstanceContainer(), getParameters().position());
        entity.spawn();

        villagers.put(this, entity);
    }

    public static SkyBlockVillagerNPC getFromImpl(VillagerEntityImpl impl) {
        for (SkyBlockVillagerNPC npc : villagers.keySet()) {
            if (villagers.get(npc) == impl) return npc;
        }
        return null;
    }

    public record PlayerClickVillagerNPCEvent(SkyBlockPlayer player, SkyBlockVillagerNPC npc) {}
}
