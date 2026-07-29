package net.swofty.pvp;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.entity.attribute.AttributeInstance;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerPacketOutEvent;
import net.minestom.server.event.trait.EntityInstanceEvent;
import net.minestom.server.network.packet.client.common.ClientKeepAlivePacket;
import net.swofty.pvp.enchantment.CombatEnchantments;
import net.swofty.pvp.feature.CombatFeatures;
import net.swofty.pvp.feature.config.CombatFeatureRegistry;
import net.swofty.pvp.player.CombatPlayer;
import net.swofty.pvp.potion.effect.CombatPotionEffects;
import net.swofty.pvp.potion.item.CombatPotionTypes;
import net.swofty.pvp.utils.AccurateLatencyListener;

/**
 * The main class of MinestomPvP, which contains the {@link MinestomPvP#init()} method.
 * <p>
 * It can also be used to set legacy attack for a player, see {@link MinestomPvP#setLegacyAttack(Player, boolean)}.
 */
public class MinestomPvP {
	/**
	 * Equivalent to creating a new event node from {@link CombatFeatures#modernVanilla()}
	 *
	 * @return the event node with all modern vanilla feature listeners attached
	 */
	public static EventNode<EntityInstanceEvent> events() {
		return CombatFeatures.modernVanilla().createNode();
	}

	/**
	 * Equivalent to creating a new event node from {@link CombatFeatures#legacyVanilla()}
	 *
	 * @return the event node with all legacy (pre-1.9) vanilla feature listeners attached
	 */
	public static EventNode<EntityInstanceEvent> legacyEvents() {
		return CombatFeatures.legacyVanilla().createNode();
	}

	/**
	 * Disables or enables legacy attack for a player.
	 * With legacy attack, the player has no attack speed.
	 *
	 * @param player the player
	 * @param legacyAttack {@code true} if legacy attack should be enabled
	 */
	public static void setLegacyAttack(Player player, boolean legacyAttack) {
		AttributeInstance speed = player.getAttribute(Attribute.ATTACK_SPEED);
		if (legacyAttack) {
			speed.setBaseValue(100);
		} else {
			speed.setBaseValue(speed.attribute().defaultValue());
		}
	}

	/**
	 * Initializes the PvP library. This has a few side effects, for more details see {@link #init(boolean)}.
	 */
	public static void init() {
		init(true);
	}

	/**
	 * Initializes the PvP library.
	 * This method will always initialize the registries and register some global event handlers.
	 * You need to implement a player provider yourself if you want to use the {@link CombatPlayer} class, look for an example in BedWarsPlayer
	 * Depending on the value of the parameters, it might also register:<br>
	 * - a custom packet listener for {@link ClientKeepAlivePacket}<br>
	 *
	 * @param keepAlive When set to true, the custom packet listener will be registered
	 */
	public static void init(boolean keepAlive) {
		CombatEnchantments.registerAll();
		CombatPotionEffects.registerAll();
		CombatPotionTypes.registerAll();

		CombatFeatureRegistry.init();
		CombatPlayer.init(MinecraftServer.getGlobalEventHandler());

		if (keepAlive) {
			MinecraftServer.getPacketListenerManager().setPlayListener(ClientKeepAlivePacket.class, AccurateLatencyListener::listener);
			MinecraftServer.getGlobalEventHandler().addListener(PlayerPacketOutEvent.class, AccurateLatencyListener::onSend);
		}
	}
}
