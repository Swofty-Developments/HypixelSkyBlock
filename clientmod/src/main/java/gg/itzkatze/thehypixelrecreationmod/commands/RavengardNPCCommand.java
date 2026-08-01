package gg.itzkatze.thehypixelrecreationmod.commands;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.math.Transformation;
import gg.itzkatze.thehypixelrecreationmod.utils.ChatUtils;
import gg.itzkatze.thehypixelrecreationmod.utils.StringUtility;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Interaction;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.storage.TagValueOutput;
import org.joml.Quaternionfc;
import org.joml.Vector3fc;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public final class RavengardNPCCommand {
    private static final double DEFAULT_RADIUS = 16;
    private static final double PART_HORIZONTAL_DISTANCE = 0.3;
    private static final float DEFAULT_INTERACTION_WIDTH = 0.6f;
    private static final float DEFAULT_INTERACTION_HEIGHT = 1.8f;

    private RavengardNPCCommand() {
    }

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, _) -> RecreationCommand.register(dispatcher,
                ClientCommands.literal("rnpc")
                        .executes(context -> execute(DEFAULT_RADIUS))
                        .then(ClientCommands.argument("radius", DoubleArgumentType.doubleArg(0))
                                .executes(context -> execute(DoubleArgumentType.getDouble(context, "radius"))))));
    }

    private static int execute(double radius) {
        Minecraft client = Minecraft.getInstance();
        if (client.player == null || client.level == null) {
            ChatUtils.warn("No world is currently loaded.");
            return 0;
        }

        List<Interaction> interactions = client.level.getEntitiesOfClass(
                Interaction.class, client.player.getBoundingBox().inflate(radius));
        interactions.sort(Comparator.comparingDouble(entity -> entity.distanceToSqr(client.player)));
        if (interactions.isEmpty()) {
            ChatUtils.warn("No minecraft:interaction entities found nearby.");
            return 0;
        }

        ChatUtils.send(Component.literal("Found " + interactions.size() + " Ravengard NPC candidate(s)."));
        interactions.forEach(interaction -> capture(client, interaction));
        return 1;
    }

    private static void capture(Minecraft client, Interaction interaction) {
        double x = interaction.getX();
        double y = interaction.getY();
        double z = interaction.getZ();
        List<Display.TextDisplay> texts = new ArrayList<>();
        List<AreaEffectCloud> clouds = new ArrayList<>();
        List<Display.ItemDisplay> items = new ArrayList<>();

        for (Entity entity : client.level.entitiesForRendering()) {
            if (entity == interaction || !belongsToNPC(entity, x, y, z)) continue;
            switch (entity) {
                case Display.TextDisplay text -> texts.add(text);
                case AreaEffectCloud cloud -> clouds.add(cloud);
                case Display.ItemDisplay item when !item.getItemStack().is(Items.STICK)
                        && !item.getItemStack().isEmpty() -> items.add(item);
                default -> {
                }
            }
        }

        Display.TextDisplay text = texts.stream()
                .min(Comparator.comparingDouble(display -> Math.abs(display.getY() - (y + 2.2))))
                .orElse(null);
        NameLines lines = parseNameLines(text == null ? "" : StringUtility.toLegacyString(text.getText()));
        String generated = generate(interaction, x, y, z, lines, text, clouds, items);
        String label = lines.name.isBlank() ? lines.bottom : lines.name;

        ChatUtils.send(Component.literal("Ravengard NPC: " + (label.isBlank() ? interaction.getUUID() : label)));
        ChatUtils.send(Component.literal("Copy RavengardNPC configuration (click)").setStyle(Style.EMPTY
                .withColor(TextColor.fromLegacyFormat(ChatFormatting.LIGHT_PURPLE))
                .withClickEvent(new ClickEvent.CopyToClipboard(generated))));
        ChatUtils.sendLine();
    }

    private static boolean belongsToNPC(Entity entity, double x, double y, double z) {
        double dx = entity.getX() - x;
        double dz = entity.getZ() - z;
        double relativeY = entity.getY() - y;
        return dx * dx + dz * dz <= PART_HORIZONTAL_DISTANCE * PART_HORIZONTAL_DISTANCE
                && relativeY >= -0.75 && relativeY <= 3;
    }

    private static NameLines parseNameLines(String legacyText) {
        String plain = ChatFormatting.stripFormatting(legacyText);
        if (plain == null || plain.isBlank()) return new NameLines("", "");
        String[] lines = plain.split("\\R", 2);
        if (lines.length == 1) return new NameLines("", stripBrackets(lines[0]));
        return new NameLines(lines[0].strip(), stripBrackets(lines[1]));
    }

    private static String stripBrackets(String value) {
        String stripped = value.strip();
        if (stripped.startsWith("<")) stripped = stripped.substring(1);
        if (stripped.endsWith(">")) stripped = stripped.substring(0, stripped.length() - 1);
        return stripped.strip();
    }

    private static String generate(Interaction interaction, double x, double y, double z, NameLines lines,
                                   Display.TextDisplay text, List<AreaEffectCloud> clouds,
                                   List<Display.ItemDisplay> items) {
        StringBuilder result = new StringBuilder("new RavengardNPC(RavengardNPC.Configuration.builder()\n")
                .append("    .position(new Pos(").append(number(x)).append(", ").append(number(y)).append(", ")
                .append(number(z)).append("))\n")
                .append("    .name(\"").append(StringUtility.escapeJavaString(lines.name)).append("\")\n")
                .append("    .bottom(\"").append(StringUtility.escapeJavaString(lines.bottom)).append("\")\n");
        if (different(interaction.getWidth(), DEFAULT_INTERACTION_WIDTH)
                || different(interaction.getHeight(), DEFAULT_INTERACTION_HEIGHT)) {
            result.append("    .interactionSize(").append(number(interaction.getWidth())).append("f, ")
                    .append(number(interaction.getHeight())).append("f)\n");
        }
        if (text != null && (different(text.getX() - x, 0) || different(text.getY() - y, 2.2)
                || different(text.getZ() - z, 0))) {
            result.append("    .textOffset(new Vec(").append(relative(text.getX(), x)).append(", ")
                    .append(relative(text.getY(), y)).append(", ").append(relative(text.getZ(), z)).append("))\n");
        }
        for (AreaEffectCloud cloud : clouds) appendCloud(result, cloud, x, y, z);
        for (Display.ItemDisplay item : items) appendItem(result, item, x, y, z);
        return result.append("    .build()) {\n    @Override\n    public void onClick(RavengardPlayer player) {\n    }\n};")
                .toString();
    }

    private static void appendCloud(StringBuilder result, AreaEffectCloud cloud, double x, double y, double z) {
        CompoundTag tag = save(cloud);
        result.append("    .areaEffectCloud(new RavengardNPC.AreaEffectCloudData(new Vec(")
                .append(relative(cloud.getX(), x)).append(", ").append(relative(cloud.getY(), y)).append(", ")
                .append(relative(cloud.getZ(), z)).append("), ").append(number(cloud.getRadius())).append("f, ")
                .append(cloud.getDuration()).append(", ").append(cloud.getWaitTime()).append(", ")
                .append(number(cloud.getRadiusOnUse())).append("f, ").append(number(cloud.getRadiusPerTick()))
                .append("f, ").append(cloud.getDurationOnUse()).append(", \"")
                .append(StringUtility.escapeJavaString(tag.get("particle") == null ? "" : tag.get("particle").toString()))
                .append("\"))\n");
    }

    private static void appendItem(StringBuilder result, Display.ItemDisplay item, double x, double y, double z) {
        var stack = item.getItemStack();
        String material = stack.getItem().builtInRegistryHolder().unwrapKey()
                .map(key -> key.identifier().getPath().toUpperCase(Locale.ROOT)).orElse("AIR");
        String itemModel = stack.getOrDefault(DataComponents.ITEM_MODEL, null) == null
                ? "" : stack.get(DataComponents.ITEM_MODEL).toString();
        CustomModelData customModelData = stack.getOrDefault(DataComponents.CUSTOM_MODEL_DATA, CustomModelData.EMPTY);
        DyedItemColor dyedColor = stack.get(DataComponents.DYED_COLOR);
        Boolean glint = stack.get(DataComponents.ENCHANTMENT_GLINT_OVERRIDE);

        result.append("    .itemDisplay(new RavengardNPC.ItemDisplayData(new Vec(")
                .append(relative(item.getX(), x)).append(", ").append(relative(item.getY(), y)).append(", ")
                .append(relative(item.getZ(), z)).append("), Material.").append(material).append(", \"")
                .append(StringUtility.escapeJavaString(itemModel)).append("\", ")
                .append(floatList(customModelData.floats())).append(", ").append(booleanList(customModelData.flags())).append(", ")
                .append(stringList(customModelData.strings())).append(", ").append(integerList(customModelData.colors())).append(", ")
                .append(dyedColor == null ? "null" : dyedColor.rgb()).append(", ")
                .append(glint == null ? "null" : glint).append(", \"")
                .append(item.getItemTransform()).append("\", ");
        appendDisplayData(result, item);
        result.append("))\n");
    }

    private static void appendDisplayData(StringBuilder result, Display display) {
        CompoundTag tag = save(display);
        Transformation transformation = display.renderState().transformation().get(1);
        result.append("new RavengardNPC.DisplayData(");
        appendVector(result, transformation.translation());
        result.append(", ");
        appendVector(result, transformation.scale());
        result.append(", ");
        appendQuaternion(result, transformation.leftRotation());
        result.append(", ");
        appendQuaternion(result, transformation.rightRotation());
        result.append(", ").append(tag.getIntOr("start_interpolation", 0)).append(", ")
                .append(tag.getIntOr("interpolation_duration", 0)).append(", ")
                .append(tag.getIntOr("teleport_duration", 0)).append(", \"")
                .append(display.renderState().billboardConstraints()).append("\", ")
                .append(display.renderState().brightnessOverride()).append(", ")
                .append(number(display.renderState().shadowRadius().get(1))).append("f, ")
                .append(number(display.renderState().shadowStrength().get(1))).append("f, ")
                .append(display.renderState().glowColorOverride()).append(")");
    }

    private static CompoundTag save(Entity entity) {
        TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING,
                entity.level().registryAccess());
        entity.save(output);
        return output.buildResult();
    }

    private static void appendVector(StringBuilder result, Vector3fc vector) {
        result.append("new Vec(").append(number(vector.x())).append(", ").append(number(vector.y())).append(", ")
                .append(number(vector.z())).append(")");
    }

    private static void appendQuaternion(StringBuilder result, Quaternionfc quaternion) {
        result.append("new float[]{").append(number(quaternion.x())).append("f, ").append(number(quaternion.y()))
                .append("f, ").append(number(quaternion.z())).append("f, ").append(number(quaternion.w())).append("f}");
    }

    private static String floatList(List<Float> values) {
        return values.isEmpty() ? "List.of()" : "List.of(" + values.stream()
                .map(value -> number(value) + "f").reduce((a, b) -> a + ", " + b).orElse("") + ")";
    }

    private static String stringList(List<String> values) {
        return values.isEmpty() ? "List.of()" : "List.of(" + values.stream()
                .map(value -> "\"" + StringUtility.escapeJavaString(value) + "\"")
                .reduce((a, b) -> a + ", " + b).orElse("") + ")";
    }

    private static String booleanList(List<Boolean> values) {
        return values.isEmpty() ? "List.of()" : "List.of(" + values.stream()
                .map(String::valueOf).reduce((a, b) -> a + ", " + b).orElse("") + ")";
    }

    private static String integerList(List<Integer> values) {
        return values.isEmpty() ? "List.of()" : "List.of(" + values.stream()
                .map(String::valueOf).reduce((a, b) -> a + ", " + b).orElse("") + ")";
    }

    private static boolean different(double first, double second) {
        return Math.abs(first - second) > 0.001;
    }

    private static String relative(double value, double origin) {
        return number(value - origin);
    }

    private static String number(double value) {
        return String.format(Locale.ROOT, "%.4f", value).replaceAll("\\.?0+$", "");
    }

    private record NameLines(String name, String bottom) {
    }
}
