package net.swofty.type.skyblockgeneric.block.blocks;

import lombok.NonNull;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.metadata.other.ArmorStandMeta;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.tag.Tag;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.skyblockgeneric.block.SkyBlockBlock;
import net.swofty.type.skyblockgeneric.block.impl.BlockBreakable;
import net.swofty.type.skyblockgeneric.block.impl.BlockInteractable;
import net.swofty.type.skyblockgeneric.block.impl.BlockPlaceable;
import net.swofty.type.skyblockgeneric.block.impl.CustomSkyBlockBlock;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Arrays;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;

public class BlockExperimentationTable implements CustomSkyBlockBlock, BlockPlaceable, BlockInteractable, BlockBreakable {

    // Lifecycle manager for spawned structures
    private static final PlacedStructureManager structureManager = new PlacedStructureManager();


    @Override
    public @NonNull net.minestom.server.instance.block.Block getDisplayMaterial() {
        return net.minestom.server.instance.block.Block.ENCHANTING_TABLE;
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
    public void onPlace(net.minestom.server.event.player.PlayerBlockPlaceEvent event, SkyBlockBlock block) {
        // This is handled by the interaction listener instead
        if (!(event.getPlayer() instanceof SkyBlockPlayer player)) return;
                        
        // Cancel the default item use
        event.setCancelled(true);
        
        // Calculate placement position (one block forward from player)
        Pos playerPos = player.getPosition();
        Pos placementPos = playerPos.add(
            Math.sin(Math.toRadians(-player.getPosition().yaw())) * 2,
            0,
            Math.cos(Math.toRadians(-player.getPosition().yaw())) * 2
        );
        
        // Handle the placement with direction detection
        BlockExperimentationTable.handlePlacement(player, placementPos);
    }

    @Override
    public void onInteract(PlayerBlockInteractEvent event, SkyBlockBlock block) {
        // Open experiments menu on interaction
        event.setCancelled(true);
        if (event.getPlayer() instanceof SkyBlockPlayer player) {
            try {
                new net.swofty.type.skyblockgeneric.gui.inventories.experiments.GUIExperiments().open(player);
            } catch (Exception ignored) { }
        }
    }

    @Override
    public void onBreak(PlayerBlockBreakEvent event, SkyBlockBlock block) {
        // This is handled by the interaction listener instead
        event.setCancelled(true);
    }

    /**
     * Checks if an item is the Experimentation Table
     */
    public static boolean isExperimentationTable(ItemStack item) {
        if (item == null || item.isAir()) return false;

        // SkyBlock items identify type via the "item_type" tag
        String itemType = item.getTag(Tag.String("item_type"));
        return "EXPERIMENTATION_TABLE".equals(itemType);
    }

    /**
     * Handles placement when a player right-clicks with the Experimentation Table item
     */
    public static void handlePlacement(SkyBlockPlayer player, Pos placementPos) {
        if (!HypixelConst.isIslandServer()) {
            player.sendMessage("§cYou can only place Experimentation Tables on your island!");
            return;
        }

        Instance instance = player.getInstance();
        if (instance == null) {
            player.sendMessage("§cCannot place Experimentation Table - no valid instance!");
            return;
        }

        // Spawn the structure
        spawnStructure(instance, placementPos, player);
        
        // Consume the item unless creative
        if (!player.getGameMode().equals(net.minestom.server.entity.GameMode.CREATIVE)) {
            ItemStack heldItem = player.getItemInMainHand();
            if (isExperimentationTable(heldItem)) {
                player.setItemInMainHand(ItemStack.AIR);
            }
        }
        
        player.sendMessage("§aExperimentation Table placed successfully!");
    }

    /**
     * Model part definition for the Experimentation Table structure
     */
    public enum PartSlot { HEAD, MAIN_HAND, OFF_HAND }
    
    public record ModelPart(
        String name,
        double dx, double dy, double dz,
        float yaw, float pitch, float roll,
        PartSlot slot,
        ItemStack item
    ) {}
    
    /**
     * Directional placement system
     */
    public enum Direction { WEST, NORTH, EAST, SOUTH }
    
    public static Direction getDirection(float yaw) {
        float rot = (yaw % 360 + 360) % 360; // normalize
        if (rot >= 45 && rot < 135) return Direction.EAST;
        if (rot >= 135 && rot < 225) return Direction.SOUTH;
        if (rot >= 225 && rot < 315) return Direction.WEST;
        return Direction.NORTH;
    }

    /**
     * Creates a player head with the specified texture
     */
    private static ItemStack createPlayerHead(String texture) {
        return ItemStackCreator.getStackHead(texture).build();
    }

    /**
     * Complete blueprint for the Experimentation Table structure - WEST direction
     */
    static List<ModelPart> EXP_TABLE_WEST = new ArrayList<>(Arrays.asList(
        new ModelPart("west_0", -0.7188348770141602, 0.3125, -1.1224511907690875, -90.0f, 0.0f, 0f, PartSlot.MAIN_HAND, ItemStack.of(Material.COBBLESTONE_WALL)),
        new ModelPart("west_1", -0.7188348770141602, 0.3125, -0.2162011907690875, -90.0f, 0.0f, 0f, PartSlot.MAIN_HAND, ItemStack.of(Material.COBBLESTONE_WALL)),
        new ModelPart("west_2", 0.6874151229858398, -0.875, 0.0337988092309125, 0.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.DARK_OAK_SLAB)),
        new ModelPart("west_3", 0.6874151229858398, -0.875, -0.6224511907690875, 0.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.DARK_OAK_SLAB)),
        new ModelPart("west_4", 0.6561651229858398, -0.875, 0.6275488092309125, 0.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.DARK_OAK_SLAB)),
        new ModelPart("west_5", 0.5624151229858398, -0.875, -0.9349511907690875, 45.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.DARK_OAK_SLAB)),
        new ModelPart("west_6", 0.5311651229858398, -0.875, 0.9712988092309125, -45.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.DARK_OAK_SLAB)),
        new ModelPart("west_7", 0.24991509318351746, -0.875, 1.0962988092309125, -90.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.DARK_OAK_SLAB)),
        new ModelPart("west_8", 0.24991509318351746, -0.875, -1.0287011907690875, -90.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.DARK_OAK_SLAB)),
        new ModelPart("west_9", 0.37491509318351746, -1.4375, -0.028701190769087503, -90.0f, 0.0f, 0f, PartSlot.HEAD, createPlayerHead("e529abc8830953f4d91edfff24695a9f62758fa4c51b29ac246c3749eaae89b3")),
        new ModelPart("west_10", -0.34383490681648254, -0.84375, 0.0025488092309124966, 0.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.STONE_BRICK_SLAB)),
        new ModelPart("west_11", 0.0, -0.875, -0.5912011907690875, -90.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.STONE_BRICK_SLAB)),
        new ModelPart("west_12", -0.031334906816482544, -0.84375, 0.3462988092309125, -90.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.STONE_BRICK_SLAB)),
        new ModelPart("west_13", 0.5624151229858398, -0.84375, 0.0025488092309124966, 0.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.STONE_BRICK_SLAB)),
        new ModelPart("west_14", 0.21866509318351746, -0.03125, -0.7162011907690875, 0.0f, 0.0f, 0f, PartSlot.MAIN_HAND, ItemStack.of(Material.POLISHED_ANDESITE)),
        new ModelPart("west_15", 0.21866509318351746, -0.03125, 0.1900488092309125, 0.0f, 0.0f, 0f, PartSlot.MAIN_HAND, ItemStack.of(Material.POLISHED_ANDESITE)),
        new ModelPart("west_16", 0.0, -1.1875, 0.0025488092309124966, -90.0f, 0.0f, 0f, PartSlot.HEAD, createPlayerHead("26e6696b63738bbcc5fd973709ea90997215b0cfeece20b2659cb7b35f1bf0")),
        new ModelPart("west_17", 1.3749151229858398, -0.125, 0.8462988092309125, 0.0f, 0.0f, 0f, PartSlot.MAIN_HAND, ItemStack.of(Material.COBBLESTONE_WALL)),
        new ModelPart("west_18", 0.18741509318351746, -0.5625, 1.1900488092309125, -90.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.RED_CARPET)),
        new ModelPart("west_19", 0.09366509318351746, -0.59375, -1.1849511907690875, -90.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.RED_CARPET)),
        new ModelPart("west_20", 0.43741509318351746, -0.5625, -1.0599511907690875, -45.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.RED_CARPET)),
        new ModelPart("west_21", 0.6249151229858398, -0.9375, -1.2787011907690875, -45.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.RED_CARPET)),
        new ModelPart("west_22", 0.12491509318351746, -0.96875, -1.4662011907690875, -90.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.RED_CARPET)),
        new ModelPart("west_23", 0.49991509318351746, -0.65625, 1.0650488092309125, -45.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.RED_CARPET)),
        new ModelPart("west_24", 0.8124151229858398, -0.96875, 1.3462988092309125, 45.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.RED_CARPET)),
        new ModelPart("west_25", 0.21866509318351746, -0.9375, 1.5337988092309125, -90.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.RED_CARPET)),
        new ModelPart("west_26", 1.4061651229858398, -0.125, -1.1849511907690875, 0.0f, 0.0f, 0f, PartSlot.MAIN_HAND, ItemStack.of(Material.COBBLESTONE_WALL)),
        new ModelPart("west_27", -0.18758490681648254, 0.0625, -1.1224511907690875, -45.0f, 0.0f, 0f, PartSlot.HEAD, createPlayerHead("b33598437e313329eb141a13e92d9b0349aabe5c6482a5dde7b73753634aba")),
        new ModelPart("west_28", -0.031334906816482544, 0.0625, -1.5599511907690875, -90.0f, 0.0f, 0f, PartSlot.MAIN_HAND, ItemStack.of(Material.PAPER)),
        new ModelPart("west_29", 0.09366509318351746, 0.03125, -0.9662011907690875, -90.0f, 0.0f, 0f, PartSlot.MAIN_HAND, ItemStack.of(Material.PAPER)),
        new ModelPart("west_30", -0.18758490681648254, -0.09375, 1.0337988092309125, -90.0f, 0.0f, 0f, PartSlot.HEAD, createPlayerHead("7261bf5c7ffa254c1828448032115e5cc154436dec0998c4d7abd996ae1d927")),
        new ModelPart("west_31", 0.21866509318351746, 0.09375, 0.3462988092309125, -90.0f, 0.0f, 0f, PartSlot.MAIN_HAND, ItemStack.of(Material.ENCHANTED_BOOK)),
        new ModelPart("west_32", -0.37508490681648254, -0.34375, -0.4349511907690875, -90.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.ALLIUM)),
        new ModelPart("west_33", -0.40633490681648254, -0.34375, 0.4087988092309125, -90.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.ALLIUM))
    ));

    /**
     * Complete blueprint for the Experimentation Table structure - EAST direction
     */
    static List<ModelPart> EXP_TABLE_EAST = new ArrayList<>(Arrays.asList(
        new ModelPart("east_0", -0.6863517761230469, -0.875, -0.03156512983695947, -180.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.DARK_OAK_SLAB)),
        new ModelPart("east_1", -0.6863517761230469, -0.875, 0.6246848701630405, -180.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.DARK_OAK_SLAB)),
        new ModelPart("east_2", -0.6551017761230469, -0.875, -0.6253151298369595, -180.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.DARK_OAK_SLAB)),
        new ModelPart("east_3", -0.5613517761230469, -0.875, 0.9371848701630405, -135.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.DARK_OAK_SLAB)),
        new ModelPart("east_4", -0.5301017761230469, -0.875, -0.9690651298369595, 135.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.DARK_OAK_SLAB)),
        new ModelPart("east_5", -0.24885180592536926, -0.875, -1.0940651298369595, 90.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.DARK_OAK_SLAB)),
        new ModelPart("east_6", -0.24885180592536926, -0.875, 1.0309348701630405, 90.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.DARK_OAK_SLAB)),
        new ModelPart("east_7", -0.37385180592536926, -1.4375, 0.03093487016304053, 90.0f, 0.0f, 0f, PartSlot.HEAD, createPlayerHead("e529abc8830953f4d91edfff24695a9f62758fa4c51b29ac246c3749eaae89b3")),
        new ModelPart("east_8", 0.7198982238769531, 0.3125, 1.1246848701630405, 90.0f, 0.0f, 0f, PartSlot.MAIN_HAND, ItemStack.of(Material.COBBLESTONE_WALL)),
        new ModelPart("east_9", 0.34489819407463074, -0.84375, -3.151298369594E-4, -180.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.STONE_BRICK_SLAB)),
        new ModelPart("east_10", 0.001148198964074254, -0.875, 0.5934348701630405, 90.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.STONE_BRICK_SLAB)),
        new ModelPart("east_11", 0.032398197799921036, -0.84375, -0.34406512983695947, 90.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.STONE_BRICK_SLAB)),
        new ModelPart("east_12", -0.5613517761230469, -0.84375, -3.15129836959E-4, -180.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.STONE_BRICK_SLAB)),
        new ModelPart("east_13", -0.21760180592536926, -0.03125, 0.7184348701630405, -180.0f, 0.0f, 0f, PartSlot.MAIN_HAND, ItemStack.of(Material.POLISHED_ANDESITE)),
        new ModelPart("east_14", -0.21760180592536926, -0.03125, -0.18781512983695947, -180.0f, 0.0f, 0f, PartSlot.MAIN_HAND, ItemStack.of(Material.POLISHED_ANDESITE)),
        new ModelPart("east_15", 0.001148198964074254, -1.1875, -3.15129836959E-4, 90.0f, 0.0f, 0f, PartSlot.HEAD, createPlayerHead("26e6696b63738bbcc5fd973709ea90997215b0cfeece20b2659cb7b35f1bf0")),
        new ModelPart("east_16", 0.7198982238769531, 0.3125, 0.21843487016304053, 90.0f, 0.0f, 0f, PartSlot.MAIN_HAND, ItemStack.of(Material.COBBLESTONE_WALL)),
        new ModelPart("east_17", -1.3738517761230469, -0.125, -0.8440651298369595, -180.0f, 0.0f, 0f, PartSlot.MAIN_HAND, ItemStack.of(Material.COBBLESTONE_WALL)),
        new ModelPart("east_18", -0.18635180592536926, -0.5625, -1.1878151298369595, 90.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.RED_CARPET)),
        new ModelPart("east_19", -0.09260179847478867, -0.59375, 1.1871848701630405, 90.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.RED_CARPET)),
        new ModelPart("east_20", -0.43635180592536926, -0.5625, 1.0621848701630405, 135.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.RED_CARPET)),
        new ModelPart("east_21", -0.6238517761230469, -0.9375, 1.2809348701630405, 135.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.RED_CARPET)),
        new ModelPart("east_22", -0.12385179847478867, -0.96875, 1.4684348701630405, 90.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.RED_CARPET)),
        new ModelPart("east_23", -0.49885180592536926, -0.65625, -1.0628151298369595, 135.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.RED_CARPET)),
        new ModelPart("east_24", -0.8113517761230469, -0.96875, -1.3440651298369595, -135.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.RED_CARPET)),
        new ModelPart("east_25", -0.21760180592536926, -0.9375, -1.5315651298369595, 90.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.RED_CARPET)),
        new ModelPart("east_26", -1.4051017761230469, -0.125, 1.1871848701630405, -180.0f, 0.0f, 0f, PartSlot.MAIN_HAND, ItemStack.of(Material.COBBLESTONE_WALL)),
        new ModelPart("east_27", 0.18864819407463074, 0.0625, 1.1246848701630405, 135.0f, 0.0f, 0f, PartSlot.HEAD, createPlayerHead("b33598437e313329eb141a13e92d9b0349aabe5c6482a5dde7b73753634aba")),
        new ModelPart("east_28", 0.032398197799921036, 0.0625, 1.5621848701630405, 90.0f, 0.0f, 0f, PartSlot.MAIN_HAND, ItemStack.of(Material.PAPER)),
        new ModelPart("east_29", -0.09260179847478867, 0.03125, 0.9684348701630405, 90.0f, 0.0f, 0f, PartSlot.MAIN_HAND, ItemStack.of(Material.PAPER)),
        new ModelPart("east_30", 0.18864819407463074, -0.09375, -1.0315651298369595, 90.0f, 0.0f, 0f, PartSlot.HEAD, createPlayerHead("7261bf5c7ffa254c1828448032115e5cc154436dec0998c4d7abd996ae1d927")),
        new ModelPart("east_31", -0.21760180592536926, 0.09375, -0.34406512983695947, 90.0f, 0.0f, 0f, PartSlot.MAIN_HAND, ItemStack.of(Material.ENCHANTED_BOOK)),
        new ModelPart("east_32", 0.37614819407463074, -0.34375, 0.43718487016304053, 90.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.ALLIUM)),
        new ModelPart("east_33", 0.40739819407463074, -0.34375, -0.40656512983695947, 90.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.ALLIUM))
    ));

    /**
     * Complete blueprint for the Experimentation Table structure - NORTH direction
     */
    static List<ModelPart> EXP_TABLE_NORTH = new ArrayList<>(Arrays.asList(
        new ModelPart("north_0", -0.028958698734641075, -0.875, 0.6866584740658261, 90.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.DARK_OAK_SLAB)),
        new ModelPart("north_1", 0.6272913217544556, -0.875, 0.6866584740658261, 90.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.DARK_OAK_SLAB)),
        new ModelPart("north_2", -0.6227086782455444, -0.875, 0.6554084740658261, 90.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.DARK_OAK_SLAB)),
        new ModelPart("north_3", 0.9397913217544556, -0.875, 0.5616584740658261, 135.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.DARK_OAK_SLAB)),
        new ModelPart("north_4", -0.9664586782455444, -0.875, 0.5304084740658261, 45.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.DARK_OAK_SLAB)),
        new ModelPart("north_5", -1.0914586782455444, -0.875, 0.24915847406582614, 0.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.DARK_OAK_SLAB)),
        new ModelPart("north_6", 1.0335413217544556, -0.875, 0.24915847406582614, 0.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.DARK_OAK_SLAB)),
        new ModelPart("north_7", 0.033541299402713776, -1.4375, 0.37415847406582614, 0.0f, 0.0f, 0f, PartSlot.HEAD, createPlayerHead("e529abc8830953f4d91edfff24695a9f62758fa4c51b29ac246c3749eaae89b3")),
        new ModelPart("north_8", 1.1272913217544556, 0.3125, -0.7195915259341739, 0.0f, 0.0f, 0f, PartSlot.MAIN_HAND, ItemStack.of(Material.COBBLESTONE_WALL)),
        new ModelPart("north_9", 0.002291301032528281, -0.84375, -0.34459152593417386, 90.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.STONE_BRICK_SLAB)),
        new ModelPart("north_10", 0.5960413217544556, -0.875, -8.4152593417E-5, 0.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.STONE_BRICK_SLAB)),
        new ModelPart("north_11", -0.3414587080478668, -0.84375, -0.03209152593417386, 0.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.STONE_BRICK_SLAB)),
        new ModelPart("north_12", 0.002291301032528281, -0.84375, 0.5616584740658261, 90.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.STONE_BRICK_SLAB)),
        new ModelPart("north_13", 0.7210413217544556, -0.03125, 0.21790847406582614, 90.0f, 0.0f, 0f, PartSlot.MAIN_HAND, ItemStack.of(Material.POLISHED_ANDESITE)),
        new ModelPart("north_14", -0.18520869314670563, -0.03125, 0.21790847406582614, 90.0f, 0.0f, 0f, PartSlot.MAIN_HAND, ItemStack.of(Material.POLISHED_ANDESITE)),
        new ModelPart("north_15", 0.002291301032528281, -1.1875, -8.0E-5, 0.0f, 0.0f, 0f, PartSlot.HEAD, createPlayerHead("26e6696b63738bbcc5fd973709ea90997215b0cfeece20b2659cb7b35f1bf0")),
        new ModelPart("north_16", 0.22104130685329437, 0.3125, -0.7195915259341739, 0.0f, 0.0f, 0f, PartSlot.MAIN_HAND, ItemStack.of(Material.COBBLESTONE_WALL)),
        new ModelPart("north_17", -0.8414586782455444, -0.125, 1.3741584740658261, 90.0f, 0.0f, 0f, PartSlot.MAIN_HAND, ItemStack.of(Material.COBBLESTONE_WALL)),
        new ModelPart("north_18", -1.1852086782455444, -0.5625, 0.18665847406582614, 0.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.RED_CARPET)),
        new ModelPart("north_19", 1.1897913217544556, -0.59375, 0.09290847406582614, 0.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.RED_CARPET)),
        new ModelPart("north_20", 1.0647913217544556, -0.5625, 0.43665847406582614, 45.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.RED_CARPET)),
        new ModelPart("north_21", 1.2835413217544556, -0.9375, 0.6241584740658261, 45.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.RED_CARPET)),
        new ModelPart("north_22", 1.4710413217544556, -0.96875, 0.12415847406582614, 0.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.RED_CARPET)),
        new ModelPart("north_23", -1.0602086782455444, -0.65625, 0.49915847406582614, 45.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.RED_CARPET)),
        new ModelPart("north_24", -1.3414586782455444, -0.96875, 0.8116584740658261, 135.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.RED_CARPET)),
        new ModelPart("north_25", -1.5289586782455444, -0.9375, 0.21790847406582614, 0.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.RED_CARPET)),
        new ModelPart("north_26", 1.1897913217544556, -0.125, 1.4054084740658261, 90.0f, 0.0f, 0f, PartSlot.MAIN_HAND, ItemStack.of(Material.COBBLESTONE_WALL)),
        new ModelPart("north_27", 1.1272913217544556, 0.0625, -0.18834152593417386, 45.0f, 0.0f, 0f, PartSlot.HEAD, createPlayerHead("b33598437e313329eb141a13e92d9b0349aabe5c6482a5dde7b73753634aba")),
        new ModelPart("north_28", 1.5647913217544556, 0.0625, -0.03209152593417386, 0.0f, 0.0f, 0f, PartSlot.MAIN_HAND, ItemStack.of(Material.PAPER)),
        new ModelPart("north_29", 0.9710413217544556, 0.03125, 0.09290847406582614, 0.0f, 0.0f, 0f, PartSlot.MAIN_HAND, ItemStack.of(Material.PAPER)),
        new ModelPart("north_30", -1.0289586782455444, -0.09375, -0.18834152593417386, 0.0f, 0.0f, 0f, PartSlot.HEAD, createPlayerHead("7261bf5c7ffa254c1828448032115e5cc154436dec0998c4d7abd996ae1d927")),
        new ModelPart("north_31", -0.3414587080478668, 0.09375, 0.21790847406582614, 0.0f, 0.0f, 0f, PartSlot.MAIN_HAND, ItemStack.of(Material.ENCHANTED_BOOK)),
        new ModelPart("north_32", 0.4397912919521332, -0.34375, -0.37584152593417386, 0.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.ALLIUM)),
        new ModelPart("north_33", -0.4039587080478668, -0.34375, -0.40709152593417386, 0.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.ALLIUM))
    ));

    /**
     * Complete blueprint for the Experimentation Table structure - SOUTH direction
     */
    static List<ModelPart> EXP_TABLE_SOUTH = new ArrayList<>(Arrays.asList(
        new ModelPart("south_0", 0.03416445851325989, -0.875, -0.6838063466525668, -90.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.DARK_OAK_SLAB)),
        new ModelPart("south_1", -0.6220855116844177, -0.875, -0.6838063466525668, -90.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.DARK_OAK_SLAB)),
        new ModelPart("south_2", 0.6279144883155823, -0.875, -0.6525563466525668, -90.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.DARK_OAK_SLAB)),
        new ModelPart("south_3", -0.9345855116844177, -0.875, -0.5588063466525668, -45.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.DARK_OAK_SLAB)),
        new ModelPart("south_4", 0.9716644883155823, -0.875, -0.5275563466525668, -135.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.DARK_OAK_SLAB)),
        new ModelPart("south_5", 1.0966644287109375, -0.875, -0.2463063466525668, -180.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.DARK_OAK_SLAB)),
        new ModelPart("south_6", -1.0283355712890625, -0.875, -0.2463063466525668, -180.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.DARK_OAK_SLAB)),
        new ModelPart("south_7", -0.028335539624094963, -1.4375, -0.3713063466525668, -180.0f, 0.0f, 0f, PartSlot.HEAD, createPlayerHead("e529abc8830953f4d91edfff24695a9f62758fa4c51b29ac246c3749eaae89b3")),
        new ModelPart("south_8", -1.1220855712890625, 0.3125, 0.7224436533474332, -180.0f, 0.0f, 0f, PartSlot.MAIN_HAND, ItemStack.of(Material.COBBLESTONE_WALL)),
        new ModelPart("south_9", 0.002914459677413106, -0.84375, 0.3474436533474332, -90.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.STONE_BRICK_SLAB)),
        new ModelPart("south_10", -0.5908355116844177, -0.875, 0.0036936533474332123, -180.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.STONE_BRICK_SLAB)),
        new ModelPart("south_11", 0.3466644585132599, -0.84375, 0.03494365334743321, -180.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.STONE_BRICK_SLAB)),
        new ModelPart("south_12", 0.002914459677413106, -0.84375, -0.5588063466525668, -90.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.STONE_BRICK_SLAB)),
        new ModelPart("south_13", -0.7158355116844177, -0.03125, -0.2150563466525668, -90.0f, 0.0f, 0f, PartSlot.MAIN_HAND, ItemStack.of(Material.POLISHED_ANDESITE)),
        new ModelPart("south_14", 0.1904144585132599, -0.03125, -0.2150563466525668, -90.0f, 0.0f, 0f, PartSlot.MAIN_HAND, ItemStack.of(Material.POLISHED_ANDESITE)),
        new ModelPart("south_15", 0.002914459677413106, -1.1875, 0.0036936533474332123, -180.0f, 0.0f, 0f, PartSlot.HEAD, createPlayerHead("26e6696b63738bbcc5fd973709ea90997215b0cfeece20b2659cb7b35f1bf0")),
        new ModelPart("south_16", -0.2158355414867401, 0.3125, 0.7224436533474332, -180.0f, 0.0f, 0f, PartSlot.MAIN_HAND, ItemStack.of(Material.COBBLESTONE_WALL)),
        new ModelPart("south_17", 0.8466644883155823, -0.125, -1.3713063466525668, -90.0f, 0.0f, 0f, PartSlot.MAIN_HAND, ItemStack.of(Material.COBBLESTONE_WALL)),
        new ModelPart("south_18", 1.1904144287109375, -0.5625, -0.1838063466525668, -180.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.RED_CARPET)),
        new ModelPart("south_19", -1.1845855712890625, -0.59375, -0.09005634665256679, -180.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.RED_CARPET)),
        new ModelPart("south_20", -1.0595855712890625, -0.5625, -0.4338063466525668, -135.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.RED_CARPET)),
        new ModelPart("south_21", -1.2783355712890625, -0.9375, -0.6213063466525668, -135.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.RED_CARPET)),
        new ModelPart("south_22", -1.4658355712890625, -0.96875, -0.12130634665256679, -180.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.RED_CARPET)),
        new ModelPart("south_23", 1.0654144287109375, -0.65625, -0.4963063466525668, -135.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.RED_CARPET)),
        new ModelPart("south_24", 1.3466644287109375, -0.96875, -0.8088063466525668, -45.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.RED_CARPET)),
        new ModelPart("south_25", 1.5341644287109375, -0.9375, -0.2150563466525668, -180.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.RED_CARPET)),
        new ModelPart("south_26", -1.1845855712890625, -0.125, -1.4025563466525668, -90.0f, 0.0f, 0f, PartSlot.MAIN_HAND, ItemStack.of(Material.COBBLESTONE_WALL)),
        new ModelPart("south_27", -1.1220855712890625, 0.0625, 0.1911936533474332, -135.0f, 0.0f, 0f, PartSlot.HEAD, createPlayerHead("b33598437e313329eb141a13e92d9b0349aabe5c6482a5dde7b73753634aba")),
        new ModelPart("south_28", -1.5595855712890625, 0.0625, 0.03494365334743321, -180.0f, 0.0f, 0f, PartSlot.MAIN_HAND, ItemStack.of(Material.PAPER)),
        new ModelPart("south_29", -0.9658355116844177, 0.03125, -0.09005634665256679, -180.0f, 0.0f, 0f, PartSlot.MAIN_HAND, ItemStack.of(Material.PAPER)),
        new ModelPart("south_30", 1.0341644287109375, -0.09375, 0.1911936533474332, -180.0f, 0.0f, 0f, PartSlot.HEAD, createPlayerHead("7261bf5c7ffa254c1828448032115e5cc154436dec0998c4d7abd996ae1d927")),
        new ModelPart("south_31", 0.3466644585132599, 0.09375, -0.2150563466525668, -180.0f, 0.0f, 0f, PartSlot.MAIN_HAND, ItemStack.of(Material.ENCHANTED_BOOK)),
        new ModelPart("south_32", -0.4345855414867401, -0.34375, 0.3786936533474332, -180.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.ALLIUM)),
        new ModelPart("south_33", 0.4091644585132599, -0.34375, 0.4099436533474332, -180.0f, 0.0f, 0f, PartSlot.HEAD, ItemStack.of(Material.ALLIUM))
    ));


    public static void setBlueprint(List<ModelPart> parts) {
        // This method is kept for compatibility but now uses directional blueprints
        // The directional blueprints are now used instead of a single blueprint
    }

    // JSON-based blueprint loading removed; using in-code blueprint only

    /**
     * Spawns the Experimentation Table structure at the given position
     */
    public static void spawnStructure(Instance instance, Pos position, SkyBlockPlayer player) {
        UUID masterUuid = UUID.randomUUID();
        List<UUID> entityUuids = new ArrayList<>();

        // Determine direction based on player's yaw
        Direction direction = getDirection(player.getPosition().yaw());
        List<ModelPart> blueprint = getBlueprintForDirection(direction);

        for (ModelPart part : blueprint) {
            LivingEntity armorStand = new LivingEntity(EntityType.ARMOR_STAND);
            
            ArmorStandMeta meta = (ArmorStandMeta) armorStand.getEntityMeta();
            meta.setInvisible(true);
            meta.setHasNoGravity(true);
            meta.setSmall(true);
            meta.setHasNoBasePlate(true);
            meta.setHasArms(true);
            
            Pos partPos = position.add(part.dx, part.dy, part.dz);
            armorStand.setInstance(instance, partPos);
            armorStand.setView(0, 0); // Keep entity facing forward for invisible stands
            
            // Apply rotation to the entity view for proper orientation
            if (part.yaw != 0 || part.pitch != 0) {
                armorStand.setView(part.yaw, part.pitch);
            }
            
            switch (part.slot) {
                case HEAD -> armorStand.setHelmet(part.item);
                case MAIN_HAND -> armorStand.setEquipment(EquipmentSlot.MAIN_HAND, part.item);
                case OFF_HAND -> armorStand.setEquipment(EquipmentSlot.OFF_HAND, part.item);
            }
            
            // Add interaction capability to the armor stand
            armorStand.setTag(Tag.String("master_uuid"), masterUuid.toString());
            armorStand.setTag(Tag.String("is_experimentation_table"), "true");
            entityUuids.add(armorStand.getUuid());
        }
        
        structureManager.registerStructure(masterUuid, entityUuids, instance, position);
        player.sendMessage("§aExperimentation Table placed.");
    }

    /**
     * Gets the appropriate blueprint for the given direction
     */
    private static List<ModelPart> getBlueprintForDirection(Direction direction) {
        return switch (direction) {
            case WEST -> EXP_TABLE_WEST;
            case EAST -> EXP_TABLE_EAST;
            case NORTH -> EXP_TABLE_NORTH;
            case SOUTH -> EXP_TABLE_SOUTH;
        };
    }


    /**
     * Removes a structure by its master UUID
     */
    public static void removeStructure(UUID masterUuid) {
        structureManager.removeStructure(masterUuid);
    }

    public static void removeAllPlacedStructures() {
        structureManager.removeAllStructures();
    }

    /**
     * Manages placed structures and their cleanup
     */
    private static class PlacedStructureManager {
        private final Map<UUID, StructureData> structures = new ConcurrentHashMap<>();
        
        public void registerStructure(UUID masterUuid, List<UUID> entityUuids, Instance instance, Pos position) {
            structures.put(masterUuid, new StructureData(entityUuids, instance, position));
        }
        
        public void removeStructure(UUID masterUuid) {
            StructureData data = structures.remove(masterUuid);
            if (data != null) {
                data.removeAllEntities();
            }
        }
        
        public StructureData getStructure(UUID masterUuid) {
            return structures.get(masterUuid);
        }
        
        public void removeAllStructures() {
            structures.values().forEach(StructureData::removeAllEntities);
            structures.clear();
        }
    }
    
    private static class StructureData {
        private final List<UUID> entityUuids;
        private final Instance instance;
        private final Pos position;
        
        public StructureData(List<UUID> entityUuids, Instance instance, Pos position) {
            this.entityUuids = entityUuids;
            this.instance = instance;
            this.position = position;
        }
        
        public void removeAllEntities() {
            entityUuids.forEach(uuid -> {
                // Find entity by UUID in the instance
                instance.getEntities().forEach(entity -> {
                    if (entity.getUuid().equals(uuid)) {
                        entity.remove();
                    }
                });
            });
        }
    }
}


