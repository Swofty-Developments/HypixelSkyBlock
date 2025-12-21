package net.swofty.type.skyblockgeneric.item.updater;

import lombok.Getter;
import net.minestom.server.color.Color;
import net.minestom.server.component.DataComponents;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.item.ItemStack;
import net.minestom.server.network.player.ResolvableProfile;
import net.minestom.server.tag.Tag;
import net.swofty.commons.item.UnderstandableSkyBlockItem;
import net.swofty.commons.item.attribute.ItemAttribute;
import net.swofty.commons.item.attribute.attributes.ItemAttributeGemData;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.skyblockgeneric.item.ItemLore;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.components.EnchantedComponent;
import net.swofty.type.skyblockgeneric.item.components.GemstoneComponent;
import net.swofty.type.skyblockgeneric.item.components.SkullHeadComponent;
import net.swofty.type.skyblockgeneric.item.components.TrackedUniqueComponent;
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

        if (item.hasComponent(EnchantedComponent.class))
            stack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);

        if (item.hasComponent(TrackedUniqueComponent.class))
            stack.setTag(Tag.UUID("unique-tracked-id"), UUID.randomUUID());

        if (item.hasComponent(SkullHeadComponent.class)) {
            SkullHeadComponent component = item.getComponent(SkullHeadComponent.class);
            JSONObject json = new JSONObject();
            json.put("isPublic", true);
            json.put("signatureRequired", false);
            json.put("textures", new JSONObject().put("SKIN", new JSONObject()
                    .put("url", "http://textures.minecraft.net/texture/" + component.getSkullTexture(item))
                    .put("metadata", new JSONObject().put("model", "slim"))));

            String texturesEncoded = Base64.getEncoder().encodeToString(json.toString().getBytes());

            stack.set(DataComponents.PROFILE, new ResolvableProfile(new PlayerSkin(texturesEncoded, null)));
        }

        if (item.hasComponent(GemstoneComponent.class)) {
            GemstoneComponent gemstoneComponent = item.getComponent(GemstoneComponent.class);

            int index = 0;
            ItemAttributeGemData.GemData gemData = item.getAttributeHandler().getGemData();
            for (GemstoneComponent.GemstoneSlot slot : gemstoneComponent.getSlots()) {
                if (slot.unlockPrice() == 0) {
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

        Color leatherColour = item.getAttributeHandler().getLeatherColour();
        if (leatherColour != null) {
            stack.set(DataComponents.DYED_COLOR, leatherColour);
            stack = ItemStackCreator.clearAttributes(stack);
        }

        for (ItemAttribute attribute : ItemAttribute.getPossibleAttributes()) {
            stack = stack.set(Tag.String(attribute.getKey()),
                    item.getAttribute(attribute.getKey()).saveIntoString());
        }

        ItemStackCreator.clearAttributes(stack);
        return stack;
    }

    private static ItemStack.Builder updateItemLore(ItemStack.Builder stack) {
        ItemLore lore = new ItemLore(stack.build());
        lore.updateLore(null);

        return stack.set(DataComponents.LORE, lore.getStack().get(DataComponents.LORE))
                .set(DataComponents.CUSTOM_NAME, lore.getStack().get(DataComponents.CUSTOM_NAME));
    }
}
