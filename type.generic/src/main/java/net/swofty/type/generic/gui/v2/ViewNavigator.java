package net.swofty.type.generic.gui.v2;

import lombok.Getter;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class ViewNavigator {

    private static final Map<HypixelPlayer, ViewNavigator> NAVIGATORS = new ConcurrentHashMap<>();

    private final HypixelPlayer player;
    private final Deque<NavigationEntry<?>> stack = new ArrayDeque<>();
    @Getter
    private ViewSession<?> currentSession;

    private ViewNavigator(HypixelPlayer player) {
        this.player = player;
    }

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

        ViewSession<S> session = ViewSession.open(view, player, state);
        this.currentSession = session;

        session.onClose(reason -> {
            if (reason == ViewSession.CloseReason.PLAYER_EXITED) {
                clear();
            }
        });

        return session;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void pushCurrentToStack() {
        if (currentSession == null) return;
        ViewSession<?> session = currentSession;
        stack.push(new NavigationEntry(session.view(), session.state()));
    }

    public <S> ViewSession<S> replace(View<S> view, S state) {
        if (currentSession != null && !currentSession.isClosed()) {
            currentSession.close(ViewSession.CloseReason.REPLACED);
        }

        ViewSession<S> session = ViewSession.open(view, player, state);
        this.currentSession = session;

        session.onClose(reason -> {
            if (reason == ViewSession.CloseReason.PLAYER_EXITED) {
                clear();
            }
        });

        return session;
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
        ViewSession<?> session = openEntry(entry);
        this.currentSession = session;

        session.onClose(reason -> {
            if (reason == ViewSession.CloseReason.PLAYER_EXITED) {
                clear();
            }
        });

        return true;
    }

    private <S> ViewSession<S> openEntry(NavigationEntry<S> entry) {
        return ViewSession.open(entry.view(), player, entry.state());
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

    public <S> Optional<NavigationEntry<S>> peekPrevious() {
        return Optional.ofNullable((NavigationEntry<S>) stack.peek());
    }

    record NavigationEntry<S>(View<S> view, S state) {}

    private static <S> View<S> getView(ViewSession<S> session) {
        return session.view();
    }
}

