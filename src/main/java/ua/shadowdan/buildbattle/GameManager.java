package ua.shadowdan.buildbattle;

import lombok.Getter;
import lombok.Setter;
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
import ua.shadowdan.buildbattle.listener.TerrainProtectionListener;
import ua.shadowdan.buildbattle.vote.VoteManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by SHADOWDAN on 30.06.2019.
 */
public class GameManager implements Listener {

    @Getter @Setter
    private GameState currentState;
    private final BuildBattle buildBattle;
    @Getter
    private final GameArena arena;
    @Getter
    private final VoteManager voteManager;

    public GameManager(BuildBattle buildBattle) {
        this.buildBattle = buildBattle;
        this.currentState = GameState.WAITING;
        this.arena = new GameArena(buildBattle);
        this.voteManager = new VoteManager(buildBattle);
    }

    public void setup() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(this, buildBattle);
        pluginManager.registerEvents(new TerrainProtectionListener(buildBattle), buildBattle);
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        if (currentState != GameState.WAITING) {
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
    }

    public List<Cuboid> getPlotsOnLocation(Location loc) {
        return buildBattle.getPluginConfig().getPlots().stream()
                .filter(plot -> plot.contains(loc))
                .collect(Collectors.toList());
    }
}
