package net.swofty.type.skyblockgeneric.block.blocks;

import lombok.NonNull;
import net.minestom.server.coordinate.Point;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.tag.Tag;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.skyblockgeneric.block.SkyBlockBlock;
import net.swofty.type.skyblockgeneric.block.impl.BlockBreakable;
import net.swofty.type.skyblockgeneric.block.impl.BlockInteractable;
import net.swofty.type.skyblockgeneric.block.impl.BlockPlaceable;
import net.swofty.type.skyblockgeneric.block.impl.CustomSkyBlockBlock;
import net.swofty.type.skyblockgeneric.gui.inventories.experiments.GUIExperiments;
import net.swofty.type.skyblockgeneric.structure.SkyBlockStructure;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class BlockExperimentationTable implements CustomSkyBlockBlock, BlockPlaceable, BlockInteractable, BlockBreakable {

    private static final Tag<Integer> ORIGIN_X = Tag.Integer("exp_origin_x");
    private static final Tag<Integer> ORIGIN_Y = Tag.Integer("exp_origin_y");
    private static final Tag<Integer> ORIGIN_Z = Tag.Integer("exp_origin_z");
    private static final Tag<Integer> ROTATION = Tag.Integer("exp_rotation");

    @Override
    public @NonNull Block getDisplayMaterial() {
        return Block.CAULDRON; // base placeholder; structure will build full visuals
    }

    @Override
    public @NonNull Boolean shouldPlace(SkyBlockPlayer player) {
        return HypixelConst.isIslandServer();
    }

    @Override
    public @NonNull Boolean shouldDestroy(SkyBlockPlayer player) {
        return HypixelConst.isIslandServer();
    }

    @Override
    public void onPlace(PlayerBlockPlaceEvent event, SkyBlockBlock block) {
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
        if (!shouldPlace(player)) {
            event.setCancelled(true);
            return;
        }

        Instance instance = event.getInstance();
        Point pos = event.getBlockPosition();

        // Cancel vanilla placement; we will place tagged base and build structure
        event.setCancelled(true);

        int rotation = yawToRotation(player.getPosition().yaw());

        // Place tagged base block at origin
        Block base = block.toBlock()
                .withTag(ORIGIN_X, (int) pos.x())
                .withTag(ORIGIN_Y, (int) pos.y())
                .withTag(ORIGIN_Z, (int) pos.z())
                .withTag(ROTATION, rotation);
        instance.setBlock(pos, base);

        // Build the multi-block structure
        new StructureExperimentationTable(rotation, (int) pos.x(), (int) pos.y(), (int) pos.z())
                .setBlocks(instance);

        // Consume one item
        SkyBlockPlayer sbp = (SkyBlockPlayer) event.getPlayer();
        sbp.setItemInMainHand(sbp.getItemInMainHand().withAmount(Math.max(0, sbp.getItemInMainHand().amount() - 1)));
    }

    @Override
    public void onInteract(net.minestom.server.event.player.PlayerBlockInteractEvent event, SkyBlockBlock block) {
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
        if (!HypixelConst.isIslandServer()) return;
        event.setBlockingItemUse(true);
        new GUIExperiments().open(player);
    }

    @Override
    public void onBreak(PlayerBlockBreakEvent event, SkyBlockBlock block) {
        Instance instance = event.getInstance();
        Integer ox = event.getBlock().getTag(ORIGIN_X);
        Integer oy = event.getBlock().getTag(ORIGIN_Y);
        Integer oz = event.getBlock().getTag(ORIGIN_Z);
        Integer rot = event.getBlock().getTag(ROTATION);

        // Remove structure if tags exist
        if (ox != null && oy != null && oz != null && rot != null) {
            new StructureExperimentationTable(rot, ox, oy, oz).clear(instance);
        }

        event.setResultBlock(Block.AIR);
        // Drop logic could be added by giving back the item to the player if desired
    }

    private int yawToRotation(float yaw) {
        float normalized = (yaw % 360 + 360) % 360;
        if (normalized >= 315 || normalized < 45) return 0; // north
        if (normalized < 135) return 1; // east
        if (normalized < 225) return 2; // south
        return 3; // west
    }

    public static class StructureExperimentationTable extends SkyBlockStructure {
        public StructureExperimentationTable(int rotation, int x, int y, int z) {
            super(rotation, x, y, z);
        }

        @Override
        public void setBlocks(Instance instance) {
            // Center basin ring + core
            set(instance, 0, 1, 0, Block.END_PORTAL);
            set(instance, 1, 1, 0, Block.STONE_BRICK_STAIRS
                    .withProperty("facing", "west")
                    .withProperty("half", "bottom")
                    .withProperty("shape", "straight"));
            set(instance, -1, 1, 0, Block.STONE_BRICK_STAIRS
                    .withProperty("facing", "east")
                    .withProperty("half", "bottom")
                    .withProperty("shape", "straight"));
            set(instance, 0, 1, 1, Block.STONE_BRICK_STAIRS
                    .withProperty("facing", "north")
                    .withProperty("half", "bottom")
                    .withProperty("shape", "straight"));
            set(instance, 0, 1, -1, Block.STONE_BRICK_STAIRS
                    .withProperty("facing", "south")
                    .withProperty("half", "bottom")
                    .withProperty("shape", "straight"));
            set(instance, 1, 1, 1, Block.STONE_BRICK_SLAB);
            set(instance, 1, 1, -1, Block.STONE_BRICK_SLAB);
            set(instance, -1, 1, 1, Block.STONE_BRICK_SLAB);
            set(instance, -1, 1, -1, Block.STONE_BRICK_SLAB);

            // Crystal pylons
            set(instance, 0, 2, -1, Block.STONE_BRICK_WALL);
            set(instance, 0, 3, -1, Block.END_ROD);
            set(instance, 0, 4, -1, Block.AMETHYST_CLUSTER);
            set(instance, 0, 2, 1, Block.STONE_BRICK_WALL);
            set(instance, 0, 3, 1, Block.END_ROD);
            set(instance, 0, 4, 1, Block.AMETHYST_CLUSTER);

            // Benches left and right (slabs)
            fill(instance, -4, 1, -1, -2, 1, 1, Block.OAK_SLAB.withProperty("type", "top"));
            fill(instance, 2, 1, -1, 4, 1, 1, Block.OAK_SLAB.withProperty("type", "top"));
            // Cloth
            fill(instance, -4, 2, -1, -2, 2, 1, Block.RED_CARPET);
            fill(instance, 2, 2, -1, 4, 2, 1, Block.RED_CARPET);
            // Diamond feet
            set(instance, -5, 1, -1, Block.DIAMOND_BLOCK);
            set(instance, -5, 1, 1, Block.DIAMOND_BLOCK);
            set(instance, 5, 1, -1, Block.DIAMOND_BLOCK);
            set(instance, 5, 1, 1, Block.DIAMOND_BLOCK);
            // Red legs
            set(instance, -4, 1, -2, Block.CRIMSON_FENCE);
            set(instance, -4, 1, 2, Block.CRIMSON_FENCE);
            set(instance, 4, 1, -2, Block.CRIMSON_FENCE);
            set(instance, 4, 1, 2, Block.CRIMSON_FENCE);

            // Front fascia and under accent
            set(instance, 0, 0, -1, Block.ENCHANTING_TABLE);
            Block frontTrap = Block.DARK_OAK_TRAPDOOR
                    .withProperty("facing", "south")
                    .withProperty("half", "top")
                    .withProperty("open", "false");
            set(instance, -2, 2, -2, frontTrap);
            set(instance, -1, 2, -2, frontTrap);
            set(instance, 0, 2, -2, frontTrap);
            set(instance, 1, 2, -2, frontTrap);
            set(instance, 2, 2, -2, frontTrap);

            // Props
            set(instance, -3, 2, 0, Block.PURPLE_STAINED_GLASS);
            set(instance, 3, 2, 0, Block.LECTERN);
            set(instance, 3, 2, 1, Block.WHITE_CARPET);
        }

        public void clear(Instance instance) {
            // Clear the known footprint (conservative bounding box)
            fill(instance, -5, 0, -3, 5, 4, 3, Block.AIR);
        }

        @Override
        public java.util.List<SkyBlockStructure.StructureHologram> getHolograms() {
            return java.util.Collections.emptyList();
        }
    }
}


