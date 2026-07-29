package net.swofty.type.skyblockgeneric.gui.inventories.fishing;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;

import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.StatelessView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;

import java.util.List;

/**
 * Shared chrome for the 3-page sea creature guide. Subclasses only need to
 * declare which page they are and which entries to show; layout, navigation,
 * sort/filter/category buttons all live here.
 *
 * Entries are modelled as a sealed {@link Entry} hierarchy: {@code Head} for
 * player heads (the common case — most creatures use one) and {@code Block}
 * for the handful that render as a regular item (dragon eggs, etc.).
 */
public abstract sealed class SeaCreatureGuidePage extends StatelessView
    permits GUI13SeaCreatureGuide, GUI23SeaCreatureGuide, GUI33SeaCreatureGuide {

    private static final int TOTAL_PAGES = 3;

    protected abstract int pageNumber();

    protected abstract List<Entry> entries();

    @Override
    public final ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>(
            "(" + pageNumber() + "/" + TOTAL_PAGES + ") Sea Creature Guide",
            InventoryType.CHEST_6_ROW
        );
    }

    @Override
    public final void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.close(layout, 49);

        layout.slot(4, ItemStackCreator.getStack(
            "§aSea Creature Guide",
            Material.BOOK,
            1,
            "§7Your guide to the creatures of the",
            "§7deep! Can also be accessed with",
            "§a/scg§7!",
            "",
            "§7Beware, Sea Creatures spawn much",
            "§7less often on your private island.",
            "",
            "§7Your Fishing: §aLevel XVIII"
        ));

        for (Entry entry : entries()) {
            layout.slot(entry.slot(), entry.render());
        }

        if (pageNumber() > 1) {
            layout.slot(45, ItemStackCreator.getStack(
                "§aPrevious Page",
                Material.ARROW,
                1,
                "§ePage " + (pageNumber() - 1)
            ));
        }

        layout.slot(48, ItemStackCreator.getStack(
            "§aGo Back",
            Material.ARROW,
            1,
            "§7To Fishing Skill"
        ));

        layout.slot(50, ItemStackCreator.getStack(
            "§aSort",
            Material.HOPPER,
            1,
            "",
            "§b▶ Fishing Level Req",
            "§7  Alphabetical",
            "§7  Mob Level",
            "§7  Killed Most",
            "§7  Ascending Rarity",
            "§7  Descending Rarity",
            "",
            "§bRight-click to go backwards!",
            "§eClick to switch!"
        ));

        layout.slot(51, ItemStackCreator.getStack(
            "§aFilter",
            Material.ENDER_EYE,
            1,
            "",
            "§f▶ All Sea Creatures",
            "§7  Has Level Requirement",
            "§7  Has Never Killed",
            "",
            "§bRight-click to go backwards!",
            "§eClick to switch!"
        ));

        layout.slot(52, ItemStackCreator.getStack(
            "§aCategory",
            Material.CAULDRON,
            1,
            "",
            "§a▶ Any Category",
            "§7  Water",
            "§7  Lava",
            "§7  Winter",
            "§7  Spooky",
            "§7  Shark",
            "§7  Oasis",
            "§7  Bayou",
            "§7  Hotspot",
            "§7  Galatea",
            "",
            "§bRight-click to go backwards!",
            "§eClick to switch!"
        ));

        if (pageNumber() < TOTAL_PAGES) {
            layout.slot(53, ItemStackCreator.getStack(
                "§aNext Page",
                Material.ARROW,
                1,
                "§ePage " + (pageNumber() + 1)
            ));
        }
    }

    /** Convenience builder for a head-rendered entry. */
    protected static Entry head(int slot, String name, String texture, String... lore) {
        return new Entry.Head(slot, name, texture, lore);
    }

    /** Convenience builder for a material-rendered entry. */
    protected static Entry block(int slot, String name, Material material, String... lore) {
        return new Entry.Block(slot, name, material, lore);
    }

    public sealed interface Entry {
        int slot();

        ItemStack.Builder render();

        record Head(int slot, String name, String texture, String... lore) implements Entry {
            @Override
            public ItemStack.Builder render() {
                return ItemStackCreator.getStackHead(name, texture, 1, lore);
            }
        }

        record Block(int slot, String name, Material material, String... lore) implements Entry {
            @Override
            public ItemStack.Builder render() {
                return ItemStackCreator.getStack(name, material, 1, lore);
            }
        }
    }
}
