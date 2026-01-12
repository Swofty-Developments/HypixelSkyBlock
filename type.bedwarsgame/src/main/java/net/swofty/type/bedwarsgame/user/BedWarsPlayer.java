package net.swofty.type.bedwarsgame.user;

import net.minestom.server.ServerFlag;
import net.minestom.server.collision.Aerodynamics;
import net.minestom.server.collision.PhysicsResult;
import net.minestom.server.collision.PhysicsUtils;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.event.entity.EntityVelocityEvent;
import net.minestom.server.instance.Chunk;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;
import net.minestom.server.potion.PotionEffect;
import net.minestom.server.potion.TimedPotion;
import net.minestom.server.tag.Tag;
import net.minestom.server.utils.chunk.ChunkUtils;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig;
import net.swofty.pvp.player.CombatPlayer;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.game.Game;
import net.swofty.type.generic.data.datapoints.DatapointLeaderboardLong;
import net.swofty.type.generic.data.handlers.BedWarsDataHandler;
import net.swofty.type.generic.user.HypixelPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

/**
 * Represents a player in the BedWars game mode.
 * This class extends HypixelPlayer and implements CombatPlayer for combat-related functionalities.
 * CombatPlayer implementation based on <a href="https://github.com/TogAr2/MinestomPvP/blob/master/src/main/java/io/github/togar2/pvp/player/CombatPlayerImpl.java">CombatPlayerImpl</a>
 */
@SuppressWarnings("UnstableApiUsage")
public class BedWarsPlayer extends HypixelPlayer implements CombatPlayer {

	private boolean velocityUpdate = false;
	private PhysicsResult previousPhysicsResult = null;

	public BedWarsPlayer(@NotNull PlayerConnection playerConnection, @NotNull GameProfile gameProfile) {
		super(playerConnection, gameProfile);
		getAttribute(Attribute.ATTACK_DAMAGE).setBaseValue(1.0);
	}

	public BedWarsDataHandler getBedWarsDataHandler() {
		return BedWarsDataHandler.getUser(this.getUuid());
	}

	@Nullable
	public String getTeamName() {
		return getTag(Tag.String("team"));
	}

	@Nullable
	public BedWarsMapsConfig.TeamKey getTeamKey() {
		return BedWarsMapsConfig.TeamKey.valueOf(getTeamName());
	}

	public Game getGame() {
		String gameId = getTag(Tag.String("gameId"));
		return TypeBedWarsGameLoader.getGameById(gameId);
	}

	public void xp(ExperienceCause cause) {
		sendMessage("§b+" + cause.getExperience() + " Bed Wars XP (" + cause.getFormattedName() + ")");
		DatapointLeaderboardLong dp = getBedWarsDataHandler().get(BedWarsDataHandler.Data.EXPERIENCE, DatapointLeaderboardLong.class);
		dp.setValue(dp.getValue() + cause.getExperience());
	}

	public void xp(ExperienceCause cause, long units) {
		long amount = cause.calculateXp(units);
		sendMessage("§b+" + amount + " Bed Wars XP (" + cause.getFormattedName() + ")");
		DatapointLeaderboardLong dp = getBedWarsDataHandler().get(BedWarsDataHandler.Data.EXPERIENCE, DatapointLeaderboardLong.class);
		dp.setValue(dp.getValue() + amount);
	}


	@Override
	public void setVelocity(@NotNull Vec velocity) {
		EntityVelocityEvent entityVelocityEvent = new EntityVelocityEvent(this, velocity);
		EventDispatcher.callCancellable(entityVelocityEvent, () -> {
			this.velocity = entityVelocityEvent.getVelocity();
			velocityUpdate = true;
		});
	}

	@Override
	public void setVelocityNoUpdate(Function<Vec, Vec> function) {
		velocity = function.apply(velocity);
	}

	@Override
	public void sendImmediateVelocityUpdate() {
		if (velocityUpdate) {
			velocityUpdate = false;
			sendPacketToViewersAndSelf(getVelocityPacket());
		}
	}

	public boolean isOnGroundAfterTicks(int ticks) {
		if (vehicle != null) return false;

		final double tps = ServerFlag.SERVER_TICKS_PER_SECOND;
		Vec velocity = this.velocity.div(tps);
		Pos position = this.position;

		// Slow falling effect
		Aerodynamics aerodynamics = getAerodynamics();
		if (velocity.y() < 0 && hasEffect(PotionEffect.SLOW_FALLING))
			aerodynamics = aerodynamics.withGravity(0.01);

		// Do movementTick() calculations for the given amount of ticks
		PhysicsResult prevPhysicsResult = previousPhysicsResult;
		for (int i = 0; i < ticks; i++) {
			PhysicsResult physicsResult = PhysicsUtils.simulateMovement(position, velocity, boundingBox,
					instance.getWorldBorder(), instance, aerodynamics, hasNoGravity(), hasPhysics, onGround, isFlying(), prevPhysicsResult);
			prevPhysicsResult = physicsResult;

			if (physicsResult.isOnGround()) return true;

			velocity = physicsResult.newVelocity();
			position = physicsResult.newPosition();

			// Levitation effect
			TimedPotion levitation = getEffect(PotionEffect.LEVITATION);
			if (levitation != null) {
				velocity = velocity.withY(
						((0.05 * (double) (levitation.potion().amplifier() + 1) - (velocity.y())) * 0.2)
				);
			}
		}

		return false;
	}

	@Override
	protected void movementTick() {
		this.gravityTickCount = onGround ? 0 : gravityTickCount + 1;
		if (vehicle != null) return;

		final double tps = ServerFlag.SERVER_TICKS_PER_SECOND;

		// Slow falling effect
		Aerodynamics aerodynamics = getAerodynamics();
		if (velocity.y() < 0 && hasEffect(PotionEffect.SLOW_FALLING))
			aerodynamics = aerodynamics.withGravity(0.01);

		PhysicsResult physicsResult = PhysicsUtils.simulateMovement(position, velocity.div(tps), boundingBox,
				instance.getWorldBorder(), instance, aerodynamics, hasNoGravity(), hasPhysics, onGround, isFlying(), previousPhysicsResult);
		this.previousPhysicsResult = physicsResult;

		Chunk finalChunk = ChunkUtils.retrieve(instance, currentChunk, physicsResult.newPosition());
		if (!ChunkUtils.isLoaded(finalChunk)) return;

		velocity = physicsResult.newVelocity().mul(tps);
		//onGround = physicsResult.isOnGround();

		// Levitation effect
		TimedPotion levitation = getEffect(PotionEffect.LEVITATION);
		if (levitation != null) {
			velocity = velocity.withY(
					((0.05 * (double)
							(levitation.potion().amplifier() + 1)
							- (velocity.y() / tps)) * 0.2) * tps
			);
		}

		//TODO
		//if (!PlayerUtils.isSocketClient(this)) {
		//	refreshPosition(physicsResult.newPosition(), true, true);
		//}
		sendImmediateVelocityUpdate();
	}
}
