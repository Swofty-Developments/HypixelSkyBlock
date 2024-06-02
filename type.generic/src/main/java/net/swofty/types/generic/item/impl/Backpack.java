package net.swofty.types.generic.item.impl;

import com.mongodb.lang.Nullable;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.List;

public interface Backpack extends CustomSkyBlockItem, SkullHead, Interactable, TrackedUniqueItem {
    int getRows();

    default ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    default void onRightInteract(SkyBlockPlayer player, SkyBlockItem item) {
        player.sendMessage("§cBackpacks cannot be opened on their own.");
        player.sendMessage("§cInstead, use the §6Storage §cmenu in your §aSkyBlock Menu §cto store backpacks.");
    }

    default List<String> getLore(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return List.of(
                "§7A bag with §a" + (getRows() * 9) + " §7slots which can be",
                "§7placed in your Storage Menu to",
                "§7store additional items."
        );
    }
}
