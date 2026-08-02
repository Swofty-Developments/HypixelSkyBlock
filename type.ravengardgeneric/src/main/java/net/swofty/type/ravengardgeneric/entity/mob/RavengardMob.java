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
public class RavengardMob {
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
    private InteractionEntity hitbox;
    private Instance instance;

    private Pos position;
    private double health;
    private final double maxHealth;
    private int frame;
    private int attackTicksLeft;
    private int attackCooldown;
    private Vec knockback = Vec.ZERO;

    public RavengardMob(RavengardAnimationClip clip, Pos position) {
        this.clip = clip;
        this.position = position;
        this.maxHealth = clip.health() <= 0 ? 100 : clip.health();
        this.health = maxHealth;
    }

    public static List<RavengardMob> mobs() {
        return MOBS;
    }

    public void spawn(Instance instance) {
        this.instance = instance;
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

        hitbox = new InteractionEntity(0.9f, 2.0f, (player, event) -> hit(player));
        hitbox.setInstance(instance, position);

        MOBS.add(this);
    }

    private Component barText() {
        int full = (int) Math.ceil(BAR_SEGMENTS * Math.max(0, health) / maxHealth);
        return Component.text("\u25A0".repeat(full)).color(BAR_FULL)
                .append(Component.text("\u25A0".repeat(BAR_SEGMENTS - full)).color(BAR_EMPTY));
    }

    public void hit(Player player) {
        if (health <= 0) {
            return;
        }
        health -= HIT_DAMAGE;
        Vec away = position.sub(player.getPosition()).asVec().withY(0);
        if (away.lengthSquared() > 0.001) {
            knockback = away.normalize().mul(0.45).withY(0.1);
        }
        healthBar.editEntityMeta(TextDisplayMeta.class, meta -> meta.setText(barText()));
        if (health <= 0) {
            remove();
        }
    }

    public void remove() {
        parts.forEach(Entity::remove);
        if (healthBar != null) healthBar.remove();
        if (hitbox != null) hitbox.remove();
        MOBS.remove(this);
    }

    public void tick() {
        if (instance == null || health <= 0) {
            return;
        }

        Player target = nearestPlayer();
        boolean attacking = attackTicksLeft > 0;
        boolean moved = false;
        float yaw = position.yaw();

        if (!attacking && target != null) {
            double distance = position.distance(target.getPosition());
            Vec toTarget = target.getPosition().sub(position).asVec().withY(0);
            if (toTarget.lengthSquared() > 0.01) {
                yaw = (float) Math.toDegrees(Math.atan2(-toTarget.x(), toTarget.z()));
            }
            if (distance <= ATTACK_RANGE && attackCooldown <= 0) {
                attackTicksLeft = attackFrames();
                attackCooldown = ATTACK_COOLDOWN_TICKS;
                frame = 0;
                target.damage(net.minestom.server.entity.damage.DamageType.MOB_ATTACK, PLAYER_DAMAGE_PER_HIT);
            } else if (distance > ATTACK_RANGE && distance <= CHASE_RANGE && clip.walkSpeed() > 0) {
                Vec step = toTarget.normalize().mul(clip.walkSpeed());
                position = position.add(step.x(), 0, step.z()).withYaw(yaw);
                moved = true;
            } else {
                position = position.withYaw(yaw);
            }
        }

        if (knockback.lengthSquared() > 0.001) {
            position = position.add(knockback.x(), 0, knockback.z());
            knockback = knockback.mul(0.6);
            moved = true;
        }

        if (attackCooldown > 0) attackCooldown--;

        String phase = attacking ? "talk" : "idle";
        List<RavengardAnimationClip.Part> clipParts = clip.parts();
        for (int i = 0; i < parts.size() && i < clipParts.size(); i++) {
            Entity part = parts.get(i);
            List<RavengardAnimationClip.Frame> frames = clipParts.get(i).phase(
                    attacking ? net.swofty.type.ravengardgeneric.entity.animation.RavengardAnimationPhase.TALK
                              : net.swofty.type.ravengardgeneric.entity.animation.RavengardAnimationPhase.IDLE);
            Pos partPos = position.add(rotateOffset(partOffsets.get(i), yaw)).withYaw(yaw);
            if (moved || attacking) {
                part.teleport(partPos);
            }
            if (frames != null && !frames.isEmpty()) {
                final RavengardAnimationClip.Frame animationFrame = frames.get(frame % frames.size());
                final float[] rotated = rotateRig(animationFrame.leftRotation(), yaw);
                final float finalYaw = yaw;
                part.editEntityMeta(ItemDisplayMeta.class, meta -> {
                    meta.setTransformationInterpolationStartDelta(0);
                    meta.setTranslation(rotateTranslation(vec(animationFrame.translation()), finalYaw));
                    meta.setLeftRotation(rotated);
                });
            }
        }
        if (moved || attacking) {
            healthBar.teleport(position.add(0, 2.25, 0));
            hitbox.teleport(position);
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

    private Player nearestPlayer() {
        Player nearest = null;
        double best = CHASE_RANGE * CHASE_RANGE;
        for (var player : HypixelGenericLoader.getLoadedPlayers()) {
            if (player.getInstance() != instance) continue;
            double distance = player.getPosition().distanceSquared(position);
            if (distance < best) {
                best = distance;
                nearest = player;
            }
        }
        return nearest;
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

    private static float[] rotateRig(float[] rotation, float yaw) {
        double theta = Math.toRadians(-yaw) / 2.0;
        float sin = (float) Math.sin(theta), cos = (float) Math.cos(theta);
        float rx = rotation[0], ry = rotation[1], rz = rotation[2], rw = rotation[3];
        return new float[]{cos * rx + sin * rz, cos * ry + sin * rw, cos * rz - sin * rx, cos * rw - sin * ry};
    }

    public static void startTicking() {
        MinecraftServer.getSchedulerManager()
                .buildTask(() -> MOBS.forEach(mob -> {
                    try {
                        mob.tick();
                    } catch (Exception ignored) {
                    }
                }))
                .repeat(net.minestom.server.timer.TaskSchedule.tick(1))
                .schedule();
    }
}
