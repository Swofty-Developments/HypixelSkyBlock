package net.swofty.type.generic.data.handlers;

import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class PunishmentService {

    private PunishmentService() {}

    public static void ensureConnected() {
        if (PunishmentsDatabase.collection == null) {
            throw new IllegalStateException("PunishmentsDatabase not connected. Call PunishmentsDatabase.connect(...) in loader.");
        }
    }

    public static PunishmentRecord createBan(UUID target, UUID banner, String reason) {
        return create(target, banner, PunishmentType.BAN, null, reason);
    }

    public static PunishmentRecord createTempBan(UUID target, UUID banner, long durationMs, String reason) {
        return create(target, banner, PunishmentType.TEMPBAN, durationMs, reason);
    }

    private static PunishmentRecord create(UUID target, UUID banner, PunishmentType type, Long durationMs, String reason) {
        ensureConnected();

        long now = System.currentTimeMillis();
        boolean active = true;

        // Persist active flag so you can query quickly; we’ll recompute on read.
        Document doc = new Document();
        doc.put("targetUuid", target.toString());
        doc.put("type", type.name());
        doc.put("reason", reason == null ? "" : reason);
        doc.put("cancelled", false);
        doc.put("active", active);
        doc.put("durationMs", durationMs); // null for perm
        doc.put("bannerUuid", banner.toString());
        doc.put("startMs", now);

        PunishmentsDatabase.collection.insertOne(doc);

        return PunishmentRecord.fromDocument(doc);
    }

    public static List<PunishmentRecord> getPunishments(UUID target, PunishmentType... types) {
        ensureConnected();

        List<Bson> filters = new ArrayList<>();
        filters.add(Filters.eq("targetUuid", target.toString()));

        if (types != null && types.length > 0) {
            List<String> typeNames = new ArrayList<>();
            for (PunishmentType t : types) typeNames.add(t.name());
            filters.add(Filters.in("type", typeNames));
        }

        Bson query = Filters.and(filters);

        FindIterable<Document> it = PunishmentsDatabase.collection
                .find(query)
                .sort(Sorts.descending("startMs"));

        List<PunishmentRecord> out = new ArrayList<>();
        for (Document d : it) {
            PunishmentRecord rec = PunishmentRecord.fromDocument(d);
            out.add(recomputeActive(rec));
        }
        return out;
    }

    /** Recompute active based on cancelled + time window so output stays correct. */
    private static PunishmentRecord recomputeActive(PunishmentRecord rec) {
        boolean active;
        if (rec.isCancelled()) {
            active = false;
        } else if (rec.getDurationMs() == null) {
            // permanent
            active = true;
        } else {
            active = System.currentTimeMillis() < (rec.getStartMs() + rec.getDurationMs());
        }

        if (active == rec.isActive()) return rec;

        // Update stored flag for consistency
        if (rec.getId() != null) {
            PunishmentsDatabase.collection.updateOne(
                    Filters.eq("_id", new org.bson.types.ObjectId(rec.getId())),
                    Updates.set("active", active)
            );
        } else {
            // fallback (rare) — use target+start+banner match
            PunishmentsDatabase.collection.updateOne(
                    Filters.and(
                            Filters.eq("targetUuid", rec.getTargetUuid().toString()),
                            Filters.eq("startMs", rec.getStartMs()),
                            Filters.eq("bannerUuid", rec.getBannerUuid().toString())
                    ),
                    Updates.set("active", active)
            );
        }

        return new PunishmentRecord(
                rec.getId(),
                rec.getTargetUuid(),
                rec.getType(),
                rec.getReason(),
                rec.isCancelled(),
                active,
                rec.getDurationMs(),
                rec.getBannerUuid(),
                rec.getStartMs(),
                rec.getCancelledAtMs(),
                rec.getCancelReason()
        );
    }

    /** Minimal duration parser: "1d", "3h", "30m", "10s" or plain seconds. */
    public static long parseDurationMs(String input) {
        if (input == null) throw new IllegalArgumentException("Missing duration");
        String s = input.trim().toLowerCase();
        if (s.isEmpty()) throw new IllegalArgumentException("Missing duration");

        // allow "perm"/"permanent" to be rejected for tempban (use /ban instead)
        if (s.equals("perm") || s.equals("permanent")) {
            throw new IllegalArgumentException("Use /ban for permanent bans");
        }

        long multiplier;
        char last = s.charAt(s.length() - 1);

        String numberPart;
        if (Character.isLetter(last)) {
            numberPart = s.substring(0, s.length() - 1);
            switch (last) {
                case 'd' -> multiplier = 24L * 60L * 60L * 1000L;
                case 'h' -> multiplier = 60L * 60L * 1000L;
                case 'm' -> multiplier = 60L * 1000L;
                case 's' -> multiplier = 1000L;
                default -> throw new IllegalArgumentException("Unknown duration suffix: " + last);
            }
        } else {
            // treat as seconds if no suffix
            numberPart = s;
            multiplier = 1000L;
        }

        long amount;
        try {
            amount = Long.parseLong(numberPart);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid duration number: " + numberPart);
        }
        if (amount <= 0) throw new IllegalArgumentException("Duration must be > 0");

        return amount * multiplier;
    }
}
