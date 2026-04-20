package net.swofty.type.skyblockgeneric.gui.inventories.fishing;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.fishing.FishingItemSupport;
import net.swofty.type.skyblockgeneric.fishing.FishingShipService;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class GUIFishingShip extends HypixelInventoryGUI {

    public GUIFishingShip() {
        super("{Fishing Ship}", InventoryType.CHEST_5_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(FILLER_ITEM);
        set(new GUIItem(4) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                SkyBlockPlayer skyBlockPlayer = (SkyBlockPlayer) player;
                var state = FishingShipService.getState(skyBlockPlayer);
                return ItemStackCreator.getStack(
                    "§6{Fishing Ship}",
                    Material.OAK_BOAT,
                    1,
                    "§7Your §6Ship §7will help you travel to",
                    "§7different §9fishing islands §7in SkyBlock.",
                    "",
                    "§7For now, it can only get you to the",
                    "§2Backwater Bayou§7.",
                    "",
                    "§7Helm: §f" + resolvePartName(state.getHelm(), "Cracked Ship Helm"),
                    "§7Engine: §f" + resolvePartName(state.getEngine(), "Missing Engine"),
                    "§7Hull: §f" + resolvePartName(state.getHull(), "Rusty Ship Hull")
                );
            }
        });
        set(new GUIItem(21) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                SkyBlockPlayer skyBlockPlayer = (SkyBlockPlayer) player;
                return buildShipPartStack(
                    FishingShipService.getState(skyBlockPlayer).getHelm(),
                    "Cracked Ship Helm",
                    "d8d4a54d1fcf47b2efc99ba4cc772250aee5c2f26ed1a19052213e0f3323ca1d",
                    "§7A cracked ship helm, incapable of",
                    "§7changing its heading which appears",
                    "§7due east."
                );
            }
        });
        set(new GUIItem(22) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                SkyBlockPlayer skyBlockPlayer = (SkyBlockPlayer) player;
                return buildShipPartStack(
                    FishingShipService.getState(skyBlockPlayer).getEngine(),
                    "Missing Engine",
                    "53e84793917c890f7f8a2c4078a29e8ba939790498727af9342c2b6f6ac43c9c",
                    "§7This ship still needs an engine before",
                    "§7it can get you anywhere."
                );
            }
        });
        set(new GUIItem(23) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                SkyBlockPlayer skyBlockPlayer = (SkyBlockPlayer) player;
                return buildShipPartStack(
                    FishingShipService.getState(skyBlockPlayer).getHull(),
                    "Rusty Ship Hull",
                    "f42d53ca6e7d80a99a699c2036dcf6e233394feb9f46fb2ff9d9a819690894a9",
                    "§7A hull rusted and dilapidated beyond",
                    "§7repair. It's a miracle the ship",
                    "§7remains afloat."
                );
            }
        });
        set(new GUIClickableItem(31) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                SkyBlockPlayer skyBlockPlayer = (SkyBlockPlayer) player;
                boolean unlocked = FishingShipService.getState(skyBlockPlayer).hasDestination("BACKWATER_BAYOU");
                return ItemStackCreator.getStack(
                    unlocked ? "§aOpen Navigator" : "§cNavigator Locked",
                    Material.COMPASS,
                    1,
                    "§7Choose where to set sail next.",
                    "",
                    unlocked ? "§eClick to browse destinations!" : "§cInstall an engine first."
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                SkyBlockPlayer skyBlockPlayer = (SkyBlockPlayer) player;
                if (!FishingShipService.getState(skyBlockPlayer).hasDestination("BACKWATER_BAYOU")) {
                    player.sendMessage("§cYour ship cannot travel anywhere yet.");
                    return;
                }
                skyBlockPlayer.openView(new GUINavigator());
            }
        });
        set(new GUIItem(44) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                SkyBlockPlayer skyBlockPlayer = (SkyBlockPlayer) player;
                return ItemStackCreator.getStack(
                    "§aRename Ship",
                    Material.NAME_TAG,
                    1,
                    "§7You may be going on long voyages",
                    "§7with your §6Ship§7, best to give it a name!",
                    "",
                    "§7Current Name: §6" + FishingShipService.getState(skyBlockPlayer).getShipName(),
                    "",
                    "§7Renaming is not implemented yet."
                );
            }
        });
        set(GUIClickableItem.getCloseItem(40));
        updateItemStacks(getInventory(), getPlayer());
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }

    private static String resolvePartName(String itemId, String fallback) {
        var definition = FishingItemSupport.getShipPart(itemId);
        return definition == null ? fallback : definition.getDisplayName();
    }

    private static ItemStack.Builder buildShipPartStack(String itemId, String fallbackName, String fallbackTexture, String... fallbackLore) {
        var definition = FishingItemSupport.getShipPart(itemId);
        String name = definition == null ? fallbackName : definition.getDisplayName();
        String texture = definition == null || definition.getTexture() == null ? fallbackTexture : definition.getTexture();
        String[] lore = fallbackLore;
        return ItemStackCreator.getStackHead("§f" + name, texture, 1, lore);
    }
}
