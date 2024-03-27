package net.swofty.types.generic.gui.inventory.inventories.sbmenu;

import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockPaginatedGUI;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.gui.inventory.item.GUIItem;
import net.swofty.types.generic.item.Rarity;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.attribute.attributes.ItemAttributePetData;
import net.swofty.types.generic.item.impl.Pet;
import net.swofty.types.generic.item.updater.NonPlayerItemUpdater;
import net.swofty.types.generic.skill.SkillCategories;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.PaginationList;
import net.swofty.types.generic.utility.StringUtility;

import java.util.ArrayList;
import java.util.List;

public class GUIPets extends SkyBlockPaginatedGUI<SkyBlockItem> {
    @Setter
    private SortType sortType = SortType.LEVEL;
    @Setter
    private boolean convertToItem = false;

    public GUIPets() {
        super(InventoryType.CHEST_6_ROW);
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }

    @Override
    public int[] getPaginatedSlots() {
        return new int[]{
                10, 11, 12, 13, 14, 15, 16,
                19, 20, 21, 22, 23, 24, 25,
                28, 29, 30, 31, 32, 33, 34,
                37, 38, 39, 40, 41, 42, 43
        };
    }

    @Override
    public PaginationList<SkyBlockItem> fillPaged(SkyBlockPlayer player, PaginationList<SkyBlockItem> paged) {
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
                    String name1 = ((Pet) pet1.getGenericInstance()).getPetName();
                    String name2 = ((Pet) pet2.getGenericInstance()).getPetName();
                    return name1.compareTo(name2);
                });
                break;
            case SKILL:
                pets.sort((pet1, pet2) -> {
                    SkillCategories skill1 = ((Pet) pet1.getGenericInstance()).getSkillCategory();
                    SkillCategories skill2 = ((Pet) pet2.getGenericInstance()).getSkillCategory();
                    return Integer.compare(skill2.ordinal(), skill1.ordinal());
                });
                break;
        }

        paged.addAll(pets);

        return paged;
    }

    @Override
    public boolean shouldFilterFromSearch(String query, SkyBlockItem item) {
        return !item.getDisplayName().toLowerCase().contains(query.toLowerCase());
    }

    @Override
    public void performSearch(SkyBlockPlayer player, String query, int page, int maxPage) {
        border(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, ""));
        set(GUIClickableItem.getCloseItem(49));
        set(createSearchItem(this, 50, query));
        set(GUIClickableItem.getGoBackItem(48, new GUISkyBlockMenu()));
        set(new GUIClickableItem(47) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                player.sendMessage("§aPet conversion to item is now " + (!convertToItem ? "§aENABLED" : "§cDISABLED") + "§a!");

                convertToItem = !convertToItem;
                GUIPets guiPets = new GUIPets();
                guiPets.setSortType(sortType);
                guiPets.setConvertToItem(convertToItem);
                guiPets.open(player);
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
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
                return itemStack;
            }
        });

        set(new GUIClickableItem(51) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                boolean isRightClick = e.getClickType().equals(ClickType.RIGHT_CLICK);

                int ordinal = sortType.ordinal();
                if (isRightClick) {
                    ordinal--;
                    if (ordinal < 0) ordinal = SortType.values().length - 1;
                } else {
                    ordinal++;
                    if (ordinal >= SortType.values().length) ordinal = 0;
                }

                sortType = SortType.values()[ordinal];

                GUIPets guiPets = new GUIPets();
                guiPets.setSortType(sortType);
                guiPets.setConvertToItem(convertToItem);
                guiPets.open(player);
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
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

                return ItemStackCreator.getStack("§aSort", Material.HOPPER, 1, lore);
            }
        });
        set(new GUIItem(4) {
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
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
            }
        });

        if (page > 1) {
            set(createNavigationButton(this, 45, query, page, false));
        }
        if (page < maxPage) {
            set(createNavigationButton(this, 53, query, page, true));
        }
    }

    @Override
    public String getTitle(SkyBlockPlayer player, String query, int page, PaginationList<SkyBlockItem> paged) {
        return "(" + page + "/" + paged.getPageCount() + ") Pets";
    }

    @Override
    public GUIClickableItem createItemFor(SkyBlockItem item, int slot, SkyBlockPlayer player) {
        boolean isPetEnabled = player.getPetData().getEnabledPet() == item;

        ItemStack.Builder itemStack = new NonPlayerItemUpdater(item).getUpdatedItem();
        List<String> lore = new ArrayList<>(itemStack.build().getLore().stream().map(StringUtility::getTextFromComponent).toList());
        if (isPetEnabled) {
            ItemStackCreator.enchant(itemStack);

            lore.add(" ");
            lore.add("§aCurrently Active!");
            lore.add("§eClick to deselect!");

            itemStack.lore(lore.stream().map(loreLine -> {
                return Component.text().content(loreLine).style(
                        Style.style()
                                .decoration(TextDecoration.ITALIC, false)
                                .build())
                        .build();
            }).toList());
        } else {
            lore.add(" ");
            lore.add(convertToItem ? "§eClick to pick up!" : "§eClick to summon!");

            itemStack.lore(lore.stream().map(loreLine -> {
                return Component.text().content(loreLine).style(
                                Style.style()
                                        .decoration(TextDecoration.ITALIC, false)
                                        .build())
                        .build();
            }).toList());
        }

        return new GUIClickableItem(slot) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                boolean selected = player.getPetData().getEnabledPet() == item;
                if (selected) {
                    player.getPetData().deselectCurrent();
                    player.getPetData().updatePetEntityImpl(player);
                    GUIPets guiPets = new GUIPets();
                    guiPets.setSortType(sortType);
                    guiPets.setConvertToItem(convertToItem);
                    guiPets.open(player);
                    player.sendMessage("§cDeselected pet " + item.getDisplayName() + "§c!");
                    return;
                }

                if (convertToItem) {
                    player.addAndUpdateItem(item);
                    player.getPetData().removePet(item.getAttributeHandler().getItemTypeAsType());
                    GUIPets guiPets = new GUIPets();
                    guiPets.setSortType(sortType);
                    guiPets.setConvertToItem(convertToItem);
                    guiPets.open(player);
                    player.sendMessage("§aYou have picked up your pet!");
                    return;
                }

                player.getPetData().setEnabled(item.getAttributeHandler().getItemTypeAsType(), true);
                player.getPetData().updatePetEntityImpl(player);
                player.sendMessage("§aSelected pet " + item.getDisplayName() + "§a!");
                GUIPets guiPets = new GUIPets();
                guiPets.setSortType(sortType);
                guiPets.setConvertToItem(convertToItem);
                guiPets.open(player);
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return itemStack;
            }
        };
    }

    public enum SortType {
        LEVEL,
        RARITY,
        ALPHABETICAL,
        SKILL
    }
}
