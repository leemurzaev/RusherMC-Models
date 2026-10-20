package com.rushermc.models.model;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.CustomModelDataComponent;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class CustomModel {

    public static final String NBT_MODEL_KEY = "rushermc_model_id";

    private final String id;
    private final String name;
    private final String hebrewName;
    private final ModelCategory category;
    private final Item item;
    private final int customModelData;
    private final ModelRarity rarity;

    public CustomModel(String id, String name, String hebrewName, ModelCategory category,
                       Item item, int customModelData, ModelRarity rarity) {
        this.id = id;
        this.name = name;
        this.hebrewName = hebrewName;
        this.category = category;
        this.item = item;
        this.customModelData = customModelData;
        this.rarity = rarity;
    }

    public ItemStack toItemStack(boolean hebrew) {
        ItemStack stack = new ItemStack(item);

        String displayName = hebrew ? hebrewName : name;
        Text nameText = Text.literal(displayName).formatted(rarity.getFormatting());
        if (rarity == ModelRarity.MYTHIC || rarity == ModelRarity.LEGENDARY) {
            nameText = Text.literal(displayName)
                    .formatted(rarity.getFormatting(), Formatting.BOLD);
        }
        stack.set(DataComponentTypes.CUSTOM_NAME, nameText);

        List<Text> loreLines = new ArrayList<>();
        loreLines.add(Text.empty());
        String rarityLabel = hebrew ? rarity.getHebrewName() : rarity.getDisplayName();
        loreLines.add(Text.literal("✦ " + rarityLabel).formatted(rarity.getFormatting()));
        String categoryLabel = hebrew ? category.getHebrewName() : category.getDisplayName();
        loreLines.add(Text.literal("⚑ " + categoryLabel).formatted(Formatting.DARK_GRAY));
        loreLines.add(Text.empty());
        loreLines.add(Text.literal("ID: " + id).formatted(Formatting.DARK_GRAY));
        loreLines.add(Text.literal("CMD: " + customModelData).formatted(Formatting.DARK_GRAY));
        stack.set(DataComponentTypes.LORE, new LoreComponent(loreLines));

        stack.set(DataComponentTypes.CUSTOM_MODEL_DATA,
                new CustomModelDataComponent(List.of((float) customModelData), List.of(), List.of(), List.of()));

        NbtCompound nbt = new NbtCompound();
        nbt.putString(NBT_MODEL_KEY, id);
        stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt));

        return stack;
    }

    public static Item parseItem(String materialName) {
        Identifier id = Identifier.of("minecraft", materialName.toLowerCase());
        Item item = Registries.ITEM.get(id);
        return item != null ? item : net.minecraft.item.Items.PAPER;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getHebrewName() { return hebrewName; }
    public ModelCategory getCategory() { return category; }
    public Item getItem() { return item; }
    public int getCustomModelData() { return customModelData; }
    public ModelRarity getRarity() { return rarity; }
}
