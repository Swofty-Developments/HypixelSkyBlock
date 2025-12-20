package net.swofty.anticheat.engine;

import lombok.Getter;
import net.swofty.anticheat.api.AnticheatAPI;
import net.swofty.anticheat.event.SwoftyEventHandler;
import net.swofty.anticheat.event.events.PlayerPositionUpdateEvent;
import net.swofty.anticheat.event.packet.RequestPingPacket;
import net.swofty.anticheat.event.packet.SwoftyPacket;
import net.swofty.anticheat.flag.FlagManager;
import net.swofty.anticheat.flag.FlagType;
import net.swofty.anticheat.loader.SwoftyAnticheat;
import net.swofty.anticheat.math.Pos;
import net.swofty.anticheat.math.Vel;
import net.swofty.anticheat.world.PlayerWorld;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

@Getter
public class SwoftyPlayer {
    public static final Map<UUID, SwoftyPlayer> players = new ConcurrentHashMap<>();
    private final UUID uuid;

    private List<PlayerTickInformation> lastTicks = new ArrayList<>();
    private PlayerTickInformation currentTick = new PlayerTickInformation(new Pos(0, 0, 0), new Vel(0, 0, 0), false);
    private final PlayerWorld world = new PlayerWorld();

    private final LinkedBlockingDeque<PingRequest> pingRequests = new LinkedBlockingDeque<>();
    private long ping;
    private long lastPingResponse;
    private final FlagManager flagManager;

    // Player ability state (from AbilitiesPacket)
    private boolean flying = false;
    private boolean allowFlight = false;
    private boolean creativeMode = false;

    public SwoftyPlayer(UUID uuid) {
        this.uuid = uuid;
        this.flagManager = new FlagManager(uuid, new HashMap<>());

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
        currentTick.setPing(ping);

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

    public int ticksSinceLastPingResponse() {
        return (int) (System.currentTimeMillis() - lastPingResponse) / 1000;
    }

    public void sendPingRequest() {
        // Between 1 and 50000000
        int randomId = new Random().nextInt(50000000) + 1;

        PingRequest request = new PingRequest(randomId);
        pingRequests.offerLast(request);
        if (pingRequests.size() > SwoftyAnticheat.getValues().getTicksAllowedToMissPing()) {
            pingRequests.pollFirst();
        }

        // Send ping packet to client
        sendPacket(new RequestPingPacket(this, randomId));
    }

    public void handlePingResponse(long id) {
        long currentTime = System.currentTimeMillis();
        for (PingRequest request : pingRequests) {
            if (request.getPingId() == id) {
                request.setTime(currentTime);
                updatePing();
                break;
            }
        }
    }

    private void updatePing() {
        long totalPing = 0;
        int count = 0;
        lastPingResponse = System.currentTimeMillis();
        for (PingRequest request : pingRequests) {
            if (request.getTime() != -1) {
                totalPing += (request.getTime() - request.getInitiatedTime());
                count++;
            }
        }
        if (count > 0) {
            ping = Math.max(0, (totalPing / count) - SwoftyAnticheat.getValues().getTickLength());
        }
    }

    public void flag(FlagType flagType, double certainty) {
        if (lastTicks.size() == 1) return; // We need at least 2 ticks to flag

        // Check if player has bypass for this flag type
        if (AnticheatAPI.hasBypass(uuid, flagType)) {
            return;
        }

        flagManager.addFlag(flagType, certainty);
    }

    public void processMovement(@NotNull Pos packetPosition, boolean onGround) {
        currentTick = new PlayerTickInformation(
                packetPosition,
                currentTick.getVel(),
                onGround
        );
    }

    public void sendPacket(SwoftyPacket packet) {
        SwoftyAnticheat.getLoader().sendPacket(uuid, packet);
    }

    public void updateAbilities(boolean flying, boolean allowFlight, boolean creativeMode) {
        this.flying = flying;
        this.allowFlight = allowFlight;
        this.creativeMode = creativeMode;
    }

    public boolean shouldBypassMovementChecks() {
        return flying || creativeMode || allowFlight;
    }
}
