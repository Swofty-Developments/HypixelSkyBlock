package net.swofty.type.bedwarsgame.shop;

import net.minestom.server.tag.Tag;

public enum TeamUpgradeId {
    SHARPNESS("sharpness"),
    REINFORCED_ARMOR("reinforced_armor"),
    MANIAC_MINER("maniac_miner"),
    HEAL_POOL("heal_pool"),
    FORGE("forge"),
    CUSHIONED_BOOTS("cushioned_boots");

    private final String key;
    private final Tag<Integer> levelTag;

    TeamUpgradeId(String key) {
        this.key = key;
        this.levelTag = Tag.Integer("upgrade_" + key);
    }

    public String key() {
        return key;
    }

    public Tag<Integer> levelTag() {
        return levelTag;
    }
}
