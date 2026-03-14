package net.swofty.type.skyblockgeneric.gui.inventories.abiphone;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.component.DataComponents;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ClickContext;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.abiphone.AbiphoneNPC;
import net.swofty.type.skyblockgeneric.abiphone.AbiphoneRegistry;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;

import java.util.List;

public final class AbiphoneView extends PaginatedView<AbiphoneNPC, AbiphoneView.State> {

    private static final int[] PAGINATED_SLOTS = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43
    };

    @Override
    public ViewConfiguration<State> configuration() {
        return ViewConfiguration.withString(
                (state, ctx) -> state.abiphone().getCleanName(),
                InventoryType.CHEST_6_ROW
        );
    }

    @Override
    protected int[] getPaginatedSlots() {
        return PAGINATED_SLOTS;
    }

    @Override
    protected ItemStack.Builder renderItem(AbiphoneNPC npc, int index, HypixelPlayer player) {
        return ItemStackCreator.updateLore(
                npc.getIcon().set(DataComponents.CUSTOM_NAME, Component.text("§f" + npc.getName())),
                List.of(
                        "§7" + npc.getDescription(),
                        "",
                        "§8Right-click to manage!",
                        "§eLeft-click to call!"
                )
        );
    }

    @Override
    protected void onItemClick(ClickContext<State> click, ViewContext ctx, AbiphoneNPC npc, int index) {
        if (click.click() instanceof Click.Left) {
            ctx.player().closeInventory();
            initiateCall(ctx.player(), npc);
        } else if (click.click() instanceof Click.Right) {
            ctx.push(new GUIContactManagementView(), new GUIContactManagementView.State(click.state().abiphone(), npc));
        }
    }

    private void initiateCall(HypixelPlayer player, AbiphoneNPC npc) {
        player.sendMessage(Component.text("§e✆ RING..."));
        MinecraftServer.getSchedulerManager().buildTask(() -> {
            player.sendMessage(Component.text("§e✆ RING... RING..."));
            MinecraftServer.getSchedulerManager().buildTask(() -> {
                player.sendMessage(Component.text("§e✆ RING... RING... RING..."));
                MinecraftServer.getSchedulerManager().buildTask(() -> {
                    npc.onCall(player);
                }).delay(TaskSchedule.seconds(1)).schedule();
            }).delay(TaskSchedule.seconds(1)).schedule();
        }).delay(TaskSchedule.seconds(1)).schedule();
    }

    @Override
    protected boolean shouldFilterFromSearch(State state, AbiphoneNPC item) {
        return !item.getName().toLowerCase().contains(state.query.toLowerCase());
    }

    @Override
    protected void layoutCustom(ViewLayout<State> layout, State state, ViewContext ctx) {
        List<AbiphoneNPC> contacts = state.abiphone().getAttributeHandler().getAbiphoneNPCs();
        Components.close(layout, 49);
        layout.slot(50, (s, c) -> ItemStackCreator.getStack(
                "§aSort §bTO-DO",
                Material.HOPPER,
                1,
                "",
                "§7  First Added",
                "§7  Alphabetical",
                "§7  Last Called",
                "§7  Most Called",
                "§7  Do Not Disturb First",
                "",
                "§bRight-click to go backwards!",
                "§eClick to switch!"
        ), (click, viewCtx) -> {
            // TODO: Implement sorting
        });

        layout.slot(51, (s, c) -> ItemStackCreator.getStack(
                "§aContacts Directory §bTO-DO",
                Material.BOOK,
                1,
                "§7Browse through all NPCs in SkyBlock",
                "§7which both own an Abiphone AND are",
                "§7willing to add you as a contact.",
                "",
                "§7Your contacts: §a" + contacts.size() + "§b/" + AbiphoneRegistry.getRegisteredContactNPCs().size(),
                "",
                "§eClick to view contacts!"
        ), (click, viewCtx) -> {
            // TODO: Open contacts directory
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

    public record State(
            SkyBlockItem abiphone,
            List<AbiphoneNPC> items,
            int page,
            String query,
            SortType sortType
    ) implements PaginatedState<AbiphoneNPC> {

        public State(SkyBlockItem abiphone) {
            this(abiphone, abiphone.getAttributeHandler().getAbiphoneNPCs(), 0, "", SortType.ALPHABETICAL);
        }

        @Override
        public PaginatedState<AbiphoneNPC> withPage(int page) {
            return new State(abiphone, items, page, query, sortType);
        }

        @Override
        public PaginatedState<AbiphoneNPC> withItems(List<AbiphoneNPC> items) {
            return new State(abiphone, items, page, query, sortType);
        }

        public State withSortType(SortType sortType) {
            return new State(abiphone, items, page, query, sortType);
        }
    }

    public enum SortType {
        FIRST_ADDED,
        ALPHABETICAL,
        LAST_CALLED,
        MOST_CALLED,
        DO_NOT_DISTURB_FIRST
    }

}

