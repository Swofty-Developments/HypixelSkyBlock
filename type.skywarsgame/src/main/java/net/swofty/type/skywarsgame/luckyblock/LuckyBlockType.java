package net.swofty.type.skywarsgame.luckyblock;

import lombok.Getter;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;

@Getter
public enum LuckyBlockType {
    GUARDIAN(Block.LIGHT_BLUE_GLAZED_TERRACOTTA, Material.LIGHT_BLUE_GLAZED_TERRACOTTA, "Guardian", 0x55FFFF),
    WEAPONRY(Block.RED_GLAZED_TERRACOTTA, Material.RED_GLAZED_TERRACOTTA, "Weaponry", 0xFF5555),
    WILD(Block.GREEN_GLAZED_TERRACOTTA, Material.GREEN_GLAZED_TERRACOTTA, "Wild", 0x55FF55),
    CRAZY(Block.YELLOW_GLAZED_TERRACOTTA, Material.YELLOW_GLAZED_TERRACOTTA, "Crazy", 0xFFFF55),
    INSANE(Block.ORANGE_GLAZED_TERRACOTTA, Material.ORANGE_GLAZED_TERRACOTTA, "Insane", 0xFFAA00),
    OP_RULE(Block.CYAN_GLAZED_TERRACOTTA, Material.CYAN_GLAZED_TERRACOTTA, "OP Rule", 0x00AAAA);

    private final Block block;
    private final Material material;
    private final String displayName;
    private final int color;

    LuckyBlockType(Block block, Material material, String displayName, int color) {
        this.block = block;
        this.material = material;
        this.displayName = displayName;
        this.color = color;
    }

    public boolean matches(Block block) {
        return this.block.compare(block);
    }

    public static LuckyBlockType fromBlock(Block block) {
        for (LuckyBlockType type : values()) {
            if (type.matches(block)) {
                return type;
            }
        }
        return null;
    }

    public static boolean isLuckyBlock(Block block) {
        return fromBlock(block) != null;
    }

    public String getColoredName() {
        return String.format("\u00A7x\u00A7%s\u00A7%s\u00A7%s\u00A7%s\u00A7%s\u00A7%s%s",
                Integer.toHexString((color >> 20) & 0xF),
                Integer.toHexString((color >> 16) & 0xF),
                Integer.toHexString((color >> 12) & 0xF),
                Integer.toHexString((color >> 8) & 0xF),
                Integer.toHexString((color >> 4) & 0xF),
                Integer.toHexString(color & 0xF),
                displayName);
    }
}
