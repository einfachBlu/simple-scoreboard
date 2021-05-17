package de.blu.scoreboard.util;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class Tablist {

  public static void sendTablist(Player p, String header, String footer) {
    p.sendPlayerListHeaderAndFooter(Component.text(header), Component.text(footer));
  }
}
