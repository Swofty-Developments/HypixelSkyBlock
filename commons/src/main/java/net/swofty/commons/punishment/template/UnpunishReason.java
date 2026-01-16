package net.swofty.commons.punishment.template;

import lombok.Getter;

@Getter
public enum UnpunishReason {
    WRONG_PLAYER("Wrong Player", null, null),
    WRONG_PUNISHMENT("Wrong Punishment", null, null),
    INSUFFICIENT_EVIDENCE("Insufficient Evidence", "IE", null),
    ACCOUNT_SECURED("Account Secured", null, "MODERATOR"),
    SECOND_CHANCE("Second Chance", "SC", "ADMIN"),
    CHARGEBACK_APPEALED("Chargeback Appealed", null, "ADMIN"),
    CHARGEBACK_FAILED("Chargeback Failed", null, "ADMIN"),
    CHARGEBACK_CANCELLED("Chargeback Cancelled", null, "ADMIN"),
    NOT_FRAUD("Not Fraud", null, "ADMIN"),
    TIME_SERVED_MAINTAIN_WEIGHT("Time Served (Maintain Weight)", "MW", "ADMIN");

    private final String reason;
    private final String tag;
    private final String requiredRank;

    UnpunishReason(String reason, String tag, String requiredRank) {
        this.reason = reason;
        this.tag = tag;
        this.requiredRank = requiredRank;
    }
}