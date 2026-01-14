package net.swofty.type.generic.data.handlers;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public final class LynxPunishmentFormatter {

    // Must match Lynx VIEW_BANS: "YYYY-MM-DD HH:MM TZZ" where TZZ is letters 1-5
    private static final DateTimeFormatter VIEW_BANS_DATE =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm 'UTC'").withZone(ZoneOffset.UTC);

    private LynxPunishmentFormatter() {}

    public static String formatBansHeader(String username, int page, int pages) {
        return "§6--- Bans for " + username + " (Page " + page + " of " + pages + ") ---";
    }

    /** One numbered line for /bans output that matches Lynx VIEW_BANS regex */
    public static String formatBansLine(int index, PunishmentRecord rec, String bannerNameOrFallback) {
        String type = rec.isPermanent() ? "Ban" : "Tempban";
        String date = VIEW_BANS_DATE.format(Instant.ofEpochMilli(rec.getStartEpochMs()));
        String reason = rec.getReason();

        String byPart = "";
        if (bannerNameOrFallback != null && !bannerNameOrFallback.isBlank()) {
            byPart = " by " + bannerNameOrFallback;
        }

        String durPart = "";
        if (!rec.isPermanent() && rec.getDurationMillis() != null) {
            durPart = " for " + DurationParser.toLynxDHMS(rec.getDurationMillis());
        }

        return index + ". " + type + " - " + date + " " + reason + byPart + durPart;
    }

    /** One line per punishment for /bans -a output that matches Lynx VIEW_BANS_2 regex */
    public static String formatBansA(PunishmentRecord rec) {
        boolean active = rec.isActive(System.currentTimeMillis());

        String cancelled = Boolean.toString(rec.isCancelled());
        String activeStr = Boolean.toString(active);

        String length = (rec.getDurationMillis() == null) ? "null" : Long.toString(rec.getDurationMillis());
        UUID banner = rec.getBannerUuid();

        return "[Lynx] " + rec.getReason()
                + "Ω" + cancelled
                + "Ω" + activeStr
                + "Ω" + length
                + "Ω" + banner
                + "Ω" + rec.getStartEpochMs();
    }
}
