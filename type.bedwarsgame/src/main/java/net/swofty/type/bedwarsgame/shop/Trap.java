package net.swofty.type.bedwarsgame.shop;

import lombok.Getter;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.TitlePart;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.swofty.type.bedwarsgame.game.Game;
import net.swofty.type.bedwarsgame.util.BedWarsInventoryManipulator;

@Getter
public abstract class Trap {
	private final String key;
	private final String name;
	private final ItemStack displayItem;
	private final String description;
	private final Currency currency;

	public Trap(String key, String name, ItemStack displayItem, String description, Currency currency) {
		this.key = key;
		this.name = name;
		this.displayItem = displayItem;
		this.description = description;
		this.currency = currency;
	}

	public void trigger(Game game, String teamName, Player triggerer) {
		game.removeTeamTrap(teamName, getKey());

		game.getPlayersOnTeam(teamName).forEach(player -> {
			player.sendTitlePart(TitlePart.TITLE, Component.text("TRAP TRIGGERED")
					.color(NamedTextColor.RED)
					.decoration(TextDecoration.BOLD, true));
			player.playSound(Sound.sound(Key.key("minecraft:entity.enderman.death"), Sound.Source.MASTER, 1.0f, 0.6f));
		});
		triggered(game, teamName, triggerer);
	}

	public abstract void triggered(Game game, String teamName, Player triggerer);

	public int getPrice(Game game, String teamName) {
		return game.getTeamTraps(teamName).size() + 1;
	}

	public ItemStack getDisplayItem() {
		return BedWarsInventoryManipulator.hideTooltip(displayItem);
	}
}

