package net.swofty.type.bedwarsgame.replay;

import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.entity.ItemEntity;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.ItemStack;
import net.minestom.server.potion.PotionEffect;
import net.minestom.server.potion.TimedPotion;
import net.minestom.server.utils.inventory.PlayerInventoryUtils;
import net.swofty.type.game.replay.ReplayRecorder;
import net.swofty.type.game.replay.dispatcher.ReplayDispatcher;
import net.swofty.type.game.replay.recordable.RecordableEntityAnimation;
import net.swofty.type.game.replay.recordable.RecordableEntityDespawn;
import net.swofty.type.game.replay.recordable.RecordableEntityEffect;
import net.swofty.type.game.replay.recordable.RecordableEntityEquipment;
import net.swofty.type.game.replay.recordable.RecordableEntitySpawn;
import net.swofty.type.game.replay.recordable.RecordablePlayerArmSwing;
import net.swofty.type.game.replay.recordable.RecordablePlayerHandItem;
import net.swofty.type.game.replay.recordable.RecordablePlayerSneak;
import net.swofty.type.game.replay.recordable.RecordablePlayerSprint;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class EntityLifecycleDispatcher implements ReplayDispatcher {
    private ReplayRecorder recorder;
    private final Instance instance;

    // Track which entities we've seen
    private final Set<Integer> trackedEntities = new HashSet<>();

    // Track player states for change detection
    private final Map<Integer, PlayerState> playerStates = new HashMap<>();

    // Track entity equipment for change detection
    private final Map<Integer, Map<EquipmentSlot, ItemStack>> entityEquipment = new HashMap<>();

    // Track entity effects for change detection
    private final Map<Integer, Set<PotionEffect>> entityEffects = new HashMap<>();

    public EntityLifecycleDispatcher(Instance instance) {
        this.instance = instance;
    }

    @Override
    public void initialize(ReplayRecorder recorder) {
        this.recorder = recorder;

        for (Entity entity : instance.getEntities()) {
            // Skip item entities - they are tracked separately via dropped item system
            if (entity instanceof ItemEntity) continue;

            recordEntitySpawn(entity);
            trackedEntities.add(entity.getEntityId());

            if (entity instanceof Player player) {
                playerStates.put(entity.getEntityId(), new PlayerState(
                    player.isSneaking(), player.isSprinting()
                ));
                recordAllEquipment(player);
                recordAllEffects(player);
            } else if (entity instanceof LivingEntity livingEntity) {
                recordAllEquipment(livingEntity);
                recordAllEffects(livingEntity);
            }
        }
    }

    @Override
    public void tick() {
        Set<Integer> currentEntities = new HashSet<>();

        // Check for new entities and state changes
        for (Entity entity : instance.getEntities()) {
            // Skip item entities - they are tracked separately via dropped item system
            if (entity instanceof ItemEntity) continue;

            int entityId = entity.getEntityId();
            currentEntities.add(entityId);

            if (!trackedEntities.contains(entityId)) {
                recordEntitySpawn(entity);
                trackedEntities.add(entityId);

                if (entity instanceof Player player) {
                    playerStates.put(entityId, new PlayerState(
                        player.isSneaking(), player.isSprinting()
                    ));
                    recordAllEquipment(player);
                    recordAllEffects(player);
                } else if (entity instanceof LivingEntity livingEntity) {
                    recordAllEquipment(livingEntity);
                    recordAllEffects(livingEntity);
                }
            }

            // Check for player state changes
            if (entity instanceof Player player) {
                checkPlayerStateChanges(player);
                checkEquipmentChanges(player);
                checkEffectChanges(player);
            } else if (entity instanceof LivingEntity livingEntity) {
                checkEquipmentChanges(livingEntity);
                checkEffectChanges(livingEntity);
            }
        }

        // Check for despawned entities
        Set<Integer> despawned = new HashSet<>(trackedEntities);
        despawned.removeAll(currentEntities);

        for (int entityId : despawned) {
            recorder.record(new RecordableEntityDespawn(entityId));
            trackedEntities.remove(entityId);
            playerStates.remove(entityId);
            entityEquipment.remove(entityId);
            entityEffects.remove(entityId);
        }
    }

    private void recordEntitySpawn(Entity entity) {
        UUID entityUuid = entity.getUuid();
        int entityTypeId = entity.getEntityType().id();

        RecordableEntitySpawn spawn = new RecordableEntitySpawn(
            entity.getEntityId(),
            entityUuid,
            entityTypeId,
            entity.getPosition().x(),
            entity.getPosition().y(),
            entity.getPosition().z(),
            entity.getPosition().yaw(),
            entity.getPosition().pitch()
        );

        recorder.record(spawn);
        if (entity instanceof Player player) {
            recordHeldItem(entity.getEntityId(), player.getItemInMainHand());
        }
    }

    private void recordAllEquipment(LivingEntity entity) {
        int entityId = entity.getEntityId();
        Map<EquipmentSlot, ItemStack> equipment = new HashMap<>();

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack item = entity.getEquipment(slot);
            if (!item.isAir()) {
                equipment.put(slot, item);
                recordEquipment(entityId, slotToId(slot), item);
            }
        }
        entityEquipment.put(entityId, equipment);
    }

    private void checkEquipmentChanges(LivingEntity entity) {
        int entityId = entity.getEntityId();
        Map<EquipmentSlot, ItemStack> cached = entityEquipment.computeIfAbsent(entityId, k -> new HashMap<>());

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack current = entity.getEquipment(slot);
            ItemStack previous = cached.get(slot);

            boolean changed = (previous == null && !current.isAir()) ||
                              (previous != null && !previous.equals(current));

            if (changed) {
                recordEquipment(entityId, slotToId(slot), current);
                if (current.isAir()) {
                    cached.remove(slot);
                } else {
                    cached.put(slot, current);
                }
            }
        }
    }

    private void recordAllEffects(LivingEntity entity) {
        int entityId = entity.getEntityId();
        Set<PotionEffect> effects = new HashSet<>();

        for (TimedPotion potion : entity.getActiveEffects()) {
            effects.add(potion.potion().effect());
            recordEffect(entityId, potion);
        }
        entityEffects.put(entityId, effects);
    }

    private void checkEffectChanges(LivingEntity entity) {
        int entityId = entity.getEntityId();
        Set<PotionEffect> cached = entityEffects.computeIfAbsent(entityId, k -> new HashSet<>());
        Set<PotionEffect> current = new HashSet<>();

        for (TimedPotion potion : entity.getActiveEffects()) {
            current.add(potion.potion().effect());
            if (!cached.contains(potion.potion().effect())) {
                recordEffect(entityId, potion);
            }
        }

        // Note: effect removal could be tracked here if needed
        entityEffects.put(entityId, current);
    }

    private void recordEffect(int entityId, TimedPotion timedPotion) {
        byte flags = 0;
        var potion = timedPotion.potion();
        if (potion.isAmbient()) flags |= 0x01;
        if (potion.hasParticles()) flags |= 0x02;
        if (potion.hasIcon()) flags |= 0x04;

        recorder.record(new RecordableEntityEffect(
            entityId,
            potion.effect().id(),
            (byte) potion.amplifier(),
            potion.duration(),
            flags
        ));
    }

    private int slotToId(EquipmentSlot slot) {
        return switch (slot) {
            case BOOTS -> PlayerInventoryUtils.BOOTS_SLOT;
            case LEGGINGS -> PlayerInventoryUtils.LEGGINGS_SLOT;
            case CHESTPLATE -> PlayerInventoryUtils.CHESTPLATE_SLOT;
            case HELMET -> PlayerInventoryUtils.HELMET_SLOT;
            case MAIN_HAND -> 0;
            case OFF_HAND -> 40;
            default -> -1;
        };
    }

    private void checkPlayerStateChanges(Player player) {
        int entityId = player.getEntityId();
        PlayerState current = playerStates.get(entityId);

        if (current == null) {
            current = new PlayerState(false, false);
            playerStates.put(entityId, current);
        }

        // Check sneaking
        if (player.isSneaking() != current.sneaking) {
            recorder.record(new RecordablePlayerSneak(entityId, player.isSneaking()));
            playerStates.put(entityId, new PlayerState(player.isSneaking(), current.sprinting));
            current = playerStates.get(entityId);
        }

        // Check sprinting
        if (player.isSprinting() != current.sprinting) {
            recorder.record(new RecordablePlayerSprint(entityId, player.isSprinting()));
            playerStates.put(entityId, new PlayerState(current.sneaking, player.isSprinting()));
        }
    }

    public void recordArmSwing(int entityId, boolean mainHand) {
        recorder.record(new RecordablePlayerArmSwing(entityId, mainHand));
    }

    public void recordAnimation(int entityId, RecordableEntityAnimation.AnimationType animationType) {
        recorder.record(new RecordableEntityAnimation(entityId, animationType));
    }

    public void recordHeldItem(int entityId, ItemStack item) {
        recorder.record(
            new RecordablePlayerHandItem(
                entityId,
                BedWarsReplayManager.serializeItemStack(item)
            )
        );
    }

    public void recordEquipment(int entityId, int slot, ItemStack itemStack) {
        recorder.record(
            new RecordableEntityEquipment(
                entityId,
                slot,
                BedWarsReplayManager.serializeItemStack(itemStack)
            )
        );
    }

    public void recordEntityEffect(int entityId, int effectId, byte amplifier, int durationTicks, byte flags) {
        recorder.record(new RecordableEntityEffect(entityId, effectId, amplifier, durationTicks, flags));
    }

    @Override
    public void cleanup() {
        trackedEntities.clear();
        playerStates.clear();
        entityEquipment.clear();
        entityEffects.clear();
    }

    @Override
    public String getName() {
        return "EntityLifecycle";
    }

    private record PlayerState(boolean sneaking, boolean sprinting) {
    }
}
