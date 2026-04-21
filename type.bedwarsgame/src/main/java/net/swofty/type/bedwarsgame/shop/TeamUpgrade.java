package net.swofty.type.bedwarsgame.shop;

import lombok.Getter;
import net.minestom.server.component.DataComponents;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.component.TooltipDisplay;
import net.minestom.server.tag.Tag;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig;
import net.swofty.type.bedwarsgame.game.v2.BedWarsGame;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.bedwarsgame.util.BedWarsInventoryManipulator;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

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

	public int getCurrentLevel(BedWarsGame game, BedWarsMapsConfig.TeamKey teamKey) {
		return game.getTeam(teamKey.name())
				.map(team -> team.getUpgradeLevel(key))
				.orElse(0);
	}

	public TeamUpgradeTier getNextTier(BedWarsGame game, BedWarsMapsConfig.TeamKey teamKey) {
		int currentLevel = getCurrentLevel(game, teamKey);
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

	public void purchase(BedWarsGame game, BedWarsPlayer player) {
		BedWarsMapsConfig.TeamKey teamKey = player.getTeamKey();
		if (teamKey == null) {
			player.sendMessage("§cYou are not on a team. Report this to the administration.");
			return;
		}

		TeamUpgradeTier nextTier = getNextTier(game, teamKey);
		if (nextTier == null) {
			player.sendMessage("§cYour team has already maxed out this upgrade.");
			return;
		}

		if (!hasEnoughCurrency(player, nextTier)) {
			player.sendMessage("§cYou do not have enough " + nextTier.getCurrency().getName() + " to purchase this.");
			return;
		}

		BedWarsInventoryManipulator.removeItems(player, nextTier.getCurrency().getMaterial(), nextTier.getPrice());

		// Set upgrade level on team
		game.getTeam(teamKey.name()).ifPresent(team -> team.setUpgradeLevel(key, nextTier.getLevel()));
		applyEffect(game, teamKey, nextTier.getLevel());

		// Notify all team members
		game.getPlayers().stream()
				.filter(p -> teamKey.equals(p.getTeamKey()))
				.forEach(p -> {
					p.sendMessage(teamKey.chatColor() + " §apurchased §6" + name + " " + nextTier.getLevel() + "!");
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
	public abstract void applyEffect(BedWarsGame game, BedWarsMapsConfig.TeamKey teamKey, int level);

	public ItemStack getDisplayItem() {
		return displayItem.with(DataComponents.TOOLTIP_DISPLAY, new TooltipDisplay(false, Set.of()));
	}

}

