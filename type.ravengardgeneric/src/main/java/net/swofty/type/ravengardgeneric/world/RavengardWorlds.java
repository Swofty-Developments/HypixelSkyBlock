package net.swofty.type.ravengardgeneric.world;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceContainer;
import net.swofty.type.generic.world.HypixelWorldLoader;
import org.tinylog.Logger;

import java.nio.file.Path;

public final class RavengardWorlds {
    public static final Path CAPTURES = Path.of("configuration/world/captures");
    public static final String TUTORIAL_WORLD = "starting_world";
    public static final Pos TUTORIAL_SPAWN = new Pos(25.5, 64, 508.5, -90f, 0f);

    private static Instance tutorial;

    private RavengardWorlds() {
    }

    public static synchronized Instance tutorial() {
        if (tutorial != null) {
            return tutorial;
        }
        try {
            InstanceContainer source = MinecraftServer.getInstanceManager().createInstanceContainer();
            tutorial = HypixelWorldLoader.loadFrom(CAPTURES.resolve(TUTORIAL_WORLD + ".polar"),
                    source, MinecraftServer.getInstanceManager());
            Logger.info("Loaded Ravengard tutorial world '{}'", TUTORIAL_WORLD);
        } catch (Exception exception) {
            Logger.error(exception, "Failed to load Ravengard tutorial world '{}'", TUTORIAL_WORLD);
        }
        return tutorial;
    }
}
