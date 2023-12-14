package net.swofty.entity.npc;

import lombok.Getter;
import net.minestom.server.coordinate.Pos;
import net.swofty.SkyBlock;
import net.swofty.entity.hologram.ServerHolograms;
import net.swofty.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public abstract class SkyBlockNPC {
    @Getter
    private static Map<SkyBlockNPC, NPCEntityImpl> npcs = new HashMap();

    @Getter
    private final NPCParameters parameters;
    @Getter
    private final String name;

    public abstract void onClick(PlayerClickNPCEvent e);


    public SkyBlockNPC(NPCParameters defaultParams) {
        this.parameters = defaultParams;
        this.name = getClass().getSimpleName().replace("NPC", "");
    }

    public void register() {
        String[] holograms = parameters.holograms();

        ServerHolograms.addExternalHologram(ServerHolograms.ExternalHologram.builder()
                .pos(new Pos(parameters.position().x(),parameters.position().y() + 1.3,parameters.position().z()))
                .text(Arrays.copyOfRange(holograms, 0, holograms.length - 1))
                .instance(SkyBlock.getInstanceContainer())
                .build());

        NPCEntityImpl entity = new NPCEntityImpl(
                holograms[holograms.length - 1],
                getParameters().texture(),
                getParameters().signature());
        entity.setInstance(SkyBlock.getInstanceContainer(), getParameters().position());
        entity.spawn();

        npcs.put(this, entity);
    }

    public static SkyBlockNPC getFromImpl(NPCEntityImpl impl) {
        for (SkyBlockNPC npc : npcs.keySet()) {
            if (npcs.get(npc) == impl) return npc;
        }
        return null;
    }

    public static void updateForPlayer(SkyBlockPlayer player) {
        SkyBlockNPC.getNpcs().forEach((npc, entity) -> {
            if (entity.getInstance() != SkyBlock.getInstanceContainer()) return;

            Pos npcPosition = entity.getPosition();
            Pos playerPosition = player.getPosition();
            ArrayList<SkyBlockPlayer> inRange = entity.getInRangeOf();

            if (npcPosition.distance(playerPosition) <= 48) {
                if (!inRange.contains(player)) {
                    inRange.add(player);
                    entity.updateNewViewer(player);
                }
            } else {
                if (inRange.contains(player)) {
                    inRange.remove(player);
                    entity.updateOldViewer(player);
                }
            }
        });
    }

    public record PlayerClickNPCEvent(SkyBlockPlayer player, int entityId, SkyBlockNPC npc) {}
}
