package net.swofty.item.updater;

import lombok.Getter;
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

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

public class PlayerItemUpdater {

    private static final QueuedUpdateManager updateManager = new QueuedUpdateManager();

    private final BiFunction<SkyBlockPlayer, SkyBlockItem, SkyBlockItem> queuedUpdate;

    public PlayerItemUpdater(BiFunction<SkyBlockPlayer, SkyBlockItem, SkyBlockItem> queuedUpdate) {
        this.queuedUpdate = queuedUpdate;
    }

    public CompletableFuture<SkyBlockItem> queueUpdate(SkyBlockPlayer player, PlayerItemOrigin origin) {
        CompletableFuture<SkyBlockItem> future = new CompletableFuture<>();
        updateManager.queueUpdate(player, origin, queuedUpdate, future);
        return future;
    }

    public static ItemStack playerUpdate(SkyBlockPlayer player, PlayerItemOrigin origin, ItemStack stack) {
        if (!SkyBlockItem.isSkyBlockItem(stack)) {
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
        ArrayList<QueuedUpdateManager.UpdatePair> updates = updateManager.getQueuedUpdates(player, origin);
        if (updates != null) {
            for (QueuedUpdateManager.UpdatePair updatePair : updates) {
                item = updatePair.getUpdateFunction().apply(player, item);
                updatePair.getFuture().complete(item);
            }
        }
        updateManager.clearQueue(player, origin);
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

    public static class QueuedUpdateManager {

        @Getter
        private static final class UpdatePair {
            private BiFunction<SkyBlockPlayer, SkyBlockItem, SkyBlockItem> updateFunction;
            private CompletableFuture<SkyBlockItem> future;

            public UpdatePair(BiFunction<SkyBlockPlayer, SkyBlockItem, SkyBlockItem> updateFunction, CompletableFuture<SkyBlockItem> future) {
                this.updateFunction = updateFunction;
                this.future = future;
            }

        }

        private final Map<Map.Entry<SkyBlockPlayer, PlayerItemOrigin>,
                ArrayList<UpdatePair>> queuedUpdates = new HashMap<>();

        public synchronized void queueUpdate(SkyBlockPlayer player, PlayerItemOrigin origin,
                                             BiFunction<SkyBlockPlayer, SkyBlockItem, SkyBlockItem> updateFunction,
                                             CompletableFuture<SkyBlockItem> future) {
            Map.Entry<SkyBlockPlayer, PlayerItemOrigin> key = new AbstractMap.SimpleEntry<>(player, origin);
            queuedUpdates.computeIfAbsent(key, k -> new ArrayList<>())
                    .add(new UpdatePair(updateFunction, future));
        }

        public synchronized ArrayList<UpdatePair> getQueuedUpdates(SkyBlockPlayer player, PlayerItemOrigin origin) {
            return queuedUpdates.get(new AbstractMap.SimpleEntry<>(player, origin));
        }

        public synchronized void clearQueue(SkyBlockPlayer player, PlayerItemOrigin origin) {
            queuedUpdates.remove(new AbstractMap.SimpleEntry<>(player, origin));
        }
    }
}