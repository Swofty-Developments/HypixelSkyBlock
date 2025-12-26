package net.swofty.type.bedwarsgame.death;

import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class BedWarsCombatTracker {

    public static final Tag<String> LAST_ATTACKER_UUID = Tag.String("bedwars_last_attacker_uuid");
    public static final Tag<Long> LAST_ATTACKER_TIME = Tag.Long("bedwars_last_attacker_time");
    public static final Tag<Integer> LAST_ATTACKER_WEAPON = Tag.Integer("bedwars_last_attacker_weapon");
    public static final long ASSIST_WINDOW_MS = 5000;

    /**
     * Records that a player was attacked by another player.
     * Should be called whenever a player takes damage from another player.
     *
     * @param victim   The player who was attacked
     * @param attacker The player who attacked
     */
    public static void recordAttack(BedWarsPlayer victim, BedWarsPlayer attacker) {
        if (victim == null || attacker == null || victim.equals(attacker)) {
            return;
        }

        victim.setTag(LAST_ATTACKER_UUID, attacker.getUuid().toString());
        victim.setTag(LAST_ATTACKER_TIME, System.currentTimeMillis());
        ItemStack weapon = attacker.getItemInMainHand();
        if (weapon != null && !weapon.isAir()) {
            victim.setTag(LAST_ATTACKER_WEAPON, weapon.material().id());
        } else {
            victim.removeTag(LAST_ATTACKER_WEAPON);
        }
    }

    /**
     * Gets the player who last attacked the victim within the assist window.
     *
     * @param victim The player to check
     * @return The last attacker if within the assist window, null otherwise
     */
    @Nullable
    public static BedWarsPlayer getRecentAttacker(BedWarsPlayer victim) {
        if (victim == null) {
            return null;
        }

        String attackerUuidStr = victim.getTag(LAST_ATTACKER_UUID);
        Long attackTime = victim.getTag(LAST_ATTACKER_TIME);

        if (attackerUuidStr == null || attackTime == null) {
            return null;
        }

        if (System.currentTimeMillis() - attackTime > ASSIST_WINDOW_MS) {
            return null;
        }

        try {
            UUID attackerUuid = UUID.fromString(attackerUuidStr);
            if (victim.getInstance() != null) {
                return victim.getInstance().getPlayers().stream()
                        .filter(p -> p.getUuid().equals(attackerUuid))
                        .filter(p -> p instanceof BedWarsPlayer)
                        .map(p -> (BedWarsPlayer) p)
                        .findFirst()
                        .orElse(null);
            }
        } catch (IllegalArgumentException ignored) {
        }

        return null;
    }

    public static void clearCombatData(BedWarsPlayer player) {
        if (player == null) {
            return;
        }

        player.removeTag(LAST_ATTACKER_UUID);
        player.removeTag(LAST_ATTACKER_TIME);
        player.removeTag(LAST_ATTACKER_WEAPON);
    }

    public static boolean hasRecentAttacker(BedWarsPlayer victim) {
        return getRecentAttacker(victim) != null;
    }

    @Nullable
    public static Material getLastAttackerWeapon(BedWarsPlayer victim) {
        if (victim == null) {
            return null;
        }

        String attackerUuidStr = victim.getTag(LAST_ATTACKER_UUID);
        Long attackTime = victim.getTag(LAST_ATTACKER_TIME);
        Integer weaponName = victim.getTag(LAST_ATTACKER_WEAPON);

        if (attackerUuidStr == null || attackTime == null || weaponName == null) {
            return null;
        }

        if (System.currentTimeMillis() - attackTime > ASSIST_WINDOW_MS) {
            return null;
        }

        try {
            return Material.fromId(weaponName);
        } catch (Exception ignored) {
            return null;
        }
    }
}

