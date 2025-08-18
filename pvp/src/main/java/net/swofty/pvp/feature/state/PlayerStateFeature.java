package net.swofty.pvp.feature.state;

import net.swofty.pvp.feature.CombatFeature;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.instance.block.Block;
import org.jetbrains.annotations.Nullable;

/**
 * Combat feature which handles certain player states. For now this is only climbing.
 */
public interface PlayerStateFeature extends CombatFeature {
	PlayerStateFeature NO_OP = new PlayerStateFeature() {
		@Override
		public boolean isClimbing(LivingEntity entity) {
			return false;
		}
		
		@Override
		public Block getLastClimbedBlock(LivingEntity entity) {
			return Block.AIR;
		}
	};
	
	boolean isClimbing(LivingEntity entity);
	
	@Nullable Block getLastClimbedBlock(LivingEntity entity);
}
