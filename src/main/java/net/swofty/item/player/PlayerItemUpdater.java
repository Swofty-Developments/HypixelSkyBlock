package net.swofty.item.player;

import net.minestom.server.item.ItemHideFlag;
import net.minestom.server.item.ItemStack;
import net.minestom.server.tag.Tag;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.SkyBlock;
import net.swofty.item.ItemLore;
import net.swofty.item.ItemType;
import net.swofty.item.Rarity;
import net.swofty.item.SkyBlockItem;
import net.swofty.item.attribute.AttributeHandler;
import net.swofty.item.attribute.ItemAttribute;
import net.swofty.user.SkyBlockPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

public class PlayerItemUpdater {

    private static HashMap<Map.Entry<SkyBlockPlayer, PlayerItemOrigin>,
            ArrayList<Map.Entry<BiFunction<SkyBlockPlayer, SkyBlockItem, SkyBlockItem>, CompletableFuture<SkyBlockItem>>>> queuedUpdates = new HashMap<>();

    private BiFunction<SkyBlockPlayer, SkyBlockItem, SkyBlockItem> queuedUpdate;

    public PlayerItemUpdater(BiFunction<SkyBlockPlayer, SkyBlockItem, SkyBlockItem> queuedUpdate) {
        this.queuedUpdate = queuedUpdate;
    }

    public CompletableFuture<SkyBlockItem> queueUpdate(SkyBlockPlayer player, PlayerItemOrigin origin) {
        if (!queuedUpdates.containsKey(Map.entry(player, origin))) {
            queuedUpdates.put(Map.entry(player, origin), new ArrayList<>());
        }

        CompletableFuture<SkyBlockItem> future = new CompletableFuture<>();
        queuedUpdates.get(Map.entry(player, origin)).add(Map.entry(queuedUpdate, future));
        return future;
    }

    public static ItemStack playerUpdate(SkyBlockPlayer player, PlayerItemOrigin origin, ItemStack stack) {
        if (!stack.hasTag(Tag.String("item_type"))) {
            /**
             * Item is not SkyBlock item, so we just instance it here
             */
            SkyBlockItem item = new SkyBlockItem(stack.material());
            ItemStack toSet = item.getItemStack();

            for (ItemAttribute attribute : ItemAttribute.getPossibleAttributes()) {
                stack = stack.withTag(Tag.String(attribute.getKey()), attribute.saveIntoString());
            }
            return stack.withMeta(toSet.meta());
        }

        /**
         * Check for value updates
         */
        SkyBlockItem item = new SkyBlockItem(stack);
        if (queuedUpdates.containsKey(Map.entry(player, origin))) {
            for (Map.Entry<BiFunction<SkyBlockPlayer, SkyBlockItem, SkyBlockItem>, CompletableFuture<SkyBlockItem>> queuedUpdate : queuedUpdates.get(Map.entry(player, origin))) {
                item = queuedUpdate.getKey().apply(player, item);
                queuedUpdate.getValue().complete(item);
            }
        }
        queuedUpdates.remove(Map.entry(player, origin));
        stack = item.getItemStack().withAmount(stack.getAmount());

        /**
         * Update SkyBlock Item Instance
         */
        AttributeHandler handler = item.getAttributeHandler();

        // Update Rarity
        try {
            handler.setRarity(ItemType.valueOf(handler.getItemType()).rarity);
        } catch (IllegalArgumentException e) {
            handler.setRarity(Rarity.COMMON);
        }
        if (handler.isRecombobulated()) {
            handler.setRarity(handler.getRarity().upgrade());
        }

        /**
         * Update Lore
         */
        ItemLore lore = new ItemLore(stack);
        lore.updateLore();
        stack = lore.getStack();

        return stack.withAmount(stack.getAmount());
    }

    public static void updateLoop(Scheduler scheduler) {
        scheduler.submitTask(() -> {
            SkyBlock.getLoadedPlayers().forEach(player -> {
                Arrays.stream(PlayerItemOrigin.values()).forEach(origin -> {
                    if (!origin.shouldBeLooped()) return;

                    ItemStack item = origin.getStack(player);
                    if (item == null) return;

                    origin.setStack(player, playerUpdate(player, origin, item));
                });
            });
            return TaskSchedule.tick(1);
        });
    }
}
