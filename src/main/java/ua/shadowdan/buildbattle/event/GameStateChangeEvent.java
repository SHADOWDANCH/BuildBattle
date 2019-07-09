package ua.shadowdan.buildbattle.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import ua.shadowdan.buildbattle.GameState;

import javax.annotation.Nonnull;

/**
 * Created by SHADOWDAN on 03.07.2019.
 */
@AllArgsConstructor
public class GameStateChangeEvent extends Event {

    @Getter
    private static final HandlerList handlerList = new HandlerList();
    @Getter @Setter
    private GameState gameState;

    @Nonnull
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}
