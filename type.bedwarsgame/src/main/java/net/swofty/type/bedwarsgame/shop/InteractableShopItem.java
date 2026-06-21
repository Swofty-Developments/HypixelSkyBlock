package net.swofty.type.bedwarsgame.shop;

import net.swofty.type.bedwarsgame.item.SimpleInteractableItem;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;

public class InteractableShopItem extends ShopItem {
    private final SimpleInteractableItem item;

    public InteractableShopItem(SimpleInteractableItem item) {
        super(
            item.getId(),
            item.getShopData().name(),
            item.getShopData().description(),
            item.getShopData().price(),
            item.getShopData().amount(),
            item.getShopData().currency(),
            item.getBlandItem().material(),
            item.getShopData().hotbarItemType()
        );
        this.item = item;
    }

    @Override
    public void onPurchase(BedWarsPlayer player) {
        giveItem(player, item.getItemStack().withAmount(getAmount()));
    }
}
