package net.swofty.type.bedwarsgame.util;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.lang.reflect.Field;

public class ColorUtil {

    @Nullable
    public static Color getColorByName(String name) {
        try {
            Field field = Color.class.getField(name.toUpperCase());
            return (Color) field.get(null);
        } catch (Exception e) {
            return null;
        }
    }

    @Nullable
    public static TextColor getTextColorByName(String name) {
        try {
            Field field = NamedTextColor.class.getField(name.toUpperCase());
            return (TextColor) field.get(null);
        } catch (Exception e) {
            return null;
        }
    }
}
