package ua.shadowdan.buildbattle;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by SHADOWDAN on 01.07.2019.
 */
public class GameArena {

    private final BuildBattle buildBattle;
    @Getter
    private Map<Player, Location> playerPlot;

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
                if (buildBattle.getGameManager().getCurrentState() != GameState.GAME) {
                    this.cancel();
                }
                Bukkit.broadcastMessage("Время вышло! Начинаем голосование.");
                buildBattle.getGameManager().getVoteManager().startVotesStage(playerPlot);
            }
        }.runTaskLater(buildBattle, buildBattle.getPluginConfig().getGameDuration());
    }

    public void finalizeGame() {
        buildBattle.getGameManager().setCurrentState(GameState.END);

        Bukkit.getOnlinePlayers().forEach(player -> player.kickPlayer("Игра окончена"));

        //TODO: возможно очистка мира, выгрузка/загрузка
    }
}
