package ua.shadowdan.buildbattle.vote;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by SHADOWDAN on 02.07.2019.
 */
@RequiredArgsConstructor
public class VoteResult implements Comparable<VoteResult> {

    @Getter
    private final Player owner;
    @Getter
    private Map<CommandSender, Integer> grades = new HashMap<>();
    @Getter
    private int finalGrade = 0;

    @Override
    public int compareTo(VoteResult o) {
        return Integer.compare(calculateFinalGrade(), o.calculateFinalGrade());
    }

    private int calculateFinalGrade() {
        return finalGrade = getGrades().values().stream().mapToInt(i -> i).sum();
    }
}
