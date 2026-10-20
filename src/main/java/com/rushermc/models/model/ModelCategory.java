package com.rushermc.models.model;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public enum ModelCategory {

    SWORDS("Swords", "חרבות", Items.DIAMOND_SWORD, 50),
    AXES("Axes", "גרזנים", Items.DIAMOND_AXE, 30),
    PICKAXES("Pickaxes", "כרייה", Items.DIAMOND_PICKAXE, 25),
    BOWS("Bows", "קשתות", Items.BOW, 25),
    HELMETS("Helmets", "קסדות", Items.LEATHER_HELMET, 40),
    CHESTPLATES("Chestplates", "שריון חזה", Items.LEATHER_CHESTPLATE, 35),
    LEGGINGS("Leggings", "מכנסי שריון", Items.LEATHER_LEGGINGS, 35),
    BOOTS("Boots", "מגפיים", Items.LEATHER_BOOTS, 35),
    SHIELDS("Shields", "מגנים", Items.SHIELD, 25),
    WINGS("Wings", "כנפיים", Items.ELYTRA, 30),
    HATS("Hats", "כובעים", Items.CARVED_PUMPKIN, 40),
    TOOLS("Tools", "כלים", Items.DIAMOND_HOE, 30),
    FOOD("Food", "אוכל", Items.COOKED_BEEF, 35),
    DECORATIONS("Decorations", "קישוטים", Items.PAPER, 45),
    PETS("Pets", "חיות מחמד", Items.PAPER, 30),
    VEHICLES("Vehicles", "רכבים", Items.OAK_BOAT, 20),
    EFFECTS("Effects", "אפקטים", Items.BLAZE_ROD, 25),
    BLOCKS("Blocks", "בלוקים", Items.PAPER, 30),
    ACCESSORIES("Accessories", "אביזרים", Items.PAPER, 35),
    SPECIAL("Special", "מיוחדים", Items.NETHER_STAR, 35);

    private final String displayName;
    private final String hebrewName;
    private final Item icon;
    private final int defaultCount;

    ModelCategory(String displayName, String hebrewName, Item icon, int defaultCount) {
        this.displayName = displayName;
        this.hebrewName = hebrewName;
        this.icon = icon;
        this.defaultCount = defaultCount;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getHebrewName() {
        return hebrewName;
    }

    public Item getIcon() {
        return icon;
    }

    public int getDefaultCount() {
        return defaultCount;
    }
}
