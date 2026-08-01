package net.swofty.type.ravengardgeneric.hud;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public final class RavengardSidebarTitle {
    private static final String TEXT = "PROTOTYPE";
    private static final int TICKS_PER_STEP = 2;
    private static final int IDLE_STEPS = 12;

    private RavengardSidebarTitle() {
    }

    public static Component frame(int tick) {
        int step = (tick / TICKS_PER_STEP) % (TEXT.length() + IDLE_STEPS);

        if (step >= TEXT.length()) {
            return Component.text(TEXT, NamedTextColor.YELLOW).decorate(TextDecoration.BOLD);
        }

        Component title = Component.empty();
        if (step > 0) {
            title = title.append(Component.text(TEXT.substring(0, step), NamedTextColor.WHITE)
                    .decorate(TextDecoration.BOLD));
        }
        title = title.append(Component.text(String.valueOf(TEXT.charAt(step)), NamedTextColor.GOLD)
                .decorate(TextDecoration.BOLD));
        if (step + 1 < TEXT.length()) {
            title = title.append(Component.text(TEXT.substring(step + 1), NamedTextColor.YELLOW)
                    .decorate(TextDecoration.BOLD));
        }
        return title;
    }
}
