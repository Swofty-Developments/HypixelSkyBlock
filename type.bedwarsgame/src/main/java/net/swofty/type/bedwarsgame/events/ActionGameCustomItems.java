package net.swofty.type.bedwarsgame.events;

import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.event.entity.projectile.ProjectileCollideWithBlockEvent;
import net.minestom.server.event.entity.projectile.ProjectileCollideWithEntityEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.event.item.PlayerFinishItemUseEvent;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.event.player.PlayerUseItemOnBlockEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig;
import net.swofty.pvp.projectile.entities.FireballProjectile;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.game.v2.BedWarsGame;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.game.game.GameState;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.event.phase.EventPhase;
import net.swofty.type.generic.event.phase.PhasedEvent;

import java.util.UUID;

public class ActionGameCustomItems implements HypixelEventClass {

	private static void handleFireballExplosion(FireballProjectile fireball) {
		final Instance instance = fireball.getInstance();
		if (instance == null) {
			fireball.remove();
			return;
		}

		final BedWarsPlayer shooter = (BedWarsPlayer) fireball.getShooter();
		if (shooter == null) {
			fireball.remove();
			return;
		}

		BedWarsGame game = shooter.getGame();
		if (game == null || game.getState() != GameState.IN_PROGRESS) {
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

	@PhasedEvent(node = EventNodes.ALL, requireDataLoaded = true, phase = EventPhase.GAMEPLAY)
	public void run(ProjectileCollideWithBlockEvent event) {
		if (event.getEntity().getEntityType() == EntityType.FIREBALL) {
			handleFireballExplosion((FireballProjectile) event.getEntity());
		}
	}

	@PhasedEvent(node = EventNodes.ALL, requireDataLoaded = true, phase = EventPhase.GAMEPLAY)
	public void run(ProjectileCollideWithEntityEvent event) {
		if (event.getEntity().getEntityType() != EntityType.FIREBALL) {
			return;
		}
		final FireballProjectile fireball = (FireballProjectile) event.getEntity();
		final Entity target = event.getTarget();

		if (target.equals(fireball.getShooter())) {
			return; // Don't explode on self
		}

		if (target instanceof BedWarsPlayer targetPlayer) {
			final BedWarsPlayer shooter = (BedWarsPlayer) fireball.getShooter();
			if (shooter != null) {
				BedWarsMapsConfig.TeamKey shooterTeam = shooter.getTeamKey();
				BedWarsMapsConfig.TeamKey targetTeam = targetPlayer.getTeamKey();
				if (shooterTeam != null && shooterTeam.equals(targetTeam)) {
					return;
				}
			}
		}

		handleFireballExplosion(fireball);
	}

	@PhasedEvent(node = EventNodes.ALL, requireDataLoaded = true, phase = EventPhase.GAMEPLAY)
	public void run(PlayerUseItemOnBlockEvent event) {
		TypeBedWarsGameLoader.getItemHandler().onItemUseOnBlock(event);
	}

	@PhasedEvent(node = EventNodes.ALL, requireDataLoaded = true, phase = EventPhase.GAMEPLAY)
	public void run(PlayerFinishItemUseEvent event) {
		TypeBedWarsGameLoader.getItemHandler().onItemFinishUse(event);
	}

	@PhasedEvent(node = EventNodes.ALL, requireDataLoaded = true, phase = EventPhase.GAMEPLAY)
	public void run(PlayerUseItemEvent event) {
		if (event.getPlayer() instanceof BedWarsPlayer player
			&& player.getGame() != null
			&& player.getGame().getGameType().isOneBlock()
			&& player.getGame().getState() == GameState.IN_PROGRESS
			&& event.getItemStack().material() == Material.VILLAGER_SPAWN_EGG) {
			event.setCancelled(true);
			ItemStack held = event.getItemStack();
			player.setItemInMainHand(held.amount() > 1 ? held.withAmount(held.amount() - 1) : ItemStack.AIR);
			player.getGame().getWorldManager().spawnTemporaryItemShop(player.getPosition());
			return;
		}
		TypeBedWarsGameLoader.getItemHandler().onItemUse(event);
	}

	@PhasedEvent(node = EventNodes.ALL, requireDataLoaded = true, phase = EventPhase.GAMEPLAY)
	public void run(PlayerBlockPlaceEvent event) {
		TypeBedWarsGameLoader.getItemHandler().onBlockPlace(event);
	}

	// InventoryClickItem
	@PhasedEvent(node = EventNodes.ALL, requireDataLoaded = true)
	public void run(InventoryPreClickEvent event) {
		TypeBedWarsGameLoader.getItemHandler().onInventoryClick(event);
	}

}
