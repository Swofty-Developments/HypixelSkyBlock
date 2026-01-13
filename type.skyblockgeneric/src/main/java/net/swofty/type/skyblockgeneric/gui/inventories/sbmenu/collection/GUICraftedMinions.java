package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.collection;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ClickContext;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointMinionData;
import net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.recipe.GUIMinionRecipes;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.components.MinionComponent;
import net.swofty.type.skyblockgeneric.minion.MinionRegistry;
import net.swofty.type.skyblockgeneric.minion.SkyBlockMinion;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.*;
import java.util.stream.Collectors;

public class GUICraftedMinions extends PaginatedView<SkyBlockItem, GUICraftedMinions.MinionsState> {

    @Override
    public ViewConfiguration<MinionsState> configuration() {
        return new ViewConfiguration<>("Crafted Minions", InventoryType.CHEST_6_ROW);
    }

    public static MinionsState createInitialState() {
        List<SkyBlockItem> minions = Arrays.stream(ItemType.values())
                .map(SkyBlockItem::new)
                .filter(item -> item.hasComponent(MinionComponent.class))
                .collect(Collectors.toList());
        return new MinionsState(minions, 0, "");
    }

    @Override
    protected int[] getPaginatedSlots() {
        return new int[]{
                10, 11, 12, 13, 14, 15, 16,
                19, 20, 21, 22, 23, 24, 25,
                28, 29, 30, 31, 32, 33, 34,
                37, 38, 39, 40, 41, 42, 43
        };
    }

    @Override
    protected int getNextPageSlot() {
        return 53;
    }

    @Override
    protected int getPreviousPageSlot() {
        return 45;
    }

    @Override
    protected ItemStack.Builder renderItem(SkyBlockItem item, int index, HypixelPlayer player) {
        SkyBlockPlayer sbPlayer = (SkyBlockPlayer) player;
        MinionRegistry minionRegistry = item.getAttributeHandler().getMinionType();
        DatapointMinionData.ProfileMinionData playerData = sbPlayer.getSkyblockDataHandler()
                .get(SkyBlockDataHandler.Data.MINION_DATA, DatapointMinionData.class).getValue();

        ArrayList<String> lore = new ArrayList<>();
        List<Integer> tiers = List.of();
        boolean unlocked = false;
        int minionAmount = 0;

        for (Map.Entry<String, List<Integer>> minion : playerData.craftedMinions()) {
            if (Objects.equals(minion.getKey(), minionRegistry.name())) {
                tiers = minion.getValue();
            }
        }
        for (SkyBlockMinion.MinionTier minionTier : minionRegistry.asSkyBlockMinion().getTiers()) {
            if (tiers.contains(minionTier.tier())) {
                lore.add("§a✔ Tier " + StringUtility.getAsRomanNumeral(minionTier.tier()));
                unlocked = true;
                minionAmount++;
            } else {
                lore.add("§c✖ Tier " + StringUtility.getAsRomanNumeral(minionTier.tier()));
            }
        }

        if (unlocked) {
            lore.add("");
            lore.add("§eClick to view recipes!");
            String color = (minionAmount == minionRegistry.asSkyBlockMinion().getTiers().size()) ? "§a" : "§e";
            return ItemStackCreator.getStackHead(color + StringUtility.toNormalCase(minionRegistry.name()) + " Minion",
                    minionRegistry.asSkyBlockMinion().getTiers().getFirst().texture(), 1, lore);
        } else {
            return ItemStackCreator.getStack("§c" + StringUtility.toNormalCase(minionRegistry.name()) + " Minion",
                    Material.GRAY_DYE, 1, "§7You haven't crafted this minion.");
        }
    }

    @Override
    protected void onItemClick(ClickContext<MinionsState> click, ViewContext ctx, SkyBlockItem item, int index) {
        ctx.push(new GUIMinionRecipes(item.getAttributeHandler().getMinionType()));
    }

    @Override
    protected boolean shouldFilterFromSearch(MinionsState state, SkyBlockItem item) {
        return false;
    }

    @Override
    protected void layoutCustom(ViewLayout<MinionsState> layout, MinionsState state, ViewContext ctx) {
        Components.close(layout, 49);
        Components.back(layout, 48, ctx);
    }

    public record MinionsState(List<SkyBlockItem> items, int page, String query) implements PaginatedState<SkyBlockItem> {
        @Override
        public PaginatedState<SkyBlockItem> withPage(int page) {
            return new MinionsState(items, page, query);
        }

        @Override
        public PaginatedState<SkyBlockItem> withItems(List<SkyBlockItem> items) {
            return new MinionsState(items, page, query);
        }
    }
}
