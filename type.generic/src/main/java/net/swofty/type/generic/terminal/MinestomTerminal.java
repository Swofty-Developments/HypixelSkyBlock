package net.swofty.type.generic.terminal;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandManager;
import org.jetbrains.annotations.ApiStatus;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.tinylog.Logger;

import java.io.IOException;

public final class MinestomTerminal implements AutoCloseable {

    private static final String PROMPT = "> ";

    private final CommandManager commandManager;
    private Terminal terminal;
    private LineReader reader;
    private volatile boolean running;

    public MinestomTerminal(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    public void start() throws IOException {
        if (System.console() == null) {
            Logger.warn("No console detected; terminal disabled.");
            return;
        }

        Thread thread = new Thread(() -> {
            try {
                terminal = TerminalBuilder.builder()
                        .system(true)
                        .streams(System.in, System.out)
                        .build();

                reader = LineReaderBuilder.builder()
                        .terminal(terminal)
                        .completer(new MinestomCompleter(commandManager))
                        .build();

                running = true;
                runLoop();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "terminal-thread");

        thread.start();
    }


    private void runLoop() {
        while (running) {
            Logger.info("Waiting for terminal input...");
            try {
                String command = reader.readLine(PROMPT);
                commandManager.execute(
                        commandManager.getConsoleSender(),
                        command
                );
            } catch (UserInterruptException e) {
                MinecraftServer.stopCleanly();
                break;
            } catch (EndOfFileException e) {
                Logger.info("Terminal closed.");
                break;
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    @Override
    public void close() {
        running = false;
        try {
            if (terminal != null) terminal.close();
        } catch (IOException ignored) {}
    }
}
