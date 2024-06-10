package net.swofty.types.generic.item.updater;

import lombok.Getter;
import net.minestom.server.color.Color;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.component.DyedItemColor;
import net.minestom.server.item.component.HeadProfile;
import net.minestom.server.tag.Tag;
import net.minestom.server.utils.Unit;
import net.swofty.commons.item.UnderstandableSkyBlockItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.item.ItemLore;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.commons.item.attribute.attributes.ItemAttributeGemData;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.GemstoneItem;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.item.impl.TrackedUniqueItem;
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

    public NonPlayerItemUpdater(UnderstandableSkyBlockItem item) {
        this.item = new SkyBlockItem(item);
        this.stack = this.item.getItemStackBuilder();
    }

    public NonPlayerItemUpdater(ItemStack item) {
        this.item = new SkyBlockItem(item);
        this.stack = ItemStackCreator.getFromStack(item);
    }

    public NonPlayerItemUpdater(ItemStack.Builder item) {
        this(item.build());
    }

    public ItemStack.Builder getUpdatedItem() {
        if (stack.build().hasTag(Tag.Boolean("uneditable")) && stack.build().getTag(Tag.Boolean("uneditable"))) {
            return stack;
        }

        ItemStack.Builder builder = item.getItemStackBuilder();
        ItemStack.Builder stack = updateItemLore(builder);

        if (item.getGenericInstance() != null) {
            if (item.getGenericInstance() instanceof Enchanted)
                stack.set(ItemComponent.ENCHANTMENT_GLINT_OVERRIDE, true);

            if (item.getGenericInstance() instanceof TrackedUniqueItem)
                stack.setTag(Tag.UUID("unique-tracked-id"), UUID.randomUUID());

            if (item.getGenericInstance() instanceof SkullHead skullHead) {
                JSONObject json = new JSONObject();
                json.put("isPublic", true);
                json.put("signatureRequired", false);
                json.put("textures", new JSONObject().put("SKIN", new JSONObject()
                        .put("url", "http://textures.minecraft.net/texture/" + skullHead.getSkullTexture(null, item))
                        .put("metadata", new JSONObject().put("model", "slim"))));

                String texturesEncoded = Base64.getEncoder().encodeToString(json.toString().getBytes());

                stack.set(ItemComponent.PROFILE, new HeadProfile(new PlayerSkin(texturesEncoded, null)));
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
            stack.set(ItemComponent.DYED_COLOR, new DyedItemColor(leatherColour, false));
            stack.set(ItemComponent.HIDE_ADDITIONAL_TOOLTIP, Unit.INSTANCE);
        }

        ItemStackCreator.clearAttributes(stack);
        return stack;
    }

    private static ItemStack.Builder updateItemLore(ItemStack.Builder stack) {
        ItemLore lore = new ItemLore(stack.build());
        lore.updateLore(null);

        return stack.set(ItemComponent.LORE, lore.getStack().get(ItemComponent.LORE))
                .set(ItemComponent.CUSTOM_NAME, lore.getStack().get(ItemComponent.CUSTOM_NAME));
    }
}
