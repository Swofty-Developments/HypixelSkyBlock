package net.swofty.type.hub.gui.elizabeth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.swofty.types.generic.item.ItemTypeLinker;

@AllArgsConstructor
@Getter
public class CommunityShopItem {
    private ItemTypeLinker itemTypeLinker;
    private int price;
    private int amount;
}
