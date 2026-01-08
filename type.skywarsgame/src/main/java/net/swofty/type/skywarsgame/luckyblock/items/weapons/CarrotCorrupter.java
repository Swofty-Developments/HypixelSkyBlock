package net.swofty.type.skywarsgame.luckyblock.items.weapons;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockItemRegistry;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockWeapon;
import net.swofty.type.skywarsgame.user.SkywarsPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class CarrotCorrupter implements LuckyBlockWeapon {

    public static final String ID = "carrot_corrupter";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getDisplayName() {
        return "Carrot Corrupter";
    }

    @Override
    public Material getBaseMaterial() {
        return Material.CARROT_ON_A_STICK;
    }

    @Override
    public ItemStack createItemStack() {
        return ItemStack.builder(Material.CARROT_ON_A_STICK)
                .customName(Component.text("Carrot Corrupter", NamedTextColor.GOLD)
                        .decoration(TextDecoration.ITALIC, false))
                .lore(List.of(
                        Component.empty(),
                        Component.text("Corrupts a random item in", NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.text("your target's hotbar into", NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.text("a ", NamedTextColor.GRAY)
                                .append(Component.text("carrot", NamedTextColor.GOLD))
                                .append(Component.text("!", NamedTextColor.GRAY))
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
        if (target instanceof Player player) {
            List<Integer> validSlots = new ArrayList<>();
            for (int i = 0; i < 9; i++) {
                ItemStack item = player.getInventory().getItemStack(i);
                if (!item.isAir()) {
                    validSlots.add(i);
                }
            }

            if (!validSlots.isEmpty()) {
                int slot = validSlots.get(ThreadLocalRandom.current().nextInt(validSlots.size()));
                player.getInventory().setItemStack(slot, ItemStack.of(Material.CARROT));
                holder.sendMessage(Component.text("You corrupted an item into a carrot!", NamedTextColor.GOLD));
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
