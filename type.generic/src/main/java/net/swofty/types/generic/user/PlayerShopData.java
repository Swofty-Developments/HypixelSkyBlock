package net.swofty.types.generic.user;

import net.minestom.server.item.Material;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.utility.Tuple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerShopData {
    public static final int MAXIMUM_STOCK = 640;

    private final Map<String, Tuple<Integer, Long>> itemData;
    private final List<Tuple<SkyBlockItem, Integer>> buybackData;

    public PlayerShopData() {
        this.itemData = new HashMap<>();
        this.buybackData = new ArrayList<>();
    }

    public boolean canPurchase(SkyBlockItem item, int amount) {
        updateStock(item);
        return getStock(item) >= amount;
    }

    public long getLastPurchase(SkyBlockItem item) {
        if (!itemData.containsKey(item.getAttributeHandler().getItemType()))
            return 0;
        return itemData.get(item.getAttributeHandler().getItemType()).getValue();
    }

    public int getStock(SkyBlockItem item) {
        if (!itemData.containsKey(item.getAttributeHandler().getItemType()))
            return MAXIMUM_STOCK;
        updateStock(item);

        return MAXIMUM_STOCK - itemData.get(item.getAttributeHandler().getItemType()).getKey();
    }

    private boolean updateStock(SkyBlockItem item) {
        if ((System.currentTimeMillis() - getLastPurchase(item)) > 1200000) {
            resetStock(item);
            return true;
        }
        return false;
    }

    public void documentPurchase(SkyBlockItem item, int amount) {
        Tuple<Integer, Long> data;
        if (itemData.containsKey(item.getAttributeHandler().getItemType())) {
            data = itemData.get(item.getAttributeHandler().getItemType());
            data.setKey(data.getKey() + amount);
            data.setValue(System.currentTimeMillis());
        } else {
            data = new Tuple<>(amount, System.currentTimeMillis());
        }
        itemData.put(item.getAttributeHandler().getItemType(), data);
    }

    private void resetStock(SkyBlockItem item) {
        itemData.put(item.getAttributeHandler().getItemType(), new Tuple<>(0, System.currentTimeMillis()));
    }

    public void resetStocks() {
        itemData.keySet().forEach(it -> itemData.put(it, new Tuple<>(0, System.currentTimeMillis())));
    }

    public int pushBuyback(SkyBlockItem item, int amount) {
        buybackData.add(new Tuple<>(item, amount));
        return buybackData.size() - 1;
    }

    public Tuple<SkyBlockItem, Integer> popBuyback() {
        if (buybackData.isEmpty())
            throw new IndexOutOfBoundsException("Woah there!");
        Tuple<SkyBlockItem, Integer> last = buybackData.get(buybackData.size() - 1);
        buybackData.remove(buybackData.size() - 1);
        return last;
    }

    public Tuple<SkyBlockItem, Integer> lastBuyback() {
        if (buybackData.isEmpty())
            throw new IndexOutOfBoundsException("Woah there!");
        return buybackData.get(buybackData.size() - 1);
    }

    public Tuple<SkyBlockItem, Integer> firstBuyback() {
        if (buybackData.isEmpty())
            throw new IndexOutOfBoundsException("Woah there!");
        return buybackData.get(0);
    }

    public boolean hasAnythingToBuyback() {
        return buybackData.size() > 0;
    }

    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> itemDataM = new HashMap<>();
        Map<String, Object> buybacks = new HashMap<>();
        itemData.forEach((item, stockNTime) -> {
            itemDataM.put(item, stockNTime.getKey() + "/" + stockNTime.getValue());
        });
        buybackData.forEach((tuple) -> buybacks.put(tuple.getKey().getAttributeHandler().getItemType(), tuple.getValue()));
        map.put("purchases", itemDataM);
        map.put("buyback", buybacks);
        return map;
    }

    public void deserialize(Map<String, Object> map) {
        ((Map<String, Object>) map.get("purchases")).forEach((itemID, stockNTimeO) -> {
            String stockNTime = stockNTimeO.toString();
            Integer stockBought = Integer.parseInt(stockNTime.split("/")[0]);
            Long lastPurchase = Long.parseLong(stockNTime.split("/")[1]);

            itemData.put(itemID, new Tuple<>(stockBought, lastPurchase));
        });

        ((Map<String, Object>) map.get("buyback")).forEach((itemID, amount) -> {
            SkyBlockItem item;
            if (ItemType.get(itemID) != null)
                item = new SkyBlockItem(ItemType.get(itemID));
            else
                item = new SkyBlockItem(Material.fromNamespaceId(itemID));

            buybackData.add(new Tuple<>(item, Integer.parseInt(amount.toString())));
        });
    }
}
