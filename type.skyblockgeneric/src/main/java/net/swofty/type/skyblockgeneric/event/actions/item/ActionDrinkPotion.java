package net.swofty.type.skyblockgeneric.event.actions.item;

import net.minestom.server.event.item.PlayerFinishItemUseEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.skyblock.item.attribute.attributes.ItemAttributePotionData;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.potion.PotionEffectService;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

/**
 * Handles potion consumption when a player finishes drinking a potion.
 * Uses PlayerFinishItemUseEvent which fires after the drinking animation completes.
 */
public class ActionDrinkPotion implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = true)
    public void run(PlayerFinishItemUseEvent event) {
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
        ItemStack itemStack = event.getItemStack();

        // Only handle potions (not splash or lingering)
        if (itemStack.material() != Material.POTION) {
            return;
        }

        // Check if this is a SkyBlock item with potion data
        if (!SkyBlockItem.isSkyBlockItem(itemStack)) {
            return;
        }

        SkyBlockItem skyBlockItem = new SkyBlockItem(itemStack);
        ItemAttributePotionData.PotionData potionData = skyBlockItem.getAttributeHandler().getPotionData();

        // If no potion data, it's a vanilla potion or water bottle - ignore
        if (potionData == null) {
            return;
        }

        // Don't process base potions (water, awkward, thick, mundane)
        String effectType = potionData.getEffectType();
        if (effectType.equals("WATER") || effectType.equals("AWKWARD") ||
            effectType.equals("THICK") || effectType.equals("MUNDANE")) {
            return;
        }

        // Apply the potion effect
        PotionEffectService.applyToPlayer(player, potionData);

        // Replace the potion with a glass bottle
        int heldSlot = player.getHeldSlot();
        ItemStack currentItem = player.getInventory().getItemStack(heldSlot);

        if (currentItem.amount() > 1) {
            // If stacked, reduce count and try to add glass bottle to inventory
            player.getInventory().setItemStack(heldSlot, currentItem.withAmount(currentItem.amount() - 1));
            // Try to add glass bottle
            if (!player.getInventory().addItemStack(ItemStack.of(Material.GLASS_BOTTLE))) {
                // Inventory full, drop it
                player.dropItem(ItemStack.of(Material.GLASS_BOTTLE));
            }
        } else {
            // Single potion, replace with glass bottle
            player.getInventory().setItemStack(heldSlot, ItemStack.of(Material.GLASS_BOTTLE));
        }
    }
}
