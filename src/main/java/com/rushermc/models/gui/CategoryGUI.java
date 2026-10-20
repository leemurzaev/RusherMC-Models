package com.rushermc.models.gui;

import com.rushermc.models.config.ModConfig;
import com.rushermc.models.manager.ModelManager;
import com.rushermc.models.model.ModelCategory;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class CategoryGUI {

    private final ModelManager modelManager;
    private final ModConfig config;

    public CategoryGUI(ModelManager modelManager, ModConfig config) {
        this.modelManager = modelManager;
        this.config = config;
    }

    public void open(ServerPlayerEntity player, ModelGUI modelGUI) {
        ModelCategory[] categories = ModelCategory.values();
        int rows = Math.max(3, Math.min(6, (categories.length / 9) + 2));

        SimpleGui gui = new SimpleGui(getScreenType(rows), player, false);
        gui.setTitle(Text.literal(config.categoryTitle));

        boolean hebrew = config.isHebrew();

        for (int i = 0; i < categories.length; i++) {
            ModelCategory category = categories[i];
            int count = modelManager.getCategoryCount(category);

            String name = hebrew ? category.getHebrewName() : category.getDisplayName();
            String countLabel = hebrew ? count + " מודלים" : count + " models";

            gui.setSlot(i, new GuiElementBuilder(category.getIcon())
                    .setName(Text.literal(name).formatted(Formatting.YELLOW, Formatting.BOLD))
                    .addLoreLine(Text.empty())
                    .addLoreLine(Text.literal(countLabel).formatted(Formatting.GRAY))
                    .addLoreLine(Text.empty())
                    .addLoreLine(Text.literal(hebrew ? "לחץ לצפייה" : "Click to browse")
                            .formatted(Formatting.GREEN))
                    .setCallback((index, type, action) -> {
                        modelGUI.open(player, category, 0, this);
                    })
            );
        }

        int infoSlot = (rows * 9) - 5;
        gui.setSlot(infoSlot, new GuiElementBuilder(Items.BOOK)
                .setName(Text.literal(hebrew ? "סה\"כ מודלים" : "Total Models")
                        .formatted(Formatting.GOLD))
                .addLoreLine(Text.literal(String.valueOf(modelManager.getTotalCount()))
                        .formatted(Formatting.WHITE))
        );

        gui.open();
    }

    private ScreenHandlerType<?> getScreenType(int rows) {
        return switch (rows) {
            case 3 -> ScreenHandlerType.GENERIC_9X3;
            case 4 -> ScreenHandlerType.GENERIC_9X4;
            case 5 -> ScreenHandlerType.GENERIC_9X5;
            default -> ScreenHandlerType.GENERIC_9X6;
        };
    }
}
