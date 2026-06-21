package net.swofty.type.bedwarsgame.shop;

import lombok.Getter;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.Nullable;

@Getter
public enum Currency {
    IRON("Iron", Material.IRON_INGOT, "§f"),
    GOLD("Gold", Material.GOLD_INGOT, "§6"),
    DIAMOND("Diamond", Material.DIAMOND, "§b"),
    EMERALD("Emerald", Material.EMERALD, "§2");

    private final String name;
	private final Material material;
	private final String color;

    Currency(String name, Material material, String color) {
        this.name = name;
		this.material = material;
		this.color = color;
    }

    @Nullable
    public static Currency byMaterial(Material material) {
        for (Currency currency : values()) {
            if (currency.getMaterial() == material) {
                return currency;
            }
        }
        return null;
    }

}

