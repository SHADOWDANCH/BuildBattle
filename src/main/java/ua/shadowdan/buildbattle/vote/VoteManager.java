package ua.shadowdan.buildbattle.vote;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import ua.shadowdan.buildbattle.BasicComparator;
import ua.shadowdan.buildbattle.BuildBattle;
import ua.shadowdan.buildbattle.GameManager;
import ua.shadowdan.buildbattle.GameState;
import ua.shadowdan.buildbattle.util.CollectionUtils;
import ua.shadowdan.buildbattle.util.CommonUtils;

import java.util.*;

/**
 * Created by SHADOWDAN on 02.07.2019.
 */
public class VoteManager {

    private final BuildBattle buildBattle;
    @Getter
    private Map<Player, VoteResult> votes;
    @Getter
    private Queue<Player> votingQueue;
    @Getter
    private Player currentPlayer;

    public VoteManager(BuildBattle buildBattle) {
        this.buildBattle = buildBattle;
    }

    public void startVotesStage(Map<Player, Location> playerPlot) {
        votes = new HashMap<>();
        votingQueue = new PriorityQueue<>(new BasicComparator<>());
        votingQueue.addAll(playerPlot.keySet());

        GameManager manager = buildBattle.getGameManager();
        manager.setCurrentState(GameState.VOTE);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (manager.getCurrentState() != GameState.VOTE) {
                    this.cancel();
                }
                if (votingQueue.peek() != null) {
                    currentPlayer = votingQueue.poll();
                    playerPlot.keySet().forEach(player -> player.teleport(playerPlot.get(currentPlayer)));
                    Bukkit.broadcastMessage("Постройка игрока " + currentPlayer.getDisplayName());
                } else {
                    Optional<Player> optionalPlayer;
                    if (votes.size() > 0) {
                        optionalPlayer = Optional.ofNullable(CollectionUtils.getKeyWithHighestValue(votes));
                    } else {
                        optionalPlayer = playerPlot.keySet().stream().findAny();
                    }
                    optionalPlayer.ifPresent(player -> {
                        Bukkit.broadcastMessage("Выиграл игрок - " + player.getDisplayName() + ". Он набрал " + votes.getOrDefault(player, VoteResult.EMPTY).getFinalGrade() + " очков!");
                        CommonUtils.countdown(buildBattle, 15, () -> manager.getArena().finalizeGame());
                    });
                    this.cancel();
                }
            }
        }.runTaskTimer(buildBattle, 0L, 2400L);
    }

    public int getPlayerGrade(Player player) {
        VoteResult result = votes.get(currentPlayer);
        if (result != null) {
            return votes.get(currentPlayer).getGrades().getOrDefault(player, 0);
        }
        return 0;
    }
}
