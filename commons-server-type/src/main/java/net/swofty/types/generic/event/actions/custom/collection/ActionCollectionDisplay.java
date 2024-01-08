package net.swofty.types.generic.event.actions.custom.collection;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.Event;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.collection.CollectionCategories;
import net.swofty.types.generic.collection.CollectionCategory;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.StringUtility;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.custom.CollectionUpdateEvent;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@EventParameters(description = "Handles the displays when updating collections",
        node = EventNodes.CUSTOM,
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
        if (CollectionCategories.getCategory(event.getItemType()) == null) return;

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
