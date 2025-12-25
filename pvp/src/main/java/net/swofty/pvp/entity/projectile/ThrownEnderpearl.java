package net.swofty.pvp.entity.projectile;

import net.swofty.pvp.feature.fall.FallFeature;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.entity.metadata.item.ThrownEnderPearlMeta;
import net.minestom.server.item.ItemStack;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ThreadLocalRandom;

public class ThrownEnderpearl extends CustomEntityProjectile implements ItemHoldingProjectile {
	private Pos prevPos = Pos.ZERO;
	
	private final FallFeature fallFeature;
	
	public ThrownEnderpearl(@Nullable Entity shooter, FallFeature fallFeature) {
		super(shooter, EntityType.ENDER_PEARL);
		this.fallFeature = fallFeature;
	}
	
	private void teleportOwner() {
		Pos position = prevPos;
		ThreadLocalRandom random = ThreadLocalRandom.current();
		
		for (int i = 0; i < 32; i++) {
			sendPacketToViewersAndSelf(new ParticlePacket(
					Particle.PORTAL, false, false,
					position.x(), position.y() + random.nextDouble() * 2, position.z(),
					(float) random.nextGaussian(), 0.0F, (float) random.nextGaussian(),
					0, 1
			));
		}
		
		if (isRemoved()) return;
		
		Entity shooter = getShooter();
		if (shooter != null) {
			Pos shooterPos = shooter.getPosition();
			position = position.withPitch(shooterPos.pitch()).withYaw(shooterPos.yaw());
		}
		
		if (shooter instanceof Player player) {
			if (player.isOnline() && player.getInstance() == getInstance()
					&& player.getPlayerMeta().getBedInWhichSleepingPosition() == null) {
				if (player.getVehicle() != null) {
					player.getVehicle().removePassenger(player);
				}
				
				player.teleport(position);
				fallFeature.resetFallDistance(player);
				
				player.damage(DamageType.FALL, 5.0F);
			}
		} else if (shooter != null) {
			shooter.teleport(position);
		}
	}
	
	@Override
	public boolean onHit(Entity entity) {
		((LivingEntity) entity).damage(new Damage(DamageType.THROWN, this, getShooter(), null, 0));
		
		teleportOwner();
		return true;
	}
	
	@Override
	public boolean onStuck() {
		teleportOwner();
		return true;
	}
	
	@Override
	public void tick(long time) {
		Entity shooter = getShooter();
		if (shooter instanceof Player && ((Player) shooter).isDead()) {
			remove();
		} else {
			prevPos = getPosition();
			super.tick(time);
		}
	}
	
	@Override
	public void setItem(@NotNull ItemStack item) {
		((ThrownEnderPearlMeta) getEntityMeta()).setItem(item);
	}
}
