package net.swofty.type.skywarsgame.luckyblock.items.usables;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.component.DataComponents;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.EnchantmentList;
import net.minestom.server.item.enchant.Enchantment;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockItem;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockItemRegistry;
import net.swofty.type.skywarsgame.user.SkywarsPlayer;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class SuperDie implements LuckyBlockItem {

    private static final Random RANDOM = new Random();

    @Override
    public String getId() {
        return "super_die";
    }

    @Override
    public String getDisplayName() {
        return "Super Die";
    }

    @Override
    public ItemStack createItemStack() {
        return ItemStack.builder(Material.MAGMA_CREAM)
                .customName(Component.text(getDisplayName(), NamedTextColor.RED)
                        .decoration(TextDecoration.ITALIC, false)
                        .decoration(TextDecoration.BOLD, true))
                .lore(List.of(
                        Component.text("Roll the die and test", NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.text("your luck!", NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.empty(),
                        Component.text("1: Instant death", NamedTextColor.DARK_RED)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.text("2: Half health", NamedTextColor.RED)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.text("3: Absorption II", NamedTextColor.GOLD)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.text("4: Diamond Sword (Sharp I)", NamedTextColor.AQUA)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.text("5: Diamond Sword (Fire II)", NamedTextColor.LIGHT_PURPLE)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.text("6: Full Diamond Armor!", NamedTextColor.GREEN)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.empty(),
                        Component.text("Right-click to roll!", NamedTextColor.YELLOW)
                                .decoration(TextDecoration.ITALIC, false)
                ))
                .set(LuckyBlockItemRegistry.LUCKY_BLOCK_ITEM_TAG, getId())
                .build();
    }

    @Override
    public boolean onUse(SkywarsPlayer holder) {
        int roll = RANDOM.nextInt(6) + 1;

        holder.sendMessage(Component.text("You rolled a ", NamedTextColor.GOLD)
                .append(Component.text(roll, NamedTextColor.YELLOW, TextDecoration.BOLD))
                .append(Component.text("!", NamedTextColor.GOLD)));

        switch (roll) {
            case 1 -> {
                holder.sendMessage(Component.text("UNLUCKY! You die instantly!", NamedTextColor.DARK_RED));
                holder.damage(Damage.fromEntity(null, 1000f));
            }
            case 2 -> {
                holder.sendMessage(Component.text("Your health is halved!", NamedTextColor.RED));
                float currentHealth = holder.getHealth();
                holder.setHealth(Math.max(1, currentHealth / 2));
            }
            case 3 -> {
                holder.sendMessage(Component.text("You gained Absorption II!", NamedTextColor.GOLD));
                holder.addEffect(new Potion(PotionEffect.ABSORPTION, (byte) 1, Integer.MAX_VALUE));
            }
            case 4 -> {
                holder.sendMessage(Component.text("You received a Diamond Sword with Sharpness I!", NamedTextColor.AQUA));
                ItemStack sword = ItemStack.builder(Material.DIAMOND_SWORD)
                        .set(DataComponents.ENCHANTMENTS, new EnchantmentList(Map.of(Enchantment.SHARPNESS, 1)))
                        .build();
                holder.getInventory().addItemStack(sword);
            }
            case 5 -> {
                holder.sendMessage(Component.text("You received a Diamond Sword with Fire Aspect II!", NamedTextColor.LIGHT_PURPLE));
                ItemStack sword = ItemStack.builder(Material.DIAMOND_SWORD)
                        .set(DataComponents.ENCHANTMENTS, new EnchantmentList(Map.of(Enchantment.FIRE_ASPECT, 2)))
                        .build();
                holder.getInventory().addItemStack(sword);
            }
            case 6 -> {
                holder.sendMessage(Component.text("JACKPOT! Full Diamond Armor!", NamedTextColor.GREEN));
                holder.setEquipment(EquipmentSlot.HELMET, ItemStack.of(Material.DIAMOND_HELMET));
                holder.setEquipment(EquipmentSlot.CHESTPLATE, ItemStack.of(Material.DIAMOND_CHESTPLATE));
                holder.setEquipment(EquipmentSlot.LEGGINGS, ItemStack.of(Material.DIAMOND_LEGGINGS));
                holder.setEquipment(EquipmentSlot.BOOTS, ItemStack.of(Material.DIAMOND_BOOTS));
            }
        }

        return true;
    }

    @Override
    public boolean hasUseEffect() {
        return true;
    }
}
