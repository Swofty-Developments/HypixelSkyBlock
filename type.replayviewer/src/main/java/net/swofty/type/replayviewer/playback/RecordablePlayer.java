package net.swofty.type.replayviewer.playback;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.instance.block.Block;
import net.swofty.commons.replay.recordable.*;
import net.swofty.type.replayviewer.entity.ReplayEntity;

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
            case PARTICLE -> playParticle((RecordableParticle) recordable, session);
            case SOUND -> playSound((RecordableSound) recordable, session);
            case EXPLOSION -> playExplosion((RecordableExplosion) recordable, session);
            default -> {} // Ignore unhandled types
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

        ReplayEntity entity = new ReplayEntity(type, rec.getEntityId(), rec.getEntityUuid());
        session.getEntityManager().spawnEntity(rec.getEntityId(), entity, pos);

        if (rec.getInitialMetadata() != null) {
            // TODO: parse metadata
        }
    }

    private static void playEntityDespawn(RecordableEntityDespawn rec, ReplaySession session) {
        session.getEntityManager().despawnEntity(rec.getEntityId());
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
        if (entity instanceof net.minestom.server.entity.LivingEntity livingEntity) {
            switch (rec.getAnimation()) {
                case SWING_MAIN_HAND -> livingEntity.swingMainHand();
                case SWING_OFFHAND -> livingEntity.swingOffHand();
                case TAKE_DAMAGE -> {
                    // todo: play animations
                }
            }
        }
    }

    private static void playEntityEquipment(RecordableEntityEquipment rec, ReplaySession session) {
        // TODO: aplpy equipment
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
        // TODO:Would play explosion effect
    }

    private static void trackEntityState(Recordable recordable, ReplaySession session) {
        // TODO: Track entity states for efficient seeking
        // The EntityStateTracker will handle this
    }
}
