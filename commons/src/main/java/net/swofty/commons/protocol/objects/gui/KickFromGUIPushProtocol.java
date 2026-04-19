package net.swofty.commons.protocol.objects.gui;

import net.swofty.commons.protocol.ServicePushProtocol;

import java.util.List;
import java.util.UUID;

public class KickFromGUIPushProtocol
        extends ServicePushProtocol<KickFromGUIPushProtocol.Request, KickFromGUIPushProtocol.Response> {

    public KickFromGUIPushProtocol() {
        super(Request.class, Response.class);
    }

    public record Request(List<UUID> playerUUIDs, String guiType) {}

    public record Response(boolean success, List<UUID> kickedPlayers, int totalKicked) {}
}
