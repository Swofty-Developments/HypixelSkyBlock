package net.swofty.type.generic.user.categories;

import lombok.Getter;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.item.Material;
import net.swofty.type.generic.user.HypixelPlayer;

@Getter
public enum RankColor {
    RED("Red", NamedTextColor.RED, Material.RED_DYE, 0),
    GOLD("Gold", NamedTextColor.GOLD, Material.ORANGE_DYE, 35),
    GREEN("Green", NamedTextColor.GREEN, Material.LIME_DYE, 45),
    YELLOW("Yellow", NamedTextColor.YELLOW, Material.YELLOW_DYE, 55),
    LIGHT_PURPLE("Light Purple", NamedTextColor.LIGHT_PURPLE, Material.PINK_DYE, 65),
    WHITE("White", NamedTextColor.WHITE, Material.BONE_MEAL, 75),
    BLUE("Blue", NamedTextColor.BLUE, Material.LIGHT_BLUE_DYE, 85),
    DARK_GREEN("Dark Green", NamedTextColor.DARK_GREEN, Material.GREEN_DYE, 95),
    DARK_AQUA("Dark Aqua", NamedTextColor.DARK_AQUA, Material.CYAN_DYE, 100),
    DARK_RED("Dark Red", NamedTextColor.DARK_RED, Material.REDSTONE, 150),
    DARK_PURPLE("Dark Purple", NamedTextColor.DARK_PURPLE, Material.PURPLE_DYE, 200),
    DARK_GRAY("Dark Gray", NamedTextColor.DARK_GRAY, Material.GRAY_DYE, 200),
    BLACK("Black", NamedTextColor.BLACK, Material.INK_SAC, 250),
    DARK_BLUE("Dark Blue", NamedTextColor.DARK_BLUE, Material.LAPIS_LAZULI, -1);

    private final String displayName;
    private final NamedTextColor color;
    private final Material material;
    private final int requiredLevel;

    RankColor(String displayName, NamedTextColor color, Material material, int requiredLevel) {
        this.displayName = displayName;
        this.color = color;
        this.material = material;
        this.requiredLevel = requiredLevel;
    }

    public boolean isUnlocked(HypixelPlayer player) {
        return this == DARK_BLUE ? player.getRanksGifted() >= 100 : player.getExperienceHandler().getLevel() >= requiredLevel;
    }
}
