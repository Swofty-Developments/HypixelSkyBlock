package net.swofty.type.generic.gui.v2;

import lombok.Getter;
import lombok.experimental.Accessors;
import net.minestom.server.item.ItemStack;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Consumer;

@Getter
@Accessors(fluent = true)
public final class SharedContext<S> {

    private static final Map<String, SharedContext<?>> CONTEXTS = new ConcurrentHashMap<>();

    private final String id;
    private volatile S state;
    private final Map<Integer, ItemStack> slotItems;
    private final Set<ViewSession<S>> sessions;
    private final List<Consumer<SlotChange>> slotChangeListeners;

    private SharedContext(String id, S initialState) {
        this.id = id;
        this.state = initialState;
        this.slotItems = new ConcurrentHashMap<>();
        this.sessions = new CopyOnWriteArraySet<>();
        this.slotChangeListeners = new CopyOnWriteArrayList<>();
    }

    @SuppressWarnings("unchecked")
    public static <S> SharedContext<S> create(String id, S initialState) {
        return (SharedContext<S>) CONTEXTS.computeIfAbsent(id, k -> new SharedContext<>(id, initialState));
    }

    @SuppressWarnings("unchecked")
    public static <S> SharedContext<S> getOrCreate(String id, S initialState) {
        return (SharedContext<S>) CONTEXTS.computeIfAbsent(id, k -> new SharedContext<>(id, initialState));
    }

    @SuppressWarnings("unchecked")
    public static <S> Optional<SharedContext<S>> get(String id) {
        return Optional.ofNullable((SharedContext<S>) CONTEXTS.get(id));
    }

    public static void remove(String id) {
        SharedContext<?> ctx = CONTEXTS.remove(id);
        if (ctx != null) ctx.closeAll();
    }

    public static boolean exists(String id) {
        return CONTEXTS.containsKey(id);
    }

    public void setState(S newState) {
        this.state = newState;
        broadcast();
    }

    public ItemStack getSlotItem(int slot) {
        return slotItems.getOrDefault(slot, ItemStack.AIR);
    }

    public void setSlotItem(int slot, ItemStack item) {
        ItemStack oldItem = slotItems.put(slot, item);
        if (oldItem == null) oldItem = ItemStack.AIR;

        SlotChange change = new SlotChange(slot, oldItem, item);
        slotChangeListeners.forEach(l -> l.accept(change));

        for (ViewSession<S> session : sessions) {
            if (!session.isClosed()) {
                session.inventory().setItemStack(slot, item);
            }
        }

        broadcastRender();
    }

    public Map<Integer, ItemStack> getAllSlotItems() {
        return new HashMap<>(slotItems);
    }

    public void setSlotItems(Map<Integer, ItemStack> items) {
        items.forEach(this::setSlotItem);
    }

    public void clearSlotItems() {
        Set<Integer> slots = new HashSet<>(slotItems.keySet());
        slotItems.clear();
        for (int slot : slots) {
            for (ViewSession<S> session : sessions) {
                if (!session.isClosed()) {
                    session.inventory().setItemStack(slot, ItemStack.AIR);
                }
            }
        }
        broadcastRender();
    }

    void registerSession(ViewSession<S> session) {
        sessions.add(session);
        slotItems.forEach((slot, item) -> session.inventory().setItemStack(slot, item));
        broadcastRender();
    }

    void unregisterSession(ViewSession<S> session) {
        sessions.remove(session);
        if (sessions.isEmpty()) {
            CONTEXTS.remove(id);
        } else {
            broadcastRender();
        }
    }

    public void broadcast() {
        for (ViewSession<S> session : sessions) {
            if (!session.isClosed()) {
                session.setStateFromShared(state);
            }
        }
    }

    public void broadcastRender() {
        for (ViewSession<S> session : sessions) {
            if (!session.isClosed()) {
                session.renderFromShared();
            }
        }
    }

    public SharedContext<S> onSlotChange(Consumer<SlotChange> listener) {
        slotChangeListeners.add(listener);
        return this;
    }

    public void closeAll() {
        for (ViewSession<S> session : new ArrayList<>(sessions)) {
            session.close(ViewSession.CloseReason.SERVER_EXITED);
        }
    }

    public int sessionCount() {
        return sessions.size();
    }

    public Set<HypixelPlayer> viewers() {
        Set<HypixelPlayer> viewers = new HashSet<>();
        for (ViewSession<S> session : sessions) {
            if (!session.isClosed()) viewers.add(session.player());
        }
        return viewers;
    }

    public void broadcastMessage(String message) {
        viewers().forEach(p -> p.sendMessage(message));
    }

    public record SlotChange(int slot, ItemStack oldItem, ItemStack newItem) {
    }
}
