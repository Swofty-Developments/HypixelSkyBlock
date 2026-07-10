package net.swofty.type.skyblockgeneric.resourcepack.resourcepack;

import lombok.AllArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

// this is named as PackSprite even though they are font glyphs as of now. I think they're going to change this at some point, so that's why I made it like this.
@AllArgsConstructor
public enum PackSprite {
    // All things found in the font from hypixel_skyblock:gui/icons.png
    GUI_LOCATION("\uE067"),
    GUI_ARROW_LEFT("\uE060"),
    GUI_ARROW_RIGHT("\uE061"),
    GUI_ARROW_UP("\uE062"),
    GUI_ARROW_DOWN("\uE063"),
    GUI_CHECKMARK("✔"),
    GUI_DENY("✖"),
    GUI_LOCK("\uE066"),
    GUI_ARROW_UPWARDS("\uE068"),

    // gui/skills
    SKILL_SWORD("\uE050"),

    // gui/stats

    // gui/mobs

    // icons/staff
    ICONS_STAFF("ዞ");

    // this is hidden like this, so you never accidentally even imagine using it as a String. Use the Component.
    // Hypixel will probably move to sprites, and then we have to use Components.
    private final String sprite;

    public Component getSprite() {
        return Component.text(sprite);
    }

    private static final Map<String, PackSprite> BY_TAG_NAME =
            Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(
                    sprite -> sprite.name().toLowerCase(Locale.ROOT),
                    Function.identity()
            ));

    public static final TagResolver TAG_RESOLVER = TagResolver.resolver(
            "sbglyph",
            (arguments, context) -> {
                String name = arguments.popOr(
                        "Expected a sprite name, for example <sbglyph:gui_deny>"
                ).value();

                PackSprite sprite = BY_TAG_NAME.get(
                        name.toLowerCase(Locale.ROOT)
                );

                if (sprite == null) {
                    throw context.newException(
                            "Unknown SkyBlock glyph '" + name + "'",
                            arguments
                    );
                }

                return Tag.inserting(sprite.getSprite());
            }
    );
}
