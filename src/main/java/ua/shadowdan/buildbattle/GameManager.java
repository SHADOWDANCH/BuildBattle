package ua.shadowdan.buildbattle;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitRunnable;
import ua.shadowdan.buildbattle.cuboid.Cuboid;
import ua.shadowdan.buildbattle.event.GameStateChangeEvent;
import ua.shadowdan.buildbattle.listener.TerrainProtectionListener;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by SHADOWDAN on 30.06.2019.
 */
public class GameManager implements Listener {

    @Getter
    private GameState currentState;
    private final BuildBattle buildBattle;
    @Getter
    private final GameArena arena;

    public GameManager(BuildBattle buildBattle) {
        this.buildBattle = buildBattle;
        this.currentState = GameState.WAITING;
        this.arena = new GameArena(buildBattle);
    }

    public void setup() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(this, buildBattle);
        pluginManager.registerEvents(new TerrainProtectionListener(buildBattle), buildBattle);
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        if (getCurrentState() != GameState.WAITING) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Игра уже началась");
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        event.setJoinMessage(player.getDisplayName() + " зашёл в игру, " + Bukkit.getOnlinePlayers().size() + "/" + buildBattle.getPluginConfig().getMaxPlayers());

        if (Bukkit.getOnlinePlayers().size() < buildBattle.getPluginConfig().getMaxPlayers()) {
            return;
        }

        new BukkitRunnable() {
            private int iter = 10;

            @Override
            public void run() {
                if (Bukkit.getOnlinePlayers().size() < buildBattle.getPluginConfig().getMaxPlayers()) {
                    this.cancel();
                    return;
                }

                iter--;
                Bukkit.broadcastMessage("Начало через " + iter);
                if (getCurrentState() != GameState.WAITING) {
                    this.cancel();
                    return;
                }
                if (iter <= 0) {
                    //TODO: возможно здесь нужно создаваь новый объект арены
                    arena.startArena(new ArrayList<>(Bukkit.getOnlinePlayers()), buildBattle.getPluginConfig().getSpawnPoints());
                    this.cancel();
                }
            }
        }.runTaskTimer(buildBattle, 0L, 40L);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        int onlinePlayers = Bukkit.getOnlinePlayers().size() - 1;
        int maxPlayers = buildBattle.getPluginConfig().getMaxPlayers();

        event.setQuitMessage(player.getDisplayName() + " вышёл из игры, " + onlinePlayers + "/" + maxPlayers);

        if (getCurrentState() != GameState.WAITING) {
            if (onlinePlayers <= 0) {
                this.getArena().finalizeGame();
                return;
            }

            getArena().getPlayerPlot().remove(player);
            buildBattle.getVoteManager().getVotes().remove(player);
            buildBattle.getVoteManager().getVotingQueue().remove(player);
        }
    }

    public List<Cuboid> getPlotsOnLocation(Location loc) {
        return buildBattle.getPluginConfig().getPlots().stream()
                .filter(plot -> plot.contains(loc))
                .collect(Collectors.toList());
    }

    public void setCurrentState(GameState currentState) {
        GameStateChangeEvent event = new GameStateChangeEvent(currentState);
        Bukkit.getPluginManager().callEvent(event);

        this.currentState = event.getGameState();
    }
}
