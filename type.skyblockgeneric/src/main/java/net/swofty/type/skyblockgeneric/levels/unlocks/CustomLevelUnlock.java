package net.swofty.type.skyblockgeneric.levels.unlocks;

import lombok.Getter;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.skyblockgeneric.levels.CustomLevelAward;
import net.swofty.type.skyblockgeneric.levels.SkyBlockLevelUnlock;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.List;

@Getter
public class CustomLevelUnlock extends SkyBlockLevelUnlock {
    private final CustomLevelAward award;
    public CustomLevelUnlock(CustomLevelAward award) {
        this.award = award;
    }

    @Override
    public UnlockType type() {
        return UnlockType.CUSTOM;
    }

    @Override
    public ItemStack.Builder getItemDisplay(SkyBlockPlayer player, int level) {
        return ItemStackCreator.getStack(award.getDisplay(), Material.GOLDEN_APPLE, 1,
                "§8Level " + level);
    }

    @Override
    public List<String> getDisplay(SkyBlockPlayer player, int level) {
        return List.of(award.getDisplay());
    }
}
