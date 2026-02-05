package net.swofty.type.skyblockgeneric.gui.inventories;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.chocolatefactory.ChocolateFactoryHelper;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointChocolateFactory;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;

public class GUIChocolateShop extends HypixelInventoryGUI {
    private static final String SHOP_TEXTURE = "9a815398e7da89b1bc08f646cafc8e7b813da0be0eec0cce6d3eff5207801026";

    public GUIChocolateShop() {
        super("Chocolate Shop", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void setItems(InventoryGUIOpenEvent e) {
        // Fill with black glass panes
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, " "));

        SkyBlockPlayer player = (SkyBlockPlayer) e.player();
        DatapointChocolateFactory.ChocolateFactoryData data = ChocolateFactoryHelper.getData(player);

        // Slot 4: Shop Info
        set(new GUIItem(4) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer skyPlayer = (SkyBlockPlayer) p;
                DatapointChocolateFactory.ChocolateFactoryData playerData = ChocolateFactoryHelper.getData(skyPlayer);

                List<String> lore = new ArrayList<>();
                lore.add("§7Spend your §6Chocolate §7on the world's");
                lore.add("§7most delectable treats!");
                lore.add("");
                lore.add("§7Your Chocolate: §6" + ChocolateFactoryHelper.formatChocolate(playerData.getChocolate()));
                lore.add("§7Total Spent: §6" + ChocolateFactoryHelper.formatChocolate(playerData.getTotalChocolateSpent()));

                return ItemStackCreator.getStackHead("§6Chocolate Shop", SHOP_TEXTURE, 1, lore);
            }
        });

        // Row 2: Shop Items
        // Slot 10: Chocolate Rabbit (consumable)
        set(createShopItem(10, "§6Chocolate Rabbit", Material.PLAYER_HEAD,
                "§7A delicious chocolate rabbit that",
                "§7restores §c+50 Health §7when consumed.",
                1000, SHOP_TEXTURE));

        // Slot 11: Chocolate Bar
        set(createShopItem(11, "§6Chocolate Bar", Material.COOKIE,
                "§7A tasty chocolate bar that",
                "§7restores §c+25 Health §7when consumed.",
                250, null));

        // Slot 12: Golden Chocolate
        set(createShopItem(12, "§6Golden Chocolate", Material.GOLD_INGOT,
                "§7Premium chocolate infused with gold.",
                "§7Grants §6+100 Chocolate §7when used.",
                5000, null));

        // Slot 13: Time Tower Charge
        set(createShopItem(13, "§dTime Tower Charge", Material.CLOCK,
                "§7Adds §a+1 charge §7to your",
                "§dTime Tower§7.",
                50000, null));

        // Slot 14: Rabbit Foot
        set(createShopItem(14, "§aLucky Rabbit Foot", Material.RABBIT_FOOT,
                "§7Increases your chance of finding",
                "§7rare rabbits by §a+5% §7for 1 hour.",
                25000, null));

        // Slot 15: Cocoa Beans Pack
        set(createShopItem(15, "§6Cocoa Beans Pack", Material.COCOA_BEANS,
                "§7A pack of premium cocoa beans.",
                "§7Grants §6+500 Chocolate §7instantly.",
                2500, null));

        // Slot 16: Chocolate Fountain
        set(createShopItem(16, "§5Chocolate Fountain", Material.CAULDRON,
                "§7A decorative chocolate fountain",
                "§7for your island.",
                100000, null));

        // Row 3: More Items
        // Slot 19: Rabbit Cage
        set(createShopItem(19, "§aRabbit Cage", Material.IRON_BARS,
                "§7Expands your §aRabbit Barn §7by",
                "§a+5 slots §7permanently.",
                75000, null));

        // Slot 20: Chocolate Recipe
        set(createShopItem(20, "§9Chocolate Recipe", Material.PAPER,
                "§7Learn a new chocolate recipe",
                "§7for your factory.",
                10000, null));

        // Slot 21: Rabbit Whistle
        set(createShopItem(21, "§dRabbit Whistle", Material.GOAT_HORN,
                "§7Summons a random rabbit to",
                "§7help in your factory for 1 hour.",
                30000, null));

        // Slot 22: Chocolate Essence
        set(createShopItem(22, "§5Chocolate Essence", Material.EXPERIENCE_BOTTLE,
                "§7Concentrated chocolate power.",
                "§7Grants §6+1,000 Chocolate §7instantly.",
                5000, null));

        // Slot 23: Factory Blueprint
        set(createShopItem(23, "§6Factory Blueprint", Material.MAP,
                "§7Unlocks additional factory",
                "§7customization options.",
                200000, null));

        // Slot 24: Chocolate Crown
        set(createShopItem(24, "§6§lChocolate Crown", Material.GOLDEN_HELMET,
                "§7The ultimate symbol of chocolate",
                "§7mastery. Purely cosmetic.",
                1000000, null));

        // Slot 25: Mystery Egg
        set(createShopItem(25, "§d§lMystery Egg", Material.DRAGON_EGG,
                "§7Contains a random rabbit!",
                "§7Could be any rarity.",
                500000, null));

        // Slot 31: Shop Milestones
        set(new GUIClickableItem(31) {
            @Override
            public void run(InventoryPreClickEvent event, HypixelPlayer p) {
                new GUIChocolateShopMilestones().open((SkyBlockPlayer) p);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer skyPlayer = (SkyBlockPlayer) p;
                DatapointChocolateFactory.ChocolateFactoryData playerData = ChocolateFactoryHelper.getData(skyPlayer);

                List<String> lore = new ArrayList<>();
                lore.add("§7Spend §6Chocolate §7in the shop to");
                lore.add("§7unlock special §aChocolate Rabbits§7!");
                lore.add("");
                lore.add("§7Total Spent: §6" + ChocolateFactoryHelper.formatChocolate(playerData.getTotalChocolateSpent()));
                lore.add("");
                lore.add("§eClick to view milestones!");

                return ItemStackCreator.getStack("§6Chocolate Shop Milestones", Material.LADDER, 1, lore);
            }
        });

        // Slot 48: Go Back
        set(new GUIClickableItem(48) {
            @Override
            public void run(InventoryPreClickEvent event, HypixelPlayer p) {
                GUIChocolateFactory.open((SkyBlockPlayer) p);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                List<String> lore = new ArrayList<>();
                lore.add("§7To Chocolate Factory");
                return ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1, lore);
            }
        });

        // Slot 49: Close
        set(GUIClickableItem.getCloseItem(49));
    }

    private GUIClickableItem createShopItem(int slot, String name, Material material,
                                             String desc1, String desc2, long cost, String textureId) {
        return new GUIClickableItem(slot) {
            @Override
            public void run(InventoryPreClickEvent event, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                DatapointChocolateFactory.ChocolateFactoryData data = ChocolateFactoryHelper.getData(player);

                if (data.getChocolate() >= cost) {
                    data.removeChocolate(cost);
                    data.addChocolateSpent(cost);
                    ChocolateFactoryHelper.getDatapoint(player).setValue(data);
                    player.sendMessage("§aPurchased " + name + " §afor §6" + ChocolateFactoryHelper.formatChocolate(cost) + " Chocolate§a!");

                    // Refresh the GUI
                    new GUIChocolateShop().open(player);
                } else {
                    player.sendMessage("§cYou don't have enough Chocolate!");
                }
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                DatapointChocolateFactory.ChocolateFactoryData data = ChocolateFactoryHelper.getData(player);
                boolean canAfford = data.getChocolate() >= cost;

                List<String> lore = new ArrayList<>();
                lore.add(desc1);
                lore.add(desc2);
                lore.add("");
                lore.add("§7Cost: " + (canAfford ? "§6" : "§c") + ChocolateFactoryHelper.formatChocolate(cost) + " Chocolate");
                lore.add("");
                lore.add(canAfford ? "§eClick to purchase!" : "§cNot enough Chocolate!");

                if (textureId != null && material == Material.PLAYER_HEAD) {
                    return ItemStackCreator.getStackHead(name, textureId, 1, lore);
                }
                return ItemStackCreator.getStack(name, material, 1, lore);
            }
        };
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
