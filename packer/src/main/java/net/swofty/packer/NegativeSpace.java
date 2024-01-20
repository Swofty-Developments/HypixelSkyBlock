package net.swofty.packer;

public class NegativeSpace {

    public static String getNegativeSpace(Integer width) {
        /**
         * Example Formula: 0xD0000 + width converted to a character (-8192 <= width <= 8192)
         */

        return "\\u" + new String(Character.toChars(0xD0000 + width));
    }

}
