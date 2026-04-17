package net.swofty.type.bedwarsgame.gui;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.commons.bedwars.BedWarsGameType;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig.TeamKey;
import net.swofty.type.bedwarsgame.game.v2.BedWarsGame;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.StatelessView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class GUIQuickCommunications extends StatelessView {

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Quick Communications", InventoryType.CHEST_5_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        addSendButton(
            layout,
            10,
            "§aHello ( ﾟ◡ﾟ)/!",
            Material.BOOK,
            "§aHello ( ﾟ◡ﾟ)/!"
        );
        addSendButton(
            layout,
            11,
            "§aI'm coming back to base!",
            Material.BOOK,
            "§aI'm coming back to base!"
        );
        addSendButton(
            layout,
            12,
            "§aI'm defending!",
            Material.IRON_BARS,
            "§aI'm defending!"
        );
        addSelectButton(
            layout,
            13,
            "§aI'm attacking!",
            Material.IRON_SWORD,
            () -> GUISelectAnOption.forTeamCommunication("I'm attacking"),
            "§7You will be able to select the Team.",
            "",
            "§eClick to select!"
        );
        addSelectButton(
            layout,
            14,
            "§aI'm collecting resources!",
            Material.DIAMOND,
            () -> GUISelectAnOption.forResourceCommunication("I'm collecting"),
            "§7You will be able to select the",
            "§7Resource.",
            "",
            "§eClick to select!"
        );
        addSelectButton(
            layout,
            15,
            "§aI have resources!",
            Material.CHEST,
            () -> GUISelectAnOption.forResourceCommunication("I have"),
            "§7You will be able to select the",
            "§7Resource.",
            "",
            "§eClick to select!"
        );
        addSendButton(
            layout,
            20,
            "§aThank You!",
            Material.BOOK,
            "§aThank You!"
        );
        addSendButton(
            layout,
            21,
            "§aGet back to base!",
            Material.BOOK,
            "§aGet back to base!"
        );
        addSendButton(
            layout,
            22,
            "§aPlease defend!",
            Material.IRON_BARS,
            "§aPlease defend!"
        );
        addSelectButton(
            layout,
            23,
            "§aLet's attack!",
            Material.IRON_SWORD,
            () -> GUISelectAnOption.forTeamCommunication("Let's attack"),
            "§7You will be able to select the Team.",
            "",
            "§eClick to select!"
        );
        addSelectButton(
            layout,
            24,
            "§aWe need resources!",
            Material.DIAMOND,
            () -> GUISelectAnOption.forResourceCommunication("We need"),
            "§7You will be able to select the",
            "§7Resource.",
            "",
            "§eClick to select!"
        );
        addSendButton(
            layout,
            25,
            "§aPlayer incoming!!",
            Material.FEATHER,
            "§aPlayer incoming!!"
        );

        Components.back(layout, 40, ctx);
    }

    private void addSendButton(ViewLayout<DefaultState> layout,
                               int slot,
                               String title,
                               Material icon,
                               String message) {
        layout.slot(slot, ItemStackCreator.getStack(
            title,
            icon,
            1,
            "",
            "§eClick to send!"
        ), (click, context) -> {
            if (!(click.player() instanceof BedWarsPlayer player)) {
                return;
            }

            sendTeamQuickMessage(player, message);
            playClickSound(player);
            player.closeInventory();
        });
    }

    private void addSelectButton(ViewLayout<DefaultState> layout,
                                 int slot,
                                 String title,
                                 Material icon,
                                 Supplier<GUISelectAnOption> selectViewSupplier,
                                 String... lore) {
        layout.slot(slot, ItemStackCreator.getStack(title, icon, 1, lore), (click, context) -> {
            playClickSound(click.player());
            context.push(selectViewSupplier.get());
        });
    }

    static void sendTeamQuickMessage(BedWarsPlayer player, String message) {
        BedWarsGame game = player.getGame();
        if (game == null) {
            return;
        }

        TeamKey teamKey = resolveTeamKey(player);
        List<BedWarsPlayer> receivers;
        if (game.getGameType() == BedWarsGameType.SOLO || teamKey == null) {
            receivers = new ArrayList<>(game.getPlayers());
        } else {
            receivers = game.getPlayersOnTeam(teamKey);
        }

        String formatted = "§a§lTEAM > §r" + player.getFullDisplayName() + "§f: " + message;
        receivers.forEach(receiver -> receiver.sendMessage(formatted));
    }

    static TeamKey resolveTeamKey(BedWarsPlayer player) {
        String teamName = player.getTeamName();
        if (teamName == null) {
            return null;
        }

        try {
            return TeamKey.valueOf(teamName);
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }

    static void playClickSound(HypixelPlayer player) {
        player.playSound(Sound.sound(Key.key("minecraft:ui.button.click"), Sound.Source.MASTER, 1.0f, 1.0f));
    }

    static void playBuySound(HypixelPlayer player) {
        player.playSound(Sound.sound(Key.key("minecraft:entity.experience_orb.pickup"), Sound.Source.MASTER, 1.0f, 1.0f));
    }
}
