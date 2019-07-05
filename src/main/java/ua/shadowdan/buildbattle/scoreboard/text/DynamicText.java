package ua.shadowdan.buildbattle.scoreboard.text;

import dk.xakeps.view.api.sidebar.Text;

/**
 * Created by SHADOWDAN on 03.07.2019.
 */
public abstract class DynamicText implements Text {
    
    @Override
    public void setText(String s) {
        throw new UnsupportedOperationException("DynamicText not support text change!");
    }
}
