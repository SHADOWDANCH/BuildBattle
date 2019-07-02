package ua.shadowdan.buildbattle.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.spigotmc.SpigotConfig;
import ua.shadowdan.buildbattle.BuildBattle;
import ua.shadowdan.buildbattle.GameManager;
import ua.shadowdan.buildbattle.GameState;

import java.util.ArrayList;
import java.util.logging.Level;

/**
 * Created by SHADOWDAN on 01.07.2019.
 */
public class BuildBattleCommand implements CommandExecutor {

    private final BuildBattle buildBattle;

    public BuildBattleCommand(BuildBattle buildBattle) {
        this.buildBattle = buildBattle;
    }

    public void setup() {
        PluginCommand command = buildBattle.getCommand("buildbattle");
        if (command != null) {
            command.setExecutor(this);
        } else {
            buildBattle.getLogger().log(Level.WARNING,"Command \"buildbattle\" not registered. Executor will not be initialized.");
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        GameManager gameManager = buildBattle.getGameManager();
        if (!sender.hasPermission("buildbattle.admin")) {
            sender.sendMessage(SpigotConfig.unknownCommandMessage);
            return true;
        }
        if (args.length < 1) {
            sender.sendMessage(new String[] {
                    "/bb state - отладочная информация",
                    "/bb forcestart - принудительный запуск игры"
            });
            return true;
        }
        if (args[0].equalsIgnoreCase("save")) {
            if (args.length < 2) {
                sender.sendMessage("Используйте /bb save true/false");
                return true;
            }
            boolean save = Boolean.getBoolean(args[1]);
            buildBattle.getPluginConfig().getPlots().forEach(plot -> plot.getWorld().setAutoSave(save));
            sender.sendMessage("Сохранение мира: " + save);
            return true;
        }
        if (args[0].equalsIgnoreCase("state")) {
            sender.sendMessage("Current game state: " + gameManager.getCurrentState().name());
            return true;
        }
        if (args[0].equalsIgnoreCase("forcestart")) {
            if (gameManager.getCurrentState() == GameState.WAITING) {
                gameManager.getArena().startArena(new ArrayList<>(Bukkit.getOnlinePlayers()), buildBattle.getPluginConfig().getSpawnPoints());
            } else {
                sender.sendMessage("Похоже игра уже началась...");
            }
            return true;
        }
        return false;
    }
}
