package de.blu.scoreboard.api;

import org.bukkit.entity.Player;

/** Defines the header of the Tablist */
public interface TablistHeaderInterface {

  /**
   * Get the Header of the tablist
   *
   * @param player the owner of the scoreboard
   * @return the header of the tablist
   */
  default String getTablistHeader(Player player) {
    return "";
  }
}
