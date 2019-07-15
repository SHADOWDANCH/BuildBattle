package ua.shadowdan.buildbattle.scoreboard.text;

import dk.xakeps.view.api.sidebar.Text;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ua.shadowdan.buildbattle.BuildBattle;
import ua.shadowdan.buildbattle.util.CommonUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Created by SHADOWDAN on 15.07.2019.
 */
public class DynamicText implements Text {

    private String text;
    private final Set<ReplaceFunc> replaceFuncs = new HashSet<ReplaceFunc>() {{
        add(new TestReplace("{gamestate}", () -> BuildBattle.getInstance().getGameManager().getCurrentState().getDescription()));
        add(new TestReplace("{online-players}", () -> Integer.toString(Bukkit.getOnlinePlayers().size())));
        add(new TestReplace("{max-players}", () -> Integer.toString(BuildBattle.getInstance().getPluginConfig().getMaxPlayers())));
        add(new TestReplace("{time-to-end}", () -> CommonUtils.ticksToTime(BuildBattle.getInstance().getGameManager().getArena().getTicksToEnd())));
        add(new TestReplace("{theme}", () -> BuildBattle.getInstance().getGameManager().getArena().getTheme()));
        add(new TestReplace("{voting-for}", () -> BuildBattle.getInstance().getVoteManager().getCurrentPlayer().getDisplayName()));
        add(new TestReplace("{next-vote}", () -> {
            Player player = BuildBattle.getInstance().getVoteManager().getVotingQueue().peek();
            return player != null ? player.getDisplayName() : "нету";
        }));
    }};

    public DynamicText(String text) {
        this.text = text;
    }

    @Override
    public String getText() {
        String str = text;
        for (ReplaceFunc func : replaceFuncs) {
            if (str.contains(func.getTag())) {
                str = func.apply(str);
            }
        }
        return str;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    public interface ReplaceFunc {
        String apply(String input);

        String getTag();
    }

    public static class TestReplace implements ReplaceFunc {

        @Getter
        private final String tag;
        private final Supplier<String> replace;

        public TestReplace(String tag, Supplier<String> replace) {
            this.tag = tag;
            this.replace = replace;
        }

        @Override
        public String apply(String input) {
            return input.replace(tag, replace.get());
        }
    }
}
