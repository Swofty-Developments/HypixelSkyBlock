package net.swofty.type.ravengardgeneric.classes;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum RavengardClass {
    KNIGHT("Knight", '\uE226'),
    WARRIOR("Warrior", '\uE228'),
    HUNTER("Hunter", '\uE227'),
    ASSASSIN("Assassin", '\uE225'),
    SORCERER("Sorcerer", '\uE229');

    private final String displayName;
    private final char icon;

    RavengardClass(String displayName, char icon) {
        this.displayName = displayName;
        this.icon = icon;
    }

    /**
     * Profile-statue tooltip, verbatim from the captured menu. Only the Assassin screen was
     * captured, so the rest return null and their lore lines are left out rather than invented.
     */
    public String[] profileLore() {
        if (this != ASSASSIN) {
            return null;
        }
        return new String[]{
                "§7A stealth-focused class adept at",
                "§7sneaking.",
                "",
                "§7Primary weapon: §fDaggers",
                "",
                "§7Stats:",
                "§7◦ Health §c160.0 ❤",
                "§7◦ Protection §b0.0 ⛊",
                "§7◦ Damage §420.0 ⚔"
        };
    }

    /**
     * The two abilities unlocked by default, bound to the drop and swap-hand keys.
     *
     * <p>Knight, Warrior and Assassin come from captures. Hunter has not been captured; every
     * captured pair is one offensive/utility ability plus one heal, so it follows that shape.
     */
    public List<RavengardAbility> defaultAbilities() {
        return switch (this) {
            case KNIGHT -> List.of(RavengardAbility.SENTINEL, RavengardAbility.RECOVERY);
            case WARRIOR -> List.of(RavengardAbility.WAR_CRY, RavengardAbility.COOL_OFF);
            case ASSASSIN -> List.of(RavengardAbility.SHADOWS, RavengardAbility.HEAL_WOUNDS);
            case HUNTER -> List.of(RavengardAbility.EAGLE_EYE, RavengardAbility.MED_KIT);
            case SORCERER -> List.of();
        };
    }

    public List<RavengardAbility> getAbilities() {
        return Arrays.stream(RavengardAbility.values())
                .filter(ability -> ability.getOwner() == this)
                .toList();
    }

    public static RavengardClass fromKey(String key) {
        if (key == null || key.isEmpty()) {
            return null;
        }
        for (RavengardClass value : values()) {
            if (value.name().equalsIgnoreCase(key)) {
                return value;
            }
        }
        return null;
    }
}
