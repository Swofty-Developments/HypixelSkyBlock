package net.swofty.type.skyblockgeneric.item.handlers.ability;

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
					player.sendMessage("Hey");
				}
		));

		register(new RegisteredAbility(
				"INSTANT_TRANSMISSION",
				"Instant Transmission",
				"§7Teleports §a8 Blocks §7ahead of you and gain §a+50 §fSpeed for §a3 seconds§f.",
				RegisteredAbility.AbilityActivation.RIGHT_CLICK,
				5,
				new RegisteredAbility.AbilityManaCost(50),
				(player, item, ignored, ignored2) -> {
					player.teleport(player.getPosition().add(player.getPosition().direction().mul(8)));
					// add speed too
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