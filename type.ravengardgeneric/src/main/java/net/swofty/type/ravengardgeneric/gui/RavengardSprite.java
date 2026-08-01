package net.swofty.type.ravengardgeneric.gui;

public interface RavengardSprite {
    int SLOT_PIXELS = 18;
    int ROW_WIDTH = 9;

    String itemModel();

    int iconCodePoint();

    int hoverBaseX();

    int hoverBaseY();

    int slotWidth();

    int slotHeight();

    default String icon() {
        return new String(Character.toChars(iconCodePoint()));
    }

    /**
     * The pack positions a button's hover glow from the colour on its custom_name, encoded as
     * {@code r = 18 * column + baseX} and {@code g = 18 * row + baseY}. The base pair centres the
     * sprite and is a constant per texture, so the colour has to be recomputed for whatever slot
     * the button actually sits in. Buttons spanning several slots repeat the origin's colour.
     */
    default int hoverColor(int originSlot) {
        int red = SLOT_PIXELS * (originSlot % ROW_WIDTH) + hoverBaseX();
        int green = SLOT_PIXELS * (originSlot / ROW_WIDTH) + hoverBaseY();
        return (Math.min(red, 0xFF) << 16) | (Math.min(green, 0xFF) << 8);
    }

    default int[] coveredSlots(int originSlot) {
        int[] slots = new int[slotWidth() * slotHeight()];
        int index = 0;
        for (int row = 0; row < slotHeight(); row++) {
            for (int column = 0; column < slotWidth(); column++) {
                slots[index++] = originSlot + row * ROW_WIDTH + column;
            }
        }
        return slots;
    }
}
