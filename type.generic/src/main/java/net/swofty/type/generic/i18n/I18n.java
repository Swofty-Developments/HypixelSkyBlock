package net.swofty.type.generic.i18n;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;

public class I18n {

    public static TranslatableComponent t(String key) {
        return Component.translatable(key);
    }

}
