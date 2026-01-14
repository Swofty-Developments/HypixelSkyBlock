package net.swofty.type.skyblockgeneric.item.handlers.ability;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.minestom.server.coordinate.BlockVec;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.swofty.commons.skyblock.statistics.ItemStatistic;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.skyblockgeneric.event.custom.CustomBlockBreakEvent;
import net.swofty.type.skyblockgeneric.item.handlers.ability.abilities.BuildersWandAbility;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.skyblockgeneric.user.statistics.TemporaryStatistic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AbilityRegistry {
    private static final Map<String, RegisteredAbility> REGISTERED_ABILITIES = new HashMap<>();

	static {
		register(new RegisteredAbility(
				"WITHER_IMPACT",
				"Wither Impact",
				(player, item) -> "§7Teleports §a10 Blocks §7ahead of you. Then implode dealing §c10000 §7damage to nearby enemies. Also applies the wither shield scroll ability reducing mobdamage taken and granting an absorption shield for §e5 §7seconds.",
				RegisteredAbility.AbilityActivation.RIGHT_CLICK,
				50,
				new RegisteredAbility.AbilityManaCost(25),
				(player, item, ignored, ignored2) -> {
					BlockVec tpPos = player.getPosition().add(player.getPosition().direction().mul(10)).asBlockVec();
					if (!player.getInstance().getBlock(tpPos.add(0, 1, 0)).isAir() || !player.getInstance().getBlock(tpPos.add(0, 2, 0)).isAir()) return false;
					player.teleport(new Pos(tpPos.add(0, 1, 0), player.getPosition().yaw(), player.getPosition().pitch()));
					return true;
					// TODO: damage nearby mobs
				}
		));

		register(new RegisteredAbility(
				"INSTANT_TRANSMISSION",
				"Instant Transmission",
				(player, item) -> "§7Teleports §a8 Blocks §7ahead of you and gain §a+50 §fSpeed for §a3 seconds§7.",
				RegisteredAbility.AbilityActivation.RIGHT_CLICK,
				5,
				new RegisteredAbility.AbilityManaCost(50),
				(player, item, ignored, ignored2) -> {
					BlockVec tpPos = player.getPosition().add(player.getPosition().direction().mul(8)).asBlockVec();
					if (!player.getInstance().getBlock(tpPos.add(0, 1, 0)).isAir() || !player.getInstance().getBlock(tpPos.add(0, 2, 0)).isAir()) return false; // TODO: Make it so you can't go through walls
					player.teleport(new Pos(tpPos.add(0, 1, 0), player.getPosition().yaw(), player.getPosition().pitch()));
					ItemStatistics speedStats = ItemStatistics.builder().withBase(ItemStatistic.SPEED, 50.0).build();
					TemporaryStatistic speedBoost = TemporaryStatistic.builder().withStatistics(speedStats).withExpirationInMs(3000).withDisplayName("Instant Transmission").build();
					player.getStatistics().boostStatistic(speedBoost);
					return true;
				}
		));

		register(new RegisteredAbility(
				"ETHER_TRANSMISSION",
				"Ether Transmission",
				(player, item) -> "§7Teleport to your targeted block up to §a57 §7blocks away.",
				RegisteredAbility.AbilityActivation.SNEAK_RIGHT_CLICK,
				5,
				new RegisteredAbility.AbilityManaSoulflowCost(180, 1),
				(player, item, ignored, ignored2) -> {
					Point targetedBlock = player.getTargetBlockPosition(57);
					if (targetedBlock == null) return false;
					BlockVec tpPos = targetedBlock.asBlockVec();
					if (!player.getInstance().getBlock(tpPos.add(0, 1, 0)).isAir() || !player.getInstance().getBlock(tpPos.add(0, 2, 0)).isAir()) return false;
					player.teleport(new Pos(tpPos.add(0, 1, 0), player.getPosition().yaw(), player.getPosition().pitch()));
					return true;
				}
		));

		register(new RegisteredAbility(
				"TRUE_DWARTH",
						"True Dwarth",
				(player, item) -> "§7Shows the way towards the nearest §6Emissary §7while in the §2Dwarven Mines§7.",
				RegisteredAbility.AbilityActivation.RIGHT_CLICK,
				20 * 3,
				new RegisteredAbility.NoAbilityCost(),
				(player, item, targetedBlock, blockFace) -> {
					player.sendMessage(Component.text("§cThis Feature is not there yet. §aOpen a Pull request HERE to get it added quickly!")
							.clickEvent(ClickEvent.openUrl("https://github.com/Swofty-Developments/HypixelSkyBlock")));
					return false;
				}
		));

		register(new RegisteredAbility(
				"SPEED_BOOST",
				"Speed Boost",
				(player, item) -> "§7Grants §7+100 Speed",
				RegisteredAbility.AbilityActivation.RIGHT_CLICK,
				20 * 5,
				new RegisteredAbility.AbilityManaCost(50),
				(player, item, ignored, ignored2) -> {
					ItemStatistics speedStats = ItemStatistics.builder().withBase(ItemStatistic.SPEED, 100.0).build();
					TemporaryStatistic speedBoost = TemporaryStatistic.builder().withStatistics(speedStats).withExpirationInMs(30000).withDisplayName("Speed Boost").build();
					player.getStatistics().boostStatistic(speedBoost);
					return true;
				}
		));

		register(new RegisteredPassiveAbility(
				"STORED_POTENTIAL",
				"Stored Potential",
				(player, item) -> {
					StringBuilder description = new StringBuilder();
					description.append("§7Grants §6+10⸕ Mining Speed §7for every\n100 blocks mined.\n§8(Max +250⸕ Mining Speed)\n\n");
					description.append("§7Blocks Mined: §a").append(item.getAttributeHandler().getStoredPotential()).append("\n");
					int miningSpeed = Math.min((item.getAttributeHandler().getStoredPotential() / 100) * 10, 250);
					description.append("§7Current Bonus: §a").append(miningSpeed);
					return description.toString();
				},
				List.of(new RegisteredPassiveAbility.Action<>(
						CustomBlockBreakEvent.class,
						EventNodes.CUSTOM,
						event -> {
							SkyBlockPlayer player = event.getPlayer();
							player.updateItemInSlot(player.getHeldSlot(), (item) -> {
								item.getAttributeHandler().setStoredPotential(item.getAttributeHandler().getStoredPotential() + 1);
								item.getAttributeHandler().setExtraDynamicStatistics(
										ItemStatistics.builder().withAdditive(
												ItemStatistic.MINING_SPEED, (double) Math.min((item.getAttributeHandler().getStoredPotential() / 100) * 10, 250)
										).build()
								);
							});
						},
						RegisteredPassiveAbility.Action.createDefaultCondition("STORED_POTENTIAL")
				))
		));

//		register(new RegisteredPassiveAbility(
//				"BEJEWELED_BLADE",
//				"Bejeweled Blade",
//				"§7Deals §a+150% §7damage to mobs on §bMining Islands.",
//				List.of(new RegisteredPassiveAbility.Action<>(
//
//				))
//		));

		register(new BuildersWandAbility());
	}

	public static void register(RegisteredAbility ability) {
		REGISTERED_ABILITIES.put(ability.getId(), ability);
	}

	public static RegisteredAbility getAbility(String id) {
		return REGISTERED_ABILITIES.get(id);
	}

	public static Map<String, RegisteredAbility> getRegisteredAbilities() {
		return REGISTERED_ABILITIES;
	}

}