package net.swofty.type.generic.event.custom;

import lombok.Getter;
import lombok.Setter;
import net.minestom.server.entity.Player;
import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import net.swofty.type.generic.entity.DragonEntity;
import net.swofty.type.generic.user.HypixelPlayer;
import org.jspecify.annotations.NonNull;

@Getter
public class DragonHitEvent implements PlayerInstanceEvent, CancellableEvent {
    private boolean cancelled = false;
    private final HypixelPlayer player;
    private final DragonEntity dragon;
    @Setter
    private float damage;

    public DragonHitEvent(HypixelPlayer player, DragonEntity dragon, float damage) {
        this.player = player;
        this.dragon = dragon;
        this.damage = damage;
    }

    @Override
    public @NonNull Player getPlayer() {
        return player;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
