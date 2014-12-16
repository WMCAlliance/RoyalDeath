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
import org.bukkit.projectiles.ProjectileSource;
import org.royaldev.royaldeath.Config;
import org.royaldev.royaldeath.RoyalDeath;

import java.util.List;
import java.util.Random;

public class DeathListener implements Listener {

    private final RoyalDeath plugin;
    private final Random r = new Random();

    public DeathListener(final RoyalDeath instance) {
        this.plugin = instance;
    }

    private String getCustomName(final ItemStack is) {
        if (!this.hasCustomName(is)) throw new IllegalArgumentException("Has no custom name!");
        return is.getItemMeta().getDisplayName();
    }

    private String getRandomMessage(final EntityDamageEvent.DamageCause cause, final boolean wasPlayer) {
        final String pullFrom;
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
        final List<String> messages = plugin.getConfig().getStringList(pullFrom);
        if (messages == null || messages.isEmpty()) return "";
        return messages.get(this.r.nextInt(messages.size()));
    }

    private ChatColor getStringColor() {
        try {
            return ChatColor.valueOf(Config.mesColor.toUpperCase());
        } catch (final IllegalArgumentException e) {
            return ChatColor.DARK_AQUA;
        }
    }

    private ChatColor getVariableColor() {
        try {
            return ChatColor.valueOf(Config.varColor.toUpperCase());
        } catch (final IllegalArgumentException e) {
            return ChatColor.DARK_AQUA;
        }
    }

    private boolean hasCustomName(final ItemStack is) {
        final ItemMeta im = is.getItemMeta();
        return im != null && im.hasDisplayName();
    }

    private void sendDeathMessage(final String message, final World in) {
        boolean interworld = Config.interworld;
        if (in == null) interworld = true;
        if (interworld) {
            this.plugin.getServer().broadcastMessage(message);
            return;
        }
        for (final Player p : this.plugin.getServer().getOnlinePlayers()) {
            if (!p.getWorld().getName().equals(in.getName())) continue;
            p.sendMessage(message);
        }
    }

    public String getDeathMessage(final Object killer, final Player dead, EntityDamageEvent.DamageCause cause) {
        if (cause == null) cause = EntityDamageEvent.DamageCause.CUSTOM;
        final String message;
        if (killer instanceof Player) {
            message = this.replaceVariables(this.getRandomMessage(cause, true), dead, null, (Player) killer);
        } else if (killer instanceof LivingEntity) {
            message = this.replaceVariables(this.getRandomMessage(cause, false), dead, (LivingEntity) killer, null);
        } else if (killer instanceof Block) {
            message = this.replaceVariables(this.getRandomMessage(cause, false), dead, null, null);
        } else if (killer instanceof Projectile) {
            final Projectile p = (Projectile) killer;
            final ProjectileSource ps = p.getShooter();
            if (ps instanceof Player) {
                message = this.replaceVariables(this.getRandomMessage(EntityDamageEvent.DamageCause.ENTITY_ATTACK, true), dead, null, (Player) ps);
            } else if (ps instanceof LivingEntity) {
                message = this.replaceVariables(this.getRandomMessage(EntityDamageEvent.DamageCause.ENTITY_ATTACK, false), dead, (LivingEntity) ps, null);
            } else
                message = this.replaceVariables(this.getRandomMessage(EntityDamageEvent.DamageCause.ENTITY_ATTACK, false), dead, null, null);
        } else {
            message = this.replaceVariables(this.getRandomMessage(cause, false), dead, null, null);
        }
        return message;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(final PlayerDeathEvent event) {
        if (event.getEntity() == null) return;
        final World in = event.getEntity().getWorld();
        if (Config.disabledWorlds.contains(in.getName())) return;
        final EntityDamageEvent ev = event.getEntity().getLastDamageCause();
        final String deathMessage;
        if (ev == null) {
            this.sendDeathMessage(this.getDeathMessage(null, event.getEntity(), null), in);
            return;
        }
        if (ev.isCancelled()) return;
        final Player p = event.getEntity();
        final EntityDamageEvent.DamageCause dc = ev.getCause();
        event.setDeathMessage(null);
        if (ev instanceof EntityDamageByEntityEvent) {
            final EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) ev;
            final Entity damager = e.getDamager();
            deathMessage = this.getDeathMessage(damager, p, dc);
        } else if (ev instanceof EntityDamageByBlockEvent) {
            final EntityDamageByBlockEvent e = (EntityDamageByBlockEvent) ev;
            deathMessage = this.getDeathMessage(e.getDamager(), p, dc);
        } else deathMessage = this.getDeathMessage(null, p, dc);
        this.sendDeathMessage(deathMessage, in);
    }

    public String replaceVariables(String message, final Player p, final LivingEntity mob, final Player killer) {
        message = this.getStringColor() + message;
        if (p != null) {
            message = message.replaceAll("(?i)\\{player}", this.getVariableColor() + p.getName() + this.getStringColor());
            message = message.replaceAll("(?i)\\{dispplayer}", this.getVariableColor() + p.getDisplayName() + this.getStringColor());
            message = message.replaceAll("(?i)\\{world}", this.getVariableColor() + p.getWorld().getName() + this.getStringColor());
        }
        if (killer != null) {
            ItemStack hand = killer.getItemInHand();
            String inHand = hand.getType().name().replace("_", " ").toLowerCase();
            if (inHand.equalsIgnoreCase("air")) inHand = "fists";
            if (inHand.equalsIgnoreCase("bow")) inHand = "bow & arrow";
            if (hasCustomName(hand)) inHand = getCustomName(hand);
            message = message.replaceAll("(?i)\\{hand}", this.getVariableColor() + inHand + this.getStringColor());
            message = message.replaceAll("(?i)\\{killer}", this.getVariableColor() + killer.getName() + this.getStringColor());
            message = message.replaceAll("(?i)\\{dispkiller}", this.getVariableColor() + killer.getDisplayName() + this.getStringColor());
        }
        if (mob != null) {
            String mname;
            if (mob instanceof Wolf) mname = "wolf";
            else mname = mob.toString().toLowerCase().replace("craft", "");
            message = message.replaceAll("(?i)\\{mob}", this.getVariableColor() + mname + this.getStringColor());
        } else message = message.replaceAll("(?i)\\{mob}", this.getVariableColor() + "monster" + this.getStringColor());
        return message;
    }

}
