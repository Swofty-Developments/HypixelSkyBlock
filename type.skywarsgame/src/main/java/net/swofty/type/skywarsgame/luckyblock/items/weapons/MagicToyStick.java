package net.swofty.type.skywarsgame.luckyblock.items.weapons;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.pvp.entity.projectile.Snowball;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockItemRegistry;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockWeapon;
import net.swofty.type.skywarsgame.user.SkywarsPlayer;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.List;

public class MagicToyStick implements LuckyBlockWeapon {

    public static final String ID = "magic_toy_stick";
    private static final double EXPLOSION_RADIUS = 5.0;
    private static final double KNOCKBACK_POWER = 30.0;

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getDisplayName() {
        return "Magic Toy Stick";
    }

    @Override
    public Material getBaseMaterial() {
        return Material.STICK;
    }

    @Override
    public ItemStack createItemStack() {
        return ItemStack.builder(Material.STICK)
                .customName(Component.text("Magic Toy Stick", NamedTextColor.AQUA)
                        .decoration(TextDecoration.ITALIC, false))
                .lore(List.of(
                        Component.empty(),
                        Component.text("Launches a magic projectile", NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.text("that explodes on impact!", NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.empty(),
                        Component.text("No block damage, but", NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.text("launches players away!", NamedTextColor.RED)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.empty(),
                        Component.text("Uses: ", NamedTextColor.GRAY)
                                .append(Component.text("1", NamedTextColor.GREEN))
                                .decoration(TextDecoration.ITALIC, false),
                        Component.empty(),
                        Component.text("LUCKY BLOCK ITEM", NamedTextColor.GOLD)
                                .decoration(TextDecoration.ITALIC, false)
                                .decoration(TextDecoration.BOLD, true)
                ))
                .set(LuckyBlockItemRegistry.LUCKY_BLOCK_ITEM_TAG, ID)
                .build();
    }

    @Override
    public boolean onUse(SkywarsPlayer holder) {
        Instance instance = holder.getInstance();
        if (instance == null) return false;

        MagicProjectile projectile = new MagicProjectile(holder, instance);
        projectile.setNoGravity(true);
        projectile.setInstance(instance, holder.getPosition().add(0, holder.getEyeHeight(), 0));
        projectile.setVelocity(holder.getPosition().direction().mul(40));

        projectile.scheduler().buildTask(projectile::remove)
                .delay(Duration.ofSeconds(10))
                .schedule();

        return true;
    }

    @Override
    public float onWeaponHit(SkywarsPlayer holder, Entity target, float damage) {
        return damage;
    }

    @Override
    public int getMaxUses() {
        return 1;
    }

    @Override
    public boolean hasUseEffect() {
        return true;
    }

    @Override
    public boolean hasOnHitEffect() {
        return false;
    }

    private static class MagicProjectile extends Snowball {

        private final Instance instance;

        public MagicProjectile(@Nullable Entity shooter, Instance instance) {
            super(shooter);
            this.instance = instance;
        }

        @Override
        public boolean onStuck() {
            triggerStatus((byte) 3);
            explode();
            return true;
        }

        @Override
        public boolean onHit(Entity entity) {
            triggerStatus((byte) 3);
            explode();
            return true;
        }

        private void explode() {
            Point impactPos = position;

            for (Entity entity : instance.getEntities()) {
                if (entity == this || entity == getShooter()) continue;
                if (!(entity instanceof Player player)) continue;
                if (player.getGameMode().name().equals("SPECTATOR")) continue;

                double distance = entity.getPosition().distance(impactPos);
                if (distance > EXPLOSION_RADIUS) continue;

                Vec direction = Vec.fromPoint(entity.getPosition().sub(impactPos)).normalize();
                double power = KNOCKBACK_POWER * (1 - (distance / EXPLOSION_RADIUS));

                Vec knockback = new Vec(
                        direction.x() * power,
                        Math.max(15, power * 0.5),
                        direction.z() * power
                );

                entity.setVelocity(knockback);
            }
        }
    }
}
