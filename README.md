# ChatStats Fabric

A **Fabric** mod for **Minecraft 26.1.2** (Fabric Loader 0.19.5) that records every chat message and shows a leaderboard of who wrote the most messages, plus how many messages each player has sent.

The mod runs server-side (it also works in single player through the integrated server), so the counts are shared by every connected player and persist across restarts.

## Install

1. Install the Fabric Loader for Minecraft 26.1.2 (https://fabricmc.net/use/installer/).
2. Install the **Fabric API** mod for Minecraft 26.1.2 (https://modrinth.com/mod/fabric-api or https://www.curseforge.com/minecraft/mc-mods/fabric-api).
3. Put the built `chatstats-1.0.0.jar` into your `mods` folder (server and/or client).
4. Start (or restart) the game/server.

## Use

- Type `/chatstats` in chat to see the top 10 most active chat participants and the total message count.
- Type `/chatstats <player>` to see how many messages a specific player has sent.
- Type `/chatstats reset` (requires operator permission level 2) to clear all recorded counts.
- Data is saved automatically to `config/chatstats_messages.json` and loaded on startup.

## Features

- Counts every chat message per player.
- `/chatstats` — shows the top 10 most active chat participants and the total message count.
- `/chatstats <player>` — shows how many messages a specific player has sent.
- `/chatstats reset` — clears all recorded counts (requires operator permission level 2).
- Data is saved automatically to `config/chatstats_messages.json` and loaded on startup.

## Requirements

- Minecraft **26.1.2**
- Fabric Loader **0.19.5** or newer
- Fabric API for **26.1.2**

## Building from source

```bash
./gradlew build
```

The compiled mod jar appears under `build/libs/`.

## Configuration

All behaviour is controlled through the in-game commands. The recorded data lives in `config/chatstats_messages.json` — feel free to back it up or edit it while the server is stopped.

### Dependencies / versions

Versions are declared in `gradle.properties`:

| Property             | Value        |
|----------------------|--------------|
| `minecraft_version`  | `26.1.2`     |
| `loader_version`     | `0.19.5`     |
| `fabric_version`     | `0.119.0+26.1.2` |
| `yarn_mappings`      | `26.1.2+build.1` |

> **Note:** the `fabric_version` in `gradle.properties` must match the Fabric API build for Minecraft 26.1.2. Check https://fabricmc.net/develop/ for the exact version string and update `gradle.properties` if needed before building.

## Commands

| Command                     | Description                                     | Permission |
|-----------------------------|-------------------------------------------------|------------|
| `/chatstats`                | Show the leaderboard (top 10) + total messages  | everyone   |
| `/chatstats <player>`       | Show the message count of one player            | everyone   |
| `/chatstats reset`          | Reset all message counts                        | op level 2 |

---

More Minecraft mod projects by EnderDevelopment: https://github.com/EnderDevelopment

Project page: https://dash.enderdevelopment.com/community/84dada3d-f540-43ce-a4f8-dfe937cf3d40?utm_source=github&utm_medium=readme&utm_campaign=auto_publish&utm_content=84dada3d-f540-43ce-a4f8-dfe937cf3d40
