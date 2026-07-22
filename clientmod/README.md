# Client Mod

This is a 26.2 mod meant for the developers of this project to easily capture data from Minecraft servers.

## Features

- `/getskins <radius>` Gets the information of player's or NPC's skins in the defined radius.
- `/getarmorstandcolors <radius>` Gets the color information of armor stands in the defined radius.
- `/getarmorstandinfos <radius>` Gets the position information of armor stands in the defined radius. (wip)
- `/getscoreboardinfo` Prints out the scoreboard information in chat.
- `/copymaptexture [all]` Copy's the texture of a map to the clipboard.
- `/chunkexporter [start|stop <name>|status]` Exports the chunk data of the player. Will be saved to .minecraft/saves
- `/nbsrecord start`, `/nbsrecord stop <name>`, `/nbsrecord status` records inbound server sounds to
  `nbs-recordings/<name>.nbs` and exports custom instrument sounds.
- `Keybind K` Copies the texture ID of the hovered player head into the clipboard.
- `Keybind L` Copies the lore of the hovered item.