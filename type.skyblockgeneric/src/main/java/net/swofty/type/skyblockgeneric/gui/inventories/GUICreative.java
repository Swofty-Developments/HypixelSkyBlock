package net.swofty.type.skyblockgeneric.gui.inventories;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.generic.gui.HypixelSignGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.TranslatableItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.Layouts;
import net.swofty.type.generic.gui.v2.PaginatedView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ClickContext;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.components.TrackedUniqueComponent;
import net.swofty.type.skyblockgeneric.item.updater.PlayerItemUpdater;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class GUICreative extends PaginatedView<SkyBlockItem, GUICreative.CreativeState> {

    @Override
    public ViewConfiguration<CreativeState> configuration() {
        return ViewConfiguration.withString(
                (state, ctx) -> {
                    int totalPages = Math.max(1, (int) Math.ceil((double) getFilteredItems(state).size() / DEFAULT_SLOTS.length));
                    Locale l = ctx.player().getLocale();
                    return I18n.string("gui_misc.creative.title", l, Map.of(
                            "page", String.valueOf(state.page() + 1),
                            "total_pages", String.valueOf(totalPages)
                    ));
                },
                InventoryType.CHEST_6_ROW
        );
    }

    public static CreativeState createInitialState() {
        return createInitialState("", 0);
    }

    public static CreativeState createInitialState(String query, int pageZeroBased) {
        List<SkyBlockItem> items = new ArrayList<>(Arrays.stream(ItemType.values()).map(SkyBlockItem::new).toList());
        List<SkyBlockItem> vanilla = new ArrayList<>(Material.values().stream().map(SkyBlockItem::new).toList());
        vanilla.removeIf((element) -> ItemType.isVanillaReplaced(element.getAttributeHandler().getTypeAsString()));
        items.addAll(vanilla);

        return new CreativeState(items, Math.max(0, pageZeroBased), query == null ? "" : query);
    }

    @Override
    protected int[] getPaginatedSlots() {
        return DEFAULT_SLOTS;
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
    protected void layoutBackground(ViewLayout<CreativeState> layout, CreativeState state, ViewContext ctx) {
        Components.fill(layout);
        layout.filler(Layouts.border(0, 53), ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, ""));
    }

    @Override
    protected void layoutCustom(ViewLayout<CreativeState> layout, CreativeState state, ViewContext ctx) {
        layout.slot(49, (s, c) -> TranslatableItemStackCreator.getStack("gui_misc.creative.close_button", Material.BARRIER, 1),
                (_, c) -> c.player().closeInventory());

        layout.slot(50, (s, c) -> TranslatableItemStackCreator.getStack("gui_misc.creative.search_button", Material.OAK_SIGN, 1,
                "gui_misc.creative.search_button.lore"), (_, c) -> {
            new HypixelSignGUI(c.player()).open(new String[]{"Enter query", ""}).thenAccept(line -> {
                if (line == null) {
                    return;
                }

                c.replace(new GUICreative(), GUICreative.createInitialState(line, 0));
            });
        });
    }

    @Override
    protected ItemStack.Builder renderItem(SkyBlockItem skyBlockItem, int index, HypixelPlayer player) {
        SkyBlockPlayer sbPlayer = (SkyBlockPlayer) player;

        SkyBlockItem displayItem = skyBlockItem.getAttributeHandler().getPotentialType() != null
                ? new SkyBlockItem(skyBlockItem.getAttributeHandler().getPotentialType())
                : new SkyBlockItem(skyBlockItem.getMaterial());

        ItemStack.Builder itemStack = PlayerItemUpdater.playerUpdate(sbPlayer, displayItem.getItemStack());

        boolean stackable = !(displayItem.hasComponent(TrackedUniqueComponent.class));

        Locale l = player.getLocale();
        ArrayList<String> lore = new ArrayList<>(displayItem.getLore());
        lore.add(" ");
        lore.add(I18n.string("gui_misc.creative.click_to_retrieve", l));
        if (stackable) lore.add(I18n.string("gui_misc.creative.right_click_stack", l));

        return ItemStackCreator.updateLore(itemStack, lore);
    }

    @Override
    protected void onItemClick(ClickContext<CreativeState> click, ViewContext ctx, SkyBlockItem skyBlockItem, int index) {
        SkyBlockPlayer player = (SkyBlockPlayer) ctx.player();

        SkyBlockItem toGive = skyBlockItem.getAttributeHandler().getPotentialType() != null
                ? new SkyBlockItem(skyBlockItem.getAttributeHandler().getPotentialType())
                : new SkyBlockItem(skyBlockItem.getMaterial());

        boolean stackable = !(toGive.hasComponent(TrackedUniqueComponent.class));

        player.playSound(Sound.sound(Key.key("block.note_block.pling"), Sound.Source.PLAYER, 1.0f, 2.0f));

        Locale l = player.getLocale();
        if (click.click() instanceof Click.Right && stackable) {
            toGive.setAmount(64);
            player.addAndUpdateItem(toGive);
            player.playSound(Sound.sound(Key.key("block.note_block.pling"), Sound.Source.PLAYER, 1.0f, 2.0f));
            player.sendMessage(I18n.t("gui_misc.creative.given_stack", Map.of("item_name", toGive.getDisplayName())));
        } else {
            toGive.setAmount(1);
            player.addAndUpdateItem(toGive);
            player.playSound(Sound.sound(Key.key("block.note_block.pling"), Sound.Source.PLAYER, 1.0f, 2.0f));
            player.sendMessage(I18n.t("gui_misc.creative.given_single", Map.of("item_name", toGive.getDisplayName())));
        }
    }

    @Override
    protected boolean shouldFilterFromSearch(CreativeState state, SkyBlockItem item) {
        if (state.query().isEmpty()) return false;
        return !item.getAttributeHandler().getTypeAsString().toLowerCase().contains(state.query().replaceAll(" ", "_").toLowerCase());
    }

    public record CreativeState(List<SkyBlockItem> items, int page, String query) implements PaginatedState<SkyBlockItem> {
        @Override
        public PaginatedState<SkyBlockItem> withPage(int page) {
            return new CreativeState(items, page, query);
        }

        @Override
        public PaginatedState<SkyBlockItem> withItems(List<SkyBlockItem> items) {
            return new CreativeState(items, page, query);
        }
    }
}
