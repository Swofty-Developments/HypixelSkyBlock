package gg.itzkatze.thehypixelrecreationmod.commands;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import gg.itzkatze.thehypixelrecreationmod.utils.ChatUtils;
import gg.itzkatze.thehypixelrecreationmod.utils.StringUtility;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public final class PlayerSkinCommand {
    private static final DecimalFormatSymbols DECIMAL_FORMAT_SYMBOLS = DecimalFormatSymbols.getInstance(Locale.US);
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.###", DECIMAL_FORMAT_SYMBOLS);

    private PlayerSkinCommand() {
    }

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, _) -> dispatcher.register(
                ClientCommands.literal("getskins")
                        .then(ClientCommands.argument("radius", DoubleArgumentType.doubleArg(0))
                                .executes(context -> execute(DoubleArgumentType.getDouble(context, "radius"))))
        ));
    }

    private static int execute(double radius) {
        Minecraft client = Minecraft.getInstance();
        Player sender = client.player;
        if (sender == null || client.level == null) {
            return 1;
        }

        List<Player> nearby = client.level.getEntitiesOfClass(
                Player.class,
                sender.getBoundingBox().inflate(radius),
                entity -> entity.getType() == EntityTypes.PLAYER
        );

        if (nearby.isEmpty()) {
            ChatUtils.warn("No players found nearby.");
            return 1;
        }

        for (Player player : nearby) {
            if (player.getName().getString().equals(sender.getName().getString())) {
                continue;
            }

            sendSkinData(player);
        }

        return 1;
    }

    private static void sendSkinData(Player player) {
        GameProfile profile = player.getGameProfile();
        Collection<Property> properties = profile.properties().get("textures");

        if (properties.isEmpty()) {
            ChatUtils.send(Component.literal("No skin data for " + getOverheadName(player)));
            return;
        }

        Property textureProperty = properties.iterator().next();
        String texture = textureProperty.value();
        String signature = textureProperty.signature();
        String npcParameters = npcParameters(player, texture, signature);

        Component textureMessage = copyMessage("Copy Texture (click)", texture, ChatFormatting.GREEN);
        Component signatureMessage = copyMessage("Copy Signature (click)", signature == null ? "" : signature, ChatFormatting.AQUA);
        Component npcParametersMessage = copyMessage("Copy premade NPCParameters (click)", npcParameters, ChatFormatting.YELLOW);

        ChatUtils.send(Component.literal("Skin data for ").append(Component.literal(getOverheadName(player))));
        ChatUtils.sendLine();
        ChatUtils.send(textureMessage);
        ChatUtils.sendLine();
        ChatUtils.send(signatureMessage);
        ChatUtils.sendLine();
        ChatUtils.send(npcParametersMessage);
        ChatUtils.sendLine();
    }

    private static Component copyMessage(String label, String clipboardValue, ChatFormatting color) {
        return Component.literal(label)
                .setStyle(Style.EMPTY
                        .withClickEvent(new ClickEvent.CopyToClipboard(clipboardValue))
                        .withColor(TextColor.fromLegacyFormat(color))
                );
    }

    private static String npcParameters(Player player, String texture, String signature) {
        String hologram = StringUtility.escapeJavaString(getOverheadName(player));
        String safeSignature = StringUtility.escapeJavaString(signature == null ? "" : signature);
        String safeTexture = StringUtility.escapeJavaString(texture == null ? "" : texture);

        return """
                super(new HumanConfiguration() {
                            @Override
                            public String[] holograms(HypixelPlayer player) {
                                return new String[]{"%s", "§e§lCLICK"};
                            }

                            @Override
                            public String signature(HypixelPlayer player) {
                                return "%s";
                            }

                            @Override
                            public String texture(HypixelPlayer player) {
                                return "%s";
                            }

                            @Override
                            public Pos position(HypixelPlayer player) {
                                return new Pos(%s, %s, %s, %s, %s);
                            }

                            @Override
                            public boolean looking(HypixelPlayer player) {
                                return true;
                            }
                        });""".formatted(
                hologram,
                safeSignature,
                safeTexture,
                DECIMAL_FORMAT.format(player.getX()),
                DECIMAL_FORMAT.format(player.getY()),
                DECIMAL_FORMAT.format(player.getZ()),
                Math.round(player.getYRot()),
                Math.round(player.getXRot())
        );
    }

    private static String getOverheadName(Entity npc) {
        Level level = npc.level();
        if (!(level instanceof ClientLevel clientLevel)) {
            return "Unknown";
        }

        AABB boxAbove = npc.getBoundingBox().inflate(0.5, 2.5, 0.5);

        for (Entity entity : clientLevel.entitiesForRendering()) {
            if (entity == npc) {
                continue;
            }

            if (entity.getBoundingBox().intersects(boxAbove)
                    && (entity instanceof Display.TextDisplay || entity instanceof ArmorStand)
                    && entity.hasCustomName()) {
                String raw = StringUtility.toLegacyString(entity.getCustomName());
                if (!raw.isEmpty()) {
                    return raw;
                }
            }
        }

        return "Unknown";
    }
}
