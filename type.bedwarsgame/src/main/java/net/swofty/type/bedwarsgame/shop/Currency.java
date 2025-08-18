package net.swofty.type.bedwarsgame.shop;

import lombok.Getter;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.item.Material;

@Getter
public enum Currency {
    IRON("Iron", Material.IRON_INGOT, NamedTextColor.WHITE),
    GOLD("Gold", Material.GOLD_INGOT, NamedTextColor.YELLOW),
    DIAMOND("Diamond", Material.DIAMOND, NamedTextColor.AQUA),
    EMERALD("Emerald", Material.EMERALD, NamedTextColor.GREEN);

    private final String name;
	private final Material material;
	private final TextColor color;

    Currency(String name, Material material, TextColor color) {
        this.name = name;
		this.material = material;
		this.color = color;
    }

}

