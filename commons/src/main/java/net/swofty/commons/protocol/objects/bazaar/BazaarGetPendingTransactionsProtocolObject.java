package net.swofty.commons.protocol.objects.bazaar;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class BazaarGetPendingTransactionsProtocolObject extends ProtocolObject<
        BazaarGetPendingTransactionsProtocolObject.BazaarGetPendingTransactionsMessage,
        BazaarGetPendingTransactionsProtocolObject.BazaarGetPendingTransactionsResponse> {

    @Override
    public Serializer<BazaarGetPendingTransactionsMessage> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(BazaarGetPendingTransactionsMessage v) {
                JSONObject o = new JSONObject();
                o.put("player-uuid", v.playerUUID.toString());
                o.put("profile-uuid", v.profileUUID.toString());
                return o.toString();
            }

            @Override
            public BazaarGetPendingTransactionsMessage deserialize(String json) {
                JSONObject o = new JSONObject(json);
                return new BazaarGetPendingTransactionsMessage(
                        UUID.fromString(o.getString("player-uuid")),
                        UUID.fromString(o.getString("profile-uuid"))
                );
            }

            @Override
            public BazaarGetPendingTransactionsMessage clone(BazaarGetPendingTransactionsMessage v) {
                return new BazaarGetPendingTransactionsMessage(v.playerUUID, v.profileUUID);
            }
        };
    }

    @Override
    public Serializer<BazaarGetPendingTransactionsResponse> getReturnSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(BazaarGetPendingTransactionsResponse v) {
                JSONArray arr = new JSONArray();
                for (PendingTransactionInfo transaction : v.transactions) {
                    JSONObject o = new JSONObject();
                    o.put("id", transaction.id);
                    o.put("transaction-type", transaction.transactionType);
                    o.put("transaction-data", transaction.transactionData);
                    o.put("created-at", transaction.createdAt.toString());
                    arr.put(o);
                }
                return arr.toString();
            }

            @Override
            public BazaarGetPendingTransactionsResponse deserialize(String json) {
                JSONArray arr = new JSONArray(json);
                var list = arr.toList().stream().map(obj -> {
                    JSONObject o = new JSONObject((java.util.Map<?,?>) obj);
                    return new PendingTransactionInfo(
                            o.getString("id"),
                            o.getString("transaction-type"),
                            o.getJSONObject("transaction-data"),
                            Instant.parse(o.getString("created-at"))
                    );
                }).collect(Collectors.toList());
                return new BazaarGetPendingTransactionsResponse(list);
            }

            @Override
            public BazaarGetPendingTransactionsResponse clone(BazaarGetPendingTransactionsResponse v) {
                return new BazaarGetPendingTransactionsResponse(v.transactions);
            }
        };
    }

    @AllArgsConstructor
    public static class BazaarGetPendingTransactionsMessage {
        public UUID playerUUID;
        public UUID profileUUID;
    }

    @AllArgsConstructor
    public static class BazaarGetPendingTransactionsResponse {
        public List<PendingTransactionInfo> transactions;
    }

    /**
     * Represents a pending transaction for protocol transfer
     */
    public static class PendingTransactionInfo {
        public String id;
        public String transactionType;
        public JSONObject transactionData;
        public Instant createdAt;

        public PendingTransactionInfo(String id, String transactionType, JSONObject transactionData, Instant createdAt) {
            this.id = id;
            this.transactionType = transactionType;
            this.transactionData = transactionData;
            this.createdAt = createdAt;
        }
    }
}