package net.swofty.type.hub.gui.kat;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.components.PetComponent;
import net.swofty.type.skyblockgeneric.item.handlers.pet.KatUpgrade;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class GUIConfirmKat extends HypixelInventoryGUI {

    private SkyBlockItem pet;

    public GUIConfirmKat(SkyBlockItem pet) {
        super("Confirm Hiring Kat", InventoryType.CHEST_3_ROW);
        this.pet = pet;
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));

        set(new GUIClickableItem(11) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p; 
                if (pet.getComponent(PetComponent.class).getKatUpgrades().getForRarity(pet.getAttributeHandler().getRarity().upgrade()) == null) return;
                KatUpgrade katUpgrade = pet.getComponent(PetComponent.class).getKatUpgrades().getForRarity(pet.getAttributeHandler().getRarity().upgrade());
                int coins = katUpgrade.getCoins();
                long time = katUpgrade.getTime();

                Long timeWhenFinished = time + System.currentTimeMillis();
                player.getKatData().setKatMap(timeWhenFinished, pet);
                player.removeCoins(coins);
                player.closeInventory();
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p; 
                if (pet.getComponent(PetComponent.class).getKatUpgrades().getForRarity(pet.getAttributeHandler().getRarity().upgrade()) == null) {
                    return ItemStackCreator.getStack("§aSomething went wrong!", Material.RED_TERRACOTTA, 1);
                }
                KatUpgrade katUpgrade = pet.getComponent(PetComponent.class).getKatUpgrades().getForRarity(pet.getAttributeHandler().getRarity().upgrade());
                long time = katUpgrade.getTime();
                return ItemStackCreator.getStack("§aConfirm", Material.GREEN_TERRACOTTA, 1,
                        "§cWARNING: You will not be able to",
                        "§cretrieve your pet for " + StringUtility.formatTimeLeftWrittenOut(time) + " and its",
                        "§clevel will change as a result of the",
                        "§crarity upgrade.");
            }
        });

        set(new GUIClickableItem(15) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p; 
                player.closeInventory();
                player.addAndUpdateItem(pet);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p; 
                return ItemStackCreator.getStack("§cCancel", Material.RED_TERRACOTTA, 1);
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
