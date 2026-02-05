package net.swofty.type.skyblockgeneric.gui.inventories;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.RefreshingGUI;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.chocolatefactory.ChocolateFactoryHelper;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointChocolateFactory;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;

public class GUIChocolateFactory extends HypixelInventoryGUI implements RefreshingGUI {
    // Texture IDs (the hash part of the minecraft texture URL)
    private static final String CHOCOLATE_TEXTURE = "9a815398e7da89b1bc08f646cafc8e7b813da0be0eec0cce6d3eff5207801026";
    private static final String HOPPITY_TEXTURE = "b79e7f3341b672d9de6564cbaca052a6a723ea466a2e66af35ba1ba855f0d692";
    private static final String COACH_JACKRABBIT_TEXTURE = "bc0cc67e79c228e541e68aeb1d81ed7af51166622ad4db9417d7a29d1b89af95";

    private static final Sound CLICK_SOUND = Sound.sound(Key.key("block.note_block.bit"), Sound.Source.PLAYER, 1.0f, 1.21f);
    private static final Sound NOT_ENOUGH_CHOCOLATE_SOUND = Sound.sound(Key.key("entity.enderman.teleport"), Sound.Source.PLAYER, 8.0f, 0.0f);

    // Employee slots (slots 28-34)
    private static final int[] EMPLOYEE_SLOTS = {28, 29, 30, 31, 32, 33, 34};

    // Employee names in order matching the slots
    private static final String[] EMPLOYEE_NAMES = {
            "Rabbit Bro", "Rabbit Cousin", "Rabbit Sis", "Rabbit Daddy",
            "Rabbit Granny", "Rabbit Uncle", "Rabbit Dog"
    };

    // Employee texture IDs (the hash part of the minecraft texture URL)
    private static final String[] EMPLOYEE_TEXTURES = {
            "287934bdd9df2705b251bb997e029b18c1e94df12992b8107e74497b205ca7e8", // Rabbit Bro
            "a982825c01b658f348a099b4579029a180d2e415183951b2e6e5e27257df4254", // Rabbit Cousin
            "fd076e0e3d4072d0fffee0a87a5d726fc34b2bcec38c264fb9b67871a8ead633", // Rabbit Sis
            "57cab0c34d7ddcf72db56ff36f2883f554cff76eb5d3b3e0562338036c976043", // Rabbit Daddy
            "d6eb2d85ee8e3af1c2ec934beb70a39c5e766b23bdab63210bd2aacd73cbbfc8", // Rabbit Granny
            "a865176723a0b9ee2916180a55a04cccb7704ad1f31fdf3e9d89c798f6802e6b", // Rabbit Uncle
            "35ca98bede3865dd1205e4d091036cd9dc36791b83ea4e0ff4a99ad61b71e898"  // Rabbit Dog
    };

    // Employee subtitles from readm.md
    private static final String[] EMPLOYEE_SUBTITLES = {
            "Ambition on two feet!",
            "Laid-back legend!",
            "Rebel with a cause!",
            "CFO with nerves of steel!",
            "Storyteller supreme!",
            "Stuck in a highlight reel!",
            "Making chocolate, not eating it!"
    };

    public GUIChocolateFactory() {
        super("Chocolate Factory", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void setItems(InventoryGUIOpenEvent e) {
        // Fill with black glass panes
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, " "));

        // Slot 13: Chocolate cookie (clickable)
        set(new GUIClickableItem(13) {
            @Override
            public void run(InventoryPreClickEvent event, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                ChocolateFactoryHelper.handleClick(player);
                player.playSound(CLICK_SOUND);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                DatapointChocolateFactory.ChocolateFactoryData data = ChocolateFactoryHelper.getData(player);

                List<String> lore = new ArrayList<>();
                lore.add("§7§6Chocolate§7, of course, is not a valid");
                lore.add("§7source of §anutrition§7. This, however,");
                lore.add("§7does not stop it from being §dawesome§7.");
                lore.add("");
                lore.add("§7Chocolate Production");
                lore.add("§6" + String.format("%.2f", data.getChocolatePerSecond()) + " §8per second");
                lore.add("");
                lore.add("§7All-time Chocolate: §6" + ChocolateFactoryHelper.formatChocolate(data.getChocolateAllTime()));
                lore.add("");
                lore.add("§eClick to uncover the meaning of life!");

                return ItemStackCreator.getStackHead(
                        "§e" + ChocolateFactoryHelper.formatChocolate(data.getChocolate()) + " §6Chocolate",
                        CHOCOLATE_TEXTURE, 1, lore);
            }
        });

        // Slot 27: Prestige/Chocolate Factory level (dropper)
        set(new GUIItem(27) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                DatapointChocolateFactory.ChocolateFactoryData data = ChocolateFactoryHelper.getData(player);

                int prestigeLevel = data.getPrestigeLevel();
                String prestigeColor = ChocolateFactoryHelper.getPrestigeRankColor(prestigeLevel);
                String prestigeName = ChocolateFactoryHelper.getPrestigeRankName(prestigeLevel);

                List<String> lore = new ArrayList<>();
                lore.add("§7Chocolate Production Multiplier: §6" + String.format("%.1fx", data.getShrineMultiplier() * data.getCoachMultiplier()));
                lore.add("§7Max Rabbit Rarity: §b§lDIVINE");
                lore.add("§7Max Chocolate: §660B");
                lore.add("§7Max Employee: §7[220§7] §bBoard Member");
                lore.add("§7Max §cRabbit Hitman §7Slots: §628");
                lore.add("");
                lore.add("§7Chocolate this Prestige: §6" + ChocolateFactoryHelper.formatChocolate(data.getChocolateAllTime()));
                lore.add("");
                if (prestigeLevel >= 6) {
                    lore.add("§aYou have reached max prestige!");
                } else {
                    lore.add("§eClick to prestige!");
                }

                return ItemStackCreator.getStack(prestigeColor + "Chocolate Factory " + toRoman(prestigeLevel + 1), Material.DROPPER, 1, lore);
            }
        });

        // Slots 28-34: Employee rabbits
        setupEmployeeSlots();

        // Slot 35: Rabbit Barn (oak fence)
        set(new GUIClickableItem(35) {
            @Override
            public void run(InventoryPreClickEvent event, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                if (ChocolateFactoryHelper.purchaseUpgrade(player, ChocolateFactoryHelper.UpgradeType.RABBIT_BARN)) {
                    DatapointChocolateFactory.ChocolateFactoryData data = ChocolateFactoryHelper.getData(player);
                    player.sendMessage("§7Your §aRabbit Barn §7capacity has been increased to §a" + (data.getMaxRabbitSlots() + 2) + " Rabbits§7!");
                    player.playSound(Sound.sound(Key.key("entity.player.levelup"), Sound.Source.PLAYER, 1.0f, 1.0f));
                } else {
                    player.sendMessage("§cYou don't have enough Chocolate!");
                    player.playSound(NOT_ENOUGH_CHOCOLATE_SOUND);
                }
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                DatapointChocolateFactory.ChocolateFactoryData data = ChocolateFactoryHelper.getData(player);
                int level = data.getRabbitBarnLevel();
                long cost = ChocolateFactoryHelper.getRabbitBarnCost(level);
                boolean canAfford = data.getChocolate() >= cost;

                List<String> lore = new ArrayList<>();
                lore.add("§7Your §aRabbit Barn §7can only hold so");
                lore.add("§7many §aChocolate Rabbits§7.");
                lore.add("");
                lore.add("§7If you try collecting more unique");
                lore.add("§7rabbits than your barn can hold,");
                lore.add("§7they will be §ccrushed§7.");
                lore.add("");
                lore.add("§7Your Barn: §a" + data.getEmployeeCount() + "§7/§a" + (data.getMaxRabbitSlots() + 2) + " Rabbits");
                lore.add("§8§m-----------------");
                lore.add("§a§lUPGRADE §8➜ §aRabbit Barn " + toRoman(level + 2));
                lore.add("  §a+2 Capacity");
                lore.add("");
                lore.add("§7Cost");
                lore.add("§6" + ChocolateFactoryHelper.formatChocolate(cost) + " Chocolate");
                lore.add("");
                lore.add(canAfford ? "§eClick to upgrade!" : "§cNot enough chocolate!");

                return ItemStackCreator.getStack("§aRabbit Barn " + toRoman(level + 1), Material.OAK_FENCE, 1, lore);
            }
        });

        // Slot 38: Hand-Baked Chocolate (cookie)
        set(new GUIClickableItem(38) {
            @Override
            public void run(InventoryPreClickEvent event, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                if (ChocolateFactoryHelper.purchaseUpgrade(player, ChocolateFactoryHelper.UpgradeType.HAND_BAKED_CHOCOLATE)) {
                    DatapointChocolateFactory.ChocolateFactoryData data = ChocolateFactoryHelper.getData(player);
                    player.sendMessage("§7You will now produce §6+" + data.getClickPower() + " Chocolate §7per click!");
                    player.playSound(Sound.sound(Key.key("block.note_block.pling"), Sound.Source.PLAYER, 8.0f, 4.05f));
                } else {
                    player.sendMessage("§cYou don't have enough Chocolate!");
                    player.playSound(NOT_ENOUGH_CHOCOLATE_SOUND);
                }
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                DatapointChocolateFactory.ChocolateFactoryData data = ChocolateFactoryHelper.getData(player);
                int level = data.getHandBakedChocolateLevel();
                long cost = ChocolateFactoryHelper.getHandBakedChocolateCost(level);
                boolean canAfford = data.getChocolate() >= cost;

                List<String> lore = new ArrayList<>();
                lore.add("§7A good boss can get down in the");
                lore.add("§7trenches and help out their");
                lore.add("§7workforce. In exchange for some");
                lore.add("§7§6Chocolate§7, you can increase the");
                lore.add("§7amount of §6Chocolate §7that you");
                lore.add("§7produce each time you click!");
                lore.add("");
                lore.add("§7Chocolate Per Click: §6+" + data.getClickPower() + " Chocolate");
                lore.add("");
                if (level >= 9) {
                    lore.add("§aYou have reached the maximum");
                    lore.add("§aamount of upgrades!");
                } else {
                    lore.add("§8§m-----------------");
                    lore.add("§a§lUPGRADE §8➜ §dHand-Baked Chocolate " + toRoman(level + 2));
                    lore.add("  §6+1 Chocolate Per Click");
                    lore.add("");
                    lore.add("§7Cost");
                    lore.add("§6" + ChocolateFactoryHelper.formatChocolate(cost) + " Chocolate");
                    lore.add("");
                    lore.add(canAfford ? "§eClick to upgrade!" : "§cNot enough chocolate!");
                }

                return ItemStackCreator.getStack("§dHand-Baked Chocolate " + toRoman(level + 1), Material.COOKIE, 1, lore);
            }
        });

        // Slot 39: Time Tower (clock)
        set(new GUIClickableItem(39) {
            @Override
            public void run(InventoryPreClickEvent event, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                DatapointChocolateFactory.ChocolateFactoryData data = ChocolateFactoryHelper.getData(player);

                // Right-click to activate
                if (event.getClick() instanceof Click.Right && data.getTimeTowerLevel() > 0 && data.getTimeTowerCharges() > 0 && !data.isTimeTowerActive()) {
                    if (ChocolateFactoryHelper.activateTimeTower(player)) {
                        player.sendMessage("§aTime Tower activated for 1 hour!");
                        player.playSound(Sound.sound(Key.key("block.beacon.activate"), Sound.Source.PLAYER, 1.0f, 1.0f));
                        return;
                    }
                }

                // Left-click to upgrade
                if (ChocolateFactoryHelper.purchaseUpgrade(player, ChocolateFactoryHelper.UpgradeType.TIME_TOWER)) {
                    player.sendMessage("§7You upgraded to §dTime Tower " + toRoman(data.getTimeTowerLevel() + 1) + "§7!");
                    player.playSound(Sound.sound(Key.key("entity.player.levelup"), Sound.Source.PLAYER, 1.0f, 1.0f));
                } else if (data.getTimeTowerLevel() >= 15) {
                    player.sendMessage("§cThe Time Tower is already at its maximum level!");
                } else {
                    player.sendMessage("§cYou don't have enough Chocolate!");
                    player.playSound(NOT_ENOUGH_CHOCOLATE_SOUND);
                }
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                DatapointChocolateFactory.ChocolateFactoryData data = ChocolateFactoryHelper.getData(player);
                int level = data.getTimeTowerLevel();
                long cost = ChocolateFactoryHelper.getTimeTowerCost(level);
                boolean canAfford = data.getChocolate() >= cost;
                boolean isActive = data.isTimeTowerActive();

                List<String> lore = new ArrayList<>();
                lore.add("§7When active, this ancient building");
                lore.add("§7increases the production of your");
                lore.add("§7§6Chocolate Factory §7by §6+" + String.format("%.1fx", data.getTimeTowerMultiplier() - 1.0 + (level > 0 ? 0.1 * level : 0.1)) + " §7for §a1h§7.");
                lore.add("");
                if (isActive) {
                    long remaining = data.getTimeTowerActiveUntil() - System.currentTimeMillis();
                    long minutes = remaining / 60000;
                    long seconds = (remaining % 60000) / 1000;
                    lore.add("§7Status: §a§lACTIVE");
                    lore.add("§7Time Remaining: §a" + minutes + "m " + seconds + "s");
                } else {
                    lore.add("§7Status: §c§lINACTIVE");
                }
                lore.add("");
                lore.add("§7Charges: §a" + data.getTimeTowerCharges() + "§7/§a3");
                lore.add("");
                if (level >= 15) {
                    lore.add("§aThe Time Tower is maxed out!");
                } else {
                    lore.add("§8§m-----------------");
                    lore.add("§a§lUPGRADE §8➜ §dTime Tower " + toRoman(level + 2));
                    lore.add("  §6+0.1x Production Multiplier");
                    lore.add("");
                    lore.add("§7Cost");
                    lore.add("§6" + ChocolateFactoryHelper.formatChocolate(cost) + " Chocolate");
                    lore.add("");
                    lore.add(canAfford ? "§eClick to upgrade!" : "§cNot enough chocolate!");
                }
                if (data.getTimeTowerCharges() > 0 && !isActive) {
                    lore.add("§dRight-click to activate!");
                }

                return ItemStackCreator.getStack("§dTime Tower " + toRoman(level + 1), Material.CLOCK, 1, lore);
            }
        });

        // Slot 41: Rabbit Shrine (rabbit foot)
        set(new GUIClickableItem(41) {
            @Override
            public void run(InventoryPreClickEvent event, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                if (ChocolateFactoryHelper.purchaseUpgrade(player, ChocolateFactoryHelper.UpgradeType.RABBIT_SHRINE)) {
                    player.sendMessage("§aUpgraded Rabbit Shrine!");
                    player.playSound(Sound.sound(Key.key("entity.player.levelup"), Sound.Source.PLAYER, 1.0f, 1.0f));
                } else {
                    player.sendMessage("§cYou don't have enough Chocolate!");
                    player.playSound(NOT_ENOUGH_CHOCOLATE_SOUND);
                }
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                DatapointChocolateFactory.ChocolateFactoryData data = ChocolateFactoryHelper.getData(player);
                int level = data.getRabbitShrineLevel();
                long cost = ChocolateFactoryHelper.getRabbitShrineCost(level);
                boolean canAfford = data.getChocolate() >= cost;
                int oddsBonus = level * 2; // 2% per level

                List<String> lore = new ArrayList<>();
                lore.add("§7The §dRabbit Shrine §7increases the");
                lore.add("§7§dodds §7of finding §aChocolate Rabbits §7of");
                lore.add("§7higher rarity during §dHoppity's Hunt");
                lore.add("§d§7by §a" + oddsBonus + "%§7.");
                lore.add("");
                if (level >= 20) {
                    lore.add("§aYour Rabbit Shrine is at its maximum");
                    lore.add("§alevel!");
                } else {
                    lore.add("§8§m-----------------");
                    lore.add("§a§lUPGRADE §8➜ §dRabbit Shrine " + toRoman(level + 2));
                    lore.add("  §a+2% Rare Rabbit Odds");
                    lore.add("");
                    lore.add("§7Cost");
                    lore.add("§6" + ChocolateFactoryHelper.formatChocolate(cost) + " Chocolate");
                    lore.add("");
                    lore.add(canAfford ? "§eClick to upgrade!" : "§cNot enough chocolate!");
                }

                return ItemStackCreator.getStack("§dRabbit Shrine " + toRoman(level + 1), Material.RABBIT_FOOT, 1, lore);
            }
        });

        // Slot 42: Coach Jackrabbit (player head)
        set(new GUIClickableItem(42) {
            @Override
            public void run(InventoryPreClickEvent event, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                if (ChocolateFactoryHelper.purchaseUpgrade(player, ChocolateFactoryHelper.UpgradeType.COACH_JACKRABBIT)) {
                    player.sendMessage("§aUpgraded Coach Jackrabbit!");
                    player.playSound(Sound.sound(Key.key("entity.player.levelup"), Sound.Source.PLAYER, 1.0f, 1.0f));
                } else {
                    player.sendMessage("§cYou don't have enough Chocolate!");
                    player.playSound(NOT_ENOUGH_CHOCOLATE_SOUND);
                }
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                DatapointChocolateFactory.ChocolateFactoryData data = ChocolateFactoryHelper.getData(player);
                int level = data.getCoachJackrabbitLevel();
                long cost = ChocolateFactoryHelper.getCoachJackrabbitCost(level);
                boolean canAfford = data.getChocolate() >= cost;
                double multiplierBonus = level * 0.01;

                List<String> lore = new ArrayList<>();
                lore.add("§8§oPep talk pro!");
                lore.add("");
                lore.add("§7§dCoach Jackrabbit §7is a motivational");
                lore.add("§7speaker that is helping you reach");
                lore.add("§7your full potential by granting §6+" + String.format("%.1fx", multiplierBonus));
                lore.add("§6Chocolate §7per second!");
                lore.add("");
                if (level >= 20) {
                    lore.add("§aCoach Jackrabbit has already taught");
                    lore.add("§ayou all that he can teach!");
                } else {
                    lore.add("§8§m-----------------");
                    lore.add("§a§lUPGRADE §8➜ §dCoach Jackrabbit " + toRoman(level + 2));
                    lore.add("  §6+0.01x Production Multiplier");
                    lore.add("");
                    lore.add("§7Cost");
                    lore.add("§6" + ChocolateFactoryHelper.formatChocolate(cost) + " Chocolate");
                    lore.add("");
                    lore.add(canAfford ? "§eClick to upgrade!" : "§cNot enough chocolate!");
                }

                return ItemStackCreator.getStackHead("§dCoach Jackrabbit " + toRoman(level + 1), COACH_JACKRABBIT_TEXTURE, 1, lore);
            }
        });

        // Slot 45: Chocolate Production info (cocoa beans)
        set(new GUIItem(45) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                DatapointChocolateFactory.ChocolateFactoryData data = ChocolateFactoryHelper.getData(player);

                double baseProduction = 0;
                for (DatapointChocolateFactory.EmployeeData employee : data.getEmployees().values()) {
                    baseProduction += employee.getProductionPerSecond();
                }

                double totalMultiplier = data.getShrineMultiplier() * data.getTimeTowerMultiplier() * data.getCoachMultiplier();

                List<String> lore = new ArrayList<>();
                lore.add("§6" + String.format("%.2f", data.getChocolatePerSecond()) + " Chocolate §8per second");
                lore.add("");
                lore.add("§7Base Chocolate: §6" + String.format("%.0f", baseProduction) + " §8per second");
                lore.add("  §6+" + String.format("%.0f", baseProduction) + " §8(Rabbit Employees§8)");
                lore.add("");
                lore.add("§7Total Multiplier: §6" + String.format("%.3fx", totalMultiplier));
                lore.add("  §6+1x §8(Base Multiplier)");
                if (data.getRabbitShrineLevel() > 0) {
                    lore.add("  §6+" + String.format("%.1fx", data.getShrineMultiplier() - 1.0) + " §8(Rabbit Shrine)");
                }
                if (data.getCoachJackrabbitLevel() > 0) {
                    lore.add("  §6+" + String.format("%.2fx", data.getCoachMultiplier() - 1.0) + " §8(Coach Jackrabbit)");
                }
                if (data.isTimeTowerActive()) {
                    lore.add("  §6+" + String.format("%.1fx", data.getTimeTowerMultiplier() - 1.0) + " §8(Time Tower)");
                }

                return ItemStackCreator.getStack("§6Chocolate Production", Material.COCOA_BEANS, 1, lore);
            }
        });

        // Slot 47: Chocolate Shop (emerald)
        set(new GUIClickableItem(47) {
            @Override
            public void run(InventoryPreClickEvent event, HypixelPlayer p) {
                // TODO: Open Chocolate Shop GUI
                p.sendMessage("§7Opening Chocolate Shop... (Coming Soon)");
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                List<String> lore = new ArrayList<>();
                lore.add("§7Spend your §6Chocolate §7on the world's");
                lore.add("§7most delectable treats in the");
                lore.add("§7§6Chocolate Shop§7.");
                lore.add("");
                lore.add("§eClick to view!");

                return ItemStackCreator.getStack("§6Chocolate Shop", Material.EMERALD, 1, lore);
            }
        });

        // Slot 48: Go Back (arrow)
        set(new GUIClickableItem(48) {
            @Override
            public void run(InventoryPreClickEvent event, HypixelPlayer p) {
                p.closeInventory();
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                List<String> lore = new ArrayList<>();
                lore.add("§7To Calendar and Events");

                return ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1, lore);
            }
        });

        // Slot 49: Close (barrier)
        set(GUIClickableItem.getCloseItem(49));

        // Slot 50: Hoppity's Collection (player head)
        set(new GUIClickableItem(50) {
            @Override
            public void run(InventoryPreClickEvent event, HypixelPlayer p) {
                // TODO: Open Hoppity's Collection GUI
                p.sendMessage("§7Opening Hoppity's Collection... (Coming Soon)");
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                DatapointChocolateFactory.ChocolateFactoryData data = ChocolateFactoryHelper.getData(player);

                int rabbitsFound = data.getEmployeeCount();
                int totalRabbits = 512;
                double percentage = (rabbitsFound / (double) totalRabbits) * 100;

                List<String> lore = new ArrayList<>();
                lore.add("§7Help §aHoppity §7find all of his §aChocolate");
                lore.add("§aRabbits §7during the §dHoppity's Hunt");
                lore.add("§d§7event!");
                lore.add("");
                lore.add("§7The more unique §aChocolate Rabbits");
                lore.add("§a§7that you find, the more your");
                lore.add("§7§6Chocolate Factory §7will produce!");
                lore.add("");
                lore.add("§7Finding duplicate Rabbits grants");
                lore.add("§7§a+10% §7extra §6Chocolate §7per duplicate,");
                lore.add("§7up to §a+100%§7!");
                lore.add("");
                lore.add("§7Rabbits Found: §e" + String.format("%.1f", percentage) + "§6%");
                lore.add("§2§l§m    §f§l§m                     §r §e" + rabbitsFound + "§6/§e" + totalRabbits);
                lore.add("");
                lore.add("§eClick to view!");

                return ItemStackCreator.getStackHead("§aHoppity's Collection", HOPPITY_TEXTURE, 1, lore);
            }
        });

        // Slot 51: Rabbit Hitman (bow)
        set(new GUIClickableItem(51) {
            @Override
            public void run(InventoryPreClickEvent event, HypixelPlayer p) {
                // TODO: Open Rabbit Hitman GUI
                p.sendMessage("§7Opening Rabbit Hitman... (Coming Soon)");
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                List<String> lore = new ArrayList<>();
                lore.add("§7§7Hire this private rabbit to hunt eggs");
                lore.add("§7for you, they will collect eggs you");
                lore.add("§7missed!");
                lore.add("");
                lore.add("§7Available eggs: §a0");
                lore.add("§7Purchased slots: §e0§7/§a28");
                lore.add("");
                lore.add("§eClick to view!");

                return ItemStackCreator.getStack("§cRabbit Hitman", Material.BOW, 1, lore);
            }
        });

        // Slot 52: Chocolate Factory Ranking (milk bucket)
        set(new GUIItem(52) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                DatapointChocolateFactory.ChocolateFactoryData data = ChocolateFactoryHelper.getData(player);

                List<String> lore = new ArrayList<>();
                lore.add("§7§7You are §8#§b??? §7in all-time");
                lore.add("§7Chocolate.");
                lore.add("§7§8You are in the top §e??%§8 of players!");

                return ItemStackCreator.getStack("§dChocolate Factory Ranking", Material.MILK_BUCKET, 1, lore);
            }
        });

        // Slot 53: Chocolate Factory Milestones (ladder)
        set(new GUIClickableItem(53) {
            @Override
            public void run(InventoryPreClickEvent event, HypixelPlayer p) {
                // TODO: Open Milestones GUI
                p.sendMessage("§7Opening Chocolate Factory Milestones... (Coming Soon)");
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                List<String> lore = new ArrayList<>();
                lore.add("§7Unlock special §aChocolate Rabbits §7by");
                lore.add("§7reaching all-time §6Chocolate");
                lore.add("§6§7milestones!");
                lore.add("");
                lore.add("§eClick to view!");

                return ItemStackCreator.getStack("§6Chocolate Factory Milestones", Material.LADDER, 1, lore);
            }
        });
    }

    private void setupEmployeeSlots() {
        for (int i = 0; i < EMPLOYEE_SLOTS.length; i++) {
            int slot = EMPLOYEE_SLOTS[i];
            String employeeName = EMPLOYEE_NAMES[i];
            String employeeTexture = EMPLOYEE_TEXTURES[i];
            String employeeSubtitle = EMPLOYEE_SUBTITLES[i];

            set(new GUIClickableItem(slot) {
                @Override
                public void run(InventoryPreClickEvent event, HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    DatapointChocolateFactory.ChocolateFactoryData data = ChocolateFactoryHelper.getData(player);

                    // Check if employee is unlocked (prerequisite at level 20)
                    if (!ChocolateFactoryHelper.isEmployeeUnlocked(player, employeeName)) {
                        String prereq = ChocolateFactoryHelper.getEmployeePrerequisite(employeeName);
                        player.sendMessage("§cPromote §f" + prereq + " §cto §7[20§7] §fEmployee §cto unlock §f" + employeeName + "§c!");
                        player.playSound(NOT_ENOUGH_CHOCOLATE_SOUND);
                        return;
                    }

                    DatapointChocolateFactory.EmployeeData existingEmployee = data.getEmployees().get(employeeName);

                    // Check if employee is not hired yet - show special message
                    if (existingEmployee == null) {
                        long cost = ChocolateFactoryHelper.getEmployeeCost(employeeName, 1);
                        if (data.getChocolate() < cost) {
                            player.sendMessage("§c" + employeeName + " does not work at your §6Chocolate Factory §cyet!");
                            player.playSound(NOT_ENOUGH_CHOCOLATE_SOUND);
                            return;
                        }
                    }

                    if (ChocolateFactoryHelper.hireOrUpgradeEmployee(player, employeeName)) {
                        // Get fresh data after the hire/upgrade
                        DatapointChocolateFactory.ChocolateFactoryData newData = ChocolateFactoryHelper.getData(player);
                        DatapointChocolateFactory.EmployeeData emp = newData.getEmployees().get(employeeName);
                        int level = emp != null ? emp.getLevel() : 1;
                        String rank = getEmployeeRank(level);
                        String rankColor = "§" + getEmployeeRankColor(level);

                        player.sendMessage(rankColor + employeeName + " §7has been promoted to §7[" + level + "§7] " + rankColor + rank + "§7!");
                        player.playSound(Sound.sound(Key.key("entity.player.levelup"), Sound.Source.PLAYER, 1.0f, 1.0f));
                    } else {
                        player.sendMessage("§cYou don't have enough Chocolate!");
                        player.playSound(NOT_ENOUGH_CHOCOLATE_SOUND);
                    }
                }

                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    DatapointChocolateFactory.ChocolateFactoryData data = ChocolateFactoryHelper.getData(player);

                    // Check if employee is unlocked (prerequisite at level 20)
                    boolean isUnlocked = ChocolateFactoryHelper.isEmployeeUnlocked(player, employeeName);
                    String prerequisite = ChocolateFactoryHelper.getEmployeePrerequisite(employeeName);

                    if (!isUnlocked) {
                        // Locked - show grey dye with unlock requirement
                        DatapointChocolateFactory.EmployeeData prereqEmployee = data.getEmployees().get(prerequisite);
                        int prereqLevel = prereqEmployee != null ? prereqEmployee.getLevel() : 0;

                        List<String> lore = new ArrayList<>();
                        lore.add("§8§o" + employeeSubtitle);
                        lore.add("");
                        lore.add("§c§lLOCKED");
                        lore.add("");
                        lore.add("§7Promote §f" + prerequisite + " §7to §7[20§7]");
                        lore.add("§fEmployee §7to unlock!");
                        lore.add("");
                        lore.add("§7Progress: §e" + prereqLevel + "§7/§a20");

                        return ItemStackCreator.getStack("§c" + employeeName, Material.GRAY_DYE, 1, lore);
                    }

                    DatapointChocolateFactory.EmployeeData employee = data.getEmployees().get(employeeName);
                    double baseProduction = ChocolateFactoryHelper.getEmployeeBaseProduction(employeeName);

                    if (employee == null) {
                        // Not hired yet - show grey dye
                        long cost = ChocolateFactoryHelper.getEmployeeCost(player, employeeName, 1);
                        boolean canAfford = data.getChocolate() >= cost;

                        List<String> lore = new ArrayList<>();
                        lore.add("§8§o" + employeeSubtitle);
                        lore.add("");
                        lore.add("§c" + employeeName + " §7does not work at");
                        lore.add("§7your §6Chocolate Factory §7yet!");
                        lore.add("");
                        lore.add("§7Hire them and they will produce");
                        lore.add("§6+" + String.format("%.0f", baseProduction) + " Chocolate §7per second!");
                        lore.add("");
                        lore.add("§7Cost: " + (canAfford ? "§6" : "§c") + ChocolateFactoryHelper.formatChocolate(cost) + " Chocolate");
                        lore.add("");
                        lore.add(canAfford ? "§eClick to hire!" : "§cYou don't have enough Chocolate!");

                        return ItemStackCreator.getStack("§c" + employeeName, Material.GRAY_DYE, 1, lore);
                    } else {
                        // Already hired - show player head
                        int level = employee.getLevel();
                        long cost = ChocolateFactoryHelper.getEmployeeCost(player, employeeName, level + 1);
                        boolean canAfford = data.getChocolate() >= cost;
                        double currentProduction = baseProduction * level;
                        String rank = getEmployeeRank(level);
                        String rankColor = getEmployeeRankColor(level);

                        List<String> lore = new ArrayList<>();
                        lore.add("§8§o" + employeeSubtitle);
                        lore.add("");
                        lore.add("§7§b" + employeeName + " §7is a §" + rankColor + rank + "§7. They");
                        if (rank.equals("Board Member")) {
                            lore.add("§7are on the Board of Rabbits and");
                        } else {
                            lore.add("§7are working hard and");
                        }
                        lore.add("§7produce §6+" + String.format("%.0f", currentProduction) + " Chocolate §7per second!");
                        lore.add("");

                        if (level >= 220) {
                            lore.add("§7§b" + employeeName + " §ahas climbed as far as the");
                            lore.add("§acorporate ladder will allow!");
                        } else {
                            lore.add("§8§m-----------------");
                            lore.add("§a§lPROMOTE §8➜ §7[" + (level + 1) + "§7] §" + getEmployeeRankColor(level + 1) + getEmployeeRank(level + 1));
                            double nextProduction = baseProduction * (level + 1);
                            lore.add("  §6+" + String.format("%.0f", baseProduction) + " Chocolate per second");
                            lore.add("");
                            lore.add("§7Cost");
                            lore.add("§6" + ChocolateFactoryHelper.formatChocolate(cost) + " Chocolate");
                            lore.add("");
                            lore.add(canAfford ? "§eClick to promote!" : "§cYou don't have enough Chocolate!");
                        }

                        return ItemStackCreator.getStackHead("§b" + employeeName + "§8 - §7[" + level + "§7] §" + rankColor + rank, employeeTexture, 1, lore);
                    }
                }
            });
        }
    }

    private String getEmployeeRank(int level) {
        if (level >= 220) return "Board Member";
        if (level >= 200) return "Executive";
        if (level >= 180) return "Director";
        if (level >= 140) return "Manager";
        if (level >= 120) return "Assistant";
        if (level >= 20) return "Employee";
        if (level >= 1) return "Intern";
        return "Unemployed";
    }

    private String getEmployeeRankColor(int level) {
        if (level >= 220) return "b"; // Aqua - Board Member
        if (level >= 200) return "d"; // Light Purple - Executive
        if (level >= 180) return "6"; // Gold - Director
        if (level >= 140) return "5"; // Dark Purple - Manager
        if (level >= 120) return "9"; // Blue - Assistant
        if (level >= 20) return "a"; // Green - Employee
        if (level >= 1) return "7"; // Gray - Intern
        return "c"; // Red - Unemployed
    }

    private String toRoman(int number) {
        if (number <= 0) return "I";
        String[] thousands = {"", "M", "MM", "MMM"};
        String[] hundreds = {"", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"};
        String[] tens = {"", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"};
        String[] ones = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"};

        return thousands[number / 1000] +
                hundreds[(number % 1000) / 100] +
                tens[(number % 100) / 10] +
                ones[number % 10];
    }

    @Override
    public void refreshItems(HypixelPlayer player) {
        // Update production before refreshing
        ChocolateFactoryHelper.updateProduction((SkyBlockPlayer) player);

        // Re-setup all items to refresh values
        setItems(new InventoryGUIOpenEvent(player, this, getInventory()));
    }

    @Override
    public int refreshRate() {
        return 20; // Refresh every second (20 ticks)
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
