package net.swofty.type.generic.entity.animalnpc;

import lombok.Getter;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.network.packet.server.play.EntityHeadLookPacket;
import net.minestom.server.network.packet.server.play.EntityRotationPacket;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.entity.hologram.ServerHolograms;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.utility.MathUtility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public abstract class HypixelAnimalNPC {
    private static final int SPAWN_DISTANCE = 48;
    private static final int LOOK_DISTANCE = 16;

    @Getter
    private static Map<HypixelAnimalNPC, NPCAnimalEntityImpl> animalNPCs = new HashMap();

    @Getter
    private final NPCAnimalParameters parameters;
    @Getter
    private final String name;

    public HypixelAnimalNPC(NPCAnimalParameters defaultParams) {
        this.parameters = defaultParams;
        this.name = getClass().getSimpleName().replace("NPC", "");
    }

    public void register() {
        String[] holograms = parameters.holograms();

        ServerHolograms.addExternalHologram(ServerHolograms.ExternalHologram.builder()
                .pos(new Pos(parameters.position().x(), parameters.hologramYOffset() + parameters.position().y() + 1.1, parameters.position().z()))
                .text(Arrays.copyOfRange(holograms, 0, holograms.length - 1))
                .instance(HypixelConst.getInstanceContainer())
                .build());

        NPCAnimalEntityImpl entity = new NPCAnimalEntityImpl(
                holograms[holograms.length - 1],
                getParameters().entityType());
        entity.setInstance(HypixelConst.getInstanceContainer(), getParameters().position());

        animalNPCs.put(this, entity);
    }

    public static HypixelAnimalNPC getFromImpl(NPCAnimalEntityImpl impl) {
        for (HypixelAnimalNPC npc : animalNPCs.keySet()) {
            if (animalNPCs.get(npc) == impl) return npc;
        }
        throw new RuntimeException("NPC not found");
    }

    public static void updateForPlayer(HypixelPlayer player) {
        HypixelAnimalNPC.getAnimalNPCs().forEach((npc, entity) -> {
            if (entity.getInstance() != HypixelConst.getInstanceContainer()) return;

            Pos npcPosition = entity.getPosition();
            Pos playerPosition = player.getPosition();
            ArrayList<HypixelPlayer> inRange = entity.getInRangeOf();
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

    public abstract void onClick(PlayerClickAnimalNPCEvent e);

    public record PlayerClickAnimalNPCEvent(HypixelPlayer player, HypixelAnimalNPC npc) {
    }
}
