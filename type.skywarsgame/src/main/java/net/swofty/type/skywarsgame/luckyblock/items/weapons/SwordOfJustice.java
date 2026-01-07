package net.swofty.type.skywarsgame.luckyblock.items.weapons;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.entity.Entity;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockItemRegistry;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockWeapon;
import net.swofty.type.skywarsgame.user.SkywarsPlayer;

import java.util.List;

public class SwordOfJustice implements LuckyBlockWeapon {

    public static final String ID = "sword_of_justice";
    private static final float HEAL_AMOUNT = 2.0f;

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getDisplayName() {
        return "Sword of Justice";
    }

    @Override
    public Material getBaseMaterial() {
        return Material.IRON_SWORD;
    }

    @Override
    public ItemStack createItemStack() {
        return ItemStack.builder(Material.IRON_SWORD)
                .customName(Component.text("Sword of Justice", NamedTextColor.WHITE)
                        .decoration(TextDecoration.ITALIC, false))
                .lore(List.of(
                        Component.empty(),
                        Component.text("Sharpness II", NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.empty(),
                        Component.text("Heals ", NamedTextColor.GRAY)
                                .append(Component.text("1\u2764", NamedTextColor.RED))
                                .append(Component.text(" on hit.", NamedTextColor.GRAY))
                                .decoration(TextDecoration.ITALIC, false),
                        Component.empty(),
                        Component.text("\"Justice is served.\"", NamedTextColor.DARK_GRAY)
                                .decoration(TextDecoration.ITALIC, true),
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
        float currentHealth = holder.getHealth();
        float maxHealth = 20.0f;

        if (currentHealth < maxHealth) {
            float newHealth = Math.min(currentHealth + HEAL_AMOUNT, maxHealth);
            holder.setHealth(newHealth);

            holder.sendMessage(Component.text("+1\u2764", NamedTextColor.RED));
        }

        return damage;
    }

    @Override
    public double getAttackDamage() {
        return 6.0 + 2.5;
    }

    @Override
    public boolean hasOnHitEffect() {
        return true;
    }
}
