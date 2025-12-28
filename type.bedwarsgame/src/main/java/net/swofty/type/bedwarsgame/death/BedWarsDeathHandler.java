package net.swofty.type.bedwarsgame.death;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig.TeamKey;
import net.swofty.type.bedwarsgame.game.Game;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BedWarsDeathHandler {

    private static final int VOID_Y_THRESHOLD = 1;
    public static BedWarsDeathResult calculateDeath(@NotNull BedWarsPlayer victim, @NotNull Game game) {
        TeamKey teamKey = victim.getTeamKey();
        boolean isFinalKill = teamKey != null && !game.getTeamManager().isBedAlive(teamKey);

        Damage lastDamage = victim.getLastDamageSource();
        BedWarsPlayer recentAttacker = BedWarsCombatTracker.getRecentAttacker(victim);

        BedWarsDeathResult.Builder builder = BedWarsDeathResult.builder()
                .victim(victim)
                .isFinalKill(isFinalKill);

        // No damage source at all
        if (lastDamage == null) {
            return handleNoDamageSource(builder, victim, recentAttacker);
        }

        Entity attacker = lastDamage.getAttacker();
        DamageType damageType = lastDamage.getType().asValue();

        // Direct player kill
        if (attacker instanceof BedWarsPlayer killer) {
            return handlePlayerKill(builder, victim, killer, damageType);
        }

        // Mob kill
        if (attacker != null) {
            return handleMobKill(builder, attacker);
        }

        // Environmental death - check for assist
        return handleEnvironmentalDeath(builder, victim, damageType, recentAttacker);
    }

    private static BedWarsDeathResult handleNoDamageSource(BedWarsDeathResult.Builder builder,
                                                           BedWarsPlayer victim,
                                                           @Nullable BedWarsPlayer recentAttacker) {
        boolean isInVoid = victim.getPosition().y() <= VOID_Y_THRESHOLD;

        if (isInVoid) {
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

    private static BedWarsDeathResult handlePlayerKill(BedWarsDeathResult.Builder builder,
                                                        BedWarsPlayer victim,
                                                        BedWarsPlayer killer,
                                                        DamageType damageType) {
        BedWarsDeathType deathType;

        if (damageType == DamageType.OUT_OF_WORLD.asValue() || victim.getPosition().y() <= VOID_Y_THRESHOLD) {
            deathType = BedWarsDeathType.PLAYER_VOID_KNOCK;
        } else if (damageType == DamageType.FALL.asValue()) {
            deathType = BedWarsDeathType.PLAYER_FALL_KNOCK;
        } else if (damageType == DamageType.ARROW.asValue() || damageType == DamageType.TRIDENT.asValue()) {
            deathType = BedWarsDeathType.PLAYER_PROJECTILE;
        } else if (damageType == DamageType.EXPLOSION.asValue() || damageType == DamageType.PLAYER_EXPLOSION.asValue()) {
            deathType = BedWarsDeathType.PLAYER_EXPLOSION;
        } else if (damageType == DamageType.ON_FIRE.asValue() || damageType == DamageType.IN_FIRE.asValue()) {
            deathType = BedWarsDeathType.PLAYER_FIRE;
        } else {
            deathType = BedWarsDeathType.PLAYER_MELEE;
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
                .deathType(BedWarsDeathType.MOB_KILL)
                .attackerEntity(attacker)
                .build();
    }

    private static BedWarsDeathResult handleEnvironmentalDeath(BedWarsDeathResult.Builder builder,
                                                                BedWarsPlayer victim,
                                                                DamageType damageType,
                                                                @Nullable BedWarsPlayer recentAttacker) {
        boolean isInVoid = victim.getPosition().y() <= VOID_Y_THRESHOLD;

        // Void death
        if (damageType == DamageType.OUT_OF_WORLD.asValue() || isInVoid) {
            if (recentAttacker != null) {
                return builder
                        .deathType(BedWarsDeathType.VOID_ASSISTED)
                        .assistPlayer(recentAttacker)
                        .weaponUsed(BedWarsCombatTracker.getLastAttackerWeapon(victim))
                        .build();
            }
            return builder.deathType(BedWarsDeathType.VOID).build();
        }

        // Fall death
        if (damageType == DamageType.FALL.asValue()) {
            if (recentAttacker != null) {
                return builder
                        .deathType(BedWarsDeathType.FALL_ASSISTED)
                        .assistPlayer(recentAttacker)
                        .weaponUsed(BedWarsCombatTracker.getLastAttackerWeapon(victim))
                        .build();
            }
            return builder.deathType(BedWarsDeathType.FALL).build();
        }

        // Fire death
        if (damageType == DamageType.ON_FIRE.asValue() || damageType == DamageType.IN_FIRE.asValue()
                || damageType == DamageType.LAVA.asValue()) {
            return builder.deathType(BedWarsDeathType.FIRE).build();
        }

        // Explosion death
        if (damageType == DamageType.EXPLOSION.asValue()) {
            return builder.deathType(BedWarsDeathType.EXPLOSION).build();
        }

        return builder.deathType(BedWarsDeathType.GENERIC).build();
    }

    public static Component createDeathMessage(@NotNull BedWarsDeathResult result) {
        String victimName = result.victim().getUsername();
        String victimColor = getTeamColor(result.victim());
        BedWarsDeathType deathType = result.deathType();
        String messageFormat = deathType.getMessageFormat();

        Component message;
        if (deathType.involvesPlayer()) {
            BedWarsPlayer involvedPlayer = deathType.isAssisted() ? result.assistPlayer() : result.killer();
            message = createPlayerInvolvedMessage(victimName, victimColor, involvedPlayer, messageFormat);
        } else if (deathType == BedWarsDeathType.MOB_KILL) {
            message = createMobKillMessage(victimName, victimColor, messageFormat, result.attackerEntity());
        } else {
            message = createSingleMessage(victimName, victimColor, messageFormat);
        }

        // Append FINAL KILL if applicable
        if (result.isFinalKill()) {
            message = message.append(
                    MiniMessage.miniMessage().deserialize("<gray> </gray><aqua><bold>FINAL KILL!</bold></aqua>")
            );
        }

        return message;
    }

    private static Component createPlayerInvolvedMessage(String victimName, String victimColor,
                                                         @Nullable BedWarsPlayer otherPlayer,
                                                         String messageFormat) {
        if (otherPlayer == null) {
            return createSingleMessage(victimName, victimColor, messageFormat);
        }

        String otherColor = getTeamColor(otherPlayer);
        return Component.text(victimColor + victimName + "§7" + messageFormat + otherColor + otherPlayer.getUsername() + "§7.");
    }

    private static Component createSingleMessage(String victimName, String victimColor, String messageFormat) {
        return Component.text(victimColor + victimName + "§7" + messageFormat);
    }

    private static Component createMobKillMessage(String victimName, String victimColor, String messageFormat,
                                                  @Nullable Entity mob) {
        String mobName = mob != null
                ? mob.getEntityType().name().toLowerCase().replace("_", " ")
                : "a mob";

        return Component.text(victimColor + victimName + "§7" + messageFormat + mobName + "§7.");
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

