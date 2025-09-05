package net.swofty.pvp.feature.tracking;

import net.swofty.pvp.damage.combat.CombatManager;
import net.swofty.pvp.feature.FeatureType;
import net.swofty.pvp.feature.RegistrableFeature;
import net.swofty.pvp.feature.config.DefinedFeature;
import net.swofty.pvp.feature.config.FeatureConfiguration;
import net.swofty.pvp.feature.fall.FallFeature;
import net.swofty.pvp.feature.state.PlayerStateFeature;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerDeathEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.event.player.PlayerTickEvent;
import net.minestom.server.event.trait.EntityInstanceEvent;
import net.minestom.server.tag.Tag;
import org.jetbrains.annotations.Nullable;

/**
 * Vanilla implementation of {@link TrackingFeature}
 */
public class VanillaDeathMessageFeature implements TrackingFeature, RegistrableFeature {
	public static final DefinedFeature<VanillaDeathMessageFeature> DEFINED = new DefinedFeature<>(
			FeatureType.TRACKING, VanillaDeathMessageFeature::new,
			VanillaDeathMessageFeature::initPlayer,
			FeatureType.FALL, FeatureType.PLAYER_STATE
	);
	
	public static final Tag<CombatManager> COMBAT_MANAGER = Tag.Transient("combatManager");
	
	private final FeatureConfiguration configuration;
	
	private FallFeature fallFeature;
	private PlayerStateFeature playerStateFeature;
	
	public VanillaDeathMessageFeature(FeatureConfiguration configuration) {
		this.configuration = configuration;
	}
	
	@Override
	public void initDependencies() {
		this.fallFeature = configuration.get(FeatureType.FALL);
		this.playerStateFeature = configuration.get(FeatureType.PLAYER_STATE);
	}
	
	public static void initPlayer(Player player, boolean firstInit) {
		if (firstInit) player.setTag(COMBAT_MANAGER, new CombatManager(player));
	}
	
	@Override
	public void init(EventNode<EntityInstanceEvent> node) {
		node.addListener(PlayerSpawnEvent.class, event -> event.getPlayer().getTag(COMBAT_MANAGER).reset());
		
		node.addListener(PlayerTickEvent.class, event -> event.getPlayer().getTag(COMBAT_MANAGER).tick());
		
		node.addListener(PlayerDeathEvent.class, event -> {
			Component message = getDeathMessage(event.getPlayer());
			event.setChatMessage(message);
			event.setDeathText(message);
		});
	}
	
	@Override
	public void recordDamage(Player player, @Nullable Entity attacker, Damage damage) {
		int id = attacker == null ? -1 : attacker.getEntityId();
		player.getTag(COMBAT_MANAGER).recordDamage(id, damage, fallFeature, playerStateFeature);
	}
	
	@Override
	public @Nullable Component getDeathMessage(Player player) {
		return player.getTag(COMBAT_MANAGER).getDeathMessage();
	}
}
