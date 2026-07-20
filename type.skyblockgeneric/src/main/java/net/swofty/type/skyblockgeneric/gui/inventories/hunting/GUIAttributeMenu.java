package net.swofty.type.skyblockgeneric.gui.inventories.hunting;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.HypixelSignGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.StatefulPaginatedView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ClickContext;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointHunting;
import net.swofty.type.skyblockgeneric.hunting.AttributeDefinition;
import net.swofty.type.skyblockgeneric.hunting.AttributeRegistry;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GUIAttributeMenu extends StatefulPaginatedView<AttributeDefinition, GUIAttributeMenu.State> {
    public enum SkillFilter {ALL, COMBAT, FISHING, FARMING, FORAGING, MINING, TAMING, ENCHANTING, HUNTING, GLOBAL}

    public enum Sort {ID_ASCENDING, ID_DESCENDING, HIGHEST_LEVEL, LOWEST_LEVEL}

    public record State(List<AttributeDefinition> items, int page, SkillFilter filter, Sort sort, boolean advanced,
                        String query)
            implements PaginatedState<AttributeDefinition> {
        public PaginatedState<AttributeDefinition> withPage(int page) {
            return new State(items, page, filter, sort, advanced, query);
        }

        public PaginatedState<AttributeDefinition> withItems(List<AttributeDefinition> items) {
            return new State(items, page, filter, sort, advanced, query);
        }
    }

    public State initialState() {
        return new State(List.of(), 0, SkillFilter.ALL, Sort.ID_ASCENDING, false, "");
    }

    public void onOpen(State state, ViewContext ctx) {
        if (state.items().isEmpty()) refresh(ctx, state.filter(), state.sort(), state.advanced(), state.query());
    }

    public ViewConfiguration<State> configuration() {
        return ViewConfiguration.withString((s, c) -> "(" + (s.page() + 1) + "/" + Math.max(1, (s.items().size() + DEFAULT_SLOTS.length - 1) / DEFAULT_SLOTS.length) + ") Attribute Menu", InventoryType.CHEST_6_ROW);
    }

    protected int[] getPaginatedSlots() {
        return DEFAULT_SLOTS;
    }

    protected int getPreviousPageSlot() {
        return 45;
    }

    protected int getNextPageSlot() {
        return 53;
    }

    protected boolean shouldFilterFromSearch(State state, AttributeDefinition item) {
        return false;
    }

    protected ItemStack.Builder renderItem(AttributeDefinition item, int index, HypixelPlayer player) {
        return AttributeGUIItems.attribute(item, ((SkyBlockPlayer) player).getHuntingData(), true);
    }

    protected void onItemClick(ClickContext<State> click, ViewContext ctx, AttributeDefinition item, int index) {
        if (click.click() instanceof Click.Right || click.click() instanceof Click.RightShift) {
            ((SkyBlockPlayer) ctx.player()).getHuntingData().toggle(item.id());
            ctx.session(State.class).setState(ctx.session(State.class).state());
        } else ctx.push(new GUIAttributeDetails(item));
    }

    protected void layoutCustom(ViewLayout<State> layout, State state, ViewContext ctx) {
        DatapointHunting.HuntingData data = ((SkyBlockPlayer) ctx.player()).getHuntingData();
        int totalXp = AttributeRegistry.values().stream().mapToInt(d -> data.level(d.id())).sum();
        long maxed = AttributeRegistry.values().stream().filter(d -> data.level(d.id()) >= 10).count();
        layout.slot(4, ItemStackCreator.getStack("§3Attribute Menu", Material.LEAD, 1,
                "§7Syphon Shards to unlock and level", "§7up your §aAttributes§7!", "§7Each Attribute grants its own unique",
                "§dpower§7, active at all times, no matter", "§7where you are.", "§aAttributes §7can reach up to level §b10§7,",
                "§7with shard costs scaling based on", "§7their §drarity§7.", "", "§7Attributes Found: §e" + data.uniqueAttributes() + "§6/§e" + AttributeRegistry.values().size(),
                "§7Attributes Maxed: §e" + maxed + "§6/§e" + AttributeRegistry.values().size(), "§7Total XP: §b" + totalXp + "§3/§b" + AttributeRegistry.values().size() * 10,
                "", "§eClick to swap to the Hunting Box!"), (_, c) -> c.push(new GUIHuntingBox()));
        layout.slot(46, ItemStackCreator.getStack("§aSearch Attributes", Material.OAK_SIGN, 1,
                "§7Search for specific attributes in", "§7your Attribute Menu. You can enter", "§7the Attribute Name, ID, Family, or",
                "§7Attribute Category!", "", "§eClick to search!"), (_, c) -> new HypixelSignGUI(c.player()).open(new String[]{"Enter query", state.query()})
                .thenAccept(q -> {
                    if (q != null) refresh(c, state.filter(), state.sort(), state.advanced(), q);
                }));
        layout.slot(47, cycleItem("§aFilter by SkyBlock Skill", Material.ENDER_EYE, SkillFilter.values(), state.filter()), (click, c) -> {
            int direction = isRight(click.click()) ? -1 : 1;
            SkillFilter next = SkillFilter.values()[Math.floorMod(state.filter().ordinal() + direction, SkillFilter.values().length)];
            refresh(c, next, state.sort(), state.advanced(), state.query());
        });
        layout.slot(48, ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1, "§7To Hunting Box"), (_, c) -> c.navigator().pop());
        layout.slot(50, cycleItem("§aSort", Material.HOPPER, Sort.values(), state.sort()), (click, c) -> {
            int direction = isRight(click.click()) ? -1 : 1;
            Sort next = Sort.values()[Math.floorMod(state.sort().ordinal() + direction, Sort.values().length)];
            refresh(c, state.filter(), next, state.advanced(), state.query());
        });
        layout.slot(51, ItemStackCreator.getStack("§aAttribute Transfer", Material.ANVIL, 1, "§7Do you still have old items, with", "§7greyed out Attributes on them?", "", "§7You can use this Anvil to transfer", "§7those Attributes to the new system!", "", "§eClick to view!"));
        layout.slot(52, ItemStackCreator.getStack("§6Advanced Mode", Material.COMPARATOR, 1, "§7Advanced Mode lets you see every", "§7attribute, and find out how to obtain", "§7them.", "", "§7Toggled: " + (state.advanced() ? "§aON" : "§cOFF"), "", "§eClick to toggle!"),
                (_, c) -> refresh(c, state.filter(), state.sort(), !state.advanced(), state.query()));
        Components.close(layout, 49);
    }

    private void refresh(ViewContext ctx, SkillFilter filter, Sort sort, boolean advanced, String query) {
        DatapointHunting.HuntingData data = ((SkyBlockPlayer) ctx.player()).getHuntingData();
        List<AttributeDefinition> result = new ArrayList<>(AttributeRegistry.values().stream()
                .filter(d -> advanced || data.level(d.id()) > 0)
                .filter(d -> filter == SkillFilter.ALL || d.skill().name().equals(filter.name()))
                .filter(d -> query.isBlank() || AttributeRegistry.search(query).contains(d)).toList());
        Comparator<AttributeDefinition> id = Comparator.comparingInt((AttributeDefinition d) -> d.rarity().ordinal()).thenComparingInt(AttributeDefinition::numericId);
        switch (sort) {
            case ID_ASCENDING -> result.sort(id);
            case ID_DESCENDING -> result.sort(id.reversed());
            case HIGHEST_LEVEL ->
                    result.sort(Comparator.comparingInt((AttributeDefinition d) -> data.level(d.id())).reversed().thenComparing(id));
            case LOWEST_LEVEL ->
                    result.sort(Comparator.comparingInt((AttributeDefinition d) -> data.level(d.id())).thenComparing(id));
        }
        ctx.session(State.class).setState(new State(result, 0, filter, sort, advanced, query));
    }

    private <E extends Enum<E>> ItemStack.Builder cycleItem(String name, Material material, E[] values, E selected) {
        List<String> lore = new ArrayList<>(List.of(""));
        for (int i = 0; i < values.length; i++) {
            if (i >= 8) {
                lore.add("  §eAnd " + (values.length - i) + " more options");
                break;
            }
            lore.add((values[i] == selected ? "§b▶ " : "§7   ") + values[i].name().replace('_', ' '));
        }
        lore.addAll(List.of("", "§bRight-click to go backwards!", "§eClick to switch!"));
        return ItemStackCreator.getStack(name, material, 1, lore);
    }

    private boolean isRight(Click click) {
        return click instanceof Click.Right || click instanceof Click.RightShift;
    }
}
