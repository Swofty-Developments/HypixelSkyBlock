package net.swofty.type.generic.gui.v2;

import lombok.Getter;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.inventory.InventoryClickEvent;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.event.trait.InventoryEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.PlayerInventory;
import net.minestom.server.item.ItemStack;
import net.minestom.server.event.inventory.InventoryOpenEvent;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.type.generic.gui.v2.context.ClickContext;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.generic.user.HypixelPlayer;

import java.time.Duration;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public final class ViewSession<S> {

    @Getter
    @Accessors(fluent = true)
    private final View<S> view;

    @Getter
    @Accessors(fluent = true)
    private final HypixelPlayer player;

    @Getter
    @Accessors(fluent = true)
    private final Inventory inventory;

    @Getter
    @Accessors(fluent = true)
    private final SharedContext<S> sharedContext;

    @Getter
    @Accessors(fluent = true)
    private final ViewContext context;
    private final EventNode<InventoryEvent> eventNode;

    @Getter
    @Accessors(fluent = true)
    private S state;

    private ViewLayout<S> cachedLayout;
    private Consumer<CloseReason> onCloseHandler;

    @Getter
    private boolean closed;
    private boolean suppressCloseEvent;

    private final Map<Integer, ItemStack> trackedSlotItems = new HashMap<>();
    private final Set<Integer> recentlyModifiedSlots = new HashSet<>();
    private final Map<Integer, Task> autoUpdateTasks = new HashMap<>();
    private final Map<UUID, Set<Integer>> componentSlots = new HashMap<>();

    private ViewSession(View<S> view, HypixelPlayer player, S initialState, SharedContext<S> sharedContext) {
        this.view = view;
        this.player = player;
        this.state = sharedContext != null ? sharedContext.state() : initialState;
        this.sharedContext = sharedContext;
        this.inventory = new Inventory(view.configuration().getInventoryType(), "");
        this.context = new ViewContext(player, inventory, this);
        this.eventNode = EventNode.type("gui-" + System.identityHashCode(this), EventFilter.INVENTORY);

        wireEvents();

        if (sharedContext != null) {
            sharedContext.registerSession(this);
        }
    }

    static <S> ViewSession<S> open(View<S> view, HypixelPlayer player, S initialState) {
        var session = new ViewSession<>(view, player, initialState, null);
        session.render();
        player.openInventory(session.inventory);
        return session;
    }

    static <S> ViewSession<S> openShared(View<S> view, HypixelPlayer player, SharedContext<S> sharedContext) {
        var session = new ViewSession<>(view, player, sharedContext.state(), sharedContext);
        session.render();
        player.openInventory(session.inventory);
        return session;
    }

    private void wireEvents() {
        eventNode.addListener(InventoryPreClickEvent.class, this::onPreClickEvent);
        eventNode.addListener(InventoryClickEvent.class, this::onPostClickEvent);
        eventNode.addListener(InventoryCloseEvent.class, this::onCloseEvent);
        eventNode.addListener(InventoryOpenEvent.class, this::onOpenEvent);
        MinecraftServer.getGlobalEventHandler().addChild(eventNode);
    }

    private void onOpenEvent(InventoryOpenEvent event) {
        if (event.getInventory() != inventory || closed) return;
        view.onOpen(state, context);
    }

    private void onPreClickEvent(InventoryPreClickEvent event) {
        if (event.getInventory() instanceof PlayerInventory) {
            if (player.getOpenInventory() == inventory) {
                var click = new ClickContext<>(event.getSlot(), event.getClick(), player, state);
                if (!view.onBottomClick(click, context)) {
                    event.setCancelled(true);
                }
            }
            return;
        }
        if (event.getInventory() != inventory || closed) return;

        int slot = event.getSlot();
        SlotBehavior behavior = cachedLayout != null ? cachedLayout.getBehavior(slot) : SlotBehavior.UI;

        if (behavior == SlotBehavior.EDITABLE) {
            trackedSlotItems.put(slot, inventory.getItemStack(slot));
            return;
        }

        event.setCancelled(true);
        ViewComponent<S> component = cachedLayout != null ? cachedLayout.components().get(slot) : null;
        var click = new ClickContext<>(slot, event.getClick(), player, state);

        if (component == null) {
            view.onClick(click, context);
        } else {
            component.onClick().accept(click, context);
        }
    }

    private void onPostClickEvent(InventoryClickEvent event) {
        if (event.getInventory() != inventory || closed) return;

        int slot = event.getSlot();
        if (cachedLayout == null || !cachedLayout.isEditable(slot)) return;

        MinecraftServer.getSchedulerManager().scheduleNextTick(() -> {
            if (closed) return;

            ItemStack oldItem = trackedSlotItems.getOrDefault(slot, ItemStack.AIR);
            ItemStack newItem = inventory.getItemStack(slot);

            if (!oldItem.equals(newItem)) {
                handleSlotChange(slot, oldItem, newItem);
            }
        });
    }

    private void handleSlotChange(int slot, ItemStack oldItem, ItemStack newItem) {
        trackedSlotItems.put(slot, newItem);
        recentlyModifiedSlots.add(slot);

        ViewComponent<S> component = cachedLayout.components().get(slot);
        if (component != null && component.changeHandler() != null) {
            component.changeHandler().onChange(slot, oldItem, newItem, state);
        }

        if (sharedContext != null) {
            sharedContext.setSlotItem(slot, newItem);
        } else {
            render();
        }

        recentlyModifiedSlots.remove(slot);
    }

    private void onCloseEvent(InventoryCloseEvent event) {
        if (event.getInventory() != inventory || suppressCloseEvent) {
            suppressCloseEvent = false;
            return;
        }
        close(CloseReason.PLAYER_EXITED);
    }

    public void render() {
        if (closed) return;

        ViewConfiguration<?> config = view.configuration();
        cachedLayout = new ViewLayout<>(config.getInventoryType());
        view.layout(cachedLayout, state, context);
        componentSlots.clear();

        @SuppressWarnings("unchecked") BiFunction<S, ViewContext, Component> titleFunction = (BiFunction<S, ViewContext, Component>) config.getTitleFunction();
        inventory.setTitle(titleFunction.apply(state, context));

        cachedLayout.components().forEach((slot, component) -> {
            if (component.behavior() == SlotBehavior.EDITABLE) {
                renderEditableSlot(slot);
                return;
            }

            renderSlot(slot, component);
            if (component.updateInterval() != null) {
                scheduleAutoUpdate(slot, component);
            }
        });

        view.onRefresh(state, context);
    }

    private void renderEditableSlot(int slot) {
        if (sharedContext != null) {
            ItemStack contextItem = sharedContext.getSlotItem(slot);
            if (!inventory.getItemStack(slot).equals(contextItem)) {
                inventory.setItemStack(slot, contextItem);
            }
            trackedSlotItems.put(slot, contextItem);
        } else if (!recentlyModifiedSlots.contains(slot)) {
            ItemStack currentItem = inventory.getItemStack(slot);
            trackedSlotItems.put(slot, currentItem);
        }
    }

    private void renderSlot(int slot, ViewComponent<S> component) {
        var item = component.render().apply(state, context).build();
        if (!inventory.getItemStack(slot).equals(item)) {
            inventory.setItemStack(slot, item);
        }

        if (component.id() != null) {
            componentSlots.computeIfAbsent(component.id(), _ -> new HashSet<>()).add(slot);
        }
    }

    private void scheduleAutoUpdate(int slot, ViewComponent<S> component) {
        if (autoUpdateTasks.containsKey(slot)) return;

        Task task = MinecraftServer.getSchedulerManager().submitTask(() -> {
            if (closed) return TaskSchedule.stop();
            renderSlot(slot, component);
            return TaskSchedule.duration(component.updateInterval());
        });
        autoUpdateTasks.put(slot, task);
    }

    public void setState(S newState) {
        this.state = newState;

        if (sharedContext != null) {
            sharedContext.setState(newState);
        } else {
            render();
        }
    }

    public void setStateQuiet(S newState) {
        this.state = newState;
        render();
    }

    public void update(UnaryOperator<S> transform) {
        setState(transform.apply(state));
    }

    public void updateQuiet(UnaryOperator<S> transform) {
        setStateQuiet(transform.apply(state));
    }

    @SuppressWarnings("unchecked")
    public <T> void updateUnchecked(java.util.function.Function<T, T> transform) {
        setState((S) transform.apply((T) state));
    }

    void setStateFromShared(S newState) {
        this.state = newState;
        render();
    }

    void renderFromShared() {
        render();
    }

    public ViewSession<S> onClose(Consumer<CloseReason> handler) {
        this.onCloseHandler = handler;
        return this;
    }

    public ViewSession<S> refreshEvery(Duration interval) {
        MinecraftServer.getSchedulerManager().submitTask(() -> {
            if (closed) return TaskSchedule.stop();
            render();
            return TaskSchedule.millis(interval.toMillis());
        });
        return this;
    }

    public void close(CloseReason reason) {
        if (closed) return;
        if (reason == CloseReason.REPLACED) {
            suppressCloseEvent = true;
        }
        closed = true;

        autoUpdateTasks.values().forEach(Task::cancel);
        autoUpdateTasks.clear();

        MinecraftServer.getGlobalEventHandler().removeChild(eventNode);
        if (sharedContext != null) {
            sharedContext.unregisterSession(this);
        }

        view.onClose(state, context, reason);
        if (onCloseHandler != null) onCloseHandler.accept(reason);
        if (reason == CloseReason.SERVER_EXITED) player.closeInventory();
    }

    public boolean isShared() {
        return sharedContext != null;
    }


    public enum CloseReason {
        PLAYER_EXITED,
        SERVER_EXITED,
        REPLACED
    }
}
