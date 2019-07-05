package ua.shadowdan.buildbattle.scoreboard;

import dk.xakeps.view.api.ViewAPI;
import dk.xakeps.view.api.sidebar.Sidebar;
import dk.xakeps.view.api.sidebar.SidebarManager;
import dk.xakeps.view.api.text.StaticText;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import ua.shadowdan.buildbattle.BuildBattle;
import ua.shadowdan.buildbattle.GameState;
import ua.shadowdan.buildbattle.event.GameStateChangeEvent;
import ua.shadowdan.buildbattle.scoreboard.text.Texts;

/**
 * Created by SHADOWDAN on 03.07.2019.
 */
public class ScoreboardManager implements Listener {

    private final SidebarManager sidebarManager;
    private final BuildBattle buildBattle;
    @Getter
    private final Texts texts;

    public ScoreboardManager(BuildBattle buildBattle) {
        this.buildBattle = buildBattle;
        this.sidebarManager = ViewAPI.getSidebarManager();
        this.texts = new Texts(buildBattle);
    }

    public void setup() {
        Bukkit.getPluginManager().registerEvents(this, buildBattle);
    }

    private void apply(Player player, GameState state) {
        sidebarManager.removeSidebar(player);
        Sidebar sidebar = sidebarManager.createSidebar(player);

        sidebar.setTitle(new StaticText("§6§lBuildBattle"));

        texts.BASIC_GROUP.forEach(sidebar::addLine);

        switch (state) {
            case WAITING: {
                break;
            }
            case GAME: {
                texts.GAME_GROUP.forEach(sidebar::addLine);
                break;
            }
            case VOTE: {
                texts.VOTE_GROUP.forEach(sidebar::addLine);
                break;
            }
            case END: {
                break;
            }
            default: {
                sidebar.addLine(new StaticText("§c§kaa§r§cError§c§kaa"));
                break;
            }
        }

        sidebar.addLine(Texts.EMPTY);
        sidebar.addLine(new StaticText("§7www.craftchan.net"));
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
