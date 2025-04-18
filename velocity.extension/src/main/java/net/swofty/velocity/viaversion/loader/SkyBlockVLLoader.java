package net.swofty.velocity.viaversion.loader;

import com.viaversion.vialoader.impl.viaversion.VLLoader;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.platform.providers.ViaProviders;
import com.viaversion.viaversion.api.protocol.version.VersionProvider;
import net.swofty.velocity.viaversion.provider.SkyBlockVersionProvider;

public class SkyBlockVLLoader extends VLLoader {

    @Override
    public void load() {
        ViaProviders providers = Via.getManager().getProviders();
        providers.use(VersionProvider.class, new SkyBlockVersionProvider());
    }
}
