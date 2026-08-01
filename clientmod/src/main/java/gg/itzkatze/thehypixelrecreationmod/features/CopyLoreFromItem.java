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

import java.util.List;

public final class CopyLoreFromItem {
    private CopyLoreFromItem() {
    }

    public static void copyLore(Minecraft client) {
        if (client.player == null) {
            return;
        }

        ItemStack stack = GUIUtils.getHoveredItem(client);
        List<String> lore = ItemStackUtils.getLoreAsStrings(stack);

        if (lore.isEmpty()) {
            ChatUtils.warn("Lore is empty!");
            return;
        }

        String loreText = String.join("\n", lore);
        ClipboardUtils.setClipboard(loreText);

        Component message = Component.literal("✓ Copied Lore")
                .withStyle(style -> style
                        .withColor(ChatFormatting.AQUA)
                        .withClickEvent(new ClickEvent.CopyToClipboard(loreText))
                        .withHoverEvent(new HoverEvent.ShowText(
                                Component.literal("Click to copy again\n\n" +
                                        (loreText.length() > 100 ?
                                                loreText.substring(0, 100) + "..." :
                                                loreText))
                        ))
                );

        ChatUtils.send(message);
    }
}
