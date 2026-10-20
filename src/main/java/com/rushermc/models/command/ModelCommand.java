package com.rushermc.models.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.rushermc.models.RusherModels;
import com.rushermc.models.model.CustomModel;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.permission.Permission;
import net.minecraft.command.permission.PermissionLevel;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class ModelCommand {

    private static final SuggestionProvider<ServerCommandSource> MODEL_SUGGESTIONS = (context, builder) -> {
        RusherModels mod = RusherModels.getInstance();
        return CommandSource.suggestMatching(mod.getModelManager().getAllModelIds(), builder);
    };

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, RusherModels mod) {
        dispatcher.register(CommandManager.literal("models")
                .executes(context -> {
                    ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
                    mod.getCategoryGUI().open(player, mod.getModelGUI());
                    return 1;
                })
                .then(CommandManager.literal("give")
                        .requires(source -> source.getPermissions().hasPermission(new Permission.Level(PermissionLevel.GAMEMASTERS)))
                        .then(CommandManager.argument("player", EntityArgumentType.player())
                                .then(CommandManager.argument("model_id", StringArgumentType.string())
                                        .suggests(MODEL_SUGGESTIONS)
                                        .executes(context -> {
                                            ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "player");
                                            String modelId = StringArgumentType.getString(context, "model_id");
                                            return giveModel(context.getSource(), target, modelId, mod);
                                        })
                                )
                        )
                )
                .then(CommandManager.literal("search")
                        .then(CommandManager.argument("query", StringArgumentType.greedyString())
                                .executes(context -> {
                                    String query = StringArgumentType.getString(context, "query");
                                    return searchModels(context.getSource(), query, mod);
                                })
                        )
                )
                .then(CommandManager.literal("reload")
                        .requires(source -> source.getPermissions().hasPermission(new Permission.Level(PermissionLevel.ADMINS)))
                        .executes(context -> {
                            mod.reload();
                            boolean hebrew = mod.getConfig().isHebrew();
                            context.getSource().sendFeedback(() -> Text.literal(
                                    hebrew ? "RusherMC Models נטען מחדש!" : "RusherMC Models reloaded!")
                                    .formatted(Formatting.GREEN), true);
                            return 1;
                        })
                )
                .then(CommandManager.literal("generate")
                        .requires(source -> source.getPermissions().hasPermission(new Permission.Level(PermissionLevel.OWNERS)))
                        .executes(context -> {
                            int count = mod.regenerateModels();
                            boolean hebrew = mod.getConfig().isHebrew();
                            context.getSource().sendFeedback(() -> Text.literal(
                                    hebrew ? "נוצרו " + count + " מודלים!" : "Generated " + count + " models!")
                                    .formatted(Formatting.GREEN), true);
                            return 1;
                        })
                )
        );

        dispatcher.register(CommandManager.literal("model")
                .redirect(dispatcher.getRoot().getChild("models")));
    }

    private static int giveModel(ServerCommandSource source, ServerPlayerEntity target,
                                 String modelId, RusherModels mod) {
        boolean hebrew = mod.getConfig().isHebrew();
        CustomModel model = mod.getModelManager().getModel(modelId);

        if (model == null) {
            source.sendError(Text.literal(hebrew
                    ? "מודל לא נמצא: " + modelId
                    : "Model not found: " + modelId));
            return 0;
        }

        target.getInventory().insertStack(model.toItemStack(hebrew));
        source.sendFeedback(() -> Text.literal(hebrew
                ? "ניתן " + model.getHebrewName() + " ל-" + target.getName().getString()
                : "Gave " + model.getName() + " to " + target.getName().getString())
                .formatted(Formatting.GREEN), false);
        return 1;
    }

    private static int searchModels(ServerCommandSource source, String query, RusherModels mod) {
        boolean hebrew = mod.getConfig().isHebrew();
        List<CustomModel> results = mod.getModelManager().search(query);

        if (results.isEmpty()) {
            source.sendFeedback(() -> Text.literal(hebrew
                    ? "לא נמצאו תוצאות עבור: " + query
                    : "No results for: " + query)
                    .formatted(Formatting.RED), false);
            return 0;
        }

        source.sendFeedback(() -> Text.literal(hebrew
                ? "--- נמצאו " + results.size() + " תוצאות ---"
                : "--- Found " + results.size() + " results ---")
                .formatted(Formatting.GOLD), false);

        int shown = Math.min(results.size(), 20);
        for (int i = 0; i < shown; i++) {
            CustomModel m = results.get(i);
            String name = hebrew ? m.getHebrewName() : m.getName();
            String cat = hebrew ? m.getCategory().getHebrewName() : m.getCategory().getDisplayName();
            source.sendFeedback(() -> Text.literal(" " + m.getId() + " ")
                    .formatted(Formatting.DARK_GRAY)
                    .append(Text.literal(name).formatted(m.getRarity().getFormatting()))
                    .append(Text.literal(" [" + cat + "]").formatted(Formatting.GRAY)), false);
        }

        if (results.size() > 20) {
            source.sendFeedback(() -> Text.literal(hebrew
                    ? "...ועוד " + (results.size() - 20) + " תוצאות"
                    : "...and " + (results.size() - 20) + " more")
                    .formatted(Formatting.GRAY), false);
        }

        return results.size();
    }
}
