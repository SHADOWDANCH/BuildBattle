package ua.shadowdan.buildbattle.listener;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import ua.shadowdan.buildbattle.BuildBattle;

/**
 * Created by SHADOWDAN on 01.07.2019.
 */
public class BaseGameListener implements Listener {

    private final BuildBattle buildBattle;

    public BaseGameListener(BuildBattle buildBattle) {
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

    private void disallowInteract(Block block, Player player, Cancellable event) {
        if (buildBattle.getGameManager().getPlotsOnLocation(block.getLocation()).size() <= 0) {
            player.sendMessage("Вы не можете здесь строить!");
            event.setCancelled(true);
        }
    }
}
