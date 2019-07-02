package ua.shadowdan.buildbattle.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import ua.shadowdan.buildbattle.BuildBattle;
import ua.shadowdan.buildbattle.GameState;
import ua.shadowdan.buildbattle.vote.VoteManager;
import ua.shadowdan.buildbattle.vote.VoteResult;

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
        VoteManager voteManager = buildBattle.getGameManager().getVoteManager();
        Player currentPlayer = voteManager.getCurrentPlayer();

        if (currentPlayer == sender) {
            sender.sendMessage("Вы не можете голосовать сами за себя");
            return true;
        }
        if (args.length < 1) {
            sender.sendMessage("Используйте: /vote <1..5>");
            return true;
        }

        int grade;
        try {
            grade = Integer.parseInt(args[0]);
        } catch (NumberFormatException ex) {
            sender.sendMessage("Вы ввели не цифру");
            return true;
        }

        if (grade <= 0 || grade > 5) {
            sender.sendMessage("Вы можете поставить оценку только от 1 до 5");
            return true;
        }

        voteManager.getVotes().putIfAbsent(currentPlayer, new VoteResult(currentPlayer));
        VoteResult voteResult = voteManager.getVotes().get(currentPlayer);
        if (voteResult.getGrades().containsKey(sender)) {
            sender.sendMessage("Вы уже поставили оценку!");
            return true;
        }

        voteResult.getGrades().put(sender, grade);
        sender.sendMessage("Вы потсавил оценку " + grade + " игроку " + currentPlayer.getDisplayName());
        return true;
    }
}
