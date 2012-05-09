package org.royaldev.royaldeath;

/*
 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.

 This plugin was written by jkcclemens <jkc.clemens@gmail.com>.
 If forked and not credited, alert him.
 */

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