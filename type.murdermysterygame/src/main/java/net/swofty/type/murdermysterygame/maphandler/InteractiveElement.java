package net.swofty.type.murdermysterygame.maphandler;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.swofty.type.murdermysterygame.game.Game;
import net.swofty.type.murdermysterygame.user.MurderMysteryPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public record InteractiveElement(
        String id,
        InteractionType type,
        List<Point> positions,
        int goldCost,
        InteractiveAction action
) {
    public static Builder builder(String id) {
        return new Builder(id);
    }

    public boolean matchesPosition(Point blockPos) {
        return positions.stream().anyMatch(p ->
                (int) p.x() == blockPos.blockX() &&
                        (int) p.y() == blockPos.blockY() &&
                        (int) p.z() == blockPos.blockZ()
        );
    }

    @FunctionalInterface
    public interface InteractiveAction {
        void execute(PlayerBlockInteractEvent event,
                     MurderMysteryPlayer player,
                     Game game,
                     MapHandler handler);
    }

    public static class Builder {
        private final String id;
        private InteractionType type = InteractionType.BUTTON;
        private final List<Point> positions = new ArrayList<>();
        private int goldCost = 0;
        private InteractiveAction action;

        public Builder(String id) {
            this.id = id;
        }

        public Builder type(InteractionType type) {
            this.type = type;
            return this;
        }

        public Builder position(double x, double y, double z) {
            this.positions.add(new Vec(x, y, z));
            return this;
        }

        public Builder positions(Point... points) {
            this.positions.addAll(Arrays.asList(points));
            return this;
        }

        public Builder goldCost(int cost) {
            this.goldCost = cost;
            return this;
        }

        public Builder action(InteractiveAction action) {
            this.action = action;
            return this;
        }

        public InteractiveElement build() {
            return new InteractiveElement(id, type, positions, goldCost, action);
        }
    }
}
