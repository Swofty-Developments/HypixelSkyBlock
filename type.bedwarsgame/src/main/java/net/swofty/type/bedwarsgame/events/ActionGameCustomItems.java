package net.swofty.type.bedwarsgame.events;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.*;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.entity.attribute.AttributeModifier;
import net.minestom.server.entity.attribute.AttributeOperation;
import net.minestom.server.event.entity.projectile.ProjectileCollideWithBlockEvent;
import net.minestom.server.event.entity.projectile.ProjectileCollideWithEntityEvent;
import net.minestom.server.event.item.PlayerFinishItemUseEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.event.player.PlayerUseItemOnBlockEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import net.minestom.server.sound.SoundEvent;
import net.minestom.server.tag.Tag;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.pvp.entity.projectile.CustomEntityProjectile;
import net.swofty.pvp.entity.projectile.ItemHoldingProjectile;
import net.swofty.pvp.projectile.entities.FireballProjectile;
import net.swofty.pvp.utils.ViewUtil;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.entity.ThrownBridgeEgg;
import net.swofty.type.bedwarsgame.game.Game;
import net.swofty.type.bedwarsgame.game.GameStatus;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class ActionGameCustomItems implements HypixelEventClass {

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

	private static void handleFireballExplosion(FireballProjectile fireball) {
		final Instance instance = fireball.getInstance();
		if (instance == null) {
			fireball.remove();
			return;
		}

		final Player shooter = (Player) fireball.getShooter();
		if (shooter == null) {
			fireball.remove();
			return;
		}

		String gameId = shooter.getTag(Tag.String("gameId"));
		Game game = TypeBedWarsGameLoader.getGameById(gameId);
		if (game == null || game.getGameStatus() != GameStatus.IN_PROGRESS) {
			fireball.remove();
			return;
		}

		final Pos explosionPos = fireball.getPosition();
		final UUID shooterUuid = shooter.getUuid();
		final long msb = shooterUuid.getMostSignificantBits();
		final long lsb = shooterUuid.getLeastSignificantBits();
		final int[] shooterUuidInts = new int[]{(int) (msb >> 32), (int) msb, (int) (lsb >> 32), (int) lsb};

		instance.explode(
				(float) explosionPos.x(), (float) explosionPos.y(), (float) explosionPos.z(),
				4f,
				CompoundBinaryTag.builder()
						.putString("requiredTag", TypeBedWarsGameLoader.PLAYER_PLACED_TAG.getKey())
						.putIntArray("blacklist", new int[]{Block.END_STONE.id(), Block.OBSIDIAN.id(), Block.BEDROCK.id()})
						.putIntArray("shooter", shooterUuidInts)
						.putString("ignore", "team")
						.build()
		);
		fireball.remove();
	}

	@HypixelEvent(node = EventNodes.ALL, requireDataLoaded = true)
	public void run(ProjectileCollideWithBlockEvent event) {
		if (event.getEntity().getEntityType() == EntityType.FIREBALL) {
			handleFireballExplosion((FireballProjectile) event.getEntity());
		}
	}

	@HypixelEvent(node = EventNodes.ALL, requireDataLoaded = true)
	public void run(ProjectileCollideWithEntityEvent event) {
		if (event.getEntity().getEntityType() != EntityType.FIREBALL) {
			return;
		}
		final FireballProjectile fireball = (FireballProjectile) event.getEntity();
		final Entity target = event.getTarget();

		if (target.equals(fireball.getShooter())) {
			return; // Don't explode on self
		}

		if (target instanceof Player targetPlayer) {
			final Player shooter = (Player) fireball.getShooter();
			if (shooter != null) {
				String shooterTeam = shooter.getTag(Tag.String("team"));
				String targetTeam = targetPlayer.getTag(Tag.String("team"));
				if (shooterTeam != null && shooterTeam.equals(targetTeam)) {
					return;
				}
			}
		}

		handleFireballExplosion(fireball);
	}

	@HypixelEvent(node = EventNodes.ALL, requireDataLoaded = true)
	public void run(PlayerUseItemOnBlockEvent event) {
		Player player = event.getPlayer();
		// it's pretty much unnecessary to check if it's actually for something else because you can't really gather them.
		if (event.getItemStack().material() == Material.FIRE_CHARGE) {
			handleFireball(player, event.getHand());
			return;
		}
	}

	@HypixelEvent(node = EventNodes.ALL, requireDataLoaded = true)
	public void run(PlayerFinishItemUseEvent event) {
		Player player = event.getPlayer();
		player.setHealth((float) (player.getHealth() + 4.0));
		player.setAdditionalHearts((float) (player.getAdditionalHearts() + 2.0));
		player.addEffect(
				new Potion(PotionEffect.REGENERATION, 1, 20 * 20) // regen for 20 seconds - not sure if hypixel
		);
		player.addEffect(
				new Potion(PotionEffect.RESISTANCE, 0, 60 * 20) // resistance for 1 minute - not sure if hypixel
		);
	}

	@HypixelEvent(node = EventNodes.ALL, requireDataLoaded = true)
	public void run(PlayerUseItemEvent event) {
		Player player = event.getPlayer();
		if (event.getItemStack().material() == Material.FIRE_CHARGE) {
			handleFireball(player, event.getHand());
			return;
		}
		if (event.getItemStack().material() != Material.EGG) return;

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

		MinecraftServer.getSchedulerManager().buildTask(() -> {
			if (projectile.isActive() && !projectile.isRemoved()) {
				projectile.remove();
			}
		}).delay(TaskSchedule.seconds(4)).schedule();
	}

	private void handleFireball(Player player, PlayerHand hand) {
		player.setItemInHand(hand, player.getItemInHand(hand).withAmount(player.getItemInHand(hand).amount() - 1));
		new FireballProjectile(EntityType.FIREBALL, player).shoot(player.getPosition().add(0, player.getEyeHeight(), 0).asVec(), 1, 1);
		player.playSound(Sound.sound(Key.key("minecraft:entity.ghast.shoot"), Sound.Source.PLAYER, 1f, 1f), Sound.Emitter.self());
		player.getAttribute(Attribute.MOVEMENT_SPEED).addModifier(new AttributeModifier(Key.key("bw:fireball"), -0.3, AttributeOperation.ADD_MULTIPLIED_TOTAL));
		MinecraftServer.getSchedulerManager().buildTask(() -> player.getAttribute(Attribute.MOVEMENT_SPEED).removeModifier(Key.key("bw:fireball"))).delay(TaskSchedule.seconds(2)).schedule();
	}

}
