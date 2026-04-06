package net.swofty.type.skyblockgeneric.event.actions.player;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.instance.block.Block;
import net.swofty.commons.ServerType;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.skyblockgeneric.mission.MissionData;
import net.swofty.type.skyblockgeneric.mission.MissionSet;
import net.swofty.type.skyblockgeneric.mission.missions.MissionUseTeleporter;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ActionPlayerTravel implements HypixelEventClass {
    public static List<UUID> delay = new ArrayList<>();
    private static final double[][] PORTAL_CHECK_OFFSETS = {
            {0, 0, 0},
            {0, -1, 0},
            {0, 1, 0},
            {0.3, 0, 0},
            {-0.3, 0, 0},
            {0, 0, 0.3},
            {0, 0, -0.3},
            {0.3, 1, 0},
            {-0.3, 1, 0},
            {0, 1, 0.3},
            {0, 1, -0.3}
    };

    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = true)
    public void run(PlayerMoveEvent event) {
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        if (delay.contains(player.getUuid())) return;

        if (isTouchingBlock(player, Block.NETHER_PORTAL)) {
            MissionData data = player.getMissionData();

            delay.add(player.getUuid());

            MinecraftServer.getSchedulerManager().buildTask(() -> delay.remove(player.getUuid()))
                    .delay(Duration.ofMillis(500))
                    .schedule();

            if (!MissionSet.GETTING_STARTED.hasCompleted(player)
                    && !data.isCurrentlyActive(MissionUseTeleporter.class)
            ) {
                player.sendMessage("§cYou must complete your starting missions!");
                return;
            }

            player.sendTo(HypixelConst.getTypeLoader().getType() == ServerType.SKYBLOCK_HUB ? ServerType.SKYBLOCK_DUNGEON_HUB : ServerType.SKYBLOCK_HUB);
            return;
        }

        if (isTouchingBlock(player, Block.END_PORTAL)) {
            player.sendTo(ServerType.SKYBLOCK_ISLAND);
            delay.add(player.getUuid());

            MinecraftServer.getSchedulerManager().buildTask(() -> delay.remove(player.getUuid()))
                    .delay(Duration.ofMillis(500))
                    .schedule();
        }
    }

    private boolean isTouchingBlock(SkyBlockPlayer player, Block expectedBlock) {
        for (double[] offset : PORTAL_CHECK_OFFSETS) {
            Block block = player.getInstance().getBlock(player.getPosition().add(offset[0], offset[1], offset[2]));
            if (block.key().equals(expectedBlock.key())) {
                return true;
            }
        }
        return false;
    }
}
