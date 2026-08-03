package net.swofty.type.ravengarddungeon.interactables;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.type.ravengardgeneric.item.RavengardItem;
import net.swofty.type.ravengardgeneric.item.RavengardItemRegistry;
import net.swofty.type.ravengardgeneric.item.RavengardItemType;

import java.util.List;
import java.util.Random;

public final class DungeonChest extends DungeonInteractable {
    private static final String BOTTOM_MODEL = "hypixel_ravengard:item/gameplay/chests/normal/normal_bottom";
    private static final String TOP_MODEL = "hypixel_ravengard:item/gameplay/chests/normal/normal_top";
    private static final String TITLE_GLYPHS = new String(Character.toChars(0xCFFD0)) + "";
    private static final int LID_TICKS = 5;
    private static final int[] LOOT_SLOTS = {10, 11, 12, 13, 14, 15, 16, 20, 22, 24};

    private final Entity bottom;
    private final Entity lid;
    private final Pos base;
    private final Random random;

    private DungeonChest(Instance instance, Pos base, Random random) {
        super(instance);
        this.base = base;
        this.random = random;
        this.interaction = spawnInteraction(base, 1.1f, 1.1f);
        this.bottom = spawnDisplay(base.add(0, 0.5, 0), BOTTOM_MODEL);
        this.lid = spawnDisplay(base.add(0, 0.5, 0), TOP_MODEL);
    }

    public static DungeonChest spawn(Instance instance, Pos base, Random random) {
        DungeonChest chest = new DungeonChest(instance, base, random);
        InteractableRegistry.register(chest);
        return chest;
    }

    @Override
    public String castLabel() {
        return "Opening Chest";
    }

    @Override
    public void open(Player player) {
        opened = true;
        double radians = Math.toRadians(base.yaw());
        Vec back = new Vec(Math.sin(radians), 0, -Math.cos(radians));
        for (int step = 1; step <= LID_TICKS; step++) {
            int tick = step;
            MinecraftServer.getSchedulerManager().buildTask(() ->
                    lid.teleport(base.add(0, 0.5, 0)
                            .add(back.mul(0.0625 * tick))
                            .add(0, 0.1375 * tick, 0))
            ).delay(TaskSchedule.tick(step)).schedule();
        }

        Inventory inventory = new Inventory(InventoryType.CHEST_3_ROW,
                Component.text(TITLE_GLYPHS, NamedTextColor.WHITE));
        List<RavengardItemType> pool = RavengardItemRegistry.all();
        int rolls = 2 + random.nextInt(2);
        for (int roll = 0; roll < rolls && !pool.isEmpty(); roll++) {
            RavengardItemType type = pool.get(random.nextInt(pool.size()));
            inventory.setItemStack(LOOT_SLOTS[random.nextInt(LOOT_SLOTS.length)],
                    RavengardItem.of(type.getId()));
        }
        InteractableRegistry.watchClose(inventory, closer -> explode());
        MinecraftServer.getSchedulerManager().buildTask(() -> player.openInventory(inventory))
                .delay(TaskSchedule.tick(LID_TICKS + 1)).schedule();
    }

    private void explode() {
        Pos center = base.add(0, 0.6, 0);
        instance.sendGroupedPacket(new ParticlePacket(Particle.POOF,
                center.x(), center.y(), center.z(), 0.4f, 0.4f, 0.4f, 0.02f, 24));
        bottom.remove();
        lid.remove();
        remove();
    }
}
