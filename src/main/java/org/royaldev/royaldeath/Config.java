package org.royaldev.royaldeath;

import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.List;

public class Config {

    public static boolean interworld;
    public static String varColor;
    public static String mesColor;
    public static List<String> disabledWorlds;
    private final RoyalDeath plugin;

    public Config(final RoyalDeath instance) {
        this.plugin = instance;
        final File config = new File(this.plugin.getDataFolder(), "config.yml");
        if (!config.exists()) {
            if (!config.getParentFile().mkdirs()) {
                this.plugin.getLogger().warning("Could not create config.yml directory.");
            }
            this.plugin.saveDefaultConfig();
        }
        this.reloadConfiguration();
    }

    public void reloadConfiguration() {
        this.plugin.reloadConfig();
        final FileConfiguration c = this.plugin.getConfig();
        Config.interworld = c.getBoolean("show_interworld", true);

        Config.varColor = c.getString("var_color", "DARK_AQUA");
        Config.mesColor = c.getString("mes_color", "RED");

        Config.disabledWorlds = c.getStringList("disabled_worlds");
    }

}
