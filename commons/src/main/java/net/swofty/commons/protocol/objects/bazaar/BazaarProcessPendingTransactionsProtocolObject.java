package net.swofty.commons.protocol.objects.bazaar;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class BazaarProcessPendingTransactionsProtocolObject extends ProtocolObject<
        BazaarProcessPendingTransactionsProtocolObject.BazaarProcessPendingTransactionsMessage,
        BazaarProcessPendingTransactionsProtocolObject.BazaarProcessPendingTransactionsResponse> {

    @Override
    public Serializer<BazaarProcessPendingTransactionsMessage> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(BazaarProcessPendingTransactionsMessage v) {
                JSONObject o = new JSONObject();
                o.put("player-uuid", v.playerUUID.toString());
                o.put("profile-uuid", v.profileUUID.toString());

                JSONArray arr = new JSONArray();
                v.transactionIds.forEach(arr::put);
                o.put("transaction-ids", arr);

                return o.toString();
            }

            @Override
            public BazaarProcessPendingTransactionsMessage deserialize(String json) {
                JSONObject o = new JSONObject(json);

                List<String> transactionIds = o.getJSONArray("transaction-ids")
                        .toList()
                        .stream()
                        .map(Object::toString)
                        .collect(Collectors.toList());

                return new BazaarProcessPendingTransactionsMessage(
                        UUID.fromString(o.getString("player-uuid")),
                        UUID.fromString(o.getString("profile-uuid")),
                        transactionIds
                );
            }

            @Override
            public BazaarProcessPendingTransactionsMessage clone(BazaarProcessPendingTransactionsMessage v) {
                return new BazaarProcessPendingTransactionsMessage(v.playerUUID, v.profileUUID, v.transactionIds);
            }
        };
    }

    @Override
    public Serializer<BazaarProcessPendingTransactionsResponse> getReturnSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(BazaarProcessPendingTransactionsResponse v) {
                JSONObject o = new JSONObject();
                o.put("processed-count", v.processedCount);
                o.put("failed-count", v.failedCount);

                JSONArray successArr = new JSONArray();
                v.successfulTransactionIds.forEach(successArr::put);
                o.put("successful-transaction-ids", successArr);

                JSONArray failedArr = new JSONArray();
                v.failedTransactionIds.forEach(failedArr::put);
                o.put("failed-transaction-ids", failedArr);

                return o.toString();
            }

            @Override
            public BazaarProcessPendingTransactionsResponse deserialize(String json) {
                JSONObject o = new JSONObject(json);

                List<String> successfulIds = o.getJSONArray("successful-transaction-ids")
                        .toList()
                        .stream()
                        .map(Object::toString)
                        .collect(Collectors.toList());

                List<String> failedIds = o.getJSONArray("failed-transaction-ids")
                        .toList()
                        .stream()
                        .map(Object::toString)
                        .collect(Collectors.toList());

                return new BazaarProcessPendingTransactionsResponse(
                        o.getInt("processed-count"),
                        o.getInt("failed-count"),
                        successfulIds,
                        failedIds
                );
            }

            @Override
            public BazaarProcessPendingTransactionsResponse clone(BazaarProcessPendingTransactionsResponse v) {
                return new BazaarProcessPendingTransactionsResponse(
                        v.processedCount, v.failedCount, v.successfulTransactionIds, v.failedTransactionIds);
            }
        };
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class BazaarProcessPendingTransactionsMessage {
        public UUID playerUUID;
        public UUID profileUUID;
        public List<String> transactionIds;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class BazaarProcessPendingTransactionsResponse {
        public int processedCount;
        public int failedCount;
        public List<String> successfulTransactionIds;
        public List<String> failedTransactionIds;
    }
}