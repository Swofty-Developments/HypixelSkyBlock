package net.swofty.type.replayviewer.view;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.commons.ServerType;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.StatelessView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.replayviewer.TypeReplayViewerLoader;
import net.swofty.type.replayviewer.util.ReplayShareUtil;

public class GUIReplayViewer extends StatelessView {

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Replay Viewer", InventoryType.CHEST_3_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        layout.slot(9, ItemStackCreator.getStack(
            "§aSettings",
            Material.OAK_SIGN,
            1,
            "§7Manage your settings for viewing",
            "§7replays.",
            "",
            "§eClick to open!"
        ), (s, c) -> {
            c.push(new GUIViewerSettings());
        });

        layout.slot(11, ItemStackCreator.getStack(
            "§aBookmarks",
            Material.BOOK,
            1,
            "§7View bookmarks for this recording.",
            "",
            "§eClick to view!"
        ), (_, c) -> c.push(new GUIBookmarks()));

        layout.slot(13, ItemStackCreator.getStack(
            "§aShare",
            Material.PAPER,
            1,
            "§7Share this replay along with your",
            "§7current timestamp and location.",
            "",
            "§eClick to share!"
        ), (_, c) -> TypeReplayViewerLoader.getSession(c.player()).ifPresentOrElse(
            session -> ReplayShareUtil.sendShareCommandMessage(c.player(), session),
            () -> c.player().sendMessage("§cError: no active replay session.")
        ));

        // for now, this can't be implemented
        layout.slot(15, ItemStackCreator.getStack(
            "§aSubmit Highlight",
            Material.FILLED_MAP,
            1,
            "§7Did something cool? Share your",
            "§7current timestamp and location with",
            "§7us for a chance to be showcased on",
            "§7Hypixel social media!",
            "",
            "§7By submitting this highlight, you",
            "§7agree to the Hypixel Server",
            "§7potentially using this content on the",
            "§7Hypixel Twitter, TikTok, Instagram, or",
            "§7other social media platform. Make",
            "§7sure you are in the right location",
            "§7and time to showcase your highlight!",
            "",
            "§eClick to submit!"
        ), (_, viewContext) -> viewContext.player().notImplemented());

        layout.slot(17, ItemStackCreator.getStack(
            "§aLeave Replay",
            Material.DARK_OAK_DOOR,
            1,
            "§eClick to leave!"
        ), (_, c) -> {
            TypeReplayViewerLoader.getSession(c.player())
                .ifPresent(session -> session.removeViewer(c.player()));
            c.player().sendTo(ServerType.PROTOTYPE_LOBBY);
        });
    }
}
