package net.swofty.types.generic.event.actions.custom.collection;

import net.kyori.adventure.text.Component;
import net.minestom.server.item.ItemStack;
import net.swofty.types.generic.collection.CollectionCategories;
import net.swofty.types.generic.collection.CollectionCategory;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.event.custom.CollectionUpdateEvent;
import net.swofty.types.generic.item.updater.NonPlayerItemUpdater;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.StringUtility;

import java.util.Arrays;

public class ActionCollectionDisplay implements SkyBlockEventClass {


    @SkyBlockEvent(node = EventNodes.CUSTOM , requireDataLoaded = true)
    public void run(CollectionUpdateEvent event) {

        if (event.getOldValue() == 0) {
            event.getPlayer().sendMessage(Component.text("  §6§lCOLLECTION UNLOCKED §e" + event.getItemType().getDisplayName(null))
                    .hoverEvent(Component.text("§eClick to view your " + event.getItemType().getDisplayName(null) + " Collection!"))
                    .clickEvent(net.kyori.adventure.text.event.ClickEvent.runCommand("/viewcollection " + event.getItemType().name()))
            );
            return;
        }
        if (CollectionCategories.getCategory(event.getItemType()) == null) return;

        CollectionCategory.ItemCollection collection = CollectionCategories.getCategory(event.getItemType()).getCollection(event.getItemType());
        CollectionCategory.ItemCollectionReward newReward = event.getPlayer().getCollection().getReward(collection);
        CollectionCategory.ItemCollectionReward oldReward = null;

        for (CollectionCategory.ItemCollectionReward reward : collection.rewards()) {
            if (event.getOldValue() <= reward.requirement()) {
                oldReward = reward;
                break;
            }
        }

        if (oldReward == newReward) return;

        if (oldReward != null) {
            SkyBlockPlayer player = event.getPlayer();

            player.sendMessage("§e§l▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
            int placement = collection.getPlacementOf(oldReward);

            if (placement == 0) {
                player.sendMessage(Component.text("  §6§lCOLLECTION LEVEL UP §e" + event.getItemType().getDisplayName(null) + " " +
                                StringUtility.getAsRomanNumeral(collection.getPlacementOf(newReward)))
                        .hoverEvent(Component.text("§eClick to view your " + event.getItemType().getDisplayName(null) + " Collection!"))
                        .clickEvent(net.kyori.adventure.text.event.ClickEvent.runCommand("/viewcollection " + event.getItemType().name()))
                );
            } else {
                player.sendMessage(Component.text("  §6§lCOLLECTION LEVEL UP §e" + event.getItemType().getDisplayName(null) + " §8" +
                                StringUtility.getAsRomanNumeral(collection.getPlacementOf(oldReward)) + ">§e" +
                                StringUtility.getAsRomanNumeral(collection.getPlacementOf(newReward)))
                        .hoverEvent(Component.text("§eClick to view your " + event.getItemType().getDisplayName(null) + " Collection!"))
                        .clickEvent(net.kyori.adventure.text.event.ClickEvent.runCommand("/viewcollection " + event.getItemType().name()))
                );
            }

            player.sendMessage(" ");

            if (oldReward.unlocks().length > 0) {
                player.sendMessage("  §a§lREWARDS");
                Arrays.stream(oldReward.unlocks()).forEach(unlock -> {
                    switch (unlock.type()) {
                        case RECIPE -> {
                            ItemStack.Builder item = ((CollectionCategory.UnlockRecipe) unlock).getRecipe().getResult().getItemStackBuilder();
                            item = new NonPlayerItemUpdater(item).getUpdatedItem();

                            player.sendMessage("    §7" + StringUtility.getTextFromComponent(item.build().getMeta().getDisplayName()) + " §7Recipes");
                        }
                        case XP -> {
                            player.sendMessage("    §8+§b" + ((CollectionCategory.UnlockXP) unlock).xp() + " SkyBlock XP");
                        }
                    }
                });
            }

            player.sendMessage("§e§l▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
        }
    }
}