package net.swofty.type.hub.gui.elizabeth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.swofty.commons.item.ItemType;

@AllArgsConstructor
@Getter
public class CommunityShopItem {
    private ItemType itemTypeLinker;
    private int price;
    private int amount;
}
