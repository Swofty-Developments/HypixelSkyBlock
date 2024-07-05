package net.swofty.anticheat.engine;

import lombok.Getter;
import net.swofty.anticheat.event.SwoftyEventHandler;
import net.swofty.anticheat.event.events.PlayerPositionUpdateEvent;
import net.swofty.anticheat.flag.FlagType;
import net.swofty.anticheat.loader.PunishmentHandler;
import net.swofty.anticheat.loader.SwoftyAnticheat;
import net.swofty.anticheat.loader.managers.SwoftyPlayerManager;
import net.swofty.anticheat.math.Pos;
import net.swofty.anticheat.math.Vel;
import net.swofty.anticheat.world.PlayerWorld;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Getter
public class SwoftyPlayer {
    public static Map<UUID, SwoftyPlayer> players = new HashMap<>();

    private final UUID uuid;
    private List<PlayerTickInformation> lastTicks = new ArrayList<>();
    private PlayerTickInformation currentTick = new PlayerTickInformation(new Pos(0, 0, 0), new Vel(0, 0, 0), false);
    private PlayerWorld world = new PlayerWorld();

    public SwoftyPlayer(UUID uuid) {
        this.uuid = uuid;

        players.put(uuid, this);
    }

    private void forgetTicks() {
        lastTicks.clear();
        currentTick = null;
    }

    public void moveTickOn() {
        if (currentTick == null) return;

        Pos currentPos = currentTick.getPos();
        Pos lastPos = !lastTicks.isEmpty() ? lastTicks.getLast().getPos() : currentPos;
        Vel calculatedVelocity = currentPos.sub(lastPos).asVel();

        currentTick = new PlayerTickInformation(currentTick.getPos(), calculatedVelocity, currentTick.isOnGround());

        if (lastTicks.isEmpty()) {
            lastTicks.add(currentTick);
            return;
        }

        SwoftyEventHandler.callEvent(new PlayerPositionUpdateEvent(this,
                lastTicks.getLast(),
                currentTick
        ));

        lastTicks.add(currentTick);
        // Remove any ticks that are older than 20 ticks
        if (lastTicks.size() > 20) lastTicks.removeFirst();

        // Resort contexts
        for (int i = 0; i < lastTicks.size(); i++) {
            lastTicks.get(i).updateContext(i == lastTicks.size() - 1 ? null : lastTicks.get(i + 1), i == 0 ? null : lastTicks.get(i - 1));
        }

        currentTick = new PlayerTickInformation(currentTick.getPos(), currentTick.getVel(), currentTick.isOnGround());
    }

    public void flag(FlagType type) {
        if (lastTicks.size() == 1) return; // We need at least 2 ticks to flag
        PunishmentHandler punishmentHandler = SwoftyAnticheat.getPunishmentHandler();
        punishmentHandler.onFlag(uuid, type);
    }

    public void processMovement(@NotNull Pos packetPosition, boolean onGround) {
        currentTick = new PlayerTickInformation(
                packetPosition,
                currentTick.getVel(),
                onGround
        );
    }

    public SwoftyPlayerManager getManager() {
        return SwoftyAnticheat.getLoader().getPlayerManager(uuid);
    }
}
