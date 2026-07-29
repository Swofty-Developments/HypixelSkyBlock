package net.swofty.type.bedwarsgame.death;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.entity.Entity;
import net.minestom.server.item.Material;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig.TeamKey;
import net.swofty.type.bedwarsgame.game.v2.BedWarsGame;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class BedWarsDeathHandler {
    private static final MiniMessage MM = MiniMessage.miniMessage();

    private BedWarsDeathHandler() {
    }

    public static BedWarsDeathResult calculateDeath(@NotNull BedWarsPlayer victim, @NotNull BedWarsGame game, boolean isVoidKill) {
        TeamKey teamKey = victim.getTeamKey();
        boolean isFinalKill = teamKey != null && !game.isBedAlive(teamKey);

        BedWarsPlayer recentAttacker = BedWarsCombatTracker.getRecentAttacker(victim);
        Material lastAttackerWeapon = BedWarsCombatTracker.getLastAttackerWeapon(victim);

        BedWarsDeathResult.Builder builder = BedWarsDeathResult.builder()
            .victim(victim)
            .isFinalKill(isFinalKill)
            .weaponUsed(lastAttackerWeapon);

        if (isVoidKill) {
            if (recentAttacker != null) {
                return builder
                    .deathType(BedWarsDeathType.VOID_ASSISTED)
                    .assistPlayer(recentAttacker)
                    .build();
            }
            return builder
                .deathType(BedWarsDeathType.VOID)
                .build();
        }

        if (recentAttacker != null) {
            BedWarsDeathType type = isRangedWeapon(lastAttackerWeapon) ? BedWarsDeathType.BOW : BedWarsDeathType.GENERIC_ASSISTED;
            return builder
                .deathType(type)
                .killer(recentAttacker)
                .build();
        }

        return builder
            .deathType(BedWarsDeathType.GENERIC)
            .build();
    }

    private static boolean isRangedWeapon(@Nullable Material weapon) {
        if (weapon == null) return false;
        return weapon == Material.BOW
            || weapon == Material.CROSSBOW
            || weapon == Material.TRIDENT;
    }

    public static Component createDeathMessage(@NotNull BedWarsDeathResult result) {
        BedWarsPlayer victim = result.victim();
        Component victimDisplay = colorizeName(victim);

        Component message = switch (result.deathType()) {
            case VOID -> MM.deserialize("<gray>").append(victimDisplay).append(MM.deserialize(" fell into the void."));
            case VOID_ASSISTED -> {
                Component assistDisplay = colorizeName(result.assistPlayer());
                yield MM.deserialize("<gray>")
                    .append(victimDisplay)
                    .append(MM.deserialize(" was knocked into the void by "))
                    .append(assistDisplay)
                    .append(MM.deserialize("."));
            }
            case GENERIC -> MM.deserialize("<gray>").append(victimDisplay).append(MM.deserialize(" died."));
            case GENERIC_ASSISTED -> {
                Component killerDisplay = colorizeName(result.getKillCreditPlayer());
                yield MM.deserialize("<gray>")
                    .append(victimDisplay)
                    .append(MM.deserialize(" was killed by "))
                    .append(killerDisplay)
                    .append(MM.deserialize("."));
            }
            case BOW -> {
                Component killerDisplay = colorizeName(result.getKillCreditPlayer());
                yield MM.deserialize("<gray>")
                    .append(victimDisplay)
                    .append(MM.deserialize(" was shot by "))
                    .append(killerDisplay)
                    .append(MM.deserialize("."));
            }
            case ENTITY -> {
                Entity entity = result.attackerEntity();
                String entityName = entity != null ? entity.getEntityType().name() : "an entity";
                BedWarsPlayer killer = result.killer();
                Component killerDisplay = colorizeName(killer);
                yield MM.deserialize("<gray>")
                    .append(victimDisplay)
                    .append(MM.deserialize(" was slain by "))
                    .append(killerDisplay)
                    .append(MM.deserialize("<gray>'s " + entityName + ".</gray>"));
            }
        };

        if (result.isFinalKill()) {
            message = message.append(MM.deserialize(" <aqua><bold>FINAL KILL!</bold></aqua>"));
        }
        return message;
    }

    private static Component colorizeName(@Nullable BedWarsPlayer player) {
        if (player == null) {
            return MM.deserialize("<gray>Unknown</gray>");
        }

        TeamKey teamKey = player.getTeamKey();
        String legacyColor = teamKey != null ? teamKey.chatColor() : "§7";
        return Component.text(legacyColor + player.getUsername());
    }
}
