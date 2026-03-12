package net.swofty.type.garden.gui;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.type.garden.GardenServices;
import net.swofty.type.garden.config.GardenConfigRegistry;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.PaginatedView;
import net.swofty.type.generic.gui.v2.StatefulPaginatedView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ClickContext;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.garden.GardenData;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class GUIVisitorLogbook extends StatefulPaginatedView<Map<String, Object>, GUIVisitorLogbook.State> {
    @Override
    public ViewConfiguration<State> configuration() {
        return new ViewConfiguration<>("Visitor's Logbook", InventoryType.CHEST_6_ROW);
    }

    @Override
    public State initialState() {
        List<Map<String, Object>> entries = new ArrayList<>(GardenServices.visitors().getVisitors());
        entries.sort(Comparator.comparing(entry -> GardenConfigRegistry.getString(entry, "display_name", "")));
        return new State(entries, 0);
    }

    @Override
    protected int[] getPaginatedSlots() {
        return DEFAULT_SLOTS;
    }

    @Override
    protected ItemStack.Builder renderItem(Map<String, Object> visitor, int index, HypixelPlayer hypixelPlayer) {
        SkyBlockPlayer player = (SkyBlockPlayer) hypixelPlayer;
        String id = GardenConfigRegistry.getString(visitor, "id", "");
        String displayName = GardenConfigRegistry.getString(visitor, "display_name", StringUtility.toNormalCase(id));
        String rarity = GardenConfigRegistry.getString(visitor, "rarity", "UNCOMMON");
        int visitCount = GardenGuiSupport.visitors(player).getVisitCounts().getOrDefault(id, 0);
        int servedCount = GardenGuiSupport.visitors(player).getServedCounts().getOrDefault(id, 0);
        boolean unlocked = isUnlockedForDisplay(player, visitor);
        boolean active = isCurrentlyPresent(player, id);

        List<String> lore = new ArrayList<>();
        lore.add(GardenGuiSupport.colorForRarity(rarity) + "§l" + rarity);
        lore.add("");

        if (!unlocked && visitCount == 0) {
            lore.add("§7Requirement:");
            for (Object requirement : GardenConfigRegistry.getList(visitor, "requirements")) {
                lore.add("§c✖ " + requirement);
            }
            if (GardenConfigRegistry.getList(visitor, "requirements").isEmpty()) {
                lore.add("§c✖ Requirement data unavailable");
            }
            return ItemStackCreator.getStack("§7" + displayName, Material.GRAY_DYE, 1, lore);
        }

        lore.add("§7Times Visited: §a" + visitCount);
        lore.add("§7Offers Accepted: §a" + servedCount);
        lore.add("§7Currently: " + (active ? "§aAt your Desk" : "§7Not visiting"));
        lore.add("");

        List<Object> wantedItems = GardenConfigRegistry.getList(visitor, "wanted_items");
        if (!wantedItems.isEmpty()) {
            lore.add("§7Wanted Items:");
            wantedItems.stream().limit(3).forEach(item -> lore.add(" §8- §7" + StringUtility.toNormalCase(String.valueOf(item))));
            if (wantedItems.size() > 3) {
                lore.add(" §8- §7+" + (wantedItems.size() - 3) + " more");
            }
            lore.add("");
        }

        lore.add(active ? "§eClick to view offer!" : "§7This visitor is not currently on your Garden.");
        return GardenGuiSupport.itemWithLore(
            getIconItemId(visitor),
            GardenGuiSupport.colorForRarity(rarity) + displayName,
            lore
        );
    }

    @Override
    protected void onItemClick(ClickContext<State> click, ViewContext ctx, Map<String, Object> visitor, int index) {
        SkyBlockPlayer player = (SkyBlockPlayer) ctx.player();
        String id = GardenConfigRegistry.getString(visitor, "id", "");
        if (isCurrentlyPresent(player, id)) {
            ctx.push(new GardenVisitorOfferView(id));
        }
    }

    @Override
    protected boolean shouldFilterFromSearch(State state, Map<String, Object> item) {
        return false;
    }

    @Override
    protected void layoutCustom(ViewLayout<State> layout, State state, ViewContext ctx) {
        SkyBlockPlayer player = (SkyBlockPlayer) ctx.player();
        GardenData.GardenVisitorsData visitors = GardenGuiSupport.visitors(player);
        long now = System.currentTimeMillis();
        String nextVisitor = visitors.getActiveVisitors().size() + visitors.getQueuedVisitors().size()
            >= GardenServices.visitors().getMaxVisibleVisitors() + GardenServices.visitors().getMaxQueuedVisitors()
            && now >= visitors.getNextArrivalAt()
            ? "§c§lQueue Full!"
            : "§a" + StringUtility.formatTimeLeft(Math.max(0L, visitors.getNextArrivalAt() - now));

        layout.slot(4, ItemStackCreator.getStackHead(
            "§aLogbook",
            "8d34f38c1bb106e11908ad3cc90162c18b863d678265c84a84a358903f8f7a1c",
            1,
            "§7Various visitors will stop by your",
            "§7Garden and wait by your Desk with",
            "§7requests for crops and supplies.",
            "",
            "§7Next Visitor: " + nextVisitor,
            "§7Arrival cadence: §a" + GardenServices.visitors().getArrivalMinutes(GardenGuiSupport.core(player).getServedUniqueVisitors().size()) + " minutes",
            "§7Known entries: §e" + visitors.getLogbookEntries().size() + "§7/§a" + GardenServices.visitors().getExpectedUniqueVisitors(),
            "§7Active / queued: §e" + visitors.getActiveVisitors().size() + "§7/§e" + visitors.getQueuedVisitors().size()
        ));

        if (!Components.back(layout, 48, ctx)) {
            layout.slot(48, FILLER);
        }
        Components.close(layout, 49);
        layout.slot(50, ItemStackCreator.getStack(
            "§aVisitor Milestones",
            Material.GOLD_BLOCK,
            1,
            "§7Review your unique visitor and",
            "§7offer-accept milestone progress.",
            "",
            "§eClick to view!"
        ), (click, viewCtx) -> viewCtx.push(new GUIVisitorMilestones()));
    }

    @Override
    protected int getPreviousPageSlot() {
        return 45;
    }

    @Override
    protected int getNextPageSlot() {
        return 53;
    }

    private boolean isUnlockedForDisplay(SkyBlockPlayer player, Map<String, Object> visitor) {
        if (GardenGuiSupport.core(player).getLevel() < GardenConfigRegistry.getInt(visitor, "garden_level", 1)) {
            return false;
        }
        for (Object rawRequirement : GardenConfigRegistry.getList(visitor, "requirements")) {
            String requirement = String.valueOf(rawRequirement);
            if (requirement.regionMatches(true, 0, "Talk to", 0, "Talk to".length())) {
                String flag = requirement
                    .replaceFirst("(?i)^Talk to\\s+", "")
                    .replaceFirst("(?i)^the\\s+", "")
                    .replaceFirst("(?i)^this\\s+", "")
                    .toLowerCase()
                    .replaceAll("[^a-z0-9]+", "_")
                    .replaceAll("^_+|_+$", "");
                if (GardenGuiSupport.personal(player).getSpokenNpcFlags().contains(flag)) {
                    continue;
                }
                if (List.of("sam", "anita", "jacob", "jeff", "pesthunter_phillip", "carpenter", "shifty", "desk").contains(flag)) {
                    return false;
                }
            } else if (requirement.regionMatches(true, 0, "Farming ", 0, "Farming ".length())
                || requirement.regionMatches(true, 0, "Fishing ", 0, "Fishing ".length())) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }

    private boolean isCurrentlyPresent(SkyBlockPlayer player, String visitorId) {
        return GardenGuiSupport.visitors(player).getActiveVisitors().stream()
            .anyMatch(visitor -> visitor.getVisitorId().equalsIgnoreCase(visitorId))
            || GardenGuiSupport.visitors(player).getQueuedVisitors().stream()
            .anyMatch(visitor -> visitor.getVisitorId().equalsIgnoreCase(visitorId));
    }

    private String getIconItemId(Map<String, Object> visitor) {
        for (Object wanted : GardenConfigRegistry.getList(visitor, "wanted_items")) {
            String item = String.valueOf(wanted).toUpperCase();
            if (!item.startsWith("ANY")) {
                return item;
            }
        }
        return "BOOK";
    }

    public record State(List<Map<String, Object>> items,
                        int page) implements PaginatedView.PaginatedState<Map<String, Object>> {
        @Override
        public PaginatedView.PaginatedState<Map<String, Object>> withPage(int page) {
            return new State(items, page);
        }

        @Override
        public PaginatedView.PaginatedState<Map<String, Object>> withItems(List<Map<String, Object>> items) {
            return new State(items, page);
        }
    }
}
