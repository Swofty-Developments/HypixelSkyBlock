package net.swofty.gui.inventory.inventories.profiles;

import lombok.SneakyThrows;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.data.DataHandler;
import net.swofty.data.datapoints.DatapointDouble;
import net.swofty.data.datapoints.DatapointLong;
import net.swofty.data.datapoints.DatapointString;
import net.swofty.data.mongodb.ProfilesDatabase;
import net.swofty.gui.inventory.ItemStackCreator;
import net.swofty.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.gui.inventory.inventories.GUISkyBlockMenu;
import net.swofty.gui.inventory.item.GUIClickableItem;
import net.swofty.user.SkyBlockPlayer;
import net.swofty.user.UserProfiles;
import net.swofty.utility.StringUtility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class GUIProfileManagement extends SkyBlockInventoryGUI {
    private static final int[] PROFILE_SLOTS = {
            11, 12, 13, 14, 15
    };

    public GUIProfileManagement() {
        super("Profile Management", InventoryType.CHEST_4_ROW);

        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getCloseItem(31));
        set(GUIClickableItem.getGoBackItem(30, new GUISkyBlockMenu()));
    }

    @SneakyThrows
    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        SkyBlockPlayer player = e.player();
        UserProfiles profiles = player.getProfiles();
        List<UUID> profileIds = profiles.getProfiles();

        for (int profileCount = 0; profileCount <= 4; profileCount++) {
            int slot = PROFILE_SLOTS[profileCount];

            if (profileIds.size() <= profileCount) {
                set(new GUIClickableItem() {

                    @Override
                    public int getSlot() {
                        return slot;
                    }

                    @Override
                    public ItemStack.Builder getItem(SkyBlockPlayer player) {
                        return ItemStackCreator.getStack("§eEmpty Profile Slot", Material.OAK_BUTTON, (short) 0, 1,
                                "§8Available",
                                " ",
                                "§7Use this slot if you want to",
                                "§7start a new SkyBlock adventure.",
                                " ",
                                "§7Each profile has its own:",
                                "§8- §7Personal island",
                                "§8- §7Inventory",
                                "§8- §7Ender Chest",
                                "§8- §7Bank & Purse",
                                "§8- §7Quests",
                                "§8- §7Collections",
                                " ",
                                "§4§lWARNING: §cCreation of profiles",
                                "§cwhich boost other profiles will",
                                "§cbe considered abusive and",
                                "§cpunished.",
                                " ",
                                "§eClick to create solo profile!"
                                );
                    }

                    @Override
                    public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                        new GUIProfileSelectMode().open(player);
                    }
                });
                continue;
            }

            UUID profileId = profileIds.get(profileCount);
            boolean selected = profileId.equals(profiles.getCurrentlySelected());
            DataHandler dataHandler;
            try {
                dataHandler = DataHandler.fromDocument(new ProfilesDatabase(profileId.toString()).getDocument());
            } catch (NullPointerException profileNotYetSaved) {
                dataHandler = DataHandler.initUserWithDefaultData(profileId);
            }

            if (selected) {
                DataHandler finalDataHandler = dataHandler;
                set(new GUIClickableItem() {
                    @Override
                    public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                        player.sendMessage("§aYep! §e" + finalDataHandler.get(DataHandler.Data.PROFILE_NAME, DatapointString.class).getValue() + "§a is the profile you are playing on!");
                        player.sendMessage("§cIf you want to delete this profile, switch to another one first!");
                    }

                    @Override
                    public int getSlot() {
                        return slot;
                    }

                    @Override
                    public ItemStack.Builder getItem(SkyBlockPlayer player) {
                        String age = StringUtility.profileAge(
                                System.currentTimeMillis() - finalDataHandler.get(DataHandler.Data.CREATED, DatapointLong.class).getValue())
                                .replaceFirst("s", " second(s)").replaceFirst("m", " minute(s)")
                                .replaceFirst("h", " hour(s)");

                        if (!age.contains("second"))
                            age = age.replaceFirst("d", " day(s)");

                        List<String> lore = new ArrayList<>(Arrays.asList("§8Selected slot", " "));

                        if (finalDataHandler.get(DataHandler.Data.COINS, DatapointDouble.class).getValue() > 0)
                            lore.add("§7Purse Coins: §6" + finalDataHandler.get(DataHandler.Data.COINS, DatapointDouble.class).getValue());
                        lore.add("§7Age: §9" + age);

                        lore.add(" ");
                        lore.add("§aYou are playing on this profile!");

                        return ItemStackCreator.getStack(
                                "§eProfile: §a" + finalDataHandler.get(DataHandler.Data.PROFILE_NAME, DatapointString.class).getValue(),
                                Material.EMERALD_BLOCK, (short) 0, 1,
                                lore);
                    }
                });
                continue;
            }

            DataHandler finalDataHandler1 = dataHandler;
            set(new GUIClickableItem() {
                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                    new GUIProfileSelect(profileId).open(player);
                }

                @Override
                public int getSlot() {
                    return slot;
                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    String age = StringUtility.profileAge(
                                    System.currentTimeMillis() - finalDataHandler1.get(DataHandler.Data.CREATED, DatapointLong.class).getValue())
                            .replaceFirst("s", " second(s)").replaceFirst("m", " minute(s)")
                            .replaceFirst("h", " hour(s)");

                    if (!age.contains("second"))
                        age = age.replaceFirst("d", " day(s)");

                    List<String> lore = new ArrayList<>(Arrays.asList("§8Slot in use", " "));

                    if (finalDataHandler1.get(DataHandler.Data.COINS, DatapointDouble.class).getValue() > 0)
                        lore.add("§7Purse Coins: §6" + finalDataHandler1.get(DataHandler.Data.COINS, DatapointDouble.class).getValue());
                    lore.add("§7Age: §9" + age);

                    lore.add(" ");
                    lore.add("§eClick to manage!");

                    return ItemStackCreator.getStack(
                            "§eProfile: §a" + finalDataHandler1.get(DataHandler.Data.PROFILE_NAME, DatapointString.class).getValue(),
                            Material.GRASS_BLOCK, (short) 0, 1,
                            lore);
                }
            });
        }
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
