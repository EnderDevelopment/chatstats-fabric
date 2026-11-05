package com.enderdevelopment.chatstats;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Map;

/**
 * Registers the {@code /chatstats} command used to display the message
 * leaderboard and some extra sub-commands.
 */
public final class LeaderboardCommand {

    private static final int MAX_LEADERBOARD_ROWS = 10;

    private LeaderboardCommand() {
    }

    /** Registers the chatstats command tree with the given dispatcher. */
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, MessageTracker tracker) {
        dispatcher.register(
                CommandManager.literal("chatstats")
                        // /chatstats — show the overall leaderboard
                        .executes(ctx -> showLeaderboard(ctx.getSource(), tracker))
                        // /chatstats <player> — show the count of one specific player
                        .then(CommandManager.argument("player", net.minecraft.command.argument.GameProfileArgumentType.gameProfile())
                                .executes(ctx -> showPlayerCount(
                                        ctx.getSource(),
                                        tracker,
                                        net.minecraft.command.argument.GameProfileArgumentType.getProfileArgument(ctx, "player")
                                                .iterator().next().getName())))
                        // /chatstats reset — clear all recorded counts
                        .then(CommandManager.literal("reset")
                                .requires(source -> source.hasPermissionLevel(2))
                                .executes(ctx -> reset(ctx.getSource(), tracker))));
    }

    private static int showLeaderboard(ServerCommandSource source, MessageTracker tracker) {
        List<Map.Entry<String, Integer>> leaderboard = tracker.sortedLeaderboard();

        source.sendFeedback(() -> Text.literal("§6====== Chat Leaderboard ======"), false);
        source.sendFeedback(() -> Text.literal("§7Total messages: §f" + tracker.totalMessages()), false);

        if (leaderboard.isEmpty()) {
            source.sendFeedback(() -> Text.literal("§7No messages recorded yet."), false);
            return 1;
        }

        int rank = 1;
        for (Map.Entry<String, Integer> entry : leaderboard) {
            if (rank > MAX_LEADERBOARD_ROWS) {
                break;
            }
            source.sendFeedback(() -> Text.literal(
                    "§e#" + rank + " §f" + entry.getKey() + " §7— §f" + entry.getValue() + " messages"),
                    false);
            rank++;
        }
        source.sendFeedback(() -> Text.literal("§7--- §e" + Math.max(0, leaderboard.size() - MAX_LEADERBOARD_ROWS)
                + "§7 more ---"), false);
        return 1;
    }

    private static int showPlayerCount(ServerCommandSource source, MessageTracker tracker, String playerName) {
        int count = tracker.snapshot().getOrDefault(playerName, 0);
        source.sendFeedback(() -> Text.literal("§e" + playerName + " §7has sent §f" + count + " §7messages."), false);
        return 1;
    }

    private static int reset(ServerCommandSource source, MessageTracker tracker) {
        tracker.reset();
        tracker.save();
        source.sendFeedback(() -> Text.literal("§aChat stats have been reset."), false);
        return 1;
    }
}
