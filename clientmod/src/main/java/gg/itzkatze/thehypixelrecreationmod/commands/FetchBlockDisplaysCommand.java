package gg.itzkatze.thehypixelrecreationmod.commands;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.math.Transformation;
import gg.itzkatze.thehypixelrecreationmod.utils.ChatUtils;
import gg.itzkatze.thehypixelrecreationmod.utils.ClipboardUtils;
import gg.itzkatze.thehypixelrecreationmod.utils.StringUtility;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.Rotations;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Display.BlockDisplay;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionfc;
import org.joml.Vector3fc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class FetchBlockDisplaysCommand {
    private static final List<EquipmentSlot> EQUIPMENT_SLOTS = List.of(
        EquipmentSlot.HEAD,
        EquipmentSlot.CHEST,
        EquipmentSlot.LEGS,
        EquipmentSlot.FEET,
        EquipmentSlot.MAINHAND,
        EquipmentSlot.OFFHAND
    );

    private FetchBlockDisplaysCommand() {
    }

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(
            ClientCommands.literal("fetchblockdisplays")
                .then(ClientCommands.argument("area", DoubleArgumentType.doubleArg(0))
                    .executes(context -> execute(
                        DoubleArgumentType.getDouble(context, "area"),
                        null
                    ))
                    .then(ClientCommands.argument("originX", DoubleArgumentType.doubleArg())
                        .then(ClientCommands.argument("originY", DoubleArgumentType.doubleArg())
                            .then(ClientCommands.argument("originZ", DoubleArgumentType.doubleArg())
                                .executes(context -> execute(
                                    DoubleArgumentType.getDouble(context, "area"),
                                    new ExportOrigin(
                                        DoubleArgumentType.getDouble(context, "originX"),
                                        DoubleArgumentType.getDouble(context, "originY"),
                                        DoubleArgumentType.getDouble(context, "originZ")
                                    )
                                ))
                            )
                        )
                    )
                )
        ));
    }

    private static int execute(double area, ExportOrigin requestedOrigin) {
        final var client = Minecraft.getInstance();
        final var player = client.player;

        if (player == null || client.level == null) {
            ChatUtils.error("Player or level is null.");
            return 1;
        }

        final var origin = requestedOrigin != null
            ? requestedOrigin
            : new ExportOrigin(player.getX(), player.getY(), player.getZ());

        final var searchBox = player.getBoundingBox().inflate(area);
        final var blockDisplays = client.level.getEntities(
            EntityTypes.BLOCK_DISPLAY,
            searchBox,
            _ -> true
        );
        final var itemDisplays = client.level.getEntities(
            EntityTypes.ITEM_DISPLAY,
            searchBox,
            _ -> true
        );
        final var modelArmorStands = client.level.getEntities(
            EntityTypes.ARMOR_STAND,
            searchBox,
            FetchBlockDisplaysCommand::isExportableModelArmorStand
        );

        final List<Entity> exportedEntities = new ArrayList<>(
            blockDisplays.size() + itemDisplays.size() + modelArmorStands.size()
        );
        exportedEntities.addAll(blockDisplays);
        exportedEntities.addAll(itemDisplays);
        exportedEntities.addAll(modelArmorStands);
        exportedEntities.sort(Comparator.comparingDouble(entity -> distanceToSqr(entity, origin)));

        if (exportedEntities.isEmpty()) {
            ChatUtils.warn("No display entities or invisible model armor stands found nearby.");
            return 1;
        }

        final var export = buildExport(exportedEntities, origin);
        ClipboardUtils.setClipboard(export);

        final Path outputPath = client.gameDirectory.toPath().resolve("block_displays_export.json");
        try {
            Files.writeString(outputPath, export);
            ChatUtils.message("Exported " + exportedEntities.size() + " entities to block_displays_export.json");
        } catch (IOException exception) {
            ChatUtils.warn("Failed to write export file: " + exception.getMessage());
        }

        ChatUtils.message("Copied " + exportedEntities.size() + " entity entries to clipboard.");
        return 1;
    }

    private static boolean isExportableModelArmorStand(ArmorStand armorStand) {
        return armorStand.isInvisible() && hasEquipment(armorStand);
    }

    private static boolean hasEquipment(ArmorStand armorStand) {
        for (var slot : EQUIPMENT_SLOTS) {
            if (!armorStand.getItemBySlot(slot).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private static String buildExport(List<? extends Entity> entities, ExportOrigin origin) {
        final var out = new StringBuilder();
        out.append("[\n");

        for (var index = 0; index < entities.size(); index++) {
            appendEntityJson(out, entities.get(index), origin, 1);
            if (index < entities.size() - 1) {
                out.append(",");
            }
            out.append("\n");
        }

        out.append("]");
        return out.toString();
    }

    private static void appendEntityJson(StringBuilder out, Entity entity, ExportOrigin origin, int indentLevel) {
        final var indent = "  ".repeat(indentLevel);
        final var fieldIndent = "  ".repeat(indentLevel + 1);
        final List<String> fields = new ArrayList<>();

        fields.add(fieldIndent + "\"type\": \"" + StringUtility.escapeJson(EntityType.getKey(entity.getType()).toString()) + "\"");
        fields.add(fieldIndent + "\"position\": " + deltaPositionToJson(entity, origin));
        fields.add(fieldIndent + "\"rotation\": {\"yaw\": " + entity.getYRot() + ", \"pitch\": " + entity.getXRot() + "}");

        if (entity instanceof Display display) {
            appendDisplayFields(fields, fieldIndent, display);
        }

        if (entity instanceof BlockDisplay blockDisplay) {
            appendBlockDisplayFields(fields, fieldIndent, blockDisplay);
        }

        if (entity instanceof Display.ItemDisplay itemDisplay) {
            appendItemDisplayFields(fields, fieldIndent, itemDisplay);
        }

        if (entity instanceof ArmorStand armorStand) {
            appendArmorStandFields(fields, fieldIndent, armorStand);
        }

        out.append(indent).append("{\n");
        for (var index = 0; index < fields.size(); index++) {
            out.append(fields.get(index));
            if (index < fields.size() - 1) {
                out.append(",");
            }
            out.append("\n");
        }
        out.append(indent).append("}");
    }

    private static void appendDisplayFields(List<String> fields, String fieldIndent, Display display) {
        final var renderState = display.renderState();
        final var transformation = renderState != null
            ? renderState.transformation().get(1.0f)
            : new Transformation(new org.joml.Vector3f(), new org.joml.Quaternionf(), new org.joml.Vector3f(1, 1, 1), new org.joml.Quaternionf());

        fields.add(fieldIndent + "\"translation\": " + vector3ToJson(transformation.translation()));
        fields.add(fieldIndent + "\"scale\": " + vector3ToJson(transformation.scale()));
        fields.add(fieldIndent + "\"leftRotation\": " + quaternionToJson(transformation.leftRotation()));
        fields.add(fieldIndent + "\"rightRotation\": " + quaternionToJson(transformation.rightRotation()));
    }

    private static void appendBlockDisplayFields(List<String> fields, String fieldIndent, BlockDisplay blockDisplay) {
        final var blockState = blockDisplay.blockRenderState() != null
            ? blockDisplay.blockRenderState().blockState()
            : null;
        final var blockId = blockState != null && blockState.getBlock() != null
            ? blockState.getBlock().builtInRegistryHolder().unwrapKey()
                .map(key -> key.identifier().toString())
                .orElse("minecraft:air")
            : "minecraft:air";

        fields.add(fieldIndent + "\"id\": \"" + StringUtility.escapeJson(blockId) + "\"");
        if (blockState != null) {
            fields.add(fieldIndent + "\"blockState\": \"" + StringUtility.escapeJson(blockState.toString()) + "\"");
        }
    }

    private static void appendItemDisplayFields(List<String> fields, String fieldIndent, Display.ItemDisplay itemDisplay) {
        final var itemRenderState = itemDisplay.itemRenderState();
        final var itemStack = itemRenderState != null ? itemRenderState.itemStack() : ItemStack.EMPTY;
        fields.add(fieldIndent + "\"item\": " + itemStackToJson(itemStack));
    }

    private static void appendArmorStandFields(List<String> fields, String fieldIndent, ArmorStand armorStand) {
        fields.add(fieldIndent + "\"invisible\": " + armorStand.isInvisible());
        fields.add(fieldIndent + "\"small\": " + armorStand.isSmall());
        fields.add(fieldIndent + "\"marker\": " + armorStand.isMarker());
        fields.add(fieldIndent + "\"showArms\": " + armorStand.showArms());
        fields.add(fieldIndent + "\"showBasePlate\": " + armorStand.showBasePlate());
        fields.add(fieldIndent + "\"equipment\": " + equipmentToJson(armorStand));
        fields.add(fieldIndent + "\"pose\": " + armorStandPoseToJson(armorStand));
    }

    private static String deltaPositionToJson(Entity entity, ExportOrigin origin) {
        return "{\"x\": " + (entity.getX() - origin.x())
            + ", \"y\": " + (entity.getY() - origin.y())
            + ", \"z\": " + (entity.getZ() - origin.z()) + "}";
    }

    private static double distanceToSqr(Entity entity, ExportOrigin origin) {
        final double dx = entity.getX() - origin.x();
        final double dy = entity.getY() - origin.y();
        final double dz = entity.getZ() - origin.z();
        return dx * dx + dy * dy + dz * dz;
    }

    private static String vector3ToJson(Vector3fc vector) {
        return "{\"x\": " + vector.x() + ", \"y\": " + vector.y() + ", \"z\": " + vector.z() + "}";
    }

    private static String quaternionToJson(Quaternionfc quaternion) {
        return "{\"x\": " + quaternion.x() + ", \"y\": " + quaternion.y() + ", \"z\": " + quaternion.z() + ", \"w\": " + quaternion.w() + "}";
    }

    private static String rotationsToJson(Rotations rotations) {
        return "{\"x\": " + rotations.x() + ", \"y\": " + rotations.y() + ", \"z\": " + rotations.z() + "}";
    }

    private static String armorStandPoseToJson(ArmorStand armorStand) {
        return "{"
            + "\"head\": " + rotationsToJson(armorStand.getHeadPose())
            + ", \"body\": " + rotationsToJson(armorStand.getBodyPose())
            + ", \"leftArm\": " + rotationsToJson(armorStand.getLeftArmPose())
            + ", \"rightArm\": " + rotationsToJson(armorStand.getRightArmPose())
            + ", \"leftLeg\": " + rotationsToJson(armorStand.getLeftLegPose())
            + ", \"rightLeg\": " + rotationsToJson(armorStand.getRightLegPose())
            + "}";
    }

    private static String equipmentToJson(ArmorStand armorStand) {
        final var entries = new ArrayList<String>();
        for (var slot : EQUIPMENT_SLOTS) {
            final var stack = armorStand.getItemBySlot(slot);
            if (stack.isEmpty()) {
                continue;
            }

            entries.add("\"" + StringUtility.escapeJson(slot.getName()) + "\": " + itemStackToJson(stack));
        }
        return "{" + String.join(", ", entries) + "}";
    }

    private static String itemStackToJson(ItemStack stack) {
        final var out = new StringBuilder();
        out.append("{");

        final var itemId = stack.getItem().builtInRegistryHolder().unwrapKey()
            .map(key -> key.identifier().toString())
            .orElse("minecraft:air");

        out.append("\"id\": \"")
            .append(StringUtility.escapeJson(itemId))
            .append("\"");
        out.append(", \"count\": ").append(stack.getCount());

        final var registryAccess = Minecraft.getInstance().level != null
            ? Minecraft.getInstance().level.registryAccess()
            : RegistryAccess.EMPTY;
        final RegistryOps<Tag> registryOps = registryAccess.createSerializationContext(NbtOps.INSTANCE);
        final var encodedTag = ItemStack.OPTIONAL_CODEC.encodeStart(registryOps, stack)
            .result()
            .orElse(new CompoundTag());
        out.append(", \"snbt\": \"")
            .append(StringUtility.escapeJson(encodedTag.toString()))
            .append("\"");

        out.append("}");
        return out.toString();
    }

    private record ExportOrigin(double x, double y, double z) {
    }
}
