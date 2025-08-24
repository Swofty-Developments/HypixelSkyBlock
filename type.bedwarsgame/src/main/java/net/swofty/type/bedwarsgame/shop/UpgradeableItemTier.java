package net.swofty.type.bedwarsgame.shop;

import lombok.Getter;
import net.minestom.server.item.Material;

@Getter
public class UpgradeableItemTier {
    private final String name;
    private final int price;
    private final Currency currency;
    private final Material material;

    public UpgradeableItemTier(String name, int price, Currency currency, Material material) {
        this.name = name;
        this.price = price;
        this.currency = currency;
        this.material = material;
    }
}

