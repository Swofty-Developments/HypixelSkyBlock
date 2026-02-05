package net.swofty.type.skyblockgeneric.gui.inventories;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ClickContext;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.skyblockgeneric.chocolatefactory.ChocolateFactoryHelper;
import net.swofty.type.skyblockgeneric.chocolatefactory.ChocolateRabbit;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointChocolateFactory;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import net.swofty.commons.StringUtility;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class GUIChocolateFactory implements StatefulView<GUIChocolateFactory.State> {
    // Texture IDs
    private static final String CHOCOLATE_TEXTURE = "9a815398e7da89b1bc08f646cafc8e7b813da0be0eec0cce6d3eff5207801026";
    private static final String HOPPITY_TEXTURE = "b79e7f3341b672d9de6564cbaca052a6a723ea466a2e66af35ba1ba855f0d692";
    private static final String COACH_JACKRABBIT_TEXTURE = "bc0cc67e79c228e541e68aeb1d81ed7af51166622ad4db9417d7a29d1b89af95";

    private static final Sound CLICK_SOUND = Sound.sound(Key.key("block.note_block.bit"), Sound.Source.PLAYER, 1.0f, 1.21f);
    private static final Sound NOT_ENOUGH_CHOCOLATE_SOUND = Sound.sound(Key.key("entity.enderman.teleport"), Sound.Source.PLAYER, 8.0f, 0.0f);
    private static final Sound UPGRADE_SOUND = Sound.sound(Key.key("block.note_block.pling"), Sound.Source.PLAYER, 8.0f, 4.05f);

    // Employee slots (slots 28-34)
    private static final int[] EMPLOYEE_SLOTS = {28, 29, 30, 31, 32, 33, 34};
    private static final String[] EMPLOYEE_NAMES = {
            "Rabbit Bro", "Rabbit Cousin", "Rabbit Sis", "Rabbit Daddy",
            "Rabbit Granny", "Rabbit Uncle", "Rabbit Dog"
    };
    private static final String[] EMPLOYEE_TEXTURES = {
            "287934bdd9df2705b251bb997e029b18c1e94df12992b8107e74497b205ca7e8",
            "a982825c01b658f348a099b4579029a180d2e415183951b2e6e5e27257df4254",
            "fd076e0e3d4072d0fffee0a87a5d726fc34b2bcec38c264fb9b67871a8ead633",
            "57cab0c34d7ddcf72db56ff36f2883f554cff76eb5d3b3e0562338036c976043",
            "d6eb2d85ee8e3af1c2ec934beb70a39c5e766b23bdab63210bd2aacd73cbbfc8",
            "a865176723a0b9ee2916180a55a04cccb7704ad1f31fdf3e9d89c798f6802e6b",
            "35ca98bede3865dd1205e4d091036cd9dc36791b83ea4e0ff4a99ad61b71e898"
    };
    private static final String[] EMPLOYEE_SUBTITLES = {
            "Ambition on two feet!", "Laid-back legend!", "Rebel with a cause!",
            "CFO with nerves of steel!", "Storyteller supreme!",
            "Stuck in a highlight reel!", "Making chocolate, not eating it!"
    };

    public record State() {}

    @Override
    public State initialState() {
        return new State();
    }

    @Override
    public ViewConfiguration<State> configuration() {
        return ViewConfiguration.withString((state, ctx) -> "Chocolate Factory", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void onOpen(State state, ViewContext ctx) {
        // Update production based on time elapsed (handles offline production)
        ChocolateFactoryHelper.updateProduction((SkyBlockPlayer) ctx.player());
    }

    @Override
    public void onRefresh(State state, ViewContext ctx) {
        ChocolateFactoryHelper.updateProduction((SkyBlockPlayer) ctx.player());
    }

    @Override
    public void layout(ViewLayout<State> layout, State state, ViewContext ctx) {
        Components.fill(layout);

        SkyBlockPlayer player = (SkyBlockPlayer) ctx.player();
        ChocolateFactoryHelper.updateProduction(player);
        DatapointChocolateFactory.ChocolateFactoryData data = ChocolateFactoryHelper.getData(player);

        // Slot 13: Chocolate cookie (clickable)
        layout.slot(13, (s, c) -> {
            SkyBlockPlayer p = (SkyBlockPlayer) c.player();
            DatapointChocolateFactory.ChocolateFactoryData d = ChocolateFactoryHelper.getData(p);

            List<String> lore = new ArrayList<>();
            lore.add("§7§6Chocolate§7, of course, is not a valid");
            lore.add("§7source of §anutrition§7. This, however,");
            lore.add("§7does not stop it from being §dawesome§7.");
            lore.add("");
            lore.add("§7Chocolate Production");
            lore.add("§6" + String.format("%.2f", d.getChocolatePerSecond()) + " §8per second");
            lore.add("");
            lore.add("§7All-time Chocolate: §6" + ChocolateFactoryHelper.formatChocolate(d.getChocolateAllTime()));
            lore.add("");
            lore.add("§eClick to uncover the meaning of life!");

            return ItemStackCreator.getStackHead(
                    "§e" + ChocolateFactoryHelper.formatChocolate(d.getChocolate()) + " §6Chocolate",
                    CHOCOLATE_TEXTURE, 1, lore);
        }, (click, c) -> {
            SkyBlockPlayer p = (SkyBlockPlayer) c.player();
            ChocolateFactoryHelper.handleClick(p);
            p.playSound(CLICK_SOUND);
            c.session(State.class).refresh();
        });

        // Slot 27: Prestige/Chocolate Factory level
        layout.slot(27, (s, c) -> {
            SkyBlockPlayer p = (SkyBlockPlayer) c.player();
            DatapointChocolateFactory.ChocolateFactoryData d = ChocolateFactoryHelper.getData(p);
            int prestigeLevel = d.getPrestigeLevel();
            String prestigeColor = ChocolateFactoryHelper.getPrestigeRankColor(prestigeLevel);

            List<String> lore = new ArrayList<>();
            lore.add("§7Chocolate Production Multiplier: §6" + String.format("%.1fx", d.getShrineMultiplier() * d.getCoachMultiplier()));
            lore.add("§7Max Rabbit Rarity: §b§lDIVINE");
            lore.add("§7Max Chocolate: §660B");
            lore.add("§7Max Employee: §7[220§7] §bBoard Member");
            lore.add("§7Max §cRabbit Hitman §7Slots: §628");
            lore.add("");
            lore.add("§7Chocolate this Prestige: §6" + ChocolateFactoryHelper.formatChocolate(d.getChocolateAllTime()));
            lore.add("");
            lore.add(prestigeLevel >= 6 ? "§aYou have reached max prestige!" : "§eClick to prestige!");

            return ItemStackCreator.getStack(prestigeColor + "Chocolate Factory " + StringUtility.getAsRomanNumeral(prestigeLevel + 1), Material.DROPPER, 1, lore);
        });

        // Employee slots (28-34)
        setupEmployeeSlots(layout, ctx);

        // Slot 35: Rabbit Barn
        layout.slot(35, (s, c) -> createRabbitBarnItem((SkyBlockPlayer) c.player()), (click, c) -> {
            SkyBlockPlayer p = (SkyBlockPlayer) c.player();
            DatapointChocolateFactory.ChocolateFactoryData d = ChocolateFactoryHelper.getData(p);

            if (d.getRabbitBarnLevel() >= 247) {
                p.sendMessage("§cYour Rabbit Barn is already at maximum capacity!");
                p.playSound(NOT_ENOUGH_CHOCOLATE_SOUND);
                return;
            }

            if (ChocolateFactoryHelper.purchaseUpgrade(p, ChocolateFactoryHelper.UpgradeType.RABBIT_BARN)) {
                d = ChocolateFactoryHelper.getData(p);
                p.sendMessage("§7Your §aRabbit Barn §7capacity has been increased to §a" + (d.getMaxRabbitSlots() + 2) + " Rabbits§7!");
                p.playSound(UPGRADE_SOUND);
            } else {
                p.sendMessage("§cYou don't have enough Chocolate!");
                p.playSound(NOT_ENOUGH_CHOCOLATE_SOUND);
            }
            c.session(State.class).refresh();
        });

        // Slot 38: Hand-Baked Chocolate
        layout.slot(38, (s, c) -> createHandBakedChocolateItem((SkyBlockPlayer) c.player()), (click, c) -> {
            SkyBlockPlayer p = (SkyBlockPlayer) c.player();
            DatapointChocolateFactory.ChocolateFactoryData d = ChocolateFactoryHelper.getData(p);

            if (d.getHandBakedChocolateLevel() >= 10) {
                p.sendMessage("§cYou only have so many fingers! You can't click any faster!");
                p.playSound(NOT_ENOUGH_CHOCOLATE_SOUND);
                return;
            }

            if (ChocolateFactoryHelper.purchaseUpgrade(p, ChocolateFactoryHelper.UpgradeType.HAND_BAKED_CHOCOLATE)) {
                d = ChocolateFactoryHelper.getData(p);
                p.sendMessage("§7You will now produce §6+" + d.getClickPower() + " Chocolate §7per click!");
                p.playSound(UPGRADE_SOUND);
            } else {
                p.sendMessage("§cYou don't have enough Chocolate!");
                p.playSound(NOT_ENOUGH_CHOCOLATE_SOUND);
            }
            c.session(State.class).refresh();
        });

        // Slot 39: Time Tower
        layout.slot(39, (s, c) -> createTimeTowerItem((SkyBlockPlayer) c.player()), (click, c) -> {
            SkyBlockPlayer p = (SkyBlockPlayer) c.player();
            DatapointChocolateFactory.ChocolateFactoryData d = ChocolateFactoryHelper.getData(p);

            if (d.getPrestigeLevel() < 1) {
                p.sendMessage("§cThis requires Chocolate Factory II!");
                p.playSound(NOT_ENOUGH_CHOCOLATE_SOUND);
                return;
            }

            // Right-click to activate
            if (click.click() instanceof Click.Right && d.getTimeTowerLevel() > 0 && d.getTimeTowerCharges() > 0 && !d.isTimeTowerActive()) {
                if (ChocolateFactoryHelper.activateTimeTower(p)) {
                    p.sendMessage("§aTime Tower activated for 1 hour!");
                    p.playSound(Sound.sound(Key.key("block.beacon.activate"), Sound.Source.PLAYER, 1.0f, 1.0f));
                    c.session(State.class).refresh();
                    return;
                }
            }

            // Left-click to upgrade
            if (d.getTimeTowerLevel() >= 15) {
                p.sendMessage("§cThe Time Tower is already at its maximum level!");
                p.playSound(NOT_ENOUGH_CHOCOLATE_SOUND);
            } else if (ChocolateFactoryHelper.purchaseUpgrade(p, ChocolateFactoryHelper.UpgradeType.TIME_TOWER)) {
                p.sendMessage("§7You upgraded to §dTime Tower " + StringUtility.getAsRomanNumeral(d.getTimeTowerLevel() + 1) + "§7!");
                p.playSound(UPGRADE_SOUND);
            } else {
                p.sendMessage("§cYou don't have enough Chocolate!");
                p.playSound(NOT_ENOUGH_CHOCOLATE_SOUND);
            }
            c.session(State.class).refresh();
        });

        // Slot 41: Rabbit Shrine
        layout.slot(41, (s, c) -> createRabbitShrineItem((SkyBlockPlayer) c.player()), (click, c) -> {
            SkyBlockPlayer p = (SkyBlockPlayer) c.player();
            DatapointChocolateFactory.ChocolateFactoryData d = ChocolateFactoryHelper.getData(p);

            if (d.getPrestigeLevel() < 2) {
                p.sendMessage("§cThis requires Chocolate Factory III!");
                p.playSound(NOT_ENOUGH_CHOCOLATE_SOUND);
                return;
            }

            if (d.getRabbitShrineLevel() >= 20) {
                p.sendMessage("§cYour Rabbit Shrine is already at its maximum level!");
                p.playSound(NOT_ENOUGH_CHOCOLATE_SOUND);
                return;
            }

            if (ChocolateFactoryHelper.purchaseUpgrade(p, ChocolateFactoryHelper.UpgradeType.RABBIT_SHRINE)) {
                p.sendMessage("§aUpgraded Rabbit Shrine!");
                p.playSound(UPGRADE_SOUND);
            } else {
                p.sendMessage("§cYou don't have enough Chocolate!");
                p.playSound(NOT_ENOUGH_CHOCOLATE_SOUND);
            }
            c.session(State.class).refresh();
        });

        // Slot 42: Coach Jackrabbit
        layout.slot(42, (s, c) -> createCoachJackrabbitItem((SkyBlockPlayer) c.player()), (click, c) -> {
            SkyBlockPlayer p = (SkyBlockPlayer) c.player();
            DatapointChocolateFactory.ChocolateFactoryData d = ChocolateFactoryHelper.getData(p);

            if (d.getPrestigeLevel() < 3) {
                p.sendMessage("§cThis requires Chocolate Factory IV!");
                p.playSound(NOT_ENOUGH_CHOCOLATE_SOUND);
                return;
            }

            if (d.getCoachJackrabbitLevel() >= 20) {
                p.sendMessage("§cCoach Jackrabbit has already taught you all that he can teach!");
                p.playSound(NOT_ENOUGH_CHOCOLATE_SOUND);
                return;
            }

            if (ChocolateFactoryHelper.purchaseUpgrade(p, ChocolateFactoryHelper.UpgradeType.COACH_JACKRABBIT)) {
                p.sendMessage("§aUpgraded Coach Jackrabbit!");
                p.playSound(UPGRADE_SOUND);
            } else {
                p.sendMessage("§cYou don't have enough Chocolate!");
                p.playSound(NOT_ENOUGH_CHOCOLATE_SOUND);
            }
            c.session(State.class).refresh();
        });

        // Slot 45: Chocolate Production info
        layout.slot(45, (s, c) -> createProductionInfoItem((SkyBlockPlayer) c.player()));

        // Slot 49: Close
        Components.close(layout, 49);

        // Slot 50: Hoppity's Collection
        layout.slot(50, (s, c) -> {
            SkyBlockPlayer p = (SkyBlockPlayer) c.player();
            DatapointChocolateFactory.ChocolateFactoryData d = ChocolateFactoryHelper.getData(p);

            int rabbitsFound = d.getFoundRabbitCount();
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
            lore.add("§7Rabbits Found: §e" + String.format("%.1f", percentage) + "§6%");
            lore.add("§2§l§m    §f§l§m                     §r §e" + rabbitsFound + "§6/§e" + totalRabbits);
            lore.add("");
            lore.add("§eClick to view!");

            return ItemStackCreator.getStackHead("§aHoppity's Collection", HOPPITY_TEXTURE, 1, lore);
        }, (click, c) -> ((SkyBlockPlayer) c.player()).openView(new GUIHoppityCollection()));

        // Slot 51: Rabbit Hitman
        layout.slot(51, (s, c) -> {
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
        }, (click, c) -> c.player().sendMessage("§7Opening Rabbit Hitman... (Coming Soon)"));

        // Slot 52: Chocolate Factory Ranking
        layout.slot(52, (s, c) -> {
            List<String> lore = new ArrayList<>();
            lore.add("§7§7You are §8#§b??? §7in all-time");
            lore.add("§7Chocolate.");
            lore.add("§7§8You are in the top §e??%§8 of players!");
            return ItemStackCreator.getStack("§dChocolate Factory Ranking", Material.MILK_BUCKET, 1, lore);
        });

        // Slot 53: Chocolate Factory Milestones
        layout.slot(53, (s, c) -> {
            List<String> lore = new ArrayList<>();
            lore.add("§7Unlock special §aChocolate Rabbits §7by");
            lore.add("§7reaching all-time §6Chocolate");
            lore.add("§6§7milestones!");
            lore.add("");
            lore.add("§eClick to view!");
            return ItemStackCreator.getStack("§6Chocolate Factory Milestones", Material.LADDER, 1, lore);
        }, (click, c) -> ((SkyBlockPlayer) c.player()).openView(new GUIChocolateFactoryMilestones()));
    }

    private void setupEmployeeSlots(ViewLayout<State> layout, ViewContext ctx) {
        for (int i = 0; i < EMPLOYEE_SLOTS.length; i++) {
            int slot = EMPLOYEE_SLOTS[i];
            String employeeName = EMPLOYEE_NAMES[i];
            String employeeTexture = EMPLOYEE_TEXTURES[i];
            String employeeSubtitle = EMPLOYEE_SUBTITLES[i];

            layout.slot(slot, (s, c) -> createEmployeeItem((SkyBlockPlayer) c.player(), employeeName, employeeTexture, employeeSubtitle),
                    (click, c) -> {
                        handleEmployeeClick((SkyBlockPlayer) c.player(), employeeName);
                        c.session(State.class).refresh();
                    });
        }
    }

    private void handleEmployeeClick(SkyBlockPlayer player, String employeeName) {
        DatapointChocolateFactory.ChocolateFactoryData data = ChocolateFactoryHelper.getData(player);

        if (!ChocolateFactoryHelper.isEmployeeUnlocked(player, employeeName)) {
            String prereq = ChocolateFactoryHelper.getEmployeePrerequisite(employeeName);
            player.sendMessage("§cPromote §f" + prereq + " §cto §7[20§7] §fEmployee §cto unlock §f" + employeeName + "§c!");
            player.playSound(NOT_ENOUGH_CHOCOLATE_SOUND);
            return;
        }

        DatapointChocolateFactory.EmployeeData existingEmployee = data.getEmployees().get(employeeName);

        if (existingEmployee != null && existingEmployee.getLevel() >= 220) {
            player.sendMessage("§b" + employeeName + " §ccannot ascend the corporate ladder any further!");
            player.playSound(NOT_ENOUGH_CHOCOLATE_SOUND);
            return;
        }

        if (existingEmployee == null) {
            long cost = ChocolateFactoryHelper.getEmployeeCost(employeeName, 1);
            if (data.getChocolate() < cost) {
                player.sendMessage("§c" + employeeName + " does not work at your §6Chocolate Factory §cyet!");
                player.playSound(NOT_ENOUGH_CHOCOLATE_SOUND);
                return;
            }
        }

        if (ChocolateFactoryHelper.hireOrUpgradeEmployee(player, employeeName)) {
            DatapointChocolateFactory.ChocolateFactoryData newData = ChocolateFactoryHelper.getData(player);
            DatapointChocolateFactory.EmployeeData emp = newData.getEmployees().get(employeeName);
            int level = emp != null ? emp.getLevel() : 1;
            String rank = getEmployeeRank(level);
            String rankColor = "§" + getEmployeeRankColor(level);

            player.sendMessage(rankColor + employeeName + " §7has been promoted to §7[" + level + "§7] " + rankColor + rank + "§7!");
            player.playSound(UPGRADE_SOUND);
        } else {
            player.sendMessage("§cYou don't have enough Chocolate!");
            player.playSound(NOT_ENOUGH_CHOCOLATE_SOUND);
        }
    }

    private ItemStack.Builder createEmployeeItem(SkyBlockPlayer player, String employeeName, String texture, String subtitle) {
        DatapointChocolateFactory.ChocolateFactoryData data = ChocolateFactoryHelper.getData(player);
        boolean isUnlocked = ChocolateFactoryHelper.isEmployeeUnlocked(player, employeeName);
        String prerequisite = ChocolateFactoryHelper.getEmployeePrerequisite(employeeName);

        if (!isUnlocked) {
            List<String> lore = new ArrayList<>();
            lore.add("§8§o" + subtitle);
            lore.add("");
            lore.add("§7Promote §f" + prerequisite + " §7to §7[20§7]");
            lore.add("§fEmployee §7to unlock!");

            return ItemStackCreator.getStack("§c" + employeeName, Material.GRAY_DYE, 1, lore);
        }

        DatapointChocolateFactory.EmployeeData employee = data.getEmployees().get(employeeName);
        double baseProduction = ChocolateFactoryHelper.getEmployeeBaseProduction(employeeName);

        if (employee == null) {
            long cost = ChocolateFactoryHelper.getEmployeeCost(player, employeeName, 1);
            boolean canAfford = data.getChocolate() >= cost;

            List<String> lore = new ArrayList<>();
            lore.add("§8§o" + subtitle);
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

            return ItemStackCreator.getStackHead("§c" + employeeName, texture, 1, lore);
        }

        int level = employee.getLevel();
        long cost = ChocolateFactoryHelper.getEmployeeCost(player, employeeName, level + 1);
        boolean canAfford = data.getChocolate() >= cost;
        double currentProduction = baseProduction * level;
        String rank = getEmployeeRank(level);
        String rankColor = getEmployeeRankColor(level);
        boolean isUnemployed = level < 1;

        List<String> lore = new ArrayList<>();
        lore.add("§8§o" + subtitle);
        lore.add("");
        lore.add("§7§" + rankColor + employeeName + " §7is " + (isUnemployed ? "" : "a ") + "§" + rankColor + rank + "§7. They");
        lore.add(rank.equals("Board Member") ? "§7are on the Board of Rabbits and" : "§7are working hard and");
        lore.add("§7produce §6+" + String.format("%.0f", currentProduction) + " Chocolate §7per second!");
        lore.add("");

        if (level >= 220) {
            lore.add("§7§" + rankColor + employeeName + " §ahas climbed as far as the");
            lore.add("§acorporate ladder will allow!");
        } else {
            lore.add("§8§m-----------------");
            String action = isUnemployed ? "§a§lHIRE" : "§a§lPROMOTE";
            lore.add(action + " §8➜ §7[" + (level + 1) + "§7] §" + getEmployeeRankColor(level + 1) + getEmployeeRank(level + 1));
            lore.add("  §6+" + String.format("%.0f", baseProduction) + " Chocolate per second");
            lore.add("");
            lore.add("§7Cost");
            lore.add("§6" + ChocolateFactoryHelper.formatChocolate(cost) + " Chocolate");
            lore.add("");
            String clickAction = isUnemployed ? "§eClick to hire!" : "§eClick to promote!";
            lore.add(canAfford ? clickAction : "§cYou don't have enough Chocolate!");
        }

        String title = isUnemployed
                ? "§" + rankColor + employeeName + "§8 - §" + rankColor + rank
                : "§" + rankColor + employeeName + "§8 - §7[" + level + "§7] §" + rankColor + rank;
        return ItemStackCreator.getStackHead(title, texture, 1, lore);
    }

    private ItemStack.Builder createRabbitBarnItem(SkyBlockPlayer player) {
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
        if (level >= 247) {
            lore.add("");
            lore.add("§aYour Rabbit Barn is at maximum capacity!");
        } else {
            lore.add("§8§m-----------------");
            lore.add("§a§lUPGRADE §8➜ §aRabbit Barn " + StringUtility.getAsRomanNumeral(level + 2));
            lore.add("  §a+2 Capacity");
            lore.add("");
            lore.add("§7Cost");
            lore.add("§6" + ChocolateFactoryHelper.formatChocolate(cost) + " Chocolate");
            lore.add("");
            lore.add(canAfford ? "§eClick to upgrade!" : "§cNot enough chocolate!");
        }

        return ItemStackCreator.getStack("§aRabbit Barn " + StringUtility.getAsRomanNumeral(level + 1), Material.OAK_FENCE, 1, lore);
    }

    private ItemStack.Builder createHandBakedChocolateItem(SkyBlockPlayer player) {
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
        if (level >= 10) {
            lore.add("§aYou have reached the maximum");
            lore.add("§aamount of upgrades!");
        } else {
            lore.add("§8§m-----------------");
            lore.add("§a§lUPGRADE §8➜ §dHand-Baked Chocolate " + StringUtility.getAsRomanNumeral(level + 2));
            lore.add("  §6+1 Chocolate Per Click");
            lore.add("");
            lore.add("§7Cost");
            lore.add("§6" + ChocolateFactoryHelper.formatChocolate(cost) + " Chocolate");
            lore.add("");
            lore.add(canAfford ? "§eClick to upgrade!" : "§cNot enough chocolate!");
        }

        return ItemStackCreator.getStack("§dHand-Baked Chocolate " + StringUtility.getAsRomanNumeral(level + 1), Material.COOKIE, 1, lore);
    }

    private ItemStack.Builder createTimeTowerItem(SkyBlockPlayer player) {
        DatapointChocolateFactory.ChocolateFactoryData data = ChocolateFactoryHelper.getData(player);

        if (data.getPrestigeLevel() < 1) {
            List<String> lore = new ArrayList<>();
            lore.add("§7What does it do? Nobody knows...");
            lore.add("");
            lore.add("§cChocolate Factory II");
            return ItemStackCreator.getStack("§c???", Material.GRAY_DYE, 1, lore);
        }

        int level = data.getTimeTowerLevel();
        long cost = ChocolateFactoryHelper.getTimeTowerCost(level, data.getPrestigeLevel());
        boolean canAfford = data.getChocolate() >= cost;
        boolean isActive = data.isTimeTowerActive();

        List<String> lore = new ArrayList<>();
        lore.add("§7When active, this ancient building");
        lore.add("§7increases the production of your");
        lore.add("§7§6Chocolate Factory §7by §6+" + String.format("%.1fx", (level > 0 ? 0.1 * level : 0.1)) + " §7for §a1h§7.");
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
            lore.add("§a§lUPGRADE §8➜ §dTime Tower " + StringUtility.getAsRomanNumeral(level + 2));
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

        return ItemStackCreator.getStack("§dTime Tower " + StringUtility.getAsRomanNumeral(level + 1), Material.CLOCK, 1, lore);
    }

    private ItemStack.Builder createRabbitShrineItem(SkyBlockPlayer player) {
        DatapointChocolateFactory.ChocolateFactoryData data = ChocolateFactoryHelper.getData(player);

        if (data.getPrestigeLevel() < 2) {
            List<String> lore = new ArrayList<>();
            lore.add("§7What does it do? Nobody knows...");
            lore.add("");
            lore.add("§cChocolate Factory III");
            return ItemStackCreator.getStack("§c???", Material.GRAY_DYE, 1, lore);
        }

        int level = data.getRabbitShrineLevel();
        long cost = ChocolateFactoryHelper.getRabbitShrineCost(level);
        boolean canAfford = data.getChocolate() >= cost;
        int oddsBonus = level * 2;

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
            lore.add("§a§lUPGRADE §8➜ §dRabbit Shrine " + StringUtility.getAsRomanNumeral(level + 2));
            lore.add("  §a+2% Rare Rabbit Odds");
            lore.add("");
            lore.add("§7Cost");
            lore.add("§6" + ChocolateFactoryHelper.formatChocolate(cost) + " Chocolate");
            lore.add("");
            lore.add(canAfford ? "§eClick to upgrade!" : "§cNot enough chocolate!");
        }

        return ItemStackCreator.getStack("§dRabbit Shrine " + StringUtility.getAsRomanNumeral(level + 1), Material.RABBIT_FOOT, 1, lore);
    }

    private ItemStack.Builder createCoachJackrabbitItem(SkyBlockPlayer player) {
        DatapointChocolateFactory.ChocolateFactoryData data = ChocolateFactoryHelper.getData(player);

        if (data.getPrestigeLevel() < 3) {
            List<String> lore = new ArrayList<>();
            lore.add("§7What does it do? Nobody knows...");
            lore.add("");
            lore.add("§cChocolate Factory IV");
            return ItemStackCreator.getStack("§c???", Material.GRAY_DYE, 1, lore);
        }

        int level = data.getCoachJackrabbitLevel();
        long cost = ChocolateFactoryHelper.getCoachJackrabbitCost(level);
        boolean canAfford = data.getChocolate() >= cost;
        double multiplierBonus = level * 0.01;

        List<String> lore = new ArrayList<>();
        lore.add("§8§oPep talk pro!");
        lore.add("");
        lore.add("§7§dCoach Jackrabbit §7is a motivational");
        lore.add("§7speaker that is helping you reach");
        lore.add("§7your full potential by granting §6+" + String.format("%.2fx", multiplierBonus));
        lore.add("§6Chocolate §7per second!");
        lore.add("");
        if (level >= 20) {
            lore.add("§aCoach Jackrabbit has already taught");
            lore.add("§ayou all that he can teach!");
        } else {
            lore.add("§8§m-----------------");
            lore.add("§a§lUPGRADE §8➜ §dCoach Jackrabbit " + StringUtility.getAsRomanNumeral(level + 2));
            lore.add("  §6+0.01x Production Multiplier");
            lore.add("");
            lore.add("§7Cost");
            lore.add("§6" + ChocolateFactoryHelper.formatChocolate(cost) + " Chocolate");
            lore.add("");
            lore.add(canAfford ? "§eClick to upgrade!" : "§cNot enough chocolate!");
        }

        return ItemStackCreator.getStackHead("§dCoach Jackrabbit " + StringUtility.getAsRomanNumeral(level + 1), COACH_JACKRABBIT_TEXTURE, 1, lore);
    }

    private ItemStack.Builder createProductionInfoItem(SkyBlockPlayer player) {
        DatapointChocolateFactory.ChocolateFactoryData data = ChocolateFactoryHelper.getData(player);

        double employeeProduction = 0;
        for (DatapointChocolateFactory.EmployeeData employee : data.getEmployees().values()) {
            employeeProduction += employee.getProductionPerSecond();
        }

        // Calculate bonuses from Hoppity's Collection
        int rabbitChocolateBonus = 0;
        double rabbitMultiplierBonus = 0;
        for (String rabbitName : data.getFoundRabbits()) {
            try {
                ChocolateRabbit rabbit = ChocolateRabbit.valueOf(rabbitName);
                rabbitChocolateBonus += rabbit.getChocolateBonus();
                rabbitMultiplierBonus += rabbit.getMultiplierBonus();
            } catch (IllegalArgumentException ignored) {
            }
        }

        double baseProduction = employeeProduction + rabbitChocolateBonus;
        double totalMultiplier = data.getShrineMultiplier() * data.getTimeTowerMultiplier() * data.getCoachMultiplier() + rabbitMultiplierBonus;

        List<String> lore = new ArrayList<>();
        lore.add("§6" + String.format("%.2f", data.getChocolatePerSecond()) + " Chocolate §8per second");
        lore.add("");
        lore.add("§7Base Chocolate: §6" + String.format("%.0f", baseProduction) + " §8per second");
        lore.add("  §6+" + String.format("%.0f", employeeProduction) + " §8(Rabbit Employees§8)");
        if (rabbitChocolateBonus > 0) {
            lore.add("  §6+" + rabbitChocolateBonus + " §8(Hoppity's Collection§8)");
        }
        lore.add("");
        lore.add("§7Total Multiplier: §6" + String.format("%.3fx", totalMultiplier));
        lore.add("  §6+1x §8(Base Multiplier)");
        if (rabbitMultiplierBonus > 0) {
            lore.add("  §6+" + String.format("%.3fx", rabbitMultiplierBonus) + " §8(Hoppity's Collection§8)");
        }
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
        if (level >= 220) return "b";
        if (level >= 200) return "d";
        if (level >= 180) return "6";
        if (level >= 140) return "5";
        if (level >= 120) return "9";
        if (level >= 20) return "a";
        if (level >= 1) return "f";
        return "c";
    }

    /**
     * Opens the Chocolate Factory GUI for a player with auto-refresh every second.
     */
    public static void open(SkyBlockPlayer player) {
        player.openView(new GUIChocolateFactory()).refreshEvery(Duration.ofSeconds(1));
    }
}
