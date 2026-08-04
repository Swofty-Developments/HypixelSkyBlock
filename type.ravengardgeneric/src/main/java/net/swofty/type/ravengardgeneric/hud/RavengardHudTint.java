package net.swofty.type.ravengardgeneric.hud;

import lombok.Getter;
import net.kyori.adventure.text.format.TextColor;

@Getter
public enum RavengardHudTint {
    TITLE_ANIM(0xFEFD00),
    CAST_BAR(0xFEFD02),
    HEALTH_TEXT(0xFEFD04),
    STAMINA_TEXT(0xFEFD06),
    BOSS_HEALTH(0xFEFD0C),
    BOSS_NAME(0xFEFD0E),
    DATE(0xFEFD10),
    SERVER(0xFEFD12),
    LEVELUP_NUMBER(0xFEFD14),
    LEVELUP_TEXT(0xFEFD16),
    KEYBINDS(0xFEFD18),
    MENU_TITLE(0xFEFD1A),
    STATIC_TEXTURE(0xFEFE00);

    private final int rgb;

    RavengardHudTint(int rgb) {
        this.rgb = rgb;
    }

    public TextColor color() {
        return TextColor.color(rgb);
    }
}
