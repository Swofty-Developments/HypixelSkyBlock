package net.swofty.commons.punishment;

import lombok.Getter;

@Getter
public enum PunishmentTag {
    PERSONAL_PROOF("Personal proof", "P", null),
    GOLIATH("Punishment applied via Goliath", "G", null),
    PLAYER_REPORT("Player Report", "R", null),
    FORUMS("Forums", "F", null),
    SLACK("Slack", "S", null),
    WELFARE("Punishment applied over Welfare concern", "W", 99),
    ACCOUNT_SECURITY_ALERT(null, "ASA", null),
    RANKED_TEAM(null, "RT", null),
    CHECK_BEFORE_UNBAN("Check with the punisher before unbanning this user", "U", null),
    OVERWRITE("This punishment overwrote another punishment", "O", null);

    private final String description;
    private final String shortCode;
    private final Integer group;

    PunishmentTag(String description, String shortCode, Integer group) {
        this.description = description;
        this.shortCode = shortCode;
        this.group = group;
    }

}