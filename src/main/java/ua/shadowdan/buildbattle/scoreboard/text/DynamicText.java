package ua.shadowdan.buildbattle.scoreboard.text;

import dk.xakeps.view.api.sidebar.Text;
import lombok.Getter;

import java.util.Set;
import java.util.function.Supplier;

/**
 * Created by SHADOWDAN on 15.07.2019.
 */
public class DynamicText implements Text {

    private String text;
    private final Set<ReplaceFunc> replaceFuncs;

    public DynamicText(String text, Set<ReplaceFunc> replaceFuncs) {
        this.text = text;
        this.replaceFuncs = replaceFuncs;
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

    public static class SupplierReplaceFunc implements ReplaceFunc {

        @Getter
        private final String tag;
        private final Supplier<String> replace;

        public SupplierReplaceFunc(String tag, Supplier<String> replace) {
            this.tag = tag;
            this.replace = replace;
        }

        @Override
        public String apply(String input) {
            return input.replace(tag, replace.get());
        }
    }
}
