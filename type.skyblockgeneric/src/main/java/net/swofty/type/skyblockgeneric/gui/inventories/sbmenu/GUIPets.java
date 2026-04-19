package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu;

import net.kyori.adventure.text.Component;
import net.minestom.server.component.DataComponents;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.skyblock.item.Rarity;
import net.swofty.commons.skyblock.item.attribute.attributes.ItemAttributePetData;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.TranslatableItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.PaginatedView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ClickContext;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.components.PetComponent;
import net.swofty.type.skyblockgeneric.item.updater.NonPlayerItemUpdater;
import net.swofty.type.skyblockgeneric.skill.SkillCategories;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GUIPets extends PaginatedView<SkyBlockItem, GUIPets.PetsState> {
    private static final int[] PAGINATED_SLOTS = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43
    };

    @Override
    public ViewConfiguration<PetsState> configuration() {
        return ViewConfiguration.withString(
            (state, ctx) -> I18n.string("gui_sbmenu.pets.title", ctx.player().getLocale(), Component.text(String.valueOf(state.page() + 1)), Component.text(String.valueOf(Math.max(1, (int) Math.ceil((double) getFilteredItems(state).size() / PAGINATED_SLOTS.length))))),
                InventoryType.CHEST_6_ROW
        );
    }

    @Override
    protected int[] getPaginatedSlots() {
        return PAGINATED_SLOTS;
    }

    @Override
    protected List<SkyBlockItem> getFilteredItems(PetsState state) {
        List<SkyBlockItem> pets = new ArrayList<>(state.items());
        pets = pets.stream().filter(item -> !shouldFilterFromSearch(state, item)).toList();

        pets = new ArrayList<>(pets);
        switch (state.sortType()) {
            case LEVEL:
                pets.sort((pet1, pet2) -> {
                    ItemAttributePetData.PetData data1 = pet1.getAttributeHandler().getPetData();
                    Rarity rarity1 = pet1.getAttributeHandler().getRarity();
                    ItemAttributePetData.PetData data2 = pet2.getAttributeHandler().getPetData();
                    Rarity rarity2 = pet2.getAttributeHandler().getRarity();
                    int level1 = data1.getAsLevel(rarity1);
                    int level2 = data2.getAsLevel(rarity2);
                    return Integer.compare(level2, level1);
                });
                break;
            case RARITY:
                pets.sort((pet1, pet2) -> {
                    int rarity1 = pet1.getAttributeHandler().getRarity().ordinal();
                    int rarity2 = pet2.getAttributeHandler().getRarity().ordinal();
                    return Integer.compare(rarity2, rarity1);
                });
                break;
            case ALPHABETICAL:
                pets.sort((pet1, pet2) -> {
                    String name1 = pet1.getComponent(PetComponent.class).getPetName();
                    String name2 = pet2.getComponent(PetComponent.class).getPetName();
                    return name1.compareTo(name2);
                });
                break;
            case SKILL:
                pets.sort((pet1, pet2) -> {
                    SkillCategories skill1 = pet1.getComponent(PetComponent.class).getSkillCategory();
                    SkillCategories skill2 = pet2.getComponent(PetComponent.class).getSkillCategory();
                    return Integer.compare(skill2.ordinal(), skill1.ordinal());
                });
                break;
        }

        return pets;
    }

    @Override
    protected ItemStack.Builder renderItem(SkyBlockItem item, int index, HypixelPlayer player) {
        SkyBlockPlayer skyBlockPlayer = (SkyBlockPlayer) player;
        boolean isPetEnabled = skyBlockPlayer.getPetData().getEnabledPet() == item;

        ItemStack.Builder itemStack = new NonPlayerItemUpdater(item).getUpdatedItem();
        List<String> lore = new ArrayList<>(itemStack.build().get(DataComponents.LORE).stream().map(StringUtility::getTextFromComponent).toList());
        Locale l = player.getLocale();
        lore.add(" ");
        if (isPetEnabled) {
            ItemStackCreator.enchant(itemStack);
            lore.add(I18n.string("gui_sbmenu.pets.currently_active", l));
            lore.add(I18n.string("gui_sbmenu.pets.click_to_deselect", l));
        } else {
            lore.add(I18n.string("gui_sbmenu.pets.click_to_summon", l));
        }
        return ItemStackCreator.updateLore(itemStack, lore);
    }

    @Override
    protected void onItemClick(ClickContext<PetsState> click, ViewContext ctx, SkyBlockItem item, int index) {
        SkyBlockPlayer player = (SkyBlockPlayer) ctx.player();
        Locale l = player.getLocale();
        PetsState state = click.state();
        boolean selected = player.getPetData().getEnabledPet() == item;

        if (selected) {
            player.getPetData().deselectCurrent();
            player.getPetData().updatePetEntityImpl(player);
            ctx.session(PetsState.class).update(s -> (PetsState) s.withItems(getPetsFromPlayer(player)));
            player.sendMessage(I18n.string("gui_sbmenu.pets.msg.deselected", l, Component.text(item.getDisplayName())));
            return;
        }

        if (state.convertToItem()) {
            player.addAndUpdateItem(item);
            player.getPetData().removePet(item.getAttributeHandler().getPotentialType());
            ctx.session(PetsState.class).update(s -> (PetsState) s.withItems(getPetsFromPlayer(player)));
            player.sendMessage(I18n.string("gui_sbmenu.pets.msg.picked_up", l));
            return;
        }

        player.getPetData().setEnabled(item.getAttributeHandler().getPotentialType(), true);
        player.getPetData().updatePetEntityImpl(player);
        player.sendMessage(I18n.string("gui_sbmenu.pets.msg.selected", l, Component.text(item.getDisplayName())));
        ctx.session(PetsState.class).update(s -> (PetsState) s.withItems(getPetsFromPlayer(player)));
    }

    @Override
    protected boolean shouldFilterFromSearch(PetsState state, SkyBlockItem item) {
        return !item.getDisplayName().toLowerCase().contains(state.query.toLowerCase());
    }

    @Override
    protected void layoutCustom(ViewLayout<PetsState> layout, PetsState state, ViewContext ctx) {
        Components.close(layout, 49);
        Components.back(layout, 48, ctx);

        layout.slot(4, (s, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            String selectedPet = player.getPetData().getEnabledPet() == null ? "§cNone" : player.getPetData().getEnabledPet().getDisplayName();
            return TranslatableItemStackCreator.getStack("gui_sbmenu.pets.info", Material.BONE, 1,
                "gui_sbmenu.pets.info.lore", Component.text(selectedPet));
        });

        layout.slot(47, (s, c) -> {
            String status = s.convertToItem() ? "§aEnabled" : "§cDisabled";
            ItemStack.Builder itemStack = TranslatableItemStackCreator.getStack("gui_sbmenu.pets.convert_to_item", Material.DIAMOND, 1,
                "gui_sbmenu.pets.convert_to_item.lore", Component.text(status));
            if (s.convertToItem())
                ItemStackCreator.enchant(itemStack);
            return itemStack;
        }, (click, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            Locale l = player.getLocale();
            String status = !click.state().convertToItem() ? "§aENABLED" : "§cDISABLED";
            player.sendMessage(I18n.string("gui_sbmenu.pets.msg.conversion_toggle", l, Component.text(status)));
            c.session(PetsState.class).update(s -> s.withConvertToItem(!s.convertToItem()));
        });

        layout.slot(51, (s, c) -> {
            Locale l = c.player().getLocale();
            List<String> lore = new ArrayList<>();
            lore.add(" ");

            for (SortType randomSortType : SortType.values()) {
                lore.add(randomSortType == s.sortType() ?
                        "§e> " + StringUtility.toNormalCase(randomSortType.name())
                        : "§7> " + StringUtility.toNormalCase(randomSortType.name()));
            }

            lore.add(" ");
            lore.add(I18n.string("gui_sbmenu.pets.sort.right_click", l));
            lore.add(I18n.string("gui_sbmenu.pets.sort.click", l));

            return TranslatableItemStackCreator.getStack("gui_sbmenu.pets.sort", Material.HOPPER, 1, lore);
        }, (click, c) -> {
            boolean isRightClick = click.click() instanceof Click.Right;

            int ordinal = click.state().sortType().ordinal();
            if (isRightClick) {
                ordinal--;
                if (ordinal < 0) ordinal = SortType.values().length - 1;
            } else {
                ordinal++;
                if (ordinal >= SortType.values().length) ordinal = 0;
            }

            SortType newSort = SortType.values()[ordinal];
            c.session(PetsState.class).update(s -> s.withSortType(newSort));
        });
    }

    @Override
    protected int getPreviousPageSlot() {
        return 45;
    }

    @Override
    protected int getNextPageSlot() {
        return 53;
    }

    private static List<SkyBlockItem> getPetsFromPlayer(SkyBlockPlayer player) {
        return new ArrayList<>(player.getPetData().getPetsMap().keySet().stream().toList());
    }

    public static PetsState createInitialState(SkyBlockPlayer player) {
        return new PetsState(getPetsFromPlayer(player), 0, "", SortType.LEVEL, false);
    }

    public record PetsState(
            List<SkyBlockItem> items,
            int page,
            String query,
            SortType sortType,
            boolean convertToItem
    ) implements PaginatedState<SkyBlockItem> {
        @Override
        public PaginatedState<SkyBlockItem> withPage(int page) {
            return new PetsState(items, page, query, sortType, convertToItem);
        }

        @Override
        public PaginatedState<SkyBlockItem> withItems(List<SkyBlockItem> items) {
            return new PetsState(items, page, query, sortType, convertToItem);
        }

        public PetsState withSortType(SortType sortType) {
            return new PetsState(items, page, query, sortType, convertToItem);
        }

        public PetsState withConvertToItem(boolean convertToItem) {
            return new PetsState(items, page, query, sortType, convertToItem);
        }
    }

    public enum SortType {
        LEVEL,
        RARITY,
        ALPHABETICAL,
        SKILL
    }
}
