package ua.shadowdan.buildbattle.util;

import com.google.common.collect.Lists;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

/**
 * Created by SHADOWDAN on 02.07.2019.
 */
public class CommonUtils {

    public static void countdown(Plugin plugin, int times, Runnable runnable) {
        new BukkitRunnable() {
            private int iter = times;

            @Override
            public void run() {
                iter--;

                if (iter <= 0) {
                    runnable.run();
                }
            }
        }.runTaskTimer(plugin, 0L, 40L);
    }

    public static List<String> getListOfStringsMatchingLastWord(String[] inputArgs, String... possibilities) {
        List<String> list = Lists.newArrayList();

        String arg = inputArgs[inputArgs.length - 1];
        for (String str : possibilities) {
            if (doesStringStartWith(arg, str)) {
                list.add(str);
            }
        }

        return list;
    }

    public static boolean doesStringStartWith(String original, String region) {
        return region.regionMatches(true, 0, original, 0, original.length());
    }
}
