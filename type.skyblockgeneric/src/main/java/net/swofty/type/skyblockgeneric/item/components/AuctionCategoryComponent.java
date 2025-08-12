package net.swofty.type.skyblockgeneric.item.components;

import lombok.Getter;
import net.swofty.commons.auctions.AuctionCategories;
import net.swofty.type.generic.item.SkyBlockItemComponent;

public class AuctionCategoryComponent extends SkyBlockItemComponent {
    @Getter
    private final AuctionCategories category;

    public AuctionCategoryComponent(String category) {
        this.category = AuctionCategories.valueOf(category);
    }
}