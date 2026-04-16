package net.swofty.type.generic.entity.npc;

import lombok.Builder;
import lombok.Getter;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.GameMode;
import net.swofty.type.generic.entity.npc.configuration.AnimalConfiguration;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.generic.entity.npc.configuration.NPCConfiguration;
import net.swofty.type.generic.entity.npc.configuration.VillagerConfiguration;
import net.swofty.type.generic.entity.npc.impl.NPCAnimalEntityImpl;
import net.swofty.type.generic.entity.npc.impl.NPCEntityImpl;
import net.swofty.type.generic.entity.npc.impl.NPCViewable;
import net.swofty.type.generic.entity.npc.impl.NPCVillagerEntityImpl;
import net.swofty.type.generic.event.custom.NPCInteractEvent;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.generic.user.HypixelPlayer;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public abstract class HypixelNPC {
    private static final int SPAWN_DISTANCE = 48;
    private static final int LOOK_DISTANCE = 5;

    @Getter
    private static final List<HypixelNPC> registeredNPCs = new ArrayList<>();
    @Getter
    private static final Map<UUID, HypixelNPC.PlayerNPCCache> perPlayerNPCs = new ConcurrentHashMap<>();

    @Getter
    private final NPCConfiguration parameters;
    private final DialogueController dialogueController;
    public HypixelNPC(NPCConfiguration configuration) {
        this.parameters = configuration;
        this.dialogueController = new DialogueController(this);
    }

    public static HypixelNPC getFromImpl(HypixelPlayer player, Entity impl) {
        PlayerNPCCache cache = perPlayerNPCs.get(player.getUuid());
        if (cache == null) return null;

        Map<HypixelNPC, Entity> npcs = cache.getEntityImpls();
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

                    if (!config.visible(player)) return;

                    String[] holograms = config.holograms(player);
                    Pos position = config.position(player);

                    String username = holograms[holograms.length - 1];
                    boolean overflowing = username.length() > 16;
                    if (overflowing) {
                        username = " ";
                    }

                    Entity entity;
                    switch (config) {
                        case HumanConfiguration humanConfig -> entity = new NPCEntityImpl(
                            player,
                            position,
                            username,
                            humanConfig.texture(player),
                            humanConfig.signature(player),
                            holograms,
                            humanConfig,
                            overflowing);
                        case VillagerConfiguration villagerConfig -> {
                            entity = new NPCVillagerEntityImpl(player,
                                position, username, villagerConfig.profession(), villagerConfig, holograms, overflowing);
                        }
                        case AnimalConfiguration animalConfig -> {
                            entity = new NPCAnimalEntityImpl(player,
                                position,
                                username,
                                animalConfig.entityType(), animalConfig, holograms, overflowing);
                        }
                        default ->
                            throw new IllegalStateException("Unknown NPCConfiguration type: " + config.getClass().getName());
                    }

                    cache.add(npc, entity);
                    perPlayerNPCs.put(player.getUuid(), cache);
                    return;
                }

                Entity entity = cache.get(npc);
                NPCConfiguration config = npc.getParameters();

                if (!config.visible(player)) {
                    entity.remove();
                    cache.remove(npc);
                    return;
                }

                Pos npcPosition = config.position(player);
                if (!(entity instanceof NPCViewable npcViewable)) {
                    Logger.error("Entity for NPC {} does not implement NPCViewable, skipping update", npc.getName());
                    return;
                }
                npcViewable.updateNPC();

                Pos playerPosition = player.getPosition();
                double entityDistance = playerPosition.distance(npcPosition);
                boolean isLookingNPC = config.looking(player) && player.getGameMode() != GameMode.SPECTATOR;

                // Get inRangeOf list based on entity type
                List<HypixelPlayer> inRange = npcViewable.getInRangeOf();
                if (entityDistance <= SPAWN_DISTANCE) {
                    if (!inRange.contains(player)) {
                        inRange.add(player);
                        entity.updateNewViewer(player);
                    }

                    if (isLookingNPC) {
                        if (entityDistance <= LOOK_DISTANCE) {
                            entity.lookAt(player);
                        } else {
                            // over the distance, reset back to default rotation
                            entity.setView(npcPosition.yaw(), npcPosition.pitch());
                        }
                    }
                } else {
                    if (inRange.contains(player)) {
                        inRange.remove(player);
                        entity.updateOldViewer(player);
                        entity.setView(npcPosition.yaw(), npcPosition.pitch());
                    }
                }
            });
        }
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

    public String getName() {
        String className = getClass().getSimpleName().replace("NPC", "").replace("Villager", "");
        return parameters.chatName() != null ? parameters.chatName() : className.replaceAll("(?<=.)(?=\\p{Lu})", " ");
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
     *               May be null when called from {@link #hasDialogue()} — implementations
     *               must handle null gracefully (e.g. return a default set).
     * @return Array of DialogueSet definitions.
     */
    protected DialogueSet[] dialogues(HypixelPlayer player) {
        return DialogueSet.EMPTY;
    }

    protected boolean hasDialogue() {
        return dialogues(null).length > 0;
    }

    public static class PlayerNPCCache {
        private final Map<HypixelNPC, Entity> npcs = new ConcurrentHashMap<>();

        public void add(HypixelNPC npc, Entity entity) {
            npcs.put(npc, entity);
        }

        public void remove(HypixelNPC npc) {
            npcs.remove(npc);
        }

        public Map<HypixelNPC, Entity> getEntityImpls() {
            return npcs;
        }

        public Entity get(HypixelNPC npc) {
            return npcs.get(npc);
        }
    }

    @Builder
    public record DialogueSet(String key, String[] lines, Sound sound) {
        public static final DialogueSet[] EMPTY = new DialogueSet[0];

        public static DialogueSet ofTranslation(String key, String translationKey) {
            return new DialogueSet(key, I18n.dialogueLines(translationKey), null);
        }

        public static DialogueSet ofTranslation(String key, String translationKey, Sound sound) {
            return new DialogueSet(key, I18n.dialogueLines(translationKey), sound);
        }

        public static DialogueSet ofTranslation(String key, String translationKey, Map<String, String> placeholders) {
            return new DialogueSet(key, I18n.dialogueLines(translationKey, placeholders), null);
        }

        public static DialogueSet ofTranslation(String key, String translationKey, Map<String, String> placeholders, Sound sound) {
            return new DialogueSet(key, I18n.dialogueLines(translationKey, placeholders), sound);
        }

        public static DialogueSet ofTranslation(String key, String translationKey, @Nullable HypixelPlayer player) {
            Locale locale = player != null ? player.getLocale() : Locale.US;
            return new DialogueSet(key, I18n.dialogueLines(translationKey, locale), null);
        }

        public static DialogueSet ofTranslation(String key, String translationKey, @Nullable HypixelPlayer player, Sound sound) {
            Locale locale = player != null ? player.getLocale() : Locale.US;
            return new DialogueSet(key, I18n.dialogueLines(translationKey, locale), sound);
        }

        public static DialogueSet ofTranslation(String key, String translationKey, @Nullable HypixelPlayer player, Map<String, String> placeholders) {
            Locale locale = player != null ? player.getLocale() : Locale.US;
            return new DialogueSet(key, I18n.dialogueLines(translationKey, locale, placeholders), null);
        }

        public static DialogueSet ofTranslation(String key, String translationKey, @Nullable HypixelPlayer player, Map<String, String> placeholders, Sound sound) {
            Locale locale = player != null ? player.getLocale() : Locale.US;
            return new DialogueSet(key, I18n.dialogueLines(translationKey, locale, placeholders), sound);
        }
    }
}
