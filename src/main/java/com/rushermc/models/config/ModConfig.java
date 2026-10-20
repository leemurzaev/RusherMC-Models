package com.rushermc.models.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class ModConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public String language = "he";
    public String prefix = "[RusherMC] ";
    public String categoryTitle = "Models - Categories";
    public String modelTitle = "Models - {category}";

    public boolean isHebrew() {
        return "he".equalsIgnoreCase(language);
    }

    public static ModConfig load(Path path) {
        if (Files.exists(path)) {
            try (Reader reader = Files.newBufferedReader(path)) {
                ModConfig config = GSON.fromJson(reader, ModConfig.class);
                return config != null ? config : new ModConfig();
            } catch (IOException e) {
                return new ModConfig();
            }
        }
        return new ModConfig();
    }

    public void save(Path path) {
        try {
            Files.createDirectories(path.getParent());
            try (Writer writer = Files.newBufferedWriter(path)) {
                GSON.toJson(this, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
