package net.swofty.gui;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.EntityType;
import net.minestom.server.instance.block.Block;
import net.minestom.server.network.packet.server.play.BlockChangePacket;
import net.minestom.server.network.packet.server.play.BlockEntityDataPacket;
import net.minestom.server.network.packet.server.play.OpenSignEditorPacket;
import net.swofty.SkyBlock;
import net.swofty.user.SkyBlockPlayer;
import org.jglrxavpok.hephaistos.nbt.NBT;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class SkyBlockSignGUI {
    public static Map<SkyBlockPlayer, CompletableFuture<String>> signGUIs = new HashMap<>();
    private static final Pos SIGN_POSITION = new Pos(0, 0, 0);
    private final SkyBlockPlayer player;

    public SkyBlockSignGUI(SkyBlockPlayer player) {
        this.player = player;
    }

    public CompletableFuture<String> open(String[] text) {
        NBTCompound compound = new NBTCompound().withEntries(
                NBT.Entry("Color", NBT.String("black")),
                NBT.Entry("GlowingText", NBT.Byte(0)),
                NBT.Entry("Text1", NBT.String("{\"text\":\"\"}")),
                NBT.Entry("Text2", NBT.String("{\"text\":\"^^^^^^^^\"}")),
                NBT.Entry("Text3", NBT.String("{\"text\":\"" + text[0] + "\"}")),
                NBT.Entry("Text4", NBT.String("{\"text\":\"" + text[1] + "\"}"))
        );

        player.sendPackets(
                new BlockChangePacket(SIGN_POSITION, Block.OAK_SIGN),
                new BlockEntityDataPacket(SIGN_POSITION, Block.OAK_SIGN.registry().blockEntityId(), compound),
                new OpenSignEditorPacket(SIGN_POSITION)
        );

        CompletableFuture<String> future = new CompletableFuture<>();
        signGUIs.put(player, future);
        return future;
    }
}
