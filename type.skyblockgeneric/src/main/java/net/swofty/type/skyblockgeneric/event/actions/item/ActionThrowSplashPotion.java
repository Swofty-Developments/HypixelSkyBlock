package net.swofty.type.skyblockgeneric.event.actions.item;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.skyblock.item.attribute.attributes.ItemAttributePotionData;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.skyblockgeneric.entity.SplashPotionEntity;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

/**
 * Handles splash potion throwing when a player right-clicks with a splash potion.
 */
public class ActionThrowSplashPotion implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = true)
    public void run(PlayerUseItemEvent event) {
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
        ItemStack itemStack = event.getItemStack();

        // Only handle splash potions
        if (itemStack.material() != Material.SPLASH_POTION) {
            return;
        }

        // Cancel the default behavior
        event.setCancelled(true);

        // Check if this is a SkyBlock item with potion data
        if (!SkyBlockItem.isSkyBlockItem(itemStack)) {
            return;
        }

        SkyBlockItem skyBlockItem = new SkyBlockItem(itemStack);
        ItemAttributePotionData.PotionData potionData = skyBlockItem.getAttributeHandler().getPotionData();

        // If no potion data, don't throw
        if (potionData == null) {
            return;
        }

        // Don't throw base potions (water, awkward, thick, mundane)
        String effectType = potionData.getEffectType();
        if (effectType.equals("WATER") || effectType.equals("AWKWARD") ||
            effectType.equals("THICK") || effectType.equals("MUNDANE")) {
            return;
        }

        // Create and spawn the splash potion entity
        SplashPotionEntity potionEntity = new SplashPotionEntity(player, skyBlockItem);

        // Spawn slightly in front of the player
        Pos playerPos = player.getPosition();
        Pos spawnPos = playerPos.add(
                -Math.sin(Math.toRadians(playerPos.yaw())) * 0.5,
                1.5, // Eye height
                Math.cos(Math.toRadians(playerPos.yaw())) * 0.5
        );

        potionEntity.setInstance(player.getInstance(), spawnPos);

        // Remove the potion from the player's hand
        int heldSlot = player.getHeldSlot();
        ItemStack currentItem = player.getInventory().getItemStack(heldSlot);

        if (currentItem.amount() > 1) {
            player.getInventory().setItemStack(heldSlot, currentItem.withAmount(currentItem.amount() - 1));
        } else {
            player.getInventory().setItemStack(heldSlot, ItemStack.AIR);
        }
    }
}
