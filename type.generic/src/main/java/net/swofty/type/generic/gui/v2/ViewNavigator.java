package net.swofty.type.generic.gui.v2;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@ToString
@RequiredArgsConstructor
public final class ViewNavigator {

    private static final Map<HypixelPlayer, ViewNavigator> NAVIGATORS = new ConcurrentHashMap<>();

    private final HypixelPlayer player;
    private final Deque<NavigationEntry<?>> stack = new ArrayDeque<>();

    @Getter
    private ViewSession<?> currentSession;

    public static ViewNavigator get(HypixelPlayer player) {
        return NAVIGATORS.computeIfAbsent(player, ViewNavigator::new);
    }

    public static Optional<ViewNavigator> find(HypixelPlayer player) {
        return Optional.ofNullable(NAVIGATORS.get(player));
    }

    public static void remove(HypixelPlayer player) {
        ViewNavigator nav = NAVIGATORS.remove(player);
        if (nav != null) {
            nav.clear();
        }
    }

    public <S> ViewSession<S> push(View<S> view, S state) {
        if (currentSession != null && !currentSession.isClosed()) {
            pushCurrentToStack();
            currentSession.close(ViewSession.CloseReason.REPLACED);
        }

        var session = ViewSession.open(view, player, state);
        this.currentSession = session;
        registerCloseHandler(session);
        return session;
    }

    public <S> ViewSession<S> push(View<S> view) {
        S state = resolveInitialState(view);
        return push(view, state);
    }

    public <S> ViewSession<S> pushShared(View<S> view, String contextId, S initialState) {
        if (currentSession != null && !currentSession.isClosed()) {
            pushCurrentToStack();
            currentSession.close(ViewSession.CloseReason.REPLACED);
        }

        SharedContext<S> ctx = SharedContext.getOrCreate(contextId, initialState);
        var session = ViewSession.openShared(view, player, ctx);
        this.currentSession = session;
        registerCloseHandler(session);
        return session;
    }

    public <S> ViewSession<S> joinShared(View<S> view, String contextId) {
        return SharedContext.<S>get(contextId).map(ctx -> {
            if (currentSession != null && !currentSession.isClosed()) {
                pushCurrentToStack();
                currentSession.close(ViewSession.CloseReason.REPLACED);
            }
            var session = ViewSession.openShared(view, player, ctx);
            this.currentSession = session;
            registerCloseHandler(session);
            return session;
        }).orElseThrow(() -> new IllegalArgumentException("Shared context not found: " + contextId));
    }

    public <S> ViewSession<S> pushShared(View<S> view, SharedContext<S> sharedContext) {
        if (currentSession != null && !currentSession.isClosed()) {
            pushCurrentToStack();
            currentSession.close(ViewSession.CloseReason.REPLACED);
        }

        var session = ViewSession.openShared(view, player, sharedContext);
        this.currentSession = session;
        registerCloseHandler(session);
        return session;
    }

    public <S> ViewSession<S> replace(View<S> view, S state) {
        if (currentSession != null && !currentSession.isClosed()) {
            currentSession.close(ViewSession.CloseReason.REPLACED);
        }

        var session = ViewSession.open(view, player, state);
        this.currentSession = session;
        registerCloseHandler(session);
        return session;
    }

    public <S> ViewSession<S> replace(View<S> view) {
        S state = resolveInitialState(view);
        return replace(view, state);
    }

    public boolean pop() {
        if (stack.isEmpty()) {
            if (currentSession != null && !currentSession.isClosed()) {
                currentSession.close(ViewSession.CloseReason.PLAYER_EXITED);
            }
            currentSession = null;
            return false;
        }

        if (currentSession != null && !currentSession.isClosed()) {
            currentSession.close(ViewSession.CloseReason.REPLACED);
        }

        NavigationEntry<?> entry = stack.pop();
        var session = openEntry(entry);
        this.currentSession = session;
        registerCloseHandler(session);
        return true;
    }

    public void popTo(int levels) {
        for (int i = 0; i < levels - 1 && !stack.isEmpty(); i++) {
            stack.pop();
        }
        pop();
    }

    public void popToRoot() {
        while (stack.size() > 1) {
            stack.pop();
        }
        pop();
    }

    public void clear() {
        stack.clear();
        if (currentSession != null && !currentSession.isClosed()) {
            currentSession.close(ViewSession.CloseReason.SERVER_EXITED);
        }
        currentSession = null;
    }

    public boolean hasStack() {
        return !stack.isEmpty();
    }

    public int depth() {
        return stack.size();
    }

    @SuppressWarnings("unchecked")
    public <S> Optional<NavigationEntry<S>> peekPrevious() {
        return Optional.ofNullable((NavigationEntry<S>) stack.peek());
    }

    public HypixelPlayer player() {
        return player;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void pushCurrentToStack() {
        if (currentSession == null) return;
        ViewSession<?> session = currentSession;
        stack.push(new NavigationEntry(session.view(), session.state()));
    }

    private <S> ViewSession<S> openEntry(NavigationEntry<S> entry) {
        return ViewSession.open(entry.view(), player, entry.state());
    }

    private void registerCloseHandler(ViewSession<?> session) {
        session.onClose(reason -> {
            if (reason == ViewSession.CloseReason.PLAYER_EXITED) {
                clear();
            }
        });
    }

    private <S> S resolveInitialState(View<S> view) {
        if (view instanceof StatefulView<S> stateful) {
            return stateful.initialState();
        }
        return null;
    }

    public record NavigationEntry<S>(View<S> view, S state) {
    }
}
