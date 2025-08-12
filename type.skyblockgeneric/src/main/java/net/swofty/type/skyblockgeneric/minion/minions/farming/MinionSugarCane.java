package net.swofty.type.skyblockgeneric.minion.minions.farming;

import net.minestom.server.color.Color;
import net.minestom.server.item.Material;
import net.swofty.type.generic.minion.MinionAction;
import net.swofty.type.generic.minion.SkyBlockMinion;

import java.util.List;

public class MinionSugarCane extends SkyBlockMinion {
    @Override
    public List<MinionTier> getTiers() {
        return List.of(
                new MinionTier(1, 22, 64,
                        "2fced0e80f0d7a5d1f45a1a7217e6a99ea9720156c63f6efc84916d4837fabde",
                        Material.WOODEN_HOE, true),
                new MinionTier(2, 22, 192,
                        "30863e2c1fdce44bc35856c25c039164845456ff1525729d993f0f40ede0f257",
                        Material.WOODEN_HOE, true),
                new MinionTier(3, 20, 192,
                        "f77e3fe28ddc55f385733175e3cf7866a696c0e8ffca4c7de5873cd6cc9fe840",
                        Material.STONE_HOE, true),
                new MinionTier(4, 20, 384,
                        "fbeac9c599b7d794a79a7879f86b10fb7743ad42c9937954d8ffeedc3ce55122",
                        Material.STONE_HOE, true),
                new MinionTier(5, 18, 384,
                        "802e05d0a041aaf3a7fa04d6c97e67a66987c7617ae45311ae2bb6f2005f59c1",
                        Material.STONE_HOE, true),
                new MinionTier(6, 18, 576,
                        "ca9351b61e93840264f5cc6c6b5a882111ae58a404f3bbbe455e07bf868d3975",
                        Material.IRON_HOE, true),
                new MinionTier(7, 16, 576,
                        "f983804d6b4afdcda8050670f51bb3890945fa4fa8a9c3cfa143a3d7912036a3",
                        Material.IRON_HOE, true),
                new MinionTier(8, 16, 768,
                        "f2212f3b630af6b32b77905e4b45fc3f11046ccc9a7dd83b15d429944c4e2102",
                        Material.IRON_HOE, true),
                new MinionTier(9, 14.5f, 768,
                        "49b33dad5234dc354a84f9217daf22684b58d80058de05c785f992f0b226590a",
                        Material.GOLDEN_HOE, true),
                new MinionTier(10, 14.5f, 960,
                        "f1a94db5ee94ffdf4f3e87e5c12c0f112122fa52dc7c15f3881b6190aed4db92",
                        Material.GOLDEN_HOE, true),
                new MinionTier(11, 12, 960,
                        "237514eb4e09053002f242a04997cfb3584928185acf99fa9a1d998bd987e1d7",
                        Material.DIAMOND_HOE, true),
                new MinionTier(12, 9, 960,
                        "601f74920c4d5c0cd4c6509958f049b0608f2367da0035ca5e1542c75656cbc1",
                        Material.DIAMOND_HOE, false)
        );
    }

    @Override
    public Color getBootColour() {
        return new Color(102,242,87);
    }

    @Override
    public Color getLeggingsColour() {
        return new Color(102,242,87);
    }

    @Override
    public Color getChestplateColour() {
        return new Color(102,242,87);
    }

    @Override
    public List<MinionExpectation> getExpectations() {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public MinionAction getAction() {
        throw new RuntimeException("Not implemented yet");
    }
}
