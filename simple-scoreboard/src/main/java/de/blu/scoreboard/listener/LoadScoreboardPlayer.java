package de.blu.scoreboard.listener;

import de.blu.scoreboard.api.Scoreboard;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class LoadScoreboardPlayer implements Listener {

  @Inject private Scoreboard scoreboard;

  @EventHandler
  public void onJoin(PlayerJoinEvent e) {
    Player player = e.getPlayer();

    this.scoreboard.loadPlayer(player);
    this.scoreboard.updateSidebar(player);
    this.scoreboard.updateTablistForAll();
  }

  @EventHandler
  public void onQuit(PlayerQuitEvent e) {
    Player player = e.getPlayer();

    this.scoreboard.unloadPlayer(player);
  }
}
