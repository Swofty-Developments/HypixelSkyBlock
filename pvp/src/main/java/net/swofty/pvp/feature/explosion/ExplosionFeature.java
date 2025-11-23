package net.swofty.pvp.feature.explosion;

import net.swofty.pvp.feature.CombatFeature;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.ExplosionSupplier;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Combat feature which handles explosions. Contains a method to prime an explosive at a certain place.
 * <p>
 * Important to note is that implementations of this feature might provide an {@link ExplosionSupplier}.
 * This explosion supplier should be registered to every (Minestom) instance which should allow explosions.
 * See {@link ExplosionFeature#getExplosionSupplier()}.
 */
public interface ExplosionFeature extends CombatFeature {
	ExplosionFeature NO_OP = new ExplosionFeature() {
		@Override
		public @Nullable ExplosionSupplier getExplosionSupplier() {
			return null;
		}
		
		@Override
		public void primeExplosive(Instance instance, Point blockPosition, @NotNull IgnitionCause cause, int fuse) {}
	};
	
	@Nullable ExplosionSupplier getExplosionSupplier();
	
	void primeExplosive(Instance instance, Point blockPosition, @NotNull IgnitionCause cause, int fuse);
	
	sealed interface IgnitionCause {
		@Nullable Entity causingEntity();
		
		/**
		 * Ignition cause when a player directly ignites an explosive.
		 *
		 * @param player the player which ignited the explosive
		 */
		record ByPlayer(Player player) implements IgnitionCause {
			@Override
			public @Nullable Player causingEntity() {
				return player;
			}
		}
		
		/**
		 * Ignition cause when an explosion causes another explosive to ignite.
		 *
		 * @param causingEntity the entity which caused the original explosion
		 */
		record Explosion(Entity causingEntity) implements IgnitionCause {}
	}
}
