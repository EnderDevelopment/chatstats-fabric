package com.enderdevelopment.chatstats;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Tracks how many chat messages each player has sent and persists the data to a
 * JSON file inside the config directory so the counts survive a server restart.
 */
public class MessageTracker {

    /** Types for reading/writing the persisted map. */
    private static final Type MAP_TYPE = new TypeToken<LinkedHashMap<String, Integer>>() {}.getType();

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Map<String, Integer> counts = new LinkedHashMap<>();

    private Path getDataFile() {
        return FabricLoader.getInstance().getConfigDir().resolve("chatstats_messages.json");
    }

    /** Loads the persisted message counts from disk, if present. */
    public void load() {
        Path file = getDataFile();
        if (!Files.exists(file)) {
            return;
        }
        try (Reader reader = Files.newBufferedReader(file)) {
            Map<String, Integer> saved = gson.fromJson(reader, MAP_TYPE);
            if (saved != null) {
                counts.clear();
                counts.putAll(saved);
            }
        } catch (IOException e) {
            ChatStatsMod.LOGGER.warn("Could not load chat stats data: {}", e.getMessage());
        }
    }

    /** Saves the current message counts to disk. */
    public void save() {
        Path file = getDataFile();
        try {
            Files.createDirectories(file.getParent());
        } catch (IOException ignored) {
            // Directory likely already exists.
        }
        try (Writer writer = Files.newBufferedWriter(file)) {
            gson.toJson(snapshot(), writer);
        } catch (IOException e) {
            ChatStatsMod.LOGGER.warn("Could not save chat stats data: {}", e.getMessage());
        }
    }

    /** Records one chat message for the given player name. */
    public void recordMessage(String playerName) {
        counts.merge(playerName, 1, Integer::sum);
    }

    /** Clears all recorded message counts. */
    public void reset() {
        counts.clear();
    }

    /** Returns a snapshot of the current counts; callers may modify the returned map freely. */
    public Map<String, Integer> snapshot() {
        return new LinkedHashMap<>(counts);
    }

    /** Returns the total number of recorded messages across all players. */
    public int totalMessages() {
        return counts.values().stream().mapToInt(Integer::intValue).sum();
    }

    /** Returns the message counts sorted by count descending (ties broken by name). */
    public List<Map.Entry<String, Integer>> sortedLeaderboard() {
        return counts.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed()
                        .thenComparing(Map.Entry.comparingByKey()))
                .toList();
    }
}
