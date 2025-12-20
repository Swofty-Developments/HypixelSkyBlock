package net.swofty.type.skyblockgeneric.user;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.ChatUtility;
import net.swofty.type.skyblockgeneric.SkyBlockGenericLoader;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointStash;

import java.util.List;

public class StashReminder {

    /**
     * Start the stash reminder loop.
     * Sends clickable reminders every 60 seconds if players have items in their stash.
     */
    public static void start(Scheduler scheduler) {
        scheduler.submitTask(() -> {
            for (SkyBlockPlayer player : SkyBlockGenericLoader.getLoadedPlayers()) {
                if (player.getSkyblockDataHandler() == null) continue;

                DatapointStash.PlayerStash stash = player.getStash();

                // Send item stash reminder
                if (stash.getItemStashCount() > 0) {
                    sendItemStashReminder(player, stash);
                }

                // Send material stash reminder
                if (stash.getMaterialStashCount() > 0) {
                    sendMaterialStashReminder(player, stash);
                }
            }
            return TaskSchedule.tick(1200); // 60 seconds = 1200 ticks
        });
    }

    private static void sendItemStashReminder(SkyBlockPlayer player, DatapointStash.PlayerStash stash) {
        int count = stash.getItemStashCount();

        // Check for near-full warning (90% = 648 items)
        if (stash.isItemStashNearFull()) {
            player.sendMessage("§cYOUR STASH IS ALMOST AT MAX CAPACITY!");
        }

        String itemWord = count == 1 ? "item" : "items";
        String itWord = count == 1 ? "it" : "them";

        player.sendMessage("");
        List<String> lines = List.of(
                "§7You have §a" + count + " " + itemWord + " §7stashed away!",
                "§6>>> CLICK HERE §eto pick " + itWord + " up! §6<<<"
        );
        lines = ChatUtility.FontInfo.centerLines(lines);
        for (String line : lines) {
            player.sendMessage(Component.text(line)
                    .clickEvent(ClickEvent.runCommand("/pickupstash item")));
        }
        player.sendMessage("");
    }

    private static void sendMaterialStashReminder(SkyBlockPlayer player, DatapointStash.PlayerStash stash) {
        int count = stash.getMaterialStashCount();
        int types = stash.getMaterialTypeCount();

        String materialWord = count == 1 ? "material" : "materials";
        String typeWord = types == 1 ? "type" : "types";
        String itWord = count == 1 ? "it" : "them";

        player.sendMessage("");
        List<String> lines = List.of(
                "§7You have §b" + count + " " + materialWord + " §7stashed away!",
                "§8(This totals " + types + " " + typeWord + " of materials stashed!)",
                "§2>>> CLICK HERE §bto pick " + itWord + " up! §2<<<"
        );
        lines = ChatUtility.FontInfo.centerLines(lines);
        for (String line : lines) {
            player.sendMessage(Component.text(line)
                    .clickEvent(ClickEvent.runCommand("/pickupstash material")));
        }

        player.sendMessage("");
    }
}
