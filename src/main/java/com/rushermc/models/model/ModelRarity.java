package com.rushermc.models.model;

import net.minecraft.util.Formatting;

public enum ModelRarity {

    COMMON("Common", "נפוץ", Formatting.GRAY),
    UNCOMMON("Uncommon", "לא נפוץ", Formatting.GREEN),
    RARE("Rare", "נדיר", Formatting.AQUA),
    EPIC("Epic", "אפי", Formatting.DARK_PURPLE),
    LEGENDARY("Legendary", "אגדי", Formatting.GOLD),
    MYTHIC("Mythic", "מיתי", Formatting.RED);

    private final String displayName;
    private final String hebrewName;
    private final Formatting formatting;

    ModelRarity(String displayName, String hebrewName, Formatting formatting) {
        this.displayName = displayName;
        this.hebrewName = hebrewName;
        this.formatting = formatting;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getHebrewName() {
        return hebrewName;
    }

    public Formatting getFormatting() {
        return formatting;
    }
}
