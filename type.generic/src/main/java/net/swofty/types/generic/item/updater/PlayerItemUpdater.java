package net.swofty.types.generic.item.updater;

import net.minestom.server.color.Color;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.DyedItemColor;
import net.minestom.server.item.component.HeadProfile;
import net.minestom.server.tag.Tag;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;
import net.minestom.server.utils.Unit;
import net.swofty.types.generic.SkyBlockGenericLoader;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.item.ItemLore;
import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.commons.item.Rarity;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.ItemAttributeHandler;
import net.swofty.commons.item.attribute.attributes.ItemAttributeGemData;
import net.swofty.types.generic.item.impl.GemstoneItem;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.item.impl.TrackedUniqueItem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.json.JSONObject;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class PlayerItemUpdater {
    public static ItemStack.Builder playerUpdate(SkyBlockPlayer player, ItemStack stack) {
        return playerUpdateFull(player, stack, false).getValue();
    }

    public static ItemStack.Builder playerUpdate(SkyBlockPlayer player, ItemStack stack, boolean isOwnedByPlayer) {
        return playerUpdateFull(player, stack, isOwnedByPlayer).getValue();
    }

    public static Map.Entry<SkyBlockItem,
            ItemStack.Builder> playerUpdateFull(SkyBlockPlayer player, ItemStack stack, boolean isOwnedByPlayer) {
        if (stack.hasTag(Tag.Boolean("uneditable")) && stack.getTag(Tag.Boolean("uneditable")))
            return Map.entry(new SkyBlockItem(stack), ItemStackCreator.getFromStack(stack));

        if (!SkyBlockItem.isSkyBlockItem(stack) || stack.material().equals(Material.AIR)) {
            /**
             * Item is not SkyBlock item, so we just instance it here
             */
            SkyBlockItem item = new SkyBlockItem(stack.material());
            ItemStack.Builder itemAsBuilder = item.getItemStackBuilder();

            ItemLore lore = new ItemLore(stack);
            lore.updateLore(player);
            stack = lore.getStack();

            return Map.entry(item, itemAsBuilder
                            .set(ItemComponent.LORE, stack.get(ItemComponent.LORE))
                            .amount(stack.amount()));
        }

        /**
         * Check for value updates
         */
        SkyBlockItem item = new SkyBlockItem(stack);
        ItemStack.Builder toReturn = item.getItemStackBuilder().amount(stack.amount());

        /**
         * Update SkyBlock Item Instance
         */
        ItemAttributeHandler handler = item.getAttributeHandler();

        // Update Rarity
        try {
            handler.setRarity(ItemTypeLinker.valueOf(handler.getItemType()).rarity);
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
        lore.updateLore(player);
        stack = lore.getStack();

        if (handler.shouldBeEnchanted()) {
            toReturn.set(ItemComponent.ENCHANTMENT_GLINT_OVERRIDE, true);
            toReturn.set(ItemComponent.HIDE_ADDITIONAL_TOOLTIP, Unit.INSTANCE);
        }

        Color leatherColour = handler.getLeatherColour();
        if (leatherColour != null) {
            toReturn.set(ItemComponent.DYED_COLOR, new DyedItemColor(
                    new Color(leatherColour.red(), leatherColour.green(), leatherColour.blue()), false));
        }

        if (item.getGenericInstance() != null
                && item.getGenericInstance() instanceof TrackedUniqueItem
                && handler.getUniqueTrackedID() == null
                && isOwnedByPlayer) {
            UUID randomUUID = UUID.randomUUID();

            handler.setUniqueTrackedID(randomUUID.toString(), player);
            toReturn.set(Tag.String("unique-tracked-id"), randomUUID.toString());
        } else if (item.getGenericInstance() != null
                && item.getGenericInstance() instanceof TrackedUniqueItem
                && handler.getUniqueTrackedID() != null
                && isOwnedByPlayer) {
            handler.setUniqueTrackedID(handler.getUniqueTrackedID(), player);
        }

        if (item.getGenericInstance() != null
                && item.getGenericInstance() instanceof SkullHead skullHead) {
            JSONObject json = new JSONObject();
            json.put("isPublic", true);
            json.put("signatureRequired", false);
            json.put("textures", new JSONObject().put("SKIN", new JSONObject()
                    .put("url", "http://textures.minecraft.net/texture/" + skullHead.getSkullTexture(player, item))
                    .put("metadata", new JSONObject().put("model", "slim"))));

            String texturesEncoded = Base64.getEncoder().encodeToString(json.toString().getBytes());

            toReturn.set(ItemComponent.PROFILE, new HeadProfile(new PlayerSkin(texturesEncoded, null)));
        }

        if (item.getGenericInstance() != null &&
                item.getGenericInstance() instanceof GemstoneItem gemstoneItem) {
            int index = 0;
            ItemAttributeGemData.GemData gemData = item.getAttributeHandler().getGemData();
            for (GemstoneItem.GemstoneItemSlot slot : gemstoneItem.getGemstoneSlots()) {
                if (slot.unlockPrice == 0) {
                    // Slot should be unlocked by default
                    if (gemData.hasGem(index)) continue;
                    gemData.putGem(
                            new ItemAttributeGemData.GemData.GemSlots(
                                    index,
                                    null
                            )
                    );
                }
                index++;
            }
            item.getAttributeHandler().setGemData(gemData);
        }

        ItemStackCreator.clearAttributes(toReturn);
        return Map.entry(item,
                toReturn.amount(stack.amount())
                        .set(ItemComponent.CUSTOM_NAME, stack.get(ItemComponent.CUSTOM_NAME))
                        .set(ItemComponent.LORE, stack.get(ItemComponent.LORE)));
    }

    public static void updateLoop(Scheduler scheduler) {
        scheduler.submitTask(() -> {
            List<CompletableFuture<Void>> futures = new ArrayList<>();

            SkyBlockGenericLoader.getLoadedPlayers().forEach(player -> {
                futures.add(CompletableFuture.runAsync(() -> {
                    PlayerItemOrigin.OriginCache cache = PlayerItemOrigin.getFromCache(player.getUuid());

                    Arrays.stream(PlayerItemOrigin.values()).forEach(origin -> {
                        if (!origin.shouldBeLooped()) return;

                        ItemStack item = origin.getStack(player);
                        if (item == null || item.isAir()) {
                            cache.put(origin, new SkyBlockItem(Material.AIR));
                            return;
                        }

                        Map.Entry<SkyBlockItem, ItemStack.Builder> builder = playerUpdateFull(player, item, true);
                        cache.put(origin, builder.getKey());
                        origin.setStack(player, builder.getValue().build());
                    });

                    PlayerItemOrigin.setCache(player.getUuid(), cache);
                }));
            });

            // Wait for all futures to complete, do it async so they're at the same time
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

            return TaskSchedule.tick(10);
        });
    }
}