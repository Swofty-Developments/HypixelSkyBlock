package net.swofty.type.skyblockgeneric.gui.inventories.experiments;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;

public class GUIExperiments extends HypixelInventoryGUI {

    private final int[] comingSoonSlot = {
            20, 21, 23, 24
    };
    private final int[] borderSlots = {
            45, 46, 47,48,51,52,53, 0, 1,2,3,4,5,6,7,8,9,18,27,36,17,26,35,44
    };


    public GUIExperiments() {
        super("Experimentation Table", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        int closeSlot = 49;
        int expBottleSlot = 50;
        int chronoSlot = 29;
        int superPairSlot = 22;
        int ultrasequenceSlot = 33;
        fill(Material.BLACK_STAINED_GLASS_PANE, " ");
        final String CHRONOMATRON_TEXTURE = "ewogICJ0aW1lc3RhbXAiIDogMTYyMjcwOTgxNjYwOSwKICAicHJvZmlsZUlkIiA6ICI2MTZiODhkNDMwNzM0ZTM3OWM3NDc1ODdlZTJkNzlmZCIsCiAgInByb2ZpbGVOYW1lIiA6ICJfX25vdGFodW1hbl9fIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzgxYjg0MzQ1MTE4NGE4Y2NkOGU2ZTQ5ZDBlZGYzNDUxZDNkZWE1MGZkZTViNmEyZjk4YWI3Y2YxMTM4YmNlY2UiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==";
        final String ULTRASEQUENCER_TEXTURE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2M2MWE3ZDExNmFiOWNkOWE1ZjljMTZhMTNlZjg3NzlhMmI4ZWUyNzQyMzRmYWVjOGJmNThkY2NiMGQ5NDc0NiJ9fX0=";
        final String SUPERPAIRS_TEXTURE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjA3M2JlMzBjMDgxNzZlMTI5ZmE1N2VlOTAyNTQwNzE5NTBkMWVhNWFlNDIyYTc4NTEyZjdhNjQ3ZDk4YzViNSJ9fX0=";



        for(int i : borderSlots) {
            set(new GUIItem(i) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer player) {
                    return ItemStackCreator.getStack(" ", Material.PURPLE_STAINED_GLASS_PANE, 1);
                }
            });
        }for(int i : comingSoonSlot) {
            set(new GUIItem(i) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer player) {
                    return ItemStackCreator.getStack("§7Pending Experiment...", Material.PINK_STAINED_GLASS_PANE, 1);
                }
            });
        }

        set(GUIClickableItem.getCloseItem(closeSlot));

        set(new GUIClickableItem(superPairSlot) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUISuperPairs().open(player);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                return ItemStackCreator.getStackHead(
                        "§dSuperpairs",
                        SUPERPAIRS_TEXTURE,
                        1,
                        "§7Main Experiment",
                        "",
                        "§7Find §bpairs §7of items on the",
                        "§7grid to unlock them.",
                        "",
                        "§7Earn §bEnchanting Exp §7and",
                        "§cpowerful §7enchanted books",
                        "§7every day!",
                        "",
                        "§dPlay add-ons first!"
                );
            }
        });

        set(new GUIClickableItem(chronoSlot) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIChronomatron().open(player);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                return ItemStackCreator.getStackHead(
                        "§dChronomatron",
                        CHRONOMATRON_TEXTURE,
                        1,
                        "§7Add-on Experiment",
                        "§aRepeat §7the musical §dpattern",
                        "§7to form the longest chain of notes.",
                        "",
                        "§7Earn extra §bEnchanting Exp",
                        "§7and extra clicks on your next §dSuperpairs§7!",
                        "",
                        "§eClick to play!"
                );
            }
        });

        set(new GUIClickableItem(ultrasequenceSlot) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIUltrasequencer().open(player);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                return ItemStackCreator.getStackHead("§dUltrasequencer",
                        ULTRASEQUENCER_TEXTURE,
                        1,

                        "§7Add-on Experiment",
                        "",
                        "§a1. §7Number(s) appear for 2 seconds.",
                        "§62. §7They all disappear!",
                        "§c3. §7Click them in order from memory.",
                        "",
                        "§7Earn extra §bEnchanting Exp",
                        "§7and extra clicks on your next §dSuperpairs§7!",
                        "",
                        "§eClick to play!"
                );
            }
        });

        set(new GUIItem(expBottleSlot) {


            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                return ItemStackCreator.getStack(
                        "§bExperience Bottles",
                        Material.EXPERIENCE_BOTTLE,
                        1,
                        "§7Missing experience?",
                        "§7Simple! Just consume the §bExperience",
                        "§bBottles §7from your inventories",
                        "§7directly or purchase some at the",
                        "§7current §6Bazaar §7price!",
                        "",
                        "§eClick to view!"
                );
            }
        });


        set(new GUIItem(expBottleSlot) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                return ItemStackCreator.getStack(
                        "§bExperience Bottles",
                        Material.EXPERIENCE_BOTTLE,
                        1,
                        "§7Missing experience?",
                        "§7Simple! Just consume the §bExperience",
                        "§bBottles §7from your inventories",
                        "§7directly or purchase some at the",
                        "§7current §6Bazaar §7price!",
                        "",
                        "§eClick to view!"
                );
            }


        });
        updateItemStacks(getInventory(), getPlayer());

    }



    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {

    }
}
