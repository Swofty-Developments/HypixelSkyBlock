package net.swofty.type.skyblockgeneric.event.actions.player;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.instance.block.Block;
import net.swofty.commons.ServerType;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.skyblockgeneric.mission.MissionData;
import net.swofty.type.skyblockgeneric.mission.MissionSet;
import net.swofty.type.skyblockgeneric.mission.missions.MissionUseTeleporter;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.skyblockgeneric.utility.WarpPortal;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ActionPlayerTravel implements HypixelEventClass {
    public static List<UUID> delay = new ArrayList<>();

    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = true)
    public void run(PlayerMoveEvent event) {
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        if (delay.contains(player.getUuid())) return;
        delay.add(player.getUuid());

        MinecraftServer.getSchedulerManager().buildTask(() -> delay.remove(player.getUuid()))
            .delay(Duration.ofMillis(500))
            .schedule();

        Key block = player.getInstance().getBlock(player.getPosition()).key();
        if (block == Block.NETHER_PORTAL.key()) {
            MissionData data = player.getMissionData();

            if (!MissionSet.GETTING_STARTED.hasCompleted(player)
                && !data.isCurrentlyActive(MissionUseTeleporter.class)
            ) {
                player.sendMessage(I18n.t("commands.hub.must_complete_missions"));
                return;
            }

            player.sendTo(HypixelConst.getTypeLoader().getType() == ServerType.SKYBLOCK_HUB ? ServerType.SKYBLOCK_DUNGEON_HUB : ServerType.SKYBLOCK_HUB);
        }

        if (block == Block.END_PORTAL.key()) {
            player.sendTo(ServerType.SKYBLOCK_ISLAND);
        }

        WarpPortal.getWarpPortals().forEach(warp -> {
            // distanceSquared is a micro-optimization
            if (event.getNewPosition().distanceSquared(warp.vector().asPos()) < 2) {
                player.teleport(warp.pos());
                player.playSound(
                    Sound.sound(
                        Key.key("minecraft:entity.enderman.teleport"),
                        Sound.Source.PLAYER,
                        1f, 1f
                    )
                );
                player.sendMessage("§dWarped to §b" + LegacyComponentSerializer.legacySection().serialize(warp.text()) + "§d!");
            }
        });
    }
}
