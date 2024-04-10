package net.swofty.velocity.via.loader;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.protocol.version.VersionProvider;
import net.raphimc.vialoader.impl.viaversion.VLLoader;
import net.swofty.velocity.via.provider.SkyBlockVersionProvider;


public class SkyBlockVLLoader extends VLLoader {
    @Override
    public void load() {
        Via.getManager().getProviders().use(VersionProvider.class , new SkyBlockVersionProvider());
    }
}
