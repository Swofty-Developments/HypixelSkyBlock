package net.swofty.type.generic.data.handlers;

import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Low-level Mongo repository.
 * Stores one document per punishment event (append-only).
 */
public final class PunishmentRepository {

    public PunishmentRepository() {
        if (PunishmentsDatabase.collection == null) {
            throw new IllegalStateException("PunishmentsDatabase.collection is null. Did you call PunishmentsDatabase.connect(...) ?");
        }
    }

    public String insert(PunishmentRecord rec) {
        Document doc = new Document();
        doc.put("targetUuid", rec.getTargetUuid().toString());
        if (rec.getTargetName() != null) doc.put("targetName", rec.getTargetName());
        if (rec.getTargetName() != null) doc.put("targetNameLower", rec.getTargetName().toLowerCase());

        doc.put("type", rec.getType().name());
        doc.put("reason", rec.getReason());
        doc.put("cancelled", rec.isCancelled());
        doc.put("active", rec.isActive());
        doc.put("lengthMs", rec.getLengthMs() < 0 ? null : rec.getLengthMs());
        doc.put("bannerUuid", rec.getBannerUuid().toString());
        doc.put("start", rec.getStartMs());

        PunishmentsDatabase.collection.insertOne(doc);
        ObjectId id = doc.getObjectId("_id");
        return id != null ? id.toHexString() : null;
    }

    public List<PunishmentRecord> getHistory(UUID targetUuid, int limit) {
        FindIterable<Document> it = PunishmentsDatabase.collection.find(Filters.eq("targetUuid", targetUuid.toString()))
                .sort(Sorts.descending("start"))
                .limit(limit);

        List<PunishmentRecord> out = new ArrayList<>();
        for (Document d : it) out.add(fromDoc(d));
        return out;
    }

    public long countByType(UUID targetUuid, PunishmentType type) {
        return PunishmentsDatabase.collection.countDocuments(
                Filters.and(
                        Filters.eq("targetUuid", targetUuid.toString()),
                        Filters.eq("type", type.name()),
                        Filters.eq("cancelled", false)
                )
        );
    }

    /**
     * Returns "current active ban/tempban" if any (not cancelled, active=true).
     * Useful if you want to prevent double-bans, etc.
     */
    public PunishmentRecord getActive(UUID targetUuid, PunishmentType type) {
        Document d = PunishmentsDatabase.collection.find(
                Filters.and(
                        Filters.eq("targetUuid", targetUuid.toString()),
                        Filters.eq("type", type.name()),
                        Filters.eq("cancelled", false),
                        Filters.eq("active", true)
                )
        ).sort(Sorts.descending("start")).first();

        return d == null ? null : fromDoc(d);
    }

    private PunishmentRecord fromDoc(Document d) {
        String id = d.getObjectId("_id") != null ? d.getObjectId("_id").toHexString() : null;
        UUID targetUuid = UUID.fromString(d.getString("targetUuid"));
        String targetName = d.getString("targetName");
        PunishmentType type = PunishmentType.valueOf(d.getString("type"));
        String reason = d.getString("reason");

        boolean cancelled = d.getBoolean("cancelled", false);
        boolean active = d.getBoolean("active", false);

        Object lenObj = d.get("lengthMs");
        long lengthMs = (lenObj == null) ? -1L : ((Number) lenObj).longValue();

        UUID banner = UUID.fromString(d.getString("bannerUuid"));
        long start = ((Number) d.get("start")).longValue();

        return new PunishmentRecord(id, targetUuid, targetName, type, reason, cancelled, active, lengthMs, banner, start);
    }
}
