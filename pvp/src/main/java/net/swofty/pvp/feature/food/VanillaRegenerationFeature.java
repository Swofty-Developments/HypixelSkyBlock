package net.swofty.pvp.feature.food;

import net.swofty.pvp.events.PlayerRegenerateEvent;
import net.swofty.pvp.feature.FeatureType;
import net.swofty.pvp.feature.RegistrableFeature;
import net.swofty.pvp.feature.config.DefinedFeature;
import net.swofty.pvp.feature.config.FeatureConfiguration;
import net.swofty.pvp.feature.provider.DifficultyProvider;
import net.swofty.pvp.utils.CombatVersion;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerTickEvent;
import net.minestom.server.event.trait.EntityInstanceEvent;
import net.minestom.server.tag.Tag;
import net.minestom.server.world.Difficulty;

/**
 * Vanilla implementation of {@link RegenerationFeature}
 */
public class VanillaRegenerationFeature implements RegenerationFeature, RegistrableFeature {
	public static final DefinedFeature<VanillaRegenerationFeature> DEFINED = new DefinedFeature<>(
			FeatureType.REGENERATION, VanillaRegenerationFeature::new,
			VanillaRegenerationFeature::initPlayer,
			FeatureType.EXHAUSTION, FeatureType.DIFFICULTY, FeatureType.VERSION
	);
	
	public static final Tag<Integer> STARVATION_TICKS = Tag.Integer("starvationTicks");
	
	private final FeatureConfiguration configuration;
	
	private ExhaustionFeature exhaustionFeature;
	private DifficultyProvider difficultyFeature;
	private CombatVersion version;
	
	public VanillaRegenerationFeature(FeatureConfiguration configuration) {
		this.configuration = configuration;
	}
	
	@Override
	public void initDependencies() {
		this.exhaustionFeature = configuration.get(FeatureType.EXHAUSTION);
		this.difficultyFeature = configuration.get(FeatureType.DIFFICULTY);
		this.version = configuration.get(FeatureType.VERSION);
	}
	
	public static void initPlayer(Player player, boolean firstInit) {
		player.setTag(STARVATION_TICKS, 0);
	}
	
	@Override
	public void init(EventNode<EntityInstanceEvent> node) {
		node.addListener(PlayerTickEvent.class, event -> onTick(event.getPlayer()));
	}
	
	protected void onTick(Player player) {
		if (player.getGameMode().invulnerable()) return;
		Difficulty difficulty = difficultyFeature.getValue(player);
		
		int food = player.getFood();
		float health = player.getHealth();
		int starvationTicks = player.getTag(STARVATION_TICKS);
		
		if (version.modern() && player.getFoodSaturation() > 0 && health > 0
				&& health < player.getAttributeValue(Attribute.MAX_HEALTH) && food >= 20) {
			starvationTicks++;
			if (starvationTicks >= 10) {
				float amount = Math.min(player.getFoodSaturation(), 6);
				regenerate(player, amount / 6, amount);
				starvationTicks = 0;
			}
		} else if (food >= 18 && health > 0
				&& health < player.getAttributeValue(Attribute.MAX_HEALTH)) {
			starvationTicks++;
			if (starvationTicks >= 80) {
				regenerate(player, 1, version.legacy() ? 3 : 6);
				starvationTicks = 0;
			}
		} else if (food <= 0) {
			starvationTicks++;
			if (starvationTicks >= 80) {
				if (health > 10 || difficulty == Difficulty.HARD
						|| ((health > 1) && (difficulty == Difficulty.NORMAL))) {
					player.damage(DamageType.STARVE, 1);
				}
				
				starvationTicks = 0;
			}
		} else {
			starvationTicks = 0;
		}
		
		player.setTag(STARVATION_TICKS, starvationTicks);
	}
	
	@Override
	public void regenerate(Player player, float health, float exhaustion) {
		PlayerRegenerateEvent event = new PlayerRegenerateEvent(player, health, exhaustion);
		EventDispatcher.callCancellable(event, () -> {
			player.setHealth(player.getHealth() + event.getAmount());
			exhaustionFeature.addExhaustion(player, event.getExhaustion());
		});
	}
}
