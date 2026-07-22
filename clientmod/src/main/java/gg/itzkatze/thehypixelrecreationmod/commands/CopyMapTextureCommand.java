package gg.itzkatze.thehypixelrecreationmod.commands;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import gg.itzkatze.thehypixelrecreationmod.utils.ChatUtils;
import gg.itzkatze.thehypixelrecreationmod.utils.ClipboardUtils;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.zip.GZIPOutputStream;

public final class CopyMapTextureCommand {
    private CopyMapTextureCommand() {
    }

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, _) -> {
            dispatcher.register(
                ClientCommands.literal("copymaptexture")
                    .then(ClientCommands.argument("radius", DoubleArgumentType.doubleArg(0))
                        .executes(context -> {
                            double radius = DoubleArgumentType.getDouble(context, "radius");
                            Minecraft client = Minecraft.getInstance();
                            Player player = client.player;

                            if (player == null || client.level == null) {
                                ChatUtils.error("Player or level is null.");
                                return 1;
                            }

                            ItemFrame anchor = getTargetedItemFrame(player);
                            if (anchor == null) {
                                ChatUtils.warn("You must be looking at an item frame.");
                                return 1;
                            }

                            // Get facing vectors from player POV
                            Vec3 forward = player.getLookAngle().normalize();
                            Vec3 up = new Vec3(0, 1, 0);
                            Vec3 right = forward.cross(up).normalize();

                            // Collect nearby item frames on same plane
                            AABB box = anchor.getBoundingBox().inflate(radius);
                            var frames = client.level.getEntitiesOfClass(ItemFrame.class, box, f ->
                                f.getDirection() == anchor.getDirection()
                            );

                            record Entry(ItemFrame frame, double x, double y) {}

                            var entries = frames.stream().map(frame -> {
                                Vec3 delta = frame.position().subtract(anchor.position());
                                double sx = delta.dot(right); // left to right
                                double sy = delta.dot(up);    // bottom to top
                                return new Entry(frame, sx, sy);
                            }).toList();

                            // Sort: top to bottom, left to right
                            var sorted = entries.stream()
                                .sorted((a, b) -> {
                                    int y = Double.compare(b.y, a.y);
                                    if (y != 0) return y;
                                    return Double.compare(a.x, b.x);
                                })
                                .toList();

                            StringBuilder out = new StringBuilder();
                            int exportedTileCount = 0;

                            for (Entry e : sorted) {
                                ItemStack stack = e.frame.getItem();
                                if (!(stack.getItem() instanceof MapItem)) continue;

                                MapId id = stack.get(net.minecraft.core.component.DataComponents.MAP_ID);
                                if (id == null) continue;

                                MapItemSavedData data = client.level.getMapData(id);
                                if (data == null || data.colors.length != 128 * 128) continue;

                                try {
                                    String compressed = compressAndEncode(data.colors);
                                    out.append("\"").append(compressed).append("\",\n");
                                    exportedTileCount++;
                                } catch (IOException ignored) {
                                }
                            }

                            if (out.isEmpty()) {
                                ChatUtils.error("No map data found.");
                                return 1;
                            }

                            out.setLength(out.length() - 2);

                            Path outputPath = Minecraft.getInstance().gameDirectory.toPath().resolve("map_export.txt");
                            try {
                                Files.writeString(outputPath, out.toString());
                                ChatUtils.message("Exported " + exportedTileCount + " map tiles to map_export.txt");
                            } catch (IOException e) {
                                ChatUtils.error("Failed to write file: " + e.getMessage());
                            }

                            ClipboardUtils.setClipboard(out.toString());
                            ChatUtils.message("Copied " + exportedTileCount + " map tiles to clipboard.");

                            return 1;
                        })
                    ));
        });
    }

    private static ItemFrame getTargetedItemFrame(Player player) {
        double reachDistance = 5.0;
        Vec3 eyePosition = player.getEyePosition(1.0f);
        Vec3 lookVector = player.getLookAngle();
        Vec3 reachVector = eyePosition.add(lookVector.scale(reachDistance));

        AABB searchBox = player.getBoundingBox().expandTowards(lookVector.scale(reachDistance)).inflate(1.0);
        EntityHitResult hitResult = ProjectileUtil.getEntityHitResult(
            player,
            eyePosition,
            reachVector,
            searchBox,
            entity -> entity instanceof ItemFrame,
            reachDistance * reachDistance
        );

        if (hitResult != null && hitResult.getEntity() instanceof ItemFrame itemFrame) {
            return itemFrame;
        }
        return null;
    }

    private static String compressAndEncode(byte[] data) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (GZIPOutputStream gzos = new GZIPOutputStream(baos)) {
            gzos.write(data);
        }
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }
}
