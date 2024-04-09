package net.swofty.types.generic.gui.inventory.inventories.sbmenu.profiles;

import lombok.SneakyThrows;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointBoolean;
import net.swofty.types.generic.data.datapoints.DatapointDouble;
import net.swofty.types.generic.data.datapoints.DatapointLong;
import net.swofty.types.generic.data.datapoints.DatapointString;
import net.swofty.types.generic.data.mongodb.CoopDatabase;
import net.swofty.types.generic.data.mongodb.ProfilesDatabase;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.GUISkyBlockMenu;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;
import net.swofty.types.generic.user.PlayerProfiles;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.StringUtility;

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
        PlayerProfiles profiles = player.getProfiles();
        List<UUID> profileIds = profiles.getProfiles();

        for (int profileCount = 0; profileCount <= 4; profileCount++) {
            int slot = PROFILE_SLOTS[profileCount];

            if (profileIds.size() <= profileCount) {
                set(new GUIClickableItem(slot) {

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
            if (selected) {
                dataHandler = player.getDataHandler();
            } else {
                try {
                    dataHandler = DataHandler.fromDocument(new ProfilesDatabase(profileId.toString()).getDocument());
                } catch (NullPointerException profileNotYetSaved) {
                    dataHandler = DataHandler.initUserWithDefaultData(profileId);
                }
            }

            if (selected) {
                DataHandler finalDataHandler = dataHandler;
                set(new GUIClickableItem(slot) {
                    @Override
                    public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                        player.sendMessage("§aYep! §e" + finalDataHandler.get(DataHandler.Data.PROFILE_NAME, DatapointString.class).getValue() + "§a is the profile you are playing on!");
                        player.sendMessage("§cIf you want to delete this profile, switch to another one first!");
                    }

                    @Override
                    public ItemStack.Builder getItem(SkyBlockPlayer player) {
                        List<String> lore = new ArrayList<>(Arrays.asList("§8Selected slot", " "));

                        updateLore(player.getUuid(), finalDataHandler, lore);

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
            set(new GUIClickableItem(slot) {
                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                    new GUIProfileSelect(profileId).open(player);
                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    List<String> lore = new ArrayList<>(Arrays.asList("§8Slot in use", " "));

                    updateLore(player.getUuid(), finalDataHandler1, lore);

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

    public static List<String> updateLore(UUID playerUuid, DataHandler handler, List<String> lore) {
        if (handler.get(DataHandler.Data.IS_COOP, DatapointBoolean.class).getValue()) {
            lore.add("§bCo-op:");

            CoopDatabase.Coop coop = CoopDatabase.getFromMember(playerUuid);
            coop.members().forEach(member -> {
                lore.add("§7Member " + SkyBlockPlayer.getDisplayName(member));
            });
            coop.memberInvites().forEach(invite -> {
                lore.add("§7Invited " + SkyBlockPlayer.getDisplayName(invite));
            });

            lore.add(" ");
        }

        SkyBlockRecipe.getMissionDisplay(lore, playerUuid);
        lore.add(" ");

        lore.add("§cNo Skills Yet!");
        lore.add(" ");

        if (handler.get(DataHandler.Data.COINS, DatapointDouble.class).getValue() > 0)
            lore.add("§7Purse Coins: §6" + handler.get(DataHandler.Data.COINS, DatapointDouble.class).getValue());

        String age = StringUtility.profileAge(
                        System.currentTimeMillis() - handler.get(DataHandler.Data.CREATED, DatapointLong.class).getValue());

        lore.add("§7Age: §9" + age);

        return lore;
    }
}
