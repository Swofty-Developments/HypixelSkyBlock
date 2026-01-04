package net.swofty.type.skywarsgame.luckyblock.items.weapons;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.component.DataComponents;
import net.minestom.server.item.component.EnchantmentList;
import net.minestom.server.item.enchant.Enchantment;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockItemRegistry;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockWeapon;
import net.swofty.type.skywarsgame.user.SkywarsPlayer;

import java.util.List;

public class VomitBagel implements LuckyBlockWeapon {

    public static final String ID = "vomit_bagel";
    private static final int NAUSEA_DURATION = 160;

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getDisplayName() {
        return "Vomit Bagel";
    }

    @Override
    public Material getBaseMaterial() {
        return Material.PUMPKIN_PIE;
    }

    @Override
    public ItemStack createItemStack() {
        return ItemStack.builder(Material.PUMPKIN_PIE)
                .customName(Component.text("Vomit Bagel", NamedTextColor.GREEN)
                        .decoration(TextDecoration.ITALIC, false))
                .lore(List.of(
                        Component.empty(),
                        Component.text("Makes your target feel", NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.text("extremely ", NamedTextColor.GRAY)
                                .append(Component.text("nauseous", NamedTextColor.GREEN))
                                .append(Component.text("!", NamedTextColor.GRAY))
                                .decoration(TextDecoration.ITALIC, false),
                        Component.empty(),
                        Component.text("LUCKY BLOCK ITEM", NamedTextColor.GOLD)
                                .decoration(TextDecoration.ITALIC, false)
                                .decoration(TextDecoration.BOLD, true)
                ))
                .set(LuckyBlockItemRegistry.LUCKY_BLOCK_ITEM_TAG, ID)
                .set(DataComponents.ENCHANTMENTS, EnchantmentList.EMPTY.with(Enchantment.SHARPNESS, 2))
                .build();
    }

    @Override
    public float onWeaponHit(SkywarsPlayer holder, Entity target, float damage) {
        if (target instanceof LivingEntity living) {
            living.addEffect(new Potion(PotionEffect.NAUSEA, (byte) 0, NAUSEA_DURATION));
            holder.sendMessage(Component.text("Your target feels sick!", NamedTextColor.GREEN));
        }

        return damage;
    }

    @Override
    public double getAttackDamage() {
        return 5.0;
    }

    @Override
    public boolean hasOnHitEffect() {
        return true;
    }
}
