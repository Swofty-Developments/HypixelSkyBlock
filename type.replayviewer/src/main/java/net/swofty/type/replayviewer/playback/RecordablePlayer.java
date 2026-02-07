package net.swofty.type.replayviewer.playback;

import net.kyori.adventure.nbt.BinaryTagIO;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.utils.inventory.PlayerInventoryUtils;
import net.swofty.type.game.replay.recordable.*;
import net.swofty.type.game.replay.recordable.bedwars.RecordableBedDestruction;
import net.swofty.type.game.replay.recordable.bedwars.RecordableFinalKill;
import net.swofty.type.game.replay.recordable.bedwars.RecordableGeneratorUpgrade;
import net.swofty.type.game.replay.recordable.bedwars.RecordableTeamElimination;
import net.swofty.type.replayviewer.entity.ReplayDroppedItemEntity;
import net.swofty.type.replayviewer.entity.ReplayEntity;
import net.swofty.type.replayviewer.entity.ReplayPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class RecordablePlayer {

    public static void play(Recordable recordable, ReplaySession session) {
        switch (recordable.getType()) {
            case BLOCK_CHANGE -> playBlockChange((RecordableBlockChange) recordable, session);
            case ENTITY_SPAWN -> playEntitySpawn((RecordableEntitySpawn) recordable, session);
            case ENTITY_DESPAWN -> playEntityDespawn((RecordableEntityDespawn) recordable, session);
            case ENTITY_LOCATIONS -> playEntityLocations((RecordableEntityLocations) recordable, session);
            case ENTITY_VELOCITY -> playEntityVelocity((RecordableEntityVelocity) recordable, session);
            case ENTITY_ANIMATION -> playEntityAnimation((RecordableEntityAnimation) recordable, session);
            case ENTITY_EQUIPMENT -> playEntityEquipment((RecordableEntityEquipment) recordable, session);
            case PLAYER_SNEAK -> playPlayerSneak((RecordablePlayerSneak) recordable, session);
            case PLAYER_SPRINT -> playPlayerSprint((RecordablePlayerSprint) recordable, session);
            case PLAYER_ARM_SWING -> playPlayerArmSwing((RecordablePlayerArmSwing) recordable, session);
            case PLAYER_DEATH -> playPlayerDeath((RecordablePlayerDeath) recordable, session);
            case PLAYER_RESPAWN -> playPlayerRespawn((RecordablePlayerRespawn) recordable, session);
            case PLAYER_SKIN -> playPlayerSkin((RecordablePlayerSkin) recordable, session);
            case PLAYER_DISPLAY_NAME -> playPlayerDisplayName((RecordablePlayerDisplayName) recordable, session);
            case PLAYER_HEALTH -> playPlayerHealth((RecordablePlayerHealth) recordable, session);
            case PLAYER_CHAT -> playPlayerChat((RecordablePlayerChat) recordable, session);
            case PARTICLE -> playParticle((RecordableParticle) recordable, session);
            case SOUND -> playSound((RecordableSound) recordable, session);
            case EXPLOSION -> playExplosion((RecordableExplosion) recordable, session);
            case PLAYER_HAND_ITEM -> playHandItem((RecordablePlayerHandItem) recordable, session);

            // bw
            case BEDWARS_BED_DESTRUCTION -> playBedDestruction((RecordableBedDestruction) recordable, session);
            case BEDWARS_FINAL_KILL -> playFinalKill((RecordableFinalKill) recordable, session);
            case BEDWARS_TEAM_ELIMINATION -> playTeamElimination((RecordableTeamElimination) recordable, session);
            case BEDWARS_EVENT_CONTINUE -> playGeneratorUpgrade((RecordableGeneratorUpgrade) recordable, session);

            // Dropped items
            case DROPPED_ITEM -> playDroppedItem((RecordableDroppedItem) recordable, session);
            case ITEM_PICKUP -> playItemPickup((RecordableItemPickup) recordable, session);

            // Dynamic text displays
            case DYNAMIC_TEXT_DISPLAY -> playDynamicTextDisplay((RecordableDynamicTextDisplay) recordable, session);
            case TEXT_DISPLAY_UPDATE -> playTextDisplayUpdate((RecordableTextDisplayUpdate) recordable, session);

            // NPC enhancements
            case NPC_DISPLAY_NAME -> playNpcDisplayName((RecordableNpcDisplayName) recordable, session);
            case NPC_TEXT_LINE -> playNpcTextLine((RecordableNpcTextLine) recordable, session);

            default -> {
            }
        }

        if (recordable.isEntityState() && recordable.getEntityId() >= 0) {
            trackEntityState(recordable, session);
        }
    }

    private static void playBlockChange(RecordableBlockChange rec, ReplaySession session) {
        Block block = Block.fromStateId(rec.getBlockStateId());
        if (block != null) {
            session.getInstance().setBlock(rec.getX(), rec.getY(), rec.getZ(), block);
        }
    }

    private static void playEntitySpawn(RecordableEntitySpawn rec, ReplaySession session) {
        Pos pos = new Pos(rec.getX(), rec.getY(), rec.getZ(), rec.getYaw(), rec.getPitch());

        // Determine entity type
        EntityType type = EntityType.fromId(rec.getEntityTypeId());
        if (type == null) type = EntityType.PLAYER;

        if (type == EntityType.PLAYER) {
            var skinData = session.getStateTracker().getSkin(rec.getEntityId());
            String displayName = session.getMetadata().getPlayers().get(rec.getEntityUuid());
            if (displayName == null) displayName = "Player";

            ReplayPlayerEntity playerEntity =
                new ReplayPlayerEntity(
                    displayName,
                    skinData != null ? skinData.textureValue() : null,
                    skinData != null ? skinData.textureSignature() : null
                );
            session.getEntityManager().spawnEntity(rec.getEntityId(), playerEntity, pos);
        } else {
            ReplayEntity entity = new ReplayEntity(type, rec.getEntityId(), rec.getEntityUuid());
            session.getEntityManager().spawnEntity(rec.getEntityId(), entity, pos);
        }
    }

    private static void playEntityDespawn(RecordableEntityDespawn rec, ReplaySession session) {
        session.getEntityManager().despawnEntity(rec.getEntityId());
    }

    private static void playHandItem(RecordablePlayerHandItem rec, ReplaySession session) {
        Entity entity = session.getEntityManager().getEntity(rec.getEntityId());
        if (entity instanceof LivingEntity livingEntity) {
            try {
                ItemStack item = nbtBytesToItem(rec.getItemBytes());
                livingEntity.setItemInMainHand(item);
            } catch (IOException e) {
                Logger.error(e);
            }
        }
    }

    private static void playEntityLocations(RecordableEntityLocations rec, ReplaySession session) {
        for (var entry : rec.getEntries()) {
            Pos pos = new Pos(entry.x(), entry.y(), entry.z(), entry.yaw(), entry.pitch());
            session.getEntityManager().updateEntityPosition(entry.entityId(), pos);
        }
    }

    private static void playEntityVelocity(RecordableEntityVelocity rec, ReplaySession session) {
        Entity entity = session.getEntityManager().getEntity(rec.getEntityId());
        if (entity != null) {
            entity.setVelocity(new net.minestom.server.coordinate.Vec(
                rec.getVelocityX(), rec.getVelocityY(), rec.getVelocityZ()
            ));
        }
    }

    private static void playEntityAnimation(RecordableEntityAnimation rec, ReplaySession session) {
        Entity entity = session.getEntityManager().getEntity(rec.getEntityId());
        if (entity instanceof ReplayPlayerEntity livingEntity) {
            switch (rec.getAnimation()) {
                case SWING_MAIN_HAND -> livingEntity.swingMainHand();
                case SWING_OFFHAND -> livingEntity.swingOffHand();
                case TAKE_DAMAGE -> livingEntity.takeVisualDamage();
            }
        }
    }

    private static ItemStack nbtBytesToItem(byte[] bytes) throws IOException {
        CompoundBinaryTag tag = BinaryTagIO.reader()
            .readNameless(new ByteArrayInputStream(bytes));

        return ItemStack.fromItemNBT(tag);
    }

    @Nullable
    private static EquipmentSlot convertSlot(int slotId) {
        return switch (slotId) {
            case PlayerInventoryUtils.BOOTS_SLOT -> EquipmentSlot.BOOTS;
            case PlayerInventoryUtils.LEGGINGS_SLOT -> EquipmentSlot.LEGGINGS;
            case PlayerInventoryUtils.CHESTPLATE_SLOT -> EquipmentSlot.CHESTPLATE;
            case PlayerInventoryUtils.HELMET_SLOT -> EquipmentSlot.HELMET;
            default -> null;
        };
    }

    private static void playEntityEquipment(RecordableEntityEquipment rec, ReplaySession session) {
        Entity entity = session.getEntityManager().getEntity(rec.getEntityId());
        if (entity instanceof LivingEntity livingEntity) {
            try {
                EquipmentSlot slot = convertSlot(rec.getSlotId());
                if (slot != null) {
                    livingEntity.setEquipment(slot, nbtBytesToItem(rec.getItemBytes()));
                } else {
                    Logger.warn("Unknown equipment slot id: " + rec.getSlotId());
                }
            } catch (IOException e) {
                Logger.error(e);
            }
        }
    }

    private static void playPlayerSneak(RecordablePlayerSneak rec, ReplaySession session) {
        Entity entity = session.getEntityManager().getEntity(rec.getEntityId());
        if (entity != null) {
            entity.setSneaking(rec.isSneaking());
        }
    }

    private static void playPlayerSprint(RecordablePlayerSprint rec, ReplaySession session) {
        Entity entity = session.getEntityManager().getEntity(rec.getEntityId());
        if (entity != null) {
            entity.setSprinting(rec.isSprinting());
        }
    }

    private static void playPlayerArmSwing(RecordablePlayerArmSwing rec, ReplaySession session) {
        Entity entity = session.getEntityManager().getEntity(rec.getEntityId());
        if (entity instanceof net.minestom.server.entity.LivingEntity livingEntity) {
            if (rec.isMainHand()) {
                livingEntity.swingMainHand();
            } else {
                livingEntity.swingOffHand();
            }
        }
    }

    private static void playPlayerDeath(RecordablePlayerDeath rec, ReplaySession session) {
        // TODO: death
    }

    private static void playPlayerRespawn(RecordablePlayerRespawn rec, ReplaySession session) {
        Pos pos = new Pos(rec.getX(), rec.getY(), rec.getZ(), rec.getYaw(), rec.getPitch());
        session.getEntityManager().updateEntityPosition(rec.getEntityId(), pos);
    }

    private static void playParticle(RecordableParticle rec, ReplaySession session) {
        // TODO: Would spawn particles for the viewer
    }

    private static void playSound(RecordableSound rec, ReplaySession session) {
        // TODO:Would play sounds for the viewer
    }

    private static void playExplosion(RecordableExplosion rec, ReplaySession session) {
        for (int[] pos : rec.getAffectedBlocks()) {
            session.getInstance().setBlock(pos[0], pos[1], pos[2], Block.AIR);
        }
    }


    private static void playPlayerSkin(RecordablePlayerSkin rec, ReplaySession session) {
        session.getStateTracker().trackSkin(rec.getEntityId(), rec.getTextureValue(), rec.getTextureSignature());

        Entity entity = session.getEntityManager().getEntity(rec.getEntityId());
        if (entity instanceof ReplayPlayerEntity playerEntity) {
            playerEntity.updateSkin(rec.getTextureValue(), rec.getTextureSignature());
        }
    }

    private static void playPlayerDisplayName(RecordablePlayerDisplayName rec, ReplaySession session) {
        Entity entity = session.getEntityManager().getEntity(rec.getEntityId());
        if (entity != null) {
            String fullName = rec.getPrefix() + rec.getDisplayName() + rec.getSuffix();
            entity.setCustomName(Component.text(fullName));
            entity.setCustomNameVisible(true);
        }
    }

    private static void playPlayerChat(RecordablePlayerChat rec, ReplaySession session) {
        String playerName = session.getEntityDisplayName(rec.getEntityId());
        String prefix = rec.isShout() ? "§6[SHOUT] " : "§7[0star] ";

        session.getViewer().sendMessage(Component.text(
            prefix + playerName + " §f" + rec.getMessage()
        ));
    }

    private static void playPlayerHealth(RecordablePlayerHealth rec, ReplaySession session) {
        session.getStateTracker().trackHealth(rec.getEntityId(), rec.getHealth(), rec.getMaxHealth());
    }

    private static void playBedDestruction(RecordableBedDestruction rec, ReplaySession session) {
        String teamName = getTeamName(rec.getTeamId());
        session.getViewer().sendMessage(Component.text(
            "§c§lBED DESTROYED! §7" + teamName + "'s bed was destroyed!"
        ));
    }

    private static void playFinalKill(RecordableFinalKill rec, ReplaySession session) {
        String victimName = session.getMetadata().getPlayers().get(rec.getVictimUuid());
        String killerName = rec.getKillerUuid() != null ?
            session.getMetadata().getPlayers().get(rec.getKillerUuid()) : null;

        String message = killerName != null ?
            "§c§lFINAL KILL! §f" + killerName + " §7killed §f" + victimName :
            "§c§lFINAL KILL! §f" + victimName + " §7was eliminated";

        session.getViewer().sendMessage(Component.text(message));
    }

    private static void playTeamElimination(RecordableTeamElimination rec, ReplaySession session) {
        String teamName = getTeamName(rec.getTeamId());
        session.getViewer().sendMessage(Component.text(
            "§c§lTEAM ELIMINATED! §7" + teamName + " has been eliminated!"
        ));
    }

    private static void playGeneratorUpgrade(RecordableGeneratorUpgrade rec, ReplaySession session) {
        String genType = rec.getGeneratorType() == 0 ? "Diamond" : "Emerald";
        session.getViewer().sendMessage(Component.text(
            "§6§lGENERATOR UPGRADE! §e" + genType + " generators upgraded to tier " + rec.getTier()
        ));
    }

    private static void playDroppedItem(RecordableDroppedItem rec, ReplaySession session) {
        Pos pos = new Pos(rec.getX(), rec.getY(), rec.getZ());

        ReplayDroppedItemEntity itemEntity = new ReplayDroppedItemEntity(
            rec.getEntityId(),
            rec.getEntityUuid(),
            rec.getItemNbt(),
            rec.getDespawnTick()
        );

        itemEntity.setItemVelocity(rec.getVelocityX(), rec.getVelocityY(), rec.getVelocityZ());
        session.getEntityManager().spawnEntity(rec.getEntityId(), itemEntity, pos);

        // Track for auto-despawn
        session.getDroppedItemManager().trackItem(rec.getEntityId(), rec.getDespawnTick());
    }

    private static void playItemPickup(RecordableItemPickup rec, ReplaySession session) {
        // Remove the item entity (it was picked up)
        session.getEntityManager().despawnEntity(rec.getItemEntityId());
        session.getDroppedItemManager().untrackItem(rec.getItemEntityId());
    }

// ========== Dynamic Text Displays ==========

    private static void playDynamicTextDisplay(RecordableDynamicTextDisplay rec, ReplaySession session) {
        Pos pos = new Pos(rec.getX(), rec.getY(), rec.getZ());

        session.getDynamicTextManager().createDisplay(
            rec.getEntityId(),
            rec.getEntityUuid(),
            pos,
            rec.getTextLines(),
            rec.getDisplayType(),
            rec.getDisplayIdentifier()
        );
    }

    private static void playTextDisplayUpdate(RecordableTextDisplayUpdate rec, ReplaySession session) {
        session.getDynamicTextManager().updateDisplayText(
            rec.getEntityId(),
            rec.getNewTextLines(),
            rec.isReplaceAll(),
            rec.getStartLineIndex()
        );
    }

// ========== NPC Enhancements ==========

    private static void playNpcDisplayName(RecordableNpcDisplayName rec, ReplaySession session) {
        session.getNpcManager().updateNpcDisplayName(
            rec.getEntityId(),
            rec.getDisplayName(),
            rec.getPrefix(),
            rec.getSuffix(),
            rec.getNameColor(),
            rec.isVisible()
        );
    }

    private static void playNpcTextLine(RecordableNpcTextLine rec, ReplaySession session) {
        session.getNpcManager().updateNpcTextLines(
            rec.getEntityId(),
            rec.getTextLines(),
            rec.getYOffset(),
            rec.getDisplayDurationTicks()
        );
    }

    private static String getTeamName(byte teamId) {
        return switch (teamId) {
            case 0 -> "Red";
            case 1 -> "Blue";
            case 2 -> "Green";
            case 3 -> "Yellow";
            case 4 -> "Aqua";
            case 5 -> "White";
            case 6 -> "Pink";
            case 7 -> "Gray";
            default -> "Team " + teamId;
        };
    }

    private static void trackEntityState(Recordable recordable, ReplaySession session) {
        session.getStateTracker().track(recordable);
    }
}
