package gg.itzkatze.thehypixelrecreationmod.commands;

import com.google.gson.*;
import com.mojang.serialization.JsonOps;
import gg.itzkatze.thehypixelrecreationmod.utils.ClipboardUtils;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementNode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.RegistryOps;

public final class CopyAdvancementsCommand {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private CopyAdvancementsCommand() {
    }

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, _) -> {
            RecreationCommand.register(dispatcher, ClientCommands.literal("copyadvancements")
                    .executes(context -> copyAdvancements(context.getSource())));
            RecreationCommand.register(dispatcher, ClientCommands.literal("copyadvanvements")
                    .executes(context -> copyAdvancements(context.getSource())));
        });
    }

    private static int copyAdvancements(FabricClientCommandSource source) {
        Minecraft client = Minecraft.getInstance();
        ClientPacketListener connection = client.getConnection();
        if (connection == null || client.level == null) {
            source.sendFeedback(Component.literal("You must be connected to a server.").withStyle(ChatFormatting.RED));
            return 0;
        }

        RegistryAccess registryAccess = client.level.registryAccess();
        RegistryOps<JsonElement> registryOps = registryAccess.createSerializationContext(JsonOps.INSTANCE);
        JsonObject export = new JsonObject();
        var tree = connection.getAdvancements().getTree();

        JsonArray roots = new JsonArray();
        for (AdvancementNode root : tree.roots()) {
            roots.add(root.holder().id().toString());
        }
        export.add("roots", roots);

        JsonObject advancements = new JsonObject();
        try {
            for (AdvancementNode node : tree.nodes()) {
                JsonElement data = Advancement.CODEC.encodeStart(registryOps, node.advancement())
                        .getOrThrow(message -> new IllegalStateException("Could not encode "
                                + node.holder().id() + ": " + message));
                advancements.add(node.holder().id().toString(), data);
            }
        } catch (RuntimeException exception) {
            source.sendFeedback(Component.literal("Failed to serialize advancements: " + exception.getMessage())
                    .withStyle(ChatFormatting.RED));
            return 0;
        }

        export.add("advancements", advancements);
        ClipboardUtils.setClipboard(GSON.toJson(export));
        source.sendFeedback(Component.literal("Copied " + advancements.size()
                + " advancements and " + roots.size() + " roots as JSON.").withStyle(ChatFormatting.GREEN));
        return 1;
    }
}
