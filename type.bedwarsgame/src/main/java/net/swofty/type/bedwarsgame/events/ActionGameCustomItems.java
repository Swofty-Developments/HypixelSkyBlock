package net.swofty.type.bedwarsgame.events;

import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.event.entity.projectile.ProjectileCollideWithBlockEvent;
import net.minestom.server.event.entity.projectile.ProjectileCollideWithEntityEvent;
import net.minestom.server.event.item.PlayerFinishItemUseEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.event.player.PlayerUseItemOnBlockEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.tag.Tag;
import net.swofty.pvp.projectile.entities.FireballProjectile;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.game.Game;
import net.swofty.type.bedwarsgame.game.GameStatus;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;

import java.util.UUID;

public class ActionGameCustomItems implements HypixelEventClass {

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
		TypeBedWarsGameLoader.getItemHandler().onItemUseOnBlock(event);
	}

	@HypixelEvent(node = EventNodes.ALL, requireDataLoaded = true)
	public void run(PlayerFinishItemUseEvent event) {
		TypeBedWarsGameLoader.getItemHandler().onItemFinishUse(event);
	}

	@HypixelEvent(node = EventNodes.ALL, requireDataLoaded = true)
	public void run(PlayerUseItemEvent event) {
		TypeBedWarsGameLoader.getItemHandler().onItemUse(event);
	}

}
