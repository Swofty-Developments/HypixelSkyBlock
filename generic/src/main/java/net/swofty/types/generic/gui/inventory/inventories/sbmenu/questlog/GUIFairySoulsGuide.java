package net.swofty.types.generic.gui.inventory.inventories.sbmenu.questlog;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.data.mongodb.FairySoulDatabase;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.gui.inventory.item.GUIItem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.fairysouls.FairySoul;
import net.swofty.types.generic.user.fairysouls.FairySoulZone;

public class GUIFairySoulsGuide extends SkyBlockInventoryGUI {

    private static final int[] LOCATION_SLOTS = {
                11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24
    };

    public GUIFairySoulsGuide() {
        super("Fairy Souls Guide", InventoryType.CHEST_5_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getCloseItem(40));
        set(GUIClickableItem.getGoBackItem(39, new GUIMissionLog()));

        set(new GUIItem(10) {
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                int x = player.getFairySoulHandler().getFound(FairySoulZone.MISC_DUNGEONS) + player.getFairySoulHandler().getFound(FairySoulZone.MISC_FISHING) + player.getFairySoulHandler().getFound(FairySoulZone.MISC_GARDEN) + player.getFairySoulHandler().getFound(FairySoulZone.MISC_PLACEABLE);
                int y = player.getFairySoulHandler().getMax(FairySoulZone.MISC_DUNGEONS) + player.getFairySoulHandler().getMax(FairySoulZone.MISC_FISHING) + player.getFairySoulHandler().getMax(FairySoulZone.MISC_GARDEN) + player.getFairySoulHandler().getMax(FairySoulZone.MISC_PLACEABLE);
                return ItemStackCreator.getStackHead("§dMiscellaneous", "126ec1ca185b47aad39f931db8b0a8500ded86a127a204886ed4b3783ad1775c", 1,
                        "§7Fairy Souls: §e" + x + "§7/§d" + y,
                        " §7Dungeon: §d" + player.getFairySoulHandler().getMax(FairySoulZone.MISC_DUNGEONS),
                        " §7Fishing: §d" + player.getFairySoulHandler().getMax(FairySoulZone.MISC_FISHING),
                        " §7Garden: §d" + player.getFairySoulHandler().getMax(FairySoulZone.MISC_GARDEN),
                        " §7Placeable: §d" + player.getFairySoulHandler().getMax(FairySoulZone.MISC_PLACEABLE));
            }
        });
        set(new GUIItem(11) {
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStackHead("§dDwarven Mines", "6b20b23c1aa2be0270f016b4c90d6ee6b8330a17cfef87869d6ad60b2ffbf3b5", 1,
                        "§7Fairy Souls: §e" + player.getFairySoulHandler().getFound(FairySoulZone.DWARVEN_MINES) + "§7/§d" + player.getFairySoulHandler().getMax(FairySoulZone.DWARVEN_MINES));
            }
        });
        set(new GUIItem(12) {
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStackHead("§dDeep Caverns", "569a1f114151b4521373f34bc14c2963a5011cdc25a6554c48c708cd96ebfc", 1,
                        "§7Fairy Souls: §e" + player.getFairySoulHandler().getFound(FairySoulZone.DEEP_CAVERNS) + "§7/§d" + player.getFairySoulHandler().getMax(FairySoulZone.DEEP_CAVERNS));
            }
        });
        set(new GUIItem(13) {
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStackHead("§dDungeon Hub", "9b56895b9659896ad647f58599238af532d46db9c1b0389b8bbeb70999dab33d", 1,
                        "§7Fairy Souls: §e" + player.getFairySoulHandler().getFound(FairySoulZone.DUNGEON_HUB) + "§7/§d" + player.getFairySoulHandler().getMax(FairySoulZone.DUNGEON_HUB));
            }
        });
        set(new GUIItem(14) {
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStackHead("§dThe Farming Islands", "4d3a6bd98ac1833c664c4909ff8d2dc62ce887bdcf3cc5b3848651ae5af6b", 1,
                        "§7Fairy Souls: §e" + player.getFairySoulHandler().getFound(FairySoulZone.THE_FARMING_ISLANDS) + "§7/§d" + player.getFairySoulHandler().getMax(FairySoulZone.THE_FARMING_ISLANDS));
            }
        });
        set(new GUIItem(15) {
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStackHead("§dCrimson Isle", "c3687e25c632bce8aa61e0d64c24e694c3eea629ea944f4cf30dcfb4fbce071", 1,
                        "§7Fairy Souls: §e" + player.getFairySoulHandler().getFound(FairySoulZone.CRIMSON_ISLE) + "§7/§d" + player.getFairySoulHandler().getMax(FairySoulZone.CRIMSON_ISLE));
            }
        });
        set(new GUIItem(16) {
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStackHead("§dThe End", "e72c04137686295dcd722be04d95a986e64220898500df2ccab9d3b5de55506f", 1,
                        "§7Fairy Souls: §e" + player.getFairySoulHandler().getFound(FairySoulZone.THE_END) + "§7/§d" + player.getFairySoulHandler().getMax(FairySoulZone.THE_END));
            }
        });
        set(new GUIItem(19) {
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStackHead("§dSpider's Den", "c754318a3376f470e481dfcd6c83a59aa690ad4b4dd7577fdad1c2ef08d8aee6", 1,
                        "§7Fairy Souls: §e" + player.getFairySoulHandler().getFound(FairySoulZone.SPIDERS_DEN) + "§7/§d" + player.getFairySoulHandler().getMax(FairySoulZone.SPIDERS_DEN));
            }
        });
        set(new GUIItem(20) {
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStackHead("§dThe Park", "a221f813dacee0fef8c59f76894dbb26415478d9ddfc44c2e708a6d3b7549b", 1,
                        "§7Fairy Souls: §e" + player.getFairySoulHandler().getFound(FairySoulZone.THE_PARK) + "§7/§d" + player.getFairySoulHandler().getMax(FairySoulZone.THE_PARK));
            }
        });
        set(new GUIItem(21) {
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStackHead("§dHub", "686718d85e25b006f2c8f160f619b23c8fd6ae75ddf1c06308ec0f539d931703", 1,
                        "§7Fairy Souls: §e" + player.getFairySoulHandler().getFound(FairySoulZone.HUB) + "§7/§d" + player.getFairySoulHandler().getMax(FairySoulZone.HUB));
            }
        });
        set(new GUIItem(22) {
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStackHead("§dJerry's Workshop", "b96923ad247310007f6ae5d326d847ad53864cf16c3565a181dc8e6b20be2387", 1,
                        "§7Fairy Souls: §e" + player.getFairySoulHandler().getFound(FairySoulZone.JERRYS_WORKSHOP) + "§7/§d" + player.getFairySoulHandler().getMax(FairySoulZone.JERRYS_WORKSHOP));
            }
        });
        set(new GUIItem(23) {
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStackHead("§dGold Mine", "73bc965d579c3c6039f0a17eb7c2e6faf538c7a5de8e60ec7a719360d0a857a9", 1,
                        "§7Fairy Souls: §e" + player.getFairySoulHandler().getFound(FairySoulZone.GOLD_MINE) + "§7/§d" + player.getFairySoulHandler().getMax(FairySoulZone.GOLD_MINE));
            }
        });
        set(new GUIItem(24) {
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStackHead("§dThe Rift", "b96923ad247310007f6ae5d326d847ad53864cf16c3565a181dc8e6b20be2387", 1,
                        "§7Fairy Souls: §e" + player.getFairySoulHandler().getFound(FairySoulZone.THE_RIFT) + "§7/§d" + player.getFairySoulHandler().getMax(FairySoulZone.THE_RIFT));
            }
        });
        updateItemStacks(getInventory(), getPlayer());
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent e, CloseReason reason) {
    }

    @Override
    public void suddenlyQuit(Inventory inventory, SkyBlockPlayer player) {
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}
