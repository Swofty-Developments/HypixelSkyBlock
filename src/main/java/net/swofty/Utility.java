package net.swofty;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.network.packet.server.play.PlayerInfoPacket;
import net.swofty.utility.Acronym;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {
    public static char ALPHABET[] = {
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'W', 'X', 'Y', 'Z'
    };

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
        if (Acronym.isAcronym(string)) return string.toUpperCase();
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

    public static List<String> splitByWordAndLength(String string, int splitLength, String separator) {
        List<String> result = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\G" + separator + "*(.{1," + splitLength + "})(?=\\s|$)", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(string);
        while (matcher.find())
            result.add(matcher.group(1));
        return result;
    }

    public static double random(double min, double max) {
        return Math.random() * (max - min) + min;
    }

    public static String zeroed(long l) {
        return l > 9 ? "" + l : "0" + l;
    }

    public static String limitStringLength(String s, int charLimit) {
        return s.substring(0, charLimit - 1);
    }

    public static String ntify(int i) {
        if (i == 11 || i == 12 || i == 13)
            return i + "th";
        String s = String.valueOf(i);
        char last = s.charAt(s.length() - 1);
        switch (last) {
            case '1':
                return i + "st";
            case '2':
                return i + "nd";
            case '3':
                return i + "rd";
            default:
                return i + "th";
        }
    }
}
