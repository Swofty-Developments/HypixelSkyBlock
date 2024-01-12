package net.swofty.types.generic.minion;

import lombok.Getter;
import lombok.Setter;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.types.generic.entity.MinionEntityImpl;
import net.swofty.types.generic.entity.hologram.ServerHolograms;
import net.swofty.types.generic.item.MaterialQuantifiable;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public record MinionHandler(Scheduler scheduler) {
    public void start() {
        scheduler.submitTask(() -> {
            minionLoop();
            return TaskSchedule.tick(3);
        }, ExecutionType.SYNC);
    }

    private void minionLoop() {
        MinionEntityImpl.getActiveMinions().forEach(minionEntity -> {
            SkyBlockMinion minion = minionEntity.getMinion();
            IslandMinionData.IslandMinion islandMinion = minionEntity.getIslandMinion();
            InternalMinionTags tags = islandMinion.getInternalMinionTags();
            Instance instance = minionEntity.getInstance();
            MinionAction action = minion.getAction();

            // Check if the minion is in a perfect location
            boolean allBlocksMeetExpectations = true;
            for (SkyBlockMinion.MinionExpectations expectation : minion.getExpectations()) {
                int yLevel = minionEntity.getPosition().blockY() + expectation.yLevel();
                List<Pos> positions = getRange(minionEntity.getPosition().withY(yLevel), 2);

                for (Pos position : positions) {
                    if (!Arrays.asList(expectation.material()).contains(instance.getBlock(position))) {
                        allBlocksMeetExpectations = false;
                        break;
                    }
                }
            }

            if (!allBlocksMeetExpectations) {
                // To not overload with replacing holograms if not needed
                if (tags.getState() == InternalMinionTags.STATE.NOT_PERFECT) return;

                if (tags.getAssociatedHologram() != null) {
                    ServerHolograms.removeExternalHologram(tags.getAssociatedHologram());
                }

                ServerHolograms.ExternalHologram hologram = ServerHolograms.ExternalHologram.builder()
                        .pos(minionEntity.getPosition())
                        .instance(instance)
                        .text(new String[]{"§c>!<", "§cThis location isn't perfect! :("})
                        .build();

                tags.setState(InternalMinionTags.STATE.NOT_PERFECT);
                tags.setAssociatedHologram(hologram);
                ServerHolograms.addExternalHologram(hologram);
                return;
            }

            SkyBlockMinion.MinionTier tier = minion.getTiers().get(islandMinion.getTier() - 1);

            // Check if the minion is full
            List<MaterialQuantifiable> itemsInMinion = islandMinion.getItemsInMinion();
            if (itemsInMinion.size() == tier.getSlots() && action.checkMaterials(
                    islandMinion,
                    instance
            )) {
                // To not overload with replacing holograms if not needed
                if (tags.getState() == InternalMinionTags.STATE.NOT_PERFECT) return;

                if (tags.getAssociatedHologram() != null) {
                    ServerHolograms.removeExternalHologram(tags.getAssociatedHologram());
                }

                ServerHolograms.ExternalHologram hologram = ServerHolograms.ExternalHologram.builder()
                        .pos(minionEntity.getPosition())
                        .instance(instance)
                        .text(new String[]{"§c>!<", "§cMy storage is full! :("})
                        .build();

                tags.setState(InternalMinionTags.STATE.NOT_PERFECT);
                tags.setAssociatedHologram(hologram);
                ServerHolograms.addExternalHologram(hologram);
                return;
            }

            // If the minion is in a perfect location, remove any previous not perfect holograms
            if (tags.getState() == InternalMinionTags.STATE.NOT_PERFECT) {
                ServerHolograms.removeExternalHologram(tags.getAssociatedHologram());
                tags.setState(InternalMinionTags.STATE.IDLE);
            }
        });
    }

    @Getter
    @Setter
    public static class InternalMinionTags {
        private ServerHolograms.ExternalHologram associatedHologram;
        private STATE state = STATE.IDLE;

        public void onMinionDespawn(IslandMinionData.IslandMinion minion) {
            if (associatedHologram != null)
                ServerHolograms.removeExternalHologram(associatedHologram);
        }

        enum STATE {
            IDLE,
            NOT_PERFECT
        }
    }

    public static List<Pos> getRange(Pos pos, int range) {
        List<Pos> positions = new ArrayList<>();
        for (int x = pos.blockX() - range; x < pos.blockX() + range; x++) {
            for (int z = pos.blockZ() - range; z < pos.blockZ() + range; z++) {
                positions.add(new Pos(x, pos.blockY(), z));
            }
        }
        // Remove the center position
        positions.remove(pos.withX(pos.blockX()).withZ(pos.blockZ()));
        return positions;
    }
}
