package net.swofty.type.bedwarsgame.game.v2;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig;
import net.swofty.commons.game.event.PlayerRespawnCompleteEvent;
import net.swofty.commons.game.event.PlayerRespawnStartEvent;
import net.swofty.commons.game.respawn.RespawnHandler;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.generic.event.HypixelEventHandler;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@RequiredArgsConstructor
public class BedWarsRespawnHandler implements RespawnHandler<BedWarsPlayer> {
    private static final int DEFAULT_RESPAWN_DELAY = 5;

    private final BedWarsGame game;
    private final Map<UUID, Task> respawnTasks = new ConcurrentHashMap<>();

    @Override
    public boolean canRespawn(UUID playerId) {
        return game.getPlayerTeam(playerId)
            .map(BedWarsTeam::isBedAlive)
            .orElse(false);
    }

    @Override
    public int getRespawnDelay(UUID playerId) {
        return DEFAULT_RESPAWN_DELAY;
    }

    @Override
    public void startRespawn(BedWarsPlayer player) {
        if (!canRespawn(player.getUuid())) {
            game.onPlayerEliminated(player);
            return;
        }

        // Put in spectator mode
        player.setGameMode(GameMode.SPECTATOR);
        player.getInventory().clear();

        // Teleport to spectator position
        BedWarsMapsConfig.Position spectatorPos = game.getMapEntry()
            .getConfiguration().getLocations().getSpectator();
        if (spectatorPos != null) {
            player.teleport(new Pos(spectatorPos.x(), spectatorPos.y(), spectatorPos.z()));
        }

        // Fire event
        HypixelEventHandler.callCustomEvent(new PlayerRespawnStartEvent(
            game.getGameId(),
            player.getUuid(),
            player.getUsername(),
            getRespawnDelay(player.getUuid())
        ));

        // Start countdown
        AtomicInteger countdown = new AtomicInteger(getRespawnDelay(player.getUuid()));
        AtomicReference<Task> taskRef = new AtomicReference<>();

        Task task = MinecraftServer.getSchedulerManager().buildTask(() -> {
            if (!player.isOnline()) {
                cancelRespawn(player.getUuid());
                return;
            }

            int remaining = countdown.getAndDecrement();

            if (remaining > 0) {
                showRespawnTitle(player, remaining);
            } else {
                completeRespawn(player);
                Task t = taskRef.get();
                if (t != null) t.cancel();
                respawnTasks.remove(player.getUuid());
            }
        }).repeat(TaskSchedule.seconds(1)).schedule();

        taskRef.set(task);
        respawnTasks.put(player.getUuid(), task);
    }

    @Override
    public void cancelRespawn(UUID playerId) {
        Task task = respawnTasks.remove(playerId);
        if (task != null) {
            task.cancel();
        }
    }

    @Override
    public boolean isRespawning(UUID playerId) {
        return respawnTasks.containsKey(playerId);
    }

    private void showRespawnTitle(BedWarsPlayer player, int seconds) {
        Title.Times times = Title.Times.times(
            Duration.ofMillis(100),
            Duration.ofSeconds(1),
            Duration.ofMillis(100)
        );

        Title title = Title.title(
            Component.text("YOU DIED!", NamedTextColor.RED),
            Component.text("You will respawn in " + seconds + " second" + (seconds == 1 ? "" : "s") + "!", NamedTextColor.YELLOW),
            times
        );

        player.showTitle(title);
    }

    private void completeRespawn(BedWarsPlayer player) {
        player.clearTitle();

        // TODO: do this behavior inside of an EventListener
        game.getPlayerTeam(player.getUuid()).ifPresent(team -> {
            BedWarsMapsConfig.MapTeam teamConfig = game.getMapEntry()
                .getConfiguration().getTeams().get(team.getTeamKey());

            if (teamConfig != null) {
                // TODO: restore upgrades
                BedWarsMapsConfig.PitchYawPosition spawn = teamConfig.getSpawn();
                player.teleport(new Pos(spawn.x(), spawn.y(), spawn.z(), spawn.yaw(), spawn.pitch()));

                player.setGameMode(GameMode.SURVIVAL);
                player.setInvisible(false);
                player.setFlying(false);
                player.getInventory().addItemStack(ItemStack.of(Material.WOODEN_SWORD));
            }
        });

        // Fire event
        HypixelEventHandler.callCustomEvent(new PlayerRespawnCompleteEvent(
            game.getGameId(),
            player.getUuid(),
            player.getUsername()
        ));
    }

    /**
     * Equips team armor to a player.
     */
    public void equipTeamArmor(BedWarsPlayer player, BedWarsMapsConfig.TeamKey teamKey) {
        // Get armor level from team upgrades
        int armorLevel = game.getTeam(teamKey.name())
            .map(team -> team.getUpgradeLevel("armor"))
            .orElse(0);

        // Apply colored leather armor based on team
        Material helmet = Material.LEATHER_HELMET;
        Material chestplate = Material.LEATHER_CHESTPLATE;
        Material leggings = Material.LEATHER_LEGGINGS;
        Material boots = Material.LEATHER_BOOTS;

        // Upgrade materials based on armor level
        switch (armorLevel) {
            case 1 -> {
                leggings = Material.CHAINMAIL_LEGGINGS;
                boots = Material.CHAINMAIL_BOOTS;
            }
            case 2 -> {
                leggings = Material.IRON_LEGGINGS;
                boots = Material.IRON_BOOTS;
            }
            case 3 -> {
                leggings = Material.DIAMOND_LEGGINGS;
                boots = Material.DIAMOND_BOOTS;
            }
        }

        player.setHelmet(ItemStack.of(helmet));
        player.setChestplate(ItemStack.of(chestplate));
        player.setLeggings(ItemStack.of(leggings));
        player.setBoots(ItemStack.of(boots));
    }
}
