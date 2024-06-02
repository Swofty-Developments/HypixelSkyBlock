package net.swofty.types.generic.entity;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.metadata.other.ArmorStandMeta;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.types.generic.SkyBlockGenericLoader;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.Pet;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.MathUtility;
import org.jetbrains.annotations.NotNull;

public class PetEntityImpl extends LivingEntity {
    private final String url;
    private final SkyBlockPlayer player;
    private final int particle;
    private Task upAndDownTask;
    private Task moveTowardsPlayer;
    @Getter
    private float yLevel = 0f;
    @Getter
    private boolean goingDown = false;

    public PetEntityImpl(@NotNull SkyBlockPlayer player, @NotNull String url, @NotNull SkyBlockItem pet) {
        super(EntityType.ARMOR_STAND);

        this.hasCollision = false;
        this.hasPhysics = false;

        this.player = player;
        this.url = url;
        this.particle = ((Pet) pet.getGenericInstance()).particleId();

        getEntityMeta().setCustomName(Component.text("§8[§7Lvl" +
                pet.getAttributeHandler().getPetData().getAsLevel(pet.getAttributeHandler().getRarity()) + "§8] "
                + pet.getAttributeHandler().getRarity().getColor() + player.getUsername() + "'s " +
                ((Pet) pet.getGenericInstance()).getPetName()));
    }

    @Override
    public void spawn() {
        super.spawn();

        ArmorStandMeta meta = (ArmorStandMeta) getEntityMeta();
        meta.setSmall(true);
        meta.setInvisible(true);
        meta.setHasNoBasePlate(true);
        meta.setHasNoGravity(true);
        meta.setHasNoGravity(true);
        getEntityMeta().setCustomNameVisible(true);

        setHelmet(ItemStackCreator.getStackHead(url).build());

        upAndDownTask = MinecraftServer.getSchedulerManager().scheduleTask(() -> {
            if (isDead() || !player.isOnline()) {
                upAndDownTask.cancel();
                if (!isDead()) {
                    this.kill();
                }
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
                if (player.getInstance() != getInstance()) return;

                player.sendPacket(new ParticlePacket(
                        particle,
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
        }, TaskSchedule.tick(5), TaskSchedule.tick(3));
        moveTowardsPlayer = MinecraftServer.getSchedulerManager().scheduleTask(() -> {
            if (isDead() || !player.isOnline()) {
                moveTowardsPlayer.cancel();
                if (!isDead()) {
                    this.kill();
                }
                return;
            }
            Pos location = getPosition().withYaw(MathUtility.getYawNeededToLookAt(getPosition(), player.getPosition()));
            double distance = getPosition().distance(player.getPosition());

            if (distance > 10) {
                teleport(player.getPosition().add(0, 1.5, 0));
                return;
            }
            if (distance > 3) {
                teleport(location.add(player.getPosition().add(0, 1.5, 0).sub(getPosition()).asVec().normalize().mul(0.7)));
            }
        }, TaskSchedule.tick(5), TaskSchedule.tick(3));
    }
}
