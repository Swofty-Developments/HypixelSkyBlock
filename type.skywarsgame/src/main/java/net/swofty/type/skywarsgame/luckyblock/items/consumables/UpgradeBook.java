package net.swofty.type.skywarsgame.luckyblock.items.consumables;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockConsumable;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockItemRegistry;
import net.swofty.type.skywarsgame.user.SkywarsPlayer;

import java.util.List;

public class UpgradeBook implements LuckyBlockConsumable {

    private static final int DURATION_TICKS = 5 * 60 * 20;

    @Override
    public String getId() {
        return "upgrade_book";
    }

    @Override
    public String getDisplayName() {
        return "Upgrade Book";
    }

    @Override
    public ItemStack createItemStack() {
        return ItemStack.builder(Material.ENCHANTED_BOOK)
                .customName(Component.text(getDisplayName(), NamedTextColor.AQUA)
                        .decoration(TextDecoration.ITALIC, false)
                        .decoration(TextDecoration.BOLD, true))
                .lore(List.of(
                        Component.text("Grants you enhanced protection", NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.text("for 5 minutes!", NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.empty(),
                        Component.text("Right-click to use!", NamedTextColor.YELLOW)
                                .decoration(TextDecoration.ITALIC, false)
                ))
                .set(LuckyBlockItemRegistry.LUCKY_BLOCK_ITEM_TAG, getId())
                .build();
    }

    @Override
    public void onConsume(SkywarsPlayer player) {
        player.addEffect(new Potion(PotionEffect.RESISTANCE, (byte) 1, DURATION_TICKS));

        boolean hasArmor = !player.getEquipment(EquipmentSlot.HELMET).isAir() ||
                !player.getEquipment(EquipmentSlot.CHESTPLATE).isAir() ||
                !player.getEquipment(EquipmentSlot.LEGGINGS).isAir() ||
                !player.getEquipment(EquipmentSlot.BOOTS).isAir();

        if (hasArmor) {
            player.sendMessage(Component.text("Your armor has been magically enhanced!", NamedTextColor.AQUA));
        } else {
            player.sendMessage(Component.text("You feel protected by ancient magic!", NamedTextColor.AQUA));
        }
    }
}
