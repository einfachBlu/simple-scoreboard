package de.blu.scoreboard.api;

import org.bukkit.entity.Player;

public interface SidebarInterface {

  /**
   * Get the Title of the Sidebar
   *
   * @param player the player who owns the sidebar
   * @return title
   */
  String getTitle(Player player);

  /**
   * Get the lines which should be shown in the sidebar
   *
   * @param player the player who owns the sidebar
   * @return String[] with all Lines
   */
  String[] getLines(Player player);
}
