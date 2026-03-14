/*
 * This file is part of spark.
 *
 *  Copyright (c) lucko (Luck) <luck@lucko.me>
 *  Copyright (c) contributors
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.lucko.spark.minestom;

import java.util.function.BiPredicate;
import me.lucko.spark.common.SparkPlatform;
import me.lucko.spark.common.SparkPlugin;
import me.lucko.spark.common.monitor.ping.PlayerPingProvider;
import me.lucko.spark.common.platform.PlatformInfo;
import me.lucko.spark.common.tick.TickHook;
import me.lucko.spark.common.tick.TickReporter;
import me.lucko.spark.common.util.SparkThreadFactory;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.CommandExecutor;
import net.minestom.server.command.builder.arguments.ArgumentStringArray;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.Suggestion;
import net.minestom.server.command.builder.suggestion.SuggestionCallback;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Level;
import java.util.stream.Stream;
import org.slf4j.Logger;

final class MinestomSparkPlugin implements SparkPlugin {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4, new SparkThreadFactory());
    private final @NotNull Path dataDirectory;
    private final @NotNull Logger logger;
    private final boolean commands;
    private final @NotNull BiPredicate<CommandSender, String> permissionHandler;

    private SparkPlatform platform;
    private MinestomSparkCommand command;

    MinestomSparkPlugin(@NotNull Path dataDirectory, @NotNull Logger logger, boolean commands, @NotNull BiPredicate<CommandSender, String> permissionHandler) {
        this.dataDirectory = dataDirectory;
        this.logger = logger;
        this.commands = commands;
        this.permissionHandler = permissionHandler;
    }

    public @NotNull SparkPlatform initialize() {
        this.platform = new SparkPlatform(this);
        this.platform.enable();

        if (this.commands) {
            this.command = new MinestomSparkCommand(this.platform);
            MinecraftServer.getCommandManager().register(this.command);
        }

        return this.platform;
    }

    public void terminate() {
        this.platform.disable();

        if (this.command != null) MinecraftServer.getCommandManager().unregister(this.command);
        this.scheduler.shutdown();
    }

    @Override
    public String getVersion() {
        return "dev";
    }

    @Override
    public Path getPluginDirectory() {
        return this.dataDirectory;
    }

    @Override
    public String getCommandName() {
        return "spark";
    }

    @Override
    public Stream<MinestomCommandSender> getCommandSenders() {
        return Stream.concat(
                MinecraftServer.getConnectionManager().getOnlinePlayers().stream(),
                Stream.of(MinecraftServer.getCommandManager().getConsoleSender())
        ).map(this::createCommandSender);
    }

    @Override
    public void executeAsync(Runnable task) {
        this.scheduler.execute(task);
    }

    @Override
    public void log(Level level, String msg) {
        if (level.intValue() >= 1000) { // severe
            this.logger.error(msg);
        } else if (level.intValue() >= 900) { // warning
            this.logger.warn(msg);
        } else {
            this.logger.info(msg);
        }
    }

    @Override
    public void log(Level level, String msg, Throwable throwable) {
        if (level.intValue() >= 1000) { // severe
            this.logger.error(msg, throwable);
        } else if (level.intValue() >= 900) { // warning
            this.logger.warn(msg, throwable);
        } else {
            this.logger.info(msg, throwable);
        }
    }

    private @NotNull MinestomCommandSender createCommandSender(@NotNull CommandSender sender) {
        return new MinestomCommandSender(sender, this.permissionHandler);
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

    private final class MinestomSparkCommand extends Command implements CommandExecutor, SuggestionCallback {
        private final SparkPlatform platform;

        public MinestomSparkCommand(SparkPlatform platform) {
            super("spark");
            this.platform = platform;

            ArgumentStringArray arrayArgument = ArgumentType.StringArray("args");
            arrayArgument.setSuggestionCallback(this);

            this.setCondition((sender, ignored) -> this.platform.hasPermissionForAnyCommand(MinestomSparkPlugin.this.createCommandSender(sender)));
            this.addSyntax(this, arrayArgument);
            this.setDefaultExecutor((sender, context) -> platform.executeCommand(MinestomSparkPlugin.this.createCommandSender(sender), new String[0]));
        }

        // execute
        @Override
        public void apply(@NotNull CommandSender sender, @NotNull CommandContext context) {
            String[] args = processArgs(context, false);
            if (args == null) return;

            this.platform.executeCommand(MinestomSparkPlugin.this.createCommandSender(sender), args);
        }

        // tab complete
        @Override
        public void apply(@NotNull CommandSender sender, @NotNull CommandContext context, @NotNull Suggestion suggestion) {
            String[] args = processArgs(context, true);
            if (args == null) return;

            Iterable<String> suggestionEntries = this.platform.tabCompleteCommand(MinestomSparkPlugin.this.createCommandSender(sender), args);
            for (String suggestionEntry : suggestionEntries) suggestion.addEntry(new SuggestionEntry(suggestionEntry));
        }

        private static String [] processArgs(CommandContext context, boolean tabComplete) {
            String[] split = context.getInput().split(" ", tabComplete ? -1 : 0);
            if (split.length == 0 || !split[0].equals("/spark") && !split[0].equals("spark")) return null;

            return Arrays.copyOfRange(split, 1, split.length);
        }
    }
}
