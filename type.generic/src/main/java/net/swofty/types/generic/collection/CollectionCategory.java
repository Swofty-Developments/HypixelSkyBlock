package net.swofty.types.generic.collection;

import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;
import net.swofty.types.generic.item.updater.NonPlayerItemUpdater;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.StringUtility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
                if (rewards[i].requirement == reward.requirement) {
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
                        ((UnlockRecipe) unlock).getRecipes().forEach(recipe -> {
                            lore.add("§7  §e" + recipe.getResult().getDisplayName() + " §7Recipes");
                        });
                    }
                    case CUSTOM_AWARD -> {
                        lore.add("§7  " + ((UnlockCustomAward) unlock).getAward().getDisplay());
                    }
                }
            });
            Arrays.stream(unlocks).forEach(unlock -> {
                if (Objects.requireNonNull(unlock.type()) == Unlock.UnlockType.XP) {
                    lore.add("§7  §8+§b" + ((UnlockXP) unlock).xp() + " SkyBlock XP");
                }
            });

            return lore;
        }
    }

    public abstract static class Unlock {
        public abstract UnlockType type();
        public abstract ItemStack.Builder getDisplay(SkyBlockPlayer player);

        public enum UnlockType {
            RECIPE,
            XP,
            CUSTOM_AWARD
        }
    }

    public abstract static class UnlockRecipe extends Unlock {
        @Override
        public UnlockType type() {
            return UnlockType.RECIPE;
        }

        @Override
        public ItemStack.Builder getDisplay(SkyBlockPlayer player) {
            ItemStack.Builder updatedItem = new NonPlayerItemUpdater(getRecipes().getFirst().getResult()).getUpdatedItem();
            ArrayList<String> lore = new ArrayList<>(
                    updatedItem.build().getLore().stream().map(StringUtility::getTextFromComponent).toList()
            );
            lore.add(" ");
            int others = getRecipes().size() - 1;
            if (others > 0) {
                lore.add("§8+" + others + " more recipes");
            }
            lore.add("§eClick to view recipe");

            return ItemStackCreator.updateLore(updatedItem, lore);
        }

        public abstract SkyBlockRecipe<?> getRecipe();

        public List<SkyBlockRecipe<?>> getRecipes() {
            if (getRecipe() != null) {
                return List.of(getRecipe());
            }
            return List.of();
        }
    }

    public abstract static class UnlockXP extends Unlock {
        @Override
        public UnlockType type() {
            return UnlockType.XP;
        }

        @Override
        public ItemStack.Builder getDisplay(SkyBlockPlayer player) {
            return ItemStackCreator.getStack("§8+§b" + xp() + " SkyBlock XP", Material.EXPERIENCE_BOTTLE, 1);
        }

        public abstract int xp();
    }

    public abstract static class UnlockCustomAward extends Unlock {
        @Override
        public UnlockType type() {
            return UnlockType.CUSTOM_AWARD;
        }

        @Override
        public ItemStack.Builder getDisplay(SkyBlockPlayer player) {
            return ItemStackCreator.getStack(getAward().getDisplay(), Material.PURPLE_STAINED_GLASS_PANE, 1);
        }

        public abstract CustomCollectionAward getAward();
    }
}
