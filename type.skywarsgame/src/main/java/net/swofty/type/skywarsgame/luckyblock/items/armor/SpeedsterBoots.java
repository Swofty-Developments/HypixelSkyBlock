package net.swofty.type.skywarsgame.luckyblock.items.armor;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockArmor;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockItemRegistry;
import net.swofty.type.skywarsgame.user.SkywarsPlayer;

import java.util.List;

public class SpeedsterBoots implements LuckyBlockArmor {

    public static final String ID = "speedster_boots";
    private static final int SPEED_REFRESH_TICKS = 40;

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getDisplayName() {
        return "Speedster Boots";
    }

    @Override
    public EquipmentSlot getSlot() {
        return EquipmentSlot.BOOTS;
    }

    @Override
    public Material getBaseMaterial() {
        return Material.LEATHER_BOOTS;
    }

    @Override
    public ItemStack createItemStack() {
        return ItemStack.builder(Material.LEATHER_BOOTS)
                .customName(Component.text("Speedster Boots", NamedTextColor.AQUA)
                        .decoration(TextDecoration.ITALIC, false))
                .lore(List.of(
                        Component.empty(),
                        Component.text("Grants ", NamedTextColor.GRAY)
                                .append(Component.text("Speed II", NamedTextColor.WHITE))
                                .append(Component.text(" while worn.", NamedTextColor.GRAY))
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
    public void onEquip(SkywarsPlayer player) {
        player.sendMessage(Component.text("You feel faster!", NamedTextColor.AQUA));
        applySpeedEffect(player);
    }

    @Override
    public void onUnequip(SkywarsPlayer player) {
        player.removeEffect(PotionEffect.SPEED);
    }

    @Override
    public void onWornTick(SkywarsPlayer player) {
        if (player.getAliveTicks() % SPEED_REFRESH_TICKS == 0) {
            applySpeedEffect(player);
        }
    }

    private void applySpeedEffect(SkywarsPlayer player) {
        player.addEffect(new Potion(PotionEffect.SPEED, (byte) 1, 100));
    }

    @Override
    public boolean hasPermanentBuff() {
        return true;
    }
}
