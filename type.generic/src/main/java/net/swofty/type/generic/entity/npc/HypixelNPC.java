package net.swofty.type.generic.entity.npc;

import lombok.Builder;
import lombok.Getter;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.network.packet.server.play.EntityHeadLookPacket;
import net.minestom.server.network.packet.server.play.EntityRotationPacket;
import net.swofty.type.generic.entity.hologram.PlayerHolograms;
import net.swofty.type.generic.entity.npc.configuration.AnimalConfiguration;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.generic.entity.npc.configuration.NPCConfiguration;
import net.swofty.type.generic.entity.npc.configuration.VillagerConfiguration;
import net.swofty.type.generic.entity.npc.impl.NPCAnimalEntityImpl;
import net.swofty.type.generic.entity.npc.impl.NPCEntityImpl;
import net.swofty.type.generic.entity.npc.impl.NPCVillagerEntityImpl;
import net.swofty.type.generic.event.custom.NPCInteractEvent;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.utility.MathUtility;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public abstract class HypixelNPC {
    private static final int SPAWN_DISTANCE = 48;
    private static final int LOOK_DISTANCE = 5;

    @Getter
    private static final List<HypixelNPC> registeredNPCs = new ArrayList<>();
    @Getter
    private static final Map<UUID, HypixelNPC.PlayerNPCCache> perPlayerNPCs = new HashMap<>();

    @Getter
    private final NPCConfiguration parameters;
    private final DialogueController dialogueController;
    @Getter
    private final String name;

    public HypixelNPC(NPCConfiguration configuration) {
        this.parameters = configuration;
        String className = getClass().getSimpleName().replace("NPC", "").replace("Villager", "");
        this.name = parameters.chatName() != null ? parameters.chatName() : className.replaceAll("(?<=.)(?=\\p{Lu})", " ");
        this.dialogueController = new DialogueController(this);
    }

    public static HypixelNPC getFromImpl(HypixelPlayer player, Entity impl) {
        Map<HypixelNPC, Entity> npcs = perPlayerNPCs.get(player.getUuid()).getEntityImpls();
        if (npcs == null) return null;

        for (Map.Entry<HypixelNPC, Entity> entry : npcs.entrySet()) {
            if (entry.getValue().equals(impl)) {
                return entry.getKey();
            }
        }

        return null;
    }

    /**
     * Removes all dialogue states and NPC cache for a player.
     * Call this when the player disconnects.
     *
     * @param player The player to clean up.
     */
    public static void removeDialogueCache(HypixelPlayer player) {
        DialogueController.removeAllDialogues(player);
        PlayerNPCCache cache = perPlayerNPCs.remove(player.getUuid());
        if (cache != null) {
            cache.getEntityImpls().forEach((npc, entity) -> {
                entity.remove();
            });
        }
    }

    public static void updateForPlayer(HypixelPlayer player) {
        PlayerNPCCache cache = perPlayerNPCs.computeIfAbsent(
                player.getUuid(),
                k -> new PlayerNPCCache()
        );

        synchronized (cache) {
			HypixelNPC.getRegisteredNPCs().forEach((npc) -> {
				// If the main username can't be used (over 16 chars), use a blank space instead and use holograms for all lines
				boolean playerHasNPC = cache.getEntityImpls().containsKey(npc);

				if (!playerHasNPC) {
					NPCConfiguration config = npc.getParameters();
					String[] holograms = config.holograms(player);
					Pos position = config.position(player);

					String username = holograms[holograms.length - 1];
					boolean overflowing = username.length() > 16;
					if (overflowing) {
						username = " ";
					}

					Entity entity;
					// if overflowing, adjust yOffset downwards to replace username
					float yOffset = overflowing ? -0.2f : 0.0f;
					switch (config) {
						case HumanConfiguration humanConfig -> entity = new NPCEntityImpl(
								username,
								humanConfig.texture(player),
								humanConfig.signature(player),
								holograms);
						case VillagerConfiguration villagerConfig -> {
                                    entity = new NPCVillagerEntityImpl(username, villagerConfig.profession());
                                    yOffset = 0.2f;
						}
						case AnimalConfiguration animalConfig -> {
                                    entity = new NPCAnimalEntityImpl(
                                            username,
                                            animalConfig.entityType());
                                    yOffset = animalConfig.hologramYOffset();
						}
						default ->
                                        throw new IllegalStateException("Unknown NPCConfiguration type: " + config.getClass().getName());
					}

					entity.setAutoViewable(false);

					PlayerHolograms.ExternalPlayerHologram holo = PlayerHolograms.ExternalPlayerHologram.builder()
                                    .pos(position.add(0, 1.1 + yOffset, 0))
                                    .text(Arrays.copyOfRange(holograms, 0, holograms.length - (overflowing ? 0 : 1)))
                                    .player(player)
                                    .instance(config.instance())
                                    .build();

					PlayerHolograms.addExternalPlayerHologram(holo);
					entity.setInstance(config.instance(), position);
					entity.addViewer(player);

					cache.add(npc, holo, entity);
					perPlayerNPCs.put(player.getUuid(), cache);
					return;
				}

				Entity entity = cache.get(npc).getValue();
				PlayerHolograms.ExternalPlayerHologram holo = cache.get(npc).getKey();

				NPCConfiguration config = npc.getParameters();
				Pos npcPosition = config.position(player);
				String[] npcHolograms = config.holograms(player);

				boolean needsUpdate = !entity.getPosition().equals(npcPosition);
				boolean needsFullUpdate = false;
				if (entity instanceof NPCEntityImpl playerEntity && config instanceof HumanConfiguration humanConfig) {
					needsFullUpdate = !Arrays.equals(playerEntity.getHolograms(), npcHolograms) ||
                                    !playerEntity.getSkinTexture().equals(humanConfig.texture(player)) ||
                                    !playerEntity.getSkinSignature().equals(humanConfig.signature(player));
				}

				if (needsUpdate && !needsFullUpdate) {
					boolean onlyViewChanged = entity.getPosition().x() == npcPosition.x() && entity.getPosition().z() == npcPosition.z() && entity.getPosition().y() == npcPosition.y();
					entity.setView(npcPosition.yaw(), npcPosition.pitch());
					entity.setInstance(config.instance(), npcPosition);
					if (!onlyViewChanged) {
						boolean overflowing = npcHolograms[npcHolograms.length - 1].length() > 16;
						float yOffset = overflowing ? -0.2f : 0.0f;
						if (config instanceof VillagerConfiguration) {
							yOffset = 0.2f;
						} else if (config instanceof AnimalConfiguration animalConfig) {
							yOffset = animalConfig.hologramYOffset();
						}
						PlayerHolograms.relocateExternalPlayerHologram(holo, npcPosition.add(0, 1.1f + yOffset, 0));
					}
					return;
				}
				if (needsFullUpdate) {
                            entity.remove();
                            PlayerHolograms.removeExternalPlayerHologram(holo);
                            cache.remove(npc);
                            return;
				}

				Pos playerPosition = player.getPosition();
				double entityDistance = playerPosition.distance(npcPosition);
				boolean isLookingNPC = config.looking(player);

				// Get inRangeOf list based on entity type
				List<HypixelPlayer> inRange = getInRangeList(entity);

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
					} else if (isLookingNPC) {
						player.sendPackets(
                                        new EntityRotationPacket(entity.getEntityId(), npcPosition.yaw(), npcPosition.pitch(), true),
                                        new EntityHeadLookPacket(entity.getEntityId(), npcPosition.yaw())
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
    }

    private static List<HypixelPlayer> getInRangeList(Entity entity) {
        if (entity instanceof NPCEntityImpl playerEntity) {
            return playerEntity.getInRangeOf();
        } else if (entity instanceof NPCAnimalEntityImpl animalEntity) {
            return animalEntity.getInRangeOf();
        } else if (entity instanceof NPCVillagerEntityImpl villagerEntity) {
            return villagerEntity.getInRangeOf();
        }
        // Fallback for unknown entity types
        return new ArrayList<>();
    }

    public abstract void onClick(NPCInteractEvent event);

    public void register() {
        registeredNPCs.add(this);
    }

    public void unregister() {
        registeredNPCs.remove(this);
    }

    public void sendNPCMessage(HypixelPlayer player, String message) {
        sendNPCMessage(player, message, Sound.sound().type(Key.key("entity.villager.celebrate")).volume(1.0f).pitch(0.8f + new Random().nextFloat() * 0.4f).build());
    }

	public void sendNPCMessage(HypixelPlayer player, String message, Sound sound) {
		player.sendMessage("§e[NPC] " + getName() + "§f: " + message);
		player.playSound(sound);
	}

    protected DialogueController dialogue() {
        return dialogueController;
    }

    /**
     * Checks if the player is currently in dialogue with this NPC.
     *
     * @param player The player to check.
     * @return True if in dialogue, false otherwise.
     */
    public boolean isInDialogue(HypixelPlayer player) {
        return dialogueController.isInDialogue(player);
    }

    /**
     * Starts a dialogue with the player using the specified dialogue key.
     * Override {@link #dialogues(HypixelPlayer)} to define dialogue sets.
     *
     * @param player The player to start dialogue with.
     * @param key    The dialogue set key to play.
     * @return CompletableFuture that completes when dialogue finishes.
     */
    public CompletableFuture<String> setDialogue(HypixelPlayer player, String key) {
        return dialogueController.setDialogue(player, key);
    }

    /**
     * Override this method to define dialogue sets for this NPC.
     * Return an array of DialogueSet objects keyed by unique identifiers.
     *
     * @param player The player to get dialogues for (they can be different per-player).
     * @return Array of DialogueSet definitions.
     */
    protected DialogueSet[] dialogues(HypixelPlayer player) {
        return DialogueSet.EMPTY;
    }

    protected boolean hasDialogue() {
        return dialogues(null).length > 0;
    }

    public static class PlayerNPCCache {
        private final Map<HypixelNPC, Map.Entry<PlayerHolograms.ExternalPlayerHologram, Entity>> npcs = new ConcurrentHashMap<>();

        public void add(HypixelNPC npc, PlayerHolograms.ExternalPlayerHologram hologram, Entity entity) {
            npcs.put(npc, Map.entry(hologram, entity));
        }

        public void remove(HypixelNPC npc) {
            npcs.remove(npc);
        }

        public Map<HypixelNPC, Entity> getEntityImpls() {
            Map<HypixelNPC, Entity> entityImpls = new HashMap<>();
            npcs.forEach((npc, entry) -> entityImpls.put(npc, entry.getValue()));
            return entityImpls;
        }

        public Map.Entry<PlayerHolograms.ExternalPlayerHologram, Entity> get(HypixelNPC npc) {
            return npcs.get(npc);
        }
    }

    @Builder
    public record DialogueSet(String key, String[] lines, Sound sound) {
        public static final DialogueSet[] EMPTY = new DialogueSet[0];
    }
}
