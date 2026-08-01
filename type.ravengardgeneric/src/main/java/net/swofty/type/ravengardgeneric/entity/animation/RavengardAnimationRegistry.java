package net.swofty.type.ravengardgeneric.entity.animation;

import com.google.gson.Gson;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.type.ravengardgeneric.RavengardGenericLoader;
import org.tinylog.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public final class RavengardAnimationRegistry {
    private static final List<AnimatedRavengardNPC> SPAWNED = new ArrayList<>();

    private RavengardAnimationRegistry() {
    }

    public static List<AnimatedRavengardNPC> spawned() {
        return List.copyOf(SPAWNED);
    }

    public static void spawnAll(Instance instance) {
        for (String name : clipNames()) {
            try {
                RavengardAnimationClip clip = RavengardAnimationClip.load(name);
                double[] position = clip.position();

                // tutorial NPCs live aboard the Nevermore, not in the lobby world
                Instance target = "tutorial".equals(clip.world())
                        ? RavengardGenericLoader.tutorialInstance
                        : instance;
                if (target == null) {
                    Logger.warn("Skipping '{}': its world is not loaded", name);
                    continue;
                }
                AnimatedRavengardNPC npc = new AnimatedRavengardNPC(
                        clip,
                        new Pos(position[0], position[1], position[2], clip.yaw(), 0f),
                        pinkName(name, clip),
                        displayName(name)
                );
                npc.spawn(target);
                SPAWNED.add(npc);
                Logger.info("Spawned animated Ravengard NPC '{}' with {} parts at {}, {}, {}",
                        name, clip.parts().size(), position[0], position[1], position[2]);
            } catch (Exception exception) {
                Logger.error(exception, "Failed to spawn animated Ravengard NPC '{}'", name);
            }
        }

        MinecraftServer.getSchedulerManager()
                .buildTask(RavengardAnimationRegistry::tickAll)
                .repeat(TaskSchedule.tick(1))
                .schedule();
    }

    private static void tickAll() {
        if (net.swofty.type.generic.HypixelGenericLoader.getLoadedPlayers().isEmpty()) {
            return;
        }

        for (AnimatedRavengardNPC npc : SPAWNED) {
            try {
                npc.tick();
            } catch (Exception exception) {
                Logger.error(exception, "Failed to tick animated Ravengard NPC");
            }
        }
    }

    private static List<String> clipNames() {
        try (InputStream stream = new FileInputStream(
                new File(RavengardAnimationClip.ANIMATIONS_DIR, "index.json"))) {
            if (stream == null) {
                Logger.warn("No npc_animations/index.json, no animated NPCs will spawn");
                return List.of();
            }
            Index index = new Gson().fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8), Index.class);
            return index == null || index.clips == null ? List.of() : index.clips;
        } catch (Exception exception) {
            Logger.error(exception, "Failed to read animation clip index");
            return List.of();
        }
    }

    /**
     * Named NPCs show their speaker name in pink above the role line. Villagers are anonymous, so
     * they get the role line only.
     */
    private static String pinkName(String name, RavengardAnimationClip clip) {
        if (name.startsWith("villager")) {
            return "";
        }
        RavengardAnimationClip.Dialogue dialogue = clip.dialogue();
        return dialogue == null || dialogue.speaker() == null ? "" : dialogue.speaker();
    }

    private static String displayName(String name) {
        String base = name.replaceAll("_\\d+$", "");
        String[] words = base.split("_");
        StringBuilder builder = new StringBuilder();
        for (String word : words) {
            if (word.isBlank()) {
                continue;
            }
            if (!builder.isEmpty()) {
                builder.append(' ');
            }
            builder.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1));
        }
        return builder.toString();
    }

    private static final class Index {
        private List<String> clips;
    }
}
