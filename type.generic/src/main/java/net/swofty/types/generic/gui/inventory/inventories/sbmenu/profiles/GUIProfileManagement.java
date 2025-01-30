package net.swofty.types.generic.gui.inventory.inventories.sbmenu.profiles;

import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointBoolean;
import net.swofty.types.generic.data.datapoints.DatapointDouble;
import net.swofty.types.generic.data.datapoints.DatapointLong;
import net.swofty.types.generic.data.datapoints.DatapointString;
import net.swofty.types.generic.data.mongodb.CoopDatabase;
import net.swofty.types.generic.data.mongodb.ProfilesDatabase;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.GUISkyBlockMenu;
import net.swofty.types.generic.item.crafting.SkyBlockRecipe;
import net.swofty.types.generic.user.PlayerProfiles;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class GUIProfileManagement extends SkyBlockAbstractInventory {
    private static final int[] PROFILE_SLOTS = {
            11, 12, 13, 14, 15
    };

    public GUIProfileManagement() {
        super(InventoryType.CHEST_4_ROW);
        doAction(new SetTitleAction(Component.text("Profile Management")));
    }

    @SneakyThrows
    @Override
    public void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE).build());

        // Navigation buttons
        attachItem(GUIItem.builder(31)
                .item(ItemStackCreator.createNamedItemStack(Material.BARRIER, "§cClose").build())
                .onClick((ctx, item) -> {
                    ctx.player().closeInventory();
                    return true;
                })
                .build());

        attachItem(GUIItem.builder(30)
                .item(ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1,
                        "§7To SkyBlock Menu").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUISkyBlockMenu());
                    return true;
                })
                .build());

        setupProfileSlots(player);
    }

    private void setupProfileSlots(SkyBlockPlayer player) {
        PlayerProfiles profiles = player.getProfiles();
        List<UUID> profileIds = profiles.getProfiles();

        for (int profileCount = 0; profileCount <= 4; profileCount++) {
            int slot = PROFILE_SLOTS[profileCount];

            if (profileIds.size() <= profileCount) {
                attachEmptyProfileSlot(slot);
                continue;
            }

            UUID profileId = profileIds.get(profileCount);
            boolean selected = profileId.equals(profiles.getCurrentlySelected());
            DataHandler dataHandler = getProfileDataHandler(player, profileId, selected);

            if (selected) {
                attachSelectedProfileSlot(slot, dataHandler);
            } else {
                attachExistingProfileSlot(slot, profileId, dataHandler);
            }
        }
    }

    private void attachEmptyProfileSlot(int slot) {
        attachItem(GUIItem.builder(slot)
                .item(ItemStackCreator.getStack("§eEmpty Profile Slot", Material.OAK_BUTTON, 1,
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
                        "§eClick to create solo profile!").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIProfileSelectMode());
                    return true;
                })
                .build());
    }

    private void attachSelectedProfileSlot(int slot, DataHandler dataHandler) {
        attachItem(GUIItem.builder(slot)
                .item(() -> {
                    List<String> lore = new ArrayList<>(Arrays.asList("§8Selected slot", " "));
                    updateLore(owner.getUuid(), dataHandler, lore);
                    lore.add(" ");
                    lore.add("§aYou are playing on this profile!");

                    return ItemStackCreator.getStack(
                            "§eProfile: §a" + dataHandler.get(DataHandler.Data.PROFILE_NAME, DatapointString.class).getValue(),
                            Material.EMERALD_BLOCK, 1,
                            lore).build();
                })
                .onClick((ctx, item) -> {
                    ctx.player().sendMessage("§aYep! §e" + dataHandler.get(DataHandler.Data.PROFILE_NAME, DatapointString.class).getValue() + "§a is the profile you are playing on!");
                    ctx.player().sendMessage("§cIf you want to delete this profile, switch to another one first!");
                    return true;
                })
                .build());
    }

    private void attachExistingProfileSlot(int slot, UUID profileId, DataHandler dataHandler) {
        attachItem(GUIItem.builder(slot)
                .item(() -> {
                    List<String> lore = new ArrayList<>(Arrays.asList("§8Slot in use", " "));
                    updateLore(owner.getUuid(), dataHandler, lore);
                    lore.add(" ");
                    lore.add("§eClick to manage!");

                    return ItemStackCreator.getStack(
                            "§eProfile: §a" + dataHandler.get(DataHandler.Data.PROFILE_NAME, DatapointString.class).getValue(),
                            Material.GRASS_BLOCK, 1,
                            lore).build();
                })
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIProfileSelect(profileId));
                    return true;
                })
                .build());
    }

    private DataHandler getProfileDataHandler(SkyBlockPlayer player, UUID profileId, boolean selected) {
        if (selected) {
            return player.getDataHandler();
        }
        try {
            return DataHandler.fromDocument(new ProfilesDatabase(profileId.toString()).getDocument());
        } catch (NullPointerException profileNotYetSaved) {
            return DataHandler.initUserWithDefaultData(profileId);
        }
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

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent event, CloseReason reason) {}

    @Override
    public void onBottomClick(InventoryPreClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onSuddenQuit(SkyBlockPlayer player) {}
}