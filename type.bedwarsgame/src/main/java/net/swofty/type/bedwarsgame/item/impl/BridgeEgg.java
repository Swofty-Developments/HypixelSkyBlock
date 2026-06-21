package net.swofty.type.bedwarsgame.item.impl;

import net.kyori.adventure.sound.Sound;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.GameMode;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.sound.SoundEvent;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig;
import net.swofty.pvp.entity.projectile.CustomEntityProjectile;
import net.swofty.pvp.entity.projectile.ItemHoldingProjectile;
import net.swofty.pvp.utils.ViewUtil;
import net.swofty.type.bedwarsgame.entity.ThrownBridgeEgg;
import net.swofty.type.bedwarsgame.item.SimpleInteractableItem;
import net.swofty.type.bedwarsgame.shop.Currency;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.generic.data.datapoints.DatapointBedWarsHotbar;
import net.swofty.type.generic.utility.ScheduleUtility;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class BridgeEgg extends SimpleInteractableItem {
	public BridgeEgg() {
		super("bridge_egg", new ShopData("Bridge Egg", "This egg creates a bridge in its trail\nafter being thrown.",
			1, 1, Currency.EMERALD, DatapointBedWarsHotbar.HotbarItemType.UTILITY, 7));
	}

	@Override
	public ItemStack getBlandItem() {
		return ItemStack.of(Material.EGG);
	}

	@Override
	public void onItemUse(PlayerUseItemEvent event) {
		BedWarsPlayer player = (BedWarsPlayer) event.getPlayer();
		BedWarsMapsConfig.TeamKey teamKey = player.getTeamKey();
		if (teamKey == null) return;

		ItemStack stack = event.getItemStack();
		Block woolBlock = teamKey.bedMaterial().block();

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
		projectile.shootFromRotation(position.pitch(), position.yaw(), 0, 1.5, 1.0, 0.0);
		projectile.setInstance(Objects.requireNonNull(player.getInstance()), position.withView(projectile.getPosition()));

		Vec playerVel = player.getVelocity();
		projectile.setVelocity(projectile.getVelocity().add(playerVel.x(),
				player.isOnGround() ? 0.0D : playerVel.y(), playerVel.z()).mul(1.5));

		if (player.getGameMode() != GameMode.CREATIVE) {
			player.setItemInHand(event.getHand(), stack.withAmount(stack.amount() - 1));
		}

		ScheduleUtility.delay(
				() -> {
					if (projectile.isActive() && !projectile.isRemoved()) {
						projectile.remove();
					}
				},
				TaskSchedule.seconds(4)
		);
	}
}
