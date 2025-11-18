package net.swofty.commons;

import org.json.JSONObject;
import org.tinylog.Logger;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Configuration {

    public static String get(String key) {
        File file = new File("./configuration/resources.json");
        if (!file.exists()) {
            System.out.println("File does not exist, returning null for key " + key + " in Configuration.get()");
            return "null";
        }

        try {
            String s = new String(Files.readAllBytes(Paths.get(file.toURI())));
            JSONObject object = new JSONObject(s);
            return object.get(key).toString();
        } catch (Exception ex) {
            Logger.error(ex, "Failed to read configuration key: {}", key);
        }

        return "null";
    }

    public static <T> T getOrDefault(String key, T def) {
        File file = new File("./configuration/resources.json");
        if (!file.exists()) {
            return def;
        }

        try {
            String s = new String(Files.readAllBytes(Paths.get(file.toURI())));
            JSONObject object = new JSONObject(s);
            return (T) object.get(key);
        } catch (Exception ex) {}

        return def;
    }

    public static JSONObject getObject(String key) {
        File file = new File("./configuration/resources.json");
        if (!file.exists()) {
            return null;
        }

        try {
            String s = new String(Files.readAllBytes(Paths.get(file.toURI())));
            JSONObject object = new JSONObject(s);
            return (JSONObject) object.get(key);
        } catch (Exception ex) {
            Logger.error(ex, "Failed to read configuration object for key: {}", key);
        }

        return null;
    }

}