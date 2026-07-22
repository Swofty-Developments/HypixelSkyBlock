package gg.itzkatze.thehypixelrecreationmod.features;

import gg.itzkatze.thehypixelrecreationmod.utils.ChatUtils;
import gg.itzkatze.thehypixelrecreationmod.utils.ClipboardUtils;
import gg.itzkatze.thehypixelrecreationmod.utils.GUIUtils;
import gg.itzkatze.thehypixelrecreationmod.utils.ItemStackUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.world.item.ItemStack;

public final class GetPlayerHeadSkin {
    private GetPlayerHeadSkin() {
    }

    public static void checkHoveredItemForSkin(Minecraft client) {
        if (client.player == null) {
            return;
        }

        ItemStack hoveredStack = GUIUtils.getHoveredItem(client);
        var prop = ItemStackUtils.getPlayerHeadTextureProperty(hoveredStack);
        if (prop == null) {
            ChatUtils.send(Component.literal("No texture found on hovered item!").withStyle(ChatFormatting.RED));
            return;
        }

        String textureId = ItemStackUtils.getPlayerHeadTexture(hoveredStack);
        String payload = ItemStackUtils.buildTexturesPropertiesJson(prop, true);
        ClipboardUtils.setClipboard(payload);

        if (!textureId.isEmpty()) {
            ChatUtils.send(Component.literal("Copied Texture-ID: ")
                    .withStyle(ChatFormatting.GREEN)
                    .append(Component.literal(textureId)
                            .withStyle(style -> style
                                    .withClickEvent(new ClickEvent.CopyToClipboard(textureId))
                                    .withColor(ChatFormatting.AQUA)
                                    .withHoverEvent(new HoverEvent.ShowText(Component.literal("Click to copy again")))
                            )
                    ));
        }

        Component mainMessage = Component.literal("✓ Copied head textures JSON: ")
                .withStyle(ChatFormatting.GREEN)
                .append(Component.literal("[click to copy]")
                        .withStyle(style -> style
                                .withClickEvent(new ClickEvent.CopyToClipboard(payload))
                                .withColor(ChatFormatting.AQUA)
                                .withHoverEvent(new HoverEvent.ShowText(Component.literal("Click to copy again")))
                        )
                );

        ChatUtils.send(mainMessage);

        ChatUtils.send(Component.literal(payload)
                .withStyle(style -> style
                        .withClickEvent(new ClickEvent.CopyToClipboard(payload))
                        .withColor(ChatFormatting.GRAY)
                        .withHoverEvent(new HoverEvent.ShowText(Component.literal("Click to copy")))
                ));
    }
}
