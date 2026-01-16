package net.swofty.commons.punishment;

import lombok.Getter;

@Getter
public enum PunishmentTag {
    PERSONAL_PROOF("Personal proof", "P", null, null),
    GOLIATH("Punishment applied via Goliath", "G", null, null),
    PLAYER_REPORT("Player Report", "R", null, null),
    FORUMS("Forums", "F", null, null),
    SLACK("Slack", "S", null, null),
    WELFARE("Punishment applied over Welfare concern", "W", "STAFF", 99),
    ACCOUNT_SECURITY_ALERT(null, "ASA", null, null),
    RANKED_TEAM(null, "RT", null, null),
    CHECK_BEFORE_UNBAN("Check with the punisher before unbanning this user", "U", "STAFF", null),
    OVERWRITE("This punishment overwrote another punishment", "O", "STAFF", null);

    private final String description;
    private final String shortCode;
    private final String requiredRank;
    private final Integer group;

    PunishmentTag(String description, String shortCode, String requiredRank, Integer group) {
        this.description = description;
        this.shortCode = shortCode;
        this.requiredRank = requiredRank;
        this.group = group;
    }

}