package net.swofty.type.bedwarsgame.item.impl;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.type.bedwarsgame.game.v2.BedWarsGame;
import net.swofty.type.bedwarsgame.item.SimpleInteractableItem;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.game.game.GameState;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class LeaveGameBed extends SimpleInteractableItem {

    private static Map<UUID, Task> leaveTasks = new ConcurrentHashMap<>();

    public LeaveGameBed() {
        super("leave_game");
    }

    @Override
    public ItemStack getBlandItem() {
        return ItemStackCreator.getStack("§c§lReturn to Lobby §7(Right Click)", Material.RED_BED, 1, "§7Right-click to leave to the lobby!").build();
    }

    @Override
    public void onItemUse(PlayerUseItemEvent event) {
        BedWarsGame game = ((BedWarsPlayer) event.getPlayer()).getGame();
        if (game != null) {
            if (leaveTasks.containsKey(event.getPlayer().getUuid())) {
                leaveTasks.get(event.getPlayer().getUuid()).cancel();
                leaveTasks.remove(event.getPlayer().getUuid());
                event.getPlayer().sendMessage("§c§lTeleport cancelled!");
                return;
            }
            leaveTasks.put(event.getPlayer().getUuid(), MinecraftServer.getSchedulerManager().scheduleTask(() -> {
                if (game.getState() != GameState.IN_PROGRESS) {
                    leaveTasks.remove(event.getPlayer().getUuid());
                    game.leave((BedWarsPlayer) event.getPlayer());
                }
                return TaskSchedule.stop();
            }, TaskSchedule.seconds(3L)));
            event.getPlayer().sendMessage("§a§lTeleporting you to the lobby in 3 seconds... Right-click again to cancel the teleport!");
        }
    }
}
