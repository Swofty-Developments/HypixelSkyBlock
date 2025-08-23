package net.swofty.type.bedwarsgame.item.impl;

import net.kyori.adventure.sound.Sound;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.sound.SoundEvent;
import net.minestom.server.tag.Tag;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.pvp.entity.projectile.CustomEntityProjectile;
import net.swofty.pvp.entity.projectile.ItemHoldingProjectile;
import net.swofty.pvp.utils.ViewUtil;
import net.swofty.type.bedwarsgame.entity.ThrownBridgeEgg;
import net.swofty.type.bedwarsgame.item.BedWarsItem;
import net.swofty.type.generic.utility.MathUtility;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class BridgeEgg extends BedWarsItem {
	public BridgeEgg() {
		super("bridge_egg");
	}

	private static Block getWoolBlockFromTeamColor(String teamColor) {
		return switch (teamColor.toLowerCase()) {
			case "red" -> Block.RED_WOOL;
			case "blue" -> Block.BLUE_WOOL;
			case "green" -> Block.LIME_WOOL; // Minecraft uses "lime" for bright green
			case "yellow" -> Block.YELLOW_WOOL;
			case "aqua" -> Block.LIGHT_BLUE_WOOL;
			case "pink" -> Block.PINK_WOOL;
			case "gray", "grey" -> Block.GRAY_WOOL;
			case "white" -> Block.WHITE_WOOL;
			case "black" -> Block.BLACK_WOOL;
			case "purple" -> Block.PURPLE_WOOL;
			case "orange" -> Block.ORANGE_WOOL;
			case "cyan" -> Block.CYAN_WOOL;
			default -> Block.WHITE_WOOL;
		};
	}

	@Override
	public ItemStack getBlandItem() {
		return ItemStack.of(Material.EGG);
	}

	@Override
	public void onItemUse(PlayerUseItemEvent event) {
		Player player = event.getPlayer();
		ItemStack stack = event.getItemStack();
		String teamColor = player.getTag(Tag.String("teamColor"));
		if (teamColor == null || teamColor.isEmpty()) {
			teamColor = "white";
		}

		Block woolBlock = getWoolBlockFromTeamColor(teamColor);

		SoundEvent soundEvent;
		CustomEntityProjectile projectile;
		soundEvent = SoundEvent.ENTITY_EGG_THROW;
		projectile = new ThrownBridgeEgg(woolBlock, player);


		((ItemHoldingProjectile) projectile).setItem(stack);

		ThreadLocalRandom random = ThreadLocalRandom.current();
		ViewUtil.viewersAndSelf(player).playSound(Sound.sound(
				soundEvent,
				Sound.Source.PLAYER,
				0.5f, 0.4f / (random.nextFloat() * 0.4f + 0.8f)
		), player);

		Pos position = player.getPosition().add(0, player.getEyeHeight(), 0);
		projectile.shootFromRotation(position.pitch(), position.yaw(), 0, 1.5, 1.0);
		projectile.setInstance(Objects.requireNonNull(player.getInstance()), position.withView(projectile.getPosition()));

		Vec playerVel = player.getVelocity();
		projectile.setVelocity(projectile.getVelocity().add(playerVel.x(),
				player.isOnGround() ? 0.0D : playerVel.y(), playerVel.z()).mul(1.5));

		if (player.getGameMode() != GameMode.CREATIVE) {
			player.setItemInHand(event.getHand(), stack.withAmount(stack.amount() - 1));
		}

		MathUtility.delay(
				() -> {
					if (projectile.isActive() && !projectile.isRemoved()) {
						projectile.remove();
					}
				},
				TaskSchedule.seconds(4)
		);
	}
}