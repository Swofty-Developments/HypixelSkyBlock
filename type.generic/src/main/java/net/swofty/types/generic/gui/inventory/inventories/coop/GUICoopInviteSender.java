package net.swofty.types.generic.gui.inventory.inventories.coop;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
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
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.RefreshingGUI;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.gui.inventory.item.GUIItem;
import net.swofty.types.generic.user.PlayerProfiles;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GUICoopInviteSender extends SkyBlockInventoryGUI implements RefreshingGUI {
    private static final Map<Integer, List<Integer>> SLOTS_MAP = new HashMap<>(
            Map.of(
                    1, List.of(13),
                    2, List.of(12, 14),
                    3, List.of(11, 13, 15),
                    4, List.of(10, 12, 14, 16),
                    5, List.of(11, 12, 13, 14, 15)
            )
    );

    private CoopDatabase.Coop coop;

    public GUICoopInviteSender(CoopDatabase.Coop coopTemp) {
        super("Co-op Invitation", InventoryType.CHEST_6_ROW);

        this.coop = coopTemp;

        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getCloseItem(49));

        set(new GUIClickableItem(29) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                coop = CoopDatabase.getFromMember(player.getUuid());
                coop.memberInvites().clear();
                coop.members().add(player.getUuid());
                coop.save();

                UUID profileId = UUID.randomUUID();
                DataHandler handler = DataHandler.initUserWithDefaultData(player.getUuid());

                handler.get(DataHandler.Data.IS_COOP, DatapointBoolean.class).setValue(true);

                if (coop.memberProfiles().isEmpty()) {
                    handler.get(DataHandler.Data.ISLAND_UUID, DatapointUUID.class).setValue(UUID.randomUUID());
                    handler.get(DataHandler.Data.PROFILE_NAME, DatapointString.class).setValue(PlayerProfiles.getRandomName());
                } else {
                    UUID otherCoopMember = coop.memberProfiles().get(0);
                    ProfilesDatabase islandDatabase = new ProfilesDatabase(otherCoopMember.toString());
                    if (islandDatabase.exists()) {
                        DataHandler islandHandler = DataHandler.fromDocument(islandDatabase.getDocument());
                        handler.get(DataHandler.Data.ISLAND_UUID, DatapointUUID.class).setValue(islandHandler.get(DataHandler.Data.ISLAND_UUID, DatapointUUID.class).getValue());
                        handler.get(DataHandler.Data.PROFILE_NAME, DatapointString.class).setValue(islandHandler.get(DataHandler.Data.PROFILE_NAME, DatapointString.class).getValue());
                    } else {
                        SkyBlockPlayer profileOwner = SkyBlockGenericLoader.getPlayerFromProfileUUID(otherCoopMember);

                        handler.get(DataHandler.Data.ISLAND_UUID, DatapointUUID.class).setValue(
                                profileOwner.getDataHandler().get(DataHandler.Data.ISLAND_UUID, DatapointUUID.class).getValue());
                        handler.get(DataHandler.Data.PROFILE_NAME, DatapointString.class).setValue(
                                profileOwner.getDataHandler().get(DataHandler.Data.PROFILE_NAME, DatapointString.class).getValue()
                        );
                    }
                }

                player.kick("§cYou must reconnect to switch profiles");

                ProfilesDatabase.collection.insertOne(handler.toDocument(profileId));
                coop.memberProfiles().add(profileId);
                coop.save();

                MinecraftServer.getSchedulerManager().scheduleTask(() -> {
                    PlayerProfiles profiles = player.getProfiles();
                    profiles.getProfiles().add(profileId);
                    profiles.setCurrentlySelected(profileId);
                    new UserDatabase(player.getUuid()).saveProfiles(profiles);
                }, TaskSchedule.tick(5), TaskSchedule.stop());
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack("§aConfirm co-op", Material.GREEN_TERRACOTTA, (short) 0, 1,
                        "§7Ends the invitation so that you may",
                        "§bplay §7on this co-op profile.",
                        " ",
                        "§eClick to confirm!");
            }
        });
        set(new GUIClickableItem(33) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                coop = CoopDatabase.getFromMember(player.getUuid());

                coop.removeInvite(player.getUuid());
                coop.save();
                player.closeInventory();
                player.sendMessage("§b[Co-op] §aYou have cancelled the co-op invitation!");
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack("§cCancel invite", Material.RED_TERRACOTTA, (short) 0, 1,
                        "§7Cancels the invite and removes",
                        "§7the co-op profile.",
                        " ",
                        "§eClick to cancel!");
            }
        });
    }

    @Override
    public void refreshItems(SkyBlockPlayer player) {
        int amountInProfile = coop.memberInvites().size() + coop.members().size();
        int[] slots = SLOTS_MAP.get(amountInProfile).stream().mapToInt(Integer::intValue).toArray();

        set(new GUIItem(slots[0]) {
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStackHead(
                        player.getFullDisplayName(), PlayerSkin.fromUuid(String.valueOf(player.getUuid())), 1,
                        " ",
                        "§7Hey that's you!",
                        "§bCreated the invite!"
                );
            }
        });

        // Put everyone who is a member as TRUE and ones only invited as FALSE
        Map<UUID, Boolean> invites = new HashMap<>();
        coop.members().forEach(uuid -> invites.put(uuid, true));
        coop.memberInvites().forEach(uuid -> invites.put(uuid, false));

        // Remove originator
        invites.remove(coop.originator());

        for (int i = 0; i < invites.size(); i++) {
            UUID target = (UUID) invites.keySet().toArray()[i];
            boolean accepted = invites.get(target);
            String displayName = SkyBlockPlayer.getDisplayName(target);

            int finalI = i;
            set(new GUIItem(slots[finalI + 1]) {
                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    return ItemStackCreator.getStackHead(
                            displayName, PlayerSkin.fromUuid(String.valueOf(target)), 1,
                            " ",
                            "§7Accepted: " + (accepted ? "§aYes" : "§cNot yet")
                    );
                }
            });
        }
    }

    @Override
    public int refreshRate() {
        return 20;
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
