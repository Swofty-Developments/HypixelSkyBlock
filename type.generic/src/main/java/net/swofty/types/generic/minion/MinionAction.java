package net.swofty.types.generic.minion;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.MinionShippingItem;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.minion.extension.MinionExtensionData;
import net.swofty.types.generic.minion.extension.MinionExtensions;
import net.swofty.types.generic.minion.extension.extensions.MinionShippingExtension;

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
            double sellAmount = item.getGenericInstance() == null ? 0 :
                    (item.getGenericInstance() instanceof Sellable
                            ? ((Sellable) item.getGenericInstance()).getSellValue() : 0);

            if (!hasAdded && sellAmount > 0) {
                MinionShippingExtension shippingExtension = (MinionShippingExtension)
                        extensionData.getOfType(MinionShippingExtension.class);
                if (shippingExtension.getItemTypeLinkerPassedIn() == null)
                    return;

                SkyBlockItem shippingItem = new SkyBlockItem(shippingExtension.getItemTypeLinkerPassedIn());
                double percentage = ((MinionShippingItem) shippingItem.getGenericInstance()).getPercentageOfOriginalPrice();
                double sellValue = sellAmount * (percentage / 100);
                shippingExtension.addCoins(sellValue);

                extensionData.setData(MinionExtensions.SHIPPING_SLOT.getSlots()[0], shippingExtension);
            }
        }

        if (extensionData.hasMinionUpgrade(ItemTypeLinker.DIAMOND_SPREADING)) {
            if (Math.random() < 0.1) {
                SkyBlockItem diamond = new SkyBlockItem(ItemTypeLinker.DIAMOND);
                islandMinion.addItem(diamond);
            }
        }
    }
}
