package net.swofty.type.skyblockgeneric.minion;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.components.MinionShippingComponent;
import net.swofty.type.skyblockgeneric.item.components.SellableComponent;
import net.swofty.type.skyblockgeneric.minion.extension.MinionExtensionData;
import net.swofty.type.skyblockgeneric.minion.extension.MinionExtensions;
import net.swofty.type.skyblockgeneric.minion.extension.extensions.MinionShippingExtension;

import java.util.List;

public abstract class MinionAction {

    public abstract @NonNull List<SkyBlockItem> onAction(MinionActionEvent event, IslandMinionData.IslandMinion minion, Instance island);
    public abstract boolean checkMaterials(IslandMinionData.IslandMinion minion, Instance island);

    @Getter
    @Setter
    public static class MinionActionEvent {
        private Pos toLook;
        private Runnable action;
    }

    public static void onMinionIteration(IslandMinionData.IslandMinion islandMinion,
                                             SkyBlockMinion minion,
                                             List<SkyBlockItem> items) {
        MinionExtensionData extensionData = islandMinion.getExtensionData();

        for (SkyBlockItem item : items) {
            boolean hasAdded = islandMinion.addItem(item);
            double sellAmount = !item.hasComponent(SellableComponent.class) ? 0 :
                    item.getComponent(SellableComponent.class).getSellValue();

            if (!hasAdded && sellAmount > 0) {
                MinionShippingExtension shippingExtension = (MinionShippingExtension)
                        extensionData.getOfType(MinionShippingExtension.class);
                if (shippingExtension.getItemTypePassedIn() == null)
                    return;

                SkyBlockItem shippingItem = new SkyBlockItem(shippingExtension.getItemTypePassedIn());
                double percentage = shippingItem.getComponent(MinionShippingComponent.class).getPercentageOfOriginalPrice();
                double sellValue = sellAmount * (percentage / 100);
                shippingExtension.addCoins(sellValue);

                extensionData.setData(MinionExtensions.SHIPPING_SLOT.getSlots()[0], shippingExtension);
            }
        }

        if (extensionData.hasMinionUpgrade(ItemType.DIAMOND_SPREADING)) {
            if (Math.random() < 0.1) {
                SkyBlockItem diamond = new SkyBlockItem(ItemType.DIAMOND);
                islandMinion.addItem(diamond);
            }
        }
    }
}
