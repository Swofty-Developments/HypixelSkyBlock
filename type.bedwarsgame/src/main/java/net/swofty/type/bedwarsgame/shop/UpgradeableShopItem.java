package net.swofty.type.bedwarsgame.shop;

import lombok.Getter;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;

import java.util.List;

public abstract class UpgradeableShopItem extends ShopItem {

    @Getter
    private final List<UpgradeableItemTier> tiers;
    private final Tag<Integer> upgradeTag;

    public UpgradeableShopItem(String name, String description, List<UpgradeableItemTier> tiers, Tag<Integer> upgradeTag) {
        // The initial price and other details are taken from the first tier.
        super(name, description, tiers.get(0).getPrice(), 1, tiers.get(0).getCurrency(), tiers.get(0).getMaterial());
        this.tiers = tiers;
        this.upgradeTag = upgradeTag;
    }

    public UpgradeableItemTier getTier(int level) {
        if (level >= 0 && level < tiers.size()) {
            return tiers.get(level);
        }
        return tiers.get(tiers.size() - 1); // Return max tier if level is out of bounds
    }

    public int getNextLevel(Player player) {
        return player.getTag(upgradeTag) == null ? 0 : player.getTag(upgradeTag);
    }

    public UpgradeableItemTier getNextTier(Player player) {
        return getTier(getNextLevel(player));
    }

    @Override
    public int getPrice() {
        // This is not really used for upgradeable items as price is dynamic.
        return 0;
    }

    @Override
    public void onBought(Player player) {
        int levelToGive = getNextLevel(player);
        UpgradeableItemTier tier = getTier(levelToGive);

        // Remove existing item of the same type
        for (int i = 0; i < player.getInventory().getSize(); i++) {
            ItemStack stack = player.getInventory().getItemStack(i);
            if (isSameType(stack.material())) {
                player.getInventory().setItemStack(i, ItemStack.AIR);
            }
        }

        player.getInventory().addItemStack(ItemStack.of(tier.getMaterial()));
        player.setTag(upgradeTag, levelToGive + 1);
    }

    @Override
    public void handlePurchase(Player player) {
        int nextLevel = getNextLevel(player);
        if (nextLevel >= tiers.size()) {
            return; // Already maxed out
        }
        UpgradeableItemTier tier = getTier(nextLevel);

        // Deduct currency
        var inventory = player.getInventory();
        var slots = inventory.getItemStacks();
        var material = tier.getCurrency().getMaterial();
        int remaining = tier.getPrice();

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

        // Call onBought to give the item and update the tag
        onBought(player);
    }

    public abstract boolean isSameType(Material material);
}
