package net.swofty.type.bedwarsgame.shop;

import lombok.Getter;
import net.swofty.type.bedwarsgame.shop.upgrades.*;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TeamShopManager {

	@Getter
	private final List<TeamUpgrade> upgrades = new ArrayList<>();

	public TeamShopManager() {
		upgrades.add(new SharpenedSwordUpgrade());
		upgrades.add(new ReinforcedArmorUpgrade());
		upgrades.add(new ManiacMinerUpgrade());
		upgrades.add(new HealPoolUpgrade());
		upgrades.add(new ForgeUpgrade());
		upgrades.add(new CushionedBootsUpgrade());
	}

	@Nullable
	public TeamUpgrade getUpgrade(String key) {
		return upgrades.stream().filter(u -> u.getKey().equals(key)).findFirst().orElse(null);
	}
}
