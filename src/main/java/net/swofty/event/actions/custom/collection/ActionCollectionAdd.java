package net.swofty.event.actions.custom.collection;

import net.minestom.server.event.Event;
import net.minestom.server.item.Material;
import net.swofty.collection.CollectionCategories;
import net.swofty.collection.CollectionCategory;
import net.swofty.event.EventNodes;
import net.swofty.event.EventParameters;
import net.swofty.event.SkyBlockEvent;
import net.swofty.event.custom.CollectionUpdateEvent;
import net.swofty.event.custom.CustomBlockBreakEvent;
import net.swofty.item.ItemType;
import net.swofty.user.SkyBlockPlayer;
import net.swofty.user.statistics.StatisticDisplayReplacement;

@EventParameters(description = "Handles adding blocks to the users collection",
        node = EventNodes.CUSTOM,
        validLocations = EventParameters.Location.HUB,
        requireDataLoaded = true)
public class ActionCollectionAdd extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return CustomBlockBreakEvent.class;
    }

    @Override
    public void run(Event tempEvent) {
        CustomBlockBreakEvent event = (CustomBlockBreakEvent) tempEvent;
        SkyBlockPlayer player = event.getPlayer();
        Material material = Material.fromNamespaceId(event.getBlock().namespace());
        ItemType type = ItemType.fromMaterial(material);

        if (type == null) return;
        int oldValue = player.getCollection().get(type);

        player.getCollection().increase(type);

        SkyBlockEvent.callSkyBlockEvent(new CollectionUpdateEvent(player, type, oldValue));

        CollectionCategory category = CollectionCategories.getCategory(type);
        CollectionCategory.ItemCollection collection = category.getCollection(type);
        CollectionCategory.ItemCollectionReward reward = player.getCollection().getReward(collection);

        if (player.getDefenseDisplayReplacement() != null) {
            String addedAmountString = player.getDefenseDisplayReplacement().getDisplay();
            int addedAmount = 1;

            try {
                addedAmount = Integer.parseInt(addedAmountString.substring(2, addedAmountString.indexOf(" "))) + 1;
            } catch (NumberFormatException ignored) {
            }

            player.setDisplayReplacement(StatisticDisplayReplacement.builder()
                    .ticksToLast(20)
                    .display(
                            "§2+" + addedAmount + " " + category.getName() +
                                    " §7(" + player.getCollection().get(type) +
                                    "/" +
                                    player.getCollection().getReward(collection).requirement() + ")")
                    .build(), StatisticDisplayReplacement.DisplayType.DEFENSE);
        } else {
            player.setDisplayReplacement(StatisticDisplayReplacement.builder()
                    .ticksToLast(20)
                    .display(
                            "§2+1 " + category.getName() +
                                    " §7(" + player.getCollection().get(type) +
                                    "/" +
                                    player.getCollection().getReward(collection).requirement() + ")")
                    .build(), StatisticDisplayReplacement.DisplayType.DEFENSE);
        }
    }
}
