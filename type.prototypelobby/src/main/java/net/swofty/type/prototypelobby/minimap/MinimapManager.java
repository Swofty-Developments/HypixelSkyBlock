package net.swofty.type.prototypelobby.minimap;

import lombok.Getter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.component.DataComponents;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.network.packet.server.play.MapDataPacket;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.concurrent.ConcurrentHashMap;

public class MinimapManager {
    private static final int UPDATE_INTERVAL_TICKS = 20;

    @Getter
    private static MinimapManager instance;

    private final ConcurrentHashMap<Player, Boolean> enabledPlayers = new ConcurrentHashMap<>();
    private final MinimapRenderer renderer = new MinimapRenderer();

    public MinimapManager() {
        instance = this;
    }

    public void start() {
        MinecraftServer.getSchedulerManager().buildTask(() -> {
            for (var entry : enabledPlayers.entrySet()) {
                Player player = entry.getKey();
                if (!player.isOnline() || player.getInstance() == null) {
                    enabledPlayers.remove(player);
                    continue;
                }

                Thread.startVirtualThread(() -> {
                    MapDataPacket packet = renderer.render(
                            player.getInstance(),
                            player.getPosition()
                    );
                    player.sendPacket(packet);
                });
            }
        }).repeat(TaskSchedule.tick(UPDATE_INTERVAL_TICKS)).schedule();
    }

    public void enableFor(HypixelPlayer player) {
        enabledPlayers.put(player, true);

        player.setEquipment(EquipmentSlot.OFF_HAND,
                ItemStack.builder(Material.FILLED_MAP)
                        .set(DataComponents.MAP_ID, MinimapRenderer.MINIMAP_MAP_ID)
                        .build()
        );
    }

    public void disableFor(HypixelPlayer player) {
        enabledPlayers.remove(player);
        player.setEquipment(EquipmentSlot.OFF_HAND, ItemStack.AIR);
    }

    public boolean isEnabled(HypixelPlayer player) {
        return enabledPlayers.containsKey(player);
    }

    public void toggle(HypixelPlayer player) {
        if (isEnabled(player)) {
            disableFor(player);
        } else {
            enableFor(player);
        }
    }
}
