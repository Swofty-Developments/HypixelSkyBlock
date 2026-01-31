package net.swofty.type.bedwarsgame.replay;

import net.minestom.server.instance.block.Block;
import net.swofty.commons.replay.ReplayRecorder;
import net.swofty.commons.replay.dispatcher.ReplayDispatcher;
import net.swofty.commons.replay.recordable.Recordable;
import net.swofty.commons.replay.recordable.RecordableBlockChange;
import net.swofty.commons.replay.recordable.RecordablePlayerArmSwing;
import net.swofty.commons.replay.recordable.RecordablePlayerDeath;
import net.swofty.commons.replay.recordable.RecordablePlayerRespawn;
import net.swofty.commons.replay.recordable.RecordablePlayerSneak;
import net.swofty.commons.replay.recordable.RecordablePlayerSprint;
import net.swofty.type.bedwarsgame.game.v2.BedWarsGame;
import net.swofty.type.bedwarsgame.game.v2.BedWarsTeam;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;

import java.util.ArrayList;
import java.util.List;

public class BedWarsEventDispatcher implements ReplayDispatcher {
    private final BedWarsGame game;
    private ReplayRecorder recorder;

    private final List<Recordable> pendingRecordables = new ArrayList<>();

    public BedWarsEventDispatcher(BedWarsGame game, ReplayRecorder recorder) {
        this.game = game;
        this.recorder = recorder;
    }

    @Override
    public void initialize(ReplayRecorder recorder) {
        this.recorder = recorder;
        registerListeners();
    }

    private void registerListeners() {
        // TODO: These would be registered with the game's event system
        // For now, we'll provide methods that can be called directly

        // Block place/break would typically be listened to via Minestom events
        // But we'd want to filter to only record player-caused changes
    }

    /**
     * Records a block place event.
     */
    public void onBlockPlace(BedWarsPlayer player, int x, int y, int z, Block block) {
        pendingRecordables.add(new RecordableBlockChange(
                x, y, z,
                block.stateId(),
                Block.AIR.stateId()
        ));
    }

    /**
     * Records a block break event.
     */
    public void onBlockBreak(BedWarsPlayer player, int x, int y, int z, Block previousBlock) {
        pendingRecordables.add(new RecordableBlockChange(
                x, y, z,
                Block.AIR.stateId(),
                previousBlock.stateId()
        ));
    }

    /**
     * Records a bed destruction.
     */
    public void onBedDestroyed(BedWarsPlayer destroyer, BedWarsTeam team, int x, int y, int z) {
        // Record as block change
        pendingRecordables.add(new RecordableBlockChange(x, y, z, Block.AIR.stateId(), Block.RED_BED.stateId()));

        // Could also record a custom BedWars-specific event if desired
    }

    /**
     * Records a player death.
     */
    public void onPlayerDeath(BedWarsPlayer player, BedWarsPlayer killer, String message) {
        pendingRecordables.add(new RecordablePlayerDeath(
                player.getEntityId(),
                killer != null ? killer.getEntityId() : -1,
                message
        ));
    }

    /**
     * Records a player respawn.
     */
    public void onPlayerRespawn(BedWarsPlayer player, double x, double y, double z) {
        pendingRecordables.add(new RecordablePlayerRespawn(
                player.getEntityId(),
                x, y, z,
                player.getPosition().yaw(),
                player.getPosition().pitch()
        ));
    }

    /**
     * Records player sneaking.
     */
    public void onPlayerSneak(BedWarsPlayer player, boolean sneaking) {
        pendingRecordables.add(new RecordablePlayerSneak(player.getEntityId(), sneaking));
    }

    /**
     * Records player sprinting.
     */
    public void onPlayerSprint(BedWarsPlayer player, boolean sprinting) {
        pendingRecordables.add(new RecordablePlayerSprint(player.getEntityId(), sprinting));
    }

    /**
     * Records arm swing.
     */
    public void onPlayerSwing(BedWarsPlayer player, boolean mainHand) {
        pendingRecordables.add(new RecordablePlayerArmSwing(player.getEntityId(), mainHand));
    }

    @Override
    public void tick() {
        if (!pendingRecordables.isEmpty()) {
            recorder.recordAll(pendingRecordables);
            pendingRecordables.clear();
        }
    }

    @Override
    public void cleanup() {
        pendingRecordables.clear();
    }

    @Override
    public String getName() {
        return "BedWarsEvent";
    }
}
