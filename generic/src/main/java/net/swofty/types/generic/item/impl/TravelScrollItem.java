package net.swofty.types.generic.item.impl;

import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointRank;
import net.swofty.types.generic.data.datapoints.DatapointStringList;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import net.swofty.types.generic.utility.StringUtility;
import net.swofty.types.generic.warps.TravelScrollIslands;
import net.swofty.types.generic.warps.TravelScrollType;

import java.util.ArrayList;
import java.util.List;

public interface TravelScrollItem extends CustomSkyBlockItem, ExtraRarityDisplay, Interactable, TrackedUniqueItem {

    @Override
    default String getExtraRarityDisplay() {
        return " TRAVEL SCROLL";
    }

    @Override
    default ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    default ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        ArrayList<String> lore = new ArrayList<>(List.of(
                "§7Consume this item to add its",
                "§7destination to your fast travel",
                "§7options.",
                ""
        ));

        Rank requiredRank = getTravelScrollType().getRequiredRank();
        if (!requiredRank.equals(Rank.DEFAULT)) {
            lore.add("§7Requires §b" + requiredRank.getPrefix() + "§7to consume!");
            lore.add("");
        }

        TravelScrollIslands island = TravelScrollIslands.getFromTravelScroll(getTravelScrollType());

        lore.add("§7Island: §a" + StringUtility.toNormalCase(island.getInternalName()));
        lore.add("§7Teleport: §e" + StringUtility.toNormalCase(getTravelScrollType().getInternalName()));

        return lore;
    }

    TravelScrollType getTravelScrollType();

    @Override
    default void onRightInteract(SkyBlockPlayer player, SkyBlockItem item) {
        List<String> scrolls = player.getDataHandler().get(DataHandler.Data.USED_SCROLLS, DatapointStringList.class).getValue();

        if (scrolls.contains(getTravelScrollType().getInternalName())) {
            player.sendMessage("§cYou have already unlocked this travel scroll!");
            return;
        }

        Rank requiredRank = getTravelScrollType().getRequiredRank();
        if (!player.getDataHandler().get(DataHandler.Data.RANK, DatapointRank.class).getValue().isEqualOrHigherThan(requiredRank)) {
            player.sendMessage("§cYou must be at least " + requiredRank.getPrefix() + "§c to unlock this travel scroll!");
            return;
        }

        player.takeItem(item.getAttributeHandler().getItemTypeAsType(), 1);

        scrolls.add(getTravelScrollType().getInternalName());
        player.getDataHandler().get(DataHandler.Data.USED_SCROLLS, DatapointStringList.class).setValue(scrolls);
        player.sendMessage("§aYou have unlocked the " + getTravelScrollType().getInternalName() + " §atravel scroll!");
    }

    @Override
    default void onLeftInteract(SkyBlockPlayer player, SkyBlockItem item) {
        List<String> scrolls = player.getDataHandler().get(DataHandler.Data.USED_SCROLLS, DatapointStringList.class).getValue();

        if (scrolls.contains(getTravelScrollType().getInternalName())) {
            player.sendMessage("§cYou have already unlocked this travel scroll!");
            return;
        }

        Rank requiredRank = getTravelScrollType().getRequiredRank();
        if (!player.getDataHandler().get(DataHandler.Data.RANK, DatapointRank.class).getValue().isEqualOrHigherThan(requiredRank)) {
            player.sendMessage("§cYou must be at least " + requiredRank.getPrefix() + "§c to unlock this travel scroll!");
            return;
        }

        player.takeItem(item.getAttributeHandler().getItemTypeAsType(), 1);

        scrolls.add(getTravelScrollType().getInternalName());
        player.getDataHandler().get(DataHandler.Data.USED_SCROLLS, DatapointStringList.class).setValue(scrolls);
        player.sendMessage("§aYou have unlocked the " + getTravelScrollType().getInternalName() + " §atravel scroll!");
    }
}
