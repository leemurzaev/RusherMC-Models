package com.rushermc.models.manager;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rushermc.models.model.CustomModel;
import com.rushermc.models.model.ModelCategory;
import com.rushermc.models.model.ModelRarity;
import net.minecraft.item.Item;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;
import java.util.stream.Collectors;

public class ModelManager {

    private final Map<String, CustomModel> modelsById = new LinkedHashMap<>();
    private final Map<ModelCategory, List<CustomModel>> modelsByCategory = new EnumMap<>(ModelCategory.class);

    public void loadModels(File modelsFile) {
        modelsById.clear();
        modelsByCategory.clear();

        for (ModelCategory cat : ModelCategory.values()) {
            modelsByCategory.put(cat, new ArrayList<>());
        }

        try (Reader reader = new FileReader(modelsFile)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();

            for (Map.Entry<String, JsonElement> entry : root.entrySet()) {
                String id = entry.getKey();
                JsonObject obj = entry.getValue().getAsJsonObject();

                String name = obj.has("name") ? obj.get("name").getAsString() : id;
                String hebrewName = obj.has("hebrewName") ? obj.get("hebrewName").getAsString() : name;

                ModelCategory category;
                try {
                    category = ModelCategory.valueOf(obj.get("category").getAsString());
                } catch (Exception e) {
                    category = ModelCategory.SPECIAL;
                }

                Item item;
                try {
                    item = CustomModel.parseItem(obj.get("material").getAsString());
                } catch (Exception e) {
                    item = category.getIcon();
                }

                int customModelData = obj.has("customModelData") ? obj.get("customModelData").getAsInt() : 1001;

                ModelRarity rarity;
                try {
                    rarity = ModelRarity.valueOf(obj.get("rarity").getAsString());
                } catch (Exception e) {
                    rarity = ModelRarity.COMMON;
                }

                CustomModel model = new CustomModel(id, name, hebrewName, category, item, customModelData, rarity);
                modelsById.put(id, model);
                modelsByCategory.get(category).add(model);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CustomModel getModel(String id) {
        return modelsById.get(id);
    }

    public List<CustomModel> getModels(ModelCategory category) {
        return Collections.unmodifiableList(modelsByCategory.getOrDefault(category, List.of()));
    }

    public Collection<CustomModel> getAllModels() {
        return Collections.unmodifiableCollection(modelsById.values());
    }

    public int getTotalCount() {
        return modelsById.size();
    }

    public int getCategoryCount(ModelCategory category) {
        return modelsByCategory.getOrDefault(category, List.of()).size();
    }

    public List<CustomModel> search(String query) {
        String lower = query.toLowerCase();
        return modelsById.values().stream()
                .filter(m -> m.getName().toLowerCase().contains(lower)
                        || m.getHebrewName().contains(query)
                        || m.getId().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }

    public Set<String> getAllModelIds() {
        return Collections.unmodifiableSet(modelsById.keySet());
    }
}
