package net.swofty.velocity.viaversion;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.platform.ViaPlatformLoader;
import com.viaversion.viaversion.api.platform.providers.ViaProviders;
import com.viaversion.viaversion.api.protocol.version.VersionProvider;

public final class SkyBlockPlatformLoader implements ViaPlatformLoader {
    @Override
    public void load() {
        ViaProviders providers = Via.getManager().getProviders();
        providers.use(VersionProvider.class, new SkyBlockVersionProvider());
    }

    @Override
    public void unload() {
    }
}
