package net.swofty.commons.punishment.template;

import lombok.Getter;

@Getter
public enum UnpunishReason {
    WRONG_PLAYER("Wrong Player", null),
    WRONG_PUNISHMENT("Wrong Punishment", null),
    INSUFFICIENT_EVIDENCE("Insufficient Evidence", "IE"),
    ACCOUNT_SECURED("Account Secured", null),
    SECOND_CHANCE("Second Chance", "SC"),
    CHARGEBACK_APPEALED("Chargeback Appealed", null),
    CHARGEBACK_FAILED("Chargeback Failed", null),
    CHARGEBACK_CANCELLED("Chargeback Cancelled", null),
    NOT_FRAUD("Not Fraud", null),
    TIME_SERVED_MAINTAIN_WEIGHT("Time Served (Maintain Weight)", "MW");

    private final String reason;
    private final String tag;

    UnpunishReason(String reason, String tag) {
        this.reason = reason;
        this.tag = tag;
    }
}