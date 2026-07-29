package net.swofty.type.generic.collectibles.bedwars.prestige;

import java.util.Arrays;
import java.util.List;

public record BedWarsPrestigeStyle(
    TextPaint openBracket,
    DigitPaint digits,
    TextPaint star,
    TextPaint closeBracket
) {
    public BedWarsPrestigeStyle {
        openBracket = openBracket == null ? TextPaint.none() : openBracket;
        digits = digits == null ? DigitPaint.none() : digits;
        star = star == null ? TextPaint.none() : star;
        closeBracket = closeBracket == null ? TextPaint.none() : closeBracket;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static BedWarsPrestigeStyle solid(String color) {
        return builder()
            .openBracket(color)
            .digits(color)
            .star(color)
            .closeBracket(color)
            .build();
    }

    public static BedWarsPrestigeStyle colors(String openColor, List<String> digitColors, String starColor, String closeColor) {
        return BedWarsPrestigeStyle.builder()
            .openBracket(openColor)
            .digits(digitColors.toArray(String[]::new))
            .star(starColor)
            .closeBracket(closeColor)
            .build();
    }

    public String render(String level, String starSymbol, BedWarsPrestigeDefinitions.Bracket bracket, boolean includeBrackets) {
        StringBuilder rendered = new StringBuilder();
        if (includeBrackets) {
            rendered.append(openBracket.apply(bracket.open()));
        }
        for (int i = 0; i < level.length(); i++) {
            rendered.append(digits.apply(String.valueOf(level.charAt(i)), i, level.length()));
        }
        rendered.append(star.apply(starSymbol));
        if (includeBrackets) {
            rendered.append(closeBracket.apply(bracket.close()));
        }
        return rendered.toString();
    }

    public static final class Builder {
        private TextPaint openBracket = TextPaint.none();
        private DigitPaint digits = DigitPaint.none();
        private TextPaint star = TextPaint.none();
        private TextPaint closeBracket = TextPaint.none();

        public Builder openBracket(String color) {
            this.openBracket = TextPaint.color(color);
            return this;
        }

        public Builder digits(String... colors) {
            this.digits = DigitPaint.gradient(colors);
            return this;
        }

        public Builder star(String color) {
            this.star = TextPaint.color(color);
            return this;
        }

        public Builder closeBracket(String color) {
            this.closeBracket = TextPaint.color(color);
            return this;
        }

        public Builder all(String color) {
            return openBracket(color).digits(color).star(color).closeBracket(color);
        }

        public BedWarsPrestigeStyle build() {
            return new BedWarsPrestigeStyle(openBracket, digits, star, closeBracket);
        }
    }

    public record TextPaint(String color) {
        public TextPaint {
            color = color == null ? "" : color;
        }

        public static TextPaint none() {
            return new TextPaint("");
        }

        public static TextPaint color(String color) {
            return new TextPaint(color);
        }

        public String apply(String text) {
            return color + text;
        }
    }

    public record DigitPaint(List<String> colors) {
        public DigitPaint {
            colors = colors == null ? List.of("") : List.copyOf(colors);
            if (colors.isEmpty()) {
                colors = List.of("");
            }
        }

        public static DigitPaint none() {
            return new DigitPaint(List.of(""));
        }

        public static DigitPaint gradient(String... colors) {
            if (colors == null || colors.length == 0) {
                return none();
            }
            return new DigitPaint(Arrays.asList(colors));
        }

        public String apply(String digit, int index, int totalDigits) {
            return colorAt(index, totalDigits) + digit;
        }

        private String colorAt(int index, int totalDigits) {
            if (colors.size() == 1 || totalDigits <= 1) {
                return colors.getFirst();
            }
            int mappedIndex = Math.min(colors.size() - 1, Math.round(index * (colors.size() - 1) / (float) (totalDigits - 1)));
            return colors.get(mappedIndex);
        }
    }

}
