package net.swofty.pvp.feature.spectate;

import net.swofty.pvp.events.PlayerSpectateEvent;
import net.swofty.pvp.feature.FeatureType;
import net.swofty.pvp.feature.RegistrableFeature;
import net.swofty.pvp.feature.config.DefinedFeature;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.entity.EntityAttackEvent;
import net.minestom.server.event.player.PlayerTickEvent;
import net.minestom.server.event.trait.EntityInstanceEvent;
import net.minestom.server.tag.Tag;

/**
 * Vanilla implementation of {@link SpectateFeature}
 */
public class VanillaSpectateFeature implements SpectateFeature, RegistrableFeature {
	public static final DefinedFeature<VanillaSpectateFeature> DEFINED = new DefinedFeature<>(
			FeatureType.SPECTATE, configuration -> new VanillaSpectateFeature()
	);
	
	public static final Tag<Entity> SPECTATING = Tag.Transient("spectating");
	
	@Override
	public int getPriority() {
		// Make sure events are called on this node before on VanillaAttackFeature
		// This seems to be the only way to have 'dependencies' without overcomplicating
		return -1;
	}
	
	@Override
	public void init(EventNode<EntityInstanceEvent> node) {
		node.addListener(EntityAttackEvent.class, event -> {
			if (event.getEntity() instanceof Player player && player.getGameMode() == GameMode.SPECTATOR)
				makeSpectate(player, event.getTarget());
		});
		
		node.addListener(PlayerTickEvent.class, event -> spectateTick(event.getPlayer()));
	}
	
	protected void spectateTick(Player player) {
		Entity spectating = player.getTag(SPECTATING);
		if (spectating == null || spectating == player) return;
		
		// This is to make sure other players don't see the player standing still while spectating
		// And when the player stops spectating,
		// they are at the entities position instead of their position before spectating
		player.teleport(spectating.getPosition());
		
		if (player.getEntityMeta().isSneaking() || spectating.isRemoved()
				|| (spectating instanceof LivingEntity livingSpectating && livingSpectating.isDead())) {
			stopSpectating(player);
		}
	}
	
	@Override
	public void makeSpectate(Player player, Entity target) {
		PlayerSpectateEvent playerSpectateEvent = new PlayerSpectateEvent(player, target);
		EventDispatcher.callCancellable(playerSpectateEvent, () -> {
			player.spectate(target);
			player.setTag(SPECTATING, target);
		});
	}
	
	@Override
	public void stopSpectating(Player player) {
		player.stopSpectating();
		player.removeTag(SPECTATING);
	}
}
