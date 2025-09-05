package net.swofty.pvp.feature.fall;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.entity.EntityTickEvent;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.event.trait.EntityInstanceEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;
import net.minestom.server.potion.PotionEffect;
import net.minestom.server.registry.Registries;
import net.minestom.server.sound.SoundEvent;
import net.minestom.server.tag.Tag;
import net.swofty.pvp.feature.FeatureType;
import net.swofty.pvp.feature.RegistrableFeature;
import net.swofty.pvp.feature.config.DefinedFeature;
import net.swofty.pvp.feature.config.FeatureConfiguration;
import net.swofty.pvp.feature.state.PlayerStateFeature;

/**
 * Vanilla implementation of {@link FallFeature}
 */
public class VanillaFallFeature implements FallFeature, RegistrableFeature {
	public static final Tag<Double> FALL_DISTANCE = Tag.Transient("fallDistance");
	public static final DefinedFeature<VanillaFallFeature> DEFINED = new DefinedFeature<>(
			FeatureType.FALL, VanillaFallFeature::new,
			VanillaFallFeature::initPlayer,
			FeatureType.PLAYER_STATE
	);
	public static final Tag<Boolean> EXTRA_FALL_PARTICLES = Tag.Transient("extraFallParticles");

	private final FeatureConfiguration configuration;

	private PlayerStateFeature playerStateFeature;

	public VanillaFallFeature(FeatureConfiguration configuration) {
		this.configuration = configuration;
	}

	public static void initPlayer(Player player, boolean firstInit) {
		player.setTag(FALL_DISTANCE, 0.0);
	}

	@Override
	public void initDependencies() {
		this.playerStateFeature = configuration.get(FeatureType.PLAYER_STATE);
	}

	@Override
	public void init(EventNode<EntityInstanceEvent> node) {
		// For living non-player entities, handle fall damage every tick
		node.addListener(EntityTickEvent.class, event -> {
			if (!(event.getEntity() instanceof LivingEntity livingEntity)) return;
			if (livingEntity instanceof Player) return;

			Pos previousPosition = livingEntity.getPreviousPosition();
			handleFallDamage(livingEntity, previousPosition, livingEntity.getPosition(), livingEntity.isOnGround());
		});

		// For players, handle fall damage on move event
		node.addListener(PlayerMoveEvent.class, event -> {
			Player player = event.getPlayer();
			if (playerStateFeature.isClimbing(player)) player.setTag(FALL_DISTANCE, 0.0);

			handleFallDamage(
					player, player.getPosition(),
					event.getNewPosition(), event.isOnGround()
			);
		});
	}

	public void handleFallDamage(LivingEntity entity, Pos currPos, Pos newPos, boolean onGround) {
		double dy = newPos.y() - currPos.y();
		double fallDistance = getFallDistance(entity);

		if ((entity instanceof Player player && player.isFlying())
				|| entity.hasEffect(PotionEffect.LEVITATION)
				|| entity.hasEffect(PotionEffect.SLOW_FALLING) || dy > 0) {
			entity.setTag(FALL_DISTANCE, 0.0);
			return;
		}

		if (entity.isFlyingWithElytra() && entity.getVelocity().y() > -0.5) {
			entity.setTag(FALL_DISTANCE, 1.0);
			return;
		}

		if (!onGround) {
			if (dy < 0) entity.setTag(FALL_DISTANCE, fallDistance - dy);
			return;
		}

		Point landingPos = getLandingPos(entity, newPos);
		Block block = entity.getInstance().getBlock(landingPos);

		if (entity.hasTag(EXTRA_FALL_PARTICLES) && entity.getTag(EXTRA_FALL_PARTICLES) && fallDistance > 0.0) {
			Vec position = Vec.fromPoint(landingPos).apply(Vec.Operator.FLOOR).add(0.5, 1, 0.5);
			int particleCount = (int) Math.max(0, Math.min(200, 50 * fallDistance));

			entity.sendPacketToViewersAndSelf(new ParticlePacket(
					Particle.BLOCK.withBlock(block),
					position.x(), position.y(), position.z(),
					0.3f, 0.3f, 0.3f,
					0.15f, particleCount
			));

			entity.removeTag(EXTRA_FALL_PARTICLES);
		}

		double safeFallDistance = entity.getAttributeValue(Attribute.SAFE_FALL_DISTANCE);
		if (fallDistance > safeFallDistance) {
			if (!block.isAir()) {
				double damageDistance = Math.ceil(fallDistance - safeFallDistance);
				double particleMultiplier = Math.min(0.2 + damageDistance / 15.0, 2.5);
				int particleCount = (int) (150 * particleMultiplier);

				entity.sendPacketToViewersAndSelf(new ParticlePacket(
						Particle.BLOCK.withBlock(block), false,
						false,
						newPos.x(), newPos.y(), newPos.z(),
						0, 0, 0,
						0.15f, particleCount
				));
			}
		}

		entity.setTag(FALL_DISTANCE, 0.0);

		if (entity instanceof Player player && player.getGameMode().invulnerable()) return;
		int damage = getFallDamage(entity, fallDistance);
		if (damage > 0) {
			playFallSound(entity, damage);
			entity.damage(DamageType.FALL, damage);
		}
	}

	public void playFallSound(LivingEntity entity, int damage) {
		boolean bigFall = damage > 4;

		entity.getViewersAsAudience().playSound(Sound.sound(
				bigFall ?
						SoundEvent.ENTITY_PLAYER_BIG_FALL :
						SoundEvent.ENTITY_PLAYER_SMALL_FALL,
				entity instanceof Player ? Sound.Source.PLAYER : Sound.Source.HOSTILE,
				1.0f, 1.0f
		), entity);
	}

	@Override
	public int getFallDamage(LivingEntity entity, double fallDistance) {
		double safeFallDistance = entity.getAttributeValue(Attribute.SAFE_FALL_DISTANCE);
		return (int) Math.ceil((fallDistance - safeFallDistance) * entity.getAttributeValue(Attribute.FALL_DAMAGE_MULTIPLIER));
	}

	@Override
	public double getFallDistance(LivingEntity entity) {
		return entity.hasTag(FALL_DISTANCE) ? entity.getTag(FALL_DISTANCE) : 0.0;
	}

	@Override
	public void resetFallDistance(LivingEntity entity) {
		entity.setTag(FALL_DISTANCE, 0.0);
	}

	@Override
	public void setExtraFallParticles(LivingEntity entity, boolean extraFallParticles) {
		if (extraFallParticles) entity.setTag(EXTRA_FALL_PARTICLES, true);
		else entity.removeTag(EXTRA_FALL_PARTICLES);
	}

	protected Point getLandingPos(LivingEntity livingEntity, Pos position) {
		Point offset = position.add(0, -0.2, 0);
		Instance instance = livingEntity.getInstance();

		if (instance == null) return offset;
		if (!instance.getBlock(offset).isAir()) return offset;

		Point offsetDown = offset.add(0, -1, 0);
		Block block = instance.getBlock(offsetDown);

		Registries registries = MinecraftServer.process();
		var fences = registries.blocks().getTag(Key.key("minecraft:fences"));
		var walls = registries.blocks().getTag(Key.key("minecraft:walls"));
		var fenceGates = registries.blocks().getTag(Key.key("minecraft:fence_gates"));

		var key = block.asKey();

		assert fences != null;
		assert walls != null;
		assert fenceGates != null;
		assert key != null;

		if (fences.contains(key)
				|| walls.contains(key)
				|| fenceGates.contains(key)) {
			return offsetDown;
		}

		return offset;
	}
}
