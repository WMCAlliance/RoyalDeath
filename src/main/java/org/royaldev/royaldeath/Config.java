package org.royaldev.royaldeath;

import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.List;

public class Config {

    private final RoyalDeath plugin;

    public Config(RoyalDeath instance) {
        plugin = instance;
        File config = new File(plugin.getDataFolder(), "config.yml");
        if (!config.exists()) {
            if (!config.getParentFile().mkdirs()) plugin.getLogger().warning("Could not create config.yml directory.");
            plugin.saveDefaultConfig();
        }
        reloadConfiguration();
    }

    public void reloadConfiguration() {
        plugin.reloadConfig();
        final FileConfiguration c = plugin.getConfig();
        interworld = c.getBoolean("show_interworld", true);

        varColor = c.getString("var_color", "DARK_AQUA");
        mesColor = c.getString("mes_color", "RED");

        disabledWorlds = c.getStringList("disabled_worlds");
    }

    public static boolean interworld;

    public static String varColor;
    public static String mesColor;

    public static List<String> disabledWorlds;

}
