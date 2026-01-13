package net.swofty.spark;

import me.lucko.spark.common.SparkPlatform;
import me.lucko.spark.common.SparkPlugin;
import me.lucko.spark.common.command.sender.CommandSender;
import me.lucko.spark.common.monitor.ping.PlayerPingProvider;
import me.lucko.spark.common.platform.PlatformInfo;
import me.lucko.spark.common.tick.TickHook;
import me.lucko.spark.common.tick.TickReporter;
import me.lucko.spark.minestom.MinestomPlayerPingProvider;
import me.lucko.spark.minestom.MinestomTickHook;
import me.lucko.spark.minestom.MinestomTickReporter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.ExecutionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.logging.Level;
import java.util.stream.Stream;

public final class Spark implements SparkPlugin {

    private static final @NotNull Logger LOGGER = LoggerFactory.getLogger(Spark.class);

    private static @Nullable Spark INSTANCE;

    private final @NotNull SparkPlatform platform;
    private final @NotNull MinestomSparkCommand command;
    private final @NotNull Path directory;

    private Spark(@NotNull Path directory) {
        this.directory = directory;
        this.platform = new SparkPlatform(this);
        this.platform.enable();
        this.command = new MinestomSparkCommand(this.platform);
        MinecraftServer.getCommandManager().register(this.command);
    }

    public static void enable(@NotNull Path directory) {
        if (INSTANCE != null) throw new IllegalStateException("Spark is already enabled!");
        INSTANCE = new Spark(directory);
    }

    public static void disable() {
        if (INSTANCE == null) throw new IllegalStateException("Spark is not enabled!");

        INSTANCE.platform.disable();
        MinecraftServer.getCommandManager().unregister(INSTANCE.command);

        INSTANCE = null;
    }

    @Override
    public String getVersion() {
        return "dev";
    }

    @Override
    public Path getPluginDirectory() {
        return this.directory;
    }

    @Override
    public String getCommandName() {
        return "spark";
    }

    @Override
    public Stream<? extends CommandSender> getCommandSenders() {
        return Stream.concat(
                MinecraftServer.getConnectionManager().getOnlinePlayers().stream(),
                Stream.of(MinecraftServer.getCommandManager().getConsoleSender())
        ).map(MinestomCommandSender::new);
    }

    @Override
    public void executeAsync(Runnable runnable) {
        MinecraftServer.getSchedulerManager().scheduleNextTick(runnable, ExecutionType.TICK_END);
    }

    @Override
    public void log(Level level, String message) {
        if (level == Level.INFO) LOGGER.info(message);
        else if (level == Level.WARNING) LOGGER.warn(message);
        else if (level == Level.SEVERE) LOGGER.error(message);
        else throw new IllegalArgumentException(level.getName());
    }

    @Override
    public void log(Level level, String s, Throwable throwable) {
        if (level == Level.INFO) LOGGER.info(s, throwable);
        else if (level == Level.WARNING) LOGGER.warn(s, throwable);
        else if (level == Level.SEVERE) LOGGER.error(s, throwable);
        else throw new IllegalArgumentException(level.getName());
    }

    @Override
    public PlatformInfo getPlatformInfo() {
        return new MinestomPlatformInfo();
    }

    @Override
    public PlayerPingProvider createPlayerPingProvider() {
        return new MinestomPlayerPingProvider();
    }

    @Override
    public TickReporter createTickReporter() {
        return new MinestomTickReporter();
    }

    @Override
    public TickHook createTickHook() {
        return new MinestomTickHook();
    }

}
