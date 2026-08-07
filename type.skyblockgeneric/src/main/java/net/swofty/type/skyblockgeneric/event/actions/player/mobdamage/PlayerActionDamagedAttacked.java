package net.swofty.type.skyblockgeneric.event.actions.player.mobdamage;

import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.damage.EntityDamage;
import net.minestom.server.event.entity.EntityAttackEvent;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.event.phase.EventPhase;
import net.swofty.type.generic.event.phase.PhasedEvent;
import net.swofty.type.skyblockgeneric.entity.mob.SkyBlockMob;
import net.swofty.type.skyblockgeneric.entity.mob.mobs.slayer.SlayerBossMob;
import net.swofty.type.skyblockgeneric.event.value.SkyBlockValueEvent;
import net.swofty.type.skyblockgeneric.event.value.events.PlayerDamagedByMobValueUpdateEvent;
import net.swofty.type.skyblockgeneric.hunting.AttributeEffectService;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.skyblockgeneric.user.statistics.PlayerStatistics;
import net.swofty.type.skyblockgeneric.utility.DamageIndicator;

import java.util.Map;

public class PlayerActionDamagedAttacked implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.ENTITY, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
    public void run(EntityAttackEvent event) {
        if (!event.getTarget().getEntityType().equals(EntityType.PLAYER)) return;

        if (event.getEntity() instanceof SkyBlockMob mob) {
            if (mob.getLastAttack() + mob.damageCooldown() > System.currentTimeMillis()) return;
            mob.setLastAttack(System.currentTimeMillis());

            ItemStatistics mobStatistics = mob.getStatistics();
            ItemStatistics playerStatistics = ((SkyBlockPlayer) event.getTarget()).getStatistics().allStatistics();

            Map.Entry<Double, Boolean> damageDealt =
                    PlayerStatistics.runPrimaryDamageFormula(mobStatistics, playerStatistics);

            SkyBlockPlayer player = (SkyBlockPlayer) event.getTarget();
            double baseDefense = playerStatistics.getOverall(net.swofty.commons.skyblock.statistics.ItemStatistic.DEFENSE);
            double resistance = AttributeEffectService.resistanceDefense(player.getHuntingData(), mob);
            double resistanceMultiplier = (100D + Math.max(0, baseDefense))
                    / (100D + Math.max(0, baseDefense + resistance));
            PlayerDamagedByMobValueUpdateEvent valueEvent = new PlayerDamagedByMobValueUpdateEvent(
                    player, (float) (damageDealt.getKey() * resistanceMultiplier), mob);
            SkyBlockValueEvent.callValueUpdateEvent(valueEvent);

            ((SkyBlockPlayer) event.getTarget()).damage(new EntityDamage(mob, (float) valueEvent.getValue()));

            if (mob instanceof SlayerBossMob slayerBoss) {
                slayerBoss.getAbility().onMeleeHit(slayerBoss, (SkyBlockPlayer) event.getTarget());
            }

            new DamageIndicator()
                    .damage((float) valueEvent.getValue())
                    .pos(event.getTarget().getPosition())
                    .critical(damageDealt.getValue())
                    .display(event.getTarget().getInstance());
        }
    }

}
