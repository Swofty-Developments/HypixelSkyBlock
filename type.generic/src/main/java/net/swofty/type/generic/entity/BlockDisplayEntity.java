package net.swofty.type.generic.entity;

import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.metadata.display.BlockDisplayMeta;
import net.minestom.server.instance.block.Block;

import java.util.function.Consumer;

public class BlockDisplayEntity extends LivingEntity {

	public BlockDisplayEntity(Block block, Consumer<BlockDisplayMeta> consumer) {
		super(EntityType.BLOCK_DISPLAY);

		editEntityMeta(BlockDisplayMeta.class, meta -> {
			meta.setBlockState(block);
			meta.setHasNoGravity(true);
			consumer.accept(meta);
		});
	}
}
