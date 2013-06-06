package org.royaldev.royaldeath.listeners;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.royaldev.royaldeath.Config;
import org.royaldev.royaldeath.RoyalDeath;

import java.util.List;
import java.util.Random;

public class RDListener implements Listener {

    private final RoyalDeath plugin;

    private ChatColor variableColor() {
        try {
            return ChatColor.valueOf(Config.varColor.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ChatColor.DARK_AQUA;
        }
    }

    private ChatColor stringColor() {
        try {
            return ChatColor.valueOf(Config.mesColor.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ChatColor.DARK_AQUA;
        }
    }

    private boolean hasCustomName(ItemStack is) {
        ItemMeta im = is.getItemMeta();
        return im != null && im.hasDisplayName();
    }

    private String getCustomName(ItemStack is) {
        if (!hasCustomName(is)) throw new IllegalArgumentException("Has no custom name!");
        return is.getItemMeta().getDisplayName();
    }

    public RDListener(RoyalDeath instance) {
        plugin = instance;
    }

    public String replacer(String message, Player p, LivingEntity mob, Player killer) {
        message = stringColor() + message;
        if (p != null) {
            message = message.replaceAll("(?i)\\{player}", variableColor() + p.getName() + stringColor());
            message = message.replaceAll("(?i)\\{dispplayer}", variableColor() + p.getDisplayName() + stringColor());
            message = message.replaceAll("(?i)\\{world}", variableColor() + plugin.getWorldName(p.getWorld()) + stringColor());
        }
        if (killer != null) {
            ItemStack hand = killer.getItemInHand();
            String inHand = hand.getType().name().replace("_", " ").toLowerCase();
            if (inHand.equalsIgnoreCase("air")) inHand = "fists";
            if (inHand.equalsIgnoreCase("bow")) inHand = "bow & arrow";
            if (hasCustomName(hand)) inHand = getCustomName(hand);
            message = message.replaceAll("(?i)\\{hand}", variableColor() + inHand + stringColor());
            message = message.replaceAll("(?i)\\{killer}", variableColor() + killer.getName() + stringColor());
            message = message.replaceAll("(?i)\\{dispkiller}", variableColor() + killer.getDisplayName() + stringColor());
        }
        if (mob != null) {
            String mname;
            if (mob instanceof Wolf) mname = "wolf";
            else mname = mob.toString().toLowerCase().replace("craft", "");
            message = message.replaceAll("(?i)\\{mob}", variableColor() + mname + stringColor());
        } else message = message.replaceAll("(?i)\\{mob}", variableColor() + "monster" + stringColor());
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
            case FALLING_BLOCK:
                pullFrom = "fab";
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
            case THORNS:
                pullFrom = "tho";
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

    private void sendDeathMessage(String message) {
        sendDeathMessage(message, null);
    }

    private void sendDeathMessage(String message, World in) {
        boolean interworld = Config.interworld;
        if (in == null) interworld = true;
        if (interworld) {
            plugin.getServer().broadcastMessage(message);
            return;
        }
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            if (!p.getWorld().getName().equals(in.getName())) continue;
            p.sendMessage(message);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent event) {
        if (event.getEntity() == null) return;
        World in = event.getEntity().getWorld();
        EntityDamageEvent ev = event.getEntity().getLastDamageCause();
        String deathMessage;
        if (ev == null) {
            sendDeathMessage(getDeathMessage(null, event.getEntity(), null), in);
            return;
        }
        if (ev.isCancelled()) return;
        Player p = event.getEntity();
        EntityDamageEvent.DamageCause dc = ev.getCause();
        event.setDeathMessage(null);
        if (ev instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) ev;
            Entity damager = e.getDamager();
            deathMessage = getDeathMessage(damager, p, dc);
        } else if (ev instanceof EntityDamageByBlockEvent) {
            EntityDamageByBlockEvent e = (EntityDamageByBlockEvent) ev;
            deathMessage = getDeathMessage(e.getDamager(), p, dc);
        } else deathMessage = getDeathMessage(null, p, dc);
        sendDeathMessage(deathMessage, in);
    }

}
