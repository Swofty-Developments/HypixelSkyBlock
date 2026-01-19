package net.swofty.type.generic.gui.v2.test;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ClickContext;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class TestMixedView implements StatefulView<TestMixedView.State> {

    private static final List<Integer> INPUT_SLOTS = List.of(10, 11, 12, 19, 20, 21, 28, 29, 30);
    private static final int OUTPUT_SLOT = 24;

    public record State(Map<Integer, ItemStack> inputs) {
        public State withInput(int slot, ItemStack item) {
            Map<Integer, ItemStack> newInputs = new HashMap<>(inputs);
            if (item.isAir()) {
                newInputs.remove(slot);
            } else {
                newInputs.put(slot, item);
            }
            return new State(newInputs);
        }

        public State clearInputs() {
            return new State(new HashMap<>());
        }

        public int inputCount() {
            return (int) inputs.values().stream().filter(i -> !i.isAir()).count();
        }

        public boolean hasOutput() {
            return inputs.containsKey(20) && !inputs.get(20).isAir();
        }
    }

    @Override
    public State initialState() {
        return new State(new HashMap<>());
    }

    @Override
    public ViewConfiguration<State> configuration() {
        return new ViewConfiguration<>("§5Crafting Station", InventoryType.CHEST_5_ROW);
    }

    @Override
    public void layout(ViewLayout<State> layout, State state, ViewContext ctx) {
        layout.filler(Components.FILLER);

        layout.slot(4, ItemStackCreator.getStack(
                "§5Crafting Station",
                Material.CRAFTING_TABLE,
                1,
                "§7Place items in the grid",
                "§7to create new items!",
                "",
                "§7Input items: §e" + state.inputCount()
        ));

        for (int slot : INPUT_SLOTS) {
            layout.editable(slot, (s, c) -> s.inputs().getOrDefault(slot, ItemStack.AIR).builder(),
                    (changedSlot, oldItem, newItem, s) ->
                            ctx.session(State.class).updateQuiet(current -> current.withInput(changedSlot, newItem))
            );
        }

        layout.slot(22, (s, c) -> ItemStackCreator.getStack(
                s.hasOutput() ? "§a→ Ready!" : "§7→",
                Material.ARROW,
                1,
                s.hasOutput() ? "§aClick output to craft!" : "§7Place an item in the center"
        ));

        layout.slot(OUTPUT_SLOT, (s, c) -> {
            if (s.hasOutput()) {
                return ItemStackCreator.getStack("§bCrafted Item", Material.DIAMOND, 1, "§7Click to take!");
            }
            return ItemStack.AIR.builder();
        }, (click, context) -> {
            State s = click.state();
            if (s.hasOutput()) {
                click.player().getInventory().addItemStack(ItemStack.of(Material.DIAMOND, 1));
                click.player().sendMessage("§aYou crafted a Diamond!");
                context.session(State.class).setState(s.clearInputs());
                for (int slot : INPUT_SLOTS) {
                    context.inventory().setItemStack(slot, ItemStack.AIR);
                }
            }
        });

        layout.slot(40, (s, c) -> ItemStackCreator.getStack(
                "§cClear Grid",
                Material.BARRIER,
                1,
                "§7Clears all input slots"
        ), (click, context) -> {
            context.session(State.class).setState(state.clearInputs());
            for (int slot : INPUT_SLOTS) {
                context.inventory().setItemStack(slot, ItemStack.AIR);
            }
            click.player().sendMessage("§cGrid cleared!");
        });

        Components.close(layout, 44);
        layout.allowHotkey(true);
    }

    @Override
    public boolean onBottomClick(ClickContext<State> click, ViewContext ctx) {
        return true;
    }

    public static void open(HypixelPlayer player) {
        ViewNavigator.get(player).push(new TestMixedView(), new State(new HashMap<>()));
    }
}
