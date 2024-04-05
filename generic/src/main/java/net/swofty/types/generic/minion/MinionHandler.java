package net.swofty.types.generic.minion;

import lombok.Getter;
import lombok.Setter;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.types.generic.SkyBlockGenericLoader;
import net.swofty.types.generic.entity.MinionEntityImpl;
import net.swofty.types.generic.entity.hologram.ServerHolograms;
import net.swofty.types.generic.item.MaterialQuantifiable;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.MathUtility;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.List;

public record MinionHandler(Scheduler scheduler) {
    private static final int STEPS = 7;

    public void start() {
        scheduler.submitTask(() -> {
            try {
                minionLoop();
            }catch (ConcurrentModificationException exception){
                exception.printStackTrace();
            }
            return TaskSchedule.tick(3);
        }, ExecutionType.ASYNC);
    }

    private void minionLoop() {
        MinionEntityImpl.getActiveMinions().forEach(minionEntity -> {
            SkyBlockMinion minion = minionEntity.getMinion();
            IslandMinionData.IslandMinion islandMinion = minionEntity.getIslandMinion();
            InternalMinionTags tags = islandMinion.getInternalMinionTags();
            Instance instance = minionEntity.getInstance();
            MinionAction action = minion.getAction();
            SkyBlockMinion.MinionTier tier = minion.getTiers().get(islandMinion.getTier() - 1);
            long timeBetweenActions = tier.timeBetweenActions() * 1000L;
            long lastAction = islandMinion.getLastAction();

            if (!instance.isChunkLoaded(minionEntity.getPosition().chunkX(), minionEntity.getPosition().chunkZ())) return;

            /** seems like I need this debug every other week, so keep it here for now
            System.out.println("Minion loop");
            System.out.println("Time between actions: " + timeBetweenActions);
            System.out.println("Current time diff: " + (System.currentTimeMillis() - lastAction));
            System.out.println("Tier: " + islandMinion.getTier());
            System.out.println("State: " + tags.getState());
            System.out.println("Step: " + tags.getCurrentStep());*/

            // Check if the minion is in a perfect location
            boolean allBlocksMeetExpectations = true;
            for (SkyBlockMinion.MinionExpectations expectation : minion.getExpectations()) {
                int yLevel = minionEntity.getPosition().blockY() + expectation.yLevel();
                List<Pos> positions = MathUtility.getRangeExcludingSelf(minionEntity.getPosition().withY(yLevel), 2);

                for (Pos position : positions) {
                    if (!Arrays.asList(expectation.material()).contains(instance.getBlock(position))) {
                        allBlocksMeetExpectations = false;
                        break;
                    }
                }
            }

            if (!allBlocksMeetExpectations) {
                // To not overload with replacing holograms if not needed
                if (tags.getState() == InternalMinionTags.State.NOT_PERFECT) return;

                if (tags.getAssociatedHologram() != null) {
                    ServerHolograms.removeExternalHologram(tags.getAssociatedHologram());
                }

                ServerHolograms.ExternalHologram hologram = ServerHolograms.ExternalHologram.builder()
                        .pos(minionEntity.getPosition())
                        .instance(instance)
                        .text(new String[]{"§c>!<", "§cThis location isn't perfect! :("})
                        .build();

                tags.setState(InternalMinionTags.State.NOT_PERFECT);
                tags.setAssociatedHologram(hologram);
                ServerHolograms.addExternalHologram(hologram);
                return;
            }

            // Check if the minion is full
            List<MaterialQuantifiable> itemsInMinion = islandMinion.getItemsInMinion();
            if (itemsInMinion.size() == tier.getSlots() && action.checkMaterials(
                    islandMinion,
                    instance
            )) {
                // To not overload with replacing holograms if not needed
                if (tags.getState() == InternalMinionTags.State.NOT_PERFECT) return;

                if (tags.getAssociatedHologram() != null) {
                    ServerHolograms.removeExternalHologram(tags.getAssociatedHologram());
                }

                ServerHolograms.ExternalHologram hologram = ServerHolograms.ExternalHologram.builder()
                        .pos(minionEntity.getPosition())
                        .instance(instance)
                        .text(new String[]{"§c>!<", "§cMy storage is full! :("})
                        .build();

                tags.setState(InternalMinionTags.State.NOT_PERFECT);
                tags.setAssociatedHologram(hologram);
                ServerHolograms.addExternalHologram(hologram);
                return;
            }

            // If the minion is in a perfect location, remove any previous not perfect holograms
            if (tags.getState() == InternalMinionTags.State.NOT_PERFECT) {
                ServerHolograms.removeExternalHologram(tags.getAssociatedHologram());
                tags.setState(InternalMinionTags.State.IDLE);
            }

            // Handle if minion should be rotating
            if (tags.getState() == InternalMinionTags.State.ROTATING) {
                if (tags.getCurrentStep() == STEPS) {
                    tags.setCurrentStep(0);
                    tags.setState(InternalMinionTags.State.IDLE);
                    islandMinion.setLastAction(System.currentTimeMillis());
                    tags.getAction().run();
                    return;
                }

                Pos point = tags.getPoints().get(tags.getCurrentStep());
                minionEntity.lookAt(point);

                tags.setCurrentStep(tags.getCurrentStep() + 1);
                return;
            }

            if (System.currentTimeMillis() - lastAction < timeBetweenActions) return;

            // Start working
            MinionAction.MinionActionEvent event = new MinionAction.MinionActionEvent();
            tags.setState(InternalMinionTags.State.ROTATING);

            SkyBlockItem item = action.onAction(event, islandMinion, instance);

            Pos toLook = event.getToLook();
            List<Pos> points = MathUtility.lookSteps(minionEntity.getPosition(), toLook, STEPS);

            tags.setPoints(points);
            tags.setCurrentStep(0);
            tags.setAction(event.getAction());

            if (item != null)
                MinionAction.onMinionIteration(islandMinion, minion, item);
        });
    }

    @Getter
    @Setter
    public static class InternalMinionTags {
        private ServerHolograms.ExternalHologram associatedHologram;
        private State state = State.IDLE;
        private int currentStep = 0;
        private List<Pos> points = new ArrayList<>();
        private Runnable action;

        public void onMinionDespawn(IslandMinionData.IslandMinion minion) {
            if (associatedHologram != null)
                ServerHolograms.removeExternalHologram(associatedHologram);
        }

        public enum State {
            IDLE,
            NOT_PERFECT,
            ROTATING
        }
    }
}
