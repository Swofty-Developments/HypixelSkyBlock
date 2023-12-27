package net.swofty.gui.inventory;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.data.DataHandler;
import net.swofty.data.datapoints.DatapointDouble;
import net.swofty.gui.inventory.inventories.shop.TradingOptionsGUI;
import net.swofty.gui.inventory.item.GUIClickableItem;
import net.swofty.item.SkyBlockItem;
import net.swofty.item.impl.Sellable;
import net.swofty.item.updater.NonPlayerItemUpdater;
import net.swofty.item.updater.PlayerItemUpdater;
import net.swofty.user.SkyBlockPlayer;
import net.swofty.utility.PaginationList;
import net.swofty.utility.StringUtility;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class SkyBlockShopGUI extends SkyBlockInventoryGUI {
      private static final int[] INTERIOR = new int[]{
              10, 11, 12, 13, 14, 15, 16,
              19, 20, 21, 22, 23, 24, 25,
              28, 29, 30, 31, 32, 33, 34,
              37, 38, 39, 40, 41, 42, 43
      };

      private final List<ShopItem> shopItemList;
      private int page;

      public SkyBlockShopGUI(String title, int page) {
            super(title, InventoryType.CHEST_6_ROW);
            this.shopItemList = new ArrayList<>();
            this.page = page;
            initializeShopItems();
      }

      @Override
      public void onOpen(InventoryGUIOpenEvent e) {
            border(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, " "));
            PaginationList<ShopItem> paginatedItems = new PaginationList<>(INTERIOR.length);
            paginatedItems.addAll(shopItemList);

            updateItemStacks(e.inventory(), getPlayer());

            if (paginatedItems.isEmpty()) page = 0;
            if (page > 1)
                  set(new GUIClickableItem() {
                        @Override
                        public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                              SkyBlockShopGUI.this.page -= 1;
                              SkyBlockShopGUI.this.open(player);
                        }

                        @Override
                        public int getSlot() {
                              return 45;
                        }

                        @Override
                        public ItemStack.Builder getItem(SkyBlockPlayer player) {
                              return ItemStackCreator.createNamedItemStack(Material.ARROW, "§a<-");
                        }
                  });

            if (page != paginatedItems.getPageCount())
                  set(new GUIClickableItem() {
                        @Override
                        public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                              SkyBlockShopGUI.this.page += 1;
                              SkyBlockShopGUI.this.open(player);
                        }

                        @Override
                        public int getSlot() {
                              return 53;
                        }

                        @Override
                        public ItemStack.Builder getItem(SkyBlockPlayer player) {
                              return ItemStackCreator.createNamedItemStack(Material.ARROW, "§a->");
                        }
                  });

            set(new GUIClickableItem()
            {
                  @Override
                  public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                        if (!player.getShoppingData().hasAnythingToBuyback())
                              return;

                        SkyBlockItem last = player.getShoppingData().lastBuyback().getKey();
                        int amountOfLast = player.getShoppingData().lastBuyback().getValue();
                        ItemStack.Builder itemStack = PlayerItemUpdater.playerUpdate(
                                player, null, last.getItemStackBuilder().build()
                        );
                        itemStack.amount(amountOfLast);

                        double value = (last.getGenericInstance() instanceof Sellable ? ((Sellable) last.getGenericInstance()).getSellValue() : 1)
                                * amountOfLast;

                        double playerCoins = player.getDataHandler().get(DataHandler.Data.COINS, DatapointDouble.class).getValue();
                        if (playerCoins < value) {
                              player.sendMessage("§cYou don't have enough coins!");
                              return;
                        }
                        boolean canHave = player.getInventory().addItemStack(itemStack.build());
                        if (!canHave) {
                              player.sendMessage("§cYou need to free up inventory space in order to buyback this item!");
                              return;
                        }
                        player.playSuccessSound();
                        player.getShoppingData().popBuyback();
                        player.getDataHandler().get(DataHandler.Data.COINS, DatapointDouble.class).setValue(playerCoins - value);
                        updateThis(player);
                  }

                  @Override
                  public int getSlot() {
                        return 49;
                  }

                  @Override
                  public ItemStack.Builder getItem(SkyBlockPlayer player) {
                        if (!player.getShoppingData().hasAnythingToBuyback()) {
                              return ItemStackCreator.getStack("§aSell Item", Material.HOPPER, (short) 0, 1,
                                      "§7Click items in your inventory to",
                                      "§7sell them to this Shop!");
                        }

                        SkyBlockItem last = player.getShoppingData().lastBuyback().getKey();
                        int amountOfLast = player.getShoppingData().lastBuyback().getValue();
                        ItemStack.Builder itemStack = PlayerItemUpdater.playerUpdate(
                                player, null, last.getItemStackBuilder().build()
                        );

                        double buyBackPrice = (last.getGenericInstance() instanceof Sellable ? ((Sellable) last.getGenericInstance()).getSellValue() : 1)
                                * amountOfLast;

                        List<String> lore = new ArrayList<>(itemStack.build().getLore()
                                .stream()
                                .map(StringUtility::getTextFromComponent)
                                .toList());
                        lore.add("");
                        lore.add("§7Cost");
                        lore.add("§6 " + StringUtility.commaify(buyBackPrice) + " Coin" + (buyBackPrice != 1 ? "s" : ""));
                        lore.add("");
                        lore.add("§eClick to buyback!");

                        itemStack.amount(amountOfLast);
                        return itemStack.lore(lore.stream().map(
                                line -> Component.text(line).decoration(TextDecoration.ITALIC, false)
                        ).toList());
                  }
            });

            updateItemStacks(e.inventory(), getPlayer());

            List<ShopItem> p = paginatedItems.getPage(page);
            if (p == null) return;
            for (int i = 0; i < p.size(); i++) {
                  int slot = INTERIOR[i];
                  ShopItem item = p.get(i);
                  SkyBlockItem sbItem = item.item;
                  double price = item.price * item.amount;
                  double stackPrice = item.price / item.modifier;
                  if (stackPrice < 1) {
                        stackPrice = 1;
                  }
                  double finalStackPrice = stackPrice;

                  set(new GUIClickableItem() {
                        @Override
                        public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                              if (!player.getShoppingData().canPurchase(item.item, item.amount)) {
                                    player.sendMessage("§cYou have reached the maximum amount of items you can buy!");
                                    return;
                              }

                              if (item.stackable() && e.getClickType().equals(ClickType.RIGHT_CLICK)) {
                                    new TradingOptionsGUI(item, SkyBlockShopGUI.this, finalStackPrice).open(player);
                                    return;
                              }

                              double purse = player.getDataHandler().get(DataHandler.Data.COINS, DatapointDouble.class).getValue();
                              if (price > purse) {
                                    player.sendMessage("§cYou don't have enough coins!");
                                    return;
                              }
                              ItemStack.Builder cleanStack = new NonPlayerItemUpdater(sbItem).getUpdatedItem();
                              cleanStack.amount(item.amount);
                              player.getInventory().addItemStack(cleanStack.build());
                              player.playSound(Sound.sound(Key.key("block.note_block.pling"), Sound.Source.PLAYER, 1.0f, 2.0f));
                              player.getDataHandler().get(DataHandler.Data.COINS, DatapointDouble.class).setValue(purse - price);
                              player.getShoppingData().documentPurchase(item.item(), item.amount);
                              updateThis(player);
                        }

                        @Override
                        public int getSlot() {
                              return slot;
                        }

                        @Override
                        public ItemStack.Builder getItem(SkyBlockPlayer player) {
                              ItemStack.Builder itemStack = PlayerItemUpdater.playerUpdate(
                                      player, null, sbItem.getItemStackBuilder().build()
                              );

                              List<String> lore = new ArrayList<>(itemStack.build().getLore()
                                      .stream()
                                      .map(StringUtility::getTextFromComponent)
                                      .toList());

                              lore.add("");
                              lore.add("§7Cost");
                              lore.add("§6 " + StringUtility.commaify(price) + " Coin" + (price != 1 ? "s" : ""));
                              lore.add("");
                              lore.add("§7Stock");
                              lore.add("§6 " + getPlayer().getShoppingData().getStock(item.item()) + " §7remaining");
                              lore.add("");
                              lore.add("§eClick to trade!");

                              if (item.stackable)
                                    lore.add("§eRight-click for more trading options!");

                              return itemStack.lore(lore.stream().map(
                                        line -> Component.text(line).decoration(TextDecoration.ITALIC, false)
                              ).toList());
                        }
                  });
            }
            updateItemStacks(e.inventory(), getPlayer());
      }

      @Override
      public void onBottomClick(InventoryPreClickEvent e) {
            ItemStack stack = e.getClickedItem();
            e.getPlayer().sendMessage("btm clock");
            e.setCancelled(true);
            if (stack.getMaterial().equals(Material.AIR)) return;
            SkyBlockItem item = new SkyBlockItem(stack);

            double sellprice = 1;
            if (item.getGenericInstance() instanceof Sellable)
                  sellprice = ((Sellable) item.getGenericInstance()).getSellValue();
            sellprice *= stack.getAmount();

            getPlayer().getShoppingData().pushBuyback(item, stack.getAmount());
            getPlayer().getDataHandler().get(DataHandler.Data.COINS, DatapointDouble.class).setValue(
                    getPlayer().getDataHandler().get(DataHandler.Data.COINS, DatapointDouble.class).getValue() + sellprice
            );
            getPlayer().sendMessage("§aYou sold §f" + StringUtility.getTextFromComponent(stack.getDisplayName()) + " §8x" + stack.getAmount() + "§a for §6"
            + StringUtility.commaify(sellprice) + " Coin" + (sellprice != 1 ? "s" : "") + "§a!");
            getPlayer().getInventory().setItemStack(e.getSlot(), ItemStack.AIR);
            updateThis(getPlayer());
      }

      public abstract void initializeShopItems();

      public void attachItem(ShopItem i) {
            shopItemList.add(i);
      }

      private void updateThis(SkyBlockPlayer player) {
            SkyBlockShopGUI.this.open(player);
      }

      public record ShopItem(SkyBlockItem item, int amount, double price, double modifier, boolean stackable) {
            public static ShopItem Stackable(SkyBlockItem item, int amount, double price, double modifier) {
                  return new ShopItem(item, amount, price, modifier, true);
            }

            public static ShopItem Single(SkyBlockItem item, int amount, double price, double modifier) {
                  return new ShopItem(item, amount, price, modifier, false);
            }
      }
}
