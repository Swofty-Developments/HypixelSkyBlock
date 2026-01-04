package net.swofty.type.skywarsgame.luckyblock.items.usables;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.coordinate.Point;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockConsumable;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockItem;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockItemRegistry;
import net.swofty.type.skywarsgame.user.SkywarsPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChestSpawn implements LuckyBlockConsumable {

    private static final Random RANDOM = new Random();

    @Override
    public String getId() {
        return "chest_spawn";
    }

    @Override
    public String getDisplayName() {
        return "Chest Spawn";
    }

    @Override
    public ItemStack createItemStack() {
        return ItemStack.builder(Material.CHEST)
                .customName(Component.text(getDisplayName(), NamedTextColor.GOLD)
                        .decoration(TextDecoration.ITALIC, false)
                        .decoration(TextDecoration.BOLD, true))
                .lore(List.of(
                        Component.text("Place a chest filled with", NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.text("2-4 random lucky block items!", NamedTextColor.GRAY)
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
        Point targetBlock = player.getTargetBlockPosition(50);
        if (targetBlock == null) {
            player.sendMessage(Component.text("No block in range!", NamedTextColor.RED));
            return;
        }

        Instance instance = player.getInstance();
        if (instance == null) return;

        Point chestPos = targetBlock.add(0, 1, 0);
        if (!instance.getBlock(chestPos).isAir()) {
            player.sendMessage(Component.text("Not enough space to place chest!", NamedTextColor.RED));
            return;
        }

        instance.setBlock(chestPos, Block.CHEST);

        List<LuckyBlockItem> allItems = new ArrayList<>(LuckyBlockItemRegistry.getAllItems());
        int itemCount = 2 + RANDOM.nextInt(3);

        Inventory chestInventory = new Inventory(InventoryType.CHEST_3_ROW, Component.text("Lucky Chest"));
        for (int i = 0; i < itemCount; i++) {
            LuckyBlockItem randomItem = allItems.get(RANDOM.nextInt(allItems.size()));
            int slot = RANDOM.nextInt(27);
            while (chestInventory.getItemStack(slot) != ItemStack.AIR) {
                slot = RANDOM.nextInt(27);
            }
            chestInventory.setItemStack(slot, randomItem.createItemStack());
        }

        player.openInventory(chestInventory);
        player.sendMessage(Component.text("A lucky chest appears!", NamedTextColor.GOLD));
    }
}
