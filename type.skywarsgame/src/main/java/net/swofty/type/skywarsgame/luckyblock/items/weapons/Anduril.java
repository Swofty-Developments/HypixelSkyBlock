package net.swofty.type.skywarsgame.luckyblock.items.weapons;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.entity.Entity;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockItemRegistry;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockWeapon;
import net.swofty.type.skywarsgame.user.SkywarsPlayer;

import java.util.List;

public class Anduril implements LuckyBlockWeapon {

    public static final String ID = "anduril";
    private static final int BUFF_REFRESH_TICKS = 20;

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getDisplayName() {
        return "Anduril";
    }

    @Override
    public Material getBaseMaterial() {
        return Material.IRON_SWORD;
    }

    @Override
    public ItemStack createItemStack() {
        return ItemStack.builder(Material.IRON_SWORD)
                .customName(Component.text("Anduril", NamedTextColor.GOLD)
                        .decoration(TextDecoration.ITALIC, false))
                .lore(List.of(
                        Component.empty(),
                        Component.text("Sharpness II", NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.empty(),
                        Component.text("While held:", NamedTextColor.GOLD)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.text(" \u2022 Speed I", NamedTextColor.AQUA)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.text(" \u2022 Resistance I", NamedTextColor.WHITE)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.empty(),
                        Component.text("\"The Flame of the West\"", NamedTextColor.DARK_GRAY)
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
        return damage;
    }

    @Override
    public void onHeldTick(SkywarsPlayer holder) {
        if (holder.getAliveTicks() % BUFF_REFRESH_TICKS == 0) {
            applyBuffs(holder);
        }
    }

    private void applyBuffs(SkywarsPlayer holder) {
        holder.addEffect(new Potion(PotionEffect.SPEED, (byte) 0, 60));
        holder.addEffect(new Potion(PotionEffect.RESISTANCE, (byte) 0, 60));
    }

    @Override
    public double getAttackDamage() {
        return 6.0 + 2.5;
    }

    @Override
    public boolean hasOnHitEffect() {
        return false;
    }

    @Override
    public boolean hasPassiveBuff() {
        return true;
    }
}
