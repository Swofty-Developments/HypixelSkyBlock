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
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.handlers.pet.dsl.PetEvent;
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

            PlayerDamagedByMobValueUpdateEvent valueEvent = new PlayerDamagedByMobValueUpdateEvent(
                    (SkyBlockPlayer) event.getTarget(), damageDealt.getKey().floatValue(), mob);
            SkyBlockValueEvent.callValueUpdateEvent(valueEvent);

            // Handle damage event pets — they may further modify the damage taken
            SkyBlockPlayer damagedPlayer = (SkyBlockPlayer) event.getTarget();
            float finalDamage = (float) valueEvent.getValue();
            SkyBlockItem pet = damagedPlayer.getPetData().getEnabledPet();
            PetEvent.DamagedByMob damageEvent = damagedPlayer.getPetData()
                    .dispatch(new PetEvent.DamagedByMob(damagedPlayer, pet, mob, finalDamage));
            finalDamage = (float) damageEvent.damage();

            damagedPlayer.damage(new EntityDamage(mob, finalDamage));

            if (mob instanceof SlayerBossMob slayerBoss) {
                slayerBoss.getAbility().onMeleeHit(slayerBoss, (SkyBlockPlayer) event.getTarget());
            }

            new DamageIndicator()
                    .damage(finalDamage)
                    .pos(event.getTarget().getPosition())
                    .critical(damageDealt.getValue())
                    .display(event.getTarget().getInstance());
        }
    }

}
