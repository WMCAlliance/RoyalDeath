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

import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.api.RApiMain;
import org.royaldev.royalcommands.api.RWorldApi;
import org.royaldev.royaldeath.commands.CmdRoyalDeath;
import org.royaldev.royaldeath.listeners.RDListener;

import java.io.File;
import java.util.logging.Logger;

public class RoyalDeath extends JavaPlugin {

    public Logger log;

    public String getWorldName(World w) {
        Plugin p = getServer().getPluginManager().getPlugin("RoyalCommands");
        if (p == null) return w.getName();
        RApiMain ram = ((RoyalCommands) p).getAPI();
        if (ram == null) return w.getName();
        RWorldApi rwa = ram.getWorldAPI();
        if (rwa == null) return w.getName();
        String name = rwa.getWorldName(w);
        if (name == null) return w.getName();
        return name;
    }

    public void onEnable() {
        log = getLogger();

        if (!new File(getDataFolder() + File.separator + "config.yml").exists()) saveDefaultConfig();

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new RDListener(this), this);

        getCommand("royaldeath").setExecutor(new CmdRoyalDeath(this));

        log.info("Enabled v" + getDescription().getVersion() + ".");
    }

    public void onDisable() {
        log.info("Disabled v" + getDescription().getVersion() + ".");
    }

}
