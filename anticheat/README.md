# Swofty AntiCheat

[<img src="https://discordapp.com/assets/e4923594e694a21542a489471ecffa50.svg" alt="" height="55" />](https://discord.gg/atlasmc)

An updated anticheat for Minestom servers under the GPL 3.0 license for public use.

## Table of contents

* [Getting Started](#getting-started)
* [Minestom Loader](#minestom-loader)
* [Spigot Loader](#spigot-loader)
* [License](#license)
* [Credits](#credits)

## Getting Started
SwoftyAnticheat 

## Minestom Loader
To use SwoftyAnticheat with Minestom, you must first create a new instance of the MinestomLoader class and register the listeners with the Minestom event handler. You must also create a new instance of the SwoftyValues class and start the anticheat.
```java
MinestomLoader minestomLoader = new MinestomLoader();
minestomLoader.registerListeners(MinecraftServer.getGlobalEventHandler());

SwoftyAnticheat.loader(minestomLoader);
SwoftyAnticheat.values(new SwoftyValues());
SwoftyAnticheat.start(); // Do this after MinecraftServer#init()
```

## License
SwoftyAnticheat's directory is licensed under the permissive MIT license. Please see [`LICENSE.txt`](https://github.com/Swofty-Developments/HypixelSkyBlock/blob/master/anticheat/LICENSE.md) for more information.

## Credits
Thanks to:
* Myself and any other contributors, which can be viewed on this Git page.
