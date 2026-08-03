package net.swofty.type.ravengarddungeon.interactables;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.metadata.display.ItemDisplayMeta;
import net.minestom.server.instance.Instance;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.type.ravengardgeneric.item.RavengardItem;
import net.swofty.type.ravengardgeneric.item.RavengardItemRegistry;
import net.swofty.type.ravengardgeneric.item.RavengardItemType;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public final class DungeonSatchel extends DungeonInteractable {
    private static final String CLOSED_MODEL = "hypixel_ravengard:item/gameplay/lootbags/lootbag_2";
    private static final String OPEN_MODEL = "hypixel_ravengard:item/gameplay/lootbags/lootbag_2_open";
    private static final String SKULL_MODEL = "hypixel_ravengard:item/gameplay/trinkets/crystal_skull/sprite";
    private static final int[] LOOT_SLOTS = {29, 30, 31, 32, 33};

    private final Entity bag;
    private final Pos base;
    private Task skullTask;

    private DungeonSatchel(Instance instance, Pos base) {
        super(instance);
        this.base = base;
        this.interaction = spawnInteraction(base.sub(0, 0.5, 0), 0.5f, 0.65f);
        this.bag = spawnDisplay(base, CLOSED_MODEL);
    }

    public static DungeonSatchel spawn(Instance instance, Pos position) {
        Pos base = position.add(0, 0.5, 0);
        DungeonSatchel satchel = new DungeonSatchel(instance, base);
        instance.sendGroupedPacket(new ParticlePacket(Particle.CAMPFIRE_COSY_SMOKE,
                base.x(), base.y(), base.z(), 0.25f, 0.2f, 0.25f, 0.01f, 14));
        satchel.skullTask = MinecraftServer.getSchedulerManager()
                .buildTask(satchel::riseSkull)
                .delay(TaskSchedule.tick(15)).repeat(TaskSchedule.tick(24)).schedule();
        InteractableRegistry.register(satchel);
        return satchel;
    }

    private void riseSkull() {
        if (opened) return;
        double jitterX = ThreadLocalRandom.current().nextDouble(-0.2, 0.2);
        double jitterZ = ThreadLocalRandom.current().nextDouble(-0.2, 0.2);
        Entity skull = spawnDisplay(base.add(jitterX, 0.2, jitterZ), SKULL_MODEL);
        for (int step = 1; step <= 10; step++) {
            int tick = step;
            MinecraftServer.getSchedulerManager().buildTask(() ->
                    skull.teleport(base.add(jitterX, 0.2 + 0.12 * tick, jitterZ))
            ).delay(TaskSchedule.tick(step * 2)).schedule();
        }
        MinecraftServer.getSchedulerManager().buildTask(skull::remove)
                .delay(TaskSchedule.tick(24)).schedule();
    }

    @Override
    public boolean openOnLook() {
        return false;
    }

    @Override
    public void open(Player player) {
        opened = true;
        if (skullTask != null) {
            skullTask.cancel();
        }
        bag.editEntityMeta(ItemDisplayMeta.class, meta -> meta.setItemStack(modelStack(OPEN_MODEL)));

        Inventory inventory = new Inventory(InventoryType.CHEST_5_ROW, Component.text("Dead Body"));
        Random random = ThreadLocalRandom.current();
        List<RavengardItemType> pool = RavengardItemRegistry.all();
        int rolls = 1 + random.nextInt(3);
        for (int roll = 0; roll < rolls && !pool.isEmpty(); roll++) {
            RavengardItemType type = pool.get(random.nextInt(pool.size()));
            inventory.setItemStack(LOOT_SLOTS[random.nextInt(LOOT_SLOTS.length)],
                    RavengardItem.of(type.getId()));
        }
        InteractableRegistry.watchClose(inventory, closer -> {
            instance.sendGroupedPacket(new ParticlePacket(Particle.POOF,
                    base.x(), base.y(), base.z(), 0.25f, 0.25f, 0.25f, 0.02f, 12));
            bag.remove();
            remove();
        });
        player.openInventory(inventory);
    }
}
