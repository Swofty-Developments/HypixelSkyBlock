package net.swofty.type.skywarsgame.luckyblock.items.armor;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockArmor;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockItemRegistry;
import net.swofty.type.skywarsgame.user.SkywarsPlayer;

import java.util.List;

public class Exodus implements LuckyBlockArmor {

    public static final String ID = "exodus";
    private static final int REGEN_DURATION_TICKS = 100;

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getDisplayName() {
        return "Exodus";
    }

    @Override
    public EquipmentSlot getSlot() {
        return EquipmentSlot.HELMET;
    }

    @Override
    public Material getBaseMaterial() {
        return Material.DIAMOND_HELMET;
    }

    @Override
    public ItemStack createItemStack() {
        return ItemStack.builder(Material.DIAMOND_HELMET)
                .customName(Component.text("Exodus", NamedTextColor.AQUA)
                        .decoration(TextDecoration.ITALIC, false))
                .lore(List.of(
                        Component.empty(),
                        Component.text("Grants ", NamedTextColor.GRAY)
                                .append(Component.text("Regeneration II", NamedTextColor.RED))
                                .append(Component.text(" on hit!", NamedTextColor.GRAY))
                                .decoration(TextDecoration.ITALIC, false),
                        Component.empty(),
                        Component.text("The departure from", NamedTextColor.DARK_GRAY)
                                .decoration(TextDecoration.ITALIC, true),
                        Component.text("death itself.", NamedTextColor.DARK_GRAY)
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
    public void onEquip(SkywarsPlayer player) {
        player.sendMessage(Component.text("You feel the power of Exodus!", NamedTextColor.AQUA));
    }

    @Override
    public void onHit(SkywarsPlayer holder, Entity target) {
        holder.addEffect(new Potion(PotionEffect.REGENERATION, (byte) 1, REGEN_DURATION_TICKS));
    }

    @Override
    public boolean hasHitEffect() {
        return true;
    }
}
