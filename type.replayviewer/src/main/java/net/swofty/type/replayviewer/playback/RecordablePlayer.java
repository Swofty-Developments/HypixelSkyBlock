package net.swofty.type.replayviewer.playback;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.BinaryTagIO;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.network.NetworkBuffer;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import net.minestom.server.utils.inventory.PlayerInventoryUtils;
import net.swofty.type.game.replay.recordable.*;
import net.swofty.type.game.replay.recordable.bedwars.RecordableBedDestruction;
import net.swofty.type.game.replay.recordable.bedwars.RecordableKill;
import net.swofty.type.game.replay.recordable.bedwars.RecordableTeamElimination;
import net.swofty.type.replayviewer.entity.ReplayDroppedItemEntity;
import net.swofty.type.replayviewer.entity.ReplayEntity;
import net.swofty.type.replayviewer.entity.ReplayPlayerEntity;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RecordablePlayer {

    public static void play(Recordable recordable, ReplaySession session) {
        switch (recordable.getType()) {
            case BLOCK_CHANGE -> playBlockChange((RecordableBlockChange) recordable, session);
            case BLOCK_BREAK_ANIMATION -> playBlockBreakAnimation((RecordableBlockBreakAnimation) recordable, session);
            case ENTITY_SPAWN -> playEntitySpawn((RecordableEntitySpawn) recordable, session);
            case ENTITY_DESPAWN -> playEntityDespawn((RecordableEntityDespawn) recordable, session);
            case ENTITY_LOCATIONS -> playEntityLocations((RecordableEntityLocations) recordable, session);
            case ENTITY_ANIMATION -> playEntityAnimation((RecordableEntityAnimation) recordable, session);
            case ENTITY_EQUIPMENT -> playEntityEquipment((RecordableEntityEquipment) recordable, session);
            case ENTITY_EFFECT -> playEntityEffect((RecordableEntityEffect) recordable, session);
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
            case BEDWARS_KILL -> playFinalKill((RecordableKill) recordable, session);
            case BEDWARS_TEAM_ELIMINATION -> playTeamElimination((RecordableTeamElimination) recordable, session);

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
        // play sound to all viewers
        for (var viewer : session.getViewers()) {
            viewer.playSound(
                Sound.sound(
                    Key.key("minecraft:block." + block.key().value() + ".break"),
                    Sound.Source.BLOCK, 1.0f, 1.0f
                ),
                rec.getX(), rec.getY(), rec.getZ()
            );
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
                    skinData != null ? skinData.textureSignature() : null,
                    rec.getEntityUuid()
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
            if (session.getNpcManager().getNpcData(entry.entityId()) != null) {
                session.getNpcManager().updateNpcPosition(entry.entityId(), pos);
            }
        }
    }

    private static void playBlockBreakAnimation(RecordableBlockBreakAnimation rec, ReplaySession session) {
        net.minestom.server.network.packet.server.play.BlockBreakAnimationPacket packet = new net.minestom.server.network.packet.server.play.BlockBreakAnimationPacket(
            rec.getEntityId(),
            new Pos(rec.getX(), rec.getY(), rec.getZ()),
            rec.getStage()
        );
        for (var viewer : session.getViewers()) {
            viewer.sendPacket(packet);
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
            case 2 -> EquipmentSlot.BOOTS; // these seem to work from vanilla
            case 3 -> EquipmentSlot.LEGGINGS;
            case 4 -> EquipmentSlot.CHESTPLATE;
            case 5 -> EquipmentSlot.HELMET;
            case 0 -> EquipmentSlot.MAIN_HAND;
            case 1 -> EquipmentSlot.OFF_HAND;
            case 40 -> EquipmentSlot.OFF_HAND;
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

    private static void playEntityEffect(RecordableEntityEffect rec, ReplaySession session) {
        Entity entity = session.getEntityManager().getEntity(rec.getEntityId());
        if (entity instanceof LivingEntity livingEntity) {
            PotionEffect effect = PotionEffect.fromId(rec.getEffectId());
            if (effect != null) {
                // Flags: bit 0 = ambient, bit 1 = particles, bit 2 = icon
                byte flags = rec.getFlags();

                Potion potion = new Potion(effect, rec.getAmplifier(), rec.getDurationTicks(), flags);
                livingEntity.addEffect(potion);
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
        byte[] packetByteArray = rec.getData();
        ParticlePacket packet = ParticlePacket.SERIALIZER.read(NetworkBuffer.wrap(packetByteArray, 0, packetByteArray.length));

        for (var viewer : session.getViewers()) {
            viewer.sendPacket(packet);
        }
    }

    private static void playSound(RecordableSound rec, ReplaySession session) {
        @Subst("minecraft:block.note_block.pling") String soundId = rec.getSoundId();
        for (var viewer : session.getViewers()) {
            viewer.playSound(
                Sound.sound(
                    Key.key(soundId),
                    Sound.Source.values()[rec.getCategory()],
                        rec.getVolume(), rec.getPitch()
                ),
                rec.getX(), rec.getY(), rec.getZ()
            );
        }
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
            session.applyPlayerDisplayName(
                rec.getEntityId(),
                rec.getDisplayName(),
                rec.getPrefix(),
                rec.getSuffix(),
                rec.getNameColor()
            );
        }
    }

    private static void playPlayerChat(RecordablePlayerChat rec, ReplaySession session) {
        String playerName = session.getEntityDisplayName(rec.getEntityId());
        String message = rec.isShout()
            ? "§6[SHOUT] §r" + playerName + "§r: " + rec.getMessage()
            : "§7" + playerName + "§7: §f" + rec.getMessage();

        for (var viewer : session.getViewers()) {
            viewer.sendMessage(Component.text(message));
        }
    }

    private static void playPlayerHealth(RecordablePlayerHealth rec, ReplaySession session) {
        session.getStateTracker().trackHealth(rec.getEntityId(), rec.getHealth(), rec.getMaxHealth());
        Entity entity = session.getEntityManager().getEntity(rec.getEntityId());
        if (entity instanceof LivingEntity livingEntity) {
            livingEntity.setHealth(rec.getHealth());
        }

        session.updateBelowNameScore(rec.getEntityId(), (int) rec.getHealth());
    }

    private static void playBedDestruction(RecordableBedDestruction rec, ReplaySession session) {
        String teamName = getTeamName(rec.getTeamId());
        String teamColor = getTeamColor(rec.getTeamId());

        UUID destroyerUuid = getEntityUuid(session, rec.getDestroyerEntityId());
        String destroyerColor = getTeamColorForPlayer(session, destroyerUuid);
        String colouredPlayerName = destroyerColor + session.getEntityDisplayName(rec.getDestroyerEntityId());

        for (var viewer : session.getViewers()) {
            viewer.sendMessage(Component.text(""));
            viewer.sendMessage(Component.text(
                "§f§lBED DESTRUCTION > " + teamColor + teamName + " Bed §7has been destroyed by " + colouredPlayerName + "§7!"
            ));
            viewer.sendMessage(Component.text(""));
        }
    }

    private static void playFinalKill(RecordableKill rec, ReplaySession session) {
        String victimBaseName = session.getMetadata().getPlayers().get(rec.getVictimUuid());
        if (victimBaseName == null) {
            victimBaseName = String.valueOf(rec.getVictimEntityId());
        }
        String victim = getTeamColor(rec.getVictimTeamId()) + victimBaseName;

        String killer = null;
        if (rec.getKillerUuid() != null) {
            String killerBaseName = session.getMetadata().getPlayers().get(rec.getKillerUuid());
            if (killerBaseName != null) {
                killer = getTeamColorForPlayer(session, rec.getKillerUuid()) + killerBaseName;
            }
        }

        String deathMessage = switch (rec.getDeathCause()) {
            case 0 -> "§7" + victim + " died.";
            case 1 -> killer != null
                ? "§7" + victim + " was killed by " + killer + "§7."
                : "§7" + victim + " died.";
            case 2 -> "§7" + victim + " fell into the void.";
            case 3 -> killer != null
                ? "§7" + victim + " was knocked into the void by " + killer + "§7."
                : "§7" + victim + " fell into the void.";
            case 4 -> killer != null
                ? "§7" + victim + " was shot by " + killer + "§7."
                : "§7" + victim + " died.";
            case 5 -> killer != null
                ? "§7" + victim + " was slain by " + killer + "§7's entity."
                : "§7" + victim + " died.";
            default -> "§7" + victim + " died.";
        };

        String message = rec.getFinalKill() != 0 ? deathMessage + " §b§lFINAL KILL!" : deathMessage;
        for (var viewer : session.getViewers()) {
            viewer.sendMessage(Component.text(message));
        }
    }

    private static void playTeamElimination(RecordableTeamElimination rec, ReplaySession session) {
        String teamName = getTeamName(rec.getTeamId());
        String teamColor = getTeamColor(rec.getTeamId());
        for (var viewer : session.getViewers()) {
            viewer.sendMessage(Component.text(""));
            viewer.sendMessage(Component.text(
                "§f§lTEAM ELIMINATED > §c" + teamColor + teamName + " §7has been eliminated!"
            ));
            viewer.sendMessage(Component.text(""));
        }
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

    private static void playDynamicTextDisplay(RecordableDynamicTextDisplay rec, ReplaySession session) {
        Pos pos = new Pos(rec.getX(), rec.getY(), rec.getZ());

        session.getDynamicTextManager().createDisplay(
            rec.getEntityId(),
            rec.getEntityUuid(),
            pos,
            rec.getTextLines(),
            rec.getDisplayType(),
            rec.getDisplayIdentifier(),
            rec.getTick()
        );
    }

    private static void playTextDisplayUpdate(RecordableTextDisplayUpdate rec, ReplaySession session) {
        session.getDynamicTextManager().updateDisplayText(
            rec.getEntityId(),
            rec.getNewTextLines(),
            rec.isReplaceAll(),
            rec.getStartLineIndex(),
            rec.getTick()
        );
    }

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

    private static String getTeamColor(byte teamId) {
        return switch (teamId) {
            case 0 -> "§c";
            case 1 -> "§9";
            case 2 -> "§a";
            case 3 -> "§e";
            case 4 -> "§b";
            case 5 -> "§f";
            case 6 -> "§d";
            case 7 -> "§7";
            default -> "§7";
        };
    }

    private static UUID getEntityUuid(ReplaySession session, int entityId) {
        Entity entity = session.getEntityManager().getEntity(entityId);
        if (entity instanceof ReplayPlayerEntity playerEntity) {
            return playerEntity.getActualUuid();
        }
        if (entity instanceof ReplayEntity replayEntity) {
            return replayEntity.getRecordedUuid();
        }
        return null;
    }

    private static String getTeamColorForPlayer(ReplaySession session, UUID playerUuid) {
        if (playerUuid == null) {
            return "§7";
        }

        for (Map.Entry<String, List<UUID>> entry : session.getMetadata().getTeams().entrySet()) {
            if (!entry.getValue().contains(playerUuid)) {
                continue;
            }
            try {
                byte teamId = Byte.parseByte(entry.getKey());
                return getTeamColor(teamId);
            } catch (NumberFormatException ignored) {
                switch (entry.getKey().toUpperCase()) {
                    case "RED" -> {
                        return getTeamColor((byte) 0);
                    }
                    case "BLUE" -> {
                        return getTeamColor((byte) 1);
                    }
                    case "GREEN" -> {
                        return getTeamColor((byte) 2);
                    }
                    case "YELLOW" -> {
                        return getTeamColor((byte) 3);
                    }
                    case "AQUA" -> {
                        return getTeamColor((byte) 4);
                    }
                    case "WHITE" -> {
                        return getTeamColor((byte) 5);
                    }
                    case "PINK" -> {
                        return getTeamColor((byte) 6);
                    }
                    case "GRAY", "GREY" -> {
                        return getTeamColor((byte) 7);
                    }
                    default -> {
                    }
                }
            }
        }

        return "§7";
    }

    private static void trackEntityState(Recordable recordable, ReplaySession session) {
        session.getStateTracker().track(recordable);
    }
}
