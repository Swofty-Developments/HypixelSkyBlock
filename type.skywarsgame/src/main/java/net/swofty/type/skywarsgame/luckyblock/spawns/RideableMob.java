package net.swofty.type.skywarsgame.luckyblock.spawns;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import net.minestom.server.instance.Instance;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.type.skywarsgame.user.SkywarsPlayer;

import java.time.Duration;

public abstract class RideableMob {

    protected final SkywarsPlayer rider;
    protected final Instance instance;
    protected EntityCreature mount;
    protected boolean active = false;

    public RideableMob(SkywarsPlayer rider, Instance instance) {
        this.rider = rider;
        this.instance = instance;
    }

    public abstract EntityType getEntityType();

    public abstract String getDisplayName();

    public abstract int getDurationSeconds();

    protected void onMountCreated() {
    }

    protected void onMountTick() {
    }

    protected void onMountRemoved() {
    }

    public void spawn(Pos position) {
        if (active) return;

        mount = new EntityCreature(getEntityType());
        mount.setCustomName(Component.text(rider.getUsername() + "'s " + getDisplayName(), NamedTextColor.GOLD));
        mount.setCustomNameVisible(true);

        onMountCreated();

        mount.setInstance(instance, position);
        active = true;

        rider.getVehicle();
        mount.addPassenger(rider);

        rider.sendMessage(Component.text("You are now riding a " + getDisplayName() + "!", NamedTextColor.GOLD));
        rider.sendMessage(Component.text("Sneak to dismount. Duration: " + getDurationSeconds() + " seconds", NamedTextColor.GRAY));

        mount.scheduler().buildTask(() -> {
            if (!active || mount.isRemoved()) return;
            onMountTick();
        }).repeat(TaskSchedule.tick(1)).schedule();

        mount.scheduler().buildTask(this::remove)
                .delay(Duration.ofSeconds(getDurationSeconds()))
                .schedule();
    }

    public void remove() {
        if (!active) return;
        active = false;

        onMountRemoved();

        if (mount != null && !mount.isRemoved()) {
            mount.getPassengers().forEach(Entity::remove);
            mount.remove();
        }

        rider.sendMessage(Component.text("Your " + getDisplayName() + " has expired!", NamedTextColor.GRAY));
    }

    public void handleSneak() {
        if (active && mount != null) {
            remove();
        }
    }

    public boolean isActive() {
        return active;
    }

    public EntityCreature getMount() {
        return mount;
    }
}
