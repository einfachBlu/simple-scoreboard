package de.blu.scoreboard.api;

import org.bukkit.entity.Player;

/** Defines the order in the Tablist */
public interface TablistSortInterface {

  /**
   * Get the Sort priority from the player
   *
   * @param player the owner of the scoreboard
   * @param target the target
   * @return the sort priority for target
   */
  default int getTablistSort(Player player, Player target) {
    return 0;
  }
}
