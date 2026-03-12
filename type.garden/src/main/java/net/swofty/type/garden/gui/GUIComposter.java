package net.swofty.type.garden.gui;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.garden.GardenServices;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.StatelessView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.skyblockgeneric.garden.GardenData;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class GUIComposter extends StatelessView {
    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Composter", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        Components.backOrClose(layout, 49, ctx);

        SkyBlockPlayer player = (SkyBlockPlayer) ctx.player();
        GardenGuiSupport.syncComposter(player);
        GardenData.GardenComposterData data = GardenGuiSupport.composter(player);
        int organicMatterCap = GardenServices.composter().getOrganicMatterCapacity(data.getUpgrades().getOrDefault("organic_matter_cap", 0));
        int fuelCap = GardenServices.composter().getFuelCapacity(data.getUpgrades().getOrDefault("fuel_cap", 0));

        String organicMatterBar = GardenGuiSupport.progressBar(data.getOrganicMatter(), organicMatterCap);
        String fuelBar = GardenGuiSupport.progressBar(data.getFuel(), fuelCap);

        layout.slot(10, ItemStackCreator.getStack(
            "§eOrganic Matter",
            Material.YELLOW_STAINED_GLASS_PANE,
            1,
            organicMatterBar
        ));
        layout.slot(16, ItemStackCreator.getStack(
            "§2Fuel",
            Material.LIME_STAINED_GLASS_PANE,
            1,
            fuelBar
        ));

        layout.slot(13, (s, c) -> ItemStackCreator.getStack(
            "§eCollect Compost",
            data.getCompostAvailable() > 0 ? Material.GREEN_TERRACOTTA : Material.RED_TERRACOTTA,
            1,
            "§7Compost Available: §a" + data.getCompostAvailable(),
            "",
            data.getCompostAvailable() > 0 ? "§eClick to collect!" : "§cNo Compost to collect"
        ), (click, c) -> {
            if (data.getCompostAvailable() <= 0) {
                return;
            }
            int available = data.getCompostAvailable();
            data.setCompostAvailable(0);
            player.addAndUpdateItem(ItemType.COMPOST, available);
            player.playSuccessSound();
            c.session(Object.class).refresh();
        });

        layout.slot(22, ItemStackCreator.getStack(
            "§aComposter Upgrades",
            Material.HOPPER,
            1,
            "§7Speed: §e" + data.getUpgrades().getOrDefault("speed", 0),
            "§7Multi Drop: §e" + data.getUpgrades().getOrDefault("multi_drop", 0),
            "§7Fuel Cap: §e" + data.getUpgrades().getOrDefault("fuel_cap", 0),
            "§7Organic Matter Cap: §e" + data.getUpgrades().getOrDefault("organic_matter_cap", 0),
            "§7Cost Reduction: §e" + data.getUpgrades().getOrDefault("cost_reduction", 0)
        ));

        layout.slot(39, ItemStackCreator.getStack(
            "§aInsert Crops from Sacks",
            Material.CAULDRON,
            1,
            "§7Grab as many crops that will fit into",
            "§7the composter from your sacks.",
            "",
            "§eLeft-click to grab from sacks!"
        ), (click, c) -> {
            int moved = insertOrganicMatter(player, true);
            if (moved > 0) {
                player.playSuccessSound();
                c.session(Object.class).refresh();
            } else {
                player.sendMessage("§cNo organic matter items could be inserted from sacks.");
            }
        });

        layout.slot(41, ItemStackCreator.getStack(
            "§aInsert Fuel from Sacks",
            Material.CAULDRON,
            1,
            "§7Grab as much fuel that will fit into",
            "§7the composter from your sacks.",
            "",
            "§eLeft-click to grab from sacks!"
        ), (click, c) -> {
            int moved = insertFuel(player, true);
            if (moved > 0) {
                player.playSuccessSound();
                c.session(Object.class).refresh();
            } else {
                player.sendMessage("§cNo fuel items could be inserted from sacks.");
            }
        });

        layout.slot(48, ItemStackCreator.getStackHead(
            "§aInsert Crops from Inventory",
            "ef835b8941fe319931749b87fe8e84c5d1f4a271b5fbce5e700a60004d881f79",
            1,
            "§7Grab as many crops that will fit into",
            "§7the composter from your inventory.",
            "",
            "§eLeft-click to grab from inventory!"
        ), (click, c) -> {
            int moved = insertOrganicMatter(player, false);
            if (moved > 0) {
                player.playSuccessSound();
                c.session(Object.class).refresh();
            } else {
                player.sendMessage("§cNo organic matter items could be inserted from your inventory.");
            }
        });

        layout.slot(50, ItemStackCreator.getStack(
            "§aInsert Fuel from Inventory",
            Material.GREEN_DYE,
            1,
            "§7Grab as much fuel that will fit into",
            "§7the composter from your inventory.",
            "",
            "§eLeft-click to grab from inventory!"
        ), (click, c) -> {
            int moved = insertFuel(player, false);
            if (moved > 0) {
                player.playSuccessSound();
                c.session(Object.class).refresh();
            } else {
                player.sendMessage("§cNo fuel items could be inserted from your inventory.");
            }
        });

        layout.slot(46, ItemStackCreator.getStack(
            "§eCrop Meter",
            Material.POTATO,
            1,
            organicMatterBar,
            "",
            "§7The composter needs §b4,000 Organic Matter",
            "§7to produce Compost."
        ));

        layout.slot(52, ItemStackCreator.getStackHead(
            "§2Fuel Meter",
            "d5d2750595477ecc13869580b12ffc3b13fc2b3ac3e5035ecfc9aafa036722a2",
            1,
            fuelBar,
            "",
            "§7The composter needs §22,000 Fuel",
            "§7to produce Compost."
        ));
    }

    private int insertOrganicMatter(SkyBlockPlayer player, boolean fromSacks) {
        return insertValues(
            player,
            GardenServices.composter().getOrganicMatterCapacity(GardenGuiSupport.composter(player).getUpgrades().getOrDefault("organic_matter_cap", 0)),
            GardenGuiSupport.composter(player).getOrganicMatter(),
            false,
            fromSacks
        );
    }

    private int insertFuel(SkyBlockPlayer player, boolean fromSacks) {
        return insertValues(
            player,
            GardenServices.composter().getFuelCapacity(GardenGuiSupport.composter(player).getUpgrades().getOrDefault("fuel_cap", 0)),
            GardenGuiSupport.composter(player).getFuel(),
            true,
            fromSacks
        );
    }

    private int insertValues(SkyBlockPlayer player, double capacity, double current, boolean fuel, boolean fromSacks) {
        GardenData.GardenComposterData data = GardenGuiSupport.composter(player);
        double remainingCapacity = capacity - current;
        if (remainingCapacity <= 0D) {
            return 0;
        }

        List<Map.Entry<String, Double>> values = (fuel
            ? GardenServices.composter().getFuelValues()
            : GardenServices.composter().getOrganicMatterValues())
            .entrySet().stream()
            .sorted(Map.Entry.<String, Double>comparingByValue(Comparator.reverseOrder()))
            .toList();

        int insertedStacks = 0;
        double insertedValue = 0D;
        Map<String, Long> tracker = fuel ? data.getInsertedFuel() : data.getInsertedMatter();
        for (Map.Entry<String, Double> entry : values) {
            if (remainingCapacity <= 0D) {
                break;
            }

            ItemType type = ItemType.get(entry.getKey());
            double perItem = entry.getValue();
            if (type == null || perItem <= 0D) {
                continue;
            }

            int available = fromSacks ? player.getSackItems().getAmount(type) : player.getAmountInInventory(type);
            if (available <= 0) {
                continue;
            }

            int insertable = (int) Math.min(available, Math.floor(remainingCapacity / perItem));
            if (insertable <= 0) {
                continue;
            }

            if (fromSacks) {
                player.getSackItems().decrease(type, insertable);
            } else if (player.takeItem(type, insertable) == null) {
                continue;
            }

            insertedStacks += insertable;
            double valueAdded = insertable * perItem;
            insertedValue += valueAdded;
            remainingCapacity -= valueAdded;
            tracker.merge(entry.getKey(), (long) insertable, Long::sum);
        }

        if (fuel) {
            data.setFuel(data.getFuel() + insertedValue);
        } else {
            data.setOrganicMatter(data.getOrganicMatter() + insertedValue);
        }
        return insertedStacks;
    }
}
