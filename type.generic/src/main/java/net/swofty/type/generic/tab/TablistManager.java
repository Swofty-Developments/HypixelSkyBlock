package net.swofty.type.generic.tab;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.GameMode;
import net.minestom.server.network.packet.server.play.PlayerInfoRemovePacket;
import net.minestom.server.network.packet.server.play.PlayerInfoUpdatePacket;
import net.minestom.server.network.packet.server.play.TeamsPacket;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.type.generic.HypixelGenericLoader;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public abstract class TablistManager {
    private static Map<HypixelPlayer, List<UUID>> tablistEntries = new HashMap<>();

    public abstract List<TablistModule> getModules();

    public void deleteTablistEntries(HypixelPlayer player) {
        tablistEntries.remove(player);
    }

    public void nullifyCache(HypixelPlayer player) {
        if (tablistEntries.containsKey(player) && tablistEntries.get(player) != null) {
            player.sendPacket(new PlayerInfoRemovePacket(tablistEntries.get(player)));
            tablistEntries.put(player, null);
        }
    }

    public void runScheduler(Scheduler scheduler) {
        scheduler.scheduleTask(() -> {
            HypixelGenericLoader.getLoadedPlayers().forEach(player -> {
                if (!tablistEntries.containsKey(player)) {
                    tablistEntries.put(player, new ArrayList<>());
                } else {
                    if (tablistEntries.get(player) == null) return;
                    player.sendPacket(new PlayerInfoRemovePacket(tablistEntries.get(player)));
                }
                tablistEntries.get(player).clear();

                AtomicReference<Map.Entry<String, Integer>> charPrefix = new AtomicReference<>(Map.entry("§", 0));

                getModules().forEach(module -> {
                    try {
                        List<TablistModule.TablistEntry> entries = module.getEntries(player);

                        entries.forEach(entry -> {
                            List<PlayerInfoUpdatePacket.Property> properties = new ArrayList<>();
                            properties.add(new PlayerInfoUpdatePacket.Property(
                                    "textures",
                                    entry.registry().getTexture(),
                                    entry.registry().getSignature()));

                            if (!charPrefix.get().getKey().equals(entry.content())) {
                                charPrefix.set(Map.entry(entry.content(), charPrefix.get().getValue() + 1));
                            }

                            // 0 is AA, 1 is AB, 2 is AC, etc.
                            // 26 is BA, 27 is BB, 28 is BC, etc.
                            StringBuilder prefix = new StringBuilder();
                            int value = charPrefix.get().getValue();
                            do {
                                // 'A' has an ASCII value of 65, so adding value % 26 gives us the letter we want.
                                // We subtract by 1 before the modulus operation because we want 'A' to represent 0, 'B' to represent 1, and so on.
                                char charToAdd = (char) ('A' + (value - 1) % 26);
                                prefix.insert(0, charToAdd); // Prepend the character
                                value = (value - 1) / 26; // Move to the next 'digit'
                            } while (value > 0);

                            UUID uuid = UUID.randomUUID();
                            tablistEntries.get(player).add(uuid);

                            String randomName = UUID.randomUUID().toString().substring(0, 8);

                            TeamsPacket teamPacket = new TeamsPacket(prefix.toString(), new TeamsPacket.CreateTeamAction(
                                    Component.text(prefix.toString()),
                                    (byte) 0x01,
                                    TeamsPacket.NameTagVisibility.ALWAYS,
                                    TeamsPacket.CollisionRule.ALWAYS,
                                    NamedTextColor.RED,
                                    Component.text(prefix.toString()),
                                    Component.empty(),
                                    new ArrayList<>(Collections.singletonList(randomName))
                            ));

                            player.sendPackets(
                                    teamPacket,
                                    new PlayerInfoUpdatePacket(EnumSet.of(
                                            PlayerInfoUpdatePacket.Action.ADD_PLAYER,
                                            PlayerInfoUpdatePacket.Action.UPDATE_DISPLAY_NAME,
                                            PlayerInfoUpdatePacket.Action.UPDATE_LISTED
                                    ), Collections.singletonList(new PlayerInfoUpdatePacket.Entry(
                                            uuid,
                                            randomName,
                                            properties,
                                            true,
                                            0,
                                            GameMode.CREATIVE,
                                            Component.text(entry.content()),
                                            null,
                                            1, true)))
                            );
                        });
                    } catch (Exception e) {}
                });
            });
        }, TaskSchedule.seconds(5), TaskSchedule.seconds(3), ExecutionType.TICK_END);
    }
}
