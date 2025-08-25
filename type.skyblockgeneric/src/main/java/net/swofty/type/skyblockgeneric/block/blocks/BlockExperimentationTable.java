package net.swofty.type.skyblockgeneric.block.blocks;

import lombok.NonNull;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.metadata.other.ArmorStandMeta;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.entity.EquipmentSlot;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.skyblockgeneric.block.SkyBlockBlock;
import net.swofty.type.skyblockgeneric.block.impl.BlockBreakable;
import net.swofty.type.skyblockgeneric.block.impl.BlockInteractable;
import net.swofty.type.skyblockgeneric.block.impl.BlockPlaceable;
import net.swofty.type.skyblockgeneric.block.impl.CustomSkyBlockBlock;
import net.swofty.type.skyblockgeneric.gui.inventories.experiments.GUIExperiments;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;

public class BlockExperimentationTable implements CustomSkyBlockBlock, BlockPlaceable, BlockInteractable, BlockBreakable {

    // Store visual structure data for cleanup
    private static final List<ExperimentationTableStructure> activeStructures = new ArrayList<>();

    @Override
    public @NonNull Block getDisplayMaterial() {
        return Block.ENCHANTING_TABLE; // matches Hypixel's experimentation table
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

        // Allow normal placement
        event.setCancelled(false);
        
        // Create the visual structure after placement
        Pos blockPos = new Pos(event.getBlockPosition().x(), event.getBlockPosition().y(), event.getBlockPosition().z());
        Instance instance = event.getInstance();
        
        // Spawn the visual structure
        ExperimentationTableStructure structure = new ExperimentationTableStructure(instance, blockPos);
        structure.spawn();
        activeStructures.add(structure);
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
        // Clean up the visual structure
        Pos blockPos = new Pos(event.getBlockPosition().x(), event.getBlockPosition().y(), event.getBlockPosition().z());
        Instance instance = event.getInstance();
        
        // Find and remove the structure
        activeStructures.removeIf(structure -> {
            if (structure.instance.equals(instance) && structure.basePos.equals(blockPos)) {
                structure.remove();
                return true;
            }
            return false;
        });
        
        event.setCancelled(false);
    }

    /**
     * Represents the visual structure of an Experimentation Table
     */
    private static class ExperimentationTableStructure {
        private final Instance instance;
        private final Pos basePos;
        private final List<LivingEntity> visualEntities;
        private final List<Pos> blockPositions;
        
        public ExperimentationTableStructure(Instance instance, Pos basePos) {
            this.instance = instance;
            this.basePos = basePos;
            this.visualEntities = new ArrayList<>();
            this.blockPositions = new ArrayList<>();
        }
        
        public void spawn() {
            // 1. Base skull support (underneath the table)
            Pos skullPos = basePos.sub(0, 1, 0);
            instance.setBlock(skullPos, Block.SKELETON_SKULL);
            blockPositions.add(skullPos);
            
            // 2. Floating book above the table
            Pos bookPos = basePos.add(0, 1.5, 0);
            LivingEntity bookStand = createArmorStand(bookPos, Material.BOOK);
            bookStand.setInstance(instance, bookPos);
            visualEntities.add(bookStand);
            
            // 3. Left pylon with purple orb
            Pos leftPylonPos = basePos.add(-1.5, 0, 0);
            createPylon(leftPylonPos, -1);
            
            // 4. Right pylon with purple orb
            Pos rightPylonPos = basePos.add(1.5, 0, 0);
            createPylon(rightPylonPos, 1);
            
            // 5. Left book
            Pos leftBookPos = basePos.add(-1, 0.5, 0);
            LivingEntity leftBookStand = createArmorStand(leftBookPos, Material.BOOK);
            leftBookStand.setInstance(instance, leftBookPos);
            visualEntities.add(leftBookStand);
            
            // 6. Right book
            Pos rightBookPos = basePos.add(1, 0.5, 0);
            LivingEntity rightBookStand = createArmorStand(rightBookPos, Material.BOOK);
            rightBookStand.setInstance(instance, rightBookPos);
            visualEntities.add(rightBookStand);
            
            // 7. Left chest (standard brown)
            Pos leftChestPos = basePos.add(-1, 0, -1);
            instance.setBlock(leftChestPos, Block.CHEST);
            blockPositions.add(leftChestPos);
            
            // 8. Right chest (ornate - using trapped chest for different appearance)
            Pos rightChestPos = basePos.add(1, 0, -1);
            instance.setBlock(rightChestPos, Block.TRAPPED_CHEST);
            blockPositions.add(rightChestPos);
            
            // 9. Foundation platform (light colored blocks)
            createFoundation();
        }
        
        private void createPylon(Pos pylonPos, int direction) {
            // Pylon base (stone)
            instance.setBlock(pylonPos, Block.STONE);
            blockPositions.add(pylonPos);
            
            // Pylon middle
            Pos middlePos = pylonPos.add(0, 1, 0);
            instance.setBlock(middlePos, Block.STONE);
            blockPositions.add(middlePos);
            
            // Pylon top with purple orb
            Pos topPos = pylonPos.add(0, 2, 0);
            instance.setBlock(topPos, Block.AMETHYST_CLUSTER);
            blockPositions.add(topPos);
        }
        
        private void createFoundation() {
            // Create a 5x5 light colored foundation around the table
            for (int x = -2; x <= 2; x++) {
                for (int z = -2; z <= 2; z++) {
                    Pos foundationPos = basePos.add(x, -1, z);
                    if (!blockPositions.contains(foundationPos)) {
                        instance.setBlock(foundationPos, Block.SMOOTH_STONE);
                        blockPositions.add(foundationPos);
                    }
                }
            }
        }
        
        private LivingEntity createArmorStand(Pos pos, Material itemMaterial) {
            LivingEntity armorStand = new LivingEntity(EntityType.ARMOR_STAND);
            
            ArmorStandMeta meta = (ArmorStandMeta) armorStand.getEntityMeta();
            meta.setInvisible(true);
            meta.setHasNoGravity(true);
            meta.setSmall(true);
            armorStand.setEquipment(EquipmentSlot.MAIN_HAND, ItemStack.of(itemMaterial));
            
            return armorStand;
        }
        
        public void remove() {
            // Remove all visual entities
            for (LivingEntity entity : visualEntities) {
                entity.remove();
            }
            
            // Remove all placed blocks
            for (Pos pos : blockPositions) {
                instance.setBlock(pos, Block.AIR);
            }
        }
    }
}


