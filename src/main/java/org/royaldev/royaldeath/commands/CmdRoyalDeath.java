package org.royaldev.royaldeath.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.royaldev.royaldeath.RoyalDeath;

public class CmdRoyalDeath implements CommandExecutor {

    private RoyalDeath plugin;

    public CmdRoyalDeath(RoyalDeath instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("royaldeath")) {
            if (!cs.hasPermission("rdeath.royaldeath")) {
                cs.sendMessage(ChatColor.RED + "You do not have permission for that!");
                return true;
            }
            plugin.c.reloadConfiguration();
            cs.sendMessage(ChatColor.BLUE + "RoyalDeath v" + ChatColor.GRAY + plugin.getDescription().getVersion() +  ChatColor.BLUE + " reloaded.");
            return true;
        }
        return false;
    }

}
