package net.swofty.type.skyblockgeneric.event.actions.player;

import net.kyori.adventure.key.Key;
import org.tinylog.Logger;
import net.kyori.adventure.sound.Sound;
import org.tinylog.Logger;
import net.minestom.server.coordinate.Pos;
import org.tinylog.Logger;
import net.minestom.server.coordinate.Vec;
import org.tinylog.Logger;
import net.minestom.server.entity.EntityType;
import org.tinylog.Logger;
import net.minestom.server.entity.LivingEntity;
import org.tinylog.Logger;
import net.minestom.server.event.player.PlayerMoveEvent;
import org.tinylog.Logger;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import org.tinylog.Logger;
import net.minestom.server.particle.Particle;
import org.tinylog.Logger;
import net.swofty.type.generic.event.EventNodes;
import org.tinylog.Logger;
import net.swofty.type.generic.event.HypixelEvent;
import org.tinylog.Logger;
import net.swofty.type.generic.event.HypixelEventClass;
import org.tinylog.Logger;
import net.swofty.type.generic.utility.MathUtility;
import org.tinylog.Logger;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import org.tinylog.Logger;
import net.swofty.type.skyblockgeneric.utility.LaunchPads;
import org.tinylog.Logger;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ActionPlayerLaunchPads implements HypixelEventClass {
    private static final int SEGMENTS = 30;
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);


    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = true, isAsync = true)
    public void run(PlayerMoveEvent event) {
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        LaunchPads pad;
        try {
            pad = LaunchPads.getLaunchPadInRange(player.getPosition(), 2);
        } catch (ExceptionInInitializerError err) {
            return;
        }
        if (pad == null) return;
        if (player.isInLaunchpad()) return;
        player.setInLaunchpad(true);

        boolean accepted = pad.getShouldAllow().apply(player);
        if (!accepted && player.getRank().isStaff()) {
            accepted = true;
            player.getLogHandler().debug("As a staff member, you have bypassed the launchpad requirement.");
        }

        player.sendPacket(new ParticlePacket(
                Particle.EXPLOSION,
                false,
                false,
                (float) player.getPosition().x(),
                (float) player.getPosition().y(),
                (float) player.getPosition().z(),
                0,
                0,
                0,
                0,
                1
        ));
        player.playSound(Sound.sound(Key.key("entity.generic.explode"), Sound.Source.PLAYER, 1, 1));

        if (!accepted) {
            player.setVelocity(player.getPosition().direction().mul(-30).withY(5));
            player.sendMessage(pad.getRejectionMessage());
            player.setInLaunchpad(false);
            return;
        }
        player.playSound(Sound.sound(Key.key("entity.firework_rocket.launch"), Sound.Source.PLAYER, 1, 1));

        LivingEntity armorStand = new LivingEntity(EntityType.ARMOR_STAND);
        armorStand.getEntityMeta().setInvisible(true);
        armorStand.getEntityMeta().setHasNoGravity(true);
        armorStand.setInstance(player.getInstance(), player.getPosition());
        armorStand.addPassenger(player);

        List<Pos> curve = MathUtility.bezierCurve(player.getPosition(), pad.getDestination(), SEGMENTS);
        long timeToSleep = 3000 / SEGMENTS;

        // Use ScheduledExecutorService to run the launch trajectory with delays
        AtomicInteger index = new AtomicInteger(0);
        ScheduledFuture<?>[] taskHolder = new ScheduledFuture<?>[1];
        taskHolder[0] = scheduler.scheduleAtFixedRate(() -> {
            int currentIndex = index.getAndIncrement();
            if (currentIndex >= curve.size()) {
                // Cancel the task
                if (taskHolder[0] != null) {
                    taskHolder[0].cancel(false);
                }
                // Execute the after finished callback
                player.sendMessage("Done");
                pad.getAfterFinished().accept(player);
                return;
            }

            Pos pos = curve.get(currentIndex);
            Vec toGoTo = pos.asVec();
            Vec direction = toGoTo.sub(player.getPosition().asVec()).normalize();
            armorStand.setVelocity(direction.mul(50, 5, 50));
        }, 0, timeToSleep, TimeUnit.MILLISECONDS);
    }

}
