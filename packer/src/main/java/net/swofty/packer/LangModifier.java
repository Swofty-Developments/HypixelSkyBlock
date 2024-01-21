package net.swofty.packer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public enum LangModifier {
    LOADING_TERRAIN("multiplayer.downloadingTerrain", "\uE000ffffffffffffffffffffffffffffffff\uE000"),
    RECONFIGURING_1("connect.reconfiging", "\uE000ffffffffffffffffffffffffffffffff\uE000"),
    RECONFIGURING_2("connect.reconfiguring", "\uE000fffffffffffffffffffffffffff\uE000"),
    CONNECT_JOINING("connect.joining", "\uE000fffffffffffffffffffffffffff\uE000"),
    ;

    private final String key;
    private final String value;

    LangModifier(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public static void modifyLangFile(String outputDirectory) throws IOException {
        File langFile = new File(outputDirectory + "/assets/minecraft/lang/en_us.json");
        String langFileContents = new String(Files.readAllBytes(langFile.toPath()));

        for (LangModifier modifier : values()) {
            // Find the line with the key, wipe the text after the key and insert our value
            langFileContents = langFileContents.replaceAll("\"" + modifier.key + "\": \"[^\"]+\"", "\"" + modifier.key + "\": \""
                    + "\\\\u" + Integer.toHexString(modifier.value.codePointAt(0)).toUpperCase() + "\"");
        }

        Files.write(langFile.toPath(), langFileContents.getBytes());
    }
}
