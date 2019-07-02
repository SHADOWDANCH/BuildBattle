package ua.shadowdan.buildbattle.listener;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import ua.shadowdan.buildbattle.BuildBattle;
import ua.shadowdan.buildbattle.GameManager;
import ua.shadowdan.buildbattle.GameState;

/**
 * Created by SHADOWDAN on 01.07.2019.
 */
public class TerrainProtectionListener implements Listener {

    private final BuildBattle buildBattle;

    public TerrainProtectionListener(BuildBattle buildBattle) {
        this.buildBattle = buildBattle;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        this.disallowInteract(event.getBlock(), event.getPlayer(), event);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        this.disallowInteract(event.getBlock(), event.getPlayer(), event);
    }

    private boolean disallowInteract(Block block, Player player, Cancellable event) {
        GameManager manager = buildBattle.getGameManager();
        if ((manager.getCurrentState() != GameState.GAME) || (manager.getPlotsOnLocation(block.getLocation()).size() <= 0)) {
            player.sendMessage("Вы не можете здесь строить!");
            event.setCancelled(true);
            return true;
        }
        return false;
    }
}
