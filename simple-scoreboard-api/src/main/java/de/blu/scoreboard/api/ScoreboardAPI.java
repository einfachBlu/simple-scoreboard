package de.blu.scoreboard.api;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public abstract class ScoreboardAPI {

  @Getter private static ScoreboardAPI instance;

  protected Map<Integer, SidebarInterface> sidebarInterfaces = new HashMap<>();
  protected Map<Integer, TablistColorInterface> tablistColorInterfaces = new HashMap<>();
  protected Map<Integer, TablistSortInterface> tablistSortInterfaces = new HashMap<>();
  protected Map<Integer, TablistPrefixInterface> tablistPrefixInterfaces = new HashMap<>();
  protected Map<Integer, TablistSuffixInterface> tablistSuffixInterfaces = new HashMap<>();
  protected Map<Integer, TablistHeaderInterface> tablistHeaderInterfaces = new HashMap<>();
  protected Map<Integer, TablistFooterInterface> tablistFooterInterfaces = new HashMap<>();

  protected ScoreboardAPI() {
    ScoreboardAPI.instance = this;
  }

  /**
   * Update the Sidebar for the target player
   *
   * @param player the player to update the sidebar
   */
  public abstract void updateSidebar(Player player);

  /**
   * Update the Tablist for the target player
   *
   * @param player the player to update the tablist
   */
  public abstract void updateTablist(Player player);

  /** Update the Sidebar for all players */
  public abstract void updateSidebarForAll();

  /** Update the Tablist for all players */
  public abstract void updateTablistForAll();

  public void registerSidebarInterface(int priority, SidebarInterface sidebarInterface) {
    this.sidebarInterfaces.put(priority, sidebarInterface);
  }

  public void registerTablistInterface(int priority, TablistInterface tablistInterface) {
    this.tablistColorInterfaces.put(priority, tablistInterface);
    this.tablistSortInterfaces.put(priority, tablistInterface);
    this.tablistPrefixInterfaces.put(priority, tablistInterface);
    this.tablistSuffixInterfaces.put(priority, tablistInterface);
  }

  public void registerTablistSortInterface(
      int priority, TablistSortInterface tablistSortInterface) {
    this.tablistSortInterfaces.put(priority, tablistSortInterface);
  }

  public void registerTablistSortInterface(
      int priority, TablistColorInterface tablistColorInterface) {
    this.tablistColorInterfaces.put(priority, tablistColorInterface);
  }

  public void registerTablistPrefixInterface(
      int priority, TablistPrefixInterface tablistPrefixInterface) {
    this.tablistPrefixInterfaces.put(priority, tablistPrefixInterface);
  }

  public void registerTablistSuffixInterface(
      int priority, TablistSuffixInterface tablistSuffixInterface) {
    this.tablistSuffixInterfaces.put(priority, tablistSuffixInterface);
  }

  public void registerTablistHeaderInterface(
      int priority, TablistHeaderInterface tablistHeaderInterface) {
    this.tablistHeaderInterfaces.put(priority, tablistHeaderInterface);
  }

  public void registerTablistFooterInterface(
      int priority, TablistFooterInterface tablistFooterInterface) {
    this.tablistFooterInterfaces.put(priority, tablistFooterInterface);
  }
}
