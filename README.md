# RusherMC Models

A Fabric mod for Minecraft 1.21.11 that adds a browsable, bilingual (Hebrew/English) catalogue of 655 custom-model items, sorted into 20 categories and 6 rarities, with a chest-menu GUI and a `/models` command.

![Minecraft](https://img.shields.io/badge/Minecraft-1.21.11-green)
![Fabric](https://img.shields.io/badge/loader-Fabric-dbd0b4)
![Java](https://img.shields.io/badge/Java-21-orange)

## Features

- **Generated catalogue.** On first start, the mod generates 655 models across 20 categories and writes them to `config/rushermc-models/models.json`. You can edit that file or replace it with your own catalogue.
- **Repeatable generation.** Names combine 50 themed prefixes (Flame, Frost, Void, Dragon and so on) with suffixes that fit each category. They are shuffled with a fixed seed for each category, so regenerating gives the same catalogue. Each model gets a unique CustomModelData value, counting up from 1001.
- **Six rarities.** Common, Uncommon, Rare, Epic, Legendary and Mythic, each with its own colour. Legendary and Mythic names are bold.
- **Category menu.** A chest GUI with an icon and a model count for every category, plus a total-models counter.
- **Model menu.** 45 models per page, with previous/next arrows, a page indicator and a back button. Clicking a model puts a copy in your inventory.
- **Ready-to-use items.** Each item has a custom name, lore (rarity, category, model ID, CustomModelData), the `minecraft:custom_model_data` component, and a `rushermc_model_id` tag in its custom data, so other mods or datapacks can identify it.
- **Search.** Look models up by English name, Hebrew name or ID. Up to 20 results are shown in chat.
- **Admin tools.** Give any model to a player (model IDs tab-complete), reload the config and catalogue without a restart, or regenerate the default catalogue.
- **Two languages.** Hebrew (default) or English for menus, item names, lore and command messages.
- **Server-side GUI.** The menus use [SGUI](https://github.com/Patbox/sgui), and the mod adds no new items or blocks, so players can join with an unmodded client.

> The mod hands out vanilla items tagged with CustomModelData. The actual models and textures come from a resource pack that maps those values, which is **not** included in this repository. Without a pack, items look like their vanilla base item.

## Requirements

| | |
|---|---|
| Minecraft | 1.21.11 |
| Mod loader | [Fabric Loader](https://fabricmc.net/) 0.19.0 or newer |
| Java | 21 or newer |
| Dependencies | [Fabric API](https://modrinth.com/mod/fabric-api) (built against `0.141.6+1.21.11`). SGUI `1.12.0+1.21.11` is bundled inside the jar, so you do not need to install it. |

## Installation

1. Install Fabric Loader and Fabric API on your server (or in a singleplayer instance).
2. Build the jar (see [Building](#building)) or take it from a release, and put it in the `mods/` folder.
3. Start the game or server. The mod creates `config/rushermc-models/config.json` and generates `config/rushermc-models/models.json`.
4. Run `/models` in game.

## Commands and Permissions

The mod has no permission nodes. Access uses the vanilla operator permission levels.

| Command | Description | Required level |
|---|---|---|
| `/models` | Opens the category menu (players only). | none |
| `/models search <query>` | Searches models by English name, Hebrew name or ID. | none |
| `/models give <player> <model_id>` | Gives a model item to a player. | 2 (gamemaster) |
| `/models reload` | Reloads `config.json` and `models.json`. | 3 (admin) |
| `/models generate` | Regenerates the default catalogue, **overwriting** `models.json`, and loads it. | 4 (owner) |

`/model <subcommand>` is an alias, for example `/model search dragon`.

## Configuration

### `config/rushermc-models/config.json`

```json
{
  "language": "he",
  "prefix": "[RusherMC] ",
  "categoryTitle": "Models - Categories",
  "modelTitle": "Models - {category}"
}
```

| Key | Default | Description |
|---|---|---|
| `language` | `"he"` | `"he"` for Hebrew. Any other value switches to English. |
| `prefix` | `"[RusherMC] "` | Reserved. The current version does not use it. |
| `categoryTitle` | `"Models - Categories"` | Title of the category menu. |
| `modelTitle` | `"Models - {category}"` | Title of the model menu. `{category}` is replaced with the category name. |

The file is saved again on every start, so any keys that are missing get added.

### `config/rushermc-models/models.json`

The catalogue is a JSON object keyed by model ID. Each entry has this shape:

```json
{
  "swords_1": {
    "name": "Dragon Fang",
    "hebrewName": "דרקון ניב",
    "category": "SWORDS",
    "material": "DIAMOND_SWORD",
    "customModelData": 1001,
    "rarity": "UNCOMMON"
  }
}
```

| Field | Description |
|---|---|
| `name` | English display name. Defaults to the model ID. |
| `hebrewName` | Hebrew display name. Defaults to `name`. |
| `category` | One of the category keys below. Unknown values fall back to `SPECIAL`. |
| `material` | Base vanilla item ID in the `minecraft` namespace, for example `DIAMOND_SWORD`. If it cannot be parsed, the category's icon item is used. |
| `customModelData` | Value written to the item's `custom_model_data` component. Defaults to `1001`. |
| `rarity` | `COMMON`, `UNCOMMON`, `RARE`, `EPIC`, `LEGENDARY` or `MYTHIC`. Defaults to `COMMON`. |

Run `/models reload` after editing.

### Categories

| Key | Name (EN / HE) | Default count |
|---|---|---|
| `SWORDS` | Swords / חרבות | 50 |
| `AXES` | Axes / גרזנים | 30 |
| `PICKAXES` | Pickaxes / כרייה | 25 |
| `BOWS` | Bows / קשתות | 25 |
| `HELMETS` | Helmets / קסדות | 40 |
| `CHESTPLATES` | Chestplates / שריון חזה | 35 |
| `LEGGINGS` | Leggings / מכנסי שריון | 35 |
| `BOOTS` | Boots / מגפיים | 35 |
| `SHIELDS` | Shields / מגנים | 25 |
| `WINGS` | Wings / כנפיים | 30 |
| `HATS` | Hats / כובעים | 40 |
| `TOOLS` | Tools / כלים | 30 |
| `FOOD` | Food / אוכל | 35 |
| `DECORATIONS` | Decorations / קישוטים | 45 |
| `PETS` | Pets / חיות מחמד | 30 |
| `VEHICLES` | Vehicles / רכבים | 20 |
| `EFFECTS` | Effects / אפקטים | 25 |
| `BLOCKS` | Blocks / בלוקים | 30 |
| `ACCESSORIES` | Accessories / אביזרים | 35 |
| `SPECIAL` | Special / מיוחדים | 35 |

### Rarities

Each model's rarity comes from its CustomModelData value. That gives this split in the generated catalogue:

| Rarity | Colour | Models in default catalogue |
|---|---|---|
| Common | Gray | 230 |
| Uncommon | Green | 164 |
| Rare | Aqua | 118 |
| Epic | Dark purple | 78 |
| Legendary | Gold (bold) | 46 |
| Mythic | Red (bold) | 19 |

## Building

You need JDK 21.

`gradle.properties` sets `org.gradle.java.home` to a local JDK path. Remove or adjust `org.gradle.java.home` in `gradle.properties` to your own JDK 21 path before building.

```bash
./gradlew build        # Windows: gradlew.bat build
```

The mod jar is written to `build/libs/RusherMC-Models-1.0.0.jar`.

The build uses Fabric Loom 1.13.6 with Yarn mappings `1.21.11+build.6`, and the Gradle wrapper is pinned to Gradle 8.14. To start a development server with the mod loaded, run `./gradlew runServer`.

## Project structure

```
src/main/java/com/rushermc/models/
  RusherModels.java              Mod entry point: config, catalogue loading, command registration
  command/ModelCommand.java      /models and /model commands
  config/ModConfig.java          config.json (Gson)
  generator/ModelGenerator.java  Generates the default 655-model catalogue
  gui/CategoryGUI.java           Category menu (SGUI)
  gui/ModelGUI.java              Paginated model menu (SGUI)
  manager/ModelManager.java      Loads models.json, lookup and search
  model/CustomModel.java         Model definition and ItemStack builder
  model/ModelCategory.java       The 20 categories
  model/ModelRarity.java         The 6 rarities
src/main/resources/
  fabric.mod.json
```

## About

Made by [ParkDawg](https://github.com/leemurzaev). Developed for the RusherMC server.
