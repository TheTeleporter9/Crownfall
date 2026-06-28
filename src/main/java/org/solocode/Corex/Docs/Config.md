# Corex Config Library Documentation
> **Design Rules:** Keep It Stupid Simple (KISS). No loops, no boilerplate, no headaches.

---

# 1. Why Build Corex Config This Way?

In regular PaperMC plugin coding, making a config file is a lot of boring work. You usually have to:

1. Create a `.yml` file.
2. Write a manager class.
3. Write loops to read things.
4. Type lines like `config.getString("path")` over and over.

If you add just *one* new variable, you have to change your config file, your object class, your constructor, and your custom loop code.

**Corex fixes this.** With Corex, the library does all the heavy lifting. You just write your variables in Java, and Corex automatically creates the files, handles the loops, and loads the data for you.

---

# 2. How to Setup Corex (The One-Line Rule)

To start Corex, you only need to write **one single line** inside your main plugin's `onEnable()` method. You don't need to register any files or classes manually.

```java
package org.solocode.myplugin;

import org.bukkit.plugin.java.JavaPlugin;
import org.solocode.Corex.Corex;

public class MyPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // This is literally all you have to do. Nothing more!!!
        new Corex(this);
    }
}
```

---

# 3. Code Examples (A Bunch of Ways to Use It)

Corex automatically reads your Java classes and changes them into YAML config files. Here are the three ways you can use it.

## Example 1: Simple Global Config (`config.yml`)

If you don't name a specific file, Corex puts everything inside the standard `config.yml` file automatically. If you leave the `@Config` empty, it just uses the variable name as the key.

```java
package org.solocode.myplugin.config;

import org.solocode.Corex.config.Config;

public class GlobalSettings {

    // Saves to config.yml as -> server-port: 25565
    @Config("server-port")
    public static int port = 25565;

    // Annotation is empty! Saves to config.yml as -> debugMode: false
    @Config
    public static boolean debugMode = false;

    // Saves to config.yml as -> prefix: "[MyPlugin]"
    @Config("prefix")
    public static String chatPrefix = "[MyPlugin]";
}
```

## Example 2: Splitting Stuff into Different Files

If you want to put settings into separate files (like `messages.yml` or `database.yml`), just add the file name and a dot (`.`) at the start of your path. Corex will create those files for you.

```java
package org.solocode.myplugin.config;

import org.solocode.Corex.config.Config;

public class ModuleSettings {

    // Creates database.yml and saves under -> credentials.url
    @Config("database.credentials.url")
    public static String dbUrl = "jdbc:mysql://localhost:3306/minecraft";

    // Creates messages.yml and saves under -> combat.kill-broadcast
    @Config("messages.combat.kill-broadcast")
    public static String killMsg = "&c{player} was killed!";

    // Goes into messages.yml under -> system.maintenance
    @Config("messages.system.maintenance")
    public static boolean maintenanceMode = false;
}
```

## Example 3: Automatic Lists (Biomes & Maps)

This is the coolest part. If you have a dynamic list of objects (like custom biomes or game maps where server admins can add as many as they want), you don't need to write loops anymore. Just make a standard Java `Map` and Corex will load them all automatically.

### First, make your simple object class:

```java
package org.solocode.myplugin.world;

public class CustomBiome {
    private final String id;
    private final String resourceKey;
    private final String fogColor;

    // Corex reads this constructor to know what keys to look for in the YAML file
    public CustomBiome(String id, String resourceKey, String fogColor) {
        this.id = id;
        this.resourceKey = resourceKey;
        this.fogColor = fogColor;
    }

    public String getId() { return id; }
    public String getResourceKey() { return resourceKey; }
    public String getFogColor() { return fogColor; }
}
```

### Next, declare your Map in a config class:

```java
package org.solocode.myplugin.config;

import org.solocode.Corex.config.Config;
import org.solocode.myplugin.world.CustomBiome;

import java.util.HashMap;
import java.util.Map;

public class WorldConfig {

    // Corex looks inside map-config.yml under the "biomes" section,
    // automatically loops through it, builds the CustomBiome objects,
    // and fills this map!
    @Config("map-config.biomes")
    public static Map<String, CustomBiome> biomes = new HashMap<>();
}
```

### Here is what Corex automatically creates on your server disk (`map-config.yml`):

```yaml
biomes:
  example_plains:
    resource-key: "minecraft:plains"
    fog-color: "#C0D8FF"
  volcano_core:
    resource-key: "custom:volcano"
    fog-color: "#FF4500"
```

---

# 4. How to Use Your Config Values in Game Code

Because your configuration variables are `public static`, you can read them from anywhere in your plugin using just one line of code.

```java
package org.solocode.myplugin.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.solocode.myplugin.config.GlobalSettings;
import org.solocode.myplugin.config.WorldConfig;
import org.solocode.myplugin.world.CustomBiome;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Reading basic settings values
        if (GlobalSettings.debugMode) {
            player.sendMessage(GlobalSettings.chatPrefix + " Debug mode is on!");
        }

        // Getting a custom object straight out of the automatic map
        CustomBiome plains = WorldConfig.biomes.get("example_plains");
        if (plains != null) {
            player.sendMessage("The fog color here is: " + plains.getFogColor());
        }
    }
}
```

---

# 5. Want More Control? (Advanced Features)

If you ever need to do something advanced manually, you can bypass the automatic system and talk directly to Corex's backend systems.

## Accessing Raw Config Data or Reloading Files

```java
// Get the raw Bukkit YamlConfiguration file instance:
YamlConfiguration rawYaml =
        corex.getConfigCore().getConfig("map-config");

// Tell Corex to reload a file from disk right now:
corex.getConfigCore().reloadConfig("map-config");

// Force Corex to run an injection update on an object:
CorexConfigInjector.inject(
        myConfigInstance,
        corex.getConfigCore()
);
```