package de.blu.scoreboard.api;

import org.bukkit.entity.Player;

/** Defines the suffx in the Tablist */
public interface TablistSuffixInterface {

  /**
   * Get the Suffix from the player
   *
   * @param player the owner of the scoreboard
   * @param target the target
   * @return the suffix for target
   */
  default String getTablistSuffix(Player player, Player target) {
    return "";
  }
}
