package net.swofty.type.generic.entity;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.metadata.display.BlockDisplayMeta;
import net.minestom.server.entity.metadata.other.InteractionMeta;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.minestom.server.instance.block.Block;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.function.BiConsumer;

public class BlockDisplayEntity extends LivingEntity {

	public BlockDisplayEntity(Block block) {
		super(EntityType.BLOCK_DISPLAY);

		editEntityMeta(BlockDisplayMeta.class, meta -> {
			meta.setBlockState(block);
			meta.setHasNoGravity(true);
		});
	}
}
