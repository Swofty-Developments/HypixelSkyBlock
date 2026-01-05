package net.swofty.type.skywarsgame.luckyblock.items.weapons;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockItemRegistry;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockWeapon;
import net.swofty.type.skywarsgame.user.SkywarsPlayer;

import java.util.List;

public class AxeOfPerun implements LuckyBlockWeapon {

    public static final String ID = "axe_of_perun";
    private static final float LIGHTNING_DAMAGE = 5.0f;

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getDisplayName() {
        return "Axe of Perun";
    }

    @Override
    public Material getBaseMaterial() {
        return Material.DIAMOND_AXE;
    }

    @Override
    public ItemStack createItemStack() {
        return ItemStack.builder(Material.DIAMOND_AXE)
                .customName(Component.text("Axe of Perun", NamedTextColor.YELLOW)
                        .decoration(TextDecoration.ITALIC, false))
                .lore(List.of(
                        Component.empty(),
                        Component.text("Strikes ", NamedTextColor.GRAY)
                                .append(Component.text("lightning", NamedTextColor.YELLOW))
                                .append(Component.text(" on hit.", NamedTextColor.GRAY))
                                .decoration(TextDecoration.ITALIC, false),
                        Component.text("Deals ", NamedTextColor.GRAY)
                                .append(Component.text("5", NamedTextColor.RED))
                                .append(Component.text(" true damage.", NamedTextColor.GRAY))
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
    public float onWeaponHit(SkywarsPlayer holder, Entity target, float damage) {
        if (target instanceof LivingEntity living) {
            Entity lightning = new Entity(EntityType.LIGHTNING_BOLT);
            lightning.setInstance(target.getInstance(), target.getPosition());

            holder.scheduler().buildTask(lightning::remove)
                    .delay(java.time.Duration.ofMillis(500))
                    .schedule();

            living.damage(Damage.fromEntity(holder, LIGHTNING_DAMAGE));

            holder.sendMessage(Component.text("Thunder strikes!", NamedTextColor.YELLOW));
        }

        return damage;
    }

    @Override
    public double getAttackDamage() {
        return 9.0;
    }

    @Override
    public boolean hasOnHitEffect() {
        return true;
    }

    @Override
    public boolean dealsTrueDamage() {
        return true;
    }
}
