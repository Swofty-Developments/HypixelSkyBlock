package net.swofty.type.skywarsgame.luckyblock.items.armor;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.color.Color;
import net.minestom.server.component.DataComponents;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockArmor;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockItemRegistry;
import net.swofty.type.skywarsgame.user.SkywarsPlayer;

import java.util.List;

public class HotHead implements LuckyBlockArmor {

    public static final String ID = "hot_head";
    private static final int FIRE_TICKS = 60;

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getDisplayName() {
        return "Hot Head";
    }

    @Override
    public EquipmentSlot getSlot() {
        return EquipmentSlot.HELMET;
    }

    @Override
    public Material getBaseMaterial() {
        return Material.LEATHER_HELMET;
    }

    @Override
    public ItemStack createItemStack() {
        return ItemStack.builder(Material.LEATHER_HELMET)
                .customName(Component.text("Hot Head", NamedTextColor.RED)
                        .decoration(TextDecoration.ITALIC, false))
                .lore(List.of(
                        Component.empty(),
                        Component.text("Protection I", NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.text("Fire Protection X", NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.text("Unbreaking I", NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.empty(),
                        Component.text("Sets ", NamedTextColor.GRAY)
                                .append(Component.text("enemies on fire", NamedTextColor.RED))
                                .append(Component.text(" when you hit them!", NamedTextColor.GRAY))
                                .decoration(TextDecoration.ITALIC, false),
                        Component.empty(),
                        Component.text("LUCKY BLOCK ITEM", NamedTextColor.GOLD)
                                .decoration(TextDecoration.ITALIC, false)
                                .decoration(TextDecoration.BOLD, true)
                ))
                .set(DataComponents.DYED_COLOR, new Color(255, 0, 0))
                .set(LuckyBlockItemRegistry.LUCKY_BLOCK_ITEM_TAG, ID)
                .build();
    }

    @Override
    public void onEquip(SkywarsPlayer player) {
        player.sendMessage(Component.text("Your head is on fire!", NamedTextColor.RED));
    }

    @Override
    public void onHit(SkywarsPlayer holder, Entity target) {
        if (target instanceof LivingEntity living) {
            living.setFireTicks(FIRE_TICKS);
        }
    }

    @Override
    public boolean hasHitEffect() {
        return true;
    }
}
