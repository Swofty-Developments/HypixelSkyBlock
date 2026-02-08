package net.swofty.type.replayviewer.playback.npc;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.instance.InstanceContainer;
import net.swofty.type.replayviewer.entity.ReplayEntityManager;
import net.swofty.type.replayviewer.entity.ReplayNpcTextEntity;
import net.swofty.type.replayviewer.playback.ReplaySession;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NpcReplayManager {

    private final ReplaySession session;
    private final InstanceContainer instance;
    private final ReplayEntityManager entityManager;
    private final Map<Integer, NpcReplayData> npcData = new HashMap<>();
    private final Map<Integer, ReplayNpcTextEntity> textEntities = new HashMap<>();
    private int nextTextEntityId = -10000; // Use negative IDs to avoid conflicts

    public NpcReplayManager(ReplaySession session) {
        this.session = session;
        this.instance = session.getInstance();
        this.entityManager = session.getEntityManager();
    }

    /**
     * Registers an entity as an NPC.
     *
     * @param entityId the entity ID
     */
    public void registerNpc(int entityId) {
        if (!npcData.containsKey(entityId)) {
            npcData.put(entityId, new NpcReplayData(entityId));
        }
    }

    /**
     * Gets the NPC data for an entity.
     *
     * @param entityId the entity ID
     * @return the NPC data, or null if not an NPC
     */
    public NpcReplayData getNpcData(int entityId) {
        return npcData.get(entityId);
    }

    /**
     * Updates the display name for an NPC.
     *
     * @param entityId    the entity ID
     * @param displayName the display name
     * @param prefix      the prefix
     * @param suffix      the suffix
     * @param nameColor   the name color
     * @param visible     whether the name is visible
     */
    public void updateNpcDisplayName(int entityId, String displayName,
                                      String prefix, String suffix,
                                      int nameColor, boolean visible) {
        NpcReplayData data = npcData.computeIfAbsent(entityId, NpcReplayData::new);
        data.updateDisplayName(displayName, prefix, suffix, nameColor, visible);

        // Apply to entity
        Entity entity = entityManager.getEntity(entityId);
        if (entity != null) {
            if (visible) {
                String fullName = data.getFullDisplayName();
                entity.setCustomName(Component.text(fullName).color(TextColor.color(nameColor)));
                entity.setCustomNameVisible(true);
            } else {
                entity.setCustomNameVisible(false);
            }
        }
    }

    /**
     * Updates text lines for an NPC.
     *
     * @param entityId     the entity ID
     * @param textLines    the text lines
     * @param yOffset      the vertical offset
     * @param durationTicks the display duration (0 = indefinite)
     */
    public void updateNpcTextLines(int entityId, List<String> textLines,
                                    double yOffset, int durationTicks) {
        NpcReplayData data = npcData.computeIfAbsent(entityId, NpcReplayData::new);
        data.updateTextLines(textLines, yOffset, durationTicks);

        // Update or create text entity
        if (textLines == null || textLines.isEmpty()) {
            removeTextEntity(entityId);
        } else {
            updateOrCreateTextEntity(entityId, data);
        }
    }

    private void updateOrCreateTextEntity(int entityId, NpcReplayData data) {
        ReplayNpcTextEntity textEntity = textEntities.get(entityId);

        if (textEntity != null) {
            // Update existing
            textEntity.updateTextLines(data.getTextLines());
        } else {
            // Create new
            Entity npcEntity = entityManager.getEntity(entityId);
            if (npcEntity != null) {
                int textId = nextTextEntityId--;
                textEntity = new ReplayNpcTextEntity(
                    entityId,
                    data.getTextLines(),
                    data.getTextYOffset(),
                    data.getTextDisplayDurationTicks()
                );

                Pos textPos = npcEntity.getPosition().add(0, data.getTextYOffset(), 0);
                textEntity.setInstance(instance, textPos);
                textEntities.put(entityId, textEntity);
                data.setTextEntityId(textId);
            }
        }
    }

    private void removeTextEntity(int entityId) {
        ReplayNpcTextEntity textEntity = textEntities.remove(entityId);
        if (textEntity != null) {
            textEntity.remove();
        }
        NpcReplayData data = npcData.get(entityId);
        if (data != null) {
            data.setTextEntityId(-1);
        }
    }

    /**
     * Updates text entity positions based on NPC movement.
     * Call this when NPCs move.
     *
     * @param entityId the entity ID
     * @param newPos   the new NPC position
     */
    public void updateNpcPosition(int entityId, Pos newPos) {
        ReplayNpcTextEntity textEntity = textEntities.get(entityId);
        NpcReplayData data = npcData.get(entityId);

        if (textEntity != null && data != null) {
            textEntity.updatePositionFromParent(newPos);
        }
    }

    /**
     * Ticks all NPC text entities.
     * Removes expired text entities.
     */
    public void tick() {
        Set<Integer> toRemove = new HashSet<>();

        for (Map.Entry<Integer, ReplayNpcTextEntity> entry : textEntities.entrySet()) {
            if (entry.getValue().tickDuration()) {
                toRemove.add(entry.getKey());
            }
        }

        for (Integer entityId : toRemove) {
            removeTextEntity(entityId);
        }
    }

    /**
     * Removes an NPC and its associated text.
     *
     * @param entityId the entity ID
     */
    public void removeNpc(int entityId) {
        removeTextEntity(entityId);
        npcData.remove(entityId);
    }

    /**
     * Gets all registered NPC entity IDs.
     *
     * @return the set of NPC entity IDs
     */
    public Set<Integer> getNpcEntityIds() {
        return new HashSet<>(npcData.keySet());
    }

    /**
     * Cleans up all NPC data and text entities.
     */
    public void cleanup() {
        for (ReplayNpcTextEntity entity : textEntities.values()) {
            entity.remove();
        }
        textEntities.clear();
        npcData.clear();
    }
}
