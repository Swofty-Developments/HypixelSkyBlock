package net.swofty.type.skyblockgeneric.data.datapoints;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.swofty.commons.protocol.Serializer;
import net.swofty.commons.protocol.serializers.UnderstandableSkyBlockItemSerializer;
import net.swofty.type.skyblockgeneric.bank.BankAccountTier;
import net.swofty.type.skyblockgeneric.calendar.SkyBlockCalendar;
import net.swofty.type.skyblockgeneric.data.SkyBlockDatapoint;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import org.json.JSONArray;
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
            json.put("accountTier", value.accountTier.name());
            json.put("lastInterest", value.lastInterest);
            json.put("museumMilestone", value.museumMilestone);
            json.put("personalBankLevel", value.personalBankLevel);
            json.put("lastRemoteBankUse", value.lastRemoteBankUse);
            json.put("personalVaultUnlocked", value.personalVaultUnlocked);
            List<String> vault = new ArrayList<>();
            for (SkyBlockItem item : value.personalVault) {
                vault.add(item == null || item.isNA() ? "null" : item.toUnderstandable().serialize());
            }
            json.put("personalVault", vault);

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

            BankAccountTier tier;
            try {
                tier = BankAccountTier.valueOf(jsonObject.optString("accountTier", ""));
            } catch (IllegalArgumentException ignored) {
                tier = BankAccountTier.fromLegacyLimit(jsonObject.optDouble("balanceLimit", 50_000_000D));
            }
            SkyBlockItem[] vault = new SkyBlockItem[27];
            JSONArray savedVault = jsonObject.optJSONArray("personalVault");
            if (savedVault != null) {
                for (int i = 0; i < Math.min(vault.length, savedVault.length()); i++) {
                    String item = savedVault.optString(i, "null");
                    if (!item.equals("null")) {
                        vault[i] = new SkyBlockItem(new UnderstandableSkyBlockItemSerializer().deserialize(item));
                    }
                }
            }
            return new BankData(jsonObject.optLong("lastClaimedInterest", SkyBlockCalendar.getElapsed()), transactions,
                jsonObject.optDouble("amount", 0), tier, jsonObject.optDouble("lastInterest", 0),
                Math.min(30, Math.max(0, jsonObject.optInt("museumMilestone", 0))),
                Math.min(3, Math.max(0, jsonObject.optInt("personalBankLevel", 0))),
                jsonObject.optLong("lastRemoteBankUse", 0), jsonObject.optBoolean("personalVaultUnlocked", false), vault);
        }

        @Override
        public BankData clone(BankData value) {
            return new BankData(value.lastClaimedInterest, new ArrayList<>(value.transactions), value.amount,
                value.accountTier, value.lastInterest, value.museumMilestone, value.personalBankLevel,
                value.lastRemoteBankUse, value.personalVaultUnlocked, value.personalVault.clone());
        }
    };

    public DatapointBankData(String key, BankData value, Serializer<BankData> serializer) {
        super(key, value, serializer);
    }

    public DatapointBankData(String key, BankData value) {
        super(key, value, serializer);
    }

    public DatapointBankData(String key) {
        super(key, new BankData(SkyBlockCalendar.getElapsed(), new ArrayList<>(), 0, BankAccountTier.STARTER,
            0, 0, 0, 0, false, new SkyBlockItem[27]), serializer);
    }

    @AllArgsConstructor
    @Getter
    @Setter
    public static class BankData {
        private long lastClaimedInterest;
        private List<Transaction> transactions;
        private double amount;
        private BankAccountTier accountTier;
        private double lastInterest;
        private int museumMilestone;
        private int personalBankLevel;
        private long lastRemoteBankUse;
        private boolean personalVaultUnlocked;
        private SkyBlockItem[] personalVault;

        public double getBalanceLimit() {
            return accountTier.getCapacity();
        }

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
