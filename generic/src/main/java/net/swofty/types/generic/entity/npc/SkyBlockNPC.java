package net.swofty.types.generic.entity.npc;

import lombok.Getter;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.network.packet.server.play.EntityHeadLookPacket;
import net.minestom.server.network.packet.server.play.EntityRotationPacket;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.entity.hologram.PlayerHolograms;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.MathUtility;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class SkyBlockNPC {
    private static final int SPAWN_DISTANCE = 48;
    private static final int LOOK_DISTANCE = 16;

    @Getter
    private static List<SkyBlockNPC> npcs = new ArrayList<>();
    @Getter
    private static Map<UUID, PlayerNPCCache> perPlayerNPCs = new HashMap<>();

    @Getter
    private final NPCParameters parameters;
    @Getter
    private final String name;

    public abstract void onClick(PlayerClickNPCEvent e);

    public SkyBlockNPC(NPCParameters defaultParams) {
        this.parameters = defaultParams;
        String className = getClass().getSimpleName().replace("NPC", "");
        this.name = className.replaceAll("(?<=.)(?=\\p{Lu})", " ");
    }

    public void register() {
        npcs.add(this);
    }

    public void sendNPCMessage(SkyBlockPlayer player, String message) {
        player.sendMessage("§e[NPC] " + getName() + "§f: " + message);
    }

    public static SkyBlockNPC getFromImpl(SkyBlockPlayer player, NPCEntityImpl impl) {
        Map<SkyBlockNPC, NPCEntityImpl> npcs = perPlayerNPCs.get(player.getUuid()).getEntityImpls();
        if (npcs == null) return null;

        for (Map.Entry<SkyBlockNPC, NPCEntityImpl> entry : npcs.entrySet()) {
            if (entry.getValue().equals(impl)) {
                return entry.getKey();
            }
        }

        return null;
    }

    public static void updateForPlayer(SkyBlockPlayer player) {
        perPlayerNPCs.putIfAbsent(player.getUuid(), new PlayerNPCCache());

        Thread.startVirtualThread(() -> {
            SkyBlockNPC.getNpcs().forEach((npc) -> {
                boolean playerHasNPC = perPlayerNPCs.containsKey(player.getUuid()) && perPlayerNPCs.get(player.getUuid()).getEntityImpls().containsKey(npc);

                if (!playerHasNPC) {
                    NPCEntityImpl entity = new NPCEntityImpl(
                            npc.getParameters().holograms(player)[npc.getParameters().holograms(player).length - 1],
                            npc.getParameters().texture(player),
                            npc.getParameters().signature(player),
                            npc.getParameters().holograms(player));
                    Pos position = npc.getParameters().position(player);

                    PlayerHolograms.ExternalPlayerHologram holo = PlayerHolograms.ExternalPlayerHologram.builder()
                            .pos(position.add(0, 1.1, 0))
                            .text(Arrays.copyOfRange(npc.getParameters().holograms(player), 0, npc.getParameters().holograms(player).length - 1))
                            .player(player)
                            .build();

                    entity.setAutoViewable(false);

                    PlayerHolograms.addExternalPlayerHologram(holo);
                    entity.setInstance(SkyBlockConst.getInstanceContainer(), position);
                    entity.addViewer(player);

                    PlayerNPCCache cache = perPlayerNPCs.get(player.getUuid());
                    cache.add(npc, holo, entity);
                    perPlayerNPCs.put(player.getUuid(), cache);
                    return;
                }

                PlayerNPCCache cache = perPlayerNPCs.get(player.getUuid());
                NPCEntityImpl entity = cache.get(npc).getValue();
                PlayerHolograms.ExternalPlayerHologram holo = cache.get(npc).getKey();

                Pos npcPosition = npc.getParameters().position(player);
                String npcTexture = npc.getParameters().texture(player);
                String npcSignature = npc.getParameters().signature(player);
                String[] npcHolograms = npc.getParameters().holograms(player);

                // If any of the parameters have changed, update the NPC
                if (!Arrays.equals(entity.getHolograms(), npcHolograms) ||
                        !entity.getSkinTexture().equals(npcTexture) ||
                        !entity.getSkinSignature().equals(npcSignature) ||
                        !entity.getPosition().equals(npcPosition)) {
                    entity.remove();
                    PlayerHolograms.removeExternalPlayerHologram(holo);
                    cache.remove(npc);
                    return;
                }

                Pos playerPosition = player.getPosition();
                List<SkyBlockPlayer> inRange = entity.getInRangeOf();
                double entityDistance = playerPosition.distance(npcPosition);
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
        });
    }

    public record PlayerClickNPCEvent(SkyBlockPlayer player, int entityId, SkyBlockNPC npc) {
    }

    public static class PlayerNPCCache {
        private final Map<SkyBlockNPC, Map.Entry<PlayerHolograms.ExternalPlayerHologram, NPCEntityImpl>> npcs = new ConcurrentHashMap<>();

        public void add(SkyBlockNPC npc, PlayerHolograms.ExternalPlayerHologram hologram, NPCEntityImpl entity) {
            npcs.put(npc, Map.entry(hologram, entity));
        }

        public void remove(SkyBlockNPC npc) {
            npcs.remove(npc);
        }

        public Map<SkyBlockNPC, NPCEntityImpl> getEntityImpls() {
            Map<SkyBlockNPC, NPCEntityImpl> entityImpls = new HashMap<>();
            npcs.forEach((npc, entry) -> entityImpls.put(npc, entry.getValue()));
            return entityImpls;
        }

        public Map.Entry<PlayerHolograms.ExternalPlayerHologram, NPCEntityImpl> get(SkyBlockNPC npc) {
            return npcs.get(npc);
        }
    }
}
