package net.swofty.anticheat.flag.flags;

import net.swofty.anticheat.event.ListenerMethod;
import net.swofty.anticheat.event.events.PlayerPositionUpdateEvent;
import net.swofty.anticheat.flag.Flag;
import net.swofty.anticheat.math.Pos;
import net.swofty.anticheat.math.Vel;
import net.swofty.anticheat.world.Block;
import net.swofty.anticheat.world.PlayerWorld;

import java.util.concurrent.CompletableFuture;

public class PhaseFlag extends Flag {

    @ListenerMethod
    public void onPlayerPositionUpdate(PlayerPositionUpdateEvent event) {
        Pos currentPos = event.getCurrentTick().getPos();
        PlayerWorld world = event.getPlayer().getWorld();

        // Check blocks at player position
        // Player hitbox is 0.6 wide x 1.8 tall
        checkCollision(event, world, currentPos);
    }

    private void checkCollision(PlayerPositionUpdateEvent event, PlayerWorld world, Pos pos) {
        int blockX = (int) Math.floor(pos.x());
        int blockY = (int) Math.floor(pos.y());
        int blockZ = (int) Math.floor(pos.z());

        // Check blocks at player's position and above (for head)
        CompletableFuture<Block> feetBlock = world.getBlock(blockX, blockY, blockZ);
        CompletableFuture<Block> headBlock = world.getBlock(blockX, blockY + 1, blockZ);

        // Wait for both block checks
        CompletableFuture.allOf(feetBlock, headBlock).thenRun(() -> {
            try {
                Block feet = feetBlock.get();
                Block head = headBlock.get();

                boolean feetSolid = isSolid(feet);
                boolean headSolid = isSolid(head);

                if (feetSolid || headSolid) {
                    // Player is inside a solid block
                    // This could be phase/noclip

                    // Check if player is moving through the block
                    Vel vel = event.getCurrentTick().getVel();
                    double speed = Math.sqrt(vel.x() * vel.x() + vel.z() * vel.z());

                    if (speed > 0.05) {
                        // Moving through solid block = phasing
                        double certainty = Math.min(0.95, 0.7 + speed * 0.5);
                        event.getPlayer().flag(net.swofty.anticheat.flag.FlagType.PHASE, certainty);
                    } else {
                        // Stationary in block could be glitch, lower certainty
                        event.getPlayer().flag(net.swofty.anticheat.flag.FlagType.PHASE, 0.5);
                    }
                }

                // Additional check: Moving through walls
                if (event.getPreviousTick() != null) {
                    Pos prevPos = event.getPreviousTick().getPos();
                    checkWallClipping(event, world, prevPos, pos);
                }

            } catch (Exception e) {
                // Ignore errors in async block fetching
            }
        });
    }

    private void checkWallClipping(PlayerPositionUpdateEvent event, PlayerWorld world, Pos from, Pos to) {
        // Simple raycast between two positions to detect wall clipping
        double dx = to.x() - from.x();
        double dy = to.y() - from.y();
        double dz = to.z() - from.z();

        double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);

        if (distance < 0.1) return; // Too small to check

        // Check midpoint for solid blocks
        double midX = (from.x() + to.x()) / 2;
        double midY = (from.y() + to.y()) / 2;
        double midZ = (from.z() + to.z()) / 2;

        world.getBlock(
            (int) Math.floor(midX),
            (int) Math.floor(midY),
            (int) Math.floor(midZ)
        ).thenAccept(block -> {
            if (isSolid(block)) {
                // Moved through a solid block
                event.getPlayer().flag(net.swofty.anticheat.flag.FlagType.PHASE, 0.8);
            }
        });
    }

    private boolean isSolid(Block block) {
        // Check if block is solid (not air, not liquid, etc.)
        // This would need to be expanded based on actual Block implementation
        // For now, assume any non-null block is solid
        return block != null;
    }
}
