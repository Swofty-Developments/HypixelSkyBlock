package net.swofty.types.generic.gui.inventory.inventories;

import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointLong;
import net.swofty.types.generic.data.datapoints.DatapointSkyBlockExperience;
import net.swofty.types.generic.data.datapoints.DatapointString;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.gui.inventory.item.GUIItem;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.updater.NonPlayerItemUpdater;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.StringUtility;

import java.util.ArrayList;
import java.util.List;

public class GUIViewPlayerProfile extends SkyBlockInventoryGUI {
    private SkyBlockPlayer viewedPlayer;
    public GUIViewPlayerProfile(SkyBlockPlayer viewedPlayer) {
        super(viewedPlayer.getUsername() + "'s Profile" , InventoryType.CHEST_6_ROW);
        this.viewedPlayer = viewedPlayer;
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getCloseItem(49));

        set(new GUIItem(2) { //Held Item
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                if (!viewedPlayer.getItemInMainHand().isAir()) {
                    return ItemStackCreator.getFromStack(viewedPlayer.getItemInMainHand());
                } else {
                    return ItemStackCreator.getStack("§7Empty Held Item Slot", Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1);
                }
            }
        });
        set(new GUIItem(11) { //Helmet
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                if (!viewedPlayer.getHelmet().isAir()) {
                    return ItemStackCreator.getFromStack(viewedPlayer.getHelmet());
                } else {
                    return ItemStackCreator.getStack("§7Empty Helmet Slot", Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1);
                }
            }
        });
        set(new GUIItem(20) { //Chestplate
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                if (!viewedPlayer.getChestplate().isAir()) {
                    return ItemStackCreator.getFromStack(viewedPlayer.getChestplate());
                } else {
                    return ItemStackCreator.getStack("§7Empty Chestplate Slot", Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1);
                }
            }
        });
        set(new GUIItem(29) { //Leggings
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                if (!viewedPlayer.getLeggings().isAir()) {
                    return ItemStackCreator.getFromStack(viewedPlayer.getLeggings());
                } else {
                    return ItemStackCreator.getStack("§7Empty Leggings Slot", Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1);
                }
            }
        });
        set(new GUIItem(38) { //Boots
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                if (!viewedPlayer.getBoots().isAir()) {
                    return ItemStackCreator.getFromStack(viewedPlayer.getBoots());
                } else {
                    return ItemStackCreator.getStack("§7Empty Boots Slot", Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1);
                }
            }
        });
        set(new GUIItem(47) { //Pet
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                if (viewedPlayer.getPetData().getEnabledPet() != null && !viewedPlayer.getPetData().getEnabledPet().getItemStack().isAir()) {
                    SkyBlockItem pet = viewedPlayer.getPetData().getEnabledPet();
                    ItemStack.Builder itemStack = new NonPlayerItemUpdater(pet).getUpdatedItem();
                    return itemStack;
                } else {
                    return ItemStackCreator.getStack("§7Empty Pet Slot", Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1);
                }
            }
        });
        set(new GUIItem(22) { //Player Stats
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                DataHandler dataHandler = viewedPlayer.getDataHandler();
                String age = StringUtility.profileAge(System.currentTimeMillis() - dataHandler.get(DataHandler.Data.CREATED, DatapointLong.class).getValue());
                List<String> lore = new ArrayList<>(List.of());


                lore.add("§7 ");
                lore.add("§7SkyBlock Level: " + viewedPlayer.getSkyBlockExperience().getLevel().getColor() + viewedPlayer.getSkyBlockExperience().getLevel());
                lore.add("§7 ");
                lore.add("§7Oldest Profile: §5" + age);

                return ItemStackCreator.getStackHead(viewedPlayer.getShortenedDisplayName(),
                        PlayerSkin.fromUuid(viewedPlayer.getUuid().toString()), 1,
                        lore);
            }
        });
        set(new GUIClickableItem(31) { //Emblem
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                player.sendMessage("§cThis feature is not added yet.");
            }
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                List<String> lore = new ArrayList<>(List.of());
                DataHandler dataHandler = viewedPlayer.getDataHandler();
                String name;
                Material material;
                if (dataHandler.get(DataHandler.Data.SKYBLOCK_EXPERIENCE, DatapointSkyBlockExperience.class).getValue().getCurrentEmblem() != null) {
                    name = "§fSelected Emblem: " + dataHandler.get(DataHandler.Data.SKYBLOCK_EXPERIENCE, DatapointSkyBlockExperience.class).getValue().getCurrentEmblem().toString();
                    material = dataHandler.get(DataHandler.Data.SKYBLOCK_EXPERIENCE, DatapointSkyBlockExperience.class).getValue().getEmblem().displayMaterial();
                    lore.add(" ");
                    lore.add("§eClick to view unlocked emblems!");
                } else {
                    name = "§cNo Selected Emblem";
                    material = Material.BARRIER;
                    lore.add("§fThis player does not have any");
                    lore.add("§femblem selected.");
                    lore.add(" ");
                    lore.add("§eClick to view unlocked emblems!");
                }
                return ItemStackCreator.getStack(name, material, 1, lore);
            }
        });
        set(new GUIClickableItem(15) { //Visit Island
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                player.sendMessage("§cThis feature is not added yet.");
            }
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack("§aVisit Island", Material.FEATHER, 1, "§eClick to visit!");
            }
        });
        set(new GUIClickableItem(16) { //Trade Request
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                player.sendMessage("§cThis feature is not added yet.");
            }
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack("§aTrade Request", Material.EMERALD, 1, "§eSend a trade request!");
            }
        });
        set(new GUIClickableItem(24) { //Invite to Island
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                player.sendMessage("§cThis feature is not added yet.");
            }
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack("§aInvite to Island", Material.POPPY, 1, "§eClick to invite!");
            }
        });
        set(new GUIClickableItem(25) { //Coop Request
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                player.sendMessage("§cThis feature is not added yet.");
            }
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack("§aCo-op Request", Material.DIAMOND, 1, "§eSend a co-op request!");
            }
        });
        set(new GUIClickableItem(33) { //Personal Vault
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                player.sendMessage("§cThis feature is not added yet.");
            }
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack("§aPersonal Vault", Material.ENDER_CHEST, 1, "§eClick to view!");
            }
        });
        set(new GUIClickableItem(34) { //Museum
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                player.sendMessage("§cThis feature is not added yet.");
            }
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                List<String> lore = new ArrayList<>(List.of());
                DataHandler dataHandler = viewedPlayer.getDataHandler();
                lore.add("§fProfile: §a" + dataHandler.get(DataHandler.Data.PROFILE_NAME, DatapointString.class).getValue());
                lore.add(" ");
                lore.add("§fItems Donated: §bMuseum not there yet :)");
                lore.add(" ");
                lore.add("§eClick to visit!");
                return ItemStackCreator.getStackHead(viewedPlayer.getUsername() + "'s Museum",
                        PlayerSkin.fromUuid(viewedPlayer.getUuid().toString()), 1,
                        lore);
            }
        });
        updateItemStacks(getInventory(), getPlayer());
    }
    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent e, CloseReason reason) {}

    @Override
    public void suddenlyQuit(Inventory inventory, SkyBlockPlayer player) {}

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}
