# Client Mod

This is a 26.2 mod meant for the developers of this project to easily capture data from Minecraft servers.

## Features

- `/getskins <radius>` Gets the information of player's or NPC's skins in the defined radius.
- `/getarmorstandcolors <radius>` Gets the color information of armor stands in the defined radius.
- `/getarmorstandinfos <radius>` Gets the position information of armor stands in the defined radius. (wip)
- `/getscoreboardinfo` Prints out the scoreboard information in chat.
- `/copymaptexture [all]` Copy's the texture of a map to the clipboard.
- `/chunkexporter start` starts a chunk-only export.
- `/chunkexporter start block_displays` includes stationary block and item displays.
- `/chunkexporter start ravengard <name>` starts or resumes a named stitched Ravengard capture. Stop it with
  `/chunkexporter stop <name>`; checkpoints are stored in `.minecraft/chunkexporter_sessions`.
- `/chunkexporter status` shows the active capture counts.
- `/nbsrecord start`, `/nbsrecord stop <name>`, `/nbsrecord status` records inbound server sounds to
  `nbs-recordings/<name>.nbs` and exports custom instrument sounds.
- `Keybind K` Copies the texture ID of the hovered player head into the clipboard.
- `Keybind L` Copies the lore of the hovered item.
- `Keybind I` Selects the entity under the crosshair and opens a client-only inspector for inbound packets referencing
  it, its passengers/vehicle, or entities within 1.5 blocks.
