package net.swofty.type.skyblockgeneric.fishing;

import net.minestom.server.item.ItemStack;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.updater.PlayerItemUpdater;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

public final class FishingBaitService {
    private FishingBaitService() {
    }

    public static @Nullable BaitDefinition getFirstAvailableBait(SkyBlockPlayer player, FishingMedium medium) {
        for (int slot = 0; slot < 36; slot++) {
            SkyBlockItem item = new SkyBlockItem(player.getInventory().getItemStack(slot));
            ItemType type = item.getAttributeHandler().getPotentialType();
            if (type == null) {
                continue;
            }

            BaitDefinition bait = FishingItemCatalog.getBait(type.name());
            if (bait == null) {
                continue;
            }
            if (!bait.mediums().isEmpty() && !bait.mediums().contains(medium)) {
                continue;
            }
            return bait;
        }
        return null;
    }

    public static boolean consumeOneBait(SkyBlockPlayer player, String baitItemId) {
        for (int slot = 0; slot < 36; slot++) {
            SkyBlockItem item = new SkyBlockItem(player.getInventory().getItemStack(slot));
            if (item.getAttributeHandler().getPotentialType() == null) {
                continue;
            }
            if (!item.getAttributeHandler().getPotentialType().name().equals(baitItemId)) {
                continue;
            }

            if (item.getAmount() > 1) {
                item.setAmount(item.getAmount() - 1);
                player.getInventory().setItemStack(slot, PlayerItemUpdater.playerUpdate(player, item.getItemStack()).build());
            } else {
                player.getInventory().setItemStack(slot, ItemStack.AIR);
            }
            return true;
        }
        return false;
    }
}
