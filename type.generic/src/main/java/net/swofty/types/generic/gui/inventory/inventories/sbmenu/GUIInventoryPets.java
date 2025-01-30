package net.swofty.types.generic.gui.inventory.inventories.sbmenu;

import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.item.Rarity;
import net.swofty.commons.item.attribute.attributes.ItemAttributePetData;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockPaginatedInventory;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.components.PetComponent;
import net.swofty.types.generic.item.updater.NonPlayerItemUpdater;
import net.swofty.types.generic.skill.SkillCategories;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.PaginationList;

import java.util.ArrayList;
import java.util.List;

@Setter
public class GUIInventoryPets extends SkyBlockPaginatedInventory<SkyBlockItem> {
    private SortType sortType = SortType.LEVEL;
    private boolean convertToItem = false;

    public GUIInventoryPets() {
        super(InventoryType.CHEST_6_ROW);
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
    protected PaginationList<SkyBlockItem> fillPaged(SkyBlockPlayer player, PaginationList<SkyBlockItem> paged) {
        List<SkyBlockItem> pets = new ArrayList<>(player.getPetData().getPetsMap().keySet().stream().toList());

        switch (sortType) {
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

        paged.addAll(pets);
        return paged;
    }

    @Override
    protected boolean shouldFilterFromSearch(String query, SkyBlockItem item) {
        return !item.getDisplayName().toLowerCase().contains(query.toLowerCase());
    }

    @Override
    protected void performSearch(SkyBlockPlayer player, String query, int page, int maxPage) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, "").build());

        attachItem(GUIItem.builder(49)
                .item(ItemStackCreator.createNamedItemStack(Material.BARRIER, "§cClose").build())
                .onClick((ctx, item) -> {
                    ctx.player().closeInventory();
                    return true;
                })
                .build());

        attachItem(createSearchItem(50, query));

        attachItem(GUIItem.builder(48)
                .item(ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1, "§7To SkyBlock Menu").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUISkyBlockMenu());
                    return true;
                })
                .build());

        attachItem(GUIItem.builder(47)
                .item(() -> {
                    ItemStack.Builder itemStack = ItemStackCreator.getStack("§aConvert to item", Material.DIAMOND, 1,
                            "§7Toggle between converting your pets to an item",
                            "§7so you can pick it up and",
                            "§7place it in your inventory!",
                            " ",
                            "§7Currently: " + (convertToItem ? "§aEnabled" : "§cDisabled"),
                            " ",
                            "§eClick to convert!");
                    if (convertToItem)
                        ItemStackCreator.enchant(itemStack);
                    return itemStack.build();
                })
                .onClick((ctx, item) -> {
                    ctx.player().sendMessage("§aPet conversion to item is now " + (!convertToItem ? "§aENABLED" : "§cDISABLED") + "§a!");
                    convertToItem = !convertToItem;

                    GUIInventoryPets guiPets = new GUIInventoryPets();
                    guiPets.setSortType(sortType);
                    guiPets.setConvertToItem(convertToItem);
                    ctx.player().openInventory(guiPets);
                    return true;
                })
                .build());

        attachItem(GUIItem.builder(51)
                .item(() -> {
                    List<String> lore = new ArrayList<>();
                    lore.add(" ");

                    for (SortType randomSortType : SortType.values()) {
                        lore.add(randomSortType == sortType ?
                                "§e> " + StringUtility.toNormalCase(randomSortType.name())
                                : "§7> " + StringUtility.toNormalCase(randomSortType.name()));
                    }

                    lore.add(" ");
                    lore.add("§bRight-Click to go backwards!");
                    lore.add("§eClick to switch sort!");

                    return ItemStackCreator.getStack("§aSort", Material.HOPPER, 1, lore).build();
                })
                .onClick((ctx, item) -> {
                    boolean isRightClick = ctx.clickType().equals(ClickType.RIGHT_CLICK);

                    int ordinal = sortType.ordinal();
                    if (isRightClick) {
                        ordinal--;
                        if (ordinal < 0) ordinal = SortType.values().length - 1;
                    } else {
                        ordinal++;
                        if (ordinal >= SortType.values().length) ordinal = 0;
                    }

                    sortType = SortType.values()[ordinal];

                    GUIInventoryPets guiPets = new GUIInventoryPets();
                    guiPets.setSortType(sortType);
                    guiPets.setConvertToItem(convertToItem);
                    ctx.player().openInventory(guiPets);
                    return true;
                })
                .build());

        attachItem(GUIItem.builder(4)
                .item(() -> ItemStackCreator.getStack("§aPets", Material.BONE, 1,
                        "§7View and manage all of your",
                        "§7Pets.",
                        " ",
                        "§7Level up your pets faster by",
                        "§7gaining XP in their favourite",
                        "§7skill!",
                        " ",
                        "§7Selected pet: " + (player.getPetData().getEnabledPet() == null ? "§cNone" : player.getPetData().getEnabledPet().getDisplayName()),
                        " ",
                        "§eClick to view!").build())
                .build());

        if (page > 1) {
            attachItem(createNavigationButton(45, query, page, false));
        }
        if (page < maxPage) {
            attachItem(createNavigationButton(53, query, page, true));
        }
    }

    @Override
    protected Component getTitle(SkyBlockPlayer player, String query, int page, PaginationList<SkyBlockItem> paged) {
        return Component.text("(" + page + "/" + paged.getPageCount() + ") Pets");
    }

    @Override
    protected GUIItem createItemFor(SkyBlockItem item, int slot, SkyBlockPlayer player) {
        boolean isPetEnabled = player.getPetData().getEnabledPet() == item;

        return GUIItem.builder(slot)
                .item(() -> {
                    ItemStack.Builder itemStack = new NonPlayerItemUpdater(item).getUpdatedItem();
                    List<String> lore = new ArrayList<>(itemStack.build().get(ItemComponent.LORE)
                            .stream()
                            .map(StringUtility::getTextFromComponent)
                            .toList());

                    if (isPetEnabled) {
                        ItemStackCreator.enchant(itemStack);
                        lore.add(" ");
                        lore.add("§aCurrently Active!");
                        lore.add("§eClick to deselect!");
                    } else {
                        lore.add(" ");
                        lore.add(convertToItem ? "§eClick to pick up!" : "§eClick to summon!");
                    }

                    return ItemStackCreator.updateLore(itemStack, lore).build();
                })
                .onClick((ctx, clickedItem) -> {
                    boolean selected = ctx.player().getPetData().getEnabledPet() == item;
                    if (selected) {
                        ctx.player().getPetData().deselectCurrent();
                        ctx.player().getPetData().updatePetEntityImpl(ctx.player());
                        ctx.player().sendMessage("§cDeselected pet " + item.getDisplayName() + "§c!");
                    } else if (convertToItem) {
                        ctx.player().addAndUpdateItem(item);
                        ctx.player().getPetData().removePet(item.getAttributeHandler().getPotentialType());
                        ctx.player().sendMessage("§aYou have picked up your pet!");
                    } else {
                        ctx.player().getPetData().setEnabled(item.getAttributeHandler().getPotentialType(), true);
                        ctx.player().getPetData().updatePetEntityImpl(ctx.player());
                        ctx.player().sendMessage("§aSelected pet " + item.getDisplayName() + "§a!");
                    }

                    GUIInventoryPets guiPets = new GUIInventoryPets();
                    guiPets.setSortType(sortType);
                    guiPets.setConvertToItem(convertToItem);
                    ctx.player().openInventory(guiPets);
                    return true;
                })
                .build();
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent event, CloseReason reason) {}

    @Override
    public void onBottomClick(InventoryPreClickEvent event) {
        event.setCancelled(true);
    }

    public enum SortType {
        LEVEL,
        RARITY,
        ALPHABETICAL,
        SKILL
    }
}