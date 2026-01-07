package net.swofty.type.generic.gui.v2;

import lombok.Getter;
import lombok.experimental.Accessors;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.event.trait.InventoryEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.type.generic.gui.v2.context.ClickContext;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.generic.user.HypixelPlayer;

import java.time.Duration;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public final class GuiSession<S> {

    private final View<S> view;
	@Getter
	@Accessors(fluent = true)
    private final HypixelPlayer player;
	@Getter
	@Accessors(fluent = true)
    private final Inventory inventory;
    private final ViewContext context;
    private final EventNode<InventoryEvent> eventNode;

    @Getter
    @Accessors(fluent = true)
    private S state;
    private S previousState;
    private GuiLayout<S> cachedLayout;
    private Consumer<CloseReason> onCloseHandler;
    @Getter
	private boolean closed;

    private GuiSession(View<S> view, HypixelPlayer player, S initialState) {
        this.view = view;
        this.player = player;
        this.state = initialState;
        this.inventory = new Inventory(view.size(), "");
        this.context = new ViewContext(player, inventory, this);
        this.eventNode = EventNode.type("gui-" + System.identityHashCode(this), EventFilter.INVENTORY);

        wireEvents();
    }

    public static <S> GuiSession<S> open(View<S> view, HypixelPlayer player, S initialState) {
        GuiSession<S> session = new GuiSession<>(view, player, initialState);
        session.render();
        player.openInventory(session.inventory);
        return session;
    }

    private void wireEvents() {
        eventNode.addListener(InventoryPreClickEvent.class, this::onClickEvent);
        eventNode.addListener(InventoryCloseEvent.class, this::onCloseEvent);
        MinecraftServer.getGlobalEventHandler().addChild(eventNode);
    }

    private void onClickEvent(InventoryPreClickEvent event) {
        if (event.getInventory() != inventory || closed) return;
        event.setCancelled(true);

        ViewComponent<S> component = cachedLayout.components().get(event.getSlot());
        if (component == null) return;

        ClickContext<S> click = new ClickContext<>(event.getSlot(), event.getClick(), player, state);
        component.onClick().accept(click, context);
    }

    private void onCloseEvent(InventoryCloseEvent event) {
        if (event.getInventory() != inventory) return;
        close(CloseReason.PLAYER_EXITED);
    }

    public void render() {
        if (closed) return;

        cachedLayout = new GuiLayout<>(view.size());
        view.layout(cachedLayout, state, context);

        if (!Objects.equals(state, previousState)) {
            inventory.setTitle(view.title(state, context));
            previousState = state;
        }

        cachedLayout.components().forEach((slot, component) -> {
            var item = component.render().apply(state, context).build();
            if (!inventory.getItemStack(slot).equals(item)) {
                inventory.setItemStack(slot, item);
            }
        });
    }

    public void setState(S newState) {
        if (Objects.equals(state, newState)) return;
        this.state = newState;
        render();
    }

    public void update(UnaryOperator<S> transform) {
        setState(transform.apply(state));
    }

    @SuppressWarnings("unchecked")
    public <T> void updateUnchecked(Function<T, T> transform) {
        setState(((Function<S, S>) transform).apply(state));
    }

    public GuiSession<S> onClose(Consumer<CloseReason> handler) {
        this.onCloseHandler = handler;
        return this;
    }

    public GuiSession<S> refreshEvery(Duration interval) {
        MinecraftServer.getSchedulerManager().submitTask(() -> {
            if (closed) return TaskSchedule.stop();
            render();
            return TaskSchedule.millis(interval.toMillis());
        });
        return this;
    }

    public void close(CloseReason reason) {
        if (closed) return;
        closed = true;

        MinecraftServer.getGlobalEventHandler().removeChild(eventNode);
        if (onCloseHandler != null) onCloseHandler.accept(reason);
        if (reason != CloseReason.PLAYER_EXITED) player.closeInventory();
    }

	public enum CloseReason {
        PLAYER_EXITED,
        SERVER_EXITED,
        REPLACED
    }
}


