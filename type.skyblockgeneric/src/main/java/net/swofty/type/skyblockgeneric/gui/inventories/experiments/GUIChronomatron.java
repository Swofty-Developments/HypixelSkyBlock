package net.swofty.type.skyblockgeneric.gui.inventories.experiments;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.RefreshingGUI;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.proxyapi.ProxyService;
import net.swofty.commons.ServiceType;
import net.swofty.commons.protocol.objects.experimentation.ChronomatronStartProtocolObject;

public class GUIChronomatron extends HypixelInventoryGUI implements RefreshingGUI {

    private final int[] borderSlots = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 37, 38, 39, 41, 42, 43, 44};

    public GUIChronomatron() {
        super("Chronomatron -> stakes", InventoryType.CHEST_5_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(Material.BLACK_STAINED_GLASS_PANE, " ");

        for (int i : borderSlots) {
            set(new GUIItem(i) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer player) {
                    return ItemStackCreator.getStack(" ", Material.PURPLE_STAINED_GLASS_PANE, 1);
                }
            });
        }

        set(new GUIClickableItem(20) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                try {
                    ProxyService service = new ProxyService(ServiceType.EXPERIMENTATION);
                    ChronomatronStartProtocolObject.StartMessage request = new ChronomatronStartProtocolObject.StartMessage(player.getUuid(), "HIGH");
                    service.handleRequest(request).thenAccept(response -> new GUIChronomatronPlay("HIGH").open(player));
                } catch (Exception ex) {
                    player.sendMessage("§cFailed to start Chronomatron: " + ex.getMessage());
                }
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                return ItemStackCreator.getStack(
                        "§aHigh Experiment",
                        Material.LIME_DYE,
                        1,
                        "§7Chronomatron",
                        "",
                        "§7Colors on board: §d3",
                        "",
                        "§7XP Reward: §b1.5k Enchanting Exp",
                        "§7per §enote §7in longest chain!",
                        "",
                        "§7Superpairs Rewards:",
                        "§7Chain of §e5 §a+1 Click",
                        "§7Chain of §e9 §a+2 Clicks",
                        "",
                        "§7Requires: §bEnchanting XX",
                        "",
                        "§eClick to play!"
                );
            }
        });

        set(new GUIItem(21) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                return ItemStackCreator.getStack(
                        "§6Supreme Experiment",
                        Material.YELLOW_DYE,
                        1,
                        "§7Chronomatron",
                        "",
                        "§7Colors on board: §d7",
                        "",
                        "§7XP Reward: §b3.5k Enchanting Exp",
                        "§7per §enote §7in longest chain!",
                        "",
                        "§7Superpairs Rewards:",
                        "§7Chain of §e5 §a+1 Click",
                        "§7Chain of §e9 §a+2 Clicks",
                        "",
                        "§7Requires: §bEnchanting XXX",
                        "",
                        "§eClick to play!"
                );
            }
        });

        set(new GUIItem(22) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                return ItemStackCreator.getStack(
                        "§eGrand Experiment",
                        Material.ORANGE_DYE,
                        1,
                        "§7Chronomatron",
                        "",
                        "§7Colors on board: §d5",
                        "",
                        "§7XP Reward: §b2.5k Enchanting Exp",
                        "§7per §enote §7in longest chain!",
                        "",
                        "§7Superpairs Rewards:",
                        "§7Chain of §e5 §a+1 Click",
                        "§7Chain of §e9 §a+2 Clicks",
                        "",
                        "§7Requires: §bEnchanting XXV",
                        "",
                        "§eClick to play!"
                );
            }
        });

        set(new GUIItem(23) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                return ItemStackCreator.getStack(
                        "§cTranscendent Experiment",
                        Material.RED_DYE,
                        1,
                        "§7Chronomatron",
                        "",
                        "§7Colors on board: §d9",
                        "",
                        "§7XP Reward: §b4.5k Enchanting Exp",
                        "§7per §enote §7in longest chain!",
                        "",
                        "§7Superpairs Rewards:",
                        "§7Chain of §e5 §a+1 Click",
                        "§7Chain of §e9 §a+2 Clicks",
                        "",
                        "§7Requires: §bEnchanting XXXV",
                        "",
                        "§eClick to play!"
                );
            }
        });

        set(new GUIItem(24) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                return ItemStackCreator.getStack(
                        "§dMetaphysical Experiment",
                        Material.PURPLE_DYE,
                        1,
                        "§7Chronomatron",
                        "",
                        "§7Colors on board: §d11",
                        "",
                        "§7XP Reward: §b6.0k Enchanting Exp",
                        "§7per §enote §7in longest chain!",
                        "",
                        "§7Superpairs Rewards:",
                        "§7Chain of §e5 §a+1 Click",
                        "§7Chain of §e9 §a+2 Clicks",
                        "",
                        "§7Requires: §bEnchanting XL",
                        "",
                        "§eClick to play!"
                );
            }
        });

        set(GUIClickableItem.getGoBackItem(40,new GUIExperiments()));


        updateItemStacks(getInventory(), getPlayer());
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {

    }

    @Override
    public void refreshItems(HypixelPlayer player) {
        // TODO: In next step we'll poll state and update the display
    }

    @Override
    public int refreshRate() {
        return 10;
    }
}
