package net.swofty.type.skyblockgeneric.gui.inventories;

import lombok.Getter;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.skyblockgeneric.chocolatefactory.ChocolateFactoryHelper;
import net.swofty.type.skyblockgeneric.chocolatefactory.ChocolateRabbit;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointChocolateFactory;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.*;
import java.util.stream.Collectors;

public class GUIHoppityCollection implements StatefulView<GUIHoppityCollection.State> {
    private static final String HOPPITY_TEXTURE = "b79e7f3341b672d9de6564cbaca052a6a723ea466a2e66af35ba1ba855f0d692";
    private static final String LOCATION_TEXTURE = "d7cc6687423d0570d556ac53e0676cb563bbdd9717cd8269bdebed6f6d4e7bf8";

    private static final int[] RABBIT_SLOTS = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43
    };

    private static final int RABBITS_PER_PAGE = RABBIT_SLOTS.length;
    private static final int TOTAL_RABBITS = 512;

    private static final int[] BORDER_SLOTS = {
            0, 1, 2, 3, 4, 5, 6, 7, 8,
            9, 17, 18, 26, 27, 35, 36, 44,
            45, 46, 47, 48, 49, 50, 51, 52, 53
    };

    public record State(int page, SortType sortType, FilterType filterType) {}

    @Override
    public State initialState() {
        return new State(1, SortType.A_TO_Z, FilterType.NONE);
    }

    @Override
    public ViewConfiguration<State> configuration() {
        return ViewConfiguration.withString((state, ctx) -> {
            int totalRabbits = ChocolateRabbit.values().length;
            int totalPages = Math.max(1, (int) Math.ceil(totalRabbits / (double) RABBITS_PER_PAGE));
            return "(" + state.page() + "/" + totalPages + ") Hoppity's Collection";
        }, InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<State> layout, State state, ViewContext ctx) {
        SkyBlockPlayer player = (SkyBlockPlayer) ctx.player();
        DatapointChocolateFactory.ChocolateFactoryData data = ChocolateFactoryHelper.getData(player);
        Set<String> foundRabbits = data.getFoundRabbits();

        List<ChocolateRabbit> rabbits = getFilteredAndSortedRabbits(foundRabbits, state.sortType(), state.filterType());
        int totalPages = Math.max(1, (int) Math.ceil(rabbits.size() / (double) RABBITS_PER_PAGE));

        // Fill border
        for (int slot : BORDER_SLOTS) {
            layout.slot(slot, (s, c) -> ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, " "));
        }

        // Slot 4: Collection info
        layout.slot(4, (s, c) -> createCollectionInfoItem((SkyBlockPlayer) c.player()));

        // Rabbit items
        int startIndex = (state.page() - 1) * RABBITS_PER_PAGE;
        for (int i = 0; i < RABBIT_SLOTS.length; i++) {
            int rabbitIndex = startIndex + i;
            int slot = RABBIT_SLOTS[i];

            if (rabbitIndex < rabbits.size()) {
                ChocolateRabbit rabbit = rabbits.get(rabbitIndex);
                layout.slot(slot, (s, c) -> {
                    SkyBlockPlayer p = (SkyBlockPlayer) c.player();
                    boolean found = ChocolateFactoryHelper.getData(p).getFoundRabbits().contains(rabbit.name());
                    return createRabbitItem(rabbit, found);
                });
            } else {
                layout.slot(slot, (s, c) -> ItemStack.AIR.builder());
            }
        }

        // Slot 47: Rabbit Locations
        layout.slot(47, (s, c) -> {
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
        }, (click, c) -> c.player().sendMessage("§7Rabbit Locations filter coming soon!"));

        // Slot 48: Go Back
        Components.back(layout, 48, ctx);

        // Slot 49: Close
        Components.close(layout, 49);

        // Slot 50: Sort
        layout.slot(50, (s, c) -> {
            List<String> lore = new ArrayList<>();
            lore.add("");
            for (SortType type : SortType.values()) {
                if (type == s.sortType()) {
                    lore.add("§b▶ " + type.getDisplayName());
                } else {
                    lore.add("§7  " + type.getDisplayName());
                }
            }
            lore.add("");
            lore.add("§bRight-click to go backwards!");
            lore.add("§eClick to switch sort!");

            return ItemStackCreator.getStack("§aSort", Material.HOPPER, 1, lore);
        }, (click, c) -> {
            boolean isRightClick = click.click() instanceof Click.Right;
            SortType newSort = isRightClick ? state.sortType().previous() : state.sortType().next();
            c.session(State.class).setState(new State(1, newSort, state.filterType()));
        });

        // Slot 51: Filter
        layout.slot(51, (s, c) -> {
            List<String> lore = new ArrayList<>();
            lore.add("");
            for (FilterType type : FilterType.values()) {
                if (type == s.filterType()) {
                    lore.add("§a▶ " + type.getDisplayName());
                } else {
                    lore.add("§7  " + type.getDisplayName());
                }
            }
            lore.add("");
            lore.add("§bRight-click to go backwards!");
            lore.add("§eClick to switch!");

            return ItemStackCreator.getStack("§aFilter", Material.ENDER_EYE, 1, lore);
        }, (click, c) -> {
            boolean isRightClick = click.click() instanceof Click.Right;
            FilterType newFilter = isRightClick ? state.filterType().previous() : state.filterType().next();
            c.session(State.class).setState(new State(1, state.sortType(), newFilter));
        });

        // Slot 53: Next Page
        if (state.page() < totalPages) {
            layout.slot(53, (s, c) -> {
                List<String> lore = new ArrayList<>();
                lore.add("§ePage " + (s.page() + 1));
                return ItemStackCreator.getStack("§aNext Page", Material.ARROW, 1, lore);
            }, (click, c) -> c.session(State.class).setState(new State(state.page() + 1, state.sortType(), state.filterType())));
        }

        // Slot 45: Previous Page
        if (state.page() > 1) {
            layout.slot(45, (s, c) -> {
                List<String> lore = new ArrayList<>();
                lore.add("§ePage " + (s.page() - 1));
                return ItemStackCreator.getStack("§aPrevious Page", Material.ARROW, 1, lore);
            }, (click, c) -> c.session(State.class).setState(new State(state.page() - 1, state.sortType(), state.filterType())));
        }
    }

    private ItemStack.Builder createCollectionInfoItem(SkyBlockPlayer player) {
        DatapointChocolateFactory.ChocolateFactoryData data = ChocolateFactoryHelper.getData(player);
        Set<String> foundRabbits = data.getFoundRabbits();
        int found = foundRabbits.size();
        double percentage = (found / (double) TOTAL_RABBITS) * 100;

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

    private ItemStack.Builder createRabbitItem(ChocolateRabbit rabbit, boolean found) {
        List<String> lore = new ArrayList<>();

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

        if (rabbit.getObtainMethod() != null) {
            lore.add("§7" + rabbit.getObtainMethod());
            lore.add("");
        }

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

        if (!found) {
            lore.add("§8You have not found this rabbit yet!");
        }

        if (rabbit.getLocation() != null) {
            lore.add("");
            lore.add(rabbit.getResidentLabel());
        }
        lore.add("");
        lore.add(rabbit.getRarity().getFormattedName());

        if (found) {
            return ItemStackCreator.getStackHead(rabbit.getFormattedName(),
                    "b79e7f3341b672d9de6564cbaca052a6a723ea466a2e66af35ba1ba855f0d692", 1, lore);
        } else {
            return ItemStackCreator.getStack(rabbit.getFormattedName(), Material.GRAY_DYE, 1, lore);
        }
    }

    private List<ChocolateRabbit> getFilteredAndSortedRabbits(Set<String> foundRabbits, SortType sortType, FilterType filterType) {
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

    @Getter
    public enum SortType {
        A_TO_Z("A to Z"),
        Z_TO_A("Z to A"),
        HIGHEST_RARITY("Highest Rarity"),
        LOWEST_RARITY("Lowest Rarity");

        private final String displayName;

        SortType(String displayName) {
            this.displayName = displayName;
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

    @Getter
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
