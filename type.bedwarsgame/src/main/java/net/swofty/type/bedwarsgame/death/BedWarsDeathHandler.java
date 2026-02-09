package net.swofty.type.bedwarsgame.death;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.minimessage.translation.Argument;
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
import org.jspecify.annotations.NonNull;

import java.util.List;

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
                return handleEnvironmentalDeath(builder, victim, DamageType.OUT_OF_WORLD.asValue(), recentAttacker);
            }
            return builder.deathType(BedWarsDeathType.GENERIC).build();
        }

        Entity attacker = lastDamage.getAttacker();
        DamageType damageType = lastDamage.getType().asValue();

        if (attacker instanceof BedWarsPlayer killer) {
            return handlePlayerKill(builder, killer, damageType);
        }

        if (attacker != null) {
            return handleMobKill(builder, attacker);
        }

        return handleEnvironmentalDeath(builder, victim, damageType, recentAttacker);
    }

    private static BedWarsDeathResult handlePlayerKill(BedWarsDeathResult.Builder builder,
                                                       BedWarsPlayer killer,
                                                       DamageType damageType) {
        BedWarsDeathType deathType;

        if (damageType == DamageType.OUT_OF_WORLD.asValue()) {
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
                                                               DamageType damageType,
                                                               @Nullable BedWarsPlayer recentAttacker) {

        if (damageType == DamageType.OUT_OF_WORLD.asValue()) {
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

        List<ComponentLike> placeholders = List.of(
            Argument.component("player", Component.text(victimColor + victimName)),
            Argument.component("killer", result.killer() != null ? Component.text(getTeamColor(result.killer()) + result.killer().getUsername()) : Component.text("")),
            Argument.component("entity", result.attackerEntity() != null ? result.attackerEntity().getCustomName() : Component.text(""))
        );

        return getDeathMessage(result, deathType, placeholders);
    }

    private static @NonNull Component getDeathMessage(@NonNull BedWarsDeathResult result, BedWarsDeathType deathType, List<ComponentLike> placeholders) {
        Component message = switch (deathType) {
            case VOID -> Component.translatable("bedwars.kill.void.default", placeholders);
            case VOID_ASSISTED -> Component.translatable("bedwars.kill.void_by.default", placeholders);
            case GENERIC -> Component.translatable("bedwars.kill.generic.default", placeholders);
            case GENERIC_ASSISTED -> Component.translatable("bedwars.kill.generic_by.default", placeholders);
            case BOW -> Component.translatable("bedwars.kill.bow.default", placeholders);
            case ENTITY -> Component.translatable("bedwars.kill.entity.default", placeholders);
        };

        if (result.isFinalKill()) {
            message = message.append(Component.translatable("bedwars.kill.final_kill"));
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

