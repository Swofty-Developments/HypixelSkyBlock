package net.swofty.types.generic.gui;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.block.Block;
import net.minestom.server.network.packet.server.play.BlockChangePacket;
import net.minestom.server.network.packet.server.play.BlockEntityDataPacket;
import net.minestom.server.network.packet.server.play.OpenSignEditorPacket;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jglrxavpok.hephaistos.nbt.NBT;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;
import org.jglrxavpok.hephaistos.nbt.NBTList;
import org.jglrxavpok.hephaistos.nbt.NBTType;
import org.jglrxavpok.hephaistos.nbt.mutable.MutableNBTCompound;

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

        NBTCompound compound = new NBTCompound().withEntries(
                NBT.Entry("is_waxed", NBT.Byte(0)),
                NBT.Entry("back_text", NBT.Compound(new MutableNBTCompound()
                        .set("has_glowing_text", NBT.Byte(0))
                        .set("color", NBT.String("black"))
                        .set("messages", new NBTList<>(NBTType.TAG_String, List.of(
                                NBT.String("{\"text\":\"\"}"),
                                NBT.String("{\"text\":\"\"}"),
                                NBT.String("{\"text\":\"\"}"),
                                NBT.String("{\"text\":\"\"}")
                        )))
                        .asMapView())),
                NBT.Entry("front_text", NBT.Compound(new MutableNBTCompound()
                        .set("has_glowing_text", NBT.Byte(0))
                        .set("color", NBT.String("black"))
                        .set("messages", new NBTList<>(NBTType.TAG_String, List.of(
                                NBT.String("{\"text\":\"\"}"),
                                NBT.String("{\"text\":\"^^^^^^^^\"}"),
                                NBT.String("{\"text\":\"" + text[0] + "\"}"),
                                NBT.String("{\"text\":\"" + text[1] + "\"}")
                        )))
                        .asMapView()))
        );

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
