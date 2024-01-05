package net.swofty.collection;

import net.minestom.server.item.Material;
import net.swofty.item.ItemType;

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

    public record ItemCollectionReward(int requirement) {
    }
}
