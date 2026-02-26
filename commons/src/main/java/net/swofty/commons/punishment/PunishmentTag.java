package net.swofty.commons.punishment;

import lombok.Getter;

@Getter
public enum PunishmentTag {
    PERSONAL_PROOF("Personal proof", "P"),
    GOLIATH("Punishment applied via Goliath", "G"),
    PLAYER_REPORT("Player Report", "R"),
    FORUMS("Forums", "F"),
    SLACK("Slack", "S"),
    WELFARE("Punishment applied over Welfare concern", "W"),
    ACCOUNT_SECURITY_ALERT(null, "ASA"),
    RANKED_TEAM(null, "RT"),
    CHECK_BEFORE_UNBAN("Check with the punisher before unbanning this user", "U"),
    OVERWRITE("This punishment overwrote another punishment", "O");

    private final String description;
    private final String shortCode;

    PunishmentTag(String description, String shortCode) {
        this.description = description;
        this.shortCode = shortCode;
    }

}