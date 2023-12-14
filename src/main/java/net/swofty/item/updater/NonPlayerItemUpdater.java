package net.swofty.item.updater;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.item.ItemHideFlag;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.Utility;
import net.swofty.item.ItemLore;
import net.swofty.item.SkyBlockItem;
import net.swofty.item.attribute.AttributeHandler;

@Getter
public class NonPlayerItemUpdater {
    private SkyBlockItem item;

    public NonPlayerItemUpdater(SkyBlockItem item) {
        this.item = item;
    }

    public NonPlayerItemUpdater(ItemStack item) {
        this.item = new SkyBlockItem(item);
    }

    public NonPlayerItemUpdater(ItemStack.Builder item) {
        this(item.build());
    }

    public ItemStack.Builder getUpdatedItem() {
        ItemStack.Builder builder = item.getItemStackBuilder();
        AttributeHandler handler = item.getAttributeHandler();

        return updateItemLore(builder)
                .displayName(Component.text(
                        handler.getRarity().getColor() + Utility.toNormalCase(handler.getItemType()))
                        .decoration(TextDecoration.ITALIC, false));
    }

    private static ItemStack.Builder updateItemLore(ItemStack.Builder stack) {
        ItemLore lore = new ItemLore(stack.build());
        lore.updateLore();

        return stack.lore(lore.getStack().getLore());
    }
}
