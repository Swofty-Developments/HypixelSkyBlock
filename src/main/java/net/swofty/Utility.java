package net.swofty;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.network.packet.server.play.PlayerInfoPacket;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.UUID;

public class Utility {
    public static PlayerInfoPacket addPlayerInfoPacket(UUID uuid, String username, PlayerSkin skin) {
        var textureProperty = new PlayerInfoPacket.AddPlayer.Property("textures", skin.textures(), skin.signature());
        var playerEntry = new PlayerInfoPacket.AddPlayer(uuid, username, Collections.singletonList(textureProperty), GameMode.CREATIVE, 0, Component.text(username), null);

        return new PlayerInfoPacket(PlayerInfoPacket.Action.ADD_PLAYER, Collections.singletonList(playerEntry));
    }

    public static String getTextFromComponent(Component component) {
        if (!(component instanceof TextComponent))
            throw new IllegalArgumentException("Component must be a TextComponent");
        return PlainTextComponentSerializer.plainText().serialize(component);
    }

    public static String toNormalCase(String string) {
        string = string.replaceAll("_", " ");
        String[] spl = string.split(" ");
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < spl.length; i++) {
            String s = spl[i];
            if (s.length() == 0) {
                continue;
            }
            if (s.length() == 1) {
                s = s.toUpperCase();
            } else {
                s = s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
            }
            // Append the processed string to the StringBuilder
            // Only add a space if it's not the first word
            if (sb.length() > 0) {
                sb.append(" ");
            }
            sb.append(s);
        }
        return sb.toString();
    }

    public static String commaify(double d) {
        if (d < 1) {
            return "0";
        }
        return new DecimalFormat("#,###.0").format(d);
    }
}
