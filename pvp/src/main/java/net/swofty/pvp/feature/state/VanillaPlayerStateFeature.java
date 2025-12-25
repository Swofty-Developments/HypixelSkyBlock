package net.swofty.pvp.feature.state;

import net.kyori.adventure.key.Key;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.event.player.PlayerTickEvent;
import net.minestom.server.event.trait.EntityInstanceEvent;
import net.minestom.server.instance.block.Block;
import net.minestom.server.tag.Tag;
import net.swofty.pvp.feature.FeatureType;
import net.swofty.pvp.feature.RegistrableFeature;
import net.swofty.pvp.feature.config.DefinedFeature;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Vanilla implementation of {@link PlayerStateFeature}
 */
public class VanillaPlayerStateFeature implements PlayerStateFeature, RegistrableFeature {
	public static final DefinedFeature<VanillaPlayerStateFeature> DEFINED = new DefinedFeature<>(
			FeatureType.PLAYER_STATE, configuration -> new VanillaPlayerStateFeature()
	);

	public static final Tag<Block> LAST_CLIMBED_BLOCK = Tag.Transient("lastClimbedBlock");

	@Override
	public void init(EventNode<EntityInstanceEvent> node) {
		node.addListener(PlayerTickEvent.class, event -> {
			Player player = event.getPlayer();
			if (player.isOnGround() && player.hasTag(LAST_CLIMBED_BLOCK)) {
				// Make sure fall damage message still has the correct climbed block
				// Due to multithreading this can be triggered before the death message is computed
				player.scheduleNextTick(p -> p.removeTag(LAST_CLIMBED_BLOCK));
			}
		});

		node.addListener(PlayerMoveEvent.class, event -> {
			Player player = event.getPlayer();
			if (isClimbing(player)) {
				player.setTag(LAST_CLIMBED_BLOCK, player.getInstance().getBlock(player.getPosition()));
			}
		});
	}

	@Override
	public boolean isClimbing(LivingEntity entity) {
		if (entity instanceof Player player && player.getGameMode() == GameMode.SPECTATOR) return false;

		var tag = MinecraftServer.process().blocks().getTag(Key.key("minecraft:climbable"));
		assert tag != null;

		Block block = Objects.requireNonNull(entity.getInstance()).getBlock(entity.getPosition());
		var key = block.asKey();
		assert key != null;
		return tag.contains(key);
	}

	@Override
	public @Nullable Block getLastClimbedBlock(LivingEntity entity) {
		return entity.getTag(LAST_CLIMBED_BLOCK);
	}
}
