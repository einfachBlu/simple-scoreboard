package de.blu.scoreboard.api;

import org.bukkit.entity.Player;

/** Defines the prefix in the Tablist */
public interface TablistPrefixInterface {

  /**
   * Get the Prefix from the player
   *
   * @param player the owner of the scoreboard
   * @param target the target
   * @return the prefix for target
   */
  default String getTablistPrefix(Player player, Player target) {
    return "";
  }
}
