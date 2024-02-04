package net.swofty.velocity.via.loader;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.version.VersionProvider;
import com.viaversion.viaversion.protocols.base.BaseVersionProvider;
import net.raphimc.vialoader.impl.viaversion.VLLoader;
import us.myles.ViaVersion.api.protocol.ProtocolVersion;

public class SkyBlockVLLoader extends VLLoader {
    @Override
    public void load() {
        Via.getManager().getProviders().use(VersionProvider.class , new BaseVersionProvider(){
            @Override
            public int getClosestServerProtocol(UserConnection connection) {
                return ProtocolVersion.v1_20_3.getVersion(); // Change the logic here to select the target server version
            }
        });
    }
}
