package net.swofty.types.generic;

import lombok.Getter;
import lombok.Setter;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.instance.SharedInstance;
import net.swofty.commons.ServerType;

public class SkyBlockConst {
    @Getter
    @Setter
    private static SharedInstance instanceContainer;
    @Getter
    @Setter
    private static SharedInstance emptyInstance;
    @Getter
    @Setter
    private static GlobalEventHandler eventHandler;
    @Getter
    @Setter
    private static String serverName;
    @Getter
    @Setter
    private static String shortenedServerName;
    @Getter
    @Setter
    private static SkyBlockTypeLoader typeLoader;
    @Getter
    private static Integer currentIslandVersion = 1;
    @Getter
    @Setter
    private static int port;
    @Getter
    @Setter
    private static int maxPlayers;

    public static boolean isIslandServer() {
        return typeLoader.getType() == ServerType.ISLAND;
    }
}