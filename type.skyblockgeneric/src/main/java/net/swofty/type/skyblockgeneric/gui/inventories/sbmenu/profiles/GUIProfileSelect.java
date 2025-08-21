package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.profiles;

import com.mongodb.client.model.Filters;
import lombok.SneakyThrows;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.ServerType;
import net.swofty.commons.SkyBlockPlayerProfiles;
import net.swofty.type.generic.data.datapoints.DatapointString;
import net.swofty.type.generic.data.mongodb.ProfilesDatabase;
import net.swofty.type.generic.data.mongodb.UserDatabase;
import net.swofty.type.generic.event.actions.data.ActionPlayerDataSave;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;
import net.swofty.type.skyblockgeneric.data.monogdb.CoopDatabase;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.UUID;

public class GUIProfileSelect extends HypixelInventoryGUI {
    private final UUID profileUuid;

    public GUIProfileSelect(UUID profileUuid) {
        super("Profile Management", InventoryType.CHEST_4_ROW);

        this.profileUuid = profileUuid;
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getGoBackItem(31, new GUIProfileManagement()));

        set(new GUIClickableItem(11) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                SkyBlockPlayerProfiles profiles = player.getProfiles();
                SkyBlockPlayerProfiles toSet = new SkyBlockPlayerProfiles();
                toSet.setProfiles(profiles.getProfiles());

                player.getHookManager().registerHook(ActionPlayerDataSave.class, (nil) -> {
                    UserDatabase database = new UserDatabase(player.getUuid());
                    toSet.setCurrentlySelected(profileUuid);
                    database.saveProfiles(toSet);
                }, false);

                player.sendTo(ServerType.SKYBLOCK_ISLAND, true);
            }

            @SneakyThrows
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                return ItemStackCreator.getStack("§aSwitch to Profile", Material.GRASS_BLOCK, 1,
                        "§7Teleports you to your island on",
                        "§7another profile and loads your",
                        "§7inventory, skills, collections",
                        "§7and more...",
                        "",
                        "§7Current: §e" + player.getSkyblockDataHandler().get(SkyBlockDataHandler.Data.PROFILE_NAME, DatapointString.class).getValue(),
                        "§7Switching to: §a" + SkyBlockDataHandler.createFromProfileOnly(new ProfilesDatabase(profileUuid.toString()).getDocument())
                                .get(SkyBlockDataHandler.Data.PROFILE_NAME, DatapointString.class).getValue(),
                        "", "§eClick to switch");
            }
        });

        set(new GUIClickableItem(15) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                return ItemStackCreator.getStack("§cDelete profile", Material.RED_STAINED_GLASS, 1,
                        "§7Clear this profile slot by",
                        "§7deleting the profile forever.",
                        "",
                        "§cWarning!",
                        "§fYou cannot revert this actions!",
                        "",
                        "§eClick to continue!");
            }

            @SneakyThrows
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                if (CoopDatabase.getFromMemberProfile(profileUuid) != null) {
                    player.sendMessage("§cYou cannot delete a profile that is in a coop!");
                    player.sendMessage("§eInstead run §a/coopleave §eto leave your coop.");
                    return;
                }

                SkyBlockPlayerProfiles profiles = player.getProfiles();
                profiles.removeProfile(profileUuid);

                SkyBlockDataHandler handler = SkyBlockDataHandler.createFromProfileOnly(new ProfilesDatabase(profileUuid.toString()).getDocument());

                player.sendMessage(
                        "§aDone! Your §e"
                                + handler.get(SkyBlockDataHandler.Data.PROFILE_NAME, DatapointString.class).getValue()
                                + " §aprofile was deleted!");

                ProfilesDatabase.collection.deleteOne(Filters.eq("_id", profileUuid.toString()));

                new GUIProfileManagement().open(player);
            }
        });
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

    }
}
