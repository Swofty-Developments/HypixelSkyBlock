package net.swofty.type.generic.language;

import net.swofty.type.generic.data.HypixelDataHandler;
import net.swofty.type.generic.data.datapoints.DatapointString;
import net.swofty.type.generic.user.HypixelPlayer;

import java.lang.reflect.Method;
import java.util.Locale;

public final class PlayerLanguageService {
    private PlayerLanguageService() {
    }

    public static void applyLanguage(HypixelPlayer player, PlayerLanguage language) {
        player.getDataHandler().get(HypixelDataHandler.Data.LANGUAGE, DatapointString.class).setValue(language.getId());
    }

    public static PlayerLanguage detectClientLanguage(HypixelPlayer player) {
        PlayerLanguage detected = fromValue(readMember(player, "getLocale"));
        if (detected != null) {
            return detected;
        }

        Object settings = readMember(player, "getSettings");
        if (settings == null) {
            settings = readMember(player, "settings");
        }

        detected = fromValue(readMember(settings, "locale"));
        if (detected != null) {
            return detected;
        }

        detected = fromValue(readMember(settings, "getLocale"));
        return detected == null ? PlayerLanguage.ENGLISH : detected;
    }

    private static Object readMember(Object target, String methodName) {
        if (target == null) {
            return null;
        }

        try {
            Method method = target.getClass().getMethod(methodName);
            return method.invoke(target);
        } catch (Exception ignored) {
            return null;
        }
    }

    private static PlayerLanguage fromValue(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Locale locale) {
            PlayerLanguage detected = PlayerLanguage.fromInput(locale.toLanguageTag());
            if (detected != null) {
                return detected;
            }
            return PlayerLanguage.fromInput(locale.getLanguage());
        }

        return PlayerLanguage.fromInput(value.toString());
    }
}
