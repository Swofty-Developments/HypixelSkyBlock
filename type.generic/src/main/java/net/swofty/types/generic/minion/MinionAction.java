package net.swofty.types.generic.minion;

import lombok.Getter;
import lombok.Setter;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.MinionShippingItem;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.minion.extension.MinionExtensionData;
import net.swofty.types.generic.minion.extension.MinionExtensions;
import net.swofty.types.generic.minion.extension.extensions.MinionShippingExtension;

public abstract class MinionAction {

    public abstract SkyBlockItem onAction(MinionActionEvent event, IslandMinionData.IslandMinion minion, Instance island);
    public abstract boolean checkMaterials(IslandMinionData.IslandMinion minion, Instance island);

    @Getter
    @Setter
    public static class MinionActionEvent {
        private Pos toLook;
        private Runnable action;
    }

    public static void onMinionIteration(IslandMinionData.IslandMinion islandMinion,
                                             SkyBlockMinion minion,
                                             SkyBlockItem item) {
        boolean hasAdded = islandMinion.addItem(item);
        MinionExtensionData extensionData = islandMinion.getExtensionData();
        double sellAmount = item.getGenericInstance() == null ? 0 :
                (item.getGenericInstance() instanceof Sellable
                        ? ((Sellable) item.getGenericInstance()).getSellValue() : 0);

        if (extensionData.hasMinionUpgrade(ItemType.DIAMOND_SPREADING)) {
            if (Math.random() < 0.1) {
                SkyBlockItem diamond = new SkyBlockItem(ItemType.DIAMOND);
                islandMinion.addItem(diamond);
            }
        }

        if (!hasAdded && sellAmount > 0) {
            MinionShippingExtension shippingExtension = (MinionShippingExtension)
                    extensionData.getOfType(MinionShippingExtension.class);
            if (shippingExtension.getItemTypePassedIn() == null)
                return;

            SkyBlockItem shippingItem = new SkyBlockItem(shippingExtension.getItemTypePassedIn());
            double percentage = ((MinionShippingItem) shippingItem.getGenericInstance()).getPercentageOfOriginalPrice();
            double sellValue = sellAmount * (percentage / 100);
            shippingExtension.addCoins(sellValue);

            extensionData.setData(MinionExtensions.SHIPPING_SLOT.getSlots()[0], shippingExtension);
        }
    }
}
