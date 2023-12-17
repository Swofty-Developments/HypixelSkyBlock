package net.swofty.utility;

import lombok.Getter;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;
import net.swofty.item.SkyBlockItem;

public class MaterialQuantifiable {
      @Getter
      private SkyBlockItem material;
      @Getter
      private int amount;

      public MaterialQuantifiable(SkyBlockItem material, int amount) {
            this.material = material;
            this.amount = amount;
      }

      public MaterialQuantifiable(SkyBlockItem material) {
            this(material, 1);
      }

      public MaterialQuantifiable setMaterial(SkyBlockItem material) {
            this.material = material;
            return this;
      }

      public MaterialQuantifiable setAmount(int amount) {
            this.amount = amount;
            return this;
      }

      @Override
      public boolean equals(Object o) {
            if (!(o instanceof MaterialQuantifiable)) return false;
            MaterialQuantifiable material = (MaterialQuantifiable) o;
            return material.material == this.material && material.amount == this.amount;
      }

      @Override
      public MaterialQuantifiable clone() {
            return new MaterialQuantifiable(material, amount);
      }

      public String toString() {
            return "MQ{material=" + (material != null ? material.getMaterial().name() : "?") + ", amount=" + amount + "}";
      }

      public static MaterialQuantifiable of(ItemStack stack) {
            if (stack == null || Material.fromNamespaceId(stack.getTag(Tag.String("id"))) == Material.AIR)
                  return new MaterialQuantifiable(new SkyBlockItem(Material.AIR), (stack != null ? stack.getAmount() : 1));
            SkyBlockItem item = new SkyBlockItem(stack);
            return new MaterialQuantifiable(item, stack.getAmount());
      }

      public static MaterialQuantifiable[] of(ItemStack[] stacks) {
            MaterialQuantifiable[] materials = new MaterialQuantifiable[stacks.length];
            for (int i = 0; i < stacks.length; i++)
                  materials[i] = of(stacks[i]);
            return materials;
      }
}
