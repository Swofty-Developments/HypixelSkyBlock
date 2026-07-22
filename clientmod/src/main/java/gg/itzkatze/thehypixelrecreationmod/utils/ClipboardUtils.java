package gg.itzkatze.thehypixelrecreationmod.utils;

import net.minecraft.client.Minecraft;

public final class ClipboardUtils {
    private ClipboardUtils() {
    }

    public static void setClipboard(String value) {
        Minecraft.getInstance().keyboardHandler.setClipboard(value);
    }
}
