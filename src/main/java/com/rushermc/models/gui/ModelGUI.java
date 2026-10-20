package com.rushermc.models.gui;

import com.rushermc.models.config.ModConfig;
import com.rushermc.models.manager.ModelManager;
import com.rushermc.models.model.CustomModel;
import com.rushermc.models.model.ModelCategory;
import eu.pb4.sgui.api.elements.GuiElement;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class ModelGUI {

    private static final int ITEMS_PER_PAGE = 45;

    private final ModelManager modelManager;
    private final ModConfig config;

    public ModelGUI(ModelManager modelManager, ModConfig config) {
        this.modelManager = modelManager;
        this.config = config;
    }

    public void open(ServerPlayerEntity player, ModelCategory category, int page, CategoryGUI categoryGUI) {
        List<CustomModel> models = modelManager.getModels(category);
        int totalPages = Math.max(1, (int) Math.ceil((double) models.size() / ITEMS_PER_PAGE));
        page = Math.max(0, Math.min(page, totalPages - 1));
        final int currentPage = page;

        boolean hebrew = config.isHebrew();
        String categoryName = hebrew ? category.getHebrewName() : category.getDisplayName();
        String title = config.modelTitle.replace("{category}", categoryName);

        SimpleGui gui = new SimpleGui(ScreenHandlerType.GENERIC_9X6, player, false);
        gui.setTitle(Text.literal(title));

        int start = page * ITEMS_PER_PAGE;
        int end = Math.min(start + ITEMS_PER_PAGE, models.size());

        for (int i = start; i < end; i++) {
            CustomModel model = models.get(i);
            gui.setSlot(i - start, new GuiElement(
                    model.toItemStack(hebrew),
                    (index, type, action) -> {
                        player.getInventory().insertStack(model.toItemStack(hebrew));
                        player.sendMessage(Text.literal(hebrew
                                ? "קיבלת: " + model.getHebrewName()
                                : "Received: " + model.getName())
                                .formatted(Formatting.GREEN), false);
                    }
            ));
        }

        if (page > 0) {
            gui.setSlot(45, new GuiElementBuilder(Items.ARROW)
                    .setName(Text.literal(hebrew ? "◄ עמוד קודם" : "◄ Previous Page")
                            .formatted(Formatting.GREEN))
                    .setCallback((index, type, action) -> {
                        open(player, category, currentPage - 1, categoryGUI);
                    })
            );
        }

        gui.setSlot(49, new GuiElementBuilder(Items.PAPER)
                .setName(Text.literal((currentPage + 1) + " / " + totalPages)
                        .formatted(Formatting.YELLOW))
        );

        if (page < totalPages - 1) {
            gui.setSlot(53, new GuiElementBuilder(Items.ARROW)
                    .setName(Text.literal(hebrew ? "עמוד הבא ►" : "Next Page ►")
                            .formatted(Formatting.GREEN))
                    .setCallback((index, type, action) -> {
                        open(player, category, currentPage + 1, categoryGUI);
                    })
            );
        }

        gui.setSlot(48, new GuiElementBuilder(Items.COMPASS)
                .setName(Text.literal(hebrew ? "◄ חזרה לקטגוריות" : "◄ Back to Categories")
                        .formatted(Formatting.RED))
                .setCallback((index, type, action) -> {
                    categoryGUI.open(player, this);
                })
        );

        gui.open();
    }
}
