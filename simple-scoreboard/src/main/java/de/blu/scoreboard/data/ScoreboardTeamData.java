package de.blu.scoreboard.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

@Setter
@Getter
@AllArgsConstructor
public class ScoreboardTeamData {
  /** The Owner of the Scoreboard */
  private Player tablistOwner;

  /** The Target Player to add */
  private Player target;

  /** The TeamName for this data */
  private String teamName;
}
