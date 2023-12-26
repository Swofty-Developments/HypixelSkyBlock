package net.swofty.user;

import net.minestom.server.item.Material;
import net.swofty.gui.inventory.SkyBlockShopGUI;
import net.swofty.item.ItemType;
import net.swofty.item.SkyBlockItem;
import net.swofty.utility.Tuple;

import java.util.*;

public class PlayerShopData
{
      public static final int MAXIMUM_STOCK = 640;

      private final Map<String, Tuple<Integer, Long>> itemData;
      private final List<Tuple<SkyBlockItem, Integer>> buybackData;

      public PlayerShopData() {
            this.itemData = new HashMap<>();
            this.buybackData = new ArrayList<>();
      }

      public List<Tuple<SkyBlockItem, Integer>> getBuybackData() {
            return buybackData;
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
            Tuple<Integer, Long> t;
            if (itemData.containsKey(item.getAttributeHandler().getItemType())) {
                  t = itemData.get(item.getAttributeHandler().getItemType());
                  t.setKey(t.getKey() + amount);
                  t.setValue(System.currentTimeMillis());
            } else {
                  t = new Tuple<>(amount, System.currentTimeMillis());
            }
            itemData.put(item.getAttributeHandler().getItemType(), t);
      }

      private void resetStock(SkyBlockItem item) {
            itemData.put(item.getAttributeHandler().getItemType(), new Tuple<>(0, System.currentTimeMillis()));
      }

      public void resetStocks() {
            itemData.keySet().forEach(it -> itemData.put(it, new Tuple<>(0, System.currentTimeMillis())));
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
