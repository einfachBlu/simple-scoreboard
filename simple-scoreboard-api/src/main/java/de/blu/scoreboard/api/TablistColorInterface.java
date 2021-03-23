package de.blu.scoreboard.api;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/** Defines the color in the Tablist */
public interface TablistColorInterface {

  /**
   * Get the Color from the player
   *
   * @param player the owner of the scoreboard
   * @param target the target
   * @return the color for target
   */
  default ChatColor getTablistColor(Player player, Player target) {
    return ChatColor.WHITE;
  }
}
