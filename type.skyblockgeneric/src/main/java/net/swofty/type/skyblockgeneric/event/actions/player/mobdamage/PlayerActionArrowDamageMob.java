package net.swofty.type.skyblockgeneric.event.actions.player.mobdamage;

import net.minestom.server.entity.damage.Damage;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.event.entity.projectile.ProjectileCollideWithEntityEvent;
import net.swofty.commons.skyblock.statistics.ItemStatistic;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.skyblockgeneric.entity.ArrowEntityImpl;
import net.swofty.type.skyblockgeneric.entity.mob.SkyBlockMob;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.skyblockgeneric.user.statistics.PlayerStatistics;
import net.swofty.type.skyblockgeneric.utility.DamageIndicator;

import java.util.Map;

public class PlayerActionArrowDamageMob implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.ALL, requireDataLoaded = false)
    public void run(ProjectileCollideWithEntityEvent event) {
        ArrowEntityImpl arrow;
        if (event.getEntity() instanceof ArrowEntityImpl arrowEntity)
            arrow = arrowEntity;
        else return;

        SkyBlockMob collidedWith;
        if (event.getTarget() instanceof SkyBlockMob mob)
            collidedWith = mob;
        else return;

        SkyBlockPlayer shooter;
        if (arrow.getShooter() instanceof SkyBlockPlayer player)
            shooter = player;
        else return;

        SkyBlockItem arrowItem = arrow.getArrowItem();
        ItemStatistics entityStats = mob.getStatistics();
        ItemStatistics playerStats = shooter.getStatistics().allStatistics();

        // Add the arrow's statistics to the player's statistics
        ItemStatistics.add(playerStats, arrowItem.getAttributeHandler().getStatistics());

        Map.Entry<Double, Boolean> hit = PlayerStatistics.runPrimaryDamageFormula(playerStats, entityStats);
        double damage = hit.getKey();
        boolean critical = hit.getValue();

        new DamageIndicator()
                .damage((float) damage)
                .pos(collidedWith.getPosition())
                .critical(critical)
                .display(collidedWith.getInstance());

        collidedWith.damage(new Damage(DamageType.PLAYER_ATTACK, player, player, player.getPosition(), (float) damage));

        double ferocity = shooter.getStatistics().allStatistics().getOverall(ItemStatistic.FEROCITY);

        int extraAttacks = (int) (ferocity / 100);
        double extraAttackChance = (ferocity % 100) / 100.0;

        // Extra attacks that are guaranteed because ferocity overflowed 100
        for (int i = 0; i < extraAttacks; i++) {
            collidedWith.damage(new Damage(DamageType.PLAYER_ATTACK, player, player, player.getPosition(), (float) damage));
        }

        // Extra attacks that have a chance to occur based on the remaining ferocity
        if (Math.random() < extraAttackChance) {
            collidedWith.damage(new Damage(DamageType.PLAYER_ATTACK, player, player, player.getPosition(), (float) damage));
        }
    }
}