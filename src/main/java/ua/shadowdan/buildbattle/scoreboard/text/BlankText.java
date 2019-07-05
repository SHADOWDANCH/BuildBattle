package ua.shadowdan.buildbattle.scoreboard.text;

import dk.xakeps.view.api.sidebar.Text;

/**
 * Created by SHADOWDAN on 05.07.2019.
 */
public class BlankText implements Text {
    @Override
    public String getText() {
        return "";
    }

    @Override
    public void setText(String s) {
        throw new UnsupportedOperationException("BlankText not support text change!");
    }
}
