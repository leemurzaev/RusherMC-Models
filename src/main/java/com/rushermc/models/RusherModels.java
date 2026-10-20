package com.rushermc.models;

import com.rushermc.models.command.ModelCommand;
import com.rushermc.models.config.ModConfig;
import com.rushermc.models.generator.ModelGenerator;
import com.rushermc.models.gui.CategoryGUI;
import com.rushermc.models.gui.ModelGUI;
import com.rushermc.models.manager.ModelManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class RusherModels implements ModInitializer {

    public static final String MOD_ID = "rushermc-models";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static RusherModels instance;

    private Path configDir;
    private ModConfig config;
    private ModelManager modelManager;
    private CategoryGUI categoryGUI;
    private ModelGUI modelGUI;

    @Override
    public void onInitialize() {
        instance = this;

        configDir = FabricLoader.getInstance().getConfigDir().resolve("rushermc-models");
        configDir.toFile().mkdirs();

        config = ModConfig.load(configDir.resolve("config.json"));
        config.save(configDir.resolve("config.json"));

        modelManager = new ModelManager();

        File modelsFile = configDir.resolve("models.json").toFile();
        if (!modelsFile.exists()) {
            generateDefaultModels(modelsFile);
        }

        modelManager.loadModels(modelsFile);
        LOGGER.info("Loaded {} models across {} categories",
                modelManager.getTotalCount(), com.rushermc.models.model.ModelCategory.values().length);

        categoryGUI = new CategoryGUI(modelManager, config);
        modelGUI = new ModelGUI(modelManager, config);

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                ModelCommand.register(dispatcher, this));

        LOGGER.info("RusherMC Models v{} loaded!", FabricLoader.getInstance()
                .getModContainer(MOD_ID).map(c -> c.getMetadata().getVersion().getFriendlyString())
                .orElse("?"));
    }

    private void generateDefaultModels(File modelsFile) {
        try {
            ModelGenerator generator = new ModelGenerator();
            int count = generator.generate(modelsFile);
            LOGGER.info("Generated {} default models", count);
        } catch (IOException e) {
            LOGGER.error("Failed to generate default models", e);
        }
    }

    public int regenerateModels() {
        File modelsFile = configDir.resolve("models.json").toFile();
        try {
            ModelGenerator generator = new ModelGenerator();
            int count = generator.generate(modelsFile);
            modelManager.loadModels(modelsFile);
            categoryGUI = new CategoryGUI(modelManager, config);
            modelGUI = new ModelGUI(modelManager, config);
            return count;
        } catch (IOException e) {
            LOGGER.error("Failed to regenerate models", e);
            return 0;
        }
    }

    public void reload() {
        config = ModConfig.load(configDir.resolve("config.json"));
        File modelsFile = configDir.resolve("models.json").toFile();
        modelManager.loadModels(modelsFile);
        categoryGUI = new CategoryGUI(modelManager, config);
        modelGUI = new ModelGUI(modelManager, config);
        LOGGER.info("Reloaded {} models", modelManager.getTotalCount());
    }

    public static RusherModels getInstance() {
        return instance;
    }

    public ModConfig getConfig() {
        return config;
    }

    public ModelManager getModelManager() {
        return modelManager;
    }

    public CategoryGUI getCategoryGUI() {
        return categoryGUI;
    }

    public ModelGUI getModelGUI() {
        return modelGUI;
    }
}
