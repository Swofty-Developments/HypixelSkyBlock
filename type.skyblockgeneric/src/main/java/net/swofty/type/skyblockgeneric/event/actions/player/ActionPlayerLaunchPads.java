package net.swofty.type.skyblockgeneric.event.actions.player;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.utility.MathUtility;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.skyblockgeneric.utility.LaunchPads;

import java.util.List;

public class ActionPlayerLaunchPads implements HypixelEventClass {
    private static final int SEGMENTS = 30;


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

        // Use Minestom's scheduler to run the launch trajectory on the main thread
        for (Pos pos : curve) {
            Vec toGoTo = pos.asVec();
            Vec direction = toGoTo.sub(player.getPosition().asVec()).normalize();
            armorStand.setVelocity(direction.mul(50, 5, 50));
            try {
                Thread.sleep(timeToSleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Execute the after finished callback on the main thread
        player.sendMessage("Done");
        pad.getAfterFinished().accept(player);
    }

}
