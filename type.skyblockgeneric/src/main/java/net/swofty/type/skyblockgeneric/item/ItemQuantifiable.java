package net.swofty.type.skyblockgeneric.item;

import lombok.Getter;
import lombok.NonNull;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.skyblock.item.ItemType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ItemQuantifiable {
    private @NonNull SkyBlockItem item;
    @Getter
    private int amount;

    public ItemQuantifiable(@NotNull SkyBlockItem item, int amount) {
        this.item = item;
        this.amount = amount;
    }

    public ItemQuantifiable(@NotNull ItemType material, int amount) {
        this.item = new SkyBlockItem(material);
        this.amount = amount;
    }

    public ItemQuantifiable(@NonNull ItemQuantifiable itemQuantifiable) {
        this(itemQuantifiable.item, itemQuantifiable.amount);
    }

    public SkyBlockItem getItem() {
        if (item.isNA()) return new SkyBlockItem(Material.AIR);
        SkyBlockItem item = this.item.clone();
        item.setAmount(amount);
        return item;
    }

    public ItemQuantifiable setMaterial(@NonNull ItemType material) {
        this.item = new SkyBlockItem(material);
        return this;
    }

    public ItemQuantifiable setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ItemQuantifiable potentialMaterial)) return false;
        return potentialMaterial.item.isSimilar(this.item) && potentialMaterial.amount == this.amount;
    }

    public ItemQuantifiable clone() {
        return new ItemQuantifiable(item, amount);
    }

    public SkyBlockItem toSkyBlockItem() {
        item.setAmount(amount);
        return item;
    }

    public boolean matchesType(SkyBlockItem item) {
        return Objects.equals(this.item.getAttributeHandler().getTypeAsString(), item.getAttributeHandler().getTypeAsString());
    }

    public String toString() {
        return "MQ{material=" + (item.getMaterial() != null ? item.getDisplayName() : "?")
                + ", amount=" + amount + "}";
    }

    public static ItemQuantifiable of(ItemStack stack) {
        if (stack == null || stack.material() == Material.AIR)
            return new ItemQuantifiable(ItemType.AIR, (stack != null ? stack.amount() : 1));
        return new ItemQuantifiable(new SkyBlockItem(stack), stack.amount());
    }

    public static ItemQuantifiable[] of(ItemStack[] stacks) {
        ItemQuantifiable[] materials = new ItemQuantifiable[stacks.length];
        for (int i = 0; i < stacks.length; i++)
            materials[i] = of(stacks[i]);
        return materials;
    }

    public static ItemQuantifiable one(ItemType type) {
        return new ItemQuantifiable(type, 1);
    }
}