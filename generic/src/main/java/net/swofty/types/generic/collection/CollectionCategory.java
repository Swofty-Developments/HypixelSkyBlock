package net.swofty.types.generic.collection;

import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.updater.NonPlayerItemUpdater;
import net.swofty.types.generic.utility.StringUtility;

import java.util.Arrays;
import java.util.List;

public abstract class CollectionCategory {
    public abstract Material getDisplayIcon();

    public abstract String getName();

    public abstract ItemCollection[] getCollections();

    public ItemCollection getCollection(ItemType type) {
        for (ItemCollection collection : getCollections()) {
            if (collection.type() == type) {
                return collection;
            }
        }
        return null;
    }

    public record ItemCollection(ItemType type, ItemCollectionReward... rewards) {
        public int getPlacementOf(ItemCollectionReward reward) {
            for (int i = 0; i < rewards.length; i++) {
                if (rewards[i].equals(reward)) {
                    return i;
                }
            }
            return -1;
        }
    }

    public record ItemCollectionReward(int requirement, Unlock... unlocks) {
        public List<String> getDisplay(List<String> lore, String itemDisplay) {
            lore.add("§7" + itemDisplay + "Rewards:");

            Arrays.stream(unlocks).forEach(unlock -> {
                switch (unlock.type()) {
                    case RECIPE -> {
                        ItemStack.Builder item = ((UnlockRecipe) unlock).getItem().getItemStackBuilder();
                        item = new NonPlayerItemUpdater(item).getUpdatedItem();

                        lore.add("§7  §e" + StringUtility.getTextFromComponent(item.build().getMeta().getDisplayName()) + " §7Recipes");
                    }
                    case XP -> {
                        lore.add("§7  §8+§b" + ((UnlockXP) unlock).xp() + " SkyBlock XP");
                    }
                }
            });

            return lore;
        }
    }

    public abstract static class Unlock {
        public abstract UnlockType type();

        public enum UnlockType {
            RECIPE,
            XP
        }
    }

    public abstract static class UnlockRecipe extends Unlock {
        @Override
        public UnlockType type() {
            return UnlockType.RECIPE;
        }

        public abstract SkyBlockItem getItem();
    }

    public abstract static class UnlockXP extends Unlock {
        @Override
        public UnlockType type() {
            return UnlockType.XP;
        }
        public abstract int xp();
    }
}
