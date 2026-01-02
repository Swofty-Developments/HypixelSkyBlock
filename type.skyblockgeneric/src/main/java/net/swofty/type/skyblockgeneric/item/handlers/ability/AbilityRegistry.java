package net.swofty.type.skyblockgeneric.item.handlers.ability;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.minestom.server.coordinate.BlockVec;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.swofty.type.skyblockgeneric.item.handlers.ability.abilities.BuildersWandAbility;

import java.util.HashMap;
import java.util.Map;

public class AbilityRegistry {
    private static final Map<String, RegisteredAbility> REGISTERED_ABILITIES = new HashMap<>();

	static {
		register(new RegisteredAbility(
				"WITHER_IMPACT",
				"Wither Impact",
				"§7Teleports §a10 Blocks §7ahead of you. Then implode dealing §c10000 §7damage to nearby enemies. Also applies the wither shield scroll ability reducing mobdamage taken and granting an absorption shield for §e5 §7seconds.",
				RegisteredAbility.AbilityActivation.RIGHT_CLICK,
				50,
				new RegisteredAbility.AbilityManaCost(25),
				(player, item, ignored, ignored2) -> {
					player.teleport(player.getPosition().add(player.getPosition().direction().mul(10)));
					// TODO: damage nearby mobs
				}
		));

		register(new RegisteredAbility(
				"INSTANT_TRANSMISSION",
				"Instant Transmission",
				"§7Teleports §a8 Blocks §7ahead of you and gain §a+50 §fSpeed for §a3 seconds§7.",
				RegisteredAbility.AbilityActivation.RIGHT_CLICK,
				5,
				new RegisteredAbility.AbilityManaCost(50),
				(player, item, ignored, ignored2) -> {
					player.teleport(player.getPosition().add(player.getPosition().direction().mul(8)));
					// TODO: add speed too
				}
		));

		register(new RegisteredAbility(
				"ETHER_TRANSMISSION",
				"Ether Transmission",
				"§7Teleport to your targeted block up to §a57 §7blocks away.",
				RegisteredAbility.AbilityActivation.SNEAK_RIGHT_CLICK,
				5,
				new RegisteredAbility.AbilityManaSoulflowCost(180, 1),
				(player, item, ignored, ignored2) -> {
					Point targetedBlock = player.getTargetBlockPosition(57);
					if (targetedBlock == null) return;
					BlockVec tpPos = targetedBlock.asBlockVec();
					if (!player.getInstance().getBlock(tpPos.add(0, 1, 0)).isAir() || !player.getInstance().getBlock(tpPos.add(0, 2, 0)).isAir()) return; // TODO: don't consume mana/soulflow if teleport fails
					player.teleport(new Pos(tpPos.add(0, 1, 0), player.getPosition().yaw(), player.getPosition().pitch()));
				}
		));

		register(new RegisteredAbility(
				"TRUE_DWARTH",
						"True Dwarth",
				"§7Shows the way towards the nearest §6Emissary §7while in the §2Dwarven Mines§7.",
				RegisteredAbility.AbilityActivation.RIGHT_CLICK,
				20 * 3,
				new RegisteredAbility.NoAbilityCost(),
				(player, item, targetedBlock, blockFace) -> {
					player.sendMessage(Component.text("§cThis Feature is not there yet. §aOpen a Pull request HERE to get it added quickly!")
							.clickEvent(ClickEvent.openUrl("https://github.com/Swofty-Developments/HypixelSkyBlock")));
				}
		));

//		register(new RegisteredAbility( // TODO: Figure out how to implement passive abiltiies
//				"BEJEWELED_BLADE",
//				"Bejeweled Blade",
//				"§7Deals §a+150% §7damage to mobs on §bMining Islands.",
//				RegisteredAbility.AbilityActivation.
//		))

		register(new BuildersWandAbility());
	}

	public static void register(RegisteredAbility ability) {
		REGISTERED_ABILITIES.put(ability.getId(), ability);
	}

	public static RegisteredAbility getAbility(String id) {
		return REGISTERED_ABILITIES.get(id);
	}
}