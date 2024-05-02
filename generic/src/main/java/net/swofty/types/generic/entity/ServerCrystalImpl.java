package net.swofty.types.generic.entity;

import lombok.Getter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.metadata.other.ArmorStandMeta;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.types.generic.SkyBlockGenericLoader;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.utility.MathUtility;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ServerCrystalImpl extends LivingEntity {
    private final String url;
    private final Function<ServerCrystalImpl, Block> toPlace;
    private final List<Material> lookingFor;
    private Task upAndDownTask;
    private Task rotationTask;
    private Task cropsTask;
    @Getter
    private float yLevel = 0f;
    @Getter
    private boolean goingDown = false;

    public ServerCrystalImpl(@NotNull Function<ServerCrystalImpl, Block> toPlace,
                             @NotNull String url,
                             List<Material> lookingFor) {
        super(EntityType.ARMOR_STAND);

        this.toPlace = toPlace;
        this.url = url;
        this.lookingFor = lookingFor;
    }

    @Override
    public void spawn() {
        super.spawn();

        List<Pos> landToPlaceOn = new ArrayList<>();
        lookingFor.forEach(materialLooking -> {
            landToPlaceOn.addAll(MathUtility.getNearbyBlocks(getInstance(),
                    getPosition(),
                    18,
                    materialLooking.block()));
        });

        ArmorStandMeta meta = (ArmorStandMeta) getEntityMeta();
        meta.setSmall(true);
        meta.setInvisible(true);
        meta.setHasNoBasePlate(true);
        meta.setHasNoGravity(true);
        meta.setHasNoGravity(true);

        setHelmet(ItemStackCreator.getStackHead(url).build());

        upAndDownTask = MinecraftServer.getSchedulerManager().scheduleTask(() -> {
            if (isDead()) {
                upAndDownTask.cancel();
                return;
            }
            yLevel = goingDown ? yLevel - 0.02f : yLevel + 0.02f;
            if (yLevel >= 0.12f) {
                goingDown = true;
            } else if (yLevel <= -0.12f) {
                goingDown = false;
            }
            teleport(getPosition().add(0, yLevel, 0));
            SkyBlockGenericLoader.getLoadedPlayers().forEach(player -> {
                player.sendPacket(new ParticlePacket(
                        Particle.HAPPY_VILLAGER.id(),
                        false,
                        getPosition().x(),
                        getPosition().y(),
                        getPosition().z(),
                        0.1f,
                        0.1f,
                        0.1f,
                        0f,
                        3,
                        null
                ));
            });
        }, TaskSchedule.tick(15), TaskSchedule.tick(3));
        rotationTask = MinecraftServer.getSchedulerManager().scheduleTask(() -> {
            if (isDead()) {
                rotationTask.cancel();
                return;
            }
            Pos location = getPosition().withYaw(getPosition().yaw() + 15.0f);
            teleport(location);
        }, TaskSchedule.tick(1), TaskSchedule.tick(3));
        cropsTask = MinecraftServer.getSchedulerManager().scheduleTask(() -> {
            if (isDead()) {
                cropsTask.cancel();
            }
            if (landToPlaceOn.isEmpty()) return;

            Map<Block, Pos> possible = new HashMap<>();
            for (Pos block : landToPlaceOn) {
                Block a = instance.getBlock(block.add(0, 1, 0));
                if (a.isAir()) {
                    possible.put(a, block);
                }
            }
            if (possible.isEmpty()) return;

            Block block = MathUtility.getRandomElement(new ArrayList<>(possible.keySet()));
            if (block == null) return;

            Pos position = possible.get(block).add(0, 1, 0);
            Block blockToPlace = toPlace.apply(this);
            try {
                instance.setBlock(position, blockToPlace.withProperty("age", "7"));
            } catch (IllegalArgumentException e) {
                instance.setBlock(position, blockToPlace);
            }

            Pos blockLocation = possible.get(block).add(0, 1, 0);
            Pos crystalLocation = getPosition().add(0, 1, 0);
            Vec direction = blockLocation.asVec().sub(crystalLocation.asVec());

            for (int i = 0; i < 20; i++) {
                Pos pos = crystalLocation.add(direction.mul((double) i / 20));

                SkyBlockGenericLoader.getLoadedPlayers().forEach(player -> {
                    player.sendPacket(new ParticlePacket(
                            38,
                            false,
                            pos.x(),
                            pos.y(),
                            pos.z(),
                            0.1f,
                            0.1f,
                            0.1f,
                            0f,
                            3,
                            null
                    ));
                });
            }
        }, TaskSchedule.tick(30), TaskSchedule.tick(30));
    }
}
