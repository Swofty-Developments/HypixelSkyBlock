package net.swofty.type.bedwarsgame.shop;

import lombok.Getter;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.TitlePart;
import net.minestom.server.component.DataComponents;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.component.TooltipDisplay;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig;
import net.swofty.type.bedwarsgame.game.Game;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;

import java.util.Set;

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

	/**
	 * Triggers the trap for the specified team.
	 *
	 * @param game      the game instance
	 * @param teamName  the name of the team that owns the trap
	 * @param triggerer the player who triggered the trap
	 */
	public void trigger(Game game, BedWarsMapsConfig.TeamKey teamName, BedWarsPlayer triggerer) {
		game.getTeamManager().removeTeamTrap(teamName, getKey());

		game.getTeamManager().getPlayersOnTeam(teamName).forEach(player -> {
			player.sendTitlePart(TitlePart.TITLE, Component.text("TRAP TRIGGERED")
					.color(NamedTextColor.RED)
					.decoration(TextDecoration.BOLD, true));
			player.playSound(Sound.sound(Key.key("minecraft:entity.enderman.death"), Sound.Source.MASTER, 1.0f, 0.6f));
		});
		onTrigger(game, teamName, triggerer);
	}

	/**
	 * Abstract method to define the trap's behavior when triggered.
	 *
	 * @param game      the game instance
	 * @param teamName  the name of the team that owns the trap
	 * @param triggerer the player who triggered the trap
	 */
	public abstract void onTrigger(Game game, BedWarsMapsConfig.TeamKey teamName, BedWarsPlayer triggerer);

	/**
	 * Gets the price of the trap for the specified team.
	 *
	 * @param game     the game instance
	 * @param teamName the name of the team that owns the trap
	 * @return the price of the trap
	 */
	public int getPrice(Game game, BedWarsMapsConfig.TeamKey teamName) {
		return game.getTeamManager().getTeamTraps(teamName).size() + 1;
	}

	public ItemStack getDisplayItem() {
		return displayItem.with(DataComponents.TOOLTIP_DISPLAY, new TooltipDisplay(false, Set.of()));
	}
}

