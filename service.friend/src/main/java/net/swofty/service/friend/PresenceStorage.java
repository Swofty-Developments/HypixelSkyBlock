package net.swofty.service.friend;

import net.swofty.commons.presence.PresenceInfo;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class PresenceStorage {
    private static final Map<UUID, PresenceInfo> presenceByUuid = new ConcurrentHashMap<>();

    public static void upsert(PresenceInfo presence) {
        presenceByUuid.put(presence.getUuid(), presence);
    }

    /**
     * Upsert presence and return the previous entry (if any).
     */
    public static PresenceInfo upsertAndGetPrevious(PresenceInfo presence) {
        return presenceByUuid.put(presence.getUuid(), presence);
    }

    /**
     * Upsert presence while preserving non-null server metadata from previous entries.
     */
    public static PresenceInfo upsertPreservingServer(PresenceInfo incoming) {
        PresenceInfo previous = presenceByUuid.get(incoming.getUuid());
        if (previous == null) {
            presenceByUuid.put(incoming.getUuid(), incoming);
            return null;
        }

        String serverType = incoming.getServerType() != null ? incoming.getServerType() : previous.getServerType();
        String serverId = incoming.getServerId() != null ? incoming.getServerId() : previous.getServerId();
        long lastSeen = incoming.getLastSeen() > 0 ? incoming.getLastSeen() : previous.getLastSeen();

        PresenceInfo merged = new PresenceInfo(
                incoming.getUuid(),
                incoming.isOnline(),
                serverType,
                serverId,
                lastSeen
        );
        presenceByUuid.put(incoming.getUuid(), merged);
        return previous;
    }

    public static List<PresenceInfo> getBulk(Collection<UUID> uuids) {
        if (uuids == null || uuids.isEmpty()) return List.of();
        return uuids.stream()
                .map(presenceByUuid::get)
                .filter(java.util.Objects::nonNull)
                .toList();
    }

    public static Map<UUID, PresenceInfo> getMap(Collection<UUID> uuids) {
        if (uuids == null || uuids.isEmpty()) return Map.of();
        return uuids.stream()
                .map(presenceByUuid::get)
                .filter(java.util.Objects::nonNull)
                .collect(Collectors.toMap(PresenceInfo::getUuid, p -> p, (a, b) -> a));
    }

    public static PresenceInfo get(UUID uuid) {
        return presenceByUuid.get(uuid);
    }

    public static Map<UUID, Boolean> getOnlineStatus(Collection<UUID> uuids) {
        if (uuids == null || uuids.isEmpty()) return Map.of();
        return uuids.stream()
                .collect(Collectors.toMap(
                        uuid -> uuid,
                        uuid -> presenceByUuid.getOrDefault(uuid, new PresenceInfo(uuid, false, null, null, System.currentTimeMillis())).isOnline(),
                        (a, b) -> a
                ));
    }
}

