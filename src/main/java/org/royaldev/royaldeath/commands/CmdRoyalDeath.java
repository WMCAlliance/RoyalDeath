package org.royaldev.royaldeath.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.royaldev.royaldeath.RoyalDeath;

public class CmdRoyalDeath implements CommandExecutor {

    private final RoyalDeath plugin;

    public CmdRoyalDeath(final RoyalDeath instance) {
        this.plugin = instance;
    }

    public boolean onCommand(final CommandSender cs, final Command cmd, final String label, final String[] args) {
        if (!cmd.getName().equalsIgnoreCase("royaldeath")) return false;
        if (!cs.hasPermission("rdeath.royaldeath")) {
            cs.sendMessage(ChatColor.RED + "You do not have permission for that!");
            return true;
        }
        this.plugin.c.reloadConfiguration();
        cs.sendMessage(ChatColor.BLUE + "RoyalDeath v" + ChatColor.GRAY + this.plugin.getDescription().getVersion() + ChatColor.BLUE + " reloaded.");
        return true;
    }

}
