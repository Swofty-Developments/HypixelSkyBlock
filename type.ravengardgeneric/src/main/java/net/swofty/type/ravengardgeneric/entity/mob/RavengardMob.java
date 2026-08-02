package net.swofty.type.ravengardgeneric.entity.mob;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.color.Color;
import net.minestom.server.component.DataComponents;
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
import net.swofty.type.ravengardgeneric.entity.animation.RavengardMobClip;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class RavengardMob extends net.minestom.server.entity.EntityCreature {
    private static final List<RavengardMob> MOBS = new CopyOnWriteArrayList<>();
    private static final TextColor BAR_FULL = TextColor.color(0x5FEC7B);
    private static final TextColor BAR_EMPTY = TextColor.color(0x3D3D3D);
    private static final int BAR_SEGMENTS = 10;
    private static final float PLAYER_DAMAGE_PER_HIT = 8f;
    private static final int ATTACK_COOLDOWN_TICKS = 30;
    private static final int FLASH_TICKS = 9;
    private static final int FLASH_COLOR = 0xFF6666;
    private static final int BASE_COLOR = 0xFFFFFF;
    private static final double KNOCKBACK_HORIZONTAL = 0.79 * 20;
    private static final double KNOCKBACK_VERTICAL = 0.50 * 20;

    private final RavengardMobClip clip;
    private final List<Entity> parts = new ArrayList<>();
    private final List<Vec> partOffsets = new ArrayList<>();
    private LivingEntity healthBar;
    private Instance instance;

    private Pos position;
    private double health;
    private final double maxHealth;
    private String currentAnimation = "idle";
    private boolean oneShot;
    private int frame;
    private float[][] lastSentRotation;
    private int[] partDuration;
    private int attackCooldown;
    private int flashTicksLeft;
    private boolean dying;
    private float rigYaw;
    private static final float MAX_TURN_PER_TICK = 25f;

    public RavengardMob(RavengardMobClip clip, Pos position) {
        super(EntityType.ZOMBIE);
        this.clip = clip;
        this.position = position;
        this.rigYaw = position.yaw();
        this.maxHealth = clip.health() <= 0 ? 100 : clip.health();
        this.health = maxHealth;
        setInvisible(true);
        setSilent(true);
        getAttribute(net.minestom.server.entity.attribute.Attribute.MOVEMENT_SPEED)
                .setBaseValue(Math.max(0.15, clip.walkSpeed() * 2));
        getAttribute(net.minestom.server.entity.attribute.Attribute.MAX_HEALTH)
                .setBaseValue((float) maxHealth);
        setHealth((float) maxHealth);
        if (clip.walkSpeed() > 0) {
            addAIGroup(
                    List.of(new net.minestom.server.entity.ai.goal.MeleeAttackGoal(this, 1.8,
                            java.time.Duration.ofMillis(1500))),
                    List.of(new net.minestom.server.entity.ai.target.LastEntityDamagerTarget(this, 16),
                            new net.minestom.server.entity.ai.target.ClosestEntityTarget(this, 14,
                                    entity -> entity instanceof Player)));
        }
    }

    @Override
    public void attack(Entity target, boolean swingHand) {
        if (dying || attackCooldown > 0) return;
        attackCooldown = ATTACK_COOLDOWN_TICKS;
        List<String> options = clip.attackAnimations();
        if (!options.isEmpty()) {
            startOneShot(options.get(ThreadLocalRandom.current().nextInt(options.size())));
        }
        if (target instanceof LivingEntity living) {
            living.damage(net.minestom.server.entity.damage.DamageType.MOB_ATTACK, PLAYER_DAMAGE_PER_HIT);
            Vec away = living.getPosition().sub(position).asVec().withY(0);
            if (away.lengthSquared() > 1e-6) {
                away = away.normalize();
                living.setVelocity(new Vec(away.x() * KNOCKBACK_HORIZONTAL, KNOCKBACK_VERTICAL,
                        away.z() * KNOCKBACK_HORIZONTAL));
            }
        }
    }

    @Override
    public boolean damage(net.minestom.server.registry.RegistryKey<net.minestom.server.entity.damage.DamageType> type, float amount) {
        if (dying) return false;
        flash();
        if (getHealth() - amount <= 0) {
            health = 0;
            updateBar();
            beginDeath();
            return true;
        }
        boolean applied = super.damage(type, amount);
        health = getHealth();
        updateBar();
        return applied;
    }

    private void beginDeath() {
        dying = true;
        getAIGroups().clear();
        getNavigator().setPathTo(null);
        if (clip.animation("death") != null) {
            startOneShot("death");
        } else {
            removeRig();
        }
    }

    private void startOneShot(String animation) {
        if (clip.animation(animation) == null) return;
        currentAnimation = animation;
        oneShot = true;
        frame = 0;
    }

    private void flash() {
        flashTicksLeft = FLASH_TICKS;
        retint(FLASH_COLOR);
    }

    private void retint(int rgb) {
        for (int i = 0; i < parts.size() && i < clip.parts().size(); i++) {
            RavengardMobClip.Part part = clip.parts().get(i);
            final int index = i;
            parts.get(index).editEntityMeta(ItemDisplayMeta.class, meta ->
                    meta.setItemStack(partItem(part, rgb)));
        }
    }

    private static ItemStack partItem(RavengardMobClip.Part part, int rgb) {
        return ItemStack.builder(Material.LEATHER_BOOTS)
                .set(DataComponents.ITEM_MODEL, part.model())
                .set(DataComponents.DYED_COLOR, new Color(rgb))
                .build();
    }

    public static List<RavengardMob> mobs() {
        return MOBS;
    }

    public void spawnMob(Instance instance) {
        this.instance = instance;
        this.lastSentRotation = new float[clip.parts().size()][];
        this.partDuration = new int[clip.parts().size()];
        setInstance(instance, position);
        for (RavengardMobClip.Part part : clip.parts()) {
            Entity display = new Entity(EntityType.ITEM_DISPLAY);
            display.setNoGravity(true);
            display.editEntityMeta(ItemDisplayMeta.class, meta -> {
                meta.setItemStack(partItem(part, BASE_COLOR));
                ItemDisplayMeta.DisplayContext[] contexts = ItemDisplayMeta.DisplayContext.values();
                int contextOrdinal = part.context();
                meta.setDisplayContext(contexts[contextOrdinal >= 0 && contextOrdinal < contexts.length
                        ? contextOrdinal : 0]);
                if (part.translation() != null) meta.setTranslation(vec(part.translation()));
                meta.setScale(vec(part.scale()));
                if (part.leftRotation() != null) meta.setLeftRotation(part.leftRotation());
                meta.setRightRotation(part.rightRotation());
                meta.setTransformationInterpolationDuration(1);
                meta.setPosRotInterpolationDuration(2);
                meta.setViewRange(part.viewRange());
            });
            double[] off = part.offset();
            Vec offset = new Vec(off[0], off[1], off[2]);
            display.setInstance(instance, position.add(offset));
            parts.add(display);
            partOffsets.add(offset);
        }
        java.util.Arrays.fill(partDuration, 1);

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

    private void updateBar() {
        if (healthBar != null) {
            healthBar.editEntityMeta(TextDisplayMeta.class, meta -> meta.setText(barText()));
        }
    }

    private Component barText() {
        int full = (int) Math.ceil(BAR_SEGMENTS * Math.max(0, health) / maxHealth);
        return Component.text("■".repeat(full)).color(BAR_FULL)
                .append(Component.text("■".repeat(BAR_SEGMENTS - full)).color(BAR_EMPTY));
    }

    public void removeRig() {
        parts.forEach(Entity::remove);
        if (healthBar != null) healthBar.remove();
        MOBS.remove(this);
        remove();
    }

    public void tickRig() {
        if (instance == null || isRemoved()) {
            return;
        }
        Pos previous = position;
        position = getPosition();
        boolean moved = !previous.samePoint(position);
        float yaw = updateRigYaw(previous, moved);

        if (attackCooldown > 0) attackCooldown--;
        if (flashTicksLeft > 0 && --flashTicksLeft == 0) {
            retint(BASE_COLOR);
        }

        RavengardMobClip.Animation animation = clip.animation(currentAnimation);
        if (!oneShot) {
            String desired = moved && clip.animation("walk") != null ? "walk" : "idle";
            if (!desired.equals(currentAnimation)) {
                currentAnimation = desired;
                frame = 0;
                animation = clip.animation(desired);
            }
        }
        if (animation == null || animation.frames().isEmpty()) {
            return;
        }
        if (frame >= animation.frames().size()) {
            if (animation.loop()) {
                frame = 0;
            } else if (dying) {
                removeRig();
                return;
            } else {
                oneShot = false;
                currentAnimation = moved && clip.animation("walk") != null ? "walk" : "idle";
                frame = 0;
                animation = clip.animation(currentAnimation);
                if (animation == null || animation.frames().isEmpty()) return;
            }
        }

        Map<String, RavengardMobClip.Keyframe> keyframes = animation.frames().get(frame);
        for (int i = 0; i < parts.size() && i < clip.parts().size(); i++) {
            Entity part = parts.get(i);
            if ((moved || oneShot) && !dying) {
                part.teleport(position.add(rotateOffset(partOffsets.get(i), yaw)));
            }
            RavengardMobClip.Keyframe keyframe = keyframes.get(String.valueOf(i));
            if (keyframe == null) continue;
            final int index = i;
            final float finalYaw = yaw;
            part.editEntityMeta(ItemDisplayMeta.class, meta -> {
                Integer duration = keyframe.duration();
                if (duration != null && duration != partDuration[index]) {
                    partDuration[index] = duration;
                    meta.setTransformationInterpolationDuration(duration);
                }
                meta.setTransformationInterpolationStartDelta(0);
                if (keyframe.translation() != null) {
                    meta.setTranslation(rotateOffset(vec(keyframe.translation()), finalYaw));
                }
                if (keyframe.leftRotation() != null) {
                    meta.setLeftRotation(rotateRig(index, keyframe.leftRotation(), finalYaw));
                }
                if (keyframe.scale() != null) {
                    meta.setScale(vec(keyframe.scale()));
                }
            });
        }
        if ((moved || oneShot) && !dying) {
            healthBar.teleport(position.add(0, 2.25, 0));
        }

        frame++;
    }

    private float updateRigYaw(Pos previous, boolean moved) {
        if (dying) return rigYaw;
        Float desired = null;
        Entity target = getTarget();
        if (target != null && !target.isRemoved()) {
            double dx = target.getPosition().x() - position.x();
            double dz = target.getPosition().z() - position.z();
            if (dx * dx + dz * dz > 1e-4) {
                desired = (float) Math.toDegrees(Math.atan2(-dx, dz));
            }
        } else if (moved) {
            double dx = position.x() - previous.x();
            double dz = position.z() - previous.z();
            if (dx * dx + dz * dz > 1e-6) {
                desired = (float) Math.toDegrees(Math.atan2(-dx, dz));
            }
        }
        if (desired != null) {
            float delta = desired - rigYaw;
            while (delta > 180) delta -= 360;
            while (delta < -180) delta += 360;
            rigYaw += Math.clamp(delta, -MAX_TURN_PER_TICK, MAX_TURN_PER_TICK);
            while (rigYaw > 180) rigYaw -= 360;
            while (rigYaw < -180) rigYaw += 360;
        }
        return rigYaw;
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

    private float[] rotateRig(int part, float[] rotation, float yaw) {
        double theta = Math.toRadians(yaw) / 2.0;
        float sin = (float) Math.sin(theta), cos = (float) Math.cos(theta);
        float rx = rotation[0], ry = rotation[1], rz = rotation[2], rw = rotation[3];
        float[] composed = {cos * rx + sin * rz, cos * ry + sin * rw,
                cos * rz - sin * rx, cos * rw - sin * ry};

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
