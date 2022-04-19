package org.royaldev.royaldeath;

import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import org.bukkit.Bukkit;
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
//	    /**
//     * Gets a world via its real name, or Multiverse name.
//     *
//     * @param name Name of world to get
//     * @return World or null if none exists
//     */
//    public static World getWorld(String name) {
//        World w;
//        w = Bukkit.getWorld(name);
//        if (w != null) return w;
//        if (RoyalDeath.mvc != null) {
//            MultiverseWorld mvw = RoyalDeath.mvc.getMVWorldManager().getMVWorld(name);
//            w = (mvw == null) ? null : mvw.getCBWorld();
//            if (w != null) return w;
//        }
//        w = RoyalDeath.wm.getWorld(name);
//        return w;
//    }

    public static String getMVWorldName(World w) {
        if (w == null) throw new NullPointerException("w can't be null!");
        return RoyalDeath.mvc.getMVWorldManager().getMVWorld(w).getColoredWorldString();
    }

}
