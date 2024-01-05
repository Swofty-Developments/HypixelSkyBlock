package net.swofty.collection;

import lombok.AllArgsConstructor;
import net.minestom.server.item.Material;
import net.swofty.item.ItemType;

public abstract class CollectionCategory {
    public abstract Material getDisplayIcon();
    public abstract String getName();

    public abstract ItemCollection[] getCollections();

    public record ItemCollection(ItemType type, ItemCollectionReward... rewards) {}

    public record ItemCollectionReward(int requirement) { }
}
