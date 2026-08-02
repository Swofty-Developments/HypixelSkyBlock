package net.swofty.type.ravengardgeneric.gui;

import lombok.Getter;

/**
 * Chrome buttons. Every base offset and footprint here was measured from the captured menus by
 * solving {@code r = 18 * column + baseX} over each button's origin slot.
 */
@Getter
public enum RavengardButton implements RavengardSprite {
    ADD("add", 0xF201, 48, 52, 1, 3),
    BANNER_ALCHEMIST("alchemist", 0xF22C, 33, 11, 3, 2),
    BANNER_ARMORER("armorer", 0xF22D, 33, 11, 3, 2),
    BANNER_BLACKSMITH("blacksmith", 0xF22E, 33, 11, 3, 2),
    BUY("buy", 0xF203, 42, 32, 1, 1),
    HOURGLASS("hourglass", 0xF22F, 48, 33, 1, 1),
    SELL("sell", 0xF218, 42, 31, 1, 1),
    TEXT_SELL("text_sell", 0xF230, 48, 36, 3, 1),
    BACK("back", 0xF202, 48, 37, 1, 1),
    BAG("bag", 0xF206, 51, 32, 2, 2),
    BOOK("book", 0xF205, 51, 45, 3, 2),
    CANDLE("candle", 0xF219, 50, 39, 1, 2),
    CHEST("chest", 0xF204, 44, 36, 2, 2),
    QUILL("quill", 0xF215, 48, 36, 1, 2),
    STATUE_ASSASSIN("statue_assassin", 0xF21B, 42, 37, 1, 3),
    STATUE_HUNTER("statue_hunter", 0xF21D, 42, 37, 1, 3),
    STATUE_KNIGHT("statue_knight", 0xF21E, 42, 37, 1, 3),
    STATUE_WARRIOR("statue_warrior", 0xF21F, 42, 37, 1, 3),
    TEXT_CANCEL("text_cancel", 0xF220, 46, 31, 3, 1),
    TEXT_CONFIRM("text_confirm", 0xF221, 46, 31, 3, 1),
    TEXT_FIGHT("text_fight", 0xF224, 46, 33, 3, 1),
    TROPHY("trophy", 0xF229, 42, 35, 2, 2);

    private static final String MODEL_ROOT = "hypixel_ravengard:ui/menu/button/";

    private final String id;
    private final int iconCodePoint;
    private final int hoverBaseX;
    private final int hoverBaseY;
    private final int slotWidth;
    private final int slotHeight;

    RavengardButton(String id, int iconCodePoint, int hoverBaseX, int hoverBaseY,
                    int slotWidth, int slotHeight) {
        this.id = id;
        this.iconCodePoint = iconCodePoint;
        this.hoverBaseX = hoverBaseX;
        this.hoverBaseY = hoverBaseY;
        this.slotWidth = slotWidth;
        this.slotHeight = slotHeight;
    }

    @Override
    public String itemModel() {
        return MODEL_ROOT + id;
    }

    @Override
    public int iconCodePoint() {
        return iconCodePoint;
    }

    @Override
    public int hoverBaseX() {
        return hoverBaseX;
    }

    @Override
    public int hoverBaseY() {
        return hoverBaseY;
    }

    @Override
    public int slotWidth() {
        return slotWidth;
    }

    @Override
    public int slotHeight() {
        return slotHeight;
    }

    public static RavengardButton byId(String id) {
        for (RavengardButton button : values()) {
            if (button.id.equalsIgnoreCase(id)) {
                return button;
            }
        }
        return null;
    }

    public static RavengardButton statueFor(net.swofty.type.ravengardgeneric.classes.RavengardClass value) {
        return switch (value) {
            case KNIGHT -> STATUE_KNIGHT;
            case WARRIOR -> STATUE_WARRIOR;
            case HUNTER -> STATUE_HUNTER;
            case ASSASSIN -> STATUE_ASSASSIN;
            case SORCERER -> null;
        };
    }
}
