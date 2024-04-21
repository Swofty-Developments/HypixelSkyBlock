package net.swofty.types.generic.utility;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtility {
    public static final char[] ALPHABET = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'W', 'X', 'Y', 'Z'
    };
    private static final DecimalFormat INTEGER_FORMAT = new DecimalFormat("#,###");
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,###.#");
    private static final Pattern COLOR_PATTERN = Pattern.compile("§[0-9a-fk-or]");

    public static String formatTimeAsAgo(long millis) {
        long timeDifference = System.currentTimeMillis() - millis;
        // Simplified the calculation logic by abstracting repetitive calculations
        long[] timeUnits = {TimeUnit.DAYS.toMillis(1), TimeUnit.HOURS.toMillis(1), TimeUnit.MINUTES.toMillis(1)};
        String[] timeLabels = {"d ago", "h ago", "m ago"};
        for (int i = 0; i < timeUnits.length; i++) {
            if (timeDifference >= timeUnits[i]) {
                return (timeDifference / timeUnits[i]) + timeLabels[i];
            }
        }
        return "Just now";
    }

    public static List<String> splitStringByNewLine(String input){
        String[] lines = input.split("\\n");
        return new ArrayList<>(Arrays.asList(lines));
    }

    public static String formatTimeWentBy(long millis) {
        long timeDifference = System.currentTimeMillis() - millis;
        // Simplified the calculation logic by abstracting repetitive calculations
        long[] timeUnits = {TimeUnit.DAYS.toMillis(1), TimeUnit.HOURS.toMillis(1), TimeUnit.MINUTES.toMillis(1)};
        String[] timeLabels = {"d", "h", "m"};
        for (int i = 0; i < timeUnits.length; i++) {
            if (timeDifference >= timeUnits[i]) {
                return (timeDifference / timeUnits[i]) + timeLabels[i];
            }
        }
        return "Just now";
    }

    public static String createLineProgressBar(int length, ChatColor progressColor, double current, double max) {
        double percent = Math.min(current, max) / max;
        long completed = Math.round((double) length * percent);
        StringBuilder builder = new StringBuilder().append(progressColor);
        for (int i = 0; i < completed; i++)
            builder.append("-");
        builder.append(ChatColor.WHITE);
        for (int i = 0; i < length - completed; i++)
            builder.append("-");
        builder.append(" ").append(ChatColor.YELLOW).append(commaify(current)).append(ChatColor.GOLD).append("/")
                .append(ChatColor.YELLOW).append(commaify(max));
        return builder.toString();
    }

    public static String createProgressText(String text, double current, double max) {
        double percent;
        if (max != 0)
            percent = (current / max) * 100.0;
        else
            percent = 0.0;
        percent = roundTo(percent, 1);
        return ChatColor.GRAY + text + ": " + (percent < 100.0 ? ChatColor.YELLOW + commaify(percent)
                + ChatColor.GOLD + "%" : ChatColor.GREEN + "100.0%");
    }

    public static double roundTo(double d, int decimalPlaces) {
        if (decimalPlaces < 1)
            throw new IllegalArgumentException();
        StringBuilder builder = new StringBuilder()
                .append("#.");
        builder.append("#".repeat(decimalPlaces));
        DecimalFormat df = new DecimalFormat(builder.toString());
        df.setRoundingMode(RoundingMode.CEILING);
        return Double.parseDouble(df.format(d));
    }

    public static String stripColor(String s) {
        // Find any '§' character followed by a color code
        Matcher matcher = COLOR_PATTERN.matcher(s);
        return matcher.replaceAll("");
    }

    public static String shortenNumber(double number) {
        if (number < 1000) return String.valueOf((int) number);
        String[] units = new String[]{"K", "M", "B"};
        for (int i = units.length - 1; i >= 0; i--) {
            double unitValue = Math.pow(1000, i + 1);
            if (number >= unitValue) {
                return String.format("%.1f%s", number / unitValue, units[i]);
            }
        }
        return String.valueOf(number); // Fallback, should not be reached
    }

    public static String formatTimeLeft(long millis) {
        StringBuilder sb = new StringBuilder();
        long days = TimeUnit.MILLISECONDS.toDays(millis);
        long hours = TimeUnit.MILLISECONDS.toHours(millis % TimeUnit.DAYS.toMillis(1));
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis % TimeUnit.HOURS.toMillis(1));
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis % TimeUnit.MINUTES.toMillis(1));

        // Eliminated redundant checks by concatenating non-zero values directly
        if (days > 0) sb.append(days).append("d ");
        if (hours > 0) sb.append(hours).append("h ");
        if (minutes > 0) sb.append(minutes).append("m ");
        if (seconds > 0 || sb.isEmpty()) sb.append(seconds).append("s");
        return sb.toString().trim();
    }

    public static String commaify(int i) {
        return INTEGER_FORMAT.format(i);
    }

    public static String decimalify(int i) {
        return DECIMAL_FORMAT.format(i);
    }

    public static Material getMaterialFromBlock(Block block) {
        return Material.fromNamespaceId(block.namespace());
    }

    public static String profileAge(long tbf) {
        return formatTimeWentBy(System.currentTimeMillis() - tbf);
    }


    public static String getAsRomanNumeral(int num) {
        if (num == 0) return "";
        int[] values = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] symbols = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
        StringBuilder roman = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            while (num >= values[i]) {
                num -= values[i];
                roman.append(symbols[i]);
            }
        }
        return roman.toString();
    }

    public static String getTextFromComponent(Component component) {
        if (!(component instanceof TextComponent))
            throw new IllegalArgumentException("Component must be a TextComponent");
        return PlainTextComponentSerializer.plainText().serialize(component);
    }

    public static String getAuctionSetupFormattedTime(long millis) {
        return formatTimeLeft(millis).replaceAll(" ", "")
                .replaceAll("s$", "");
    }

    public static String toNormalCase(String string) {
        if (Acronym.isAcronym(string)) return string.toUpperCase();
        string = string.replaceAll("_", " ");
        String[] spl = string.split(" ");
        StringBuilder sb = new StringBuilder();

        for (String value : spl) {
            String s = value;
            if (s.isEmpty()) {
                continue;
            }
            if (s.length() == 1) {
                s = s.toUpperCase();
            } else {
                s = s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
            }
            // Append the processed string to the StringBuilder
            // Only add a space if it's not the first word
            if (!sb.isEmpty()) {
                sb.append(" ");
            }
            sb.append(s);
        }
        return sb.toString();
    }

    public static String commaify(double d) {
        return d < 1 ? "0" : INTEGER_FORMAT.format(d);
    }

    public static String decimalify(double d) {
        return d < 1 ? "0" : DECIMAL_FORMAT.format(d);
    }

    public static List<String> splitByWordAndLength(String string, int splitLength) {
        List<String> result = new ArrayList<>();
        String[] words = string.split(" ");
        StringBuilder currentString = new StringBuilder();

        for (String word : words) {
            // Check if adding the next word exceeds the split length (considering the space)
            if (currentString.length() + word.length() + (!currentString.isEmpty() ? 1 : 0) > splitLength) {
                // Add the currentString to the result and reset it
                result.add(currentString.toString());
                currentString = new StringBuilder();
            }
            // Add a space before the word if it's not the first word in the currentString
            if (!currentString.isEmpty()) {
                currentString.append(" ");
            }
            currentString.append(word);
        }

        // Add any remaining text to the result
        if (!currentString.isEmpty()) {
            result.add(currentString.toString());
        }

        return result;
    }

    public static double random(double min, double max) {
        return Math.random() * (max - min) + min;
    }

    public static String zeroed(long l) {
        return String.format("%02d", l);
    }

    public static String commaify(long l) {
        return INTEGER_FORMAT.format(l);
    }

    public static String decimalify(long l) {
        return DECIMAL_FORMAT.format(l);
    }

    public static String limitStringLength(String s, int charLimit) {
        return s.length() <= charLimit ? s : s.substring(0, charLimit);
    }

    public static String ntify(int i) {
        return switch (i % 100) {
            case 11, 12, 13 -> i + "th";
            default -> switch (i % 10) {
                case 1 -> i + "st";
                case 2 -> i + "nd";
                case 3 -> i + "rd";
                default -> i + "th";
            };
        };
    }
}
