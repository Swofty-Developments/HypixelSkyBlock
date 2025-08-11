package net.swofty.type.skyblockgeneric.minion.minions.combat;

import net.minestom.server.color.Color;
import net.minestom.server.item.Material;
import net.swofty.type.skyblockgeneric.minion.MinionAction;
import net.swofty.type.skyblockgeneric.minion.SkyBlockMinion;

import java.util.List;

public class MinionSlime extends SkyBlockMinion {
    @Override
    public List<MinionTier> getTiers() {
        return List.of(
                new MinionTier(1, 26, 64,
                        "c95eced85db62c922724efca804ea0060c4a87fcdedf2fd5c4f9ac1130a6eb26",
                        Material.WOODEN_SWORD, true),
                new MinionTier(2, 26, 192,
                        "4a3ea6b0c297c5156249353ff7fcf57b1175e1b90b56a815aa039009ff0ea04f",
                        Material.WOODEN_SWORD, true),
                new MinionTier(3, 24, 192,
                        "b6b35286eb19278b65c61a98ed28e04ca56a58386161b1ae6a347c7181cda73b",
                        Material.STONE_SWORD, true),
                new MinionTier(4, 24, 384,
                        "7afc7e081dcc29042129e1b17a10baa05c8e74432600465bf75b31c99bab3fae",
                        Material.STONE_SWORD, true),
                new MinionTier(5, 22, 384,
                        "f0d0c0365bc692b560d8e33e9ef6e232c65907957f6bec4733e3efa4ed03ef58",
                        Material.STONE_SWORD, true),
                new MinionTier(6, 22, 576,
                        "a0356eda9d7227d59ad1c8616bad1bed33831670867755e1bc71a240013de867",
                        Material.IRON_SWORD, true),
                new MinionTier(7, 19, 576,
                        "7266c128064e202143402ac7caee52392e3b003274c25ad8ac5c6773bf863ca2",
                        Material.IRON_SWORD, true),
                new MinionTier(8, 19, 768,
                        "b967e05936b33c2819d32f3aecbecdd478130fccbe877275e131235968ffb6b2",
                        Material.IRON_SWORD, true),
                new MinionTier(9, 16, 768,
                        "827b73cde1cdf73e4393f5177626c681bfaaeaf5c93f9237b6cce4f2f6a74ee8",
                        Material.GOLDEN_SWORD, true),
                new MinionTier(10, 16, 960,
                        "7a2d1ca7dc1a6d9b3b2ee4cf5641bf4add7419f6ac97060898bd98924ab91589",
                        Material.GOLDEN_SWORD, true),
                new MinionTier(11, 12, 960,
                        "c04c9cb411cfd504c3bc7972fc74acd5045c55e1a76379d40e37f5d73c92e453",
                        Material.DIAMOND_SWORD, true)
        );
    }

    @Override
    public Color getBootColour() {
        return new Color(48,242,80);
    }

    @Override
    public Color getLeggingsColour() {
        return new Color(48,242,80);
    }

    @Override
    public Color getChestplateColour() {
        return new Color(48,242,80);
    }

    @Override
    public List<MinionExpectation> getExpectations() {
        return List.of(
                new MobGapExpectation(1)
        );
    }

    @Override
    public MinionAction getAction() {
        throw new RuntimeException("Not implemented yet");
    }
}
