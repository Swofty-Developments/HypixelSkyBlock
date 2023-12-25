package net.swofty.item.updater;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.item.ItemStack;
import net.swofty.utility.StringUtility;
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

        return updateItemLore(builder);
    }

    private static ItemStack.Builder updateItemLore(ItemStack.Builder stack) {
        ItemLore lore = new ItemLore(stack.build());
        lore.updateLore(null);

        return stack.lore(lore.getStack().getLore()).displayName(lore.getStack().getDisplayName());
    }
}
