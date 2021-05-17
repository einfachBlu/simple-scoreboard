package de.blu.scoreboard.api;

import com.google.inject.Inject;
import de.blu.scoreboard.data.ScoreboardTeamData;
import de.blu.scoreboard.util.FastBoard;
import de.blu.scoreboard.util.Tablist;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@Singleton
@Getter
public final class Scoreboard extends ScoreboardAPI {

  private static final AtomicInteger STATIC_ID = new AtomicInteger(0);
  @Inject private JavaPlugin javaPlugin;

  private Collection<ScoreboardTeamData> scoreboardTeamData = new ArrayList<>();
  private Map<Player, FastBoard> fastBoards = new HashMap<>();
  private ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

  @Override
  public void updateSidebar(Player player) {
    // Update Scoreboard Teams
    FastBoard fastBoard = this.getFastBoards().get(player);

    SidebarInterface sidebarInterface = this.getHighestPriority(this.sidebarInterfaces);

    if (sidebarInterface == null) {
      fastBoard.updateTitle("");
      fastBoard.updateLines();
    } else {
      fastBoard.updateTitle(sidebarInterface.getTitle(player));
      fastBoard.updateLines(sidebarInterface.getLines(player));
    }
  }

  private <T> T getHighestPriority(Map<Integer, T> interfaces) {
    int bestPriority = 0;
    T bestInterface = null;

    for (Map.Entry<Integer, T> entry : interfaces.entrySet()) {
      int priority = entry.getKey();
      T currentInterface = entry.getValue();

      if (bestInterface == null) {
        bestPriority = priority;
        bestInterface = currentInterface;
        continue;
      }

      if (priority < bestPriority) {
        continue;
      }

      bestPriority = priority;
      bestInterface = currentInterface;
    }

    return bestInterface;
  }

  @Override
  public void updateTablist(Player player) {
    this.sendHeaderFooter(player);

    // Update Scoreboard Teams
    if (player.getScoreboard() == null) {
      player.setScoreboard(player.getServer().getScoreboardManager().getNewScoreboard());
    }

    for (Player onlinePlayer : player.getServer().getOnlinePlayers()) {
      if (onlinePlayer.getScoreboard() == null) {
        onlinePlayer.setScoreboard(
            onlinePlayer.getServer().getScoreboardManager().getNewScoreboard());
      }

      Scoreboard.this.addTeamEntry(player, onlinePlayer);
      Scoreboard.this.addTeamEntry(onlinePlayer, player);
    }
  }

  @Override
  public void updateSidebarForAll() {
    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
      this.updateSidebar(onlinePlayer);
    }
  }

  @Override
  public void updateTablistForAll() {
    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
      this.updateTablist(onlinePlayer);
    }
  }

  public void sendHeaderFooter(Player player) {
    TablistHeaderInterface tablistHeaderInterface =
        this.getHighestPriority(this.tablistHeaderInterfaces);
    TablistFooterInterface tablistFooterInterface =
        this.getHighestPriority(this.tablistFooterInterfaces);
    String header =
        tablistHeaderInterface == null ? "" : tablistHeaderInterface.getTablistHeader(player);
    String footer =
        tablistFooterInterface == null ? "" : tablistFooterInterface.getTablistFooter(player);

    Tablist.sendTablist(player, header, footer);
  }

  public void loadPlayer(Player player) {
    if (this.getFastBoards().containsKey(player)) {
      return;
    }

    this.getFastBoards().put(player, new FastBoard(player));
  }

  public void unloadPlayer(Player player) {
    if (!this.getFastBoards().containsKey(player)) {
      return;
    }

    FastBoard fastBoard = this.getFastBoards().remove(player);
    fastBoard.delete();
  }

  private void addTeamEntry(Player player, Player target) {
    Team team = this.getTeam(player, target);
    if (team == null) {
      return;
    }

    TablistPrefixInterface tablistPrefixInterface =
        this.getHighestPriority(this.tablistPrefixInterfaces);
    TablistSuffixInterface tablistSuffixInterface =
        this.getHighestPriority(this.tablistSuffixInterfaces);
    TablistColorInterface tablistColorInterface =
        this.getHighestPriority(this.tablistColorInterfaces);

    ChatColor color = ChatColor.WHITE;
    String prefix = "";
    String suffix = "";

    if (tablistPrefixInterface != null) {
      prefix =
          ChatColor.translateAlternateColorCodes(
              '&', tablistPrefixInterface.getTablistPrefix(player, target));
    }

    if (tablistSuffixInterface != null) {
      suffix =
          ChatColor.translateAlternateColorCodes(
              '&', tablistSuffixInterface.getTablistSuffix(player, target));
    }

    if (tablistColorInterface != null) {
      color = tablistColorInterface.getTablistColor(player, target);
    }

    // Because 1.8 doesn't support color directly
    prefix += color;

    if (!team.getColor().equals(color)) {
      // team.setColor(color);
      team.color(NamedTextColor.NAMES.value(color.name()));
    }

    if (!team.prefix().toString().equals(prefix)) {
      // team.setPrefix(prefix);
      team.prefix(Component.text(prefix));
    }

    if (!team.suffix().toString().equals(suffix)) {
      // team.setSuffix(suffix);
      team.suffix(Component.text(suffix));
    }

    if (!team.hasEntry(target.getName())) {
      team.addEntry(target.getName());
    }
  }

  private Team getTeam(Player player, Player target) {
    synchronized (this.getScoreboardTeamData()) {
      for (ScoreboardTeamData scoreboardTeamData : new ArrayList<>(this.getScoreboardTeamData())) {
        if (scoreboardTeamData.getTablistOwner().equals(player)
            && scoreboardTeamData.getTarget().equals(target)) {
          String sort = this.getTablistSort(player, target);
          Team team = player.getScoreboard().getTeam(sort + scoreboardTeamData.getTeamName());

          if (team != null) {
            return team;
          }
        }
      }
    }

    return this.registerTeam(player, target);
  }

  private Team registerTeam(Player player, Player target) {
    ScoreboardTeamData scoreboardTeamData =
        new ScoreboardTeamData(player, target, "_" + STATIC_ID.incrementAndGet());
    this.getScoreboardTeamData().add(scoreboardTeamData);

    String sort = this.getTablistSort(player, target);
    Team team = player.getScoreboard().getTeam(sort + scoreboardTeamData.getTeamName());
    if (team == null) {
      try {
        team = player.getScoreboard().registerNewTeam(sort + scoreboardTeamData.getTeamName());
      } catch (IllegalArgumentException e) {
        // already exist so ignore Exception
        team = player.getScoreboard().getTeam(sort + scoreboardTeamData.getTeamName());
      }
    }

    return team;
  }

  private String getTablistSort(Player player, Player target) {
    String sort = "00000";
    TablistSortInterface tablistSortInterface = this.getHighestPriority(this.tablistSortInterfaces);

    if (tablistSortInterface != null) {
      sort = String.valueOf(tablistSortInterface.getTablistSort(player, target));
      while (sort.length() < 5) {
        sort = "0" + sort;
      }
    }

    return sort;
  }
}
