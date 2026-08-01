package gg.itzkatze.thehypixelrecreationmod.mixin;

import gg.itzkatze.thehypixelrecreationmod.features.SpraySchemaRecorder;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.MouseButtonEvent;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractContainerScreen.class)
public class AbstractContainerScreenMixin {

    @Inject(method = "mouseClicked", at = @At("HEAD"))
    private void thehypixelrecreationmod$captureSprayRightClick(MouseButtonEvent event, boolean doubleClick, CallbackInfoReturnable<Boolean> cir) {
        if (event.button() == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
            SpraySchemaRecorder.handleContainerRightClick((AbstractContainerScreen<?>) (Object) this);
        }
    }
}
