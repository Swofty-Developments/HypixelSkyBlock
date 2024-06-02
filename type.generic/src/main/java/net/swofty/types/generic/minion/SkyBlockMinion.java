package net.swofty.types.generic.minion;

import net.minestom.server.color.Color;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;

import java.util.List;

public abstract class SkyBlockMinion {
    public abstract List<MinionTier> getTiers();
    public abstract Color getBootColour();
    public abstract Color getLeggingsColour();
    public abstract Color getChestplateColour();
    public abstract List<MinionExpectations> getExpectations();
    public abstract MinionAction getAction();

    public record MinionTier(int tier, int timeBetweenActions, int storage, String texture, Material heldItem, boolean craftable) {
        public int getSlots() {
            return storage / 64;
        }
    }

    public record MinionExpectations(int yLevel, Block... material) {}
}
