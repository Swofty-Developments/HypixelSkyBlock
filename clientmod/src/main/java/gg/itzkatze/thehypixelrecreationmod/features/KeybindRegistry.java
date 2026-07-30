package gg.itzkatze.thehypixelrecreationmod.features;

import gg.itzkatze.thehypixelrecreationmod.features.entitypacket.EntityPacketInspector;
import gg.itzkatze.thehypixelrecreationmod.features.entitypacket.EntityPacketInspectorScreen;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public final class KeybindRegistry {
    public static KeyMapping checkSkinKey;
    public static KeyMapping copyLoreKey;
    public static KeyMapping copyChatKey;
    public static KeyMapping copyGuiKey;
    public static KeyMapping entityPacketInspectorKey;
    private static boolean chatCopyPressed = false;

    private KeybindRegistry() {
    }

    public static void register() {
        checkSkinKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.thehypixelrecreationmod.copyplayerheadskin",
                GLFW.GLFW_KEY_K,
                KeyMapping.Category.MISC
        ));

        copyLoreKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.thehypixelrecreationmod.copylore",
                GLFW.GLFW_KEY_L,
                KeyMapping.Category.MISC
        ));

        copyChatKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.thehypixelrecreationmod.copychat",
                GLFW.GLFW_KEY_P,
                KeyMapping.Category.MISC
        ));

        copyGuiKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.thehypixelrecreationmod.copygui",
                GLFW.GLFW_KEY_B,
                KeyMapping.Category.MISC
        ));

        entityPacketInspectorKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.thehypixelrecreationmod.entitypacketinspector",
                GLFW.GLFW_KEY_I,
                KeyMapping.Category.MISC
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            EntityPacketInspector.tick(client);
            while (entityPacketInspectorKey.consumeClick()) {
                if (client.gui.screen() == null) {
                    EntityPacketInspector.selectLookedAtEntity(client);
                    client.gui.setScreen(new EntityPacketInspectorScreen());
                }
            }
        });

        // Check keybinds inside of GUI's
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            ScreenKeyboardEvents.allowKeyPress(screen).register((scancode, key) -> {
                if (checkSkinKey.matches(key)) {
                    GetPlayerHeadSkin.checkHoveredItemForSkin(client);
                    return false;
                }
                if (copyLoreKey.matches(key)) {
                    CopyLoreFromItem.copyLore(client);
                    return false;
                }
                if (copyChatKey.matches(key)) {
                    chatCopyPressed = true;
                    return false;
                }
                if (copyGuiKey.matches(key)) {
                    CopyCurrentGui.copyCurrentGui(client);
                    return false;
                }
                return true;
            });
        });
    }

    public static boolean wasChatCopyPressed() {
        if (chatCopyPressed) {
            chatCopyPressed = false;
            return true;
        }
        return false;
    }
}
