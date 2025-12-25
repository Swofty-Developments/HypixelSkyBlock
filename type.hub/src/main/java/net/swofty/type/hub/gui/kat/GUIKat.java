package net.swofty.type.hub.gui.kat;

import net.minestom.server.component.DataComponents;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.components.PetComponent;
import net.swofty.type.skyblockgeneric.item.handlers.pet.KatUpgrade;
import net.swofty.type.skyblockgeneric.item.updater.PlayerItemUpdater;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;

public class GUIKat extends HypixelInventoryGUI {

    boolean pricePaid = false;

    public GUIKat() {
        super("Pet Sitter", InventoryType.CHEST_5_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getCloseItem(40));

        updateFromItem(null);
    }

    public void updateFromItem(SkyBlockItem item) {

        if (item == null) {
            set(new GUIClickableItem(13) {
                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                    ItemStack stack = p.getInventory().getCursorItem();

                    if (stack.get(DataComponents.CUSTOM_NAME) == null) {
                        updateFromItem(null);
                        return;
                    }

                    SkyBlockItem item = new SkyBlockItem(stack);
                    updateFromItem(item);
                }

                @Override
                public boolean canPickup() {
                    return true;
                }

                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                    return ItemStack.builder(Material.AIR);
                }
            });
            set(new GUIClickableItem(22) {
                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                    player.sendMessage("§cPlace a pet in the empty slot for Kat to take care of!");
                }

                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                    return ItemStackCreator.getStack(
                            "§ePet Sitter", Material.RED_TERRACOTTA, 1,
                            "§7Place a pet above for Kat to take",
                            "§7care of!",
                            "",
                            "§7After some time, your pet §9Rarity §7will",
                            "§7be upgraded!"
                    );
                }
            });
            updateItemStacks(getInventory(), getPlayer());
            return;
        }

        set(new GUIClickableItem(13) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                return PlayerItemUpdater.playerUpdate(player, item.getItemStack());
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                ItemStack stack = e.getClickedItem();
                if (stack.isAir()) return;

                updateFromItem(null);

                player.addAndUpdateItem(stack);
            }
        });

        if (item.getAmount() > 1 || !(item.hasComponent(PetComponent.class))) {
            set(new GUIItem(22) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                    return ItemStackCreator.getStack(
                            "§cError!", Material.BARRIER, 1,
                            "§cKat only takes care of pets!"
                    );
                }
            });
            updateItemStacks(getInventory(), getPlayer());
            return;
        }

        set(new GUIClickableItem(22) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                if (item.getComponent(PetComponent.class).getKatUpgrades().getForRarity(item.getAttributeHandler().getRarity().upgrade()) == null) return;
                KatUpgrade katUpgrade = item.getComponent(PetComponent.class).getKatUpgrades().getForRarity(item.getAttributeHandler().getRarity().upgrade());
                int coins = katUpgrade.getCoins();
                long time = katUpgrade.getTime();
                ItemType upgradeItem = katUpgrade.getItem();
                Integer itemAmount = katUpgrade.getAmount();

                if (player.getCoins() < coins) return;
                if (player.getAmountInInventory(upgradeItem) < itemAmount) return;

                new GUIConfirmKat(item).open(player);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                if (item.getComponent(PetComponent.class).getKatUpgrades().getForRarity(item.getAttributeHandler().getRarity().upgrade()) == null) {
                    return ItemStackCreator.getStack("§aSomething went wrong!", Material.RED_TERRACOTTA, 1);
                }
                KatUpgrade katUpgrade = item.getComponent(PetComponent.class).getKatUpgrades().getForRarity(item.getAttributeHandler().getRarity().upgrade());
                int coins = katUpgrade.getCoins();
                long time = katUpgrade.getTime();
                ItemType upgradeItem = katUpgrade.getItem();
                Integer itemAmount = katUpgrade.getAmount();
                ArrayList<String> lore = new ArrayList<>();
                Material material = Material.RED_TERRACOTTA;
                if (player.getCoins() >= coins && player.getAmountInInventory(upgradeItem) >= itemAmount) {
                    material = Material.GREEN_TERRACOTTA;
                }
                lore.add("§7Kat will take care of your §5" + item.getDisplayName());
                lore.add("§7for §9" + StringUtility.formatTimeLeftWrittenOut(time) + "§7 then its §9rarity§7 will be");
                lore.add("§7upgraded!");
                lore.add("");
                lore.add("§7Cost");
                if (upgradeItem != null) {
                    lore.add("§9" + StringUtility.toNormalCase(upgradeItem.name()) + " §8x" + itemAmount);
                }
                if (coins != 0) {
                    lore.add("§6" + StringUtility.commaify(coins) + " Coins");
                }
                if (player.getCoins() >= coins && player.getAmountInInventory(upgradeItem) >= itemAmount) {
                    lore.add("");
                    lore.add("§eClick to hire Kat!");
                } else if (player.getCoins() < coins) {
                    lore.add("");
                    lore.add("§cYou don't have enough Coins!");
                } else {
                    lore.add("");
                    lore.add("§cYou don't have the required items!");
                }
                return ItemStackCreator.getStack("§aHire Kat", material, 1, lore);
            }
        });
        updateItemStacks(getInventory(), getPlayer());
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent e, CloseReason reason) {
        if (reason == CloseReason.SERVER_EXITED && pricePaid) return;
        ((SkyBlockPlayer) e.getPlayer()).addAndUpdateItem(new SkyBlockItem(e.getInventory().getItemStack(13)));
    }

    @Override
    public void suddenlyQuit(Inventory inventory, HypixelPlayer player) {
        ((SkyBlockPlayer) player).addAndUpdateItem(new SkyBlockItem(inventory.getItemStack(13)));
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {

    }
}
