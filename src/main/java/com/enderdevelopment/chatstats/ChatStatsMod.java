package com.enderdevelopment.chatstats;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main entry point for the Chat Stats mod.
 *
 * <p>Runs on the dedicated server and the integrated (single player) server.
 * It listens for chat messages, counts them per player and exposes a command
 * that shows a leaderboard of the most active chat participants.</p>
 */
public class ChatStatsMod implements ModInitializer {

    public static final String MOD_ID = "chatstats";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private final MessageTracker tracker = new MessageTracker();

    @Override
    public void onInitialize() {
        tracker.load();

        // Register the chat message listener so every chat message is counted.
        ServerMessageEvents.CHAT_MESSAGE.register((player, message, signedMessage) -> {
            tracker.recordMessage(player.getGameProfile().getName());
        });

        // Register the /chatstats command(s).
        CommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess, environment) -> LeaderboardCommand.register(dispatcher, tracker));

        LOGGER.info("Chat Stats enabled. Tracking chat messages per player.");
    }
}
