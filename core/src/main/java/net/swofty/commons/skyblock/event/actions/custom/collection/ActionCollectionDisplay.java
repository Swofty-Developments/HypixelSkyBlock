package net.swofty.commons.skyblock.event.actions.custom.collection;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.Event;
import net.swofty.commons.skyblock.collection.CollectionCategories;
import net.swofty.commons.skyblock.collection.CollectionCategory;
import net.swofty.commons.skyblock.event.EventNodes;
import net.swofty.commons.skyblock.event.EventParameters;
import net.swofty.commons.skyblock.event.SkyBlockEvent;
import net.swofty.commons.skyblock.event.custom.CollectionUpdateEvent;
import net.swofty.commons.skyblock.user.SkyBlockPlayer;
import net.swofty.commons.skyblock.utility.StringUtility;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@EventParameters(description = "Handles the displays when updating collections",
        node = EventNodes.CUSTOM,
        validLocations = EventParameters.Location.HUB,
        requireDataLoaded = true)
public class ActionCollectionDisplay extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return CollectionUpdateEvent.class;
    }

    @Override
    public void run(Event tempEvent) {
        CollectionUpdateEvent event = (CollectionUpdateEvent) tempEvent;

        if (event.getOldValue() == 0) {
            event.getPlayer().sendMessage(Component.text("  §6§lCOLLECTION UNLOCKED §e" + event.getItemType().getDisplayName())
                    .hoverEvent(Component.text("§eClick to view your " + event.getItemType().getDisplayName() + " Collection!"))
                    .clickEvent(net.kyori.adventure.text.event.ClickEvent.runCommand("/viewcollection " + event.getItemType().name()))
            );
            return;
        }

        CollectionCategory.ItemCollection collection = CollectionCategories.getCategory(event.getItemType()).getCollection(event.getItemType());
        List<CollectionCategory.ItemCollectionReward> rewards = Arrays.asList(collection.rewards());
        Collections.reverse(rewards);

        CollectionCategory.ItemCollectionReward oldReward = null;

        for (CollectionCategory.ItemCollectionReward reward : rewards) {
            if (event.getOldValue() <= reward.requirement()) {
                oldReward = reward;
            }
        }

        CollectionCategory.ItemCollectionReward newReward = event.getPlayer().getCollection().getReward(collection);

        if (oldReward == newReward) return;

        if (oldReward != null) {
            SkyBlockPlayer player = event.getPlayer();

            player.sendMessage("§e§l▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
            player.sendMessage(" ");

            player.sendMessage(Component.text("  §6§lCOLLECTION LEVEL UP §e" + event.getItemType().getDisplayName() + " " +
                            StringUtility.getAsRomanNumeral(collection.getPlacementOf(newReward)))
                    .hoverEvent(Component.text("§eClick to view your " + event.getItemType().getDisplayName() + " Collection!"))
                    .clickEvent(net.kyori.adventure.text.event.ClickEvent.runCommand("/viewcollection " + event.getItemType().name()))
            );

            player.sendMessage(" ");
            player.sendMessage("§e§l▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
        }
    }
}
