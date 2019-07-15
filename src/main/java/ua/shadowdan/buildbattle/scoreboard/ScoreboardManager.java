package ua.shadowdan.buildbattle.scoreboard;

import dk.xakeps.view.api.ViewAPI;
import dk.xakeps.view.api.sidebar.Sidebar;
import dk.xakeps.view.api.sidebar.SidebarManager;
import dk.xakeps.view.api.text.StaticText;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import ua.shadowdan.buildbattle.BuildBattle;
import ua.shadowdan.buildbattle.GameState;
import ua.shadowdan.buildbattle.event.GameStateChangeEvent;
import ua.shadowdan.buildbattle.scoreboard.text.DynamicText;

/**
 * Created by SHADOWDAN on 03.07.2019.
 */
public class ScoreboardManager implements Listener {

    private final SidebarManager sidebarManager;
    private final BuildBattle buildBattle;

    public ScoreboardManager(BuildBattle buildBattle) {
        this.buildBattle = buildBattle;
        this.sidebarManager = ViewAPI.getSidebarManager();
    }

    public void setup() {
        Bukkit.getPluginManager().registerEvents(this, buildBattle);
    }

    private void apply(Player player, GameState state) {
        sidebarManager.removeSidebar(player);
        Sidebar sidebar = sidebarManager.createSidebar(player);

        sidebar.setTitle(new StaticText("§6§lBuildBattle"));

        String[] template = buildBattle.getPluginConfig().getScoreboard().getOrDefault(state, new String[] {"§c§lERROR!"});

        for (String s : template) {
            sidebar.addLine(new DynamicText(s));
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        apply(event.getPlayer(), buildBattle.getGameManager().getCurrentState());
    }

    @EventHandler
    public void onGameStateChange(GameStateChangeEvent event) {
        Bukkit.getOnlinePlayers().forEach(player -> apply(player, event.getGameState()));
    }
}
