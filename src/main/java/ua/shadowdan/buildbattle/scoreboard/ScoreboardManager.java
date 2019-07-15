package ua.shadowdan.buildbattle.scoreboard;

import com.google.common.collect.Sets;
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
import ua.shadowdan.buildbattle.util.CommonUtils;

import java.util.Set;

/**
 * Created by SHADOWDAN on 03.07.2019.
 */
public class ScoreboardManager implements Listener {

    private final SidebarManager sidebarManager;
    private final BuildBattle buildBattle;
    private final Set<DynamicText.ReplaceFunc> replaceFuncs;

    public ScoreboardManager(BuildBattle buildBattle) {
        this.buildBattle = buildBattle;
        this.sidebarManager = ViewAPI.getSidebarManager();

        replaceFuncs = Sets.newHashSet(
                new DynamicText.SupplierReplaceFunc("{gamestate}", () -> buildBattle.getGameManager().getCurrentState().getDescription()),
                new DynamicText.SupplierReplaceFunc("{online-players}", () -> Integer.toString(Bukkit.getOnlinePlayers().size())),
                new DynamicText.SupplierReplaceFunc("{max-players}", () -> Integer.toString(buildBattle.getPluginConfig().getMaxPlayers())),
                new DynamicText.SupplierReplaceFunc("{time-to-end}", () -> CommonUtils.ticksToTime(buildBattle.getGameManager().getArena().getTicksToEnd())),
                new DynamicText.SupplierReplaceFunc("{theme}", () -> buildBattle.getGameManager().getArena().getTheme()),
                new DynamicText.SupplierReplaceFunc("{voting-for}", () -> buildBattle.getVoteManager().getCurrentPlayer().getDisplayName()),
                new DynamicText.SupplierReplaceFunc("{next-vote}", () -> {
                    Player player = buildBattle.getVoteManager().getVotingQueue().peek();
                    return player != null ? player.getDisplayName() : "нету";
                })
        );
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
            sidebar.addLine(new DynamicText(s, replaceFuncs));
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
