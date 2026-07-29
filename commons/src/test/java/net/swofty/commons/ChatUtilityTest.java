package net.swofty.commons;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChatUtilityTest {

    @Test
    void stripTokensRemovesColorCodes() {
        assertEquals("Hello", ChatUtility.FontInfo.stripTokens("§aHello"));
        assertEquals("Plain", ChatUtility.FontInfo.stripTokens("Plain"));
        assertEquals("Mixed text", ChatUtility.FontInfo.stripTokens("§cMixed §btext"));
        assertEquals("", ChatUtility.FontInfo.stripTokens("§a§b§c"));
    }

    @Test
    void getDefaultFontInfoFindsKnownChars() {
        assertEquals(ChatUtility.FontInfo.A, ChatUtility.FontInfo.getDefaultFontInfo('A'));
        assertEquals(ChatUtility.FontInfo.i, ChatUtility.FontInfo.getDefaultFontInfo('i'));
        assertEquals(ChatUtility.FontInfo.SPACE, ChatUtility.FontInfo.getDefaultFontInfo(' '));
        assertEquals(ChatUtility.FontInfo.NUM_5, ChatUtility.FontInfo.getDefaultFontInfo('5'));
    }

    @Test
    void getDefaultFontInfoFallsBackForUnknown() {
        // Characters that aren't in the table return DEFAULT
        assertEquals(ChatUtility.FontInfo.DEFAULT, ChatUtility.FontInfo.getDefaultFontInfo('Ω'));
        assertEquals(ChatUtility.FontInfo.DEFAULT, ChatUtility.FontInfo.getDefaultFontInfo('€'));
    }

    @Test
    void getLengthEmptyStringIsZero() {
        assertEquals(0, ChatUtility.FontInfo.getLength(""));
    }

    @Test
    void getLengthIgnoresColorCodes() {
        // Color codes don't contribute width
        int plain = ChatUtility.FontInfo.getLength("Hello");
        int colored = ChatUtility.FontInfo.getLength("§aHello");
        int doubleColored = ChatUtility.FontInfo.getLength("§c§oHello");
        assertEquals(plain, colored);
        assertEquals(plain, doubleColored);
    }

    @Test
    void getLengthCountsEachCharacter() {
        // Each letter adds its declared length + 1 spacing pixel.
        // 'H','e','l','l','o' -> 5+1 + 5+1 + 1+1 + 1+1 + 5+1 = 22
        assertEquals(22, ChatUtility.FontInfo.getLength("Hello"));
    }

    @Test
    void centerLinesPadsEachLine() {
        List<String> input = List.of("Short", "A longer line");
        List<String> output = ChatUtility.FontInfo.centerLines(input);
        assertEquals(input.size(), output.size());
        // Each output line ends with the input line
        for (int i = 0; i < input.size(); i++) {
            assertTrue(output.get(i).endsWith(input.get(i)),
                    "Output line should end with input: " + output.get(i));
        }
    }

    @Test
    void centerEmptyStringYieldsEmptyPad() {
        // Implementation returns "" for null or empty
        assertEquals("", ChatUtility.FontInfo.getCenterSpaces(""));
        assertEquals("", ChatUtility.FontInfo.getCenterSpaces(null));
    }

    @Test
    void fontInfoCharacterAndLengthMatch() {
        // Spot-check a few entries to lock down the data
        assertEquals('A', ChatUtility.FontInfo.A.getCharacter());
        assertEquals(5, ChatUtility.FontInfo.A.getLength());
        assertEquals(1, ChatUtility.FontInfo.i.getLength());
        assertEquals(3, ChatUtility.FontInfo.SPACE.getLength());
    }
}
