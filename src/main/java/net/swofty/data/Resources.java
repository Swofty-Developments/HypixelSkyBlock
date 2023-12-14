package net.swofty.data;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Resources {

    public static String get(String key) {
        File file = new File("./resources.json");
        if (!file.exists()) {
            return "null";
        }

        try {
            String s = new String(Files.readAllBytes(Paths.get(file.toURI())));
            JSONObject object = new JSONObject(s);
            return object.get(key).toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "null";
    }

    public static JSONObject getObject(String key) {
        File file = new File("./resources.json");
        if (!file.exists()) {
            return null;
        }

        try {
            String s = new String(Files.readAllBytes(Paths.get(file.toURI())));
            JSONObject object = new JSONObject(s);
            return (JSONObject) object.get(key);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
}