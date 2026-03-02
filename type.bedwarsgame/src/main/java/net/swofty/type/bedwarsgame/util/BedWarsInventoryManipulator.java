package net.swofty.type.bedwarsgame.util;

import net.minestom.server.entity.Player;
import net.minestom.server.inventory.PlayerInventory;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.generic.data.datapoints.DatapointBedWarsHotbar;
import net.swofty.type.generic.data.handlers.BedWarsDataHandler;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.IntStream;

public class BedWarsInventoryManipulator {

    public static void addItemWithHotbarPriority(BedWarsPlayer player,
                                                 ItemStack item,
                                                 @Nullable DatapointBedWarsHotbar.HotbarItemType hotbarItemType) {
        if (item == null || item.isAir()) {
            return;
        }

        if (hotbarItemType == null) {
            player.getInventory().addItemStack(item);
            return;
        }

        PlayerInventory inventory = player.getInventory();
        int maxStackSize = item.material().maxStackSize();
        int remaining = item.amount();

        int[] preferredSlots = getPreferredHotbarSlots(player, hotbarItemType);

        for (int slot : preferredSlots) {
            if (remaining <= 0) {
                break;
            }

            ItemStack existing = inventory.getItemStack(slot);
            if (existing.isAir()) {
                int placed = Math.min(remaining, maxStackSize);
                inventory.setItemStack(slot, item.withAmount(placed));
                remaining -= placed;
                continue;
            }

            if (existing.isSimilar(item) && existing.amount() < maxStackSize) {
                int add = Math.min(remaining, maxStackSize - existing.amount());
                inventory.setItemStack(slot, existing.withAmount(existing.amount() + add));
                remaining -= add;
                continue;
            }

            if (!existing.isSimilar(item) && moveToUpperInventory(player, existing)) {
                int placed = Math.min(remaining, maxStackSize);
                inventory.setItemStack(slot, item.withAmount(placed));
                remaining -= placed;
            }
        }

        if (remaining > 0) {
            player.getInventory().addItemStack(item.withAmount(remaining));
        }
    }

    private static int[] getPreferredHotbarSlots(BedWarsPlayer player, DatapointBedWarsHotbar.HotbarItemType hotbarItemType) {
        Map<Byte, DatapointBedWarsHotbar.HotbarItemType> layout = player.getBedWarsDataHandler()
            .get(BedWarsDataHandler.Data.HOTBAR_LAYOUT, DatapointBedWarsHotbar.class)
            .getValue();

        return IntStream.range(0, 9)
            .filter(slot -> layout.get((byte) slot) == hotbarItemType)
            .toArray();
    }

    private static boolean moveToUpperInventory(BedWarsPlayer player, ItemStack stack) {
        PlayerInventory inventory = player.getInventory();
        int maxStackSize = stack.material().maxStackSize();
        int remaining = stack.amount();

        for (int slot = 9; slot <= 35 && remaining > 0; slot++) {
            ItemStack current = inventory.getItemStack(slot);
            if (current.isAir()) {
                continue;
            }

            if (current.isSimilar(stack) && current.amount() < maxStackSize) {
                int add = Math.min(remaining, maxStackSize - current.amount());
                inventory.setItemStack(slot, current.withAmount(current.amount() + add));
                remaining -= add;
            }
        }

        for (int slot = 9; slot <= 35 && remaining > 0; slot++) {
            ItemStack current = inventory.getItemStack(slot);
            if (!current.isAir()) {
                continue;
            }

            int placed = Math.min(remaining, maxStackSize);
            inventory.setItemStack(slot, stack.withAmount(placed));
            remaining -= placed;
        }

        return remaining == 0;
    }

    public static void removeItems(Player player, Material material, int amount) {
        PlayerInventory inventory = player.getInventory();
        ItemStack[] slots = inventory.getItemStacks();
        int remaining = amount;

        for (int i = 0; i < slots.length && remaining > 0; i++) {
            var stack = slots[i];
            if (stack.material() == material) {
                int remove = Math.min(stack.amount(), remaining);
                int updated = stack.amount() - remove;
                inventory.setItemStack(i,
                    updated > 0 ? stack.withAmount(updated) : ItemStack.AIR);
                remaining -= remove;
            }
        }
    }

    public static boolean hasEnoughMaterial(Player player, Material material, int amount) {
        return Arrays.stream(player.getInventory().getItemStacks())
            .filter(stack -> stack.material() == material)
            .mapToInt(ItemStack::amount)
            .sum() >= amount;
    }

    public static boolean canBeChested(Material m) {
        if (m.name().endsWith("_axe") ||
            m.name().endsWith("_sword") ||
            m.name().endsWith("_pickaxe") ||
            m.name().endsWith("_shovel") ||
            m.name().endsWith("_hoe")) {
            return false;
        }

        return !m.isArmor();
    }

}
