package net.swofty.type.ravengardgeneric.entity;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.util.RGBLike;
import net.minestom.server.component.DataComponents;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.metadata.display.AbstractDisplayMeta;
import net.minestom.server.entity.metadata.display.ItemDisplayMeta;
import net.minestom.server.entity.metadata.display.TextDisplayMeta;
import net.minestom.server.entity.metadata.other.AreaEffectCloudMeta;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.CustomModelData;
import net.swofty.type.generic.entity.InteractionEntity;
import net.swofty.type.ravengardgeneric.user.RavengardPlayer;

import java.util.ArrayList;
import java.util.List;

public abstract class RavengardNPC {
    @Getter
    private final Configuration configuration;
    @Getter
    private final List<Entity> entities = new ArrayList<>();

    protected RavengardNPC(Configuration configuration) {
        this.configuration = configuration;
    }

    public abstract void onClick(RavengardPlayer player);

    public void spawn(Instance instance) {
        if (!entities.isEmpty()) {
            throw new IllegalStateException("NPC is already spawned");
        }

        Pos position = configuration.position;
        InteractionEntity interaction = new InteractionEntity(configuration.interactionWidth,
                configuration.interactionHeight, (player, _) -> onClick((RavengardPlayer) player));
        spawn(interaction, instance, position);

        spawnText(instance, position);

        for (AreaEffectCloudData data : configuration.areaEffectClouds) {
            LivingEntity cloud = new LivingEntity(EntityType.AREA_EFFECT_CLOUD);
            cloud.editEntityMeta(AreaEffectCloudMeta.class, meta -> meta.setRadius(data.radius));
            spawn(cloud, instance, position.add(data.offset));
        }

        for (ItemDisplayData data : configuration.itemDisplays) {
            LivingEntity item = new LivingEntity(EntityType.ITEM_DISPLAY);
            item.editEntityMeta(ItemDisplayMeta.class, meta -> {
                ItemStack.Builder stack = ItemStack.builder(data.material);
                if (!data.itemModel.isBlank()) stack.set(DataComponents.ITEM_MODEL, data.itemModel);
                if (!data.customModelFloats.isEmpty() || !data.customModelFlags.isEmpty()
                        || !data.customModelStrings.isEmpty() || !data.customModelColors.isEmpty()) {
                    stack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(
                            data.customModelFloats,
                            data.customModelFlags,
                            data.customModelStrings,
                            data.customModelColors.stream().map(color -> (RGBLike) TextColor.color(color)).toList()
                    ));
                }
                if (data.dyedColor != null) stack.set(DataComponents.DYED_COLOR, TextColor.color(data.dyedColor));
                if (data.enchantmentGlint != null) {
                    stack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, data.enchantmentGlint);
                }
                meta.setItemStack(stack.build());
                meta.setDisplayContext(displayContext(data.displayContext));
                applyDisplayData(meta, data.display);
            });
            spawn(item, instance, position.add(data.offset));
        }
    }

    public void remove() {
        entities.forEach(Entity::remove);
        entities.clear();
    }

    private void spawnText(Instance instance, Pos origin) {
        LivingEntity display = new LivingEntity(EntityType.TEXT_DISPLAY);
        Component bottom = Component.text("< ", NamedTextColor.DARK_GRAY)
                .append(Component.text(configuration.bottom, NamedTextColor.GRAY))
                .append(Component.text(" >", NamedTextColor.DARK_GRAY));
        Component text = configuration.name.isBlank()
                ? bottom
                : Component.text(configuration.name, NamedTextColor.LIGHT_PURPLE)
                .append(Component.newline())
                .append(bottom);
        display.editEntityMeta(TextDisplayMeta.class, meta -> {
            meta.setText(text);
            meta.setBillboardRenderConstraints(AbstractDisplayMeta.BillboardConstraints.CENTER);
            meta.setBackgroundColor(0);
            meta.setSeeThrough(false);
            meta.setHasNoGravity(true);
        });
        spawn(display, instance, origin.add(configuration.textOffset));
    }

    private static void applyDisplayData(AbstractDisplayMeta meta, DisplayData data) {
        meta.setTranslation(data.translation);
        meta.setScale(data.scale);
        meta.setLeftRotation(data.leftRotation);
        meta.setRightRotation(data.rightRotation);
        meta.setTransformationInterpolationStartDelta(data.interpolationStartDelta);
        meta.setTransformationInterpolationDuration(data.interpolationDuration);
        meta.setPosRotInterpolationDuration(data.positionRotationInterpolationDuration);
        meta.setBillboardRenderConstraints(billboard(data.billboard));
        meta.setBrightnessOverride(data.brightnessOverride);
        meta.setShadowRadius(data.shadowRadius);
        meta.setShadowStrength(data.shadowStrength);
        meta.setGlowColorOverride(data.glowColorOverride);
    }

    private static AbstractDisplayMeta.BillboardConstraints billboard(String value) {
        try {
            return AbstractDisplayMeta.BillboardConstraints.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException ignored) {
            return AbstractDisplayMeta.BillboardConstraints.FIXED;
        }
    }

    private void spawn(Entity entity, Instance instance, Pos position) {
        entity.setNoGravity(true);
        entity.setHasPhysics(false);
        entity.updateViewableRule(net.swofty.type.generic.world.HypixelWorldLoader.LOADED_ONLY);
        entity.setInstance(instance, position);
        entities.add(entity);
    }

    private static ItemDisplayMeta.DisplayContext displayContext(String value) {
        try {
            return ItemDisplayMeta.DisplayContext.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException ignored) {
            return ItemDisplayMeta.DisplayContext.NONE;
        }
    }

    public record AreaEffectCloudData(Vec offset, float radius, int duration, int waitTime, float radiusOnUse,
                                      float radiusPerTick, int durationOnUse, String particle) {
    }

    public record DisplayData(Vec translation, Vec scale, float[] leftRotation, float[] rightRotation,
                              int interpolationStartDelta, int interpolationDuration,
                              int positionRotationInterpolationDuration, String billboard, int brightnessOverride,
                              float shadowRadius, float shadowStrength, int glowColorOverride) {
    }

    public record ItemDisplayData(Vec offset, Material material, String itemModel, List<Float> customModelFloats,
                                  List<Boolean> customModelFlags, List<String> customModelStrings,
                                  List<Integer> customModelColors, Integer dyedColor, Boolean enchantmentGlint,
                                  String displayContext, DisplayData display) {
    }

    @Getter
    public static class Configuration {
        private final Pos position;
        private final String name;
        private final String bottom;
        private final float interactionWidth;
        private final float interactionHeight;
        private final Vec textOffset;
        private final List<AreaEffectCloudData> areaEffectClouds;
        private final List<ItemDisplayData> itemDisplays;

        private Configuration(Builder builder) {
            position = builder.position;
            name = builder.name;
            bottom = builder.bottom;
            interactionWidth = builder.interactionWidth;
            interactionHeight = builder.interactionHeight;
            textOffset = builder.textOffset;
            areaEffectClouds = List.copyOf(builder.areaEffectClouds);
            itemDisplays = List.copyOf(builder.itemDisplays);
        }

        public static Builder builder() {
            return new Builder();
        }

        public static final class Builder {
            private Pos position;
            private String name = "";
            private String bottom = "";
            private float interactionWidth = 0.6f;
            private float interactionHeight = 1.8f;
            private Vec textOffset = new Vec(0, 2.2, 0);
            private final List<AreaEffectCloudData> areaEffectClouds = new ArrayList<>();
            private final List<ItemDisplayData> itemDisplays = new ArrayList<>();

            public Builder position(Pos value) {
                position = value;
                return this;
            }

            public Builder name(String value) {
                name = value;
                return this;
            }

            public Builder bottom(String value) {
                bottom = value;
                return this;
            }

            public Builder interactionSize(float width, float height) {
                interactionWidth = width;
                interactionHeight = height;
                return this;
            }

            public Builder textOffset(Vec value) {
                textOffset = value;
                return this;
            }

            public Builder areaEffectCloud(AreaEffectCloudData value) {
                areaEffectClouds.add(value);
                return this;
            }

            public Builder itemDisplay(ItemDisplayData value) {
                itemDisplays.add(value);
                return this;
            }

            public Configuration build() {
                if (position == null) throw new IllegalStateException("position is required");
                if (bottom == null || bottom.isBlank()) throw new IllegalStateException("bottom is required");
                if (name == null) name = "";
                return new Configuration(this);
            }
        }
    }
}
