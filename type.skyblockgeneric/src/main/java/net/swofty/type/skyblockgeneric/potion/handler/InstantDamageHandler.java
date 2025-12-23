package net.swofty.type.skyblockgeneric.potion.handler;

import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.entity.damage.DamageType;
import net.swofty.type.skyblockgeneric.entity.mob.SkyBlockMob;
import net.swofty.type.skyblockgeneric.potion.PotionEffectType;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

/**
 * Handler for instant damage potions.
 * Damages entities, heals undead mobs.
 */
public class InstantDamageHandler implements PotionEffectHandler {

    @Override
    public void applyToPlayer(SkyBlockPlayer player, int level, long durationMs) {
        // Erm idk if this needs to be done
    }

    @Override
    public void applySplashToPlayer(SkyBlockPlayer player, int level, long durationMs, double distance) {
        // Damage potions do affect players when splashed
        double damageAmount = PotionEffectType.DAMAGE.getInstantDamageAmount(level);
        double modifier = getDistanceModifier(distance);
        damageAmount *= modifier;

        player.damage(new Damage(DamageType.MAGIC, null, null, null, (float) damageAmount));
    }

    @Override
    public void applyToMob(SkyBlockMob mob, int level, long durationMs, double distance) {
        double damageAmount = PotionEffectType.DAMAGE.getInstantDamageAmount(level);
        double modifier = getDistanceModifier(distance);
        damageAmount *= modifier;

        if (isUndeadMob(mob)) {
            // Damage heals undead
            float maxHealth = (float) mob.getAttributeValue(Attribute.MAX_HEALTH);
            float newHealth = Math.min(maxHealth, mob.getHealth() + (float) damageAmount);
            mob.setHealth(newHealth);
        } else {
            mob.damage(new Damage(DamageType.MAGIC, null, null, null, (float) damageAmount));
        }
    }

    @Override
    public boolean affectsMobs() {
        return true;
    }

    @Override
    public boolean affectsOtherPlayers() {
        return false;
    }

    private boolean isUndeadMob(SkyBlockMob mob) {
        String mobName = mob.getEntityType().name().toLowerCase();
        return mobName.contains("zombie") ||
               mobName.contains("skeleton") ||
               mobName.contains("wither") ||
               mobName.contains("phantom");
    }
}
