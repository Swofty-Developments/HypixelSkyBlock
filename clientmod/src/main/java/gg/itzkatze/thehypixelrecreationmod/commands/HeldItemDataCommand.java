package gg.itzkatze.thehypixelrecreationmod.commands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.item.ItemStack;

public final class HeldItemDataCommand {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private HeldItemDataCommand() {
    }

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, _) -> {
            dispatcher.register(ClientCommands.literal("helditemdata")
                    .executes(context -> inspectHeldItem(context.getSource())));
            dispatcher.register(ClientCommands.literal("getitemdata")
                    .executes(context -> inspectHeldItem(context.getSource())));
        });
    }

    private static int inspectHeldItem(FabricClientCommandSource source) {
        Minecraft client = Minecraft.getInstance();
        if (client.player == null) {
            source.sendFeedback(Component.literal("Client player is not available.").withStyle(ChatFormatting.RED));
            return 0;
        }

        ItemStack stack = client.player.getMainHandItem();
        if (stack.isEmpty()) {
            source.sendFeedback(Component.literal("You are not holding an item.").withStyle(ChatFormatting.RED));
            return 0;
        }

        String json;
        try {
            json = toPrettyJson(stack);
        } catch (RuntimeException exception) {
            source.sendFeedback(Component.literal("Failed to serialize held item data: " + exception.getMessage())
                    .withStyle(ChatFormatting.RED));
            return 0;
        }

        source.sendFeedback(Component.literal("Copied held item NBT/component JSON: ")
                .withStyle(ChatFormatting.GREEN)
                .append(Component.literal("[click to copy]")
                        .withStyle(style -> style
                                .withClickEvent(new ClickEvent.CopyToClipboard(json))
                                .withColor(ChatFormatting.AQUA)
                                .withHoverEvent(new HoverEvent.ShowText(Component.literal("Click to copy again"))))));
        source.sendFeedback(Component.literal(json)
                .withStyle(style -> style
                        .withClickEvent(new ClickEvent.CopyToClipboard(json))
                        .withColor(ChatFormatting.GRAY)
                        .withHoverEvent(new HoverEvent.ShowText(Component.literal("Click to copy")))));
        return 1;
    }

    private static String toPrettyJson(ItemStack stack) {
        Minecraft client = Minecraft.getInstance();
        RegistryAccess registryAccess = client.level != null
                ? client.level.registryAccess()
                : RegistryAccess.EMPTY;
        RegistryOps<JsonElement> registryOps = registryAccess.createSerializationContext(JsonOps.INSTANCE);
        JsonElement element = ItemStack.OPTIONAL_CODEC.encodeStart(registryOps, stack)
                .getOrThrow(message -> new IllegalStateException("Could not encode item stack: " + message));
        return GSON.toJson(element);
    }
}
