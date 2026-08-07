package net.swofty.type.ravengardgeneric.event.actions.player;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.instance.Instance;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.event.phase.EventPhase;
import net.swofty.type.generic.event.phase.PhasedEvent;
import net.swofty.type.ravengardgeneric.RavengardGenericLoader;
import net.swofty.type.ravengardgeneric.hud.RavengardHudState;
import net.swofty.type.ravengardgeneric.user.RavengardPlayer;
import org.tinylog.Logger;

public class ActionPlayerEnterWorld implements HypixelEventClass {
    private static final int WORLD_MOVE_DELAY_TICKS = 20;


    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = true, phase = EventPhase.POST_SPAWN)
    public void run(PlayerSpawnEvent event) {
        if (!event.isFirstSpawn()) {
            return;
        }
        if (!(event.getPlayer() instanceof RavengardPlayer player)) {
            return;
        }

        player.setGameMode(net.minestom.server.entity.GameMode.SURVIVAL);
        player.getAttribute(net.minestom.server.entity.attribute.Attribute.MAX_HEALTH)
                .setBaseValue(RavengardHudState.VANILLA_HEARTS_HEALTH);
        player.setHealth(RavengardHudState.VANILLA_HEARTS_HEALTH);

        net.swofty.type.ravengardgeneric.profile.RavengardProfiles.announce(player);

        // A player who has not picked a class yet starts the tutorial aboard the Nevermore
        boolean tutorial = player.isTutorial() || player.getRavengardClass() == null;

        Instance world = tutorial ? RavengardGenericLoader.tutorialInstance : HypixelConst.getInstanceContainer();
        if (world == null || player.getInstance() == world) {
            return;
        }

        Pos spawn = tutorial
                ? new Pos(25.5, 64, 508.5, -90f, 0f)
                : HypixelConst.getTypeLoader()
                .getLoaderValues()
                .spawnPosition()
                .apply(player.getOriginServer());

        net.minestom.server.MinecraftServer.getSchedulerManager().buildTask(() -> moveIntoWorld(player, world, spawn))
                .delay(net.minestom.server.timer.TaskSchedule.tick(WORLD_MOVE_DELAY_TICKS))
                .schedule();
    }

    private static void moveIntoWorld(RavengardPlayer player, Instance world, Pos spawn) {
        if (!player.isOnline()) {
            return;
        }

        player.setInstance(world, spawn).whenComplete((ignored, throwable) -> {
            if (throwable != null) {
                Logger.error(throwable, "Failed moving {} into the Ravengard world", player.getUsername());
                return;
            }
            player.teleport(spawn);
            Logger.info("Moved {} from limbo instance into the Ravengard world", player.getUsername());
        });
    }
}
