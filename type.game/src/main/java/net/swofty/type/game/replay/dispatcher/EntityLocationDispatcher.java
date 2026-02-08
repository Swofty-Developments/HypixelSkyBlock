package net.swofty.type.game.replay.dispatcher;

import net.minestom.server.entity.Entity;
import net.minestom.server.instance.Instance;
import net.swofty.type.game.replay.ReplayRecorder;
import net.swofty.type.game.replay.recordable.RecordableEntityLocations;

import java.util.HashMap;
import java.util.Map;

public class EntityLocationDispatcher implements ReplayDispatcher {
    private ReplayRecorder recorder;
    private Instance instance;

    private final Map<Integer, CachedLocation> lastLocations = new HashMap<>();

    private static final double POSITION_THRESHOLD = 0.01;
    private static final float ROTATION_THRESHOLD = 0.5f;

    public EntityLocationDispatcher(Instance instance) {
        this.instance = instance;
    }

    @Override
    public void initialize(ReplayRecorder recorder) {
        this.recorder = recorder;
    }

    @Override
    public void tick() {
        RecordableEntityLocations locations = new RecordableEntityLocations();

        for (Entity entity : instance.getEntities()) {
            int entityId = entity.getEntityId();

            double x = entity.getPosition().x();
            double y = entity.getPosition().y();
            double z = entity.getPosition().z();
            float yaw = entity.getPosition().yaw();
            float pitch = entity.getPosition().pitch();
            boolean onGround = entity.isOnGround();

            CachedLocation cached = lastLocations.get(entityId);
            if (cached == null || hasChanged(cached, x, y, z, yaw, pitch)) {
                locations.addEntry(entityId, x, y, z, yaw, pitch, onGround);
                lastLocations.put(entityId, new CachedLocation(x, y, z, yaw, pitch));
            }
        }

        // Only record if there are changes
        if (!locations.getEntries().isEmpty()) {
            recorder.record(locations);
        }

        // Clean up removed entities
        lastLocations.keySet().removeIf(id ->
                instance.getEntityById(id) == null
        );
    }

    private boolean hasChanged(CachedLocation cached, double x, double y, double z, float yaw, float pitch) {
        return Math.abs(cached.x - x) > POSITION_THRESHOLD ||
                Math.abs(cached.y - y) > POSITION_THRESHOLD ||
                Math.abs(cached.z - z) > POSITION_THRESHOLD ||
                Math.abs(cached.yaw - yaw) > ROTATION_THRESHOLD ||
                Math.abs(cached.pitch - pitch) > ROTATION_THRESHOLD;
    }

    @Override
    public void cleanup() {
        lastLocations.clear();
    }

    @Override
    public String getName() {
        return "EntityLocation";
    }

    private record CachedLocation(double x, double y, double z, float yaw, float pitch) {}
}
