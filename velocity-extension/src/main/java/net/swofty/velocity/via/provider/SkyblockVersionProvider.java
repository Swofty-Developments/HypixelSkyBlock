package net.swofty.velocity.via.provider;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.protocols.base.BaseVersionProvider;
import net.swofty.velocity.SkyBlockVelocity;
import net.swofty.velocity.via.injector.SkyBlockViaInjector;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.IntStream;

public class SkyblockVersionProvider extends BaseVersionProvider {

    @Override
    public int getClosestServerProtocol(UserConnection user) throws Exception {
        return user.isClientSide() ? ProtocolVersion.v1_20_3.getVersion() : getFrontProtocol(user);
    }

    private int getFrontProtocol(UserConnection user) throws Exception {
        int playerVersion = user.getProtocolInfo().getProtocolVersion();

        IntStream versions = com.velocitypowered.api.network.ProtocolVersion.SUPPORTED_VERSIONS.stream()
                .mapToInt(com.velocitypowered.api.network.ProtocolVersion::getProtocol);

        if (SkyBlockViaInjector.GET_PLAYER_INFO_FORWARDING_MODE != null
                && ((Enum<?>) SkyBlockViaInjector.GET_PLAYER_INFO_FORWARDING_MODE.invoke(SkyBlockVelocity.getServer().getConfiguration()))
                .name().equals("MODERN")) {
            versions = versions.filter(ver -> ver >= ProtocolVersion.v1_13.getVersion());
        }
        int[] compatibleProtocols = versions.toArray();

        if (Arrays.binarySearch(compatibleProtocols, playerVersion) >= 0) {
            // Velocity supports it
            return playerVersion;
        }

        if (playerVersion < compatibleProtocols[0]) {
            // Older than Velocity supports, get the lowest version
            return compatibleProtocols[0];
        }

        // Loop through all protocols to get the closest protocol id that Velocity supports (and that Via does too)

        // TODO: This needs a better fix, i.e checking ProtocolRegistry to see if it would work.
        // This is more of a workaround for snapshot support
        for (int i = compatibleProtocols.length - 1; i >= 0; i--) {
            int protocol = compatibleProtocols[i];
            if (playerVersion > protocol && ProtocolVersion.isRegistered(protocol)) {
                return protocol;
            }
        }

        System.out.println("Panic, no protocol id found for " + playerVersion);
        return playerVersion;
    }
}
