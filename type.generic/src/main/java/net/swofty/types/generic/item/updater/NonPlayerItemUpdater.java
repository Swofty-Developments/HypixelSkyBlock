package net.swofty.types.generic.item.updater;

import lombok.Getter;
import net.minestom.server.color.Color;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.item.Enchantment;
import net.minestom.server.item.ItemHideFlag;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.metadata.LeatherArmorMeta;
import net.minestom.server.tag.Tag;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.item.ItemLore;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.attribute.attributes.ItemAttributeGemData;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.GemstoneItem;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.item.impl.TrackedUniqueItem;
import net.swofty.types.generic.utility.ExtraItemTags;
import org.json.JSONObject;

import java.util.Base64;
import java.util.UUID;

@Getter
public class NonPlayerItemUpdater {
    private final SkyBlockItem item;
    private final ItemStack.Builder stack;

    public NonPlayerItemUpdater(SkyBlockItem item) {
        this.item = item;
        this.stack = item.getItemStackBuilder();
    }

    public NonPlayerItemUpdater(ItemStack item) {
        this.item = new SkyBlockItem(item);
        this.stack = ItemStackCreator.getFromStack(item);
    }

    public NonPlayerItemUpdater(ItemStack.Builder item) {
        this(item.build());
    }

    public ItemStack.Builder getUpdatedItem() {
        if (stack.build().hasTag(Tag.Boolean("Uneditable")) && stack.build().getTag(Tag.Boolean("Uneditable"))) {
            return stack;
        }

        ItemStack.Builder builder = item.getItemStackBuilder();
        ItemStack.Builder stack = updateItemLore(builder);

        if (item.getGenericInstance() != null) {
            if (item.getGenericInstance() instanceof Enchanted)
                stack.meta(meta -> {
                    meta.enchantment(Enchantment.EFFICIENCY, (short) 1);
                    meta.hideFlag(ItemHideFlag.HIDE_ENCHANTS);
                });

            if (item.getGenericInstance() instanceof TrackedUniqueItem)
                stack.meta(meta -> {
                    meta.set(Tag.UUID("unique-tracked-id"), UUID.randomUUID());
                });

            if (item.getGenericInstance() instanceof SkullHead skullHead) {
                JSONObject json = new JSONObject();
                json.put("isPublic", true);
                json.put("signatureRequired", false);
                json.put("textures", new JSONObject().put("SKIN", new JSONObject()
                        .put("url", "http://textures.minecraft.net/texture/" + skullHead.getSkullTexture(null, item))
                        .put("metadata", new JSONObject().put("model", "slim"))));

                String texturesEncoded = Base64.getEncoder().encodeToString(json.toString().getBytes());

                stack.meta(meta -> {
                    meta.set(ExtraItemTags.SKULL_OWNER, new ExtraItemTags.SkullOwner(null,
                            "25", new PlayerSkin(texturesEncoded, null)));
                });
            }

            if (item.getGenericInstance() instanceof GemstoneItem gemstoneItem) {
                int index = 0;
                ItemAttributeGemData.GemData gemData = item.getAttributeHandler().getGemData();
                for (GemstoneItem.GemstoneItemSlot slot : gemstoneItem.getGemstoneSlots()) {
                    if (slot.unlockPrice == 0) {
                        // Slot should be unlocked by default
                        if (gemData.hasGem(index)) continue;
                        item.getAttributeHandler().getGemData().putGem(
                                new ItemAttributeGemData.GemData.GemSlots(
                                        index,
                                        null
                                )
                        );
                    }
                    index++;
                }
                item.getAttributeHandler().setGemData(gemData);
            }
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
