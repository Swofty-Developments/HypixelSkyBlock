package net.swofty.type.skyblockgeneric.event.actions.player.mobdamage;

import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.damage.EntityDamage;
import net.minestom.server.event.entity.EntityAttackEvent;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.skyblockgeneric.entity.mob.SkyBlockMob;
import net.swofty.type.skyblockgeneric.event.value.SkyBlockValueEvent;
import net.swofty.type.skyblockgeneric.event.value.events.PlayerDamagedByMobValueUpdateEvent;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.skyblockgeneric.user.statistics.PlayerStatistics;
import net.swofty.type.skyblockgeneric.utility.DamageIndicator;

import java.util.Map;

public class PlayerActionDamagedAttacked implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.ENTITY, requireDataLoaded = false)
    public void run(EntityAttackEvent event) {
        if (!event.getTarget().getEntityType().equals(EntityType.PLAYER)) return;

        if (event.getEntity() instanceof SkyBlockMob mob) {
            if (mob.getLastAttack() + mob.damageCooldown() > System.currentTimeMillis()) return;
            mob.setLastAttack(System.currentTimeMillis());

            ItemStatistics mobStatistics = mob.getStatistics();
            ItemStatistics playerStatistics = ((SkyBlockPlayer) event.getTarget()).getStatistics().allStatistics();

            Map.Entry<Double, Boolean> damageDealt =
                    PlayerStatistics.runPrimaryDamageFormula(mobStatistics, playerStatistics);

            PlayerDamagedByMobValueUpdateEvent valueEvent = new PlayerDamagedByMobValueUpdateEvent(
                    (SkyBlockPlayer) event.getTarget(), damageDealt.getKey().floatValue(), mob);
            SkyBlockValueEvent.callValueUpdateEvent(valueEvent);

            ((SkyBlockPlayer) event.getTarget()).damage(new EntityDamage(mob, (float) valueEvent.getValue()));

            new DamageIndicator()
                    .damage((float) valueEvent.getValue())
                    .pos(event.getTarget().getPosition())
                    .critical(damageDealt.getValue())
                    .display(event.getTarget().getInstance());
        }
    }

}
