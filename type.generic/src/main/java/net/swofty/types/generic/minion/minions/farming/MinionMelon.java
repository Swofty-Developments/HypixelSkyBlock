package net.swofty.types.generic.minion.minions.farming;

import net.minestom.server.color.Color;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.types.generic.minion.MinionAction;
import net.swofty.types.generic.minion.SkyBlockMinion;
import net.swofty.types.generic.minion.actions.MinionMinePumpkinOrMelonAction;

import java.util.List;

public class MinionMelon extends SkyBlockMinion {
    @Override
    public List<MinionTier> getTiers() {
        return List.of(
                new MinionTier(1, 24, 64,
                        "95d54539ac8d3fba9696c91f4dcc7f15c320ab86029d5c92f12359abd4df811e",
                        Material.WOODEN_HOE, true),
                new MinionTier(2, 24, 192,
                        "93dbb7b41ddd998842719915179a6b5a82d0c223e4c313c9eb081b52c84a764f",
                        Material.WOODEN_HOE, true),
                new MinionTier(3, 22.5f, 192,
                        "9ed762a1b1bf0c811a6cc62742526840e8eb3c01fc86cc5afed89ec9beeb530e",
                        Material.STONE_HOE, true),
                new MinionTier(4, 22.5f, 384,
                        "9e2654f305b788b9345bef6be076d8f36e7c946d6eae26d3c0803ddc4843b596",
                        Material.STONE_HOE, true),
                new MinionTier(5, 21, 384,
                        "1234f7f7ad67acd50b30932781c21a0b1cc22530a4980f688549123e10d9c474",
                        Material.STONE_HOE, true),
                new MinionTier(6, 21, 576,
                        "8fea2e4ddf7314f21b87fc8d8634c60cab23320112002932d6c12c2e92d5549b",
                        Material.IRON_HOE, true),
                new MinionTier(7, 18.5f, 576,
                        "213637a2898fd04f0ced904c0293f136238057c33b16983e0262ff9ae0047dd2",
                        Material.IRON_HOE, true),
                new MinionTier(8, 18.5f, 768,
                        "3b7e6694866967e222664641461c0a1b08b9aa0c390944e6142e769d473987a5",
                        Material.IRON_HOE, true),
                new MinionTier(9, 16, 768,
                        "8ebac9c3ddbb76ea862b91a6992bae358c0eaed7b2ca73fb28b8115be019e32b",
                        Material.GOLDEN_HOE, true),
                new MinionTier(10, 16, 960,
                        "dca93d1d2dc3f7ee1ac66ab5280c619fbe37e1e338a6484808a5433b1a3ee911",
                        Material.GOLDEN_HOE, true),
                new MinionTier(11, 13, 960,
                        "166a1693eb7764d0c78d683bb53787db6836518de0b5087869df692dc6be942",
                        Material.DIAMOND_HOE, true),
                new MinionTier(12, 10, 960,
                        "2fb5329dc6085afab236a3cc1245dff1f227c33418e08edac47b530501813cd5",
                        Material.DIAMOND_HOE, false)
        );
    }

    @Override
    public Color getBootColour() {
        return new Color(112,214,96);
    }

    @Override
    public Color getLeggingsColour() {
        return new Color(112,214,96);
    }

    @Override
    public Color getChestplateColour() {
        return new Color(112,214,96);
    }

    @Override
    public List<MinionExpectations> getExpectations() {
        return List.of(
                new MinionExpectations(1, Block.MELON, Block.DIRT)
        );
    }

    @Override
    public MinionAction getAction() {
        return new MinionMinePumpkinOrMelonAction(Block.MELON);
    }
}
