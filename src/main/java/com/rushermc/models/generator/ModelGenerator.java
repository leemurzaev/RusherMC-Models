package com.rushermc.models.generator;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.rushermc.models.model.ModelCategory;
import com.rushermc.models.model.ModelRarity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

public class ModelGenerator {

    private static final String[] EN_PREFIXES = {
            "Flame", "Frost", "Shadow", "Thunder", "Void",
            "Dragon", "Phoenix", "Crystal", "Blood", "Night",
            "Storm", "Soul", "Doom", "Star", "Golden",
            "Silver", "Dark", "Light", "Chaos", "Ancient",
            "Sacred", "Cursed", "Enchanted", "Mystical", "Royal",
            "Imperial", "Divine", "Infernal", "Celestial", "Ethereal",
            "Crimson", "Azure", "Emerald", "Obsidian", "Iron",
            "Titanium", "Cobalt", "Phantom", "Spectral", "Abyssal",
            "Radiant", "Corrupted", "Blessed", "Wicked", "Primal",
            "Arcane", "Runic", "Demonic", "Angelic", "Toxic"
    };

    private static final String[] HE_PREFIXES = {
            "להבה", "קרח", "צל", "רעם", "חלל",
            "דרקון", "פניקס", "קריסטל", "דם", "לילה",
            "סערה", "נשמה", "אבדון", "כוכב", "זהב",
            "כסף", "חושך", "אור", "כאוס", "עתיק",
            "קדוש", "מקולל", "קסום", "מיסטי", "מלכותי",
            "קיסרי", "אלוהי", "גיהינום", "שמימי", "רוחני",
            "ארגמן", "תכלת", "ברקת", "אובסידיאן", "ברזל",
            "טיטניום", "קובלט", "רוח רפאים", "ספקטרלי", "תהומי",
            "זוהר", "מושחת", "מבורך", "מרושע", "קדמוני",
            "קסם", "רוני", "שדי", "מלאכי", "רעיל"
    };

    private static final Map<ModelCategory, String[]> EN_SUFFIXES = new EnumMap<>(ModelCategory.class);
    private static final Map<ModelCategory, String[]> HE_SUFFIXES = new EnumMap<>(ModelCategory.class);

    static {
        EN_SUFFIXES.put(ModelCategory.SWORDS, new String[]{
                "Blade", "Edge", "Cutter", "Slicer", "Fang", "Saber", "Sword", "Rapier", "Katana", "Scimitar"});
        HE_SUFFIXES.put(ModelCategory.SWORDS, new String[]{
                "להב", "חד", "חותך", "פורס", "ניב", "סייף", "חרב", "רפיר", "קטנה", "שמשיר"});

        EN_SUFFIXES.put(ModelCategory.AXES, new String[]{
                "Axe", "Cleaver", "Hatchet", "Splitter", "Chopper", "Tomahawk"});
        HE_SUFFIXES.put(ModelCategory.AXES, new String[]{
                "גרזן", "קופיץ", "גרזן קרב", "מפצל", "חוטב", "טומהוק"});

        EN_SUFFIXES.put(ModelCategory.PICKAXES, new String[]{
                "Pick", "Digger", "Miner", "Drill", "Crusher", "Excavator"});
        HE_SUFFIXES.put(ModelCategory.PICKAXES, new String[]{
                "מכוש", "חופר", "כורה", "מקדח", "מרסק", "חופר עפר"});

        EN_SUFFIXES.put(ModelCategory.BOWS, new String[]{
                "Bow", "Longbow", "Shortbow", "Crossbow", "Sniper", "Hunter"});
        HE_SUFFIXES.put(ModelCategory.BOWS, new String[]{
                "קשת", "קשת ארוכה", "קשת קצרה", "ארבלט", "צלף", "צייד"});

        EN_SUFFIXES.put(ModelCategory.HELMETS, new String[]{
                "Helm", "Crown", "Mask", "Visor", "Hood", "Cap", "Headband", "Tiara"});
        HE_SUFFIXES.put(ModelCategory.HELMETS, new String[]{
                "קסדה", "כתר", "מסכה", "מגן פנים", "ברדס", "כובע", "סרט ראש", "עטרה"});

        EN_SUFFIXES.put(ModelCategory.CHESTPLATES, new String[]{
                "Plate", "Vest", "Armor", "Guard", "Jacket", "Mail", "Cuirass"});
        HE_SUFFIXES.put(ModelCategory.CHESTPLATES, new String[]{
                "שריון", "אפוד", "מגן חזה", "שומר", "מעיל", "שריון קשקשים", "שריון חזה"});

        EN_SUFFIXES.put(ModelCategory.LEGGINGS, new String[]{
                "Greaves", "Guards", "Leggings", "Pants", "Tassets", "Cuisses", "Legplates"});
        HE_SUFFIXES.put(ModelCategory.LEGGINGS, new String[]{
                "מגני שוק", "מגני רגל", "מכנסיים", "שריון ירך", "מגני ברך", "שוקיות", "לוחות רגל"});

        EN_SUFFIXES.put(ModelCategory.BOOTS, new String[]{
                "Boots", "Shoes", "Treads", "Stompers", "Sandals", "Sabatons", "Walkers"});
        HE_SUFFIXES.put(ModelCategory.BOOTS, new String[]{
                "מגפיים", "נעליים", "סוליות", "רומסים", "סנדלים", "שריון רגל", "הולכים"});

        EN_SUFFIXES.put(ModelCategory.SHIELDS, new String[]{
                "Shield", "Buckler", "Barrier", "Ward", "Aegis", "Bulwark"});
        HE_SUFFIXES.put(ModelCategory.SHIELDS, new String[]{
                "מגן", "מגן קטן", "מחסום", "משמר", "אגיס", "חומה"});

        EN_SUFFIXES.put(ModelCategory.WINGS, new String[]{
                "Wings", "Glider", "Flight", "Soar", "Feathers", "Drape"});
        HE_SUFFIXES.put(ModelCategory.WINGS, new String[]{
                "כנפיים", "דאון", "מעוף", "מרחף", "נוצות", "גלימה"});

        EN_SUFFIXES.put(ModelCategory.HATS, new String[]{
                "Hat", "Cap", "Crown", "Hood", "Mask", "Beret", "Turban", "Helm"});
        HE_SUFFIXES.put(ModelCategory.HATS, new String[]{
                "כובע", "מצנפת", "כתר", "ברדס", "מסכה", "כומתה", "מצנפת ראש", "קסדה"});

        EN_SUFFIXES.put(ModelCategory.TOOLS, new String[]{
                "Shovel", "Hoe", "Shears", "Wrench", "Hammer", "Saw"});
        HE_SUFFIXES.put(ModelCategory.TOOLS, new String[]{
                "את", "מעדר", "מזמרה", "מפתח", "פטיש", "מסור"});

        EN_SUFFIXES.put(ModelCategory.FOOD, new String[]{
                "Feast", "Bite", "Meal", "Snack", "Treat", "Brew", "Elixir"});
        HE_SUFFIXES.put(ModelCategory.FOOD, new String[]{
                "משתה", "נשיכה", "ארוחה", "חטיף", "פינוק", "שיקוי", "אליקסיר"});

        EN_SUFFIXES.put(ModelCategory.DECORATIONS, new String[]{
                "Ornament", "Statue", "Trophy", "Relic", "Totem", "Banner", "Crystal", "Lantern", "Shrine"});
        HE_SUFFIXES.put(ModelCategory.DECORATIONS, new String[]{
                "קישוט", "פסל", "גביע", "שריד", "טוטם", "דגל", "קריסטל", "פנס", "מקדש"});

        EN_SUFFIXES.put(ModelCategory.PETS, new String[]{
                "Companion", "Familiar", "Spirit", "Guardian", "Minion", "Creature"});
        HE_SUFFIXES.put(ModelCategory.PETS, new String[]{
                "נלווה", "שותף", "רוח", "שומר", "משרת", "יצור"});

        EN_SUFFIXES.put(ModelCategory.VEHICLES, new String[]{
                "Mount", "Steed", "Ride", "Chariot", "Racer", "Cruiser"});
        HE_SUFFIXES.put(ModelCategory.VEHICLES, new String[]{
                "רכב", "סוס", "רכיבה", "מרכבה", "מירוץ", "שייט"});

        EN_SUFFIXES.put(ModelCategory.EFFECTS, new String[]{
                "Aura", "Trail", "Burst", "Wave", "Pulse", "Glow"});
        HE_SUFFIXES.put(ModelCategory.EFFECTS, new String[]{
                "הילה", "שובל", "פרץ", "גל", "פעימה", "זוהר"});

        EN_SUFFIXES.put(ModelCategory.BLOCKS, new String[]{
                "Block", "Slab", "Pillar", "Wall", "Tile", "Panel"});
        HE_SUFFIXES.put(ModelCategory.BLOCKS, new String[]{
                "בלוק", "לוח", "עמוד", "קיר", "אריח", "פנל"});

        EN_SUFFIXES.put(ModelCategory.ACCESSORIES, new String[]{
                "Ring", "Pendant", "Bracelet", "Chain", "Amulet", "Charm", "Brooch"});
        HE_SUFFIXES.put(ModelCategory.ACCESSORIES, new String[]{
                "טבעת", "תליון", "צמיד", "שרשרת", "קמע", "קסם", "סיכה"});

        EN_SUFFIXES.put(ModelCategory.SPECIAL, new String[]{
                "Artifact", "Relic", "Treasure", "Legacy", "Omega", "Ultimate", "Prime"});
        HE_SUFFIXES.put(ModelCategory.SPECIAL, new String[]{
                "ארטיפקט", "שריד", "אוצר", "מורשת", "אומגה", "אולטימטיבי", "פריים"});
    }

    public int generate(File modelsFile) throws IOException {
        JsonObject root = new JsonObject();
        int cmdValue = 1001;
        int totalGenerated = 0;

        for (ModelCategory category : ModelCategory.values()) {
            int count = category.getDefaultCount();
            List<String[]> names = generateNames(category, count);

            for (int i = 0; i < names.size(); i++) {
                String[] namePair = names.get(i);
                String id = category.name().toLowerCase() + "_" + (i + 1);

                JsonObject model = new JsonObject();
                model.addProperty("name", namePair[0]);
                model.addProperty("hebrewName", namePair[1]);
                model.addProperty("category", category.name());
                model.addProperty("material", category.getIcon().toString().toUpperCase());
                model.addProperty("customModelData", cmdValue);
                model.addProperty("rarity", pickRarity(cmdValue).name());

                root.add(id, model);
                cmdValue++;
                totalGenerated++;
            }
        }

        try (Writer writer = new FileWriter(modelsFile)) {
            new GsonBuilder().setPrettyPrinting().create().toJson(root, writer);
        }

        return totalGenerated;
    }

    private List<String[]> generateNames(ModelCategory category, int count) {
        String[] enSuffix = EN_SUFFIXES.get(category);
        String[] heSuffix = HE_SUFFIXES.get(category);

        List<String[]> allCombinations = new ArrayList<>();
        for (int p = 0; p < EN_PREFIXES.length; p++) {
            for (int s = 0; s < enSuffix.length; s++) {
                allCombinations.add(new String[]{
                        EN_PREFIXES[p] + " " + enSuffix[s],
                        HE_PREFIXES[p] + " " + heSuffix[s]
                });
            }
        }

        Collections.shuffle(allCombinations, new Random(category.name().hashCode()));
        return allCombinations.subList(0, Math.min(count, allCombinations.size()));
    }

    private ModelRarity pickRarity(int seed) {
        int roll = Math.abs(seed * 31 + 17) % 100;
        if (roll < 3) return ModelRarity.MYTHIC;
        if (roll < 10) return ModelRarity.LEGENDARY;
        if (roll < 22) return ModelRarity.EPIC;
        if (roll < 40) return ModelRarity.RARE;
        if (roll < 65) return ModelRarity.UNCOMMON;
        return ModelRarity.COMMON;
    }
}
