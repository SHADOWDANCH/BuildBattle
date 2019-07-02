package ua.shadowdan.buildbattle;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import ua.shadowdan.buildbattle.util.CollectionUtils;
import ua.shadowdan.buildbattle.util.Pair;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by SHADOWDAN on 01.07.2019.
 */
public class GameArena {

    private final BuildBattle buildBattle;
    @Getter
    private Map<Player, Location> playerPlot;
    @Getter
    private Map<Player, Integer> playerGrade = new HashMap<>();
    @Getter
    private Pair<Player, Location> currentVoting;

    public GameArena(BuildBattle buildBattle) {
        this.buildBattle = buildBattle;
    }

    public void startArena(List<Player> arenaPlayers, List<Location> spawnPoints) {
        buildBattle.getGameManager().setCurrentState(GameState.GAME);

        playerPlot = IntStream.range(0, Math.min(arenaPlayers.size(), spawnPoints.size()))
                .boxed()
                .collect(Collectors.toMap(arenaPlayers::get, spawnPoints::get));

        playerPlot.forEach(Player::teleport);

        Bukkit.broadcastMessage("Игра началась!");

        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.broadcastMessage("Время вышло! Начинаем голосование.");
                processVote();
            }
        }.runTaskLater(buildBattle, buildBattle.getPluginConfig().getGameDuration());
    }

    private void processVote() {
        buildBattle.getGameManager().setCurrentState(GameState.VOTE);
        Queue<Player> queue = new PriorityQueue<>(playerPlot.keySet());

        new BukkitRunnable() {
            @Override
            public void run() {
                if (queue.peek() != null) {
                    Player nextPlayer = queue.poll();
                    currentVoting = new Pair<>(nextPlayer, playerPlot.get(nextPlayer));
                    playerPlot.keySet().forEach(player -> player.teleport(currentVoting.getValue()));
                    Bukkit.broadcastMessage("Постройка игрока " + currentVoting.getKey().getDisplayName());
                } else {
                    Player player = CollectionUtils.getKeyWithHighestValue(playerGrade);
                    Bukkit.broadcastMessage("Выиграл игрок - " + player.getDisplayName() + ". Он набрал " + playerGrade.get(player) + " очков!");
                    this.cancel();
                }
            }
        }.runTaskTimer(buildBattle, 0L, 2400L);
    }

}
