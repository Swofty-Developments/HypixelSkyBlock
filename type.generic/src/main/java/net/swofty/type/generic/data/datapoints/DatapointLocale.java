package net.swofty.type.generic.data.datapoints;

import lombok.Getter;
import net.swofty.commons.protocol.Serializer;
import net.swofty.type.generic.data.Datapoint;

import java.util.Locale;

public class DatapointLocale extends Datapoint<DatapointLocale.LocaleType> {
    private static final Serializer<LocaleType> serializer = new Serializer<>() {
        @Override
        public String serialize(LocaleType value) {
            return value.currentLocale.name();
        }

        @Override
        public LocaleType deserialize(String json) {
            return new LocaleType(SupportedLocale.valueOf(json));
        }

        @Override
        public LocaleType clone(LocaleType value) {
            return new LocaleType(value.currentLocale);
        }
    };

    public DatapointLocale(String key, LocaleType value) {
        super(key, value, serializer);
    }

    public DatapointLocale(String key) {
        super(key, null, serializer);
    }

    @Getter
    public static class LocaleType {
        private SupportedLocale currentLocale;

        public LocaleType(SupportedLocale currentLocale) {
            this.currentLocale = currentLocale;
        }

        public void switchTo(SupportedLocale locale) {
            this.currentLocale = locale;
        }
    }

    @Getter
    public enum SupportedLocale {
        ENGLISH("English", Locale.US, "4cac9774da1217248532ce147f7831f67a12fdcca1cf0cb4b3848de6bc94b4"),
        FINNISH("Suomi", Locale.forLanguageTag("fi-FI"), "59f2349729a7ec8d4b1478adfe5ca8af96479e983fbad238ccbd81409b4ed");

        private final String name;
        private final Locale locale;
        private final String icon;

        SupportedLocale(String name, Locale locale, String icon) {
            this.name = name;
            this.locale = locale;
            this.icon = icon;
        }

        public static SupportedLocale fromLocale(Locale locale) {
            for (SupportedLocale sl : values()) {
                if (sl.locale.equals(locale)) {
                    return sl;
                }
            }
            throw new IllegalArgumentException("Unsupported locale: " + locale);
        }
    }
}
