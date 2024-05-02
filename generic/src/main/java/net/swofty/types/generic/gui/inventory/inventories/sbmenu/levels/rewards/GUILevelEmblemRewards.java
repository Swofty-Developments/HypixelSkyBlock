package net.swofty.types.generic.gui.inventory.inventories.sbmenu.levels.rewards;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.levels.emblem.GUIEmblems;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.gui.inventory.item.GUIItem;
import net.swofty.types.generic.levels.SkyBlockEmblems;
import net.swofty.types.generic.levels.causes.LevelCause;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;

public class GUILevelEmblemRewards extends SkyBlockInventoryGUI {
    private static final int[] SLOTS = new int[]{
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43,
    };

    public GUILevelEmblemRewards() {
        super("Emblem Rewards", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getCloseItem(49));
        set(GUIClickableItem.getGoBackItem(48, new GUILevelEmblemRewards()));
        set(new GUIClickableItem(50) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                new GUIEmblems().open(player);
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack("§aPrefix Emblems", Material.NAME_TAG, 1,
                        "§7Add some spice by having an emblem",
                        "§7next to your name in chat and in tab!",
                        " ",
                        "§7Emblems are unlocked through various",
                        "§7activities such as leveling up",
                        "§7or completing achievements!",
                        " ",
                        "§7Emblems also show important data",
                        "§7associated with them in chat!",
                        " ",
                        "§eClick to view!");
            }
        });

        set(new GUIItem(14) {
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                List<String> lore = new ArrayList<>();
                lore.add("§7Emblems to show next to your name");
                lore.add("§7that signify special achievements.");
                lore.add(" ");
                lore.add("§7Next Reward:");

                List<SkyBlockEmblems.SkyBlockEmblem> levelEmblems = SkyBlockEmblems.getEmblemsWithLevelCause();
                SkyBlockEmblems.SkyBlockEmblem nextEmblem = null;
                for (SkyBlockEmblems.SkyBlockEmblem emblem : levelEmblems) {
                    if (player.getSkyBlockExperience().hasExperienceFor(emblem.cause())) continue;
                    nextEmblem = emblem;
                    break;
                }

                if (nextEmblem == null) {
                    lore.add("§cNo more rewards!");
                } else {
                    lore.add("§f" + nextEmblem.displayName() + " " + nextEmblem.emblem());
                    lore.add("§8at Level " + ((LevelCause) nextEmblem.cause()).getLevel());
                }

                lore.add(" ");
                lore.addAll(GUILevelRewards.getAsDisplay(
                        player.getSkyBlockExperience().getOfType(LevelCause.class).size(),
                        levelEmblems.size()
                ));

                return ItemStackCreator.getStack("§aEmblem Rewards",
                        Material.NAME_TAG, 1, lore);
            }
        });

        int index = 0;
        for (SkyBlockEmblems.SkyBlockEmblem emblem : SkyBlockEmblems.getEmblemsWithLevelCause()) {
            int slot = SLOTS[index];

            set(new GUIItem(slot) {
                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    return ItemStackCreator.getStack(emblem.displayName() + " " + emblem.emblem(),
                            Material.NAME_TAG, 1,
                            "§8Level " + ((LevelCause) emblem.cause()).getLevel(),
                            " ",
                            "§7Preview: " + player.getFullDisplayName(emblem),
                            " ",
                            (player.getSkyBlockExperience().hasExperienceFor(emblem.cause()) ? "§aYou have unlocked this reward!" : "§7Levels left to unlock: §3" + (((LevelCause) emblem.cause()).getLevel() - player.getSkyBlockExperience().getLevel().asInt())));
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
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}
