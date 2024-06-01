package org.qo.komeiji;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Configuration {
    public static final String config_file = "/Komeji/settings.json";
    public static Gson gson = new Gson();
    public static JsonObject cfgObject = null;
    public static String get(String key){
        if (cfgObject.has(key)) return cfgObject.get(key).getAsString();
        return null;
    }
    public static void init() throws IOException {
        if (Files.isRegularFile(Path.of(config_file))){
           cfgObject = (JsonObject) JsonParser.parseString(Files.readString(Path.of(config_file)));
        }
    }
}
