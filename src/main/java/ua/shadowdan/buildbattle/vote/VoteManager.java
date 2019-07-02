package ua.shadowdan.buildbattle.vote;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import ua.shadowdan.buildbattle.BasicComparator;
import ua.shadowdan.buildbattle.BuildBattle;
import ua.shadowdan.buildbattle.GameState;
import ua.shadowdan.buildbattle.util.CollectionUtils;

import java.util.*;

/**
 * Created by SHADOWDAN on 02.07.2019.
 */
public class VoteManager {

    private final BuildBattle buildBattle;
    @Getter
    private Map<Player, VoteResult> votes = new HashMap<>();
    @Getter
    private Queue<Player> votingQueue = new PriorityQueue<>(new BasicComparator<>());
    @Getter
    private Player currentPlayer;

    public VoteManager(BuildBattle buildBattle) {
        this.buildBattle = buildBattle;
    }

    public void startVotesStage(Map<Player, Location> playerPlot) {
        buildBattle.getGameManager().setCurrentState(GameState.VOTE);
        votingQueue.addAll(playerPlot.keySet());

        new BukkitRunnable() {
            @Override
            public void run() {
                if (votingQueue.peek() != null) {
                    currentPlayer = votingQueue.poll();
                    playerPlot.keySet().forEach(player -> player.teleport(playerPlot.get(currentPlayer)));
                    Bukkit.broadcastMessage("Постройка игрока " + currentPlayer.getDisplayName());
                } else {
                    Player player = CollectionUtils.getKeyWithHighestValue(votes);
                    Bukkit.broadcastMessage("Выиграл игрок - " + player.getDisplayName() + ". Он набрал " + votes.get(player).getFinalGrade() + " очков!");
                    this.cancel();
                }
            }
        }.runTaskTimer(buildBattle, 0L, 2400L);
    }
}
