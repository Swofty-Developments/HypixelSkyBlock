package net.swofty.types.generic.minion.minions.farming;

import net.minestom.server.color.Color;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.types.generic.minion.MinionAction;
import net.swofty.types.generic.minion.SkyBlockMinion;
import net.swofty.types.generic.minion.actions.MinionMineAction;

import java.util.List;

public class MinionNetherWart extends SkyBlockMinion {
    @Override
    public List<MinionTier> getTiers() {
        return List.of(
                new MinionTier(1, 50, 64,
                        "71a4620bb3459c1c2fa74b210b1c07b4a02254351f75173e643a0e009a63f558",
                        Material.WOODEN_SWORD, true),
                new MinionTier(2, 50, 192,
                        "153a8dc9cf122c9d045e540d0624ccc348a85b8829074593d9262543671dc213",
                        Material.WOODEN_SWORD, true),
                new MinionTier(3, 47, 192,
                        "d7820332f21afe31d88f42111364cb8aa33746b6f1e7581fb5f50bbfe870f0ad",
                        Material.STONE_SWORD, true),
                new MinionTier(4, 47, 384,
                        "4b07451870cbd1804654e5b5db62e700efad8e7bcc7bf113a54ef6f5a5ab47e6",
                        Material.STONE_SWORD, true),
                new MinionTier(5, 44, 384,
                        "8ef39a8e6958dafc2b5dbc55993d63e065f3d88e62e94ac6d28c865d85b9432",
                        Material.STONE_SWORD, true),
                new MinionTier(6, 44, 576,
                        "3245cbc9d11455ad30f9b7860604a372cc6bdba64ebe13babf6815de9ac5ab89",
                        Material.IRON_SWORD, true),
                new MinionTier(7, 41, 576,
                        "d45298dcfb39274d0eed5df91ad744d7161d75da155f52955c44767231e88584",
                        Material.IRON_SWORD, true),
                new MinionTier(8, 41, 768,
                        "b5c14d391dd776ebd5d0245bb762495371f666f5772022e82aebfdffc9b9447",
                        Material.IRON_SWORD, true),
                new MinionTier(9, 38, 768,
                        "3d8780f780548eb3d7fce773c09e89307a090b175570271808953eb81b5a9d72",
                        Material.GOLDEN_SWORD, true),
                new MinionTier(10, 38, 960,
                        "3e5291d28b362a5d8c17b521a317b3a66d68f0ed9e8f322b65db0c32c42e10a2",
                        Material.GOLDEN_SWORD, true),
                new MinionTier(11, 32, 960,
                        "79d99c73e1f9a5376bd697c7ccbe3844f762a2b196fa72a5831988747aaacfa",
                        Material.DIAMOND_SWORD, true),
                new MinionTier(12, 27, 960,
                        "4883d4d2b7240ef7f4750ea5a1f7c5ed95113f92d0d2089fa38717d3d7efc438",
                        Material.DIAMOND_SWORD, false)
        );
    }

    @Override
    public Color getBootColour() {
        return new Color(189,59,229);
    }

    @Override
    public Color getLeggingsColour() {
        return new Color(189,59,229);
    }

    @Override
    public Color getChestplateColour() {
        return new Color(189,59,229);
    }

    @Override
    public List<MinionExpectations> getExpectations() {
        return List.of(
                new MinionExpectations(1, Block.NETHER_WART, Block.SOUL_SAND)
        );
    }

    @Override
    public MinionAction getAction() {
        return new MinionMineAction(Block.NETHER_WART);
    }
}
