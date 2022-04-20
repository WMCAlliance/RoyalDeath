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
package org.royaldev.royaldeath;

import com.onarandombox.MultiverseCore.MultiverseCore;
import org.royaldev.royalcommands.RoyalCommands;
import java.io.File;
import java.util.logging.Logger;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.royaldev.royaldeath.commands.CmdRoyalDeath;
import org.royaldev.royaldeath.listeners.DeathListener;

public class RoyalDeath extends JavaPlugin {

    public Logger log;
    public Config c;
    public static MultiverseCore mvc = null;
    public static RoyalCommands rcmds = null;

    public void onDisable() {
        this.log.info("Disabled v" + this.getDescription().getVersion() + ".");
    }

    public void onEnable() {
        this.log = this.getLogger();

        this.c = new Config(this);

        if (!new File(getDataFolder(), "config.yml").exists()) this.saveDefaultConfig();

        final PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(new DeathListener(this), this);

        this.getCommand("royaldeath").setExecutor(new CmdRoyalDeath(this));
		
        RoyalDeath.mvc = (MultiverseCore) this.getServer().getPluginManager().getPlugin("Multiverse-Core");
        if (RoyalDeath.mvc != null){
            this.log.info("MultiverseCore detected, will be used where possible");
        }
        RoyalDeath.rcmds = (RoyalCommands) this.getServer().getPluginManager().getPlugin("RoyalCommands");
        if (RoyalDeath.rcmds != null){
            this.log.info("RoyalCommands detected, will be used where possible");
        }
        this.log.info("Enabled v" + this.getDescription().getVersion() + ".");
    }

}
