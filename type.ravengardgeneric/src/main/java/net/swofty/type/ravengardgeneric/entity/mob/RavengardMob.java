package net.swofty.type.ravengardgeneric.entity.mob;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.metadata.display.AbstractDisplayMeta;
import net.minestom.server.entity.metadata.display.ItemDisplayMeta;
import net.minestom.server.entity.metadata.display.TextDisplayMeta;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.component.DataComponents;
import net.swofty.type.generic.HypixelGenericLoader;
import net.swofty.type.generic.entity.InteractionEntity;
import net.swofty.type.ravengardgeneric.entity.animation.RavengardAnimationClip;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A combat mob driven by a captured rig: the clip's idle and attack tracks animate the parts,
 * it chases the nearest player and swings when in reach, and the captured ten square health bar
 * floats above it, draining as it takes hits.
 */
public class RavengardMob extends net.minestom.server.entity.EntityCreature {
    private static final List<RavengardMob> MOBS = new CopyOnWriteArrayList<>();
    private static final TextColor BAR_FULL = TextColor.color(0x5FEC7B);
    private static final TextColor BAR_EMPTY = TextColor.color(0x3D3D3D);
    private static final int BAR_SEGMENTS = 10;
    private static final double CHASE_RANGE = 14;
    private static final double ATTACK_RANGE = 2.4;
    private static final float PLAYER_DAMAGE_PER_HIT = 8f;
    private static final int HIT_DAMAGE = 20;
    private static final int ATTACK_COOLDOWN_TICKS = 30;

    private final RavengardAnimationClip clip;
    private final List<Entity> parts = new ArrayList<>();
    private final List<Vec> partOffsets = new ArrayList<>();
    private LivingEntity healthBar;
    private Instance instance;

    private Pos position;
    private double health;
    private final double maxHealth;
    private int frame;
    private float[][] lastSentRotation;
    private int attackTicksLeft;

    public RavengardMob(RavengardAnimationClip clip, Pos position) {
        super(EntityType.ZOMBIE);
        this.clip = clip;
        this.position = position;
        this.maxHealth = clip.health() <= 0 ? 100 : clip.health();
        this.health = maxHealth;
        setInvisible(true);
        setSilent(true);
        getAttribute(net.minestom.server.entity.attribute.Attribute.MOVEMENT_SPEED)
                .setBaseValue(Math.max(0.15, clip.walkSpeed() * 2));
        getAttribute(net.minestom.server.entity.attribute.Attribute.MAX_HEALTH)
                .setBaseValue((float) maxHealth);
        setHealth((float) maxHealth);
        addAIGroup(
                List.of(new net.minestom.server.entity.ai.goal.MeleeAttackGoal(this, 1.8,
                        java.time.Duration.ofMillis(1500))),
                List.of(new net.minestom.server.entity.ai.target.LastEntityDamagerTarget(this, 16),
                        new net.minestom.server.entity.ai.target.ClosestEntityTarget(this, 14,
                                entity -> entity instanceof Player)));
    }

    /** MeleeAttackGoal path: play the captured swing and hurt the target. */
    @Override
    public void attack(Entity target, boolean swingHand) {
        if (attackTicksLeft <= 0) {
            attackTicksLeft = attackFrames();
            frame = 0;
        }
        if (target instanceof net.minestom.server.entity.LivingEntity living) {
            living.damage(net.minestom.server.entity.damage.DamageType.MOB_ATTACK, PLAYER_DAMAGE_PER_HIT);
        }
    }

    /** Damage from any source drains the captured bar; vanilla handles the knockback. */
    @Override
    public boolean damage(net.minestom.server.registry.RegistryKey<net.minestom.server.entity.damage.DamageType> type, float amount) {
        boolean applied = super.damage(type, amount);
        health = getHealth();
        if (healthBar != null) {
            healthBar.editEntityMeta(TextDisplayMeta.class, meta -> meta.setText(barText()));
        }
        if (health <= 0) {
            removeRig();
        }
        return applied;
    }

    public static List<RavengardMob> mobs() {
        return MOBS;
    }

    public void spawnMob(Instance instance) {
        this.instance = instance;
        this.lastSentRotation = new float[clip.parts().size()][];
        setInstance(instance, position);
        for (RavengardAnimationClip.Part part : clip.parts()) {
            RavengardAnimationClip.Base base = part.base();
            Entity display = new Entity(EntityType.ITEM_DISPLAY);
            display.setNoGravity(true);
            display.editEntityMeta(ItemDisplayMeta.class, meta -> {
                meta.setItemStack(ItemStack.builder(Material.LEATHER_BOOTS)
                        .set(DataComponents.ITEM_MODEL, base.model()).build());
                ItemDisplayMeta.DisplayContext[] contexts = ItemDisplayMeta.DisplayContext.values();
                int contextOrdinal = base.itemDisplayContext();
                meta.setDisplayContext(contexts[contextOrdinal >= 0 && contextOrdinal < contexts.length
                        ? contextOrdinal : 0]);
                meta.setTranslation(vec(base.translation()));
                meta.setScale(vec(base.scale()));
                meta.setLeftRotation(base.leftRotation());
                meta.setRightRotation(base.rightRotation());
                meta.setTransformationInterpolationDuration(clip.interpolationDuration());
                meta.setPosRotInterpolationDuration(2);
                meta.setViewRange((float) base.viewRange());
            });
            double[] off = base.offset();
            Vec offset = new Vec(off[0], off[1], off[2]);
            display.setInstance(instance, position.add(offset));
            parts.add(display);
            partOffsets.add(offset);
        }

        healthBar = new LivingEntity(EntityType.TEXT_DISPLAY);
        healthBar.setNoGravity(true);
        healthBar.editEntityMeta(TextDisplayMeta.class, meta -> {
            meta.setText(barText());
            meta.setScale(new Vec(0.3, 0.3, 0.3));
            meta.setBillboardRenderConstraints(AbstractDisplayMeta.BillboardConstraints.CENTER);
            meta.setBackgroundColor(0);
            meta.setHasNoGravity(true);
        });
        healthBar.setInstance(instance, position.add(0, 2.25, 0));

        MOBS.add(this);
    }

    private Component barText() {
        int full = (int) Math.ceil(BAR_SEGMENTS * Math.max(0, health) / maxHealth);
        return Component.text("\u25A0".repeat(full)).color(BAR_FULL)
                .append(Component.text("\u25A0".repeat(BAR_SEGMENTS - full)).color(BAR_EMPTY));
    }

    public void removeRig() {
        parts.forEach(Entity::remove);
        if (healthBar != null) healthBar.remove();
        MOBS.remove(this);
        remove();
    }

    /** Glues the rig, bar and animations onto wherever the creature's pathfinding took it. */
    public void tickRig() {
        if (instance == null || isDead()) {
            return;
        }
        Pos previous = position;
        position = getPosition();
        boolean attacking = attackTicksLeft > 0;
        boolean moved = !previous.samePoint(position);
        float yaw = position.yaw();

        List<RavengardAnimationClip.Part> clipParts = clip.parts();
        for (int i = 0; i < parts.size() && i < clipParts.size(); i++) {
            Entity part = parts.get(i);
            List<RavengardAnimationClip.Frame> frames = clipParts.get(i).phase(
                    attacking ? net.swofty.type.ravengardgeneric.entity.animation.RavengardAnimationPhase.TALK
                              : net.swofty.type.ravengardgeneric.entity.animation.RavengardAnimationPhase.IDLE);
            Pos partPos = position.add(rotateOffset(partOffsets.get(i), yaw - (float) clip.yaw()));
            if (moved || attacking) {
                part.teleport(partPos);
            }
            if (frames != null && !frames.isEmpty()) {
                final RavengardAnimationClip.Frame animationFrame = frames.get(frame % frames.size());
                final float[] rotated = rotateRig(i, animationFrame.leftRotation(), yaw);
                final float finalYaw = yaw;
                part.editEntityMeta(ItemDisplayMeta.class, meta -> {
                    meta.setTransformationInterpolationStartDelta(0);
                    meta.setTranslation(rotateTranslation(vec(animationFrame.translation()), finalYaw - (float) clip.yaw()));
                    meta.setLeftRotation(rotated);
                });
            }
        }
        if (moved || attacking) {
            healthBar.teleport(position.add(0, 2.25, 0));
        }

        frame++;
        if (attacking) {
            attackTicksLeft--;
            if (attackTicksLeft <= 0) frame = 0;
        }
    }

    private int attackFrames() {
        int max = 0;
        for (RavengardAnimationClip.Part part : clip.parts()) {
            List<RavengardAnimationClip.Frame> frames = part.phase(
                    net.swofty.type.ravengardgeneric.entity.animation.RavengardAnimationPhase.TALK);
            if (frames != null) max = Math.max(max, frames.size());
        }
        return Math.max(1, max);
    }

    private static Vec vec(float[] values) {
        return new Vec(values[0], values[1], values[2]);
    }

    private static Vec rotateOffset(Vec offset, float yaw) {
        double theta = Math.toRadians(yaw);
        double sin = Math.sin(theta), cos = Math.cos(theta);
        return new Vec(offset.x() * cos + offset.z() * sin, offset.y(),
                -offset.x() * sin + offset.z() * cos);
    }

    private static Vec rotateTranslation(Vec translation, float yaw) {
        return rotateOffset(translation, yaw);
    }

    /**
     * Same composition the NPC rig uses: the yaw delta from the captured facing applied to both
     * quaternion and translation with one sign convention, so the body turns as one piece.
     */
    private float[] rotateRig(int part, float[] rotation, float yaw) {
        double theta = Math.toRadians(yaw - clip.yaw()) / 2.0;
        float sin = (float) Math.sin(theta), cos = (float) Math.cos(theta);
        float rx = rotation[0], ry = rotation[1], rz = rotation[2], rw = rotation[3];
        float[] composed = {cos * rx + sin * rz, cos * ry + sin * rw,
                cos * rz - sin * rx, cos * rw - sin * ry};

        // q and -q are one pose, but the client lerps raw components, so a sign flip between
        // consecutive sends plays as a full spin; keep every part on the hemisphere it last used
        float[] last = lastSentRotation[part];
        if (last != null) {
            float dot = last[0] * composed[0] + last[1] * composed[1]
                    + last[2] * composed[2] + last[3] * composed[3];
            if (dot < 0) {
                for (int i = 0; i < 4; i++) composed[i] = -composed[i];
            }
        }
        lastSentRotation[part] = composed;
        return composed;
    }

    public static void startTicking() {
        MinecraftServer.getSchedulerManager()
                .buildTask(() -> MOBS.forEach(mob -> {
                    try {
                        mob.tickRig();
                    } catch (Exception ignored) {
                    }
                }))
                .repeat(net.minestom.server.timer.TaskSchedule.tick(1))
                .schedule();
    }
}
