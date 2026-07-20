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
import net.swofty.type.skyblockgeneric.item.components.AttributeShardComponent;
import net.swofty.type.skyblockgeneric.skill.SkillCategories;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GUIHuntingBox extends StatefulPaginatedView<AttributeDefinition, GUIHuntingBox.State> {
    public enum Sort {ID_ASCENDING, ID_DESCENDING, NEW_SHARDS}

    public record State(List<AttributeDefinition> items, int page, Sort sort, String query)
            implements PaginatedState<AttributeDefinition> {
        public PaginatedState<AttributeDefinition> withPage(int page) {
            return new State(items, page, sort, query);
        }

        public PaginatedState<AttributeDefinition> withItems(List<AttributeDefinition> items) {
            return new State(items, page, sort, query);
        }
    }

    public State initialState() {
        return new State(List.of(), 0, Sort.ID_ASCENDING, "");
    }

    public void onOpen(State state, ViewContext ctx) {
        if (state.items().isEmpty()) refresh(ctx, state.sort(), state.query());
    }

    public ViewConfiguration<State> configuration() {
        return ViewConfiguration.withString((state, ctx) -> "(" + (state.page() + 1) + "/" +
                        Math.max(1, (state.items().size() + DEFAULT_SLOTS.length - 1) / DEFAULT_SLOTS.length) + ") Hunting Box",
                InventoryType.CHEST_6_ROW);
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

    protected ItemStack.Builder renderItem(AttributeDefinition definition, int index, HypixelPlayer rawPlayer) {
        SkyBlockPlayer player = (SkyBlockPlayer) rawPlayer;
        return AttributeGUIItems.huntingShard(definition, player.getHuntingData());
    }

    protected void onItemClick(ClickContext<State> click, ViewContext ctx, AttributeDefinition definition, int index) {
        handleShardClick(click.click(), (SkyBlockPlayer) ctx.player(), definition, ctx);
    }

    protected void layoutCustom(ViewLayout<State> layout, State state, ViewContext ctx) {
        SkyBlockPlayer player = (SkyBlockPlayer) ctx.player();
        DatapointHunting.HuntingData data = player.getHuntingData();
        layout.slot(4, ItemStackCreator.getStack("§aHunting Box", Material.CHEST, 1,
                "§7This is where all your Shards are", "§7stored!", "", "§7From here you can Syphon shards,",
                "§7or turn them into items!", "", "§7Shards: §2" + data.getShards().values().stream().mapToInt(Integer::intValue).sum(),
                "", "§eClick to swap to the Attribute Menu!"), (_, c) -> c.push(new GUIAttributeMenu()));
        layout.slot(46, ItemStackCreator.getStack("§aSearch Shards", Material.OAK_SIGN, 1,
                "§7Search for specific shards in your", "§7Hunting Box. You can enter the", "§7Shard Name, ID, Family, or Attribute",
                "§7Category!", "", "§eClick to search!"), (_, c) -> new HypixelSignGUI(c.player())
                .open(new String[]{"Enter query", state.query()}).thenAccept(q -> {
                    if (q != null) refresh(c, state.sort(), q);
                }));
        layout.slot(48, ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1, "§7To Hunting Skill"), (_, c) -> c.navigator().pop());
        layout.slot(50, sortItem(state.sort()), (click, c) -> {
            int direction = click.click() instanceof Click.Right || click.click() instanceof Click.RightShift ? -1 : 1;
            Sort next = Sort.values()[Math.floorMod(state.sort().ordinal() + direction, Sort.values().length)];
            refresh(c, next, state.query());
        });
        layout.slot(51, massSyphonItem(data), (_, c) -> massSyphon((SkyBlockPlayer) c.player(), c));
        layout.slot(52, ItemStackCreator.getStack("§aHunting Toolkit", Material.IRON_HOE, 1,
                "§7Store all of your Hunting Tools in", "§7one convenient place and swap", "§7between them with ease.", "",
                "§8Also accessible via /huntingtoolkit", "", "§eClick to view!"));
        Components.close(layout, 49);
    }

    private void refresh(ViewContext ctx, Sort sort, String query) {
        DatapointHunting.HuntingData data = ((SkyBlockPlayer) ctx.player()).getHuntingData();
        List<AttributeDefinition> result = new ArrayList<>(AttributeRegistry.values().stream()
                .filter(d -> data.shardCount(d.id()) > 0)
                .filter(d -> query.isBlank() || AttributeRegistry.search(query).contains(d)).toList());
        Comparator<AttributeDefinition> id = Comparator.comparingInt((AttributeDefinition d) -> d.rarity().ordinal()).thenComparingInt(AttributeDefinition::numericId);
        if (sort == Sort.ID_DESCENDING) result.sort(id.reversed());
        else if (sort == Sort.NEW_SHARDS)
            result.sort(Comparator.comparingLong((AttributeDefinition d) -> data.getDiscoveredAt().getOrDefault(d.id().toString(), 0L)).reversed());
        else result.sort(id);
        ctx.session(State.class).setState(new State(result, 0, sort, query));
    }

    private ItemStack.Builder sortItem(Sort selected) {
        List<String> lore = new ArrayList<>(List.of(""));
        for (Sort sort : Sort.values())
            lore.add((sort == selected ? "§b▶ " : "§7   ") + switch (sort) {
                case ID_ASCENDING -> "ID (Lowest to Highest)";
                case ID_DESCENDING -> "ID (Highest to Lowest)";
                case NEW_SHARDS -> "New Shards";
            });
        lore.addAll(List.of("", "§bRight-click to go backwards!", "§eClick to switch sort!"));
        return ItemStackCreator.getStack("§aSort", Material.HOPPER, 1, lore);
    }

    private ItemStack.Builder massSyphonItem(DatapointHunting.HuntingData data) {
        List<AttributeDefinition> usable = AttributeRegistry.values().stream().filter(d -> data.shardCount(d.id()) > 0 && data.level(d.id()) < 10).toList();
        int count = usable.stream().mapToInt(d -> data.shardCount(d.id())).sum();
        List<String> lore = new ArrayList<>(List.of("§7You have §c" + count + " Shards §7from §a" + usable.size() + " §7Attributes", "§7that are not maxed out yet.", "§7Do you want to Syphon them all?", "", "§7Shards to Syphon:"));
        usable.stream().limit(8).forEach(d -> lore.add("§8- " + data.shardCount(d.id()) + "x " + d.rarity().itemRarity().getLegacyColor() + d.shardName()));
        lore.addAll(List.of("", "§eClick to mass Syphon!"));
        return ItemStackCreator.getStack("§6Mass Syphon", Material.GOLDEN_APPLE, 1, lore);
    }

    private void handleShardClick(Click click, SkyBlockPlayer player, AttributeDefinition d, ViewContext ctx) {
        DatapointHunting.HuntingData data = player.getHuntingData();
        if (click instanceof Click.Right || click instanceof Click.RightShift) {
            int amount = click instanceof Click.RightShift ? data.shardCount(d.id()) : 1;
            if (data.removeShards(d.id(), amount)) player.addAndUpdateItem(AttributeShardComponent.create(d, amount));
        } else if (player.getSkills().getCurrentLevel(SkillCategories.HUNTING) >= d.rarity().huntingRequirement()) {
            int amount = click instanceof Click.LeftShift ? data.shardCount(d.id()) : 1;
            int used = data.syphon(d.id(), amount);
            if (used > 0) player.getSkills().increase(player, SkillCategories.HUNTING, used * syphonExperience(d));
        } else
            player.sendMessage("§cYou need Hunting Level " + d.rarity().huntingRequirement() + " to Syphon this Attribute!");
        refresh(ctx, ctx.session(State.class).state().sort(), ctx.session(State.class).state().query());
    }

    private void massSyphon(SkyBlockPlayer player, ViewContext ctx) {
        for (AttributeDefinition d : AttributeRegistry.values())
            if (player.getSkills().getCurrentLevel(SkillCategories.HUNTING) >= d.rarity().huntingRequirement())
                player.getHuntingData().syphon(d.id(), player.getHuntingData().shardCount(d.id()));
        refresh(ctx, ctx.session(State.class).state().sort(), ctx.session(State.class).state().query());
    }

    private double syphonExperience(AttributeDefinition d) {
        return switch (d.rarity()) {
            case COMMON -> 75;
            case UNCOMMON -> 150;
            case RARE -> 300;
            case EPIC -> 500;
            case LEGENDARY -> 1000;
        };
    }
}
