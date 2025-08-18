package net.swofty.pvp.entity.projectile;

import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.entity.metadata.item.ThrownEggMeta;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ThrownEgg extends CustomEntityProjectile implements ItemHoldingProjectile {
	
	public ThrownEgg(@Nullable Entity shooter) {
		super(shooter, EntityType.EGG);
	}
	
	@Override
	public boolean onHit(Entity entity) {
		triggerStatus((byte) 3); // Egg particles
		
		((LivingEntity) entity).damage(new Damage(DamageType.THROWN, this, getShooter(), null, 0));
		
		return true;
	}
	
	@Override
	public boolean onStuck() {
		triggerStatus((byte) 3); // Egg particles
		
		return true;
	}
	
	@Override
	public void setItem(@NotNull ItemStack item) {
		((ThrownEggMeta) getEntityMeta()).setItem(item);
	}
}
