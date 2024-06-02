package net.swofty.types.generic.gui.inventory.inventories.sbmenu;

import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.ServerType;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.bags.GUIYourBags;
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.collection.GUICollections;
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.fasttravel.GUIFastTravel;
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.levels.GUISkyBlockLevels;
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.profiles.GUIProfileManagement;
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.questlog.GUIMissionLog;
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.recipe.GUIRecipeBook;
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.skills.GUISkills;
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.storage.GUIStorage;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;
import net.swofty.types.generic.levels.SkyBlockLevelRequirement;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.PlayerStatistics;
import net.swofty.types.generic.utility.StringUtility;

import java.util.ArrayList;
import java.util.List;

public class GUISkyBlockMenu extends SkyBlockInventoryGUI {
    public GUISkyBlockMenu() {
        super("SkyBlock Menu", InventoryType.CHEST_6_ROW);

        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getCloseItem(49));

        set(new GUIClickableItem(13) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                new GUISkyBlockProfile().open(player);
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                PlayerStatistics statistics = player.getStatistics();
                List<String> lore = new ArrayList<>(List.of("§7View your equipment, stats, and more!", "§e "));
                List<String> stats = new ArrayList<>(List.of("Health", "Defense", "Speed", "Strength", "Intelligence",
                        "Crit Chance", "Crit Damage", "Swing Range"
                ));
                statistics.allStatistics().getOverall().forEach((statistic, value) -> {
                    if (!value.equals(statistic.getBaseAdditiveValue()) || stats.contains(statistic.getDisplayName())) {
                        lore.add(" " + statistic.getDisplayColor() + statistic.getSymbol() + " " +
                                StringUtility.toNormalCase(statistic.name()) + " §f" +
                                StringUtility.decimalify(value, 2) + statistic.getSuffix());
                    }
                });

                lore.add("§e ");
                lore.add("§eClick to view!");

                return ItemStackCreator.getStackHead("§aYour SkyBlock Profile",
                        PlayerSkin.fromUuid(player.getUuid().toString()), 1,
                        lore
                );
            }
        });
        set(new GUIClickableItem(22) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                new GUISkyBlockLevels().open(player);
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                SkyBlockLevelRequirement levelRequirement = player.getSkyBlockExperience().getLevel();
                SkyBlockLevelRequirement nextLevel = levelRequirement.getNextLevel();

                return ItemStackCreator.getStackHead("§aSkyBlock Leveling", "3255327dd8e90afad681a19231665bea2bd06065a09d77ac1408837f9e0b242", 1,
                        "§7Your SkyBlock Level: §8[" + levelRequirement.getColor() + levelRequirement + "§8]",
                        " ",
                        "§7Determine how far you've",
                        "§7progressed in SkyBlock and earn",
                        "§7rewards from completing unique",
                        "§7tasks.",
                        " ",
                        "§7Progress to Level " + (nextLevel == null ? "§cMAX" : nextLevel),
                        player.getSkyBlockExperience().getNextLevelDisplay(),
                        " ",
                        "§eClick to view!"
                );
            }
        });
        set(new GUIClickableItem(29) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                new GUIYourBags().open(player);
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStackHead("§aYour Bags", "961a918c0c49ba8d053e522cb91abc74689367b4d8aa06bfc1ba9154730985ff", 1,
                        "§7Different bags allow you to store",
                        "§7many different items inside!",
                        " ",
                        "§eClick to view!"
                );
            }
        });
        set(new GUIClickableItem(30) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                new GUIPets().open(player);
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack("§aPets", Material.BONE, 1,
                        "§7View and manage all of your",
                        "§7Pets.",
                        " ",
                        "§7Level up your pets faster by",
                        "§7gaining XP in their favourite",
                        "§7skill!",
                        " ",
                        "§7Selected pet: " + (player.getPetData().getEnabledPet() == null ? "§cNone" : player.getPetData().getEnabledPet().getDisplayName()),
                        " ",
                        "§eClick to view!"
                );
            }
        });

        set(new GUIClickableItem(21) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                new GUIRecipeBook().open(player);
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                List<String> lore = new ArrayList<>(List.of(
                        "§7Through your adventure, you will",
                        "§7unlock recipes for all kinds of",
                        "§7special items! You can view how to",
                        "§7craft these items here.",
                        " "
                ));

                SkyBlockRecipe.getMissionDisplay(lore, player.getUuid());

                lore.add(" ");
                lore.add("§eClick to view!");
                return ItemStackCreator.getStack("§aRecipe Book", Material.BOOK, (short) 0, 1, lore);
            }
        });
        set(new GUIClickableItem(25) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                new GUIStorage().open(player);
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack("§aStorage", Material.CHEST, 1,
                        "§7Store global items that you",
                        "§7want to access at any time",
                        "§7from anywhere here.",
                        " ",
                        "§eClick to view!"
                );
            }
        });

        set(new GUIClickableItem(23) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                new GUIMissionLog().open(player);
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack("§aQuest Log", Material.WRITABLE_BOOK, (short) 0, 1,
                        "§7View your active quests, progress",
                        "§7and rewards.",
                        " ",
                        "§eClick to view!"
                );
            }
        });

        set(new GUIClickableItem(19) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                new GUISkills().open(player);
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack("§aYour Skills", Material.DIAMOND_SWORD, 1,
                        "§7View your Skill progression and",
                        "§7rewards.",
                        " ",
                        "§eClick to view!"
                );
            }
        });

        set(new GUIClickableItem(20) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                new GUICollections().open(player);
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                List<String> lore = new ArrayList<>(List.of(
                        "§7View all of the items available in",
                        "§7SkyBlock. Collect more of an item to",
                        "§7unlock rewards on your way to",
                        "§7becoming a master of SkyBlock!",
                        " "
                ));

                player.getCollection().getDisplay(lore);

                lore.add(" ");
                lore.add("§eClick to view!");
                return ItemStackCreator.getStack("§aCollections", Material.PAINTING, 1, lore.toArray(new String[0]));
            }
        });

        set(new GUIClickableItem(31) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                new GUICrafting().open(player);
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack("§aCrafting Table", Material.CRAFTING_TABLE, (short) 0, 1,
                        "§7Opens the crafting grid.",
                        " ",
                        "§eClick to open!"
                );
            }
        });
        set(new GUIClickableItem(47) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                if (e.getClickType().equals(ClickType.RIGHT_CLICK)) {
                    player.closeInventory();
                    player.sendTo(ServerType.ISLAND);
                    return;
                }

                new GUIFastTravel().open(player);
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStackHead("§bFast Travel", "f151cffdaf303673531a7651b36637cad912ba485643158e548d59b2ead5011", 1,
                        "§7Teleport to islands you've already",
                        "§7visited.",
                        " ",
                        "§8Right-click to warp home!",
                        "§eClick to pick location!"
                );
            }
        });

        set(new GUIClickableItem(48) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                new GUIProfileManagement().open(player);
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack("§aProfile Management", Material.NAME_TAG, (short) 0, 1,
                        "§7You can have multiple SkyBlock",
                        "§7profiles at the same time.",
                        " ",
                        "§7Each profile has its own island,",
                        "§7inventory, quest log...",
                        " ",
                        "§7Profiles: §e" + player.getProfiles().getProfiles().size() + "§6/§e4",
                        " ",
                        "§eClick to manage!"
                );
            }
        });
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
