package net.swofty.type.generic.data.handlers;

import org.bson.Document;

import java.util.Objects;
import java.util.UUID;

public final class PunishmentRecord {

    private final String id; // Mongo ObjectId as string (optional)
    private final UUID targetUuid;
    private final PunishmentType type;

    private final String reason;       // first Ω part (must be clean)
    private final boolean cancelled;   // second Ω part
    private final boolean active;      // third Ω part (computed or stored)
    private final Long durationMs;     // fourth Ω part: null => "null" (perm)
    private final UUID bannerUuid;     // fifth Ω part
    private final long startMs;        // sixth Ω part

    private final Long cancelledAtMs;  // optional
    private final String cancelReason; // optional

    public PunishmentRecord(
            String id,
            UUID targetUuid,
            PunishmentType type,
            String reason,
            boolean cancelled,
            boolean active,
            Long durationMs,
            UUID bannerUuid,
            long startMs,
            Long cancelledAtMs,
            String cancelReason
    ) {
        this.id = id;
        this.targetUuid = Objects.requireNonNull(targetUuid, "targetUuid");
        this.type = Objects.requireNonNull(type, "type");
        this.reason = reason == null ? "" : reason;
        this.cancelled = cancelled;
        this.active = active;
        this.durationMs = durationMs; // null means permanent
        this.bannerUuid = Objects.requireNonNull(bannerUuid, "bannerUuid");
        this.startMs = startMs;
        this.cancelledAtMs = cancelledAtMs;
        this.cancelReason = cancelReason;
    }

    public String getId() { return id; }
    public UUID getTargetUuid() { return targetUuid; }
    public PunishmentType getType() { return type; }
    public String getReason() { return reason; }
    public boolean isCancelled() { return cancelled; }
    public boolean isActive() { return active; }
    public Long getDurationMs() { return durationMs; }
    public UUID getBannerUuid() { return bannerUuid; }
    public long getStartMs() { return startMs; }
    public Long getCancelledAtMs() { return cancelledAt
