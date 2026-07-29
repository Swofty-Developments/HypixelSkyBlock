package net.swofty.type.skyblockgeneric.gui.inventories.banker;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.bank.BankAccountTier;
import net.swofty.type.skyblockgeneric.bank.PersonalBankService;
import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointBankData;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;

public class GUIBankUpgrades extends HypixelInventoryGUI {
    private static final int[] TIER_SLOTS = {10, 11, 12, 13, 14, 15, 16};
    private static final String LUXURIOUS_TEXTURE =
        "2b3b73ee2c9c725d807f35a988cb743732b75d7390796621324c207a8c407a90";
    private static final String PALATIAL_TEXTURE =
        "3366a9633a88d038db2771e32ed851845cb1d88b0ac8b7be8ac07299b5f2050";

    public GUIBankUpgrades() {
        super("Bank Account Upgrades", InventoryType.CHEST_4_ROW);
    }

    @Override
    public void setItems(InventoryGUIOpenEvent e) {
        fill(Material.BLACK_STAINED_GLASS_PANE, "");

        for (BankAccountTier tier : BankAccountTier.values()) {
            set(new GUIClickableItem(TIER_SLOTS[tier.ordinal()]) {
                @Override
                public void run(InventoryPreClickEvent event, HypixelPlayer p) {
                    tryUpgrade((SkyBlockPlayer) p, tier);
                }

                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    return createTierItem((SkyBlockPlayer) p, tier);
                }
            });
        }

        set(new GUIClickableItem(31) {
            @Override
            public void run(InventoryPreClickEvent event, HypixelPlayer p) {
                new GUIBanker().open((SkyBlockPlayer) p);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                return ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1, "§7To Bank");
            }
        });
    }

    private ItemStack.Builder createTierItem(SkyBlockPlayer player, BankAccountTier tier) {
        DatapointBankData.BankData data = PersonalBankService.data(player);
        List<String> lore = new ArrayList<>();
        String color = tierColor(tier);

        lore.add(tier == BankAccountTier.STARTER ? "§8Not upgraded" : "§8Bank Upgrade");
        lore.add("");
        lore.add(color + ">§m------- §r  §6Interest Tranches" + color + "  §m-------" + color + "<");
        addInterestTranches(lore, tier);
        lore.add("");

        double multiplier = 1D + Math.clamp(data.getMuseumMilestone(), 0, 30) * 0.02D;
        lore.add(" §7Max interest: §6" + StringUtility.commaify(tier.getBaseMaximumInterest() * multiplier));
        lore.add(" §8 (With " + StringUtility.commaify(interestBalance(tier)) + " balance)");
        lore.add(color + ">§m---------------------------------" + color + "<");
        lore.add("");
        lore.add("§7Max balance: §6" + balanceLabel(tier));
        lore.add("");
        addCostAndRequirements(lore, tier);
        lore.add(status(player, data, tier));

        String name = tier.getDisplayName() + " Account";
        return switch (tier) {
            case LUXURIOUS -> ItemStackCreator.getStackHead(name, LUXURIOUS_TEXTURE, 1, lore);
            case PALATIAL -> ItemStackCreator.getStackHead(name, PALATIAL_TEXTURE, 1, lore);
            default -> ItemStackCreator.getStack(name, tierMaterial(tier), 1, lore);
        };
    }

    private void addCostAndRequirements(List<String> lore, BankAccountTier tier) {
        if (tier == BankAccountTier.STARTER) {
            lore.add("§7Cost: §aComplimentary");
            lore.add("");
            return;
        }

        lore.add("§7Cost");
        lore.add("§6" + StringUtility.commaify(tier.getCoinCost()) + " Coins");
        lore.add("§9Enchanted Gold Block §8x" + tier.getEnchantedGoldBlocks());
        lore.add("");

        if (tier.getMuseumMilestone() > 0)
            lore.add("§cRequires Museum Milestone " + tier.getMuseumMilestone() + "!");
        if (tier.getGoldCollection() > 0)
            lore.add("§cRequires " + compactNumber(tier.getGoldCollection()) + " gold collection!");
        lore.add("");
    }

    private String status(SkyBlockPlayer player, DatapointBankData.BankData data, BankAccountTier tier) {
        BankAccountTier current = data.getAccountTier();
        if (tier.ordinal() < current.ordinal()) return "§cYou have a better account!";
        if (tier == current) return "§aThis is your account!";
        if (tier.ordinal() > current.ordinal() + 1) return "§cNeed previous upgrade!";
        if (data.getMuseumMilestone() < tier.getMuseumMilestone())
            return "§cMuseum Milestone too low!";
        if (player.getCollection().get(ItemType.GOLD_INGOT) < tier.getGoldCollection())
            return "§cGold collection too low!";
        if (player.getCoins() < tier.getCoinCost()) return "§cNot enough coins!";
        if (player.getAmountInInventory(ItemType.ENCHANTED_GOLD_BLOCK) < tier.getEnchantedGoldBlocks())
            return "§cNot enough Enchanted Gold Blocks!";
        return "§eClick to upgrade!";
    }

    private void tryUpgrade(SkyBlockPlayer player, BankAccountTier selected) {
        DatapointBankData.BankData data = PersonalBankService.data(player);
        if (data.getAccountTier().next() != selected) return;

        String status = status(player, data, selected);
        if (!status.equals("§eClick to upgrade!")) {
            player.sendMessage(status);
            return;
        }

        player.removeCoins(selected.getCoinCost());
        player.takeItem(ItemType.ENCHANTED_GOLD_BLOCK, selected.getEnchantedGoldBlocks());
        data.setAccountTier(selected);
        player.getSkyblockDataHandler()
            .get(SkyBlockDataHandler.Data.BANK_DATA, DatapointBankData.class)
            .setValue(data);
        player.sendMessage("§aUpgraded your bank account to " + selected.getDisplayName() + "§a!");
        new GUIBankUpgrades().open(player);
    }

    private void addInterestTranches(List<String> lore, BankAccountTier tier) {
        lore.add(" §eFirst §610M §ecoins yields §b2% §einterest.");
        lore.add(" §eFrom §610M §eto §6" + (tier == BankAccountTier.STARTER ? "15M" : "20M")
            + " §ecoins yields §b1% §einterest.");
        if (tier.ordinal() >= BankAccountTier.DELUXE.ordinal())
            lore.add(" §eFrom §620M §eto §630M §ecoins yields §b0.5% §einterest.");
        if (tier.ordinal() >= BankAccountTier.SUPER_DELUXE.ordinal())
            lore.add(" §eFrom §630M §eto §650M §ecoins yields §b0.2% §einterest.");
        if (tier.ordinal() >= BankAccountTier.PREMIER.ordinal())
            lore.add(" §eFrom §650M §eto §6160M §ecoins yields §b0.1% §einterest.");
        if (tier.ordinal() >= BankAccountTier.LUXURIOUS.ordinal())
            lore.add(" §eFrom §6160M §eto §65.2B §ecoins yields §b0.01% §einterest.");
        if (tier.ordinal() >= BankAccountTier.PALATIAL.ordinal())
            lore.add(" §eFrom §65.2B §eto §655.2B §ecoins yields §b0.001% §einterest.");
    }

    private Material tierMaterial(BankAccountTier tier) {
        return switch (tier) {
            case STARTER -> Material.WHEAT_SEEDS;
            case GOLD -> Material.GOLD_NUGGET;
            case DELUXE -> Material.GOLD_INGOT;
            case SUPER_DELUXE -> Material.GOLDEN_CHESTPLATE;
            case PREMIER -> Material.GOLDEN_HORSE_ARMOR;
            default -> throw new IllegalArgumentException("Tier uses a custom head: " + tier);
        };
    }

    private String tierColor(BankAccountTier tier) {
        return switch (tier) {
            case STARTER -> "§a";
            case GOLD -> "§6";
            case DELUXE -> "§d";
            case SUPER_DELUXE -> "§5";
            case PREMIER -> "§c";
            case LUXURIOUS -> "§3";
            case PALATIAL -> "§4";
        };
    }

    private double interestBalance(BankAccountTier tier) {
        return switch (tier) {
            case STARTER -> 15_000_000D;
            case GOLD -> 20_000_000D;
            case DELUXE -> 30_000_000D;
            case SUPER_DELUXE -> 50_000_000D;
            case PREMIER -> 160_000_000D;
            case LUXURIOUS -> 5_160_000_000D;
            case PALATIAL -> 55_160_000_000D;
        };
    }

    private String balanceLabel(BankAccountTier tier) {
        return switch (tier) {
            case STARTER -> "50 Million Coins";
            case GOLD -> "100 Million Coins";
            case DELUXE -> "250 Million Coins";
            case SUPER_DELUXE -> "500 Million Coins";
            case PREMIER -> "1 Billion Coins";
            case LUXURIOUS -> "6 Billion Coins";
            case PALATIAL -> "60 Billion Coins";
        };
    }

    private String compactNumber(int number) {
        if (number % 1_000_000 == 0) return number / 1_000_000 + "M";
        if (number % 1_000 == 0) return number / 1_000 + "K";
        return String.valueOf(number);
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }
}
