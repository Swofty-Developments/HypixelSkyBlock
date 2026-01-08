package net.swofty.service.darkauction.loot;

import net.swofty.commons.skyblock.item.ItemType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DarkAuctionItemPool {
    private final List<WeightedItem> items = new ArrayList<>();
    private static final double DARK_PURPLE_DYE_FIRST_ROLL = 0.0025; // 0.25% chance
    private static final Random random = new Random();

    public DarkAuctionItemPool() {
        // Special Items (weight 16)
        items.add(new WeightedItem(ItemType.HYPERION, 16));
        items.add(new WeightedItem(ItemType.MIDAS_SWORD, 16));

        // Epic Pets (weight 16)
        items.add(new WeightedItem(ItemType.BEE_PET, 16));
        items.add(new WeightedItem(ItemType.PERFECT_AMBER_GEM, 16));
    }

    /**
     * Select an item and remove it from the pool (so it can't be selected again in same auction)
     */
    public ItemType selectAndRemove() {
        // First roll for Dark Purple Dye
        if (random.nextDouble() < DARK_PURPLE_DYE_FIRST_ROLL) {
            return ItemType.DARK_PURPLE_DYE;
        }

        // Weighted random selection
        double totalWeight = items.stream().mapToDouble(WeightedItem::weight).sum();
        double randomValue = random.nextDouble() * totalWeight;
        double cumulative = 0;

        for (int i = 0; i < items.size(); i++) {
            cumulative += items.get(i).weight();
            if (randomValue < cumulative) {
                return items.remove(i).type();
            }
        }

        // If pool is exhausted, return Midas Sword
        return ItemType.MIDAS_SWORD;
    }

    /**
     * Peek at what items are still available without removing
     */
    public List<ItemType> getAvailableItems() {
        return items.stream().map(WeightedItem::type).toList();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public record WeightedItem(ItemType type, double weight) {}
}
