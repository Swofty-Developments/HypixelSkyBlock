package net.swofty.type.skyblockgeneric.item.components;

import lombok.Getter;
import net.swofty.type.generic.data.HypixelDataHandler;
import net.swofty.type.generic.data.datapoints.DatapointRank;
import net.swofty.type.generic.data.datapoints.DatapointStringList;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.SkyBlockItemComponent;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.generic.user.categories.Rank;
import net.swofty.type.skyblockgeneric.warps.TravelScrollType;

import java.util.ArrayList;
import java.util.List;

@Getter
public class TravelScrollComponent extends SkyBlockItemComponent {
    private final TravelScrollType travelScrollType;
    private final List<String> lore;

    public TravelScrollComponent(String scrollType) {
        this.travelScrollType = TravelScrollType.valueOf(scrollType);
        this.lore = new ArrayList<>(List.of(
                "§7Consume this item to add its",
                "§7destination to your fast travel",
                "§7options."
        ));
        addInheritedComponent(new LoreUpdateComponent(lore, false));
        addInheritedComponent(new ExtraRarityComponent("TRAVEL SCROLL"));
        addInheritedComponent(new InteractableComponent(this::onInteract, this::onInteract, null));
        addInheritedComponent(new TrackedUniqueComponent());
    }

    private void onInteract(SkyBlockPlayer player, SkyBlockItem item) {
        List<String> scrolls = player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.USED_SCROLLS, DatapointStringList.class).getValue();

        if (scrolls.contains(getTravelScrollType().getInternalName())) {
            player.sendMessage("§cYou have already unlocked this travel scroll!");
            return;
        }

        Rank requiredRank = getTravelScrollType().getRequiredRank();
        if (!player.getDataHandler().get(HypixelDataHandler.Data.RANK, DatapointRank.class).getValue().isEqualOrHigherThan(requiredRank)) {
            player.sendMessage("§cYou must be at least " + requiredRank.getPrefix() + "§c to unlock this travel scroll!");
            return;
        }

        player.takeItem(item.getAttributeHandler().getPotentialType(), 1);

        scrolls.add(getTravelScrollType().getInternalName());
        player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.USED_SCROLLS, DatapointStringList.class).setValue(scrolls);
        player.sendMessage("§aYou have unlocked the " + getTravelScrollType().getDisplayName() + " §atravel scroll!");

    }
}