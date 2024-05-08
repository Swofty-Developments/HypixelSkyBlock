package net.swofty.type.hub.gui.elizabeth.subguis;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.hub.gui.elizabeth.GUICityProjects;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.gui.inventory.item.GUIItem;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class GUIPreviousCityProjects extends SkyBlockInventoryGUI {

    private final int[] projectSlots = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 15,
            28, 29, 30, 31, 32, 33, 34
    };

    private enum CityProjects {
        FARM_MERCHANTS_DWELLING(ItemStackCreator.getStack("§aCity project: §eFarm Merchant's Dwelling", Material.HAY_BLOCK, 1,
                "§8Released November 2020",
                " ",
                "§7Upgrade the farm merchant's",
                "§7dwelling, adding new tools both to",
                "§7build farms and harvest them quicker!",
                " ",
                "§7Bonuses:",
                "§7▶ §b4§7: 1 day headstart",
                "§7▶ §b8§7: 2 days headstart",
                "§7▶ §b16§7: §65% discount §7at farm merchant",
                "§7▶ §b24§7: §610% discount §7at farm merchant")),
        BARTENDERS_BREWERY(ItemStackCreator.getStackHead("§aCity project: §eBartender's Brewery", "d672c57f4c7b9e962b45b55dd7bd7886880d7eef26db6c2cce03c8ff8c48", 1,
                "§8Released March 2021",
                " ",
                "§7Upgrade the Bartender's brewery,",
                "§7offering new drinks and a new tier",
                "§7of Zombie slayer.",
                " ",
                "§7Bonuses:",
                "§7▶ §b4§7: §65% discount §7at bartender",
                "§7▶ §b8§7: §610% discount §7at bartender",
                "§7▶ §b16§7: §615% discount §7at bartender",
                "§7▶ §b24§7: §65% discount §7at Maddox")),
        BLACKSMITH_WORKSPACE(ItemStackCreator.getStack("§aCity project: §eBlacksmith Workspace", Material.ANVIL, 1,
                "§8Released October 2020",
                " ",
                "§7Add §a3 §7new reforges to the",
                "§7Blacksmith and make his workspace",
                "§7more comfortable.",
                " ",
                "§7Bonuses:",
                "§7▶ §b1§7: 1 day headstart",
                "§7▶ §b2§7: 3 days headstart",
                "§7▶ §b3§7: 5 days headstart",
                "§7▶ §b4§7: 1 week headstart")),
        BUILDERS_HOUSE(ItemStackCreator.getStack("§aCity project: §eBuilder's House", Material.BRICKS, 1,
                "§8Released October 2020",
                " ",
                "§7The Builder shop NPC will move from",
                "§7a stall to its own house, with §etons §7of",
                "§7new blocks for sale without a daily",
                "§7limit.",
                " ",
                "§7Bonuses:",
                "§7▶ §b1§7: §65% discount §7at builder",
                "§7▶ §b2§7: §610% discount §7at builder",
                "§7▶ §b3§7: §615% discount §7at builder",
                "§7▶ §b4§7: §630% discount §7at builder")),
        WEAPONSMITH_WORKSHOP(ItemStackCreator.getStack("§aCity project: §eWeaponsmith Workshop", Material.BOW, 1,
                "§8Released September 2021",
                " ",
                "§7Upgrade the Weaponsmith Workshop,",
                "§7offering brand new arrow items and",
                "§7starter gear for beginner players!",
                " ",
                "§7Bonuses:",
                "§7▶ §b4§7: 1 day headstart",
                "§7▶ §b8§7: 2 days headstart",
                "§7▶ §b16§7: §65% discount §7at Jax",
                "§7▶ §b24§7: §610% discount §7at Jax")),
        REPAIR_WIZARD_PORTAL(ItemStackCreator.getStack("§aCity project: §eRepair Wizard Portal", Material.END_PORTAL_FRAME, 1,
                "§8Released June 2023",
                " ",
                "§7Help out §9Barry §7with the last efforts",
                "§7to open the §dWizard Portal§7.",
                " ",
                "§7Bonuses:")),
        PET_CARE_EXPANSION(ItemStackCreator.getStack("§aCity project: §ePet Care Expansion", Material.EGG, 1,
                "§8Released December 2023",
                " ",
                "§7§7Introduces §dPet Care§7, adding ways to",
                "§7train and level up your §apets§7!",
                " ",
                "§7Bonuses:")),
        ;

        private final ItemStack.Builder item;

        CityProjects(ItemStack.Builder item) {
            this.item = item;
        }
    }

    public GUIPreviousCityProjects() {
        super("Previous City Projects", InventoryType.CHEST_5_ROW);
    }

    public void onOpen(InventoryGUIOpenEvent e) {
        border(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getGoBackItem(40, new GUICityProjects()));

        CityProjects[] cityProjects = CityProjects.values();
        int index = 0;
        for (int slot : projectSlots) {
            if (index < cityProjects.length) {
                CityProjects cityProject = cityProjects[index];
                set(new GUIItem(slot) {
                    @Override
                    public ItemStack.Builder getItem(SkyBlockPlayer player) {
                        return cityProject.item;
                    }
                });
                index++;
            }
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
    }
}
