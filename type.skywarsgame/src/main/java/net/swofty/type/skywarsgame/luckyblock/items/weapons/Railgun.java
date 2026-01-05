package net.swofty.type.skywarsgame.luckyblock.items.weapons;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockItemRegistry;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockWeapon;
import net.swofty.type.skywarsgame.user.SkywarsPlayer;

import java.util.List;

public class Railgun implements LuckyBlockWeapon {

    public static final String ID = "railgun";
    private static final float TRUE_DAMAGE = 12.0f;
    private static final double MAX_RANGE = 50.0;
    private static final double HIT_RADIUS = 1.0;

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getDisplayName() {
        return "Railgun";
    }

    @Override
    public Material getBaseMaterial() {
        return Material.BLAZE_ROD;
    }

    @Override
    public ItemStack createItemStack() {
        return ItemStack.builder(Material.BLAZE_ROD)
                .customName(Component.text("Railgun", NamedTextColor.GOLD)
                        .decoration(TextDecoration.ITALIC, false))
                .lore(List.of(
                        Component.empty(),
                        Component.text("Single-shot weapon.", NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.empty(),
                        Component.text("Right-click to fire a", NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.text("devastating ray that deals", NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.text("12 true damage", NamedTextColor.RED)
                                .append(Component.text(" to the first", NamedTextColor.GRAY))
                                .decoration(TextDecoration.ITALIC, false),
                        Component.text("target within 50 blocks.", NamedTextColor.GRAY)
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

        Pos eyePos = holder.getPosition().add(0, holder.getEyeHeight(), 0);
        Vec direction = holder.getPosition().direction();

        LivingEntity target = findTarget(holder, instance, eyePos, direction);

        if (target != null) {
            target.damage(Damage.fromEntity(holder, TRUE_DAMAGE));
            holder.sendMessage(Component.text("Direct hit!", NamedTextColor.RED));
        } else {
            holder.sendMessage(Component.text("Missed!", NamedTextColor.GRAY));
        }

        return true;
    }

    private LivingEntity findTarget(SkywarsPlayer holder, Instance instance, Pos eyePos, Vec direction) {
        for (double distance = 0; distance <= MAX_RANGE; distance += 0.5) {
            Vec rayPoint = eyePos.asVec().add(direction.mul(distance));

            for (Entity entity : instance.getEntities()) {
                if (entity == holder) continue;
                if (!(entity instanceof LivingEntity living)) continue;
                if (entity instanceof Player player && player.getGameMode().name().equals("SPECTATOR")) continue;

                double entityDistance = entity.getPosition().asVec().distance(rayPoint);
                if (entityDistance <= HIT_RADIUS) {
                    return living;
                }
            }
        }
        return null;
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

    @Override
    public boolean dealsTrueDamage() {
        return true;
    }
}
