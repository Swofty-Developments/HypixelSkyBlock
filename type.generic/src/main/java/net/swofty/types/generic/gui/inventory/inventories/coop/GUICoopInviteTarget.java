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
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.user.PlayerProfiles;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.*;

public class GUICoopInviteTarget extends SkyBlockAbstractInventory {
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
    private final CoopDatabase.Coop coop;

    public GUICoopInviteTarget(CoopDatabase.Coop coop) {
        super(InventoryType.CHEST_5_ROW);
        this.coop = coop;
        doAction(new SetTitleAction(Component.text("Co-op Invitation")));
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE).build());

        int amountInProfile = coop.memberInvites().size() + coop.members().size();
        int[] slots = SLOTS_MAP.get(amountInProfile).stream().mapToInt(Integer::intValue).toArray();

        // Set up states for members and invites
        Map<UUID, Boolean> invites = new HashMap<>();
        coop.members().forEach(uuid -> {
            invites.put(uuid, true);
            doAction(new AddStateAction(STATE_MEMBER + uuid));
        });
        coop.memberInvites().forEach(uuid -> {
            invites.put(uuid, false);
            doAction(new AddStateAction(STATE_INVITE + uuid));
        });

        // Remove originator from invites map
        invites.remove(coop.originator());

        // Originator display
        attachItem(GUIItem.builder(slots[0])
                .item(ItemStackCreator.getStackHead(
                        SkyBlockPlayer.getDisplayName(coop.originator()),
                        PlayerSkin.fromUuid(String.valueOf(coop.originator())), 1,
                        " ",
                        "§bCreated the invite!").build())
                .build());

        // Setup member/invite displays
        setupMemberDisplays(slots, invites);

        // Setup action buttons
        setupActionButtons();
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
        // Deny button
        attachItem(GUIItem.builder(33)
                .item(ItemStackCreator.getStack("§cDeny Invite", Material.BARRIER, 1).build())
                .onClick((ctx, item) -> {
                    SkyBlockPlayer player = ctx.player();
                    coop.removeInvite(player.getUuid());
                    coop.save();
                    player.sendMessage("§b[Co-op] §eYou have denied the co-op invite!");
                    player.closeInventory();

                    notifyOriginator(player, false);
                    return true;
                })
                .build());

        // Accept button
        attachItem(GUIItem.builder(29)
                .item(ItemStackCreator.getStack("§aAccept Invite", Material.GREEN_TERRACOTTA, 1,
                        "§7Creates a NEW §bco-op §7profile on your",
                        "§7account with the above player.",
                        " ",
                        "§7Shared with co-op:",
                        "§8- §bPrivate Island",
                        "§8- §bCollections progress",
                        "§8- §bBanks & Auctions",
                        " ",
                        "§7Per-player:",
                        "§8- §aInventory",
                        "§8- §aPurse",
                        "§8- §aStorage",
                        "§8- §aQuests and Objectives",
                        "§8- §aPets",
                        "§7 ",
                        "§7Uses a profile slot!",
                        "§7Profiles: §e" + owner.getProfiles().getProfiles().size() + "§6/§e4",
                        " ",
                        "§eClick to accept!",
                        " ",
                        "§4§lWARNING: §cCreation of profiles",
                        "§cwhich boost other profiles will",
                        "§cbe considered abusive and",
                        "§cpunished.").build())
                .onClick((ctx, item) -> {
                    handleAcceptInvite(ctx.player());
                    return true;
                })
                .build());
    }

    private void handleAcceptInvite(SkyBlockPlayer player) {
        coop.memberInvites().remove(player.getUuid());
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

        notifyOriginator(player, true);
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

    private void notifyOriginator(SkyBlockPlayer player, boolean accepted) {
        SkyBlockPlayer target = SkyBlockGenericLoader.getLoadedPlayers().stream()
                .filter(p -> p.getUuid().equals(coop.originator()))
                .findFirst()
                .orElse(null);

        if (target != null && (coop.memberInvites().contains(target.getUuid()) || coop.members().contains(target.getUuid()))) {
            target.sendMessage("§b[Co-op] " + (accepted ?
                    "§a" + player.getUsername() + " §ahas accepted your co-op invite!" :
                    "§e" + player.getUsername() + " §chas denied your co-op invite!"));
        }
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