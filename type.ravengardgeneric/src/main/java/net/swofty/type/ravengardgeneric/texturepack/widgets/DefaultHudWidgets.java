package net.swofty.type.ravengardgeneric.texturepack.widgets;

public final class DefaultHudWidgets {
    private DefaultHudWidgets() {
    }

    public static void registerInto(HudWidgetRegistry registry) {
        registry.register(HudWidgetType.WORLD_HUD, new TexturePackWorldHudWidget(HudWidgetType.WORLD_HUD));
        registry.register(HudWidgetType.SERVER_INFO, new ServerInfoHudWidget(
                HudWidgetType.SERVER_INFO,
                ServerInfoHudWidget.DEFAULT_DOMAIN
        ));
        registry.register(HudWidgetType.TOP_BANNER, new TopBannerHudWidget(
                HudWidgetType.TOP_BANNER,
                TopBannerHudWidget.DEFAULT_TITLE
        ));
        registry.register(HudWidgetType.STATS_HUD, new TexturePackStatsHudWidget(
                HudWidgetType.STATS_HUD,
                () -> "99:99",
                () -> 3,
                () -> 1,
                () -> 1
        ));
    }
}
