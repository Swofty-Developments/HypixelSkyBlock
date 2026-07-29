package net.swofty.commons;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.bson.Document;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
public class TrackedItem {
    private final UUID itemUUID;
    private final long created;
    private final ArrayList<PlayerOwnershipLog> attachedPlayers;
    private final String itemType;
    private final Integer numberMade;

    @JsonCreator
    public TrackedItem(@JsonProperty("itemUUID") UUID itemUUID,
                       @JsonProperty("created") long created,
                       @JsonProperty("attachedPlayers") ArrayList<PlayerOwnershipLog> attachedPlayers,
                       @JsonProperty("itemType") String itemType,
                       @JsonProperty("numberMade") Integer numberMade) {
        this.itemUUID = itemUUID;
        this.created = created;
        this.attachedPlayers = attachedPlayers;
        this.itemType = itemType;
        this.numberMade = numberMade;
    }

    @Override
    public TrackedItem clone() {
        return new TrackedItem(itemUUID, created, attachedPlayers, itemType, numberMade);
    }

    public Document toDocument() {
        return new Document(Map.of(
                "_id", itemUUID.toString(),
                "created", created,
                "attached-players", attachedPlayers.stream().map(PlayerOwnershipLog::toJson).toList(),
                "item-type", itemType,
                "number-made", numberMade
        ));
    }

    public void addOrUpdateAttachedPlayer(UUID playerUUID, UUID profileUUID) {
        // If the last log entry has the same playeruuid and profile uuid, then merely update the lastSeen
        if (attachedPlayers.isEmpty()) {
            attachedPlayers.add(new PlayerOwnershipLog(playerUUID, profileUUID, System.currentTimeMillis(), System.currentTimeMillis()));
        } else {
            PlayerOwnershipLog lastLog = attachedPlayers.getLast();
            if (lastLog.playerUUID.equals(playerUUID) && lastLog.playerProfileUUID.equals(profileUUID)) {
                lastLog = new PlayerOwnershipLog(lastLog.playerUUID, lastLog.playerProfileUUID, lastLog.firstSeen, System.currentTimeMillis());
                attachedPlayers.set(attachedPlayers.size() - 1, lastLog);
                return;
            }

            attachedPlayers.add(new PlayerOwnershipLog(playerUUID, profileUUID, System.currentTimeMillis(), System.currentTimeMillis()));
        }
    }

    public static TrackedItem newTrackedItem(UUID itemUUID, UUID playerUUID, UUID profileUUID, String itemType,
                                             Integer numberMade) {
        return new TrackedItem(itemUUID, System.currentTimeMillis(),
                new ArrayList<>(List.of(new PlayerOwnershipLog(playerUUID, profileUUID, System.currentTimeMillis(), System.currentTimeMillis()))),
                itemType, numberMade + 1);
    }

    public static TrackedItem fromDocument(Document document) {
        return new TrackedItem(
                UUID.fromString(document.getString("_id")),
                document.getLong("created"),
                new ArrayList<>(document.getList("attached-players", String.class)
                        .stream().map(JSONObject::new).map(TrackedItem::fromJson).toList()),
                document.getString("item-type"),
                document.getInteger("number-made")
        );
    }

    public record PlayerOwnershipLog(UUID playerUUID, UUID playerProfileUUID, long firstSeen, long lastSeen) {
        public String toJson() {
            return new JSONObject(Map.of(
                    "player-uuid", playerUUID.toString(),
                    "player-profile-uuid", playerProfileUUID.toString(),
                    "first-seen", firstSeen,
                    "last-seen", lastSeen
            )).toString();
        }
    }

    private static PlayerOwnershipLog fromJson(JSONObject json) {
        return new PlayerOwnershipLog(UUID.fromString(json.getString("player-uuid")),
                UUID.fromString(json.getString("player-profile-uuid")),
                json.getLong("first-seen"),
                json.getLong("last-seen"));
    }
}