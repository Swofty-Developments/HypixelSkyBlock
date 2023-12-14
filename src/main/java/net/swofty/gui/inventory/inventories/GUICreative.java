package net.swofty.gui.inventory.inventories;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemHideFlag;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.gui.inventory.ItemStackCreator;
import net.swofty.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.gui.inventory.item.GUIClickableItem;
import net.swofty.gui.inventory.item.GUIQueryItem;
import net.swofty.item.ItemType;
import net.swofty.item.SkyBlockItem;
import net.swofty.item.updater.NonPlayerItemUpdater;
import net.swofty.user.SkyBlockPlayer;
import net.swofty.utility.PaginationList;

import java.util.List;

public class GUICreative extends SkyBlockInventoryGUI
{
      private static final int[] _Interior = new int[]{
              10, 11, 12, 13, 14, 15, 16,
              19, 20, 21, 22, 23, 24, 25,
              28, 29, 30, 31, 32, 33, 34,
              37, 38, 39, 40, 41, 42, 43
      };

      public GUICreative(String query, int page) {
            super("Item List", InventoryType.CHEST_6_ROW);
            border(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, ""));

            PaginationList<ItemType> paged = new PaginationList<>(28);
            paged.addAll(ItemType.values());
            if (!query.equals("")) {
                  paged.removeIf(type ->
                          !type.name().toLowerCase().contains(query.replaceAll(" ", "_").toLowerCase()));
            }
            if (paged.size() == 0) page = 0;
            this.title = "Creative Menu | Page " + page + "/" + paged.getPageCount();
            int finalPage = page;
            if (page > 1) {
                  set(new GUIClickableItem()
                  {
                        @Override
                        public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                              new GUICreative(query, finalPage - 1).open(player);
                        }

                        @Override
                        public int getSlot() {
                              return 45;
                        }

                        @Override
                        public ItemStack.Builder getItem(SkyBlockPlayer player) {
                              return ItemStackCreator.createNamedItemStack(Material.ARROW, "&a<-");
                        }
                  });
            }

            if (page != paged.getPageCount()) {
                  set(new GUIClickableItem()
                  {
                        @Override
                        public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                              new GUICreative(query, finalPage + 1).open(player);
                        }

                        @Override
                        public int getSlot() {
                              return 53;
                        }

                        @Override
                        public ItemStack.Builder getItem(SkyBlockPlayer player) {
                              return ItemStackCreator.createNamedItemStack(Material.ARROW, "&a->");
                        }
                  });
            }

            set(new GUIQueryItem()
            {
                  @Override
                  public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {

                  }

                  @Override
                  public SkyBlockInventoryGUI onQueryFinish(String query, SkyBlockPlayer player) {
                        return new GUICreative(query, 1);
                  }

                  @Override
                  public int getSlot() {
                        return 48;
                  }

                  @Override
                  public ItemStack.Builder getItem(SkyBlockPlayer player) {
                        return ItemStackCreator.createNamedItemStack(Material.BIRCH_SIGN, "§aSearch");
                  }
            });
            set(GUIClickableItem.getCloseItem(50));
            List<ItemType> thisPage = paged.getPage(page);
            if (thisPage == null) return;

            for (int i = 0; i < thisPage.size(); i++) {
                  int slot = _Interior[i];
                  SkyBlockItem item = new SkyBlockItem(thisPage.get(i));
                  set(new GUIClickableItem()
                  {
                        @Override
                        public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                              player.playSound(Sound.sound(Key.key("block.note_block.pling"), Sound.Source.PLAYER, 1.0f, 2.0f));
                              player.sendMessage("§aGave you a §e" + item.getAttributeHandler().getItemType() + "§a.");
                              player.getInventory().addItemStack(item.getItemStack());
                        }

                        @Override
                        public int getSlot() {
                              return slot;
                        }

                        @Override
                        public ItemStack.Builder getItem(SkyBlockPlayer player) {
                              return new NonPlayerItemUpdater(item).getUpdatedItem();
                        }
                  });
            }
      }

      @Override
      public boolean allowHotkeying() {
            return false;
      }

      @Override
      public void onClose(InventoryCloseEvent e, CloseReason reason) {

      }

      @Override
      public void suddenlyQuit(SkyBlockPlayer player) {

      }

      @Override
      public void onBottomClick(InventoryPreClickEvent e) {

      }
}
