package org.royaldev.royaldeath;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.royaldev.royaldeath.listeners.RDListener;

import java.io.File;
import java.util.logging.Logger;

public class RoyalDeath extends JavaPlugin {

    public Logger log;

    public void onEnable() {

        log = getLogger();

        if (!new File(getDataFolder() + File.separator + "config.yml").exists()) saveDefaultConfig();

        final RDListener rdListener = new RDListener(this);

        PluginManager pm = getServer().getPluginManager();

        pm.registerEvents(rdListener, this);

        log.info("Starting v" + getDescription().getVersion() + ".");

    }

    public void onDisable() {

        log.info("Stopping v" + getDescription().getVersion() + ".");

    }

}
