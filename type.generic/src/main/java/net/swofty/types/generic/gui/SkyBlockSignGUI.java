package net.swofty.types.generic.gui;

import net.kyori.adventure.nbt.ByteBinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.ListBinaryTag;
import net.kyori.adventure.nbt.StringBinaryTag;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.block.Block;
import net.minestom.server.network.packet.server.play.BlockChangePacket;
import net.minestom.server.network.packet.server.play.BlockEntityDataPacket;
import net.minestom.server.network.packet.server.play.OpenSignEditorPacket;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class SkyBlockSignGUI {
    public static Map<SkyBlockPlayer, SignGUI> signGUIs = new HashMap<>();
    private final SkyBlockPlayer player;

    public SkyBlockSignGUI(SkyBlockPlayer player) {
        this.player = player;
    }

    public CompletableFuture<String> open(String[] text) {
        Pos pos = player.getPosition().add(0, 6, 0);

        CompoundBinaryTag compound = CompoundBinaryTag.builder()
                .put("is_waxed", ByteBinaryTag.byteBinaryTag((byte) 0))
                .put("back_text", CompoundBinaryTag.builder()
                        .put("has_glowing_text", ByteBinaryTag.byteBinaryTag((byte) 0))
                        .put("color", StringBinaryTag.stringBinaryTag("black"))
                        .put("messages", ListBinaryTag.from(List.of(
                                StringBinaryTag.stringBinaryTag("{\"text\":\"\"}"),
                                StringBinaryTag.stringBinaryTag("{\"text\":\"\"}"),
                                StringBinaryTag.stringBinaryTag("{\"text\":\"\"}"),
                                StringBinaryTag.stringBinaryTag("{\"text\":\"\"}"))))
                        .build())
                .put("front_text", CompoundBinaryTag.builder()
                        .put("has_glowing_text", ByteBinaryTag.byteBinaryTag((byte) 0))
                        .put("color", StringBinaryTag.stringBinaryTag("black"))
                        .put("messages", ListBinaryTag.from(List.of(
                                StringBinaryTag.stringBinaryTag("{\"text\":\"\"}"),
                                StringBinaryTag.stringBinaryTag("{\"text\":\"^^^^^^^^\"}"),
                                StringBinaryTag.stringBinaryTag("{\"text\":\"" + text[0] + "\"}"),
                                StringBinaryTag.stringBinaryTag("{\"text\":\"" + text[1] + "\"}")))
                        ).build())
                .build();

        player.sendPackets(
                new BlockChangePacket(pos, Block.OAK_SIGN),
                new BlockEntityDataPacket(pos, Block.OAK_SIGN.registry().blockEntityId(), compound)
        );
        MinecraftServer.getSchedulerManager().scheduleTask(() -> {
            player.sendPacket(new OpenSignEditorPacket(pos, true));
        }, TaskSchedule.tick(2), TaskSchedule.stop());

        CompletableFuture<String> future = new CompletableFuture<>();
        signGUIs.put(player, new SignGUI(future, pos, player.getInstance().getBlock(pos)));
        return future;
    }

    public record SignGUI(CompletableFuture<String> future, Pos pos, Block block) {
    }
}
