package de.blu.scoreboard.api;

import org.bukkit.entity.Player;

/** Defines the footer of the Tablist */
public interface TablistFooterInterface {

  /**
   * Get the Footer of the tablist
   *
   * @param player the owner of the scoreboard
   * @return the footer of the tablist
   */
  default String getTablistFooter(Player player) {
    return "";
  }
}
