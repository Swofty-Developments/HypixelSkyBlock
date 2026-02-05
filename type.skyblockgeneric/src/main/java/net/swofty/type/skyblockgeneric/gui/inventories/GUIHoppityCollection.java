package net.swofty.type.skyblockgeneric.gui.inventories;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.chocolatefactory.ChocolateFactoryHelper;
import net.swofty.type.skyblockgeneric.chocolatefactory.ChocolateRabbit;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointChocolateFactory;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.*;
import java.util.stream.Collectors;

public class GUIHoppityCollection extends HypixelInventoryGUI {
    private static final String HOPPITY_TEXTURE = "b79e7f3341b672d9de6564cbaca052a6a723ea466a2e66af35ba1ba855f0d692";
    private static final String LOCATION_TEXTURE = "d7cc6687423d0570d556ac53e0676cb563bbdd9717cd8269bdebed6f6d4e7bf8";

    // Rabbit display slots (7 per row, 4 rows)
    private static final int[] RABBIT_SLOTS = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43
    };

    private static final int RABBITS_PER_PAGE = RABBIT_SLOTS.length;
    private static final int TOTAL_RABBITS = 512; // Total rabbits in Hypixel

    private int currentPage;
    private SortType sortType;
    private FilterType filterType;

    public GUIHoppityCollection() {
        this(1, SortType.A_TO_Z, FilterType.NONE);
    }

    public GUIHoppityCollection(int page, SortType sortType, FilterType filterType) {
        super(buildTitle(page, sortType, filterType), InventoryType.CHEST_6_ROW);
        this.currentPage = page;
        this.sortType = sortType;
        this.filterType = filterType;
    }

    private static String buildTitle(int page, SortType sortType, FilterType filterType) {
        // Calculate total pages based on filtered rabbits (approximation for title)
        int totalRabbits = ChocolateRabbit.values().length;
        int totalPages = Math.max(1, (int) Math.ceil(totalRabbits / (double) RABBITS_PER_PAGE));
        return "(" + page + "/" + totalPages + ") Hoppity's Collection";
    }

    // Border slots (top row, bottom row, and left/right edges)
    private static final int[] BORDER_SLOTS = {
            0, 1, 2, 3, 4, 5, 6, 7, 8,  // Top row
            9, 17,                       // Row 2 edges
            18, 26,                      // Row 3 edges
            27, 35,                      // Row 4 edges
            36, 44,                      // Row 5 edges
            45, 46, 47, 48, 49, 50, 51, 52, 53  // Bottom row
    };

    @Override
    public void setItems(InventoryGUIOpenEvent e) {
        SkyBlockPlayer player = (SkyBlockPlayer) e.player();
        DatapointChocolateFactory.ChocolateFactoryData data = ChocolateFactoryHelper.getData(player);
        Set<String> foundRabbits = data.getFoundRabbits();

        // Get and filter rabbits
        List<ChocolateRabbit> rabbits = getFilteredAndSortedRabbits(foundRabbits);
        int totalPages = Math.max(1, (int) Math.ceil(rabbits.size() / (double) RABBITS_PER_PAGE));

        // Only fill border with black glass panes
        for (int slot : BORDER_SLOTS) {
            set(new GUIItem(slot) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    return ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, " ");
                }
            });
        }

        // Slot 4: Hoppity's Collection info
        set(new GUIItem(4) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                return createCollectionInfoItem((SkyBlockPlayer) p);
            }
        });

        // Set rabbit items for current page (empty slots will be AIR by default)
        int startIndex = (currentPage - 1) * RABBITS_PER_PAGE;
        for (int i = 0; i < RABBIT_SLOTS.length; i++) {
            int rabbitIndex = startIndex + i;
            int slot = RABBIT_SLOTS[i];

            if (rabbitIndex < rabbits.size()) {
                ChocolateRabbit rabbit = rabbits.get(rabbitIndex);
                boolean found = foundRabbits.contains(rabbit.name());
                set(createRabbitItem(slot, rabbit, found));
            }
            // Empty slots are left as AIR (no item)
        }

        // Slot 47: Rabbit Locations
        set(new GUIClickableItem(47) {
            @Override
            public void run(InventoryPreClickEvent event, HypixelPlayer p) {
                // TODO: Cycle through location filters
                p.sendMessage("§7Rabbit Locations filter coming soon!");
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                List<String> lore = new ArrayList<>();
                lore.add("§7Each rabbit has a specific location");
                lore.add("§7on a specific island where it can be");
                lore.add("§7found, just for fun.");
                lore.add("");
                lore.add("§7The §9Hotspot §7of a Rabbit means that");
                lore.add("§7this season they have a §a50% §7higher");
                lore.add("§7chance to be found on this specific");
                lore.add("§7island.");
                lore.add("");
                lore.add("§6Resident §7rabbits however, can §cONLY");
                lore.add("§7be found on their respective islands.");
                lore.add("");
                lore.add("§7Currently selected: §aAll Rabbits");
                lore.add("");
                lore.add("§bRight-click to go backwards!");
                lore.add("§eClick to cycle!");

                return ItemStackCreator.getStackHead("§9Rabbit Locations", LOCATION_TEXTURE, 1, lore);
            }
        });

        // Slot 48: Go Back
        set(new GUIClickableItem(48) {
            @Override
            public void run(InventoryPreClickEvent event, HypixelPlayer p) {
                GUIChocolateFactory.open((SkyBlockPlayer) p);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                List<String> lore = new ArrayList<>();
                lore.add("§7To Chocolate Factory");
                return ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1, lore);
            }
        });

        // Slot 49: Close
        set(GUIClickableItem.getCloseItem(49));

        // Slot 50: Sort
        int finalTotalPages = totalPages;
        set(new GUIClickableItem(50) {
            @Override
            public void run(InventoryPreClickEvent event, HypixelPlayer p) {
                boolean isRightClick = event.getClick() instanceof Click.Right;
                SortType newSort = isRightClick
                        ? sortType.previous()
                        : sortType.next();
                new GUIHoppityCollection(1, newSort, filterType).open((SkyBlockPlayer) p);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                List<String> lore = new ArrayList<>();
                lore.add("");
                for (SortType type : SortType.values()) {
                    if (type == sortType) {
                        lore.add("§b▶ " + type.getDisplayName());
                    } else {
                        lore.add("§7  " + type.getDisplayName());
                    }
                }
                lore.add("");
                lore.add("§bRight-click to go backwards!");
                lore.add("§eClick to switch sort!");

                return ItemStackCreator.getStack("§aSort", Material.HOPPER, 1, lore);
            }
        });

        // Slot 51: Filter
        set(new GUIClickableItem(51) {
            @Override
            public void run(InventoryPreClickEvent event, HypixelPlayer p) {
                boolean isRightClick = event.getClick() instanceof Click.Right;
                FilterType newFilter = isRightClick
                        ? filterType.previous()
                        : filterType.next();
                new GUIHoppityCollection(1, sortType, newFilter).open((SkyBlockPlayer) p);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                List<String> lore = new ArrayList<>();
                lore.add("");
                for (FilterType type : FilterType.values()) {
                    if (type == filterType) {
                        lore.add("§a▶ " + type.getDisplayName());
                    } else {
                        lore.add("§7  " + type.getDisplayName());
                    }
                }
                lore.add("");
                lore.add("§bRight-click to go backwards!");
                lore.add("§eClick to switch!");

                return ItemStackCreator.getStack("§aFilter", Material.ENDER_EYE, 1, lore);
            }
        });

        // Slot 53: Next Page (if applicable)
        if (currentPage < totalPages) {
            set(new GUIClickableItem(53) {
                @Override
                public void run(InventoryPreClickEvent event, HypixelPlayer p) {
                    new GUIHoppityCollection(currentPage + 1, sortType, filterType).open((SkyBlockPlayer) p);
                }

                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    List<String> lore = new ArrayList<>();
                    lore.add("§ePage " + (currentPage + 1));
                    return ItemStackCreator.getStack("§aNext Page", Material.ARROW, 1, lore);
                }
            });
        }

        // Slot 45: Previous Page (if applicable)
        if (currentPage > 1) {
            set(new GUIClickableItem(45) {
                @Override
                public void run(InventoryPreClickEvent event, HypixelPlayer p) {
                    new GUIHoppityCollection(currentPage - 1, sortType, filterType).open((SkyBlockPlayer) p);
                }

                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    List<String> lore = new ArrayList<>();
                    lore.add("§ePage " + (currentPage - 1));
                    return ItemStackCreator.getStack("§aPrevious Page", Material.ARROW, 1, lore);
                }
            });
        }
    }

    private ItemStack.Builder createCollectionInfoItem(SkyBlockPlayer player) {
        DatapointChocolateFactory.ChocolateFactoryData data = ChocolateFactoryHelper.getData(player);
        Set<String> foundRabbits = data.getFoundRabbits();
        int found = foundRabbits.size();
        double percentage = (found / (double) TOTAL_RABBITS) * 100;

        // Calculate bonuses
        int totalChocolate = 0;
        double totalMultiplier = 0;
        for (String rabbitName : foundRabbits) {
            try {
                ChocolateRabbit rabbit = ChocolateRabbit.valueOf(rabbitName);
                totalChocolate += rabbit.getChocolateBonus();
                totalMultiplier += rabbit.getMultiplierBonus();
            } catch (IllegalArgumentException ignored) {
            }
        }

        List<String> lore = new ArrayList<>();
        lore.add("§7Help §aHoppity §7find all of his §aChocolate");
        lore.add("§aRabbits §7during the §dHoppity's Hunt");
        lore.add("§7event!");
        lore.add("");
        lore.add("§7The more unique §aChocolate Rabbits");
        lore.add("§7that you find, the more your");
        lore.add("§6Chocolate Factory §7will produce!");
        lore.add("");
        lore.add("§7Finding duplicate Rabbits grants");
        lore.add("§a+10% §7extra §6Chocolate §7per duplicate,");
        lore.add("§7up to §a+100%§7!");
        lore.add("");
        lore.add("§7Rabbits Found: §e" + String.format("%.1f", percentage) + "§6%");
        lore.add(createProgressBar(percentage) + " §e" + found + "§6/§e" + TOTAL_RABBITS);
        lore.add("");
        if (totalChocolate > 0) {
            lore.add("§6+" + totalChocolate + " Chocolate per second");
        }
        if (totalMultiplier > 0) {
            lore.add("§6+" + String.format("%.3fx", totalMultiplier) + " Chocolate Multiplier");
        }

        return ItemStackCreator.getStackHead("§aHoppity's Collection", HOPPITY_TEXTURE, 1, lore);
    }

    private GUIItem createRabbitItem(int slot, ChocolateRabbit rabbit, boolean found) {
        return new GUIItem(slot) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                List<String> lore = new ArrayList<>();

                // Bonus info
                if (rabbit.getRarity() == ChocolateRabbit.Rarity.LEGENDARY ||
                        rabbit.getRarity() == ChocolateRabbit.Rarity.DIVINE ||
                        rabbit.getRarity() == ChocolateRabbit.Rarity.MYTHIC) {
                    lore.add("§7Grants §6+" + String.format("%.2fx", rabbit.getMultiplierBonus()) + " Chocolate §7per second");
                    lore.add("§7to your §6Chocolate Factory§7.");
                } else {
                    lore.add("§7Grants §6+" + rabbit.getChocolateBonus() + " Chocolate §7and §6" +
                            String.format("%.3fx", rabbit.getMultiplierBonus()));
                    lore.add("§6Chocolate §7per second to your");
                    lore.add("§6Chocolate Factory§7.");
                }
                lore.add("");

                // Obtain method if exists
                if (rabbit.getObtainMethod() != null) {
                    lore.add("§7" + rabbit.getObtainMethod());
                    lore.add("");
                }

                // Requirement if exists
                if (rabbit.getRequirement() != null) {
                    lore.add("§c✖ §7Requirement");
                    lore.add("§7" + rabbit.getRequirement());
                    lore.add("");
                    if (!found) {
                        lore.add("§8You cannot find this rabbit until you");
                        lore.add("§8meet the requirement!");
                        lore.add("");
                    }
                }

                // Found status
                if (found) {
                    lore.add("§a§lFOUND");
                } else {
                    lore.add("§8You have not found this rabbit yet!");
                }

                // Location if exists (show as Resident label after found status)
                if (rabbit.getLocation() != null) {
                    lore.add(rabbit.getResidentLabel());
                }
                lore.add("");

                // Rarity
                lore.add(rabbit.getRarity().getFormattedName());

                if (found) {
                    // Use a default rabbit head texture for found rabbits
                    return ItemStackCreator.getStackHead(rabbit.getFormattedName(),
                            "b79e7f3341b672d9de6564cbaca052a6a723ea466a2e66af35ba1ba855f0d692", 1, lore);
                } else {
                    return ItemStackCreator.getStack(rabbit.getFormattedName(), Material.GRAY_DYE, 1, lore);
                }
            }
        };
    }

    private List<ChocolateRabbit> getFilteredAndSortedRabbits(Set<String> foundRabbits) {
        List<ChocolateRabbit> rabbits = Arrays.stream(ChocolateRabbit.values())
                .filter(rabbit -> {
                    boolean found = foundRabbits.contains(rabbit.name());
                    return switch (filterType) {
                        case NONE -> true;
                        case FOUND -> found;
                        case NOT_FOUND -> !found;
                        case HAS_REQUIREMENT -> rabbit.getRequirement() != null;
                        case NO_REQUIREMENT -> rabbit.getRequirement() == null;
                    };
                })
                .collect(Collectors.toList());

        // Sort
        switch (sortType) {
            case A_TO_Z -> rabbits.sort(Comparator.comparing(ChocolateRabbit::getDisplayName));
            case Z_TO_A -> rabbits.sort(Comparator.comparing(ChocolateRabbit::getDisplayName).reversed());
            case HIGHEST_RARITY -> rabbits.sort(Comparator.comparing((ChocolateRabbit r) -> r.getRarity().ordinal()).reversed());
            case LOWEST_RARITY -> rabbits.sort(Comparator.comparing(r -> r.getRarity().ordinal()));
        }

        return rabbits;
    }

    private String createProgressBar(double progress) {
        int filled = (int) (progress / 4);
        int empty = 25 - filled;

        StringBuilder bar = new StringBuilder("§2§l§m");
        for (int i = 0; i < filled; i++) {
            bar.append(" ");
        }
        bar.append("§f§l§m");
        for (int i = 0; i < empty; i++) {
            bar.append(" ");
        }
        bar.append("§r");

        return bar.toString();
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent e, CloseReason reason) {
    }

    @Override
    public void suddenlyQuit(Inventory inventory, HypixelPlayer player) {
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }

    public enum SortType {
        A_TO_Z("A to Z"),
        Z_TO_A("Z to A"),
        HIGHEST_RARITY("Highest Rarity"),
        LOWEST_RARITY("Lowest Rarity");

        private final String displayName;

        SortType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        public SortType next() {
            SortType[] values = values();
            return values[(ordinal() + 1) % values.length];
        }

        public SortType previous() {
            SortType[] values = values();
            return values[(ordinal() - 1 + values.length) % values.length];
        }
    }

    public enum FilterType {
        NONE("None"),
        FOUND("Rabbits Found"),
        NOT_FOUND("Rabbits Not Found"),
        HAS_REQUIREMENT("Has Requirement"),
        NO_REQUIREMENT("No Requirement");

        private final String displayName;

        FilterType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        public FilterType next() {
            FilterType[] values = values();
            return values[(ordinal() + 1) % values.length];
        }

        public FilterType previous() {
            FilterType[] values = values();
            return values[(ordinal() - 1 + values.length) % values.length];
        }
    }
}
