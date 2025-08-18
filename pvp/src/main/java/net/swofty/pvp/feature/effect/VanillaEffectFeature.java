package net.swofty.pvp.feature.effect;

import net.swofty.pvp.entity.projectile.Arrow;
import net.swofty.pvp.events.PotionVisibilityEvent;
import net.swofty.pvp.feature.FeatureType;
import net.swofty.pvp.feature.RegistrableFeature;
import net.swofty.pvp.feature.config.DefinedFeature;
import net.swofty.pvp.feature.config.FeatureConfiguration;
import net.swofty.pvp.feature.food.ExhaustionFeature;
import net.swofty.pvp.feature.food.FoodFeature;
import net.swofty.pvp.potion.effect.CombatPotionEffect;
import net.swofty.pvp.potion.effect.CombatPotionEffects;
import net.swofty.pvp.potion.item.CombatPotionType;
import net.swofty.pvp.potion.item.CombatPotionTypes;
import net.swofty.pvp.utils.CombatVersion;
import net.swofty.pvp.utils.PotionFlags;
import net.kyori.adventure.util.RGBLike;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.metadata.LivingEntityMeta;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.entity.EntityDeathEvent;
import net.minestom.server.event.entity.EntityPotionAddEvent;
import net.minestom.server.event.entity.EntityPotionRemoveEvent;
import net.minestom.server.event.entity.EntityTickEvent;
import net.minestom.server.event.trait.EntityInstanceEvent;
import net.minestom.server.item.component.PotionContents;
import net.minestom.server.particle.Particle;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import net.minestom.server.potion.PotionType;
import net.minestom.server.potion.TimedPotion;
import net.minestom.server.tag.Tag;
import net.minestom.server.utils.time.TimeUnit;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Vanilla implementation of {@link EffectFeature}
 */
public class VanillaEffectFeature implements EffectFeature, RegistrableFeature {
	public static final DefinedFeature<VanillaEffectFeature> DEFINED = new DefinedFeature<>(
			FeatureType.EFFECT, VanillaEffectFeature::new,
			FeatureType.EXHAUSTION, FeatureType.FOOD, FeatureType.VERSION
	);
	
	public static final Tag<Map<PotionEffect, Integer>> DURATION_LEFT = Tag.Transient("effectDurationLeft");
	public static final int DEFAULT_POTION_COLOR = 0xff385dc6;
	
	private final FeatureConfiguration configuration;
	
	private ExhaustionFeature exhaustionFeature;
	private FoodFeature foodFeature;
	private CombatVersion version;
	
	public VanillaEffectFeature(FeatureConfiguration configuration) {
		this.configuration = configuration;
	}
	
	@Override
	public void initDependencies() {
		this.exhaustionFeature = configuration.get(FeatureType.EXHAUSTION);
		this.foodFeature = configuration.get(FeatureType.FOOD);
		this.version = configuration.get(FeatureType.VERSION);
	}
	
	@Override
	public void init(EventNode<EntityInstanceEvent> node) {
		node.addListener(EntityDeathEvent.class, event ->
				event.getEntity().clearEffects());
		
		node.addListener(EntityTickEvent.class, event -> {
			if (!(event.getEntity() instanceof LivingEntity entity)) return;
			Map<PotionEffect, Integer> potionMap = getDurationLeftMap(entity);
			
			for (TimedPotion potion : entity.getActiveEffects()) {
				potionMap.putIfAbsent(potion.potion().effect(), potion.potion().duration() - 1);
				int durationLeft = potionMap.get(potion.potion().effect());
				
				if (durationLeft > 0) {
					CombatPotionEffect combatPotionEffect = CombatPotionEffects.get(potion.potion().effect());
					int amplifier = potion.potion().amplifier();
					
					if (combatPotionEffect.canApplyUpdateEffect(durationLeft, amplifier)) {
						combatPotionEffect.applyUpdateEffect(entity, amplifier, exhaustionFeature, foodFeature);
					}
					
					potionMap.put(potion.potion().effect(), durationLeft - 1);
				}
			}
			
			if (entity instanceof Player player && player.hasEffect(PotionEffect.ABSORPTION) && player.getAdditionalHearts() <= 0) {
				player.removeEffect(PotionEffect.ABSORPTION);
			}
			
			//TODO keep track of underlying potions with longer duration
			if (potionMap.size() != entity.getActiveEffects().size()) {
				potionMap.keySet().removeIf(effect -> !entity.hasEffect(effect));
			}
		});
		
		node.addListener(EntityPotionAddEvent.class, event -> {
			if (!(event.getEntity() instanceof LivingEntity entity)) return;
			Map<PotionEffect, Integer> potionMap = getDurationLeftMap(entity);
			boolean infinite = event.getPotion().duration() == Potion.INFINITE_DURATION;
			potionMap.put(event.getPotion().effect(), infinite ? Integer.MAX_VALUE : event.getPotion().duration());
			
			CombatPotionEffect combatPotionEffect = CombatPotionEffects.get(event.getPotion().effect());
			combatPotionEffect.onApplied(entity, event.getPotion().amplifier(), version);
			
			updatePotionVisibility(entity);
		});
		
		node.addListener(EntityPotionRemoveEvent.class, event -> {
			if (!(event.getEntity() instanceof LivingEntity entity)) return;
			
			CombatPotionEffect combatPotionEffect = CombatPotionEffects.get(event.getPotion().effect());
			combatPotionEffect.onRemoved(entity, event.getPotion().amplifier(), version);
			
			//Delay update 1 tick because we need to have the removing effect removed
			MinecraftServer.getSchedulerManager()
					.buildTask(() -> updatePotionVisibility(entity))
					.delay(1, TimeUnit.SERVER_TICK)
					.schedule();
		});
	}
	
	private Map<PotionEffect, Integer> getDurationLeftMap(Entity entity) {
		Map<PotionEffect, Integer> potionMap = entity.getTag(DURATION_LEFT);
		if (potionMap == null) {
			potionMap = new ConcurrentHashMap<>();
			entity.setTag(DURATION_LEFT, potionMap);
		}
		return potionMap;
	}
	
	@Override
	public int getPotionColor(PotionContents contents) {
		if (contents.customColor() != null) {
			RGBLike rgbLike = contents.customColor();
			return PotionColorUtils.rgba(255, rgbLike.red(), rgbLike.green(), rgbLike.blue());
		} else if (contents.equals(PotionContents.EMPTY)) {
			return DEFAULT_POTION_COLOR;
		} else {
			Collection<Potion> effects = getAllPotions(contents);
			int color = PotionColorUtils.getPotionColor(effects);
			return color == -1 ? DEFAULT_POTION_COLOR : color;
		}
	}
	
	@Override
	public List<Potion> getAllPotions(PotionType potionType,
	                                  Collection<net.minestom.server.potion.CustomPotionEffect> customEffects) {
		// PotionType effects plus custom effects
		List<Potion> potions = new ArrayList<>();
		
		CombatPotionType combatPotionType = CombatPotionTypes.get(potionType);
		if (combatPotionType != null) potions.addAll(combatPotionType.getEffects(version));
		
		potions.addAll(customEffects.stream().map((customPotion) ->
				new Potion(Objects.requireNonNull(customPotion.id()),
						(byte)customPotion.amplifier(), customPotion.duration(),
						PotionFlags.create(
								customPotion.isAmbient(),
								customPotion.showParticles(),
								customPotion.showIcon()
						))).toList());
		
		return potions;
	}
	
	@Override
	public void updatePotionVisibility(LivingEntity entity) {
		boolean ambient;
		List<Particle> particles;
		boolean invisible;
		
		if (entity instanceof Player player && player.getGameMode() == GameMode.SPECTATOR) {
			ambient = false;
			particles = List.of();
			invisible = true;
		} else {
			Collection<TimedPotion> effects = entity.getActiveEffects();
			if (effects.isEmpty()) {
				ambient = false;
				particles = List.of();
				invisible = false;
			} else {
				ambient = true;
				particles = new ArrayList<>();
				
				for (TimedPotion potion : effects) {
					if (!potion.potion().isAmbient()) {
						ambient = false;
					}
					
					if (potion.potion().hasParticles()) {
						CombatPotionEffect effect = CombatPotionEffects.get(potion.potion().effect());
						particles.add(effect.getParticle(potion.potion()));
					}
				}
				
				invisible = entity.hasEffect(PotionEffect.INVISIBILITY);
			}
		}
		
		PotionVisibilityEvent potionVisibilityEvent = new PotionVisibilityEvent(entity, ambient, particles, invisible);
		EventDispatcher.callCancellable(potionVisibilityEvent, () -> {
			LivingEntityMeta meta = (LivingEntityMeta) entity.getEntityMeta();
			
			meta.setPotionEffectAmbient(potionVisibilityEvent.isAmbient());
			meta.setEffectParticles(potionVisibilityEvent.getParticles());
			meta.setInvisible(potionVisibilityEvent.isInvisible());
		});
	}
	
	@Override
	public void addArrowEffects(LivingEntity entity, Arrow arrow) {
		PotionContents potionContents = arrow.getPotion();
		
		CombatPotionType combatPotionType = CombatPotionTypes.get(potionContents.potion());
		if (combatPotionType != null) {
			for (Potion potion : combatPotionType.getEffects(version)) {
				CombatPotionEffect combatPotionEffect = CombatPotionEffects.get(potion.effect());
				if (combatPotionEffect.isInstant()) {
					combatPotionEffect.applyInstantEffect(arrow, null,
							entity, potion.amplifier(), 1.0, exhaustionFeature, foodFeature);
				} else {
					int duration = Math.max(potion.duration() / 8, 1);
					entity.addEffect(new Potion(potion.effect(), potion.amplifier(), duration, potion.flags()));
				}
			}
		}
		
		if (potionContents.customEffects().isEmpty()) return;
		
		potionContents.customEffects().stream().map(customPotion ->
						new Potion(Objects.requireNonNull(customPotion.id()),
								(byte)customPotion.amplifier(), customPotion.duration(),
								PotionFlags.create(
										customPotion.isAmbient(),
										customPotion.showParticles(),
										customPotion.showIcon()
								)))
				.forEach(potion -> {
					CombatPotionEffect combatPotionEffect = CombatPotionEffects.get(potion.effect());
					if (combatPotionEffect.isInstant()) {
						combatPotionEffect.applyInstantEffect(arrow, null,
								entity, potion.amplifier(), 1.0, exhaustionFeature, foodFeature);
					} else {
						entity.addEffect(new Potion(potion.effect(), potion.amplifier(),
								potion.duration(), potion.flags()));
					}
				});
	}
	
	@Override
	public void addSplashPotionEffects(LivingEntity entity, PotionContents potionContents, double proximity,
	                                   @Nullable Entity source, @Nullable Entity attacker) {
		for (Potion potion : getAllPotions(potionContents)) {
			CombatPotionEffect combatPotionEffect = CombatPotionEffects.get(potion.effect());
			if (combatPotionEffect.isInstant()) {
				combatPotionEffect.applyInstantEffect(source, attacker,
						entity, potion.amplifier(), proximity, exhaustionFeature, foodFeature);
			} else {
				int duration = potion.duration();
				if (version.legacy()) duration = (int) Math.floor(duration * 0.75);
				duration = (int) (proximity * (double) duration + 0.5);
				
				if (duration > 20) {
					entity.addEffect(new Potion(potion.effect(), potion.amplifier(), duration, potion.flags()));
				}
			}
		}
	}
}
