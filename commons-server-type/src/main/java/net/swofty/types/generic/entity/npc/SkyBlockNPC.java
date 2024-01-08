package net.swofty.types.generic.entity.npc;

import lombok.Getter;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.network.packet.server.play.EntityHeadLookPacket;
import net.minestom.server.network.packet.server.play.EntityRotationPacket;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.entity.hologram.ServerHolograms;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.MathUtility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public abstract class SkyBlockNPC {
    private static final int SPAWN_DISTANCE = 48;
    private static final int LOOK_DISTANCE = 16;

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
                .pos(new Pos(parameters.position().x(), parameters.position().y() + 1.1, parameters.position().z()))
                .text(Arrays.copyOfRange(holograms, 0, holograms.length - 1))
                .instance(SkyBlockConst.getInstanceContainer())
                .build());

        NPCEntityImpl entity = new NPCEntityImpl(
                holograms[holograms.length - 1],
                getParameters().texture(),
                getParameters().signature());
        entity.setInstance(SkyBlockConst.getInstanceContainer(), getParameters().position());
        entity.spawn();

        npcs.put(this, entity);
    }

    public void sendMessage(SkyBlockPlayer player, String message) {
        player.sendMessage("§e[NPC] " + getName() + ": §f" + message);
    }

    public static SkyBlockNPC getFromImpl(NPCEntityImpl impl) {
        for (SkyBlockNPC npc : npcs.keySet()) {
            if (npcs.get(npc) == impl) return npc;
        }
        return null;
    }

    public static void updateForPlayer(SkyBlockPlayer player) {
        SkyBlockNPC.getNpcs().forEach((npc, entity) -> {
            if (entity.getInstance() != SkyBlockConst.getInstanceContainer()) return;

            Pos npcPosition = entity.getPosition();
            Pos playerPosition = player.getPosition();
            ArrayList<SkyBlockPlayer> inRange = entity.getInRangeOf();
            double entityDistance = entity.getDistance(playerPosition);
            boolean isLookingNPC = npc.getParameters().looking();

            if (entityDistance <= SPAWN_DISTANCE) {
                if (!inRange.contains(player)) {
                    inRange.add(player);
                    entity.updateNewViewer(player);
                }

                if (isLookingNPC && entityDistance <= LOOK_DISTANCE) {
                    double diffX = playerPosition.x() - npcPosition.x();
                    double diffZ = playerPosition.z() - npcPosition.z();
                    double theta = Math.atan2(diffZ, diffX);
                    double yaw = MathUtility.normalizeAngle(Math.toDegrees(theta) + 90, 360.0);

                    player.sendPackets(
                            new EntityRotationPacket(entity.getEntityId(), (float) yaw, npcPosition.pitch(), true),
                            new EntityHeadLookPacket(entity.getEntityId(), (float) yaw)
                    );
                }
            } else {
                if (inRange.contains(player)) {
                    inRange.remove(player);
                    entity.updateOldViewer(player);

                    player.sendPackets(
                            new EntityRotationPacket(entity.getEntityId(), npcPosition.yaw(), npcPosition.pitch(), true),
                            new EntityHeadLookPacket(entity.getEntityId(), npcPosition.yaw())
                    );
                }
            }
        });
    }

    public record PlayerClickNPCEvent(SkyBlockPlayer player, int entityId, SkyBlockNPC npc) {
    }
}
