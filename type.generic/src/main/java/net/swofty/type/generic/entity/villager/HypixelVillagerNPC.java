package net.swofty.type.generic.entity.villager;

import lombok.Getter;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.network.packet.server.play.EntityHeadLookPacket;
import net.minestom.server.network.packet.server.play.EntityRotationPacket;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.entity.hologram.ServerHolograms;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.utility.MathUtility;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class HypixelVillagerNPC {
    private static final int LOOK_DISTANCE = 16;
    @Getter
    private static final Map<HypixelVillagerNPC, VillagerEntityImpl> villagers = new HashMap();

    private final NPCVillagerParameters parameters;
    private final String ID;

    protected HypixelVillagerNPC(NPCVillagerParameters parameters) {
        this.parameters = parameters;
        this.ID = this.getClass().getSimpleName();
    }

    public abstract void onClick(PlayerClickVillagerNPCEvent e);

    public void register() {
        String[] holograms = parameters.holograms();

        ServerHolograms.addExternalHologram(ServerHolograms.ExternalHologram.builder()
                .pos(new Pos(parameters.position().x(), parameters.position().y() + 0.9, parameters.position().z()))
                .text(holograms)
                .instance(HypixelConst.getInstanceContainer())
                .build());

        VillagerEntityImpl entity = new VillagerEntityImpl(getParameters().profession());
        entity.setInstance(HypixelConst.getInstanceContainer(), getParameters().position());
        entity.spawn();

        villagers.put(this, entity);
    }

    public static void updateForPlayer(HypixelPlayer player) {
        villagers.forEach((npc, entity) -> {
            if (entity.getInstance() != HypixelConst.getInstanceContainer()) return;

            Pos npcPosition = entity.getPosition();
            Pos playerPosition = player.getPosition();
            double entityDistance = entity.getDistance(playerPosition);
            boolean isLookingNPC = npc.getParameters().looking();

            if (entityDistance <= LOOK_DISTANCE && isLookingNPC) {
                double diffX = playerPosition.x() - npcPosition.x();
                double diffZ = playerPosition.z() - npcPosition.z();
                double theta = Math.atan2(diffZ, diffX);
                double yaw = MathUtility.normalizeAngle(Math.toDegrees(theta) + 90, 360.0);

                player.sendPackets(
                        new EntityRotationPacket(entity.getEntityId(), (float) yaw, npcPosition.pitch(), true),
                        new EntityHeadLookPacket(entity.getEntityId(), (float) yaw)
                );
            } else {
                player.sendPackets(
                        new EntityRotationPacket(entity.getEntityId(), npcPosition.yaw(), npcPosition.pitch(), true),
                        new EntityHeadLookPacket(entity.getEntityId(), npcPosition.yaw())
                );
            }
        });
    }

    public static HypixelVillagerNPC getFromImpl(VillagerEntityImpl impl) {
        for (HypixelVillagerNPC npc : villagers.keySet()) {
            if (villagers.get(npc) == impl) return npc;
        }
        return null;
    }

    public record PlayerClickVillagerNPCEvent(HypixelPlayer player, HypixelVillagerNPC npc) {
    }
}
