package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu;

import net.minestom.server.component.DataComponents;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.skyblock.item.Rarity;
import net.swofty.commons.skyblock.item.attribute.attributes.ItemAttributePetData;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ClickContext;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.components.PetComponent;
import net.swofty.type.skyblockgeneric.item.updater.NonPlayerItemUpdater;
import net.swofty.type.skyblockgeneric.skill.SkillCategories;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;

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
                (state, ctx) -> "(" + (state.page() + 1) + "/" + Math.max(1, (int) Math.ceil((double) getFilteredItems(state).size() / PAGINATED_SLOTS.length)) + ") Pets",
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

        // Apply sorting
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
        lore.add(" ");
        if (isPetEnabled) {
            ItemStackCreator.enchant(itemStack);
            lore.add("§aCurrently Active!");
            lore.add("§eClick to deselect!");
        } else {
            lore.add("§eClick to summon!");
        }
        return ItemStackCreator.updateLore(itemStack, lore);
    }

    @Override
    protected void onItemClick(ClickContext<PetsState> click, ViewContext ctx, SkyBlockItem item, int index) {
        SkyBlockPlayer player = (SkyBlockPlayer) ctx.player();
        PetsState state = click.state();
        boolean selected = player.getPetData().getEnabledPet() == item;

        if (selected) {
            player.getPetData().deselectCurrent();
            player.getPetData().updatePetEntityImpl(player);
            ctx.session(PetsState.class).update(s -> (PetsState) s.withItems(getPetsFromPlayer(player)));
            player.sendMessage("§cDeselected pet " + item.getDisplayName() + "§c!");
            return;
        }

        if (state.convertToItem()) {
            player.addAndUpdateItem(item);
            player.getPetData().removePet(item.getAttributeHandler().getPotentialType());
            ctx.session(PetsState.class).update(s -> (PetsState) s.withItems(getPetsFromPlayer(player)));
            player.sendMessage("§aYou have picked up your pet!");
            return;
        }

        player.getPetData().setEnabled(item.getAttributeHandler().getPotentialType(), true);
        player.getPetData().updatePetEntityImpl(player);
        player.sendMessage("§aSelected pet " + item.getDisplayName() + "§a!");
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

        // Info item
        layout.slot(4, (s, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            return ItemStackCreator.getStack("§aPets", Material.BONE, 1,
                    "§7View and manage all of your",
                    "§7Pets.",
                    " ",
                    "§7Level up your pets faster by",
                    "§7gaining XP in their favourite",
                    "§7skill!",
                    " ",
                    "§7Selected pet: " + (player.getPetData().getEnabledPet() == null ? "§cNone" : player.getPetData().getEnabledPet().getDisplayName()),
                    " ",
                    "§eClick to view!");
        });

        // Convert to item button
        layout.slot(47, (s, c) -> {
            ItemStack.Builder itemStack = ItemStackCreator.getStack("§aConvert to item", Material.DIAMOND, 1,
                    "§7Toggle between converting your pets to an item",
                    "§7so you can pick it up and",
                    "§7place it in your inventory!",
                    " ",
                    "§7Currently: " + (s.convertToItem() ? "§aEnabled" : "§cDisabled"),
                    " ",
                    "§eClick to convert!");
            if (s.convertToItem())
                ItemStackCreator.enchant(itemStack);
            return itemStack;
        }, (click, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            player.sendMessage("§aPet conversion to item is now " + (!click.state().convertToItem() ? "§aENABLED" : "§cDISABLED") + "§a!");
            c.session(PetsState.class).update(s -> s.withConvertToItem(!s.convertToItem()));
        });

        // Sort button
        layout.slot(51, (s, c) -> {
            List<String> lore = new ArrayList<>();
            lore.add(" ");

            for (SortType randomSortType : SortType.values()) {
                lore.add(randomSortType == s.sortType() ?
                        "§e> " + StringUtility.toNormalCase(randomSortType.name())
                        : "§7> " + StringUtility.toNormalCase(randomSortType.name()));
            }

            lore.add(" ");
            lore.add("§bRight-Click to go backwards!");
            lore.add("§eClick to switch sort!");

            return ItemStackCreator.getStack("§aSort", Material.HOPPER, 1, lore);
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
