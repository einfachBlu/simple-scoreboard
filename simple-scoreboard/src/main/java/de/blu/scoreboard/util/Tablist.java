package de.blu.scoreboard.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class Tablist {

  public static Class<?> getNmsClass(String nmsClassName) throws ClassNotFoundException {
    return Class.forName(
        "net.minecraft.server."
            + Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3]
            + "."
            + nmsClassName);
  }

  public static String getServerVersion() {
    return Bukkit.getServer().getClass().getPackage().getName().substring(23);
  }

  public static void sendTablist(Player p, String header, String footer) {
    /*
    p.setPlayerListHeader(header);
    p.setPlayerListFooter(footer);
    */
    try {
      if ((getServerVersion().equalsIgnoreCase("v1_9_R1"))
          || (getServerVersion().equalsIgnoreCase("v1_9_R2"))) {
        Object headerObject =
            getNmsClass("ChatComponentText")
                .getConstructor(new Class[] {String.class})
                .newInstance(ChatColor.translateAlternateColorCodes('&', header));
        Object footerObject =
            getNmsClass("ChatComponentText")
                .getConstructor(new Class[] {String.class})
                .newInstance(ChatColor.translateAlternateColorCodes('&', footer));

        Object ppoplhf =
            getNmsClass("PacketPlayOutPlayerListHeaderFooter")
                .getConstructor(new Class[] {getNmsClass("IChatBaseComponent")})
                .newInstance(headerObject);

        Field f = ppoplhf.getClass().getDeclaredField("b");
        f.setAccessible(true);
        f.set(ppoplhf, footerObject);

        Object nmsp = p.getClass().getMethod("getHandle", new Class[0]).invoke(p);
        Object pcon = nmsp.getClass().getField("playerConnection").get(nmsp);

        pcon.getClass()
            .getMethod("sendPacket", new Class[] {getNmsClass("Packet")})
            .invoke(pcon, ppoplhf);
      } else if ((getServerVersion().equalsIgnoreCase("v1_8_R2"))
          || (getServerVersion().equalsIgnoreCase("v1_8_R3"))) {
        Object headerObject =
            getNmsClass("IChatBaseComponent$ChatSerializer")
                .getMethod("a", new Class[] {String.class})
                .invoke(null, "{'text': '" + header + "'}");
        Object footerObject =
            getNmsClass("IChatBaseComponent$ChatSerializer")
                .getMethod("a", new Class[] {String.class})
                .invoke(null, "{'text': '" + footer + "'}");

        Object ppoplhf =
            getNmsClass("PacketPlayOutPlayerListHeaderFooter")
                .getConstructor(new Class[] {getNmsClass("IChatBaseComponent")})
                .newInstance(headerObject);

        Field f = ppoplhf.getClass().getDeclaredField("b");
        f.setAccessible(true);
        f.set(ppoplhf, footerObject);

        Object nmsp = p.getClass().getMethod("getHandle", new Class[0]).invoke(p);
        Object pcon = nmsp.getClass().getField("playerConnection").get(nmsp);

        pcon.getClass()
            .getMethod("sendPacket", new Class[] {getNmsClass("Packet")})
            .invoke(pcon, ppoplhf);
      } else {
        Object headerObject =
            getNmsClass("ChatSerializer")
                .getMethod("a", new Class[] {String.class})
                .invoke(null, "{'text': '" + header + "'}");
        Object footerObject =
            getNmsClass("ChatSerializer")
                .getMethod("a", new Class[] {String.class})
                .invoke(null, "{'text': '" + footer + "'}");

        Object ppoplhf =
            getNmsClass("PacketPlayOutPlayerListHeaderFooter")
                .getConstructor(new Class[] {getNmsClass("IChatBaseComponent")})
                .newInstance(headerObject);

        Field f = ppoplhf.getClass().getDeclaredField("b");
        f.setAccessible(true);
        f.set(ppoplhf, footerObject);

        Object nmsp = p.getClass().getMethod("getHandle", new Class[0]).invoke(p);
        Object pcon = nmsp.getClass().getField("playerConnection").get(nmsp);

        pcon.getClass()
            .getMethod("sendPacket", new Class[] {getNmsClass("Packet")})
            .invoke(pcon, ppoplhf);
      }
    } catch (IllegalAccessException
        | IllegalArgumentException
        | InvocationTargetException
        | NoSuchMethodException
        | SecurityException
        | ClassNotFoundException
        | InstantiationException
        | NoSuchFieldException e) {
      e.printStackTrace();
    }
  }
}
