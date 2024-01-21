package net.swofty.packer;

public class NegativeSpace {

    public static String getNegativeSpace(Integer width) {
        return "\uF801".repeat(Math.max(1, width));
    }

}
