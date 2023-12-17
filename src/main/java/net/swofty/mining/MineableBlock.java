package net.swofty.mining;

import lombok.Getter;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.item.SkyBlockItem;
import net.swofty.region.RegionType;
import net.swofty.utility.MaterialQuantifiableRandom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public enum MineableBlock
{
      // Default minable blocks
      STONE(Material.STONE, 15, 1, MiningLoot.Default(), RegionType.COAL_MINE, RegionType.COAL_MINE_CAVES, RegionType.GOLD_MINE, RegionType.DEEP_CAVERN, RegionType.DWARVEN_MINES),
      SAND(Material.SAND, 5, 1, MiningLoot.Default(), RegionType.MUSHROOM_DESERT),
      NETHERRACK(Material.NETHERRACK, 4, 1, MiningLoot.Default(), RegionType.BLAZING_FORTRESS),
      COBBLESTONE(Material.COBBLESTONE, 20, 1, MiningLoot.Default(), RegionType.COAL_MINE, RegionType.COAL_MINE_CAVES, RegionType.GOLD_MINE, RegionType.DEEP_CAVERN, RegionType.DWARVEN_MINES),
      ANDESITE(Material.ANDESITE, 15, 1, MiningLoot.Default(), RegionType.COAL_MINE, RegionType.COAL_MINE_CAVES, RegionType.GOLD_MINE, RegionType.DEEP_CAVERN, RegionType.DWARVEN_MINES),
      GRANITE(Material.GRANITE, 15, 1, MiningLoot.Default(), RegionType.COAL_MINE, RegionType.COAL_MINE_CAVES, RegionType.GOLD_MINE, RegionType.DEEP_CAVERN, RegionType.DWARVEN_MINES),
      DIORITE(Material.DIORITE, 15, 1, MiningLoot.Default(), RegionType.COAL_MINE, RegionType.COAL_MINE_CAVES, RegionType.GOLD_MINE, RegionType.DEEP_CAVERN, RegionType.DWARVEN_MINES),
      END_STONE(Material.END_STONE, 30, 1, MiningLoot.Default(), RegionType.THE_END, RegionType.THE_END_NEST),
      OBSIDIAN(Material.END_STONE, 500, 4, MiningLoot.Default(), RegionType.COAL_MINE_CAVES, RegionType.COAL_MINE, RegionType.GOLD_MINE, RegionType.DEEP_CAVERN, RegionType.THE_END, RegionType.THE_END_NEST),
      ORE_IRON(Material.IRON_ORE, 30, 2, MiningLoot.Default(), RegionType.COAL_MINE_CAVES, RegionType.COAL_MINE, RegionType.GOLD_MINE, RegionType.DEEP_CAVERN),
      ORE_COAL(Material.COAL_ORE, 30, 1, MiningLoot.Default(), RegionType.COAL_MINE_CAVES, RegionType.COAL_MINE, RegionType.GOLD_MINE, RegionType.DEEP_CAVERN),
      ORE_GOLD(Material.GOLD_ORE, 30, 3, MiningLoot.Default(), RegionType.COAL_MINE_CAVES, RegionType.COAL_MINE, RegionType.GOLD_MINE, RegionType.DEEP_CAVERN),
      ORE_DIAMOND(Material.DIAMOND_ORE, 30, 3, MiningLoot.Default(), RegionType.COAL_MINE_CAVES, RegionType.COAL_MINE, RegionType.GOLD_MINE, RegionType.DEEP_CAVERN),
      ORE_REDSTONE(Material.REDSTONE_ORE, 30, 3, MiningLoot.Default(), RegionType.COAL_MINE_CAVES, RegionType.COAL_MINE, RegionType.GOLD_MINE, RegionType.DEEP_CAVERN),

      // Forest minable blocks
      ACACIA_LOG(Material.ACACIA_LOG, 1, 0, MiningLoot.Default(), RegionType.FOREST),
      BIRCH_LOG(Material.BIRCH_LOG, 1, 0, MiningLoot.Default(), RegionType.FOREST),
      JUNGLE_LOG(Material.JUNGLE_LOG, 1, 0, MiningLoot.Default(), RegionType.FOREST),
      SPRUCE_LOG(Material.SPRUCE_LOG, 1, 0, MiningLoot.Default(), RegionType.FOREST),
      DARK_OAK_LOG(Material.DARK_OAK_LOG, 1, 0, MiningLoot.Default(), RegionType.FOREST),
      MANGROVE_LOG(Material.MANGROVE_LOG, 1, 0, MiningLoot.Default(), RegionType.FOREST),
      OAK_LOG(Material.OAK_LOG, 1, 0, MiningLoot.Default(), RegionType.FOREST),

      // Farming minable blocks
      PUMPKIN(Material.PUMPKIN, 1, 0, MiningLoot.Default(), RegionType.THE_BARN),
      CARVED_PUMPKIN(Material.CARVED_PUMPKIN, 1, 0, MiningLoot.Default(), RegionType.THE_BARN),
      MELON(Material.MELON, 1, 0, MiningLoot.Default(), RegionType.THE_BARN),

      // Dwarven mines minable blocks
      MITHRIL_WEAK(Material.GRAY_WOOL, 500, 4, MiningLoot.None(), RegionType.DWARVEN_MINES, RegionType.DWARVEN_VILLAGE),
      MITHRIL_WEAK2(Material.CYAN_TERRACOTTA, 500, 4, MiningLoot.None(), RegionType.DWARVEN_MINES, RegionType.DWARVEN_VILLAGE),
      MITHRIL_MEDIUM(Material.PRISMARINE, 800, 4, MiningLoot.None(), RegionType.DWARVEN_MINES, RegionType.DWARVEN_VILLAGE),
      MITHRIL_STRONG(Material.LIGHT_BLUE_WOOL, 1500, 4, MiningLoot.None(), RegionType.DWARVEN_MINES, RegionType.DWARVEN_VILLAGE),
      TITANIUM(Material.DIORITE, 2500, 5, MiningLoot.None(), RegionType.DWARVEN_MINES, RegionType.DWARVEN_VILLAGE),
      DWARVEN_GOLD(Material.GOLD_BLOCK, 600, 3, MiningLoot.Custom(Material.GOLD_INGOT, 2, 4), RegionType.DWARVEN_MINES, RegionType.DWARVEN_VILLAGE),
      ;

      private final Material                    material;
      private final double                      strength;
      private final int                         miningPowerRequirement;
      private final MaterialQuantifiableRandom  drops;
      private final RegionType[]                acceptedRegionTypes;

      MineableBlock(Material material, double strength, int miningPowerRequirement, MiningLoot drops, RegionType... acceptableRegionTypes) {
            this.material = material;
            this.strength = strength;
            this.miningPowerRequirement = miningPowerRequirement;
            this.drops = drops.identifier().equals("default") ? utilDrops(material, 1, 1) : drops.mat();
            this.acceptedRegionTypes = acceptableRegionTypes;
      }

      public static MaterialQuantifiableRandom utilDrops(Material mat, int bounds1, int bounds2) {
            return new MaterialQuantifiableRandom(new SkyBlockItem(mat), bounds1, bounds2);
      }

      public List<RegionType> getSupportedRegions() {
            return new ArrayList<>(Arrays.asList(acceptedRegionTypes));
      }

      public static MineableBlock get(Block b) {
            for (MineableBlock m : values()) {
                  if (b.namespace().equals(m.getMaterial().namespace()))
                        return m;
            }

            return null;
      }
}
