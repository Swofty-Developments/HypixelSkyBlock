package gg.itzkatze.thehypixelrecreationmod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public final class RecreationCommand {
    private RecreationCommand() {
    }

    public static void register(
        CommandDispatcher<FabricClientCommandSource> dispatcher,
        LiteralArgumentBuilder<FabricClientCommandSource> command
    ) {
        dispatcher.register(ClientCommands.literal("recreation").then(command));
    }
}
