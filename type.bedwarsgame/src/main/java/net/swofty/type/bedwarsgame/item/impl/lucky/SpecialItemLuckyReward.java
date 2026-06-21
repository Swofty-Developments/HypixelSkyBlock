package net.swofty.type.bedwarsgame.item.impl.lucky;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.item.Material;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.item.impl.LuckySpecialItem;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;

public class SpecialItemLuckyReward extends LuckyReward {
    private final Material material;
    private final String action;
    private final int uses;
    private final String[] lore;

    public SpecialItemLuckyReward(String name, Material material, String action, int uses, String... lore) {
        super(name);
        this.material = material;
        this.action = action;
        this.uses = uses;
        this.lore = lore;
    }

    @Override
    public void apply(BedWarsPlayer player, Pos openedAt) {
        LuckySpecialItem specialItem = (LuckySpecialItem) TypeBedWarsGameLoader.getItemHandler().getItem("lucky_special");
        give(player, specialItem.item(name(), material, action, uses, lore));
    }
}
