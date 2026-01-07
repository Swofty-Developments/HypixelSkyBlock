package net.swofty.type.skywarsgame.luckyblock.items.weapons;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.component.DataComponents;
import net.minestom.server.item.component.EnchantmentList;
import net.minestom.server.item.enchant.Enchantment;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockItemRegistry;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockWeapon;
import net.swofty.type.skywarsgame.user.SkywarsPlayer;

import java.util.List;

public class StickOfTruth implements LuckyBlockWeapon {

    public static final String ID = "stick_of_truth";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getDisplayName() {
        return "Stick of Truth";
    }

    @Override
    public Material getBaseMaterial() {
        return Material.STICK;
    }

    @Override
    public ItemStack createItemStack() {
        return ItemStack.builder(Material.STICK)
                .customName(Component.text("Stick of Truth", NamedTextColor.AQUA)
                        .decoration(TextDecoration.ITALIC, false))
                .lore(List.of(
                        Component.empty(),
                        Component.text("Reveals the ", NamedTextColor.GRAY)
                                .append(Component.text("true identity", NamedTextColor.AQUA))
                                .decoration(TextDecoration.ITALIC, false),
                        Component.text("of disguised players!", NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.empty(),
                        Component.text("LUCKY BLOCK ITEM", NamedTextColor.GOLD)
                                .decoration(TextDecoration.ITALIC, false)
                                .decoration(TextDecoration.BOLD, true)
                ))
                .set(LuckyBlockItemRegistry.LUCKY_BLOCK_ITEM_TAG, ID)
                .set(DataComponents.ENCHANTMENTS, EnchantmentList.EMPTY.with(Enchantment.SHARPNESS, 1))
                .build();
    }

    @Override
    public float onWeaponHit(SkywarsPlayer holder, Entity target, float damage) {
        if (target instanceof Player player) {
            String realName = player.getUsername();
            Component displayName = player.getDisplayName();

            if (displayName != null) {
                String displayString = net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
                        .plainText().serialize(displayName);

                if (!displayString.equals(realName)) {
                    holder.sendMessage(Component.text("That player's true identity is: ", NamedTextColor.AQUA)
                            .append(Component.text(realName, NamedTextColor.YELLOW)));
                }
            }
        }

        return damage;
    }

    @Override
    public double getAttackDamage() {
        return 4.0;
    }

    @Override
    public boolean hasOnHitEffect() {
        return true;
    }
}
