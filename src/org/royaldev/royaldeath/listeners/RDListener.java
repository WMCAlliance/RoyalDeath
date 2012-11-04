package org.royaldev.royaldeath.listeners;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.royaldev.royaldeath.RoyalDeath;

import java.util.List;
import java.util.Random;

public class RDListener implements Listener {

    RoyalDeath plugin;

    private ChatColor variable;
    private ChatColor string;

    public RDListener(RoyalDeath instance) {
        plugin = instance;
        try {
            variable = ChatColor.valueOf(plugin.getConfig().getString("var_color"));
            string = ChatColor.valueOf(plugin.getConfig().getString("mes_color"));
        } catch (Exception e) {
            variable = ChatColor.DARK_AQUA;
            string = ChatColor.RED;
        }
    }

    public String replacer(String message, Player p, LivingEntity mob, Player killer) {
        message = string + message;
        if (p != null) {
            message = message.replaceAll("(?i)\\{player\\}", variable + p.getName() + string);
            message = message.replaceAll("(?i)\\{dispplayer\\}", variable + p.getDisplayName() + string);
            message = message.replaceAll("(?i)\\{world\\}", variable + plugin.getWorldName(p.getWorld()) + string);
        }
        if (killer != null) {
            String inHand = killer.getItemInHand().getType().name().replace("_", " ").toLowerCase();
            if (inHand.equalsIgnoreCase("air")) inHand = "fists";
            if (inHand.equalsIgnoreCase("bow")) inHand = "bow & arrow";
            message = message.replaceAll("(?i)\\{hand\\}", variable + inHand + string);
            message = message.replaceAll("(?i)\\{killer\\}", variable + killer.getName() + string);
            message = message.replaceAll("(?i)\\{dispkiller\\}", variable + killer.getDisplayName() + string);
        }
        if (mob != null) {
            String mname;
            if (mob instanceof Wolf) mname = "wolf";
            else mname = mob.toString().toLowerCase().replace("craft", "");
            message = message.replaceAll("(?i)\\{mob\\}", variable + mname + string);
        } else message = message.replaceAll("(?i)\\{mob\\}", variable + "monster" + string);
        return message;
    }

    private String selectRandomMessage(EntityDamageEvent.DamageCause cause, boolean wasPlayer) {
        String pullFrom;
        switch (cause) {
            case BLOCK_EXPLOSION:
                pullFrom = "blo";
                break;
            case CONTACT:
                pullFrom = "con";
                break;
            case ENTITY_ATTACK:
                pullFrom = (wasPlayer) ? "pvp" : "mob";
                break;
            case PROJECTILE:
                pullFrom = (wasPlayer) ? "pvp" : "mob";
                break;
            case SUFFOCATION:
                pullFrom = "suf";
                break;
            case FALL:
                pullFrom = "fal";
                break;
            case FIRE:
                pullFrom = "fir";
                break;
            case FIRE_TICK:
                pullFrom = "fir";
                break;
            case MELTING:
                pullFrom = "oth";
                break;
            case LAVA:
                pullFrom = "lav";
                break;
            case DROWNING:
                pullFrom = "dro";
                break;
            case ENTITY_EXPLOSION:
                pullFrom = "cre";
                break;
            case VOID:
                pullFrom = "voi";
                break;
            case LIGHTNING:
                pullFrom = "lig";
                break;
            case SUICIDE:
                pullFrom = "sui";
                break;
            case STARVATION:
                pullFrom = "sta";
                break;
            case POISON:
                pullFrom = "poi";
                break;
            case MAGIC:
                pullFrom = "mag";
                break;
            case WITHER:
                pullFrom = "wit";
                break;
            case CUSTOM:
                pullFrom = "oth";
                break;
            default:
                pullFrom = "oth";
                break;
        }
        List<String> messages = plugin.getConfig().getStringList(pullFrom);
        if (messages == null || messages.isEmpty()) return "";
        return messages.get(new Random().nextInt(messages.size()));
    }

    public String getDeathMessage(Object killer, Player dead, EntityDamageEvent.DamageCause cause) {
        if (cause == null) cause = EntityDamageEvent.DamageCause.CUSTOM;
        String message;
        if (killer instanceof Player) {
            message = replacer(selectRandomMessage(cause, true), dead, null, (Player) killer);
        } else if (killer instanceof LivingEntity) {
            message = replacer(selectRandomMessage(cause, false), dead, (LivingEntity) killer, null);
        } else if (killer instanceof Block) {
            message = replacer(selectRandomMessage(cause, false), dead, null, null);
        } else if (killer instanceof Projectile) {
            Projectile p = (Projectile) killer;
            LivingEntity le = p.getShooter();
            if (le instanceof Player)
                message = replacer(selectRandomMessage(EntityDamageEvent.DamageCause.ENTITY_ATTACK, true), dead, null, (Player) le);
            else
                message = replacer(selectRandomMessage(EntityDamageEvent.DamageCause.ENTITY_ATTACK, false), dead, le, null);
        } else {
            message = replacer(selectRandomMessage(cause, false), dead, null, null);
        }
        return message;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent event) {
        if (event.getEntity() == null) return;
        EntityDamageEvent ev = event.getEntity().getLastDamageCause();
        if (ev == null) {
            event.setDeathMessage(getDeathMessage(null, event.getEntity(), null));
            return;
        }
        if (ev.isCancelled()) return;
        Player p = event.getEntity();
        EntityDamageEvent.DamageCause dc = ev.getCause();
        if (ev instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) ev;
            Entity damager = e.getDamager();
            String message = getDeathMessage(damager, p, dc);
            event.setDeathMessage(message);
        } else if (ev instanceof EntityDamageByBlockEvent) {
            EntityDamageByBlockEvent e = (EntityDamageByBlockEvent) ev;
            String message = getDeathMessage(e.getDamager(), p, dc);
            event.setDeathMessage(message);
        } else {
            String message = getDeathMessage(null, p, dc);
            event.setDeathMessage(message);
        }
    }

}
