package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.profiles;

import lombok.SneakyThrows;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.skyblock.SkyBlockPlayerProfiles;
import net.swofty.commons.StringUtility;
import net.swofty.type.generic.data.datapoints.DatapointBoolean;
import net.swofty.type.generic.data.datapoints.DatapointDouble;
import net.swofty.type.generic.data.datapoints.DatapointString;
import net.swofty.type.generic.data.mongodb.ProfilesDatabase;
import net.swofty.type.generic.data.datapoints.DatapointLong;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;
import net.swofty.type.skyblockgeneric.data.monogdb.CoopDatabase;
import net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.GUISkyBlockMenu;
import net.swofty.type.skyblockgeneric.item.crafting.SkyBlockRecipe;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class GUIProfileManagement extends HypixelInventoryGUI {
    private static final int[] PROFILE_SLOTS = {
            11, 12, 13, 14, 15
    };

    public GUIProfileManagement() {
        super("Profile Management", InventoryType.CHEST_4_ROW);

        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getCloseItem(31));
        set(new GUIClickableItem(30) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                player.openView(new GUISkyBlockMenu());
            }
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1, "§7To SkyBlock Menu");
            }
        });
    }

    @SneakyThrows
    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        SkyBlockPlayer player = (SkyBlockPlayer) e.player();
        SkyBlockPlayerProfiles profiles = player.getProfiles();
        List<UUID> profileIds = profiles.getProfiles();

        for (int profileCount = 0; profileCount <= 4; profileCount++) {
            int slot = PROFILE_SLOTS[profileCount];

            if (profileIds.size() <= profileCount) {
                set(new GUIClickableItem(slot) {

                    @Override
                    public ItemStack.Builder getItem(HypixelPlayer p) {
                        SkyBlockPlayer player = (SkyBlockPlayer) p;
                        return ItemStackCreator.getStack("§eEmpty Profile Slot", Material.OAK_BUTTON, 1,
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
                    public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                        SkyBlockPlayer player = (SkyBlockPlayer) p;
                        new GUIProfileSelectMode().open(player);
                    }
                });
                continue;
            }

            UUID profileId = profileIds.get(profileCount);
            boolean selected = profileId.equals(profiles.getCurrentlySelected());
            SkyBlockDataHandler dataHandler;

            if (selected) {
                // Use the cached handler for the current profile
                dataHandler = SkyBlockDataHandler.getUser(player.getUuid());
            } else {
                // Load the other profile's data temporarily (not cached)
                try {
                    ProfilesDatabase profileDb = new ProfilesDatabase(profileId.toString());
                    dataHandler = SkyBlockDataHandler.createFromProfileOnly(profileDb.getDocument());
                } catch (NullPointerException profileNotYetSaved) {
                    dataHandler = SkyBlockDataHandler.initUserWithDefaultData(player.getUuid(), profileId);
                }
            }

            if (selected) {
                SkyBlockDataHandler finalDataHandler = dataHandler;
                set(new GUIClickableItem(slot) {
                    @Override
                    public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                        SkyBlockPlayer player = (SkyBlockPlayer) p;
                        String profileName = finalDataHandler.get(SkyBlockDataHandler.Data.PROFILE_NAME, DatapointString.class).getValue();
                        player.sendMessage("§aYep! §e" + profileName + "§a is the profile you are playing on!");
                        player.sendMessage("§cIf you want to delete this profile, switch to another one first!");
                    }

                    @Override
                    public ItemStack.Builder getItem(HypixelPlayer p) {
                        SkyBlockPlayer player = (SkyBlockPlayer) p;
                        List<String> lore = new ArrayList<>(Arrays.asList("§8Selected slot", " "));

                        updateLore(player.getUuid(), finalDataHandler, lore);

                        lore.add(" ");
                        lore.add("§aYou are playing on this profile!");

                        String profileName = finalDataHandler.get(SkyBlockDataHandler.Data.PROFILE_NAME, DatapointString.class).getValue();
                        return ItemStackCreator.getStack(
                                "§eProfile: §a" + profileName,
                                Material.EMERALD_BLOCK, 1,
                                lore);
                    }
                });
                continue;
            }

            SkyBlockDataHandler finalDataHandler1 = dataHandler;
            set(new GUIClickableItem(slot) {
                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    new GUIProfileSelect(profileId).open(player);
                }

                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    List<String> lore = new ArrayList<>(Arrays.asList("§8Slot in use", " "));

                    updateLore(player.getUuid(), finalDataHandler1, lore);

                    lore.add(" ");
                    lore.add("§eClick to manage!");

                    String profileName = finalDataHandler1.get(SkyBlockDataHandler.Data.PROFILE_NAME, DatapointString.class).getValue();
                    return ItemStackCreator.getStack(
                            "§eProfile: §a" + profileName,
                            Material.GRASS_BLOCK, 1,
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
    public void suddenlyQuit(Inventory inventory, HypixelPlayer player) {

    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }

    public static List<String> updateLore(UUID playerUuid, SkyBlockDataHandler handler, List<String> lore) {
        if (handler.get(SkyBlockDataHandler.Data.IS_COOP, DatapointBoolean.class).getValue()) {
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

        Double coins = handler.get(SkyBlockDataHandler.Data.COINS, DatapointDouble.class).getValue();
        if (coins > 0) {
            lore.add("§7Purse Coins: §6" + coins);
        }

        Long createdTime = handler.get(SkyBlockDataHandler.Data.CREATED, DatapointLong.class).getValue();
        String age = StringUtility.profileAge(System.currentTimeMillis() - createdTime);
        lore.add("§7Age: §9" + age);

        return lore;
    }
}