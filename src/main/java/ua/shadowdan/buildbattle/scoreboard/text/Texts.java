package ua.shadowdan.buildbattle.scoreboard.text;

import dk.xakeps.view.api.sidebar.Text;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ua.shadowdan.buildbattle.BuildBattle;
import ua.shadowdan.buildbattle.util.CommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SHADOWDAN on 03.07.2019.
 */
public class Texts {

    public final static Text EMPTY = new BlankText();
    public final Text GAMESTATE = new DynamicText() {
        @Override
        public String getText() {
            return "§fСтадия: §e" + buildBattle.getGameManager().getCurrentState().getDescription();
        }
    };
    public final Text ONLINE = new DynamicText() {
        @Override
        public String getText() {
            return String.format("§fИгроки: §e%d/%d", Bukkit.getOnlinePlayers().size(),
                    buildBattle.getPluginConfig().getMaxPlayers());
        }
    };
    public final Text THEME = new DynamicText() {
        @Override
        public String getText() {
            return "§fТема: §e" + buildBattle.getGameManager().getArena().getTheme();
        }
    };
    public final Text TIME_TO_END = new DynamicText() {
        @Override
        public String getText() {
            return "§fОсталось: §e" + CommonUtils.ticksToTime(buildBattle.getGameManager().getArena().getTicksToEnd());
        }
    };
    // TODO: реализовать все что ниже
    public final Text VOTING_FOR = new DynamicText() {
        @Override
        public String getText() {
            return "§fГолосвани: §e" + buildBattle.getVoteManager().getCurrentPlayer().getDisplayName();
        }
    };
    public final Text YOU_GRADE = new DynamicText() {
        @Override
        public String getText() {
            return "§fВаша оценка: §e" + 0;
        }
    };
    public final Text NEXT_VOTE = new DynamicText() {
        @Override
        public String getText() {
            Player player = buildBattle.getVoteManager().getVotingQueue().peek();
            return "§fСледующий: §e" + (player != null ? player.getDisplayName() : "нету");
        }
    };

    public final List<Text> BASIC_GROUP = new ArrayList<Text>() {{
        add(EMPTY);
        add(GAMESTATE);
        add(ONLINE);
    }};
    public final List<Text> GAME_GROUP = new ArrayList<Text>() {{
        add(EMPTY);
        add(TIME_TO_END);
        add(THEME);
    }};
    public final List<Text> VOTE_GROUP = new ArrayList<Text>() {{
       addAll(GAME_GROUP);
       add(EMPTY);
       add(VOTING_FOR);
       //add(YOU_GRADE);
       add(NEXT_VOTE);
    }};

    private final BuildBattle buildBattle;

    public Texts(BuildBattle buildBattle) {
        this.buildBattle = buildBattle;
    }

}
