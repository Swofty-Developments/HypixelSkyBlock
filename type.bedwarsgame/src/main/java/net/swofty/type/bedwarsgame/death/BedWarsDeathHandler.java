package net.swofty.type.bedwarsgame.death;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig.TeamKey;
import net.swofty.type.bedwarsgame.game.v2.BedWarsGame;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// While this file is somewhat sloppy and definitely is in need of a refactor
// it also works pretty well. I haven't refactored it completely yet as I don't want
// to risk breaking the functionality completely.
public class BedWarsDeathHandler {
    public static BedWarsDeathResult calculateDeath(@NotNull BedWarsPlayer victim, @NotNull BedWarsGame game, boolean isVoidKill) {
        TeamKey teamKey = victim.getTeamKey();
        boolean isFinalKill = teamKey != null && !game.isBedAlive(teamKey);

        BedWarsPlayer recentAttacker = BedWarsCombatTracker.getRecentAttacker(victim);
        BedWarsDeathResult.Builder builder = BedWarsDeathResult.builder()
            .victim(victim)
            .isFinalKill(isFinalKill);

        Damage lastDamage = victim.getLastDamageSource();
        if (lastDamage == null) {
            if (isVoidKill) {
                return handleEnvironmentalDeath(builder, victim, true, recentAttacker);
            }
            return builder.deathType(BedWarsDeathType.GENERIC).build();
        }

        Entity attacker = lastDamage.getAttacker();
        DamageType damageType = lastDamage.getType().asValue();

        if (attacker instanceof BedWarsPlayer killer) {
            return handlePlayerKill(builder, killer, damageType, isVoidKill);
        }

        if (attacker != null) {
            return handleMobKill(builder, attacker);
        }

        return handleEnvironmentalDeath(builder, victim, false, recentAttacker);
    }

    private static BedWarsDeathResult handlePlayerKill(BedWarsDeathResult.Builder builder,
                                                       BedWarsPlayer killer,
                                                       DamageType damageType,
                                                       boolean isVoidKill) {
        BedWarsDeathType deathType;

        if (isVoidKill) {
            deathType = BedWarsDeathType.VOID_ASSISTED;
        } else if (damageType == DamageType.ARROW.asValue() || damageType == DamageType.TRIDENT.asValue()) {
            deathType = BedWarsDeathType.BOW;
        } else {
            deathType = BedWarsDeathType.GENERIC;
        }

        Material weaponUsed = null;
        ItemStack weapon = killer.getItemInMainHand();
        if (weapon != null && !weapon.isAir()) {
            weaponUsed = weapon.material();
        }

        return builder
            .deathType(deathType)
            .killer(killer)
            .weaponUsed(weaponUsed)
            .build();
    }

    private static BedWarsDeathResult handleMobKill(BedWarsDeathResult.Builder builder, Entity attacker) {
        return builder
            .deathType(BedWarsDeathType.ENTITY)
            .attackerEntity(attacker)
            .build();
    }

    private static BedWarsDeathResult handleEnvironmentalDeath(BedWarsDeathResult.Builder builder,
                                                               BedWarsPlayer victim,
                                                               boolean isVoid,
                                                               @Nullable BedWarsPlayer recentAttacker) {

        if (isVoid) {
            if (recentAttacker != null) {
                return builder
                    .deathType(BedWarsDeathType.VOID_ASSISTED)
                    .assistPlayer(recentAttacker)
                    .weaponUsed(BedWarsCombatTracker.getLastAttackerWeapon(victim))
                    .build();
            }
            return builder.deathType(BedWarsDeathType.VOID).build();
        }

        return builder.deathType(BedWarsDeathType.GENERIC).build();
    }

    public static Component createDeathMessage(@NotNull BedWarsDeathResult result) {
        String victimName = result.victim().getUsername();
        String victimColor = getTeamColor(result.victim());
        BedWarsDeathType deathType = result.deathType();

        // would not be slop if we had translations :)
        Component message = switch (deathType) {
            case VOID -> Component.text(victimColor + victimName).append(Component.text(
                " fell into the void.", NamedTextColor.GRAY
            ));
            case VOID_ASSISTED -> Component.text(victimColor + victimName).append(Component.text(
                    " was knocked into the void by ", NamedTextColor.GRAY
                )).append(Component.text(getTeamColor(result.assistPlayer()) + result.assistPlayer().getUsername()))
                .append(Component.text(".", NamedTextColor.GRAY));
            case GENERIC ->
                Component.text(victimColor + victimName).append(Component.text(" died.", NamedTextColor.GRAY));
            case GENERIC_ASSISTED -> Component.text(victimColor + victimName).append(Component.text(
                    " was killed by ", NamedTextColor.GRAY
                )).append(Component.text(getTeamColor(result.assistPlayer()) + result.assistPlayer().getUsername()))
                .append(Component.text(".", NamedTextColor.GRAY));
            case BOW -> Component.text(victimColor + victimName).append(Component.text(
                    " was shot by ", NamedTextColor.GRAY
                )).append(Component.text(getTeamColor(result.killer()) + result.killer().getUsername()))
                .append(Component.text(".", NamedTextColor.GRAY));
            case ENTITY -> Component.text(victimColor + victimName).append(Component.text(
                    " was slain by ", NamedTextColor.GRAY
                )).append(Component.text(getTeamColor(result.killer()) + result.killer().getUsername()))
                .append(Component.text(".", NamedTextColor.GRAY));
        };

        if (result.isFinalKill()) {
            message = message.append(MiniMessage.miniMessage().deserialize(" <aqua><bold>FINAL KILL!</bold></aqua>"));
        }
        return message;
    }

    private static String getTeamColor(@Nullable BedWarsPlayer player) {
        if (player == null) {
            return "§7";
        }

        TeamKey teamKey = player.getTeamKey();
        if (teamKey == null) {
            return "§7";
        }

        return teamKey.chatColor();
    }
}

