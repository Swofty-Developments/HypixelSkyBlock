package net.swofty.type.bedwarsgame.shop;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.tag.Tag;
import net.swofty.type.bedwarsgame.game.Game;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.bedwarsgame.util.BedWarsInventoryManipulator;
import net.swofty.type.bedwarsgame.util.ColorUtil;

import java.util.Arrays;
import java.util.List;

@Getter
public abstract class TeamUpgrade {

	private final String key;
	private final String name;
	private final String description;
	private final ItemStack displayItem;
	private final List<TeamUpgradeTier> tiers;

	public TeamUpgrade(String key, String name, String description, ItemStack displayItem, List<TeamUpgradeTier> tiers) {
		this.key = key;
		this.name = name;
		this.description = description;
		this.displayItem = displayItem;
		this.tiers = tiers;
	}

	public int getCurrentLevel(Game game, String teamName) {
		return game.getTeamUpgradeLevel(teamName, key);
	}

	public TeamUpgradeTier getNextTier(Game game, String teamName) {
		int currentLevel = getCurrentLevel(game, teamName);
		if (currentLevel >= tiers.size()) {
			return null;
		}
		return tiers.get(currentLevel);
	}

	public boolean hasEnoughCurrency(Player player, TeamUpgradeTier tier) {
		return Arrays.stream(player.getInventory().getItemStacks())
				.filter(stack -> stack.material() == tier.getCurrency().getMaterial())
				.mapToInt(ItemStack::amount)
				.sum() >= tier.getPrice();
	}

	public void purchase(Game game, BedWarsPlayer player) {
		String teamName = player.getTeamName();
		if (teamName == null) {
			player.sendMessage("You are not on a team.");
			return;
		}

		TeamUpgradeTier nextTier = getNextTier(game, teamName);
		if (nextTier == null) {
			player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Your team has already maxed out this upgrade.</red>"));
			return;
		}

		if (!hasEnoughCurrency(player, nextTier)) {
			player.sendMessage(MiniMessage.miniMessage().deserialize("<red>You do not have enough " + nextTier.getCurrency().getName() + " to purchase this.</red>"));
			return;
		}

		BedWarsInventoryManipulator.removeItems(player, nextTier.getCurrency().getMaterial(), nextTier.getPrice());

		game.setTeamUpgradeLevel(teamName, key, nextTier.getLevel());
		applyEffect(game, teamName, nextTier.getLevel());

		game.getPlayers().stream()
				.filter(p -> teamName.equals(p.getTeamName()))
				.forEach(p -> {
					String tt = p.getTag(Tag.String("teamColor"));
					TextColor teamColor = ColorUtil.getTextColorByName(tt);
					if (teamColor == null) {
						teamColor = NamedTextColor.WHITE;
					}
					p.sendMessage(
							Component.text(player.getUsername()).color(teamColor)
									.append(Component.text(" purchased ").color(NamedTextColor.GREEN))
									.append(Component.text(name + " " + nextTier.getLevel()).color(NamedTextColor.GOLD))
					);
					p.setTag(Tag.Integer("upgrade_" + key), nextTier.getLevel());
				});
	}

	/**
	 * Apply the effect of the upgrade to the team.
	 * This can be an instant effect or a permanent change.
	 *
	 * @param game     The game instance.
	 * @param teamName The name of the team.
	 * @param level    The new level of the upgrade.
	 */
	public abstract void applyEffect(Game game, String teamName, int level);

	public ItemStack getDisplayItem() {
		return BedWarsInventoryManipulator.hideTooltip(displayItem);
	}

}

