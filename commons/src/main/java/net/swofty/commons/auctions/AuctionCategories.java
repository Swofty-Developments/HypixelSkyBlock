package net.swofty.commons.auctions;

import lombok.Getter;
import net.minestom.server.item.Material;

@Getter
public enum AuctionCategories {
    WEAPONS(Material.ORANGE_STAINED_GLASS_PANE),
    ARMOR(Material.BLUE_STAINED_GLASS_PANE),
    ACCESSORIES(Material.GREEN_STAINED_GLASS_PANE),
    CONSUMABLES(Material.RED_STAINED_GLASS_PANE),
    BLOCKS(Material.BROWN_STAINED_GLASS_PANE),
    TOOLS(Material.PURPLE_STAINED_GLASS_PANE),
    ;

    private final Material material;

    AuctionCategories(Material material) {
        this.material = material;
    }
}
