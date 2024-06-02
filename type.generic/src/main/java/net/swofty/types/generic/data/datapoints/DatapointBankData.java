package net.swofty.types.generic.data.datapoints;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.swofty.service.protocol.Serializer;
import net.swofty.types.generic.calendar.SkyBlockCalendar;
import net.swofty.types.generic.data.Datapoint;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DatapointBankData extends Datapoint<DatapointBankData.BankData> {
    private static final Serializer<BankData> serializer = new Serializer<>() {
        @Override
        public String serialize(BankData value) {
            JSONObject json = new JSONObject();

            List<JSONObject> transactions = new ArrayList<>();
            for (Transaction transaction : value.transactions) {
                JSONObject transactionJson = new JSONObject();
                transactionJson.put("timestamp", transaction.timestamp);
                transactionJson.put("amount", transaction.amount);
                transactionJson.put("originator", transaction.originator);
                transactions.add(transactionJson);
            }
            json.put("transactions", transactions);
            json.put("lastClaimedInterest", value.lastClaimedInterest);
            json.put("amount", value.amount);
            json.put("balanceLimit", value.balanceLimit);
            json.put("sessionHash", value.sessionHash.toString());

            return json.toString();
        }

        @Override
        public BankData deserialize(String json) {
            JSONObject jsonObject = new JSONObject(json);

            List<Transaction> transactions = new ArrayList<>();
            for (Object transaction : jsonObject.getJSONArray("transactions")) {
                JSONObject transactionJson = (JSONObject) transaction;
                transactions.add(new Transaction(transactionJson.getLong("timestamp"),
                        transactionJson.getDouble("amount"), transactionJson.getString("originator")));
            }

            return new BankData(jsonObject.optLong("lastClaimedInterest", System.currentTimeMillis()), transactions,
                    UUID.fromString(jsonObject.getString("sessionHash")), jsonObject.getDouble("amount"),
                    jsonObject.getDouble("balanceLimit"));
        }

        @Override
        public BankData clone(BankData value) {
            return new BankData(value.lastClaimedInterest, new ArrayList<>(value.transactions),
                    UUID.randomUUID(), value.amount, value.balanceLimit);
        }
    };

    public DatapointBankData(String key, BankData value, Serializer<BankData> serializer) {
        super(key, value, serializer);
    }

    public DatapointBankData(String key, BankData value) {
        super(key, value, serializer);
    }

    public DatapointBankData(String key) {
        super(key, new BankData(SkyBlockCalendar.getElapsed(), new ArrayList<>(),
                UUID.randomUUID(), 0, 50000000), serializer);
    }

    @AllArgsConstructor
    @Getter
    @Setter
    public static class BankData {
        private long lastClaimedInterest;
        private List<Transaction> transactions;
        private UUID sessionHash;
        private double amount;
        private double balanceLimit;

        public void removeAmount(double amount) {
            this.amount -= amount;
        }

        public void addAmount(double amount) {
            this.amount += amount;
        }

        public void addTransaction(Transaction transaction) {
            transactions.add(transaction);
        }
    }

    @AllArgsConstructor
    public static class Transaction {
        public long timestamp;
        public double amount;
        public String originator;
    }
}
