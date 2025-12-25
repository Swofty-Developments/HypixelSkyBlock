package net.swofty.pvp.damage.combat;

import net.swofty.pvp.damage.DamageTypeInfo;
import net.swofty.pvp.utils.EntityUtil;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.damage.Damage;
import org.jetbrains.annotations.Nullable;

public record CombatEntry(Damage damage, @Nullable String fallLocation, double fallDistance) {
	
	public String getMessageFallLocation() {
		return fallLocation == null ? "generic" : fallLocation;
	}
	
	public double getFallDistance() {
		DamageTypeInfo info = DamageTypeInfo.of(damage.getType());
		return info.outOfWorld() ? Double.MAX_VALUE : fallDistance;
	}
	
	public boolean isCombat() {
		return damage.getAttacker() instanceof LivingEntity;
	}
	
	public @Nullable Entity getAttacker() {
		return damage.getAttacker();
	}
	
	public @Nullable Component getAttackerName() {
		return getAttacker() == null ? null : EntityUtil.getName(getAttacker());
	}
}
