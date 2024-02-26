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

public class ServerOrbImpl extends LivingEntity {
    private final String url;
    private final Material toPlace;
    private Task upAndDownTask;
    private Task particleTask;
    private Task cropsTask;
    @Getter
    private float yLevel = 0f;

    public ServerOrbImpl(@NotNull Material toPlace, @NotNull String url) {
        super(EntityType.ARMOR_STAND);

        this.toPlace = toPlace;
        this.url = url;
    }

    @Override
    public void spawn() {
        super.spawn();

        List<Pos> farmland = MathUtility.getNearbyBlocks(getInstance(),
                getPosition(),
                18,
                Material.FARMLAND.block());

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
            yLevel = (getYLevel() < 0D) ? 0.1f : -0.1f;
            teleport(getPosition().add(0, yLevel, 0));
        }, TaskSchedule.tick(15), TaskSchedule.tick(8));
        particleTask = MinecraftServer.getSchedulerManager().scheduleTask(() -> {
            if (isDead()) {
                particleTask.cancel();
                return;
            }
            Pos location = getPosition().withYaw(getPosition().yaw() + 15.0f);
            teleport(location);
        }, TaskSchedule.tick(1), TaskSchedule.tick(3));
        cropsTask = MinecraftServer.getSchedulerManager().scheduleTask(() -> {
            if (isDead()) {
                cropsTask.cancel();
            }
            if (farmland.isEmpty()) return;

            Map<Block, Pos> possible = new HashMap<>();
            for (Pos block : farmland) {
                Block a = instance.getBlock(block.add(0, 1, 0));
                if (a.isAir()) {
                    possible.put(a, block);
                }
            }
            if (possible.isEmpty()) return;

            Block block = MathUtility.getRandomElement(new ArrayList<>(possible.keySet()));
            if (block == null) return;

            instance.setBlock(possible.get(block).add(0, 1, 0),
                    toPlace.block().withProperty("age", "7"));

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
                            new byte[]{}
                    ));
                });
            }
        }, TaskSchedule.tick(30), TaskSchedule.tick(30));
    }
}
