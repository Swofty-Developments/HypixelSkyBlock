package net.swofty.type.ravengardgeneric.hud;

import lombok.Getter;

@Getter
public enum RavengardHudLayer {
    STATS("team_1", 1, null),
    SPACER_2("team_2", 2, "              "),
    SPACER_3("team_3", 3, "             "),
    SPACER_4("team_4", 4, "            "),
    ABILITIES("team_5", 5, "\uE020\uE022\uE020\uE021"),
    PLAYER_MARKER("team_6", 6, "\uE102"),
    SPACER_7("team_7", 7, "         "),
    SPACER_8("team_8", 8, "        "),
    SPACER_9("team_9", 9, "       "),
    SPACER_10("team_10", 10, "      "),
    MINIMAP_FRAME("team_11", 11, "\uE101"),
    MINIMAP_TILES("team_12", 12, "\uE120\uE121\uE122\uE123\uE124\uE125"),
    MINIMAP_BACKGROUND("team_13", 13, "\uE100"),
    KEYBIND_ICONS("team_14", 14, "\uE000\uE001"),
    OFFSCREEN("team_15", 15, "\uD2000");

    private final String teamName;
    private final int score;
    private final String staticContent;

    RavengardHudLayer(String teamName, int score, String staticContent) {
        this.teamName = teamName;
        this.score = score;
        this.staticContent = staticContent;
    }

    public boolean isDynamic() {
        return staticContent == null;
    }
}
