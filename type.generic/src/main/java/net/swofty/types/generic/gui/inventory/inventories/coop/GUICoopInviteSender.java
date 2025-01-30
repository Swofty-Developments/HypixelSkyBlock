package net.swofty.types.generic.gui.inventory.inventories.coop;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.types.generic.SkyBlockGenericLoader;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointBoolean;
import net.swofty.types.generic.data.datapoints.DatapointString;
import net.swofty.types.generic.data.datapoints.DatapointUUID;
import net.swofty.types.generic.data.mongodb.CoopDatabase;
import net.swofty.types.generic.data.mongodb.ProfilesDatabase;
import net.swofty.types.generic.data.mongodb.UserDatabase;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.AddStateAction;
import net.swofty.types.generic.gui.inventory.actions.RefreshAction;
import net.swofty.types.generic.gui.inventory.actions.RemoveStateAction;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.user.PlayerProfiles;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.*;

public class GUICoopInviteSender extends SkyBlockAbstractInventory {
    private static final Map<Integer, List<Integer>> SLOTS_MAP = new HashMap<>(
            Map.of(
                    1, List.of(13),
                    2, List.of(12, 14),
                    3, List.of(11, 13, 15),
                    4, List.of(10, 12, 14, 16),
                    5, List.of(11, 12, 13, 14, 15)
            )
    );

    private static final String STATE_MEMBER = "member_";
    private static final String STATE_INVITE = "invite_";

    private CoopDatabase.Coop coop;

    public GUICoopInviteSender(CoopDatabase.Coop coopTemp) {
        super(InventoryType.CHEST_6_ROW);
        this.coop = coopTemp;
        doAction(new SetTitleAction(Component.text("Co-op Invitation")));
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE).build());

        // Close button
        attachItem(GUIItem.builder(49)
                .item(ItemStackCreator.createNamedItemStack(Material.BARRIER, "§cClose").build())
                .onClick((ctx, item) -> {
                    ctx.player().closeInventory();
                    return true;
                })
                .build());

        setupActionButtons();
        refreshDisplay(player);
        startLoop("refresh", 20, () -> refreshDisplay(player));
    }

    private void refreshDisplay(SkyBlockPlayer player) {
        int amountInProfile = coop.memberInvites().size() + coop.members().size();
        int[] slots = SLOTS_MAP.get(amountInProfile).stream().mapToInt(Integer::intValue).toArray();

        // Clear previous states
        for (String state : getStates()) {
            if (state.startsWith(STATE_MEMBER) || state.startsWith(STATE_INVITE)) {
                doAction(new RemoveStateAction(state));
            }
        }

        // Setup originator display
        attachItem(GUIItem.builder(slots[0])
                .item(ItemStackCreator.getStackHead(
                        player.getFullDisplayName(),
                        PlayerSkin.fromUuid(String.valueOf(player.getUuid())), 1,
                        " ",
                        "§7Hey that's you!",
                        "§bCreated the invite!").build())
                .build());

        // Set up member and invite states
        Map<UUID, Boolean> invites = new HashMap<>();
        coop.members().forEach(uuid -> {
            invites.put(uuid, true);
            doAction(new AddStateAction(STATE_MEMBER + uuid));
        });
        coop.memberInvites().forEach(uuid -> {
            invites.put(uuid, false);
            doAction(new AddStateAction(STATE_INVITE + uuid));
        });

        invites.remove(coop.originator());
        setupMemberDisplays(slots, invites);
        doAction(new RefreshAction());
    }

    private void setupMemberDisplays(int[] slots, Map<UUID, Boolean> invites) {
        int i = 0;
        for (Map.Entry<UUID, Boolean> entry : invites.entrySet()) {
            UUID target = entry.getKey();
            boolean accepted = entry.getValue();
            String displayName = SkyBlockPlayer.getDisplayName(target);
            int slot = slots[i + 1];

            attachItem(GUIItem.builder(slot)
                    .item(ItemStackCreator.getStackHead(
                            displayName,
                            PlayerSkin.fromUuid(String.valueOf(target)), 1,
                            " ",
                            "§7Accepted: " + (accepted ? "§aYes" : "§cNot yet")).build())
                    .requireState(accepted ? STATE_MEMBER + target : STATE_INVITE + target)
                    .build());
            i++;
        }
    }

    private void setupActionButtons() {
        // Confirm button
        attachItem(GUIItem.builder(29)
                .item(ItemStackCreator.getStack("§aConfirm co-op", Material.GREEN_TERRACOTTA, 1,
                        "§7Ends the invitation so that you may",
                        "§bplay §7on this co-op profile.",
                        " ",
                        "§eClick to confirm!").build())
                .onClick((ctx, item) -> {
                    handleConfirmCoop(ctx.player());
                    return true;
                })
                .build());

        // Cancel button
        attachItem(GUIItem.builder(33)
                .item(ItemStackCreator.getStack("§cCancel invite", Material.RED_TERRACOTTA, 1,
                        "§7Cancels the invite and removes",
                        "§7the co-op profile.",
                        " ",
                        "§eClick to cancel!").build())
                .onClick((ctx, item) -> {
                    handleCancelInvite(ctx.player());
                    return true;
                })
                .build());
    }

    private void handleConfirmCoop(SkyBlockPlayer player) {
        coop = CoopDatabase.getFromMember(player.getUuid());
        coop.memberInvites().clear();
        coop.members().add(player.getUuid());
        coop.save();

        UUID profileId = UUID.randomUUID();
        DataHandler handler = DataHandler.initUserWithDefaultData(player.getUuid());
        setupCoopProfile(handler, profileId);
        player.kick("§cYou must reconnect to switch profiles");

        MinecraftServer.getSchedulerManager().scheduleTask(() -> {
            PlayerProfiles profiles = player.getProfiles();
            profiles.getProfiles().add(profileId);
            profiles.setCurrentlySelected(profileId);
            new UserDatabase(player.getUuid()).saveProfiles(profiles);
        }, TaskSchedule.tick(5), TaskSchedule.stop());
    }

    private void setupCoopProfile(DataHandler handler, UUID profileId) {
        handler.get(DataHandler.Data.IS_COOP, DatapointBoolean.class).setValue(true);

        if (coop.memberProfiles().isEmpty()) {
            handler.get(DataHandler.Data.ISLAND_UUID, DatapointUUID.class).setValue(UUID.randomUUID());
            handler.get(DataHandler.Data.PROFILE_NAME, DatapointString.class).setValue(PlayerProfiles.getRandomName());
        } else {
            copyExistingProfileData(handler);
        }

        ProfilesDatabase.collection.insertOne(handler.toDocument(profileId));
        coop.memberProfiles().add(profileId);
        coop.save();
    }

    private void copyExistingProfileData(DataHandler handler) {
        UUID otherCoopMember = coop.memberProfiles().get(0);
        ProfilesDatabase islandDatabase = new ProfilesDatabase(otherCoopMember.toString());

        if (islandDatabase.exists()) {
            DataHandler islandHandler = DataHandler.fromDocument(islandDatabase.getDocument());
            handler.get(DataHandler.Data.ISLAND_UUID, DatapointUUID.class).setValue(
                    islandHandler.get(DataHandler.Data.ISLAND_UUID, DatapointUUID.class).getValue());
            handler.get(DataHandler.Data.PROFILE_NAME, DatapointString.class).setValue(
                    islandHandler.get(DataHandler.Data.PROFILE_NAME, DatapointString.class).getValue());
        } else {
            SkyBlockPlayer profileOwner = SkyBlockGenericLoader.getPlayerFromProfileUUID(otherCoopMember);
            handler.get(DataHandler.Data.ISLAND_UUID, DatapointUUID.class).setValue(
                    profileOwner.getDataHandler().get(DataHandler.Data.ISLAND_UUID, DatapointUUID.class).getValue());
            handler.get(DataHandler.Data.PROFILE_NAME, DatapointString.class).setValue(
                    profileOwner.getDataHandler().get(DataHandler.Data.PROFILE_NAME, DatapointString.class).getValue());
        }
    }

    private void handleCancelInvite(SkyBlockPlayer player) {
        coop = CoopDatabase.getFromMember(player.getUuid());
        coop.removeInvite(player.getUuid());
        coop.save();
        player.closeInventory();
        player.sendMessage("§b[Co-op] §aYou have cancelled the co-op invitation!");
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent event, CloseReason reason) {
        // No cleanup needed
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onSuddenQuit(SkyBlockPlayer player) {
        // No cleanup needed
    }
}