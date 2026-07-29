package net.swofty.type.skyblockgeneric.entity.mob.mobs.slayer.ability;

import net.minestom.server.entity.damage.Damage;
import net.swofty.type.skyblockgeneric.entity.mob.mobs.slayer.SlayerBossMob;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public interface SlayerBossAbility {
    void onSpawn(SlayerBossMob boss);

    float modifyIncomingDamage(SlayerBossMob boss, Damage damage, float amount);

    void onDamaged(SlayerBossMob boss, Damage damage, float appliedDamage);

    void onMeleeHit(SlayerBossMob boss, SkyBlockPlayer target);

    void onDeath(SlayerBossMob boss);

    static SlayerBossAbility none() {
        return NoopSlayerBossAbility.INSTANCE;
    }
}
