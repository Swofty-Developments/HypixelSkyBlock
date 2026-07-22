package gg.itzkatze.thehypixelrecreationmod.commands;

import gg.itzkatze.thehypixelrecreationmod.utils.ClipboardUtils;
import gg.itzkatze.thehypixelrecreationmod.utils.StringUtility;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.biome.Biome;

public final class CopyBiomeData {
    private CopyBiomeData() {
    }

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, _) ->
                dispatcher.register(ClientCommands.literal("copybiomedata")
                        .executes(context -> {
                            try {
                                Minecraft client = Minecraft.getInstance();
                                if (client.player == null || client.level == null) {
                                    context.getSource().sendFeedback(Component.literal("§cClient not ready or player/world missing."));
                                    return 0;
                                }

                                BlockPos pos = client.player.blockPosition();
                                var holder = client.level.getBiome(pos);
                                String biomeData = buildBiomeData(holder.toString(), holder.value());

                                ClipboardUtils.setClipboard(biomeData);
                                context.getSource().sendFeedback(Component.literal("§aBiome data copied to clipboard: " + holder));
                                return 1;
                            } catch (Exception exception) {
                                context.getSource().sendFeedback(Component.literal("§cFailed to copy biome data: " + exception.getMessage()));
                                return 0;
                            }
                        }))
        );
    }

    private static String buildBiomeData(String holder, Biome biome) {
        StringBuilder output = new StringBuilder();
        output.append('{');
        output.append("\n  \"holder\": \"").append(StringUtility.escapeJson(holder)).append('\"');
        output.append(",\n  \"biomeToString\": \"").append(StringUtility.escapeJson(String.valueOf(biome))).append('\"');

        if (biome != null) {
            output.append(",\n  \"effects\": \"")
                    .append(StringUtility.escapeJson(String.valueOf(biome.getSpecialEffects())))
                    .append('\"');
        }

        output.append('\n').append('}');
        return output.toString();
    }
}
