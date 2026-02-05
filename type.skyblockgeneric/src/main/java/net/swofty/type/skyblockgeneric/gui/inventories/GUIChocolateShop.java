package net.swofty.type.skyblockgeneric.gui.inventories;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.skyblockgeneric.chocolatefactory.ChocolateFactoryHelper;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointChocolateFactory;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;

public class GUIChocolateShop implements StatefulView<GUIChocolateShop.State> {
    // Texture IDs (extracted from skull texture URLs)
    private static final String SUPREME_CHOCOLATE_BAR_TEXTURE = "254b7f3f2a6f0d1c2c054678128ec2322619aefff0f450c390d6a41b5950302e";
    private static final String EGGLOCATOR_TEXTURE = "14a7ff9a10fdae14446499ef3bc1df13b7888d6cd2e311ccab51b8352c6093b4";
    private static final String NIBBLE_CHOCOLATE_STICK_TEXTURE = "888188d62908af6e114f73a109e15ac7f1faded39abd6a2054034ec5cc70c727";
    private static final String SMOOTH_CHOCOLATE_BAR_TEXTURE = "a9372efd2ca1a6c6dfc066f1ec83f9456575c3850a0e7d01109c4f1af300ba8";
    private static final String RICH_CHOCOLATE_CHUNK_TEXTURE = "6f942717364c0fecf7ad11bac8cd98dd7ad4dbd72e3d3ce2b57eb48713824ff";
    private static final String GANACHE_CHOCOLATE_SLAB_TEXTURE = "f89512331edfdc27cb7d4e80f3e0db460d05caf66c7c1c42e0e712130a9b690";
    private static final String PRESTIGE_CHOCOLATE_REALM_TEXTURE = "af19ceeabf2ecb020610b8aabc9299264fa670048c010c9699ce687fc9bf351e";
    private static final String DARK_CACAO_TRUFFLE_TEXTURE = "db9db373cadbec1912a9ab386d31ceb3e0cd4d6a64f222426588a3b2eb31ed29";
    private static final String CHOCOLATE_DYE_TEXTURE = "a15e7208539306f65d68df9be6c3124c48027e307739fc8dc35526febd643c21";
    private static final String BARN_SKIN_TEXTURE = "af90da40c557af4ac01d39b6733e204c74ae9fee8c2bc40be1fd4f28f837d52";
    private static final String CHOCOLATE_SYRINGE_TEXTURE = "7dcb67a72c01f3ca75da846f957ffed6417f0c45ad814fb3e340c317cf316718";
    private static final String CHOCO_RABBIT_MINION_TEXTURE = "9a815398e7da89b1bc08f646cafc8e7b813da0be0eec0cce6d3eff5207801026";
    private static final String ZORROS_CAPE_TEXTURE = "81f7226a927558d069a6ae343b4e089fbd60fc6037190097c7713208e988faae";
    private static final String FISH_CHOCOLAT_TEXTURE = "422b0e5faa97ca109cd45f1fba2d84ca2b9b601de50b47f4add2d835aa360f78";
    private static final String HOT_CHOCOLATE_MIXIN_TEXTURE = "4fde9c68bc5a89f01a5e5203eecc5367d494d55a47c81e6b1d689a0c4488b6e";

    public record State() {}

    @Override
    public State initialState() {
        return new State();
    }

    @Override
    public ViewConfiguration<State> configuration() {
        return ViewConfiguration.withString((state, ctx) -> "Chocolate Shop", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<State> layout, State state, ViewContext ctx) {
        Components.fill(layout);

        // Slot 10: Supreme Chocolate Bar
        layout.slot(10, (s, c) -> {
            List<String> lore = new ArrayList<>();
            lore.add("§7Bring §63,000 §7of these to §5Carrolyn §7in");
            lore.add("§5Scarleton §7on the §cCrimson Isle §7to");
            lore.add("§7permanently gain §c+5\u2764 Health §7and");
            lore.add("§6+12\u2618 Cocoa Beans Fortune§7.");
            lore.add("");
            lore.add("§a§lUNCOMMON");
            lore.add("");
            addCostLore(lore, (SkyBlockPlayer) c.player(), 2_000_000L);
            lore.add("");
            lore.add("§7Annual Stock §8Year 471");
            lore.add("§6500 §7remaining");
            lore.add("");
            lore.add("§eClick to trade!");
            lore.add("§eRight-click for more trading options!");
            return ItemStackCreator.getStackHead("§aSupreme Chocolate Bar", SUPREME_CHOCOLATE_BAR_TEXTURE, 1, lore);
        }, (click, c) -> handlePurchase((SkyBlockPlayer) c.player(), 2_000_000L, "§aSupreme Chocolate Bar", 0, c));

        // Slot 11: Egglocator
        layout.slot(11, (s, c) -> {
            List<String> lore = new ArrayList<>();
            lore.add("§7Uses the magic of §aHoppity §7to");
            lore.add("§7uncover hidden §aChocolate Rabbit");
            lore.add("§aEggs§7.");
            lore.add("");
            lore.add("§6Ability: Egglocator  §e§lRIGHT CLICK");
            lore.add("§7Points towards the nearest unclaimed");
            lore.add("§aChocolate Rabbit Egg§7!");
            lore.add("§8Cooldown: §a5s");
            lore.add("");
            lore.add("§7Only works during §dHoppity's Hunt§7.");
            lore.add("");
            lore.add("§f§lCOMMON");
            lore.add("");
            addCostLore(lore, (SkyBlockPlayer) c.player(), 7_500_000L);
            lore.add("");
            lore.add("§7Annual Stock §8Year 471");
            lore.add("§61 §7remaining");
            lore.add("");
            lore.add("§eClick to trade!");
            return ItemStackCreator.getStackHead("§fEgglocator", EGGLOCATOR_TEXTURE, 1, lore);
        }, (click, c) -> handlePurchase((SkyBlockPlayer) c.player(), 7_500_000L, "§fEgglocator", 0, c));

        // Slot 12: Nibble Chocolate Stick
        layout.slot(12, (s, c) -> {
            SkyBlockPlayer p = (SkyBlockPlayer) c.player();
            int prestige = ChocolateFactoryHelper.getData(p).getPrestigeLevel();
            if (prestige < 1) {
                return createLockedItem("§cChocolate Factory II.");
            }
            List<String> lore = new ArrayList<>();
            lore.add("§7Grants §a+2% §7chance to find a");
            lore.add("§aChocolate Rabbit §7that you haven't");
            lore.add("§7found yet and grants §6+10 Chocolate");
            lore.add("§7per second.");
            lore.add("");
            lore.add("§8§oA delightful treat from the Factory.");
            lore.add("§8§oIts crisp taste sparks joy with");
            lore.add("§8§oevery bite.");
            lore.add("");
            lore.add("§f§lCOMMON ACCESSORY");
            lore.add("");
            addCostLore(lore, p, 250_000_000L);
            lore.add("");
            lore.add("§7Annual Stock §8Year 471");
            lore.add("§61 §7remaining");
            lore.add("");
            lore.add("§eClick to trade!");
            return ItemStackCreator.getStackHead("§fNibble Chocolate Stick", NIBBLE_CHOCOLATE_STICK_TEXTURE, 1, lore);
        }, (click, c) -> handlePurchase((SkyBlockPlayer) c.player(), 250_000_000L, "§fNibble Chocolate Stick", 1, c));

        // Slot 13: Smooth Chocolate Bar
        layout.slot(13, (s, c) -> {
            SkyBlockPlayer p = (SkyBlockPlayer) c.player();
            int prestige = ChocolateFactoryHelper.getData(p).getPrestigeLevel();
            if (prestige < 1) {
                return createLockedItem("§cChocolate Factory II.");
            }
            List<String> lore = new ArrayList<>();
            lore.add("§7Grants §a+4% §7chance to find a");
            lore.add("§aChocolate Rabbit §7that you haven't");
            lore.add("§7found yet and grants §6+20 Chocolate");
            lore.add("§7per second.");
            lore.add("");
            lore.add("§8§oCrafted in the Factory, its");
            lore.add("§8§osmoothness melts hearts and tastes");
            lore.add("§8§olike a sweet escape.");
            lore.add("");
            lore.add("§a§lUNCOMMON ACCESSORY");
            lore.add("");
            addCostLore(lore, p, 1_000_000_000L);
            lore.add("§fNibble Chocolate Stick");
            lore.add("");
            lore.add("§7Annual Stock §8Year 471");
            lore.add("§61 §7remaining");
            lore.add("");
            lore.add("§eClick to trade!");
            return ItemStackCreator.getStackHead("§aSmooth Chocolate Bar", SMOOTH_CHOCOLATE_BAR_TEXTURE, 1, lore);
        }, (click, c) -> handlePurchase((SkyBlockPlayer) c.player(), 1_000_000_000L, "§aSmooth Chocolate Bar", 1, c));

        // Slot 14: Rich Chocolate Chunk
        layout.slot(14, (s, c) -> {
            SkyBlockPlayer p = (SkyBlockPlayer) c.player();
            int prestige = ChocolateFactoryHelper.getData(p).getPrestigeLevel();
            if (prestige < 2) {
                return createLockedItem("§cChocolate Factory III.");
            }
            List<String> lore = new ArrayList<>();
            lore.add("§7Grants §a+6% §7chance to find a");
            lore.add("§aChocolate Rabbit §7that you haven't");
            lore.add("§7found yet and grants §6+30 Chocolate");
            lore.add("§7per second.");
            lore.add("");
            lore.add("§8§oFrom the Factory's secret");
            lore.add("§8§oreserves, its rich flavor is a deep");
            lore.add("§8§odive into indulgence.");
            lore.add("");
            lore.add("§9§lRARE ACCESSORY");
            lore.add("");
            addCostLore(lore, p, 2_000_000_000L);
            lore.add("§aSmooth Chocolate Bar");
            lore.add("");
            lore.add("§7Annual Stock §8Year 471");
            lore.add("§61 §7remaining");
            lore.add("");
            lore.add("§eClick to trade!");
            return ItemStackCreator.getStackHead("§9Rich Chocolate Chunk", RICH_CHOCOLATE_CHUNK_TEXTURE, 1, lore);
        }, (click, c) -> handlePurchase((SkyBlockPlayer) c.player(), 2_000_000_000L, "§9Rich Chocolate Chunk", 2, c));

        // Slot 15: Ganache Chocolate Slab
        layout.slot(15, (s, c) -> {
            SkyBlockPlayer p = (SkyBlockPlayer) c.player();
            int prestige = ChocolateFactoryHelper.getData(p).getPrestigeLevel();
            if (prestige < 2) {
                return createLockedItem("§cChocolate Factory III.");
            }
            List<String> lore = new ArrayList<>();
            lore.add("§7Grants §a+8% §7chance to find a");
            lore.add("§aChocolate Rabbit §7that you haven't");
            lore.add("§7found yet and grants §6+40 Chocolate");
            lore.add("§7per second.");
            lore.add("");
            lore.add("§8§oA Factory masterpiece - its divine");
            lore.add("§8§otaste transcends reality, offering a");
            lore.add("§8§oheavenly escape.");
            lore.add("");
            lore.add("§5§lEPIC ACCESSORY");
            lore.add("");
            addCostLore(lore, p, 3_000_000_000L);
            lore.add("§9Rich Chocolate Chunk");
            lore.add("");
            lore.add("§7Annual Stock §8Year 471");
            lore.add("§61 §7remaining");
            lore.add("");
            lore.add("§eClick to trade!");
            return ItemStackCreator.getStackHead("§5Ganache Chocolate Slab", GANACHE_CHOCOLATE_SLAB_TEXTURE, 1, lore);
        }, (click, c) -> handlePurchase((SkyBlockPlayer) c.player(), 3_000_000_000L, "§5Ganache Chocolate Slab", 2, c));

        // Slot 16: Prestige Chocolate Realm
        layout.slot(16, (s, c) -> {
            SkyBlockPlayer p = (SkyBlockPlayer) c.player();
            int prestige = ChocolateFactoryHelper.getData(p).getPrestigeLevel();
            if (prestige < 3) {
                return createLockedItem("§cChocolate Factory IV.");
            }
            List<String> lore = new ArrayList<>();
            lore.add("§7Grants §a+10% §7chance to find a");
            lore.add("§aChocolate Rabbit §7that you haven't");
            lore.add("§7found yet and grants §6+50 Chocolate");
            lore.add("§7per second.");
            lore.add("");
            lore.add("§8§oThe Factory's pinnacle creation - its");
            lore.add("§8§oepic taste shatters expectations,");
            lore.add("§8§ooffering a taste of utopia.");
            lore.add("");
            lore.add("§6§lLEGENDARY ACCESSORY");
            lore.add("");
            addCostLore(lore, p, 4_500_000_000L);
            lore.add("§5Ganache Chocolate Slab");
            lore.add("");
            lore.add("§7Annual Stock §8Year 471");
            lore.add("§61 §7remaining");
            lore.add("");
            lore.add("§eClick to trade!");
            return ItemStackCreator.getStackHead("§6Prestige Chocolate Realm", PRESTIGE_CHOCOLATE_REALM_TEXTURE, 1, lore);
        }, (click, c) -> handlePurchase((SkyBlockPlayer) c.player(), 4_500_000_000L, "§6Prestige Chocolate Realm", 3, c));

        // Slot 19: Dark Cacao Truffle
        layout.slot(19, (s, c) -> {
            SkyBlockPlayer p = (SkyBlockPlayer) c.player();
            int prestige = ChocolateFactoryHelper.getData(p).getPrestigeLevel();
            if (prestige < 3) {
                return createLockedItem("§cChocolate Factory IV.");
            }
            List<String> lore = new ArrayList<>();
            lore.add("§7Consume to boost your §6\u2618 Global");
            lore.add("§6Fortune §7for §a60m§7.");
            lore.add("");
            lore.add("§7Keep this item in your inventory to");
            lore.add("§7increase the bonus up to §6+30§6\u2618");
            lore.add("§6Global Fortune§7, at which point the");
            lore.add("§7item §c§oevolves§7!");
            lore.add("");
            lore.add("§7Current Bonus: §6+0§6\u2618 Global Fortune");
            lore.add("");
            lore.add("§9§lRARE");
            lore.add("");
            addCostLore(lore, p, 2_500_000_000L);
            lore.add("");
            lore.add("§7Annual Stock §8Year 471");
            lore.add("§62 §7remaining");
            lore.add("");
            lore.add("§eClick to trade!");
            return ItemStackCreator.getStackHead("§9Dark Cacao Truffle", DARK_CACAO_TRUFFLE_TEXTURE, 1, lore);
        }, (click, c) -> handlePurchase((SkyBlockPlayer) c.player(), 2_500_000_000L, "§9Dark Cacao Truffle", 3, c));

        // Slot 20: Chocolate Dye
        layout.slot(20, (s, c) -> {
            SkyBlockPlayer p = (SkyBlockPlayer) c.player();
            int prestige = ChocolateFactoryHelper.getData(p).getPrestigeLevel();
            if (prestige < 5) {
                return createLockedItem("§cChocolate Factory VI.");
            }
            List<String> lore = new ArrayList<>();
            lore.add("§8Combinable in Anvil");
            lore.add("");
            lore.add("§7Changes the color of an armor piece");
            lore.add("§7to §6#7B3F00§7!");
            lore.add("");
            lore.add("§5§lEPIC DYE");
            lore.add("");
            addCostLore(lore, p, 40_000_000_000L);
            lore.add("");
            lore.add("§7Annual Stock §8Year 471");
            lore.add("§61 §7remaining");
            lore.add("");
            lore.add("§eClick to trade!");
            return ItemStackCreator.getStackHead("§6Chocolate Dye", CHOCOLATE_DYE_TEXTURE, 1, lore);
        }, (click, c) -> handlePurchase((SkyBlockPlayer) c.player(), 40_000_000_000L, "§6Chocolate Dye", 5, c));

        // Slot 21: Chocolate Factory Barn Skin
        layout.slot(21, (s, c) -> {
            SkyBlockPlayer p = (SkyBlockPlayer) c.player();
            int prestige = ChocolateFactoryHelper.getData(p).getPrestigeLevel();
            if (prestige < 4) {
                return createLockedItem("§cChocolate Factory V.");
            }
            List<String> lore = new ArrayList<>();
            lore.add("§7Consume this item to unlock the");
            lore.add("§6Chocolate Factory Barn Skin §7on §aThe");
            lore.add("§aGarden§7!");
            lore.add("");
            lore.add("§eClick to consume!");
            lore.add("");
            lore.add("§6§lLEGENDARY COSMETIC");
            lore.add("");
            addCostLore(lore, p, 7_000_000_000L);
            lore.add("");
            lore.add("§7Annual Stock §8Year 471");
            lore.add("§61 §7remaining");
            lore.add("");
            lore.add("§eClick to trade!");
            return ItemStackCreator.getStackHead("§6Chocolate Factory Barn Skin", BARN_SKIN_TEXTURE, 1, lore);
        }, (click, c) -> handlePurchase((SkyBlockPlayer) c.player(), 7_000_000_000L, "§6Chocolate Factory Barn Skin", 4, c));

        // Slot 22: Chocolate Syringe
        layout.slot(22, (s, c) -> {
            SkyBlockPlayer p = (SkyBlockPlayer) c.player();
            int prestige = ChocolateFactoryHelper.getData(p).getPrestigeLevel();
            if (prestige < 4) {
                return createLockedItem("§cChocolate Factory V.");
            }
            List<String> lore = new ArrayList<>();
            lore.add("§7Use at §bKat §7to upgrade §eRabbit Pets §7to");
            lore.add("§dMythic §7rarity.");
            lore.add("");
            lore.add("§d§lMYTHIC");
            lore.add("");
            addCostLore(lore, p, 10_000_000_000L);
            lore.add("");
            lore.add("§7Annual Stock §8Year 471");
            lore.add("§61 §7remaining");
            lore.add("");
            lore.add("§eClick to trade!");
            return ItemStackCreator.getStackHead("§dChocolate Syringe", CHOCOLATE_SYRINGE_TEXTURE, 1, lore);
        }, (click, c) -> handlePurchase((SkyBlockPlayer) c.player(), 10_000_000_000L, "§dChocolate Syringe", 4, c));

        // Slot 23: Choco Rabbit Minion Skin
        layout.slot(23, (s, c) -> {
            SkyBlockPlayer p = (SkyBlockPlayer) c.player();
            int prestige = ChocolateFactoryHelper.getData(p).getPrestigeLevel();
            if (prestige < 4) {
                return createLockedItem("§cChocolate Factory V.");
            }
            List<String> lore = new ArrayList<>();
            lore.add("§7This Minion skin changes your");
            lore.add("§7minion's appearance to a §eChoco");
            lore.add("§eRabbit§7.");
            lore.add("");
            lore.add("§7You can place this item in any minion");
            lore.add("§7of your choice!");
            lore.add("");
            lore.add("§5§lEPIC COSMETIC");
            lore.add("");
            addCostLore(lore, p, 2_500_000_000L);
            lore.add("");
            lore.add("§7Annual Stock §8Year 471");
            lore.add("§62 §7remaining");
            lore.add("");
            lore.add("§eClick to trade!");
            return ItemStackCreator.getStackHead("§5Choco Rabbit Minion Skin", CHOCO_RABBIT_MINION_TEXTURE, 1, lore);
        }, (click, c) -> handlePurchase((SkyBlockPlayer) c.player(), 2_500_000_000L, "§5Choco Rabbit Minion Skin", 4, c));

        // Slot 24: Zorro's Cape
        layout.slot(24, (s, c) -> {
            List<String> lore = new ArrayList<>();
            lore.add("§7Strength: §c+10");
            lore.add("§7Ferocity: §c+2");
            lore.add("§7Farming Fortune: §6+10");
            lore.add("§7Farming Wisdom: §3+1");
            lore.add("");
            lore.add("§7The stats of this Cape §adouble ");
            lore.add("§7during §eJacob's Farming Contest§7.");
            lore.add("§7Additionally, you have a §a20% §7chance");
            lore.add("§7to obtain an extra medal from");
            lore.add("§7contests.");
            lore.add("");
            lore.add("§8§oNot all Rabbits wear capes.");
            lore.add("");
            lore.add("§8This item can be reforged!");
            lore.add("§4\u2763 §cRequires §dZorro §cin Hoppity's Collection§c.");
            lore.add("§6§lLEGENDARY CLOAK");
            lore.add("");
            addCostLore(lore, (SkyBlockPlayer) c.player(), 20_000_000_000L);
            lore.add("");
            lore.add("§cNot unlocked!");
            return ItemStackCreator.getStackHead("§6Zorro's Cape", ZORROS_CAPE_TEXTURE, 1, lore);
        });

        // Slot 25: Fish Chocolat à la Vapeur
        layout.slot(25, (s, c) -> {
            SkyBlockPlayer p = (SkyBlockPlayer) c.player();
            int prestige = ChocolateFactoryHelper.getData(p).getPrestigeLevel();
            if (prestige < 5) {
                return createLockedItem("§cChocolate Factory VI.");
            }
            List<String> lore = new ArrayList<>();
            lore.add("§7Give this dish to §aHoppity §7to obtain his");
            lore.add("§aAbiphone Contact§7.");
            lore.add("");
            lore.add("§8§oSavory fish with a chocolate twist.");
            lore.add("§8§oMwah! C'est magnifique, no?");
            lore.add("");
            lore.add("§5§lEPIC");
            lore.add("");
            addCostLore(lore, p, 50_000_000_000L);
            lore.add("§cRabbit the Fish");
            lore.add("");
            lore.add("§7Annual Stock §8Year 471");
            lore.add("§61 §7remaining");
            lore.add("");
            lore.add("§eClick to trade!");
            return ItemStackCreator.getStackHead("§5Fish Chocolat \u00e0 la Vapeur", FISH_CHOCOLAT_TEXTURE, 1, lore);
        }, (click, c) -> handlePurchase((SkyBlockPlayer) c.player(), 50_000_000_000L, "§5Fish Chocolat \u00e0 la Vapeur", 5, c));

        // Slot 28: Hot Chocolate Mixin
        layout.slot(28, (s, c) -> {
            SkyBlockPlayer p = (SkyBlockPlayer) c.player();
            int prestige = ChocolateFactoryHelper.getData(p).getPrestigeLevel();
            if (prestige < 5) {
                List<String> lore = new ArrayList<>();
                lore.add("§8Brewing Ingredient");
                lore.add("");
                lore.add("§7Mixins provide a buff that can be");
                lore.add("§7added to §cGod Potions §7in a brewing");
                lore.add("§7stand and lasts for the full duration.");
                lore.add("");
                lore.add("§7Gain §d+15\u2663 Pet Luck §7and §6+0.05x");
                lore.add("§6Chocolate §7per second.");
                lore.add("");
                lore.add("§7Duration: §a36h 0m");
                lore.add("");
                lore.add("§7The duration of Mixins can be stacked!");
                lore.add("");
                lore.add("§eRight-click to consume!§8");
                lore.add("§8(Requires active Booster Cookie)");
                lore.add("");
                lore.add("§4\u2763 §cRequires §cChocolate Factory VI§c.");
                lore.add("§9§lRARE");
                lore.add("");
                addCostLore(lore, p, 1_500_000_000L);
                lore.add("");
                lore.add("§cNot unlocked!");
                return ItemStackCreator.getStackHead("§9Hot Chocolate Mixin", HOT_CHOCOLATE_MIXIN_TEXTURE, 1, lore);
            }
            List<String> lore = new ArrayList<>();
            lore.add("§8Brewing Ingredient");
            lore.add("");
            lore.add("§7Mixins provide a buff that can be");
            lore.add("§7added to §cGod Potions §7in a brewing");
            lore.add("§7stand and lasts for the full duration.");
            lore.add("");
            lore.add("§7Gain §d+15\u2663 Pet Luck §7and §6+0.05x");
            lore.add("§6Chocolate §7per second.");
            lore.add("");
            lore.add("§7Duration: §a36h 0m");
            lore.add("");
            lore.add("§7The duration of Mixins can be stacked!");
            lore.add("");
            lore.add("§eRight-click to consume!§8");
            lore.add("§8(Requires active Booster Cookie)");
            lore.add("");
            lore.add("§9§lRARE");
            lore.add("");
            addCostLore(lore, p, 1_500_000_000L);
            lore.add("");
            lore.add("§eClick to trade!");
            return ItemStackCreator.getStackHead("§9Hot Chocolate Mixin", HOT_CHOCOLATE_MIXIN_TEXTURE, 1, lore);
        }, (click, c) -> handlePurchase((SkyBlockPlayer) c.player(), 1_500_000_000L, "§9Hot Chocolate Mixin", 5, c));

        // Slot 29: Chocolate Fortune
        layout.slot(29, (s, c) -> {
            SkyBlockPlayer p = (SkyBlockPlayer) c.player();
            int prestige = ChocolateFactoryHelper.getData(p).getPrestigeLevel();
            if (prestige < 5) {
                List<String> lore = new ArrayList<>();
                lore.add("§7Permanently gain §6+1\u2618 Cocoa Beans");
                lore.add("§6Fortune §7per tier.");
                lore.add("");
                addCostLore(lore, p, 2_000_000_000L);
                lore.add("");
                lore.add("§cChocolate Factory VI.");
                return ItemStackCreator.getStack("§eChocolate Fortune", Material.COCOA_BEANS, 1, lore);
            }
            List<String> lore = new ArrayList<>();
            lore.add("§7Permanently gain §6+1\u2618 Cocoa Beans");
            lore.add("§6Fortune §7per tier.");
            lore.add("");
            addCostLore(lore, p, 2_000_000_000L);
            lore.add("");
            lore.add("§eClick to trade!");
            return ItemStackCreator.getStack("§eChocolate Fortune", Material.COCOA_BEANS, 1, lore);
        }, (click, c) -> handlePurchase((SkyBlockPlayer) c.player(), 2_000_000_000L, "§eChocolate Fortune", 5, c));

        // Slot 48: Go Back
        Components.back(layout, 48, ctx);

        // Slot 49: Close
        Components.close(layout, 49);

        // Slot 50: Chocolate Shop Milestones
        layout.slot(50, (s, c) -> {
            SkyBlockPlayer p = (SkyBlockPlayer) c.player();
            DatapointChocolateFactory.ChocolateFactoryData data = ChocolateFactoryHelper.getData(p);

            List<String> lore = new ArrayList<>();
            lore.add("§7Unlock special §aChocolate Rabbits §7by");
            lore.add("§7spending §6Chocolate §7in the §6Chocolate");
            lore.add("§6Shop§7.");
            lore.add("");
            lore.add("§7Chocolate Spent: §6" + ChocolateFactoryHelper.formatChocolate(data.getTotalChocolateSpent()));
            lore.add("");
            lore.add("§eClick to view!");
            return ItemStackCreator.getStack("§6Chocolate Shop Milestones", Material.LADDER, 1, lore);
        }, (click, c) -> ((SkyBlockPlayer) c.player()).openView(new GUIChocolateShopMilestones()));
    }

    private void addCostLore(List<String> lore, SkyBlockPlayer player, long cost) {
        lore.add("§7Cost");
        lore.add("§6" + ChocolateFactoryHelper.formatChocolate(cost) + " Chocolate");
    }

    private ItemStack.Builder createLockedItem(String requirement) {
        List<String> lore = new ArrayList<>();
        lore.add("§7???");
        lore.add("");
        lore.add(requirement);
        return ItemStackCreator.getStack("§c???", Material.GRAY_DYE, 1, lore);
    }

    private void handlePurchase(SkyBlockPlayer player, long cost, String itemName, int requiredPrestige, ViewContext c) {
        DatapointChocolateFactory.ChocolateFactoryData data = ChocolateFactoryHelper.getData(player);

        if (data.getPrestigeLevel() < requiredPrestige) {
            player.sendMessage("§cYou don't meet the requirements for this item!");
            return;
        }

        if (data.getChocolate() >= cost) {
            data.removeChocolate(cost);
            data.addChocolateSpent(cost);
            player.getChocolateFactoryDatapoint().setValue(data);
            player.sendMessage("§aPurchased " + itemName + " §afor §6" + ChocolateFactoryHelper.formatChocolate(cost) + " Chocolate§a!");
            c.session(State.class).refresh();
        } else {
            player.sendMessage("§cYou don't have enough Chocolate!");
        }
    }

    public static void open(SkyBlockPlayer player) {
        player.openView(new GUIChocolateShop());
    }
}
