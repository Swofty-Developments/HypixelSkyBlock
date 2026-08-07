package gg.itzkatze.thehypixelrecreationmod.commands;

import gg.itzkatze.thehypixelrecreationmod.mixin.BossHealthOverlayAccessor;
import gg.itzkatze.thehypixelrecreationmod.mixin.PlayerTabOverlayAccessor;
import gg.itzkatze.thehypixelrecreationmod.utils.ChatUtils;
import gg.itzkatze.thehypixelrecreationmod.utils.ClipboardUtils;
import gg.itzkatze.thehypixelrecreationmod.utils.StringUtility;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class CopyTabBossbarCommand {
    private CopyTabBossbarCommand() {
    }

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, _) -> RecreationCommand.register(dispatcher,
                ClientCommands.literal("copytab").executes(_ -> copy())));

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, _) -> RecreationCommand.register(dispatcher,
                ClientCommands.literal("copybossbar").executes(_ -> copyBossbar())));
    }

    private static int copy() {
        Minecraft client = Minecraft.getInstance();
        if (client.player == null || client.getConnection() == null) {
            ChatUtils.warn("Not connected to a server.");
            return 0;
        }

        PlayerTabOverlay tabOverlay = client.gui.hud.getTabList();
        PlayerTabOverlayAccessor tabAccessor = (PlayerTabOverlayAccessor) tabOverlay;

        List<String> tab = new ArrayList<>();
        if (tabAccessor.recreation$getHeader() != null) {
            tab.add(StringUtility.toLegacyString(tabAccessor.recreation$getHeader()));
        }
        client.getConnection().getListedOnlinePlayers().stream()
                .sorted(Comparator.comparingInt(PlayerInfo::getTabListOrder)
                        .thenComparing(info -> info.getProfile().name(), String.CASE_INSENSITIVE_ORDER))
                .map(tabOverlay::getNameForDisplay)
                .map(StringUtility::toLegacyString)
                .forEach(tab::add);
        if (tabAccessor.recreation$getFooter() != null) {
            tab.add(StringUtility.toLegacyString(tabAccessor.recreation$getFooter()));
        }
        String thing = String.join("\n", tab);
        ClipboardUtils.setClipboard(thing);

        ChatUtils.send(Component.literal("Copied TAB")
                .withStyle(style -> style
                        .withColor(0x55FFFF)
                        .withClickEvent(new ClickEvent.CopyToClipboard(thing))
                        .withHoverEvent(new HoverEvent.ShowText(Component.literal("Click to copy again")))));
        return 1;
    }

    private static int copyBossbar() {
        Minecraft client = Minecraft.getInstance();
        if (client.player == null || client.getConnection() == null) {
            ChatUtils.warn("Not connected to a server.");
            return 0;
        }

        List<String> bossbars = ((BossHealthOverlayAccessor) client.gui.hud.getBossOverlay()).recreation$getEvents()
                .values().stream()
                .map(event -> StringUtility.toLegacyString(event.getName()))
                .toList();
        String thing = String.join("\n", bossbars);
        ClipboardUtils.setClipboard(thing);
        ChatUtils.send(Component.literal("Copied Bossbar")
                .withStyle(style -> style
                        .withColor(0x55FFFF)
                        .withClickEvent(new ClickEvent.CopyToClipboard(thing))
                        .withHoverEvent(new HoverEvent.ShowText(Component.literal("Click to copy again")))));
        return 1;
    }
}
