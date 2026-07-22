package gg.itzkatze.thehypixelrecreationmod.mixin;

import gg.itzkatze.thehypixelrecreationmod.features.nbs.SoundNbsRecorder;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundSoundEntityPacket;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin {
    @Inject(method = "handleSoundEvent", at = @At("HEAD"))
    private void recordSoundEvent(ClientboundSoundPacket packet, CallbackInfo callbackInfo) {
        SoundNbsRecorder.recordServerSound(
                packet.getSound().value().location(),
                packet.getVolume(),
                packet.getPitch(),
                packet.getSeed()
        );
    }

    @Inject(method = "handleSoundEntityEvent", at = @At("HEAD"))
    private void recordSoundEntityEvent(ClientboundSoundEntityPacket packet, CallbackInfo callbackInfo) {
        SoundNbsRecorder.recordServerSound(
                packet.getSound().value().location(),
                packet.getVolume(),
                packet.getPitch(),
                packet.getSeed()
        );
    }
}
