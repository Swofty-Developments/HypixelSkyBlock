package net.swofty.type.skyblockgeneric.utility;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.instance.Instance;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.type.skyblockgeneric.entity.mob.SkyBlockMob;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

public final class AttackService {
    public static final int EXTRA_HIT_DELAY_TICKS = 5;
    public static final double MAX_MELEE_FEROCITY_DISTANCE = 6D;
    public static final double MAX_FEROCITY = 500D;

    private AttackService() {
    }

    public static boolean applyHit(SkyBlockPlayer attacker, SkyBlockMob target, float damage, boolean critical) {
        if (target.isRemoved() || target.isDead()) return false;

        Instance instance = target.getInstance();
        Pos position = target.getPosition();
        boolean applied = target.damage(new Damage(
                DamageType.PLAYER_ATTACK,
                attacker,
                attacker,
                attacker.getPosition(),
                damage
        ));
        if (!applied) return false;

        String entityName = target.getEntityType().name().toLowerCase(Locale.ROOT).replace("minecraft:", "");
        attacker.playSound(
                Sound.sound(Key.key("entity." + entityName + ".hurt"), Sound.Source.PLAYER, 1f, 1f),
                Sound.Emitter.self()
        );
        if (instance != null) {
            new DamageIndicator()
                    .damage(damage)
                    .pos(position)
                    .critical(critical)
                    .display(instance);
        }
        return true;
    }

    public static void scheduleExtraHits(SkyBlockPlayer attacker, SkyBlockMob target,
                                         float damage, boolean critical, double ferocity) {
        double effectiveFerocity = Math.max(0D, Math.min(MAX_FEROCITY, ferocity));
        int extraHits = calculateExtraHits(effectiveFerocity, ThreadLocalRandom.current().nextDouble(100D));

        for (int hit = 0; hit < extraHits; hit++) {
            int delay = EXTRA_HIT_DELAY_TICKS * (hit + 1);
            MinecraftServer.getSchedulerManager().scheduleTask(
                    () -> applyHit(attacker, target, damage, critical),
                    TaskSchedule.tick(delay),
                    TaskSchedule.stop()
            );
        }
    }

    static int calculateExtraHits(double ferocity, double chanceRoll) {
        double effectiveFerocity = Math.max(0D, Math.min(MAX_FEROCITY, ferocity));
        int extraHits = (int) (effectiveFerocity / 100D);
        double remainingChance = effectiveFerocity % 100D;
        if (remainingChance > 0D && chanceRoll < remainingChance) {
            extraHits++;
        }
        return extraHits;
    }
}
