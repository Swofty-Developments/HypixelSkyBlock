package net.swofty.type.ravengarddungeon.interactables;

import net.minestom.server.component.DataComponents;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.metadata.display.ItemDisplayMeta;
import net.minestom.server.entity.metadata.other.InteractionMeta;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

public abstract class DungeonInteractable {
    protected final Instance instance;
    protected Entity interaction;
    protected boolean opened;

    protected DungeonInteractable(Instance instance) {
        this.instance = instance;
    }

    public Entity getInteraction() {
        return interaction;
    }

    public boolean isOpened() {
        return opened;
    }

    public Pos focusPoint() {
        return interaction.getPosition().add(0, heightOf(interaction) / 2.0, 0);
    }

    public abstract void open(Player player);

    public boolean openOnLook() {
        return true;
    }

    public String castLabel() {
        return "Opening";
    }

    public boolean allowReactivation() {
        return false;
    }

    public void remove() {
        InteractableRegistry.unregister(this);
        if (interaction != null) {
            interaction.remove();
        }
    }

    protected Entity spawnInteraction(Pos position, float width, float height) {
        Entity entity = new Entity(EntityType.INTERACTION);
        entity.setNoGravity(true);
        entity.editEntityMeta(InteractionMeta.class, meta -> {
            meta.setWidth(width);
            meta.setHeight(height);
            meta.setResponse(true);
        });
        entity.setInstance(instance, position);
        return entity;
    }

    protected Entity spawnDisplay(Pos position, String model) {
        Entity display = new Entity(EntityType.ITEM_DISPLAY);
        display.setNoGravity(true);
        display.editEntityMeta(ItemDisplayMeta.class, meta -> {
            meta.setItemStack(modelStack(model));
            meta.setPosRotInterpolationDuration(2);
            meta.setViewRange(4096f);
            meta.setBrightnessOverride(7340144);
        });
        display.setInstance(instance, position);
        return display;
    }

    protected static ItemStack modelStack(String model) {
        return ItemStack.builder(Material.STICK)
                .set(DataComponents.ITEM_MODEL, model)
                .build();
    }

    private static double heightOf(Entity entity) {
        InteractionMeta meta = (InteractionMeta) entity.getEntityMeta();
        return meta.getHeight();
    }
}
