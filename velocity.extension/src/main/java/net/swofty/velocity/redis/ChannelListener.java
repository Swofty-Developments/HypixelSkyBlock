package net.swofty.velocity.redis;

import net.swofty.commons.proxy.ToProxyChannels;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ChannelListener {
    ToProxyChannels channel();
}
