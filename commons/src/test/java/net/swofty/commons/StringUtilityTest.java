package net.swofty.commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class StringUtilityTest {

    @Test
    void romanNumeralZeroIsEmpty() {
        assertEquals("", StringUtility.getAsRomanNumeral(0));
    }

    @Test
    void romanNumeralSingleDigits() {
        assertEquals("I", StringUtility.getAsRomanNumeral(1));
        assertEquals("IV", StringUtility.getAsRomanNumeral(4));
        assertEquals("V", StringUtility.getAsRomanNumeral(5));
        assertEquals("IX", StringUtility.getAsRomanNumeral(9));
    }

    @Test
    void romanNumeralCompound() {
        assertEquals("XLII", StringUtility.getAsRomanNumeral(42));
        assertEquals("MCMXCIX", StringUtility.getAsRomanNumeral(1999));
        assertEquals("MMXXVI", StringUtility.getAsRomanNumeral(2026));
    }

    @Test
    void capitalizeNormalCase() {
        assertEquals("Hello", StringUtility.capitalize("hello"));
        assertEquals("World", StringUtility.capitalize("WORLD"));
    }

    @Test
    void capitalizeEdgeCases() {
        assertEquals("", StringUtility.capitalize(""));
        assertNull(StringUtility.capitalize(null));
        assertEquals("A", StringUtility.capitalize("a"));
        assertEquals("A", StringUtility.capitalize("A"));
    }

    @Test
    void shortenNumberSmallValues() {
        assertEquals("0", StringUtility.shortenNumber(0));
        assertEquals("999", StringUtility.shortenNumber(999));
    }

    @Test
    void shortenNumberThousands() {
        assertEquals("1.0K", StringUtility.shortenNumber(1000));
        assertEquals("1.5K", StringUtility.shortenNumber(1500));
        assertEquals("999.9K", StringUtility.shortenNumber(999_900));
    }

    @Test
    void shortenNumberMillionsAndBillions() {
        assertEquals("1.0M", StringUtility.shortenNumber(1_000_000));
        assertEquals("2.5M", StringUtility.shortenNumber(2_500_000));
        assertEquals("1.0B", StringUtility.shortenNumber(1_000_000_000));
    }

    @Test
    void commaifyAddsThousandsSeparators() {
        assertEquals("1,234", StringUtility.commaify(1234));
        assertEquals("1,000,000", StringUtility.commaify(1_000_000));
        assertEquals("0", StringUtility.commaify(0));
    }

    @Test
    void roundToPrecision() {
        assertEquals(3.14, StringUtility.roundTo(3.14159, 2), 1e-9);
        assertEquals(3.142, StringUtility.roundTo(3.14159, 3), 1e-9);
        assertEquals(3.0, StringUtility.roundTo(3.14159, 0), 1e-9);
    }

    @Test
    void formatTimeLeftBoundaries() {
        assertEquals("0s", StringUtility.formatTimeLeft(0));
        assertEquals("59s", StringUtility.formatTimeLeft(59_000));
        assertEquals("1m", StringUtility.formatTimeLeft(60_000));
        assertEquals("1m 30s", StringUtility.formatTimeLeft(90_000));
        assertEquals("1h", StringUtility.formatTimeLeft(3_600_000));
        assertEquals("1d", StringUtility.formatTimeLeft(86_400_000));
    }

    @Test
    void stripColorRemovesSectionCodes() {
        assertEquals("Hello", StringUtility.stripColor("§aHello"));
        assertEquals("Plain", StringUtility.stripColor("Plain"));
        assertEquals("Mixed text", StringUtility.stripColor("§cMixed §btext"));
    }
}
