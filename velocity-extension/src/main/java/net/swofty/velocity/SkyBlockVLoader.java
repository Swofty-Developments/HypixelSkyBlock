package net.swofty.velocity;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.api.protocol.version.VersionProvider;
import com.viaversion.viaversion.protocols.base.BaseVersionProvider;
import net.raphimc.vialoader.ViaLoader;
import net.raphimc.vialoader.impl.viaversion.VLLoader;

public class SkyBlockVLoader extends VLLoader {
    public void load() {
        Via.getManager().getProviders().use(VersionProvider.class, new BaseVersionProvider() {
            @Override
            public int getClosestServerProtocol(UserConnection connection) {
                return ProtocolVersion.v1_20_3.getVersion(); // Target server version
            }
        });
    }
}