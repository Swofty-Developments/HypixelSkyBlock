package net.swofty.types.generic.gui.inventory.inventories;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointMinionData;
import net.swofty.types.generic.data.mongodb.CoopDatabase;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.RefreshingGUI;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.gui.inventory.item.GUIItem;
import net.swofty.types.generic.item.MaterialQuantifiable;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.item.impl.MinionFuelItem;
import net.swofty.types.generic.item.impl.recipes.MinionUpgradeSpeedItem;
import net.swofty.types.generic.item.updater.NonPlayerItemUpdater;
import net.swofty.types.generic.item.updater.PlayerItemUpdater;
import net.swofty.types.generic.minion.IslandMinionData;
import net.swofty.types.generic.minion.SkyBlockMinion;
import net.swofty.types.generic.minion.extension.MinionExtension;
import net.swofty.types.generic.minion.extension.MinionExtensionData;
import net.swofty.types.generic.minion.extension.MinionExtensions;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.StringUtility;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GUIMinion extends SkyBlockInventoryGUI implements RefreshingGUI {
    private static final int[] SLOTS = new int[]{
            21, 22, 23, 24, 25,
            30, 31, 32, 33, 34,
            39, 40, 41, 42, 43
    };
    private final IslandMinionData.IslandMinion minion;

    public GUIMinion(IslandMinionData.IslandMinion minion) {
        super(minion.getMinion().getDisplay() + " " + StringUtility.getAsRomanNumeral(minion.getTier()),
                InventoryType.CHEST_6_ROW);

        this.minion = minion;
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        if (e.player().isCoop()) {
            CoopDatabase.Coop coop = e.player().getCoop();
            coop.getOnlineMembers().forEach(member -> {
                if (member.getUuid() == e.player().getUuid()) return;

                if (SkyBlockInventoryGUI.GUI_MAP.containsKey(member.getUuid())) {
                    if (SkyBlockInventoryGUI.GUI_MAP.get(member.getUuid()) instanceof GUIMinion) {
                        e.player().closeInventory();
                        e.player().sendMessage("§cYou can't open this inventory while a coop member has it open!");
                    }
                }
            });
        }
        fill(Material.BLACK_STAINED_GLASS_PANE, "");

        set(new GUIClickableItem(53) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                player.closeInventory();
                player.addAndUpdateItem(minion.asSkyBlockItem());
                minion.getItemsInMinion().forEach(item -> player.addAndUpdateItem(item.toSkyBlockItem()));
                for (SkyBlockItem item : minion.getExtensionData().getMinionUpgrades()) {
                    player.addAndUpdateItem(item);
                }
                player.addAndUpdateItem(minion.getExtensionData().getMinionSkin());
                player.addAndUpdateItem(minion.getExtensionData().getAutomatedShipping());
                if (minion.getExtensionData().getFuel() != null) {
                    long timeFuelLasts = ((MinionFuelItem)minion.getExtensionData().getFuel().getGenericInstance()).getFuelLastTimeInMS();
                    if (timeFuelLasts == 0) {
                        player.addAndUpdateItem(minion.getExtensionData().getFuel());
                    }
                }
                minion.removeMinion();
                player.getSkyBlockIsland().getMinionData().getMinions().remove(minion);

                player.sendMessage("§aYou picked up a minion! You currently have " +
                        player.getSkyBlockIsland().getMinionData().getMinions().size() +
                " out of a maximum of " + player.getDataHandler().get(
                        DataHandler.Data.MINION_DATA,
                        DatapointMinionData.class).getValue().getSlots()
                + " minions placed.");
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack("§aPickup Minion", Material.BEDROCK, 1,
                        "§eClick to pickup!");
            }
        });

        set(new GUIClickableItem(48) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                if (minion.getItemsInMinion().isEmpty()) {
                    player.sendMessage("§cThis Minion does not have any items stored!");
                    return;
                }

                minion.getItemsInMinion().forEach(item -> {
                    player.addAndUpdateItem(new SkyBlockItem(item.getMaterial(), item.getAmount()));
                });
                minion.setItemsInMinion(new ArrayList<>());

                refreshItems(player);
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack("§aCollect All", Material.CHEST, 1,
                        "§eClick to collect all items!");
            }
        });

        set(new GUIClickableItem(3) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {

            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack("§aIdeal Layout", Material.REDSTONE_TORCH, 1,
                        "§7View the most efficient spot for this",
                        "§7minion to be placed in.",
                        " ",
                        "§eClick to view!");
            }
        });

        set(new GUIItem(4) {
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                List<Component> lore = new ArrayList<>();
                Minion.getLore(minion.asSkyBlockItem(),minion.getSpeedPercentage()).forEach(line -> {
                    lore.add(Component.text("§r" + line.replace("&", "§"))
                            .decorations(Collections.singleton(TextDecoration.ITALIC), false));
                });
                return PlayerItemUpdater.playerUpdate(player,minion.asSkyBlockItem().getItemStack()).lore(lore);
            }
        });

        set(new GUIClickableItem(5) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                new GUIMinionRecipes(minion.getMinion(), GUIMinion.this).open(player);
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                List<SkyBlockMinion.MinionTier> minionTiers = minion.getMinion().asSkyBlockMinion().getTiers();

                int speedPercentage = minion.getSpeedPercentage();
                final DecimalFormat formattter = new DecimalFormat("#.##");

                return ItemStackCreator.getStack("§aNext Tier", Material.GOLD_INGOT, 1,
                        "§7View the items required to upgrade",
                        "§7this minion to the next tier.",
                        " ",
                        "§7Time Between Actions: §8" + formattter.format(minionTiers.get(minion.getTier() - 1).timeBetweenActions() / (1. + speedPercentage/100.)) + "s"
                             + " §l> §a" + formattter.format(minionTiers.get(minion.getTier()).timeBetweenActions() / (1. + speedPercentage/100.)) + "s",
                        "§7Max Storage: §8" + minionTiers.get(minion.getTier() - 1).storage() + " §l> " +
                                "§e" + minionTiers.get(minion.getTier()).storage(),
                        " ",
                        "§eClick to view!");
            }
        });

        MinionExtensionData extensionData = minion.getExtensionData();
        Arrays.stream(MinionExtensions.values()).forEach(extensionValue -> {
            for (int slot : extensionValue.getSlots()) {
                MinionExtension minionExtension = extensionData.getMinionExtension(slot);
                set(minionExtension.getDisplayItem(minion, slot));
            }
        });

        SkyBlockMinion.MinionTier minionTier = minion.getMinion().asSkyBlockMinion().getTiers().get(
                minion.getTier() - 1
        );

        int i = 0;
        for (int slot : SLOTS) {
            i++;
            boolean unlocked = minionTier.getSlots() >= i;

            set(new GUIItem(slot) {
                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    return unlocked ? ItemStack.builder(Material.AIR) :
                            ItemStackCreator.createNamedItemStack(Material.WHITE_STAINED_GLASS_PANE);
                }
            });
        }
    }

    @Override
    public void refreshItems(SkyBlockPlayer player) {
        if (!player.getSkyBlockIsland().getMinionData().getMinions().contains(minion)) {
            player.closeInventory();
            return;
        }

        MinionExtensionData extensionData = minion.getExtensionData();
        Arrays.stream(MinionExtensions.values()).forEach(extensionValue -> {
            for (int slot : extensionValue.getSlots()) {
                MinionExtension minionExtension = extensionData.getMinionExtension(slot);
                set(minionExtension.getDisplayItem(minion, slot));
            }
        });

        SkyBlockMinion.MinionTier minionTier = minion.getMinion().asSkyBlockMinion().getTiers().get(
                minion.getTier() - 1
        );

        int i = 0;
        for (int slot : SLOTS) {
            i++;
            boolean unlocked = minionTier.getSlots() >= i;

            int finalI = i;
            set(new GUIClickableItem(slot) {
                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                    if (!e.getCursorItem().isAir()) {
                        player.sendMessage("§cYou can't put items in this inventory!");

                        e.setCancelled(true);
                        return;
                    }

                    if (!unlocked) {
                        e.setCancelled(true);
                        return;
                    }
                    if (minion.getItemsInMinion().size() < finalI) return;

                    MaterialQuantifiable item = minion.getItemsInMinion().get(finalI - 1);
                    minion.getItemsInMinion().remove(item);

                    player.addAndUpdateItem(new SkyBlockItem(item.getMaterial(), item.getAmount()));
                    refreshItems(player);
                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    if (!unlocked) return ItemStackCreator.createNamedItemStack(Material.WHITE_STAINED_GLASS_PANE);

                    if (minion.getItemsInMinion().size() < finalI) return ItemStack.builder(Material.AIR);

                    MaterialQuantifiable item = minion.getItemsInMinion().get(finalI - 1);
                    SkyBlockItem skyBlockItem = new SkyBlockItem(item.getMaterial(), item.getAmount());

                    return new NonPlayerItemUpdater(skyBlockItem).getUpdatedItem();
                }
            });
        }
    }

    @Override
    public int refreshRate() {
        return 5;
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
