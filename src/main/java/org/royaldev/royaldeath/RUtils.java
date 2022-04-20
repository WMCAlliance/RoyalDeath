package org.royaldev.royaldeath;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

public class RUtils {

    public static String formatVariable(final String variable) {
        return RUtils.getVariableColor() + variable + RUtils.getStringColor();
    }

    public static String getCustomName(final ItemStack is) {
        if (!is.hasItemMeta()) return null;
        return is.getItemMeta().getDisplayName();
    }

    public static String getItemStackName(final ItemStack is) {
        String name = is.getType().name().toLowerCase().replace('_', ' ');
        if (name.equalsIgnoreCase("air")) name = "fists";
        else if (name.equalsIgnoreCase("bow")) name = "bow & arrow";
        final String customName = RUtils.getCustomName(is);
        if (customName != null) name = customName;
        return name;
    }

    public static ChatColor getStringColor() {
        try {
            return ChatColor.valueOf(Config.messageColor.toUpperCase());
        } catch (final IllegalArgumentException e) {
            return ChatColor.DARK_AQUA;
        }
    }

    public static ChatColor getVariableColor() {
        try {
            return ChatColor.valueOf(Config.variableColor.toUpperCase());
        } catch (final IllegalArgumentException e) {
            return ChatColor.DARK_AQUA;
        }
    }

    public static String getMVWorldName(World w) {
        if (w == null) throw new NullPointerException("w can't be null!");
        if (RoyalDeath.mvc != null) {
            return RoyalDeath.mvc.getMVWorldManager().getMVWorld(w).getColoredWorldString();
        } else if (RoyalDeath.rcmds != null) {
            return org.royaldev.royalcommands.RUtils.getMVWorldName(w);
        } else
            return w.getName();
    }
}
