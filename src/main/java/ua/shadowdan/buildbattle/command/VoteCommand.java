package ua.shadowdan.buildbattle.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import ua.shadowdan.buildbattle.BuildBattle;
import ua.shadowdan.buildbattle.GameState;

import java.util.Map;
import java.util.logging.Level;

/**
 * Created by SHADOWDAN on 02.07.2019.
 */
public class VoteCommand implements CommandExecutor {
    private final BuildBattle buildBattle;

    public VoteCommand(BuildBattle buildBattle) {
        this.buildBattle = buildBattle;
    }

    public void setup() {
        PluginCommand command = buildBattle.getCommand("vote");
        if (command != null) {
            command.setExecutor(this);
        } else {
            buildBattle.getLogger().log(Level.WARNING,"Command \"vote\" not registered. Executor will not be initialized.");
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (buildBattle.getGameManager().getCurrentState() != GameState.VOTE) {
            sender.sendMessage("Голосование ещё не началось!");
            return true;
        }
        Player currentPlayer = buildBattle.getGameManager().getVoteManager().getCurrentPlayer();
        if (currentPlayer == sender) {
            sender.sendMessage("Вы не можете голосовать сами за себя");
            return true;
        }
        if (args.length < 1) {
            sender.sendMessage("Используйте: /vote <1..5>");
            return true;
        }
        Map<Player, Integer> grades = buildBattle.getGameManager().getVoteManager().getGrades();
        int grade;
        try {
            grade = grades.getOrDefault(currentPlayer, 0) + Integer.parseInt(args[0]);
        } catch (NumberFormatException ex) {
            sender.sendMessage("Вы ввели не цифру");
            return true;
        }
        if (grade > 5) {
            sender.sendMessage("Вы не сожете ставить оценку больше 5");
            return true;
        }
        grades.put(currentPlayer, grade);
        sender.sendMessage("Вы потсавил оценку " + grade + " игроку " + currentPlayer.getDisplayName());
        return true;
    }
}
