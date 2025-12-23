package net.swofty.type.skyblockgeneric.event.actions.custom.collection;

import net.kyori.adventure.text.Component;
import net.minestom.server.component.DataComponents;
import net.minestom.server.item.ItemStack;
import net.swofty.commons.StringUtility;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.skyblockgeneric.collection.CollectionCategories;
import net.swofty.type.skyblockgeneric.collection.CollectionCategory;
import net.swofty.type.skyblockgeneric.event.custom.CollectionUpdateEvent;
import net.swofty.type.skyblockgeneric.item.updater.NonPlayerItemUpdater;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import org.tinylog.Logger;

import java.util.Arrays;

public class ActionCollectionDisplay implements HypixelEventClass {


    @HypixelEvent(node = EventNodes.CUSTOM, requireDataLoaded = true)
    public void run(CollectionUpdateEvent event) {

        if (event.getOldValue() == 0 && CollectionCategories.getCategory(event.getItemType()) != null) {
            event.getPlayer().sendMessage(Component.text("  §6§lCOLLECTION UNLOCKED §e" + event.getItemType().getDisplayName())
                    .hoverEvent(Component.text("§eClick to view your " + event.getItemType().getDisplayName() + " Collection!"))
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

        if (oldReward != null && newReward != null) {
            SkyBlockPlayer player = event.getPlayer();

            player.sendMessage("§e§l▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
            int placement = collection.getPlacementOf(oldReward);

            if (placement == 0) {
                player.sendMessage(Component.text("  §6§lCOLLECTION LEVEL UP §e" + event.getItemType().getDisplayName() + " " +
                                StringUtility.getAsRomanNumeral(collection.getPlacementOf(newReward)))
                        .hoverEvent(Component.text("§eClick to view your " + event.getItemType().getDisplayName() + " Collection!"))
                        .clickEvent(net.kyori.adventure.text.event.ClickEvent.runCommand("/viewcollection " + event.getItemType().name()))
                );
            } else {
                player.sendMessage(Component.text("  §6§lCOLLECTION LEVEL UP §e" + event.getItemType().getDisplayName() + " §8" +
                                StringUtility.getAsRomanNumeral(collection.getPlacementOf(oldReward)) + "➜§e" +
                                StringUtility.getAsRomanNumeral(collection.getPlacementOf(newReward)))
                        .hoverEvent(Component.text("§eClick to view your " + event.getItemType().getDisplayName() + " Collection!"))
                        .clickEvent(net.kyori.adventure.text.event.ClickEvent.runCommand("/viewcollection " + event.getItemType().name()))
                );
            }

            player.sendMessage(" ");

            if (oldReward.unlocks().length > 0) {
                player.sendMessage("  §a§lREWARDS");
                Arrays.stream(oldReward.unlocks()).forEach(unlock -> {
                    switch (unlock.type()) {
                        case RECIPE -> {
                            CollectionCategory.UnlockRecipe recipeUnlock = (CollectionCategory.UnlockRecipe) unlock;
                            if (recipeUnlock.getRecipe() == null) {
                                Logger.error("We have a null recipe in collection unlocks for " + event.getItemType().name() + " in " + event.getPlayer().getCollection().get(event.getItemType()));
                                return;
                            }
                            ItemStack.Builder item = ((CollectionCategory.UnlockRecipe) unlock).getRecipe().getResult().getItemStackBuilder();
                            item = new NonPlayerItemUpdater(item).getUpdatedItem();

                            player.sendMessage("    §7" + StringUtility.getTextFromComponent(item.build().get(DataComponents.CUSTOM_NAME)) + " §7Recipes");
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