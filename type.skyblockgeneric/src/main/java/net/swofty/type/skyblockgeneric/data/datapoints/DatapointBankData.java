package net.swofty.type.skyblockgeneric.data.datapoints;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.swofty.commons.protocol.Serializer;
import net.swofty.type.skyblockgeneric.calendar.SkyBlockCalendar;
import net.swofty.type.skyblockgeneric.data.SkyBlockDatapoint;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DatapointBankData extends SkyBlockDatapoint<DatapointBankData.BankData> {
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
                    jsonObject.getDouble("amount"), jsonObject.getDouble("balanceLimit"));
        }

        @Override
        public BankData clone(BankData value) {
            return new BankData(value.lastClaimedInterest, new ArrayList<>(value.transactions), value.amount, value.balanceLimit);
        }
    };

    public DatapointBankData(String key, BankData value, Serializer<BankData> serializer) {
        super(key, value, serializer);
    }

    public DatapointBankData(String key, BankData value) {
        super(key, value, serializer);
    }

    public DatapointBankData(String key) {
        super(key, new BankData(SkyBlockCalendar.getElapsed(), new ArrayList<>(), 0, 50000000), serializer);
    }

    @AllArgsConstructor
    @Getter
    @Setter
    public static class BankData {
        private long lastClaimedInterest;
        private List<Transaction> transactions;
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
