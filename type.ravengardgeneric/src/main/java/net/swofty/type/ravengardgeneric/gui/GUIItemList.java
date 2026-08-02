package net.swofty.type.ravengardgeneric.gui;

import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.ViewNavigator;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.ravengardgeneric.item.RavengardItem;
import net.swofty.type.ravengardgeneric.item.RavengardItemRegistry;
import net.swofty.type.ravengardgeneric.item.RavengardItemType;
import net.swofty.type.ravengardgeneric.user.RavengardPlayer;

import java.util.Comparator;
import java.util.List;

public class GUIItemList extends RavengardView {
    private static final int PAGE_SIZE = 36;
    private static final int SLOT_PREVIOUS = 45;
    private static final int SLOT_NEXT = 53;

    private final String filter;
    private final int page;

    public GUIItemList(String filter, int page) {
        this.filter = filter;
        this.page = page;
    }

    private List<RavengardItemType> matching() {
        return RavengardItemRegistry.all().stream()
                .filter(type -> type.getComponents().stream().noneMatch(component ->
                        component.id().equals("PLACEHOLDER_SLOT")))
                .filter(type -> filter == null
                        || type.getId().toLowerCase().contains(filter.toLowerCase())
                        || (type.getDisplayName() != null
                        && type.getDisplayName().toLowerCase().contains(filter.toLowerCase())))
                .sorted(Comparator.comparing(RavengardItemType::getId))
                .toList();
    }

    @Override
    protected String title() {
        return filter == null ? "Items" : "Items: " + filter;
    }

    @Override
    protected boolean usesChrome() {
        return false;
    }

    @Override
    protected void content(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        List<RavengardItemType> items = matching();
        int pages = Math.max(1, (items.size() + PAGE_SIZE - 1) / PAGE_SIZE);
        int current = Math.min(page, pages - 1);
        int from = current * PAGE_SIZE;
        int to = Math.min(from + PAGE_SIZE, items.size());

        for (int index = from; index < to; index++) {
            RavengardItemType type = items.get(index);
            layout.slot(index - from, RavengardItem.displayBuilder(type), (click, viewContext) -> {
                if (viewContext.player() instanceof RavengardPlayer player) {
                    player.getInventory().addItemStack(RavengardItem.of(type, player));
                    player.sendMessage("§aGave you §f" + type.getId() + "§a.");
                }
            });
        }

        if (current > 0) {
            layout.slot(SLOT_PREVIOUS, arrow("§aPrevious Page", current, pages),
                    (click, viewContext) -> {
                        if (viewContext.player() instanceof RavengardPlayer player) {
                            ViewNavigator.get(player).push(new GUIItemList(filter, current - 1));
                        }
                    });
        }
        if (current < pages - 1) {
            layout.slot(SLOT_NEXT, arrow("§aNext Page", current + 2, pages),
                    (click, viewContext) -> {
                        if (viewContext.player() instanceof RavengardPlayer player) {
                            ViewNavigator.get(player).push(new GUIItemList(filter, current + 1));
                        }
                    });
        }
    }

    private static net.minestom.server.item.ItemStack.Builder arrow(String label, int page, int pages) {
        return net.minestom.server.item.ItemStack.builder(net.minestom.server.item.Material.ARROW)
                .customName(net.kyori.adventure.text.Component.text(label)
                        .decoration(net.kyori.adventure.text.format.TextDecoration.ITALIC, false))
                .lore(net.kyori.adventure.text.Component.text("§ePage " + page + " of " + pages)
                        .decoration(net.kyori.adventure.text.format.TextDecoration.ITALIC, false));
    }
}
