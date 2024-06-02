package net.swofty.types.generic.gui.inventory.inventories.sbmenu.bestiary;

import lombok.Getter;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.skills.GUISkillCategory;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.gui.inventory.item.GUIItem;
import net.swofty.types.generic.skill.SkillCategories;
import net.swofty.types.generic.user.SkyBlockPlayer;


public class GUIBestiary extends SkyBlockInventoryGUI {

    private static final int[] displaySlots = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
                    30, 31, 32

    };

    @Getter
    private enum BestiaryRegions {
        YOUR_ISLAND("§aYour Island", Material.PLAYER_HEAD, "c9c8881e42915a9d29bb61a16fb26d059913204d265df5b439b3d792acd56", "§7View all of the mobs that you've", "§7found and killed on §aYour Island§7."),
        HUB("§aHub", Material.PLAYER_HEAD, "d7cc6687423d0570d556ac53e0676cb563bbdd9717cd8269bdebed6f6d4e7bf8", "§7View all of the mobs that you've", "§7found and killed in the §aHub§7."),
        THE_FARMING_ISLANDS("§aThe Farming Islands", Material.PLAYER_HEAD, "4d3a6bd98ac1833c664c4909ff8d2dc62ce887bdcf3cc5b3848651ae5af6b", "§7View all of the mobs that you've", "§7found and killed in §aThe Farming", "§aIslands§7."),
        GARDEN("§bGarden", Material.PLAYER_HEAD, "f4880d2c1e7b86e87522e20882656f45bafd42f94932b2c5e0d6ecaa490cb4c", "§7View all of the §6Pests §7that you've", "§7killed on the §bGarden§7."),
        SPIDERS_DEN("§cSpider's Den", Material.PLAYER_HEAD, "c754318a3376f470e481dfcd6c83a59aa690ad4b4dd7577fdad1c2ef08d8aee6", "§7View all of the mobs that you've", "§7found and killed in the §cSpider's Den§7."),
        THE_END("§dThe End", Material.PLAYER_HEAD, "7840b87d52271d2a755dedc82877e0ed3df67dcc42ea479ec146176b02779a5", "§7View all of the mobs that you've", "§7found and killed in §dThe End§7."),
        CRIMSON_ISLE("§cCrimson Isle", Material.PLAYER_HEAD, "c3687e25c632bce8aa61e0d64c24e694c3eea629ea944f4cf30dcfb4fbce071", "§7View all of the mobs that you've", "§7found and killed in the §cCrimson Isle§7."),
        DEEP_CAVERNS("§bDeep Caverns", Material.PLAYER_HEAD, "569a1f114151b4521373f34bc14c2963a5011cdc25a6554c48c708cd96ebfc", "§7View all of the mobs that you've", "§7found and killed in the §bDeep Caverns§7."),
        DWARVEN_MINES("§2Dwarven Mines", Material.PLAYER_HEAD, "6b20b23c1aa2be0270f016b4c90d6ee6b8330a17cfef87869d6ad60b2ffbf3b5", "§7View all of the mobs that you've", "§7found and killed in the §2Dwarven Mines§7."),
        CRYSTAL_HALLOWS("§5Crystal Hallows", Material.PLAYER_HEAD, "21dbe30b027acbceb612563bd877cd7ebb719ea6ed1399027dcee58bb9049d4a", "§7View all of the mobs that you've", "§7found and killed in the §5Crystal", "§5Hollows§7."),
        THE_PARK("§3The Park", Material.PLAYER_HEAD, "a221f813dacee0fef8c59f76894dbb26415478d9ddfc44c2e708a6d3b7549b", "§7View all of the mobs that you've", "§7found and killed in §3The Park§7."),
        SPOOKY_FESTIVAL("§6Spooky Festival", Material.JACK_O_LANTERN, "", "§7View all of the mobs that you've", "§7found and killed during the §6Spooky", "§6Festival§7."),
        CATACOMBS("§cCatacombs", Material.PLAYER_HEAD, "964e1c3e315c8d8fffc37985b6681c5bd16a6f97ffd07199e8a05efbef103793", "§7View all of the mobs that you've", "§7found and killed in §cThe Catacombs§7."),
        FISHING("§3Fishing", Material.FISHING_ROD, "", "§7View all of the §3Sea Creatures §7that", "§7you've killed while fishing."),
        MYTHOLOGICAL_CREATURES("§bMythological Creatures", Material.PLAYER_HEAD, "83cc1cf672a4b2540be346ead79ac2d9ed19d95b6075bf95be0b6d0da61377be", "§7View all of the §bMythological", "§bCreatures §7that you've killed."),
        JERRY("§6Jerry", Material.PLAYER_HEAD, "45f729736996a38e186fe9fe7f5a04b387ed03f3871ecc82fa78d8a2bdd31109", "§7View all of the mobs that you've", "§7found and killed while fighting §6Jerry§7."),
        KUUDRA("§cKuudra", Material.PLAYER_HEAD, "5051c83d9ebf69013f1ec8c9efc979ec2d925a921cc877ff64abe09aadd2f6cc", "§7View all of the mobs that you've", "§7found and killed while fighting §cKuudra§7."),
        ;
        private final String regionName;
        private final Material material;
        private final String texture;
        private final String[] lore;

        BestiaryRegions(String regionName, Material material, String texture, String... lore) {
            this.regionName = regionName;
            this.material = material;
            this.texture = texture;
            this.lore = lore;
        }
    }

    public GUIBestiary() {
        super("Bestiary", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(Material.BLACK_STAINED_GLASS_PANE, "");
        set(GUIClickableItem.getCloseItem(49));
        set(GUIClickableItem.getGoBackItem(40, new GUISkillCategory(SkillCategories.COMBAT, 0)));
        set(new GUIItem(4) {
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack("§3Bestiary", Material.WRITTEN_BOOK, 1,
                        "§7The Bestiary is a compendium of",
                        "§7mobs in SkyBlock. It contains detailed",
                        "§7information on loot drops, your mob",
                        "§7stats, and more!",
                        " ",
                        "§7Kill mobs within §aFamilies §7to progress",
                        "§7and earn §arewards§7, including §b✯ Magic",
                        "§bFind §7bonuses towards mobs in the",
                        "§7Family.",
                        " ",
                        "§c§lHERE PROGRESS BAR",
                        " ",
                        "§eClick to view!"
                );
            }
        });
        BestiaryRegions[] allBestiaryRegions = BestiaryRegions.values();
        int index = 0;
        for (int slot : displaySlots) {
            BestiaryRegions bestiaryRegion = allBestiaryRegions[index];
            set(new GUIItem(slot) {
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    if (bestiaryRegion.material == Material.PLAYER_HEAD) {
                        return ItemStackCreator.getStackHead(bestiaryRegion.regionName, bestiaryRegion.texture, 1, bestiaryRegion.lore);
                    } else {
                        return ItemStackCreator.getStack(bestiaryRegion.regionName, bestiaryRegion.material, 1, bestiaryRegion.lore);
                    }
                }
            });
            index++;
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
