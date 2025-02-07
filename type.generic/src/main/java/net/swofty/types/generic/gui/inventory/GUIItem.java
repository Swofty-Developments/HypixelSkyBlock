package net.swofty.types.generic.gui.inventory;

import lombok.Getter;
import lombok.Setter;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.entity.Player;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class GUIItem {
    @Getter
    private final int slot;
    @Getter
    @Setter
    private ItemStack item;
    @Getter
    private final List<List<String>> stateRequirements;
    private final BiFunction<ClickContext, ItemStack, Boolean> onClick;
    @Getter
    private final Consumer<SkyBlockPlayer> onPostClick;
    @Getter
    private final String id;
    @Getter
    @Setter
    private long attachedTimestamp;

    private GUIItem(Builder builder) {
        this.slot = builder.slot;
        this.item = builder.item;
        this.stateRequirements = builder.stateRequirements;
        this.onClick = builder.onClick;
        this.onPostClick = builder.onPostClick;
        this.id = builder.id;
        this.attachedTimestamp = System.currentTimeMillis();
    }

    public boolean isVisible(Set<String> currentStates) {
        if (stateRequirements.isEmpty()) return true;
        return stateRequirements.stream()
                .anyMatch(stateGroup -> currentStates.containsAll(stateGroup));
    }

    public boolean handleClickAndShouldAllow(ClickContext context, ItemStack clickedItem) {
        boolean result = onClick != null && onClick.apply(context, clickedItem);
        if (result && onPostClick != null) {
            onPostClick.accept(context.player());
        }
        return result;
    }

    public static Builder builder(int slot) {
        return new Builder(slot);
    }

    public static class Builder {
        private final int slot;
        private ItemStack item = ItemStack.AIR;
        private final List<List<String>> stateRequirements = new ArrayList<>();
        private BiFunction<ClickContext, ItemStack, Boolean> onClick;
        private Consumer<SkyBlockPlayer> onPostClick;
        private String id;

        private Builder(int slot) {
            this.slot = slot;
        }

        public Builder item(ItemStack item) {
            this.item = item;
            return this;
        }

        public Builder item(Supplier<ItemStack> item) {
            this.item = item.get();
            return this;
        }

        public Builder item(ItemStack.Builder item) {
            this.item = item.build();
            return this;
        }

        public Builder requireState(String state) {
            this.stateRequirements.add(List.of(state));
            return this;
        }

        public Builder requireStates(String... states) {
            this.stateRequirements.add(List.of(states));
            return this;
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder requireAnyState(String... states) {
            for (String state : states) {
                this.stateRequirements.add(List.of(state));
            }
            return this;
        }

        public Builder onClick(BiFunction<ClickContext, ItemStack, Boolean> handler) {
            this.onClick = handler;
            return this;
        }

        public Builder onPostClick(Consumer<SkyBlockPlayer> handler) {
            this.onPostClick = handler;
            return this;
        }

        public GUIItem build() {
            if (id == null) {
                id = UUID.randomUUID().toString();
            }
            return new GUIItem(this);
        }
    }

    public record ClickContext(ItemStack cursorItem, SkyBlockPlayer player, ClickType clickType) {}
}