package org.royaldev.royaldeath.listeners;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.royaldev.royaldeath.Config;
import org.royaldev.royaldeath.Death;
import org.royaldev.royaldeath.RoyalDeath;

public class DeathListener implements Listener {

    private final RoyalDeath plugin;

    public DeathListener(final RoyalDeath instance) {
        this.plugin = instance;
    }

    private void sendDeathMessage(final String message, final World in) {
        final boolean interworld = Config.interworld;
        if (interworld) {
            this.plugin.getServer().broadcastMessage(message);
            return;
        }
        for (final Player p : this.plugin.getServer().getOnlinePlayers()) {
            if (!p.getWorld().getName().equals(in.getName())) continue;
            p.sendMessage(message);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(final PlayerDeathEvent event) {
        if (event.getEntity() == null) return;
        final World in = event.getEntity().getWorld();
        if (Config.disabledWorlds.contains(in.getName())) return;
        event.setDeathMessage(null);
        final Death death = new Death(this.plugin, event);
        this.sendDeathMessage(death.getNewDeathMessage(), in);
    }

}
