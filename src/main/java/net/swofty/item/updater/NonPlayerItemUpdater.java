package net.swofty.item.updater;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.color.Color;
import net.minestom.server.item.Enchantment;
import net.minestom.server.item.ItemHideFlag;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.metadata.LeatherArmorMeta;
import net.swofty.item.impl.Enchanted;
import net.swofty.utility.StringUtility;
import net.swofty.item.ItemLore;
import net.swofty.item.SkyBlockItem;
import net.swofty.item.attribute.AttributeHandler;
import org.tinylog.Logger;
import net.minestom.server.color.Color;

@Getter
public class NonPlayerItemUpdater {
    private final SkyBlockItem item;

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
        ItemStack.Builder stack = updateItemLore(builder);

        if (item.getGenericInstance() != null
                && item.getGenericInstance() instanceof Enchanted) {
            stack.meta(meta -> {
                meta.enchantment(Enchantment.EFFICIENCY, (short) 1);
                meta.hideFlag(ItemHideFlag.HIDE_ENCHANTS);
            });
        }

        Color leatherColour = item.getAttributeHandler().getLeatherColour();
        if (leatherColour != null) {
            stack.meta(meta -> {
                LeatherArmorMeta.Builder leatherMeta = new LeatherArmorMeta.Builder(meta.tagHandler());
                leatherMeta.color(leatherColour);
                meta.hideFlag(ItemHideFlag.HIDE_DYE,
                        ItemHideFlag.HIDE_ATTRIBUTES,
                        ItemHideFlag.HIDE_ENCHANTS);
            });
        }

        return stack;
    }

    private static ItemStack.Builder updateItemLore(ItemStack.Builder stack) {
        ItemLore lore = new ItemLore(stack.build());
        lore.updateLore(null);

        return stack.lore(lore.getStack().getLore())
                .displayName(lore.getStack().getDisplayName());
    }
}
