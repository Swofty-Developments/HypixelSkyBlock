package net.swofty.commons.punishment.template;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum BanType {
    WATCHDOG(PunishmentCategory.ADMIN_ONLY, "WATCHDOG CHEAT DETECTION", "WATCHDOG", 4, "STAFF",
            "https://www.hypixel.net/watchdog", true, true),
    BLACKLISTED_MODIFICATIONS(PunishmentCategory.CHEATING, "Cheating through the use of unfair game advantages.", "BM", 4, null,
            null, true, true, Arrays.asList("Blacklisted Modifications", "Cheating/Unfair Advantage", "Using unfair advantages in game")),
    CROSS_TEAMING(PunishmentCategory.GAMEPLAY, "Cross teaming, you were found to be working with another team or player.", "CT", 1, null, null, false, false),
    TEAM_GRIEFING(PunishmentCategory.GAMEPLAY, "You were found to be negatively affecting your fellow team members.", "TG", 1, null, null, false, false),
    INAPPROPRIATE_BUILD(PunishmentCategory.INAPPROPRIATE_CONTENT, "Creating a build or drawing which is not appropriate on the server.", "IB", 1, null,
            null, false, false, Arrays.asList("Inappropriate Build", "Inappropriate Drawing")),
    INAPPROPRIATE_ITEM_NAME(PunishmentCategory.INAPPROPRIATE_CONTENT, "Creating or using an item that has an inappropriate name", "IN", 1, null, null, false, false),
    INAPPROPRIATE_ITEM_USAGE(PunishmentCategory.INAPPROPRIATE_CONTENT, "Using pets or cosmetics in an inappropriate way.", "IU", 1, null, null, false, false),
    STAFF_IMPERSONATION(null, "Misleading others to believe you are a youtuber or staff member.", "SI", 1, null, null, false, false),
    SCAMMING(null, "Attempting to obtain information or something of value from players.", "SC", 2, null, null, false, false),
    ENCOURAGING_CHEATING_LVL2(PunishmentCategory.CHEATING, "Discussing or acting in a manner which encourages cheating or rule breaking.", "EC2", 2, null, null, false, false),
    ENCOURAGING_CHEATING_LVL3(PunishmentCategory.CHEATING, "Discussing or acting in a manner which encourages cheating or rule breaking.", "EC3", 4, null, null, false, false),
    EXTREME_USER_DISRESPECT(null, "Acting in a manner that is extremely disrespectful to members within the community.", "EUD", 2, null,
            null, false, false, List.of("Extreme Negative behaviour")),
    STATS_BOOSTING(PunishmentCategory.ADMIN_ONLY, "Boosting your account to improve your stats.", "SB", 4, "STAFF",
            null, false, false, List.of("Boosting")),
    INAPPROPRIATE_AESTHETICS(PunishmentCategory.INAPPROPRIATE_CONTENT, "Using inappropriate skins or capes on the server.", "IA", 2, null, null, false, false),
    EXPLOITING(PunishmentCategory.ADMIN_ONLY, "Exploiting a bug or issue within the server and using it to your advantage.", "EX", 4, "STAFF",
            null, true, true, List.of("Exploits")),
    FALSIFIED_INFORMATION(null, "Making or sharing fake information.", "FI", 3, null, null, false, false),
    CHARGEBACK(PunishmentCategory.ADMIN_ONLY, "Chargeback: for more info and appeal, go to support.hypixel.net.", null, -1, "STAFF",
            null, false, false, List.of("Chargeback")),
    FRAUD(null, null, "FR", 0, null, null, false, false),
    ACCOUNT_SELLING(null, "Attempting to sell minecraft accounts.", "AS", 4, null, null, false, false),
    COMPROMISED_ACCOUNT(PunishmentCategory.ACCOUNT_SECURITY, "Your account has a security alert, please secure it and contact appeals.", "CA", -1, null,
            null, false, false, Arrays.asList("Compromised Account", "Account Security Alert"), "Account Security Alert"),
    ACCOUNT_SECURITY_ALERT_SERVER_ADVERTISING(PunishmentCategory.ACCOUNT_SECURITY, "Your account has a security alert, please secure it and contact appeals.", "CAS", -1, null, null, false, false),
    ACCOUNT_SECURITY_ALERT_BLACKLISTED(PunishmentCategory.ACCOUNT_SECURITY, "Your account has a security alert, please secure it and contact appeals.", "CAB", -1, null, null, false, false),
    PHISHING_LINK(null, "Attempting to gain access to other user's accounts/information.", "PL", 4, null, null, false, false),
    UN_INTENTIONALLY_CAUSING_DISTRESS_2(null, "Unintentionally/Intentionally Causing distress.", "UIB", 3, null, null, false, false),
    UN_INTENTIONALLY_CAUSING_DISTRESS_3(PunishmentCategory.ADMIN_ONLY, "Unintentionally/Intentionally Causing distress.", null, -1, "STAFF", null, false, false),
    INAPPROPRIATE_CONTENT_LVL2(PunishmentCategory.INAPPROPRIATE_CONTENT, "Talking or sharing inappropriate content with adult themes on the server.", "IC2", 3, null, null, false, false),
    ACCOUNT_DELETION(PunishmentCategory.ADMIN_ONLY, "Upon request, data for this user has been deleted.", null, 0, "STAFF", "https://support.hypixel.net", false, false),
    CREATOR_BAN(PunishmentCategory.ADMIN_ONLY, "Please contact staff for assistance.", null, -1, "STAFF", // Please contact creators@hypixel.net for assistance.
            null, false, false, List.of("Creator Ban"), "Creator Ban"),
    CREATOR_ACCOUNT_SECURITY_ALERT(PunishmentCategory.ADMIN_ONLY, "Your account has a security alert, please secure it and contact staff for assistance.", //Your account has a security alert, please secure it and contact creators@hypixel.net for assistance
            null, -1, "STAFF", null, false, false, Arrays.asList("Creator Compromised Account", "Creator Account Security Alert"), "Creator Account Security Alert");

    private final PunishmentCategory category;
    private final String reason;
    private final String shortName;
    private final int weight;
    private final String requiredRank;
    private final String url;
    private final boolean preventRanked;
    private final boolean wipe;
    private final List<String> aliases;
    private final String cleanName;

    BanType(PunishmentCategory category, String reason, String shortName, int weight, String requiredRank,
            String url, boolean preventRanked, boolean wipe) {
        this(category, reason, shortName, weight, requiredRank, url, preventRanked, wipe, null, null);
    }

    BanType(PunishmentCategory category, String reason, String shortName, int weight, String requiredRank,
            String url, boolean preventRanked, boolean wipe, List<String> aliases) {
        this(category, reason, shortName, weight, requiredRank, url, preventRanked, wipe, aliases, null);
    }

    BanType(PunishmentCategory category, String reason, String shortName, int weight, String requiredRank,
            String url, boolean preventRanked, boolean wipe, List<String> aliases, String cleanName) {
        this.category = category;
        this.reason = reason;
        this.shortName = shortName;
        this.weight = weight;
        this.requiredRank = requiredRank;
        this.url = url;
        this.preventRanked = preventRanked;
        this.wipe = wipe;
        this.aliases = aliases;
        this.cleanName = cleanName;
    }
}
