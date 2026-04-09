package net.swofty.type.skyblockgeneric.gui.inventories.coop;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.skyblock.SkyBlockPlayerProfiles;
import net.swofty.type.generic.data.datapoints.DatapointBoolean;
import net.swofty.type.generic.data.datapoints.DatapointString;
import net.swofty.type.generic.data.mongodb.ProfilesDatabase;
import net.swofty.type.generic.data.mongodb.UserDatabase;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.RefreshingGUI;
import net.swofty.type.generic.gui.inventory.TranslatableItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.SkyBlockGenericLoader;
import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointUUID;
import net.swofty.type.skyblockgeneric.data.monogdb.CoopDatabase;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class GUICoopInviteSender extends HypixelInventoryGUI implements RefreshingGUI {
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
        super(I18n.t("gui_coop.sender.title"), InventoryType.CHEST_6_ROW);

        this.coop = coopTemp;

        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getCloseItem(49));

        set(new GUIClickableItem(29) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                coop = CoopDatabase.getFromMember(player.getUuid());
                coop.memberInvites().clear();
                coop.members().add(player.getUuid());
                coop.save();

                UUID profileId = UUID.randomUUID();
                // Fixed: Pass both player UUID and profile ID
                SkyBlockDataHandler handler = SkyBlockDataHandler.initUserWithDefaultData(player.getUuid(), profileId);

                handler.get(SkyBlockDataHandler.Data.IS_COOP, DatapointBoolean.class).setValue(true);

                if (coop.memberProfiles().isEmpty()) {
                    handler.get(SkyBlockDataHandler.Data.ISLAND_UUID, DatapointUUID.class).setValue(UUID.randomUUID());
                    handler.get(SkyBlockDataHandler.Data.PROFILE_NAME, DatapointString.class).setValue(SkyBlockPlayerProfiles.getRandomName());
                } else {
                    UUID otherCoopMember = coop.memberProfiles().getFirst();
                    ProfilesDatabase islandDatabase = new ProfilesDatabase(otherCoopMember.toString());
                    if (islandDatabase.exists()) {
                        SkyBlockDataHandler islandHandler = SkyBlockDataHandler.createFromProfileOnly(islandDatabase.getDocument());
                        handler.get(SkyBlockDataHandler.Data.ISLAND_UUID, DatapointUUID.class).setValue(islandHandler.get(SkyBlockDataHandler.Data.ISLAND_UUID, DatapointUUID.class).getValue());
                        handler.get(SkyBlockDataHandler.Data.PROFILE_NAME, DatapointString.class).setValue(islandHandler.get(SkyBlockDataHandler.Data.PROFILE_NAME, DatapointString.class).getValue());
                    } else {
                        SkyBlockPlayer profileOwner = SkyBlockGenericLoader.getPlayerFromProfileUUID(otherCoopMember);

                        // Fixed: Access SkyBlock data handler through separate cache
                        SkyBlockDataHandler profileOwnerHandler = SkyBlockDataHandler.getUser(profileOwner.getUuid());
                        handler.get(SkyBlockDataHandler.Data.ISLAND_UUID, DatapointUUID.class).setValue(
                                profileOwnerHandler.get(SkyBlockDataHandler.Data.ISLAND_UUID, DatapointUUID.class).getValue());
                        handler.get(SkyBlockDataHandler.Data.PROFILE_NAME, DatapointString.class).setValue(
                                profileOwnerHandler.get(SkyBlockDataHandler.Data.PROFILE_NAME, DatapointString.class).getValue()
                        );
                    }
                }

                player.kick(I18n.string("gui_coop.sender.reconnect_kick", player.getLocale()));

                // Fixed: Use the updated method signature
                ProfilesDatabase.collection.insertOne(handler.toProfileDocument());
                coop.memberProfiles().add(profileId);
                coop.save();

                MinecraftServer.getSchedulerManager().scheduleTask(() -> {
                    SkyBlockPlayerProfiles profiles = player.getProfiles();
                    profiles.getProfiles().add(profileId);
                    profiles.setCurrentlySelected(profileId);
                    new UserDatabase(player.getUuid()).saveProfiles(profiles);
                }, TaskSchedule.tick(5), TaskSchedule.stop());
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                return TranslatableItemStackCreator.getStack("gui_coop.sender.confirm_button", Material.GREEN_TERRACOTTA, 1,
                        "gui_coop.sender.confirm_button.lore");
            }
        });
        set(new GUIClickableItem(33) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                Locale l = p.getLocale();
                coop = CoopDatabase.getFromMember(player.getUuid());

                coop.removeInvite(player.getUuid());
                coop.save();
                player.closeInventory();
                player.sendMessage(I18n.t("gui_coop.sender.cancelled_message"));
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                return TranslatableItemStackCreator.getStack("gui_coop.sender.cancel_button", Material.RED_TERRACOTTA, 1,
                        "gui_coop.sender.cancel_button.lore");
            }
        });
    }

    @Override
    public void refreshItems(HypixelPlayer player) {
        int amountInProfile = coop.memberInvites().size() + coop.members().size();
        int[] slots = SLOTS_MAP.get(amountInProfile).stream().mapToInt(Integer::intValue).toArray();

        set(new GUIItem(slots[0]) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                Locale l = p.getLocale();
                return ItemStackCreator.getStackHead(
                        player.getFullDisplayName(), PlayerSkin.fromUuid(String.valueOf(player.getUuid())), 1,
                        I18n.lore("gui_coop.sender.player_head_self.lore", l));
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

            set(new GUIItem(slots[i + 1]) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    Locale l = p.getLocale();
                    String status = accepted ? I18n.string("gui_coop.sender.accepted_yes", l) : I18n.string("gui_coop.sender.accepted_no", l);
                    return ItemStackCreator.getStackHead(
                            displayName, PlayerSkin.fromUuid(String.valueOf(target)), 1,
                            List.of(" ", I18n.string("gui_coop.sender.player_accepted", l, Map.of("status", status))));
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
    public void suddenlyQuit(Inventory inventory, HypixelPlayer player) {

    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}
