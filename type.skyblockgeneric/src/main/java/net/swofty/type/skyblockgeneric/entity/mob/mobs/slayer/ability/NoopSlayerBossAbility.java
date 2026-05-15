package net.swofty.type.skyblockgeneric.entity.mob.mobs.slayer.ability;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minestom.server.entity.damage.Damage;
import net.swofty.type.skyblockgeneric.entity.mob.mobs.slayer.SlayerBossMob;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class NoopSlayerBossAbility implements SlayerBossAbility {
    static final NoopSlayerBossAbility INSTANCE = new NoopSlayerBossAbility();

    @Override public void onSpawn(SlayerBossMob boss) {}
    @Override public float modifyIncomingDamage(SlayerBossMob boss, Damage damage, float amount) { return amount; }
    @Override public void onDamaged(SlayerBossMob boss, Damage damage, float appliedDamage) {}
    @Override public void onMeleeHit(SlayerBossMob boss, SkyBlockPlayer target) {}
    @Override public void onDeath(SlayerBossMob boss) {}
}
